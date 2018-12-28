package com.thinkerwolf.hantis.executor;

import com.thinkerwolf.hantis.cache.Cache;
import com.thinkerwolf.hantis.cache.CacheKey;
import com.thinkerwolf.hantis.cache.SimpleCache;
import com.thinkerwolf.hantis.common.DefaultNameHandler;
import com.thinkerwolf.hantis.common.NameHandler;
import com.thinkerwolf.hantis.common.Param;
import com.thinkerwolf.hantis.common.log.InternalLoggerFactory;
import com.thinkerwolf.hantis.common.log.Logger;
import com.thinkerwolf.hantis.common.util.PropertyUtils;
import com.thinkerwolf.hantis.session.Configuration;
import com.thinkerwolf.hantis.session.SessionFactoryBuilder;
import com.thinkerwolf.hantis.sql.Sql;
import com.thinkerwolf.hantis.transaction.ConnectionHolder;
import com.thinkerwolf.hantis.transaction.ConnectionUtils;
import com.thinkerwolf.hantis.type.JDBCType;
import com.thinkerwolf.hantis.type.TypeHandler;

import javax.sql.CommonDataSource;
import javax.sql.DataSource;
import javax.sql.XADataSource;


import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractExecutor implements Executor {

	protected final Logger logger = InternalLoggerFactory.getLogger(getClass());

	private CommonDataSource dataSource;

	private NameHandler nameHandler = new DefaultNameHandler();

	private Configuration configuration;

	/** First level cache */
	private Cache cache = new SimpleCache();

	private SessionFactoryBuilder sessionFactoryBuilder;

	@Override
	public void setDataSource(CommonDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	@Override
	public void setSessionFactoryBuilder(SessionFactoryBuilder sessionFactoryBuilder) {
		this.sessionFactoryBuilder = sessionFactoryBuilder;
		this.configuration = sessionFactoryBuilder.getConfiguration();
	}

	@Override
	public <T> List<T> queryForList(Sql sql, Class<T> clazz) {
		return queryForList(sql, new ResultSetListHandler<>(new ClassRowHander<>(clazz, nameHandler)));
	}

	@Override
	public <T> T queryForOne(Sql sql, Class<T> clazz) {
		List<T> l = queryForList(sql, clazz);
		if (l.size() == 0) {
			return null;
		}
		if (l.size() > 1) {
			throw new RuntimeException("");
		}
		return l.get(0);
	}

	@Override
	public List<Map<String, Object>> queryForList(Sql sql) {
		return queryForList(sql, new ResultSetListHandler<>(new MapRowHandler()));
	}

	@Override
	public Map<String, Object> queryForOne(Sql sql) {
		List<Map<String, Object>> l = queryForList(sql);
		if (l.size() == 0) {
			return null;
		}
		if (l.size() > 1) {
			throw new ExecutorException("The result size > 1");
		}
		return l.get(0);
	}

	@Override
	public <T> List<T> queryForList(Sql sql, ResultSetListHandler<T> listHandler) {
		Connection connection = getConnection();
		flushStatments(false);
		CacheKey cacheKey = createCacheKey(sql, listHandler);

		// Query from first level cache
		List<T> result = (List <T>) cache.getObject(cacheKey);
		if (result != null && logger.isDebugEnabled()) {
			logger.debug("Use first level cache:" + sql);
		}

		// Query from second leval cache
		if (result == null && sql.getCache() != null) {
			result = (List <T>) sql.getCache().getObject(cacheKey);
			if (result != null && logger.isDebugEnabled()) {
				logger.debug("Use second level cache:" + sql);
			}
		}

		if (result == null) {
			// Query from database
			if (logger.isDebugEnabled()) {
				logger.debug("Query no cache: " + sql);
			}
			PreparedStatementBuilder builder = new PreparedStatementBuilderImpl(connection, sql.getSql(), sql.getParams());
			QueryStatementExecuteCallback<List<T>> callback = new QueryStatementExecuteCallback<>(builder, listHandler);
			result = execute(callback);

			// Save to cache
			cache.removeObject(cacheKey);
			cache.putObject(cacheKey, result);
			if (sql.getCache() != null) {
				sql.getCache().removeObject(cacheKey);
				sql.getCache().putObject(cacheKey, result);
			}
		}
		return result;
	}


	private <T> CacheKey createCacheKey(Sql sql, ResultSetListHandler<T> listHandler) {
		CacheKey key = new CacheKey();
		key.append(sql.getSql()).append(sql.getParams()).append(listHandler.getClass());
		return key;
	}


	@Override
	public List<BatchResult> flushStatments(boolean isRollback) {
		if (logger.isDebugEnabled()) {
			logger.debug("Flush statments");
		}
		List<BatchResult> batchResults = doFlushStatments(isRollback);
		if (batchResults != null && batchResults.size() > 0) {
			clearAllCache();
		}
		return batchResults;
	}

	protected abstract List<BatchResult> doFlushStatments(boolean isRollback);


	@Override
	public <T> T execute(StatementExecuteCallback<T> callback) {
		return callback.execute();
	}

	@Override
	public int update(Sql sql) {
		Connection connection = getConnection();
		int num = doUpdate(sql, connection);
		clearAllCache();
		return num;
	}

	protected abstract int doUpdate(Sql sql, Connection connection);

	private void clearAllCache() {
		cache.clear();
		sessionFactoryBuilder.clearCache();
	}

	@Override
	public boolean execute(Sql sql) {
		Connection connection = getConnection();
		flushStatments(false);
		return execute(new CommonStatmentExecuteCallback(new StatementBuilderImpl(connection), sql.getSql()));
	}

	protected Connection getConnection() {
		ConnectionHolder holder = ConnectionUtils.getConnectionHolder(dataSource);
		if (holder != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("The ConnectionHolder exists");
			}
			return holder.getConnection();
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("The ConnectionHolder not exists, generate new ConnectionHolder!");
			}
			try {
				Connection conn;
				if (dataSource instanceof DataSource) {
					DataSource ds = (DataSource) dataSource;
					conn = ds.getConnection();
				} else {
					XADataSource xds = (XADataSource) dataSource;
					conn = xds.getXAConnection().getConnection();
				}
				conn.setAutoCommit(false);
				ConnectionUtils.bindConnection(dataSource, conn, configuration.getTransactionManager());
				return conn;
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void close() {
		cache.clear();
		doClose();
	}

	protected void doClose() {
	}

	private static class ClassRowHander<T> implements RowHandler<T> {
		private Class<T> clazz;
		private NameHandler nameHandler;

		public ClassRowHander(Class<T> clazz, NameHandler nameHandler) {
			this.clazz = clazz;
			this.nameHandler = nameHandler;
		}

		@Override
		public T processRow(ResultSet rs) throws Exception {
			T t = clazz.newInstance();
			ResultSetMetaData meta = rs.getMetaData();
			int count = meta.getColumnCount();
			for (int i = 0; i < count; i++) {
				Object obj = rs.getObject(i + 1);
				String columnName = meta.getColumnName(i + 1);
				PropertyUtils.setProperty(t, nameHandler.convertToPropertyName(columnName), obj);
			}
			return t;
		}
	}

	private static class MapRowHandler implements RowHandler<Map<String, Object>> {
		@Override
		public Map<String, Object> processRow(ResultSet rs) throws Exception {
			Map<String, Object> map = new HashMap<>();
			ResultSetMetaData meta = rs.getMetaData();
			int count = meta.getColumnCount();
			for (int i = 0; i < count; i++) {
				Object obj = rs.getObject(i + 1);
				String columnName = meta.getColumnName(i + 1);
				map.put(columnName, obj);
			}
			return map;
		}
	}

	protected class StatementBuilderImpl implements StatementBuilder {
		private Connection connection;

		public StatementBuilderImpl(Connection connection) {
			this.connection = connection;
		}

		@Override
		public Statement build() throws Exception {
			Statement s = connection.createStatement();
			return s;
		}
	}

	protected class PreparedStatementBuilderImpl implements PreparedStatementBuilder {

		private Connection connection;
		private String sql;
		private List<Param> params;

		public PreparedStatementBuilderImpl(Connection connection, String sql, List<Param> params) {
			this.connection = connection;
			this.sql = sql;
			this.params = params;
		}

		@Override
		public PreparedStatement build() throws Exception {
			PreparedStatement ps = connection.prepareStatement(sql);
			if (params != null) {
				for (int i = 0; i < params.size(); i++) {
					Param param = params.get(i);
					TypeHandler<?> handler;
					if (param.getType() != JDBCType.UNKONWN) {
						handler = configuration.getTypeHandlerRegistry().getHandler(param.getType());
					} else {
						if (param.getValue() != null) {
							handler = configuration.getTypeHandlerRegistry().getHandler(param.getValue().getClass());
						} else {
							handler = configuration.getTypeHandlerRegistry().getHandler((Class<?>) null);
						}
					}
					handler.setParameter(ps, i + 1, param.getValue(), param.getType());
				}
			}
			return ps;
		}
	}

}

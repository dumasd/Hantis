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
import com.thinkerwolf.hantis.transaction.ConnectionHolder;
import com.thinkerwolf.hantis.transaction.ConnectionUtils;
import com.thinkerwolf.hantis.type.JDBCType;
import com.thinkerwolf.hantis.type.TypeHandler;

import javax.sql.CommonDataSource;
import javax.sql.DataSource;
import javax.sql.XADataSource;


import java.sql.*;
import java.util.Collections;
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

	public CommonDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(CommonDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public void doBeforeCommit() throws SQLException {

	}

	@Override
	public void doBeforeRollback() throws SQLException {

	}

	@Override
	public void doAfterCommit() throws SQLException {

	}

	@Override
	public void doAfterRollback() throws SQLException {

	}

	@Override
	public <T> List<T> queryForList(String sql, List<Param> params, Class<T> clazz) {
		return queryForList(sql, params, new ResultSetListHandler<>(new ClassRowHander<>(clazz, nameHandler)));
	}

	@Override
	public <T> List<T> queryForList(String sql, Class<T> clazz) {
		return queryForList(sql, Collections.emptyList(), clazz);
	}

	@Override
	public <T> T queryForOne(String sql, List<Param> params, Class<T> clazz) {
		List<T> l = queryForList(sql, params, clazz);
		if (l.size() == 0) {
			return null;
		}
		if (l.size() > 1) {
			throw new RuntimeException("");
		}
		return l.get(0);
	}

	@Override
	public List<Map<String, Object>> queryForList(String sql, List<Param> params) {
		return queryForList(sql, params, new ResultSetListHandler<>(new MapRowHandler()));
	}

	@Override
	public List<Map<String, Object>> queryForList(String sql) {
		return queryForList(sql, Collections.emptyList());
	}

	@Override
	public Map<String, Object> queryForOne(String sql, List<Param> params) {
		List<Map<String, Object>> l = queryForList(sql, params);
		if (l.size() == 0) {
			return null;
		}
		if (l.size() > 1) {
			throw new ExecutorException("The result size > 1");
		}
		return l.get(0);
	}

	@Override
	public <T> List<T> queryForList(String sql, List<Param> params, ResultSetListHandler<T> listHandler) {
		Connection connection = getConnection();
		CacheKey cacheKey = createCacheKey(sql, params);
		RowBound rb = (RowBound) cache.getObject(cacheKey);
		QueryStatementExecuteCallback<List<T>> callback;
		if (rb == null) {
			PreparedStatementBuilder builder = new PreparedStatementBuilderImpl(connection, sql, params);
			callback = new QueryStatementExecuteCallback<>(builder, listHandler);
			if (logger.isDebugEnabled()) {
				logger.debug("[" + sql + "] queryForList no cache");
			}
		} else {
			callback = new QueryStatementExecuteCallback<>(listHandler, rb);
			if (logger.isDebugEnabled()) {
				logger.debug("[" + sql + "] queryForList use cache");
			}
		}
		List<T> result = execute(callback);
		cache.putObject(cacheKey, callback.getRowBound());
		return result;
	}

	@Override
	public <T> T execute(StatementExecuteCallback<T> callback) {
		return callback.execute();
	}

	@Override
	public int update(String sql, List<Param> params) {
		Connection connection = getConnection();
		int num = doUpdate(sql, params, connection);
		cache.clear();
		return num;
	}

	protected abstract int doUpdate(String sql, List<Param> params, Connection connection);

	private CacheKey createCacheKey(String sql, List<Param> params) {
		CacheKey key = new CacheKey();
		key.append(sql).append(params);
		return key;
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
		public T processRow(ResultSet rs) throws Throwable {
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
		public Map<String, Object> processRow(ResultSet rs) throws Throwable {
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

	private static class QueryStatementExecuteCallback<T> implements StatementExecuteCallback<T> {
		private PreparedStatementBuilder builder;
		private ResultSetHandler<T> listHandler;
		private RowBound rowBound;

		public QueryStatementExecuteCallback(PreparedStatementBuilder builder, ResultSetHandler<T> listHandler) {
			this.builder = builder;
			this.listHandler = listHandler;
		}

		public QueryStatementExecuteCallback(ResultSetHandler<T> listHandler, RowBound rowBound) {
			this.listHandler = listHandler;
			this.rowBound = rowBound;
		}

		@Override
		public T execute() {
			try {
				ResultSet rs;
				if (rowBound == null) {
					PreparedStatement ps = builder.build();
					rs = ps.executeQuery();
					rowBound = new RowBound(rs);
				}
				return listHandler.process(rowBound.getResultSet());
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
		}

		public RowBound getRowBound() {
			return rowBound;
		}
	}

	protected class PreparedStatementBuilderImpl implements PreparedStatementBuilder {

		private Connection connection;
		private String sql;
		private List<Param> params;
		private PreparedStatement ps;

		public PreparedStatementBuilderImpl(Connection connection, String sql, List<Param> params) {
			this.connection = connection;
			this.sql = sql;
			this.params = params;
		}

		public PreparedStatementBuilderImpl(PreparedStatement ps, List<Param> params) {
			this.ps = ps;
			this.params = params;
		}

		@Override
		public PreparedStatement build() throws Throwable {
			if (ps == null) {
				ps = connection.prepareStatement(sql);
			}
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

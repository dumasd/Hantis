package com.thinkerwolf.hantis.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.thinkerwolf.hantis.common.DefaultNameHandler;
import com.thinkerwolf.hantis.common.NameHandler;
import com.thinkerwolf.hantis.common.Param;
import com.thinkerwolf.hantis.common.type.JDBCType;
import com.thinkerwolf.hantis.common.type.TypeHandler;
import com.thinkerwolf.hantis.common.util.PropertyUtils;
import com.thinkerwolf.hantis.session.Configuration;
import com.thinkerwolf.hantis.transaction.TransactionSychronizationManager;
import com.thinkerwolf.hantis.transaction.jdbc.JdbcTransactionManager.JdbcResourceHolder;

/**
 * sql执行器
 * 
 * @author wukai
 *
 */
public class SqlExecutor {

	private DataSource dataSource;

	private NameHandler nameHandler = new DefaultNameHandler();

	private Configuration configuration;

	public <T> List<T> queryForList(String sql, List<Param> params, Class<T> clazz) {
		RowHandler<T> rowHandler = new ClassRowHander<>(clazz, nameHandler);
		ResultSetListHandler<T> listHandler = new ResultSetListHandler<>(rowHandler);
		PreparedStatementBuilder builder = new PreparedStatementBuilder() {
			@Override
			public PreparedStatement build(Connection connection, String sql) throws Throwable {
				return connection.prepareStatement(sql);
			}
		};
		StatementExecuteCallback<List<T>> callback = new StatementExecuteCallback<List<T>>() {
			@Override
			public List<T> execute() {
				try {
					PreparedStatement ps = builder.build(getConnection(), sql);
					for (int i = 0; i < params.size(); i++) {
						Param param = params.get(i);
						TypeHandler<?> handler;
						if (param.getType() != JDBCType.UNKONWN) {
							handler = configuration.getTypeHandlerRegistry().getHandler(param.getType());
						} else {
							handler = configuration.getTypeHandlerRegistry().getHandler(param.getValue().getClass());
						}
						handler.setParameter(ps, i + 1, param.getValue(), param.getType());
					}
					ResultSet rs = ps.executeQuery();
					return listHandler.process(rs);
				} catch (Throwable e) {
					throw new RuntimeException(e);
				}
			}
		};
		return execute(callback);
	}

	public <T> List<T> queryForList(String sql, Class<T> clazz) {
		return queryForList(sql, Collections.emptyList(), clazz);
	}

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

	public List<Map<String, Object>> queryForList(String sql, List<Param> params) {
		RowHandler<Map<String, Object>> rowHandler = new MapRowHandler();
		ResultSetListHandler<Map<String, Object>> listHandler = new ResultSetListHandler<>(rowHandler);
		PreparedStatementBuilder builder = new PreparedStatementBuilder() {
			@Override
			public PreparedStatement build(Connection connection, String sql) throws Throwable {
				return connection.prepareStatement(sql);
			}
		};
		StatementExecuteCallback<List<Map<String, Object>>> callback = new StatementExecuteCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> execute() {
				try {
					PreparedStatement ps = builder.build(getConnection(), sql);
					for (int i = 0; i < params.size(); i++) {
						Param param = params.get(i);
						TypeHandler<?> handler;
						if (param.getType() != JDBCType.UNKONWN) {
							handler = configuration.getTypeHandlerRegistry().getHandler(param.getType());
						} else {
							handler = configuration.getTypeHandlerRegistry().getHandler(param.getValue().getClass());
						}
						handler.setParameter(ps, i + 1, param.getValue(), param.getType());
					}
					ResultSet rs = ps.executeQuery();
					return listHandler.process(rs);
				} catch (Throwable e) {
					throw new RuntimeException(e);
				} finally {

				}
			}
		};
		return execute(callback);
	}

	public List<Map<String, Object>> queryForList(String sql) {
		return queryForList(sql, Collections.emptyList());
	}

	public Map<String, Object> queryForOne(String sql, List<Param> params) {
		List<Map<String, Object>> l = queryForList(sql, params);
		if (l.size() == 0) {
			return null;
		}
		if (l.size() > 1) {
			throw new RuntimeException("");
		}
		return l.get(0);
	}

	public <T> T execute(StatementExecuteCallback<T> callback) {
		return callback.execute();
	}

	/**
	 * @param sql
	 * @param t
	 * @return
	 */
	public int update(String sql, List<Param> params) {
		PreparedStatementBuilder builder = new PreparedStatementBuilder() {
			@Override
			public PreparedStatement build(Connection connection, String sql) throws Throwable {
				return connection.prepareStatement(sql);
			}
		};
		StatementExecuteCallback<Integer> callback = new StatementExecuteCallback<Integer>() {
			@Override
			public Integer execute() {
				try {
					PreparedStatement ps = builder.build(getConnection(), sql);
					for (int i = 0; i < params.size(); i++) {
						ps.setObject(i + 1, params.get(i).getValue(), params.get(i).getType().getType());
					}
					return ps.executeUpdate();
				} catch (Throwable e) {
					throw new RuntimeException(e);
				}
			}
		};
		return execute(callback);
	}

	protected Connection getConnection() {
		JdbcResourceHolder resourceHolder = (JdbcResourceHolder) TransactionSychronizationManager
				.getResource(dataSource);
		if (resourceHolder != null) {
			return resourceHolder.getConnection();
		} else {
			try {
				return dataSource.getConnection();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
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

}

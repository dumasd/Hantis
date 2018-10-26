package com.thinkerwolf.hantis.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.thinkerwolf.hantis.common.DefaultNameHandler;
import com.thinkerwolf.hantis.common.NameHandler;
import com.thinkerwolf.hantis.common.Params;
import com.thinkerwolf.hantis.common.util.PropertyUtils;
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

	public List<Map<String, Object>> queryForMapList() {

		return null;
	}

	public <T> List<T> queryForList(String sql, Params params, Class<T> clazz) throws Throwable {
		Connection conn = getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		for (int i = 0; i < params.size(); i++) {
			ps.setObject(i + 1, params.get(i).getValue(), params.get(i).getType().getType());
		}
		List<T> resultList = new ArrayList<T>();
		ResultSet rs = ps.executeQuery();
		for (; rs.next();) {
			T t = clazz.newInstance();
			ResultSetMetaData meta = rs.getMetaData();
			int count = meta.getColumnCount();
			for (int i = 0; i < count; i++) {
				Object obj = rs.getObject(i + 1);
				String columnName = meta.getColumnName(i + 1);
				PropertyUtils.setProperty(t, nameHandler.convertToPropertyName(columnName), obj);
			}
			resultList.add(t);
		}
		return resultList;
	}

	public <T> T queryForOne(String sql, Params params, Class<T> clazz) throws Throwable {
		List<T> l = queryForList(sql, params, clazz);
		if (l.size() == 0) {
			return null;
		}
		return l.get(0);
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

	private static class ClassRowHander<T> implements RowHandler<T> {
		private Class<T> clazz;
		private NameHandler nameHandler;
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

}

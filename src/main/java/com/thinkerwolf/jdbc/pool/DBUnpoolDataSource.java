package com.thinkerwolf.jdbc.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 无缓存数据源
 * 
 * @author wukai
 *
 */
public class DBUnpoolDataSource extends AbstractDataSource {

	@Override
	protected Connection doGetConnection() throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}

	@Override
	protected Connection doGetConnection(String username, String password) throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}

}

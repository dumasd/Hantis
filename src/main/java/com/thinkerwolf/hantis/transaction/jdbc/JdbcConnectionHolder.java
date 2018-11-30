package com.thinkerwolf.hantis.transaction.jdbc;

import java.sql.Connection;

import javax.sql.DataSource;

import com.thinkerwolf.hantis.transaction.ConnectionHolder;

public class JdbcConnectionHolder extends ConnectionHolder {
	private DataSource dataSource;

	public JdbcConnectionHolder(Connection connection, DataSource dataSource) {
		super(connection);
		this.dataSource = dataSource;
	}

	@Override
	public Object getResource() {
		return dataSource;
	}

}

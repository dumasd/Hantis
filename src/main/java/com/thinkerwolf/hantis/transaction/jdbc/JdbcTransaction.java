package com.thinkerwolf.hantis.transaction.jdbc;

import java.sql.SQLException;

import com.thinkerwolf.hantis.transaction.ConnectionUtils;
import com.thinkerwolf.hantis.transaction.Transaction;

public class JdbcTransaction implements Transaction {
	private JdbcConnectionHolder resourceHolder;

	public JdbcTransaction(JdbcConnectionHolder resourceHolder) {
		this.resourceHolder = resourceHolder;
	}

	@Override
	public void commit() throws SQLException {
		ConnectionUtils.commitConnection(resourceHolder.getConnection());
	}

	@Override
	public void rollback() throws SQLException {
		ConnectionUtils.rollbackConnection(resourceHolder.getConnection());
	}

	@Override
	public void close() throws SQLException {
		ConnectionUtils.closeConnection(resourceHolder.getConnection());
	}

	public JdbcConnectionHolder getResourceHolder() {
		return resourceHolder;
	}

	public void setResourceHolder(JdbcConnectionHolder resourceHolder) {
		this.resourceHolder = resourceHolder;
	}

	@Override
	public boolean isComplete() {
		return false;
	}

	@Override
	public void setComplete() {
	}
}

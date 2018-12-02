package com.thinkerwolf.hantis.transaction.jta;

import java.sql.SQLException;

import javax.transaction.UserTransaction;

import com.thinkerwolf.hantis.transaction.Transaction;

public class JtaTransaction implements Transaction {

	private UserTransaction userTransaction;

	public JtaTransaction(UserTransaction userTransaction) {
		this.userTransaction = userTransaction;
	}

	@Override
	public void commit() throws SQLException {
		try {
			userTransaction.commit();
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

	@Override
	public void rollback() throws SQLException {
		try {
			userTransaction.rollback();
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

	public UserTransaction getUserTransaction() {
		return userTransaction;
	}

	@Override
	public void close() throws SQLException {

    }

	@Override
	public boolean isComplete() {
		return false;
	}

	@Override
	public void setComplete() {

	}

}

package com.thinkerwolf.jdbc.transaction.jdbc;

import javax.sql.DataSource;

import com.thinkerwolf.jdbc.transaction.AbstractTransactionDefinition;
import com.thinkerwolf.jdbc.transaction.Transaction;
import com.thinkerwolf.jdbc.transaction.TransactionIsolationLevel;
import com.thinkerwolf.jdbc.transaction.TransactionPropagationBehavior;

public class JdbcTransactionDefinition extends AbstractTransactionDefinition {

	private DataSource dataSource;

	public JdbcTransactionDefinition(DataSource dataSource, TransactionIsolationLevel isolationLevel,
			TransactionPropagationBehavior propagationBehavior) {
		super(isolationLevel, propagationBehavior);
		this.dataSource = dataSource;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Transaction newTransaction() {
		return null;
	}

}

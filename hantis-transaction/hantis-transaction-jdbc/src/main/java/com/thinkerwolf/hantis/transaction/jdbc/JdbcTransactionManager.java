package com.thinkerwolf.hantis.transaction.jdbc;

import com.thinkerwolf.hantis.transaction.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JdbcTransactionManager extends AbstractTransactionManager {

	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	protected Transaction doGetTransaction(TransactionDefinition defination) {
		JdbcConnectionHolder holder = (JdbcConnectionHolder) TransactionSychronizationManager.getResource(dataSource);
		JdbcTransaction jdbcTransaction = new JdbcTransaction(holder);
		return jdbcTransaction;
	}

	@Override
	protected boolean isExistsTransaction(Transaction transaction) {
		JdbcTransaction jdbcTransaction = (JdbcTransaction) transaction;
		return jdbcTransaction.getResourceHolder() != null;
	}

	@Override
	protected Object doSuspend(Transaction transaction) throws TransactionException {
		JdbcTransaction jdbcTransaction = (JdbcTransaction) transaction;
		jdbcTransaction.setResourceHolder(null);
		return TransactionSychronizationManager.unbindResource(dataSource);
	}

	@Override
	protected void doBegin(Transaction transaction, TransactionDefinition definition) throws TransactionException {
		JdbcTransaction jdbcTransaction = (JdbcTransaction) transaction;
		JdbcConnectionHolder holder = jdbcTransaction.getResourceHolder();
		try {
			Connection connection = dataSource.getConnection();
			if (holder == null) {
				holder = (JdbcConnectionHolder) createResourceHolder(connection);
				jdbcTransaction.setResourceHolder(holder);
			} else {
				holder.setPreviousIsolationLevel(connection.getTransactionIsolation());
				holder.setPreviousAutoCommit(connection.getAutoCommit());
			}

			if (connection.getAutoCommit() != definition.isAutoCommit()) {
				connection.setAutoCommit(definition.isAutoCommit());
			}
			connection.setTransactionIsolation(definition.getIsolationLevel().getId());
			TransactionSychronizationManager.bindResource(dataSource, holder);
		} catch (SQLException e) {
			throw new TransactionException("Can't get jdbc !", e);
		}
	}

	@Override
	protected void doResume(Transaction transaction, Object suspendResources) {
		JdbcTransaction jdbcTransaction = (JdbcTransaction) transaction;
		JdbcConnectionHolder holder = (JdbcConnectionHolder) suspendResources;
		jdbcTransaction.setResourceHolder(holder);
		TransactionSychronizationManager.bindResource(dataSource, holder);
	}

	@Override
	protected void doCommit(Transaction transaction) throws TransactionException {
		try {
			transaction.commit();
		} catch (SQLException e) {
			throw new TransactionException("commit error");
		}
	}

	@Override
	protected void doRollback(Transaction transaction) {
		try {
			transaction.rollback();
		} catch (SQLException e) {
            throw new TransactionException("rollback error");
        }
    }

	@Override
	protected void doClearAfterCompletion(Transaction transaction) {
		TransactionSychronizationManager.unbindResource(dataSource);
		JdbcTransaction jdbcTransaction = (JdbcTransaction) transaction;
		JdbcConnectionHolder holder = jdbcTransaction.getResourceHolder();
		try {
			if (holder != null) {
				ConnectionUtils.clearAfterCommitOrRollback(holder);
			}
		} catch (SQLException e) {
			throw new TransactionException("Can't reset jdbc connection");
		}
	}

	@Override
    public ConnectionHolder doCreateResourceHolder(Connection connection) throws SQLException {
        return new JdbcConnectionHolder(connection, dataSource);
    }

	@Override
	public void afterPropertiesSet() throws Throwable {
		// nothing
	}

    @Override
    public boolean isDistributed() {
        return false;
    }
}

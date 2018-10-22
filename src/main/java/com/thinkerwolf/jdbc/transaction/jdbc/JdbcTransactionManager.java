package com.thinkerwolf.jdbc.transaction.jdbc;

import com.thinkerwolf.jdbc.transaction.*;

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
		JdbcResourceHolder holder = (JdbcResourceHolder) TransactionSychronizationManager.getResource(dataSource);
		JdbcTransaction jdbcTransaction = new JdbcTransaction(holder);
		return jdbcTransaction;
	}

	@Override
	protected boolean isExistsTransaction(Transaction transaction) {
		JdbcTransaction jdbcTransaction = (JdbcTransaction) transaction;
		return jdbcTransaction.getResourceHolder() != null;
	}

	@Override
	protected Object doSuspend(Transaction transaction) {
		JdbcTransaction jdbcTransaction = (JdbcTransaction) transaction;
		jdbcTransaction.setResourceHolder(null);
		return TransactionSychronizationManager.unbindResource(dataSource);
	}

	@Override
	protected void doBegin(Transaction transaction, TransactionDefinition definition) throws TransactionException {
		JdbcTransaction jdbcTransaction = (JdbcTransaction) transaction;
		JdbcResourceHolder holder = jdbcTransaction.resourceHolder;
		try {
			if (holder == null) {
				holder = new JdbcResourceHolder(dataSource);

				jdbcTransaction.setResourceHolder(holder);
			}
			TransactionSychronizationManager.bindResource(dataSource, holder);
			Connection connection = dataSource.getConnection();
			connection.setTransactionIsolation(definition.getIsolationLevel().getId());
			connection.setAutoCommit(false);
			holder.setConnection(connection);
			holder.setPreviousIsolationLevel(connection.getTransactionIsolation());
		} catch (SQLException e) {
			throw new TransactionException("");
		}
	}

	@Override
	protected void doResume(Transaction transaction, Object suspendResources) {
		JdbcTransaction jdbcTransaction = (JdbcTransaction) transaction;
		JdbcResourceHolder holder = (JdbcResourceHolder) suspendResources;
		jdbcTransaction.setResourceHolder(holder);
		TransactionSychronizationManager.bindResource(dataSource, holder);
	}

	@Override
	protected void doCommit(Transaction transaction) {
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
			throw new TransactionException("rollback errot");
		}
	}

	@Override
	protected void doClearAfterCompletion(Transaction transaction) {
		TransactionSychronizationManager.unbindResource(dataSource);
		JdbcTransaction jdbcTransaction = (JdbcTransaction) transaction;
		JdbcResourceHolder holder = jdbcTransaction.resourceHolder;
		try {
			if (holder != null && holder.getConnection() != null) {
				holder.getConnection().setTransactionIsolation(holder.getPreviousIsolationLevel());
				if (holder.getConnection().isReadOnly()) {
					holder.getConnection().setReadOnly(false);
				}
			}
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.debug("Can't reset jdbc connection");
			}
		}

	}

	private static class JdbcTransaction implements Transaction {

		private JdbcResourceHolder resourceHolder;

		public JdbcTransaction(JdbcResourceHolder resourceHolder) {
			this.resourceHolder = resourceHolder;
		}

		@Override
		public void commit() throws SQLException {
			resourceHolder.connection.commit();
		}

		@Override
		public void rollback() throws SQLException {
			resourceHolder.connection.rollback();
		}

		@Override
		public void close() throws SQLException {
			resourceHolder.connection.close();
		}

		public JdbcResourceHolder getResourceHolder() {
			return resourceHolder;
		}

		public void setResourceHolder(JdbcResourceHolder resourceHolder) {
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

	public static class JdbcResourceHolder implements ResourceHolder {

		private DataSource dataSource;

		private Connection connection;

		private int previousIsolationLevel;

		public JdbcResourceHolder(DataSource dataSource) {
			this.dataSource = dataSource;
		}

		@Override
		public Object getResource() {
			return dataSource;
		}

		public Connection getConnection() {
			return connection;
		}

		public void setConnection(Connection connection) {
			this.connection = connection;
		}

		public int getPreviousIsolationLevel() {
			return previousIsolationLevel;
		}

		public void setPreviousIsolationLevel(int previousIsolationLevel) {
			this.previousIsolationLevel = previousIsolationLevel;
		}

	}

}

package com.thinkerwolf.hantis.transaction.jta;

import com.thinkerwolf.hantis.transaction.*;

import javax.transaction.UserTransaction;

import java.sql.Connection;
import java.sql.SQLException;

import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

public class JtaTransactionManager extends AbstractTransactionManager {

	private UserTransaction userTransaction;

	private TransactionManager transactionManager;
	
	protected int transactionTimeout;
	
	@Override
	protected Transaction doGetTransaction(TransactionDefinition defination) {
		UserTransaction userTransaction = getUserTransaction();
		if (userTransaction == null) {
			throw new TransactionException("UserTransaction is null");
		}
		return new JtaTransaction(userTransaction);
	}

	@Override
	protected boolean isExistsTransaction(Transaction transaction) {
		JtaTransaction jtaTransaction = getJtaTransaction(transaction);
		try {
			return jtaTransaction.getUserTransaction().getStatus() != Status.STATUS_NO_TRANSACTION;
		} catch (SystemException e) {
			throw new TransactionException("JTA failure on getStatus", e);
		}
	}

	@Override
	protected Object doSuspend(Transaction transaction) throws TransactionException {
		TransactionManager transactionManager = getTransactionManager();
		if (transactionManager == null) {
			throw new TransactionException("Suspend operation is not supported");
		}
		try {
			return transactionManager.suspend();
		} catch (SystemException e) {
			throw new TransactionException("JTA failure on suspend", e);
		}
	}

	@Override
	protected void doBegin(Transaction transaction, TransactionDefinition definition) throws TransactionException {
		JtaTransaction jtaTransaction = getJtaTransaction(transaction);
		try {
			jtaTransaction.getUserTransaction().begin();
            //transactionManager.begin();
        } catch (Throwable e) {
            throw new TransactionException(e);
		}
	}

	@Override
	protected void doCommit(Transaction transaction) throws TransactionException {
		JtaTransaction jtaTransaction = getJtaTransaction(transaction);
		try {
			jtaTransaction.getUserTransaction().commit();
            //transactionManager.commit();
        } catch (Throwable e) {
            throw new TransactionException(e);
		}
	}

	@Override
	protected void doResume(Transaction transaction, Object suspendResources) throws TransactionException {
		TransactionManager transactionManager = getTransactionManager();
		if (transactionManager == null) {
			throw new TransactionException("Resume operation is not supported");
		}
		javax.transaction.Transaction resumeObject = (javax.transaction.Transaction) suspendResources;
		try {
			transactionManager.resume(resumeObject);
		} catch (Exception e) {
			throw new TransactionException("JTA failure on resume", e);
		}
	}

	@Override
	protected void doRollback(Transaction transaction) throws TransactionException {
		JtaTransaction jtaTransaction = getJtaTransaction(transaction);
		try {
			jtaTransaction.getUserTransaction().rollback();
		} catch (Exception e) {
			throw new TransactionException("JTA failure on rollback", e);
		}
	}

	@Override
	protected void doClearAfterCompletion(Transaction transaction) throws TransactionException {

	}

	public void setUserTransaction(UserTransaction userTransaction) {
		this.userTransaction = userTransaction;
	}

	public void setTransactionManager(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public UserTransaction getUserTransaction() {
		return userTransaction;
	}

	public TransactionManager getTransactionManager() {
		return transactionManager;
	}

	public int getTransactionTimeout() {
		return transactionTimeout;
	}

	public void setTransactionTimeout(int transactionTimeout) {
		this.transactionTimeout = transactionTimeout;
	}

	protected JtaTransaction getJtaTransaction(Transaction transaction) {
		return (JtaTransaction) transaction;
	}

	protected UserTransaction retrieveUserTransaction() throws TransactionException {
		return null;
	}

	protected TransactionManager retrieveTransactionManager() throws TransactionException {
		return null;
	}

	@Override
	public void afterPropertiesSet() throws Throwable {
		if (userTransaction == null) {
			userTransaction = retrieveUserTransaction();
		}

		if (transactionManager == null) {
			transactionManager = retrieveTransactionManager();

			if (transactionManager == null && userTransaction != null) {
				if (userTransaction instanceof TransactionManager) {
					transactionManager = (TransactionManager) userTransaction;
				}
			}
		}

		if (userTransaction == null && transactionManager != null) {
			userTransaction = new UserTransactionAdapter(transactionManager);
		}
	}

	@Override
    public ConnectionHolder doCreateResourceHolder(Connection connection) throws SQLException {
        return new JtaConnectionHolder(connection);
    }

    @Override
    public boolean isDistributed() {
        return true;
    }
}

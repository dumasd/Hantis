package com.thinkerwolf.hantis.transaction.jta;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

public class UserTransactionAdapter implements UserTransaction {

	private TransactionManager transactionManager;

	public UserTransactionAdapter(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	@Override
	public void begin() throws NotSupportedException, SystemException {
		transactionManager.begin();
	}

	@Override
	public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException,
			SecurityException, IllegalStateException, SystemException {
		transactionManager.commit();
	}

	@Override
	public void rollback() throws IllegalStateException, SecurityException, SystemException {
		transactionManager.rollback();
	}

	@Override
	public void setRollbackOnly() throws IllegalStateException, SystemException {
		transactionManager.setRollbackOnly();
	}

	@Override
	public int getStatus() throws SystemException {
		return transactionManager.getStatus();
	}

	@Override
	public void setTransactionTimeout(int seconds) throws SystemException {
		transactionManager.setTransactionTimeout(seconds);
	}

}

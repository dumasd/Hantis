package com.thinkerwolf.hantis.transaction.jta.atomikos;

import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.thinkerwolf.hantis.transaction.TransactionException;
import com.thinkerwolf.hantis.transaction.jta.JtaTransactionManager;

/**
 * Atomikos
 * 
 * @author wukai
 *
 */
public class AtomikosTransactionManager extends JtaTransactionManager {

    com.atomikos.icatch.config.UserTransactionService uts =
            new com.atomikos.icatch.config.UserTransactionServiceImp();

	@Override
	protected TransactionManager retrieveTransactionManager() throws TransactionException {

        return new UserTransactionManager();
    }

	@Override
	protected UserTransaction retrieveUserTransaction() throws TransactionException {
		UserTransaction ut = new UserTransactionImp();
		try {
			ut.setTransactionTimeout(getTransactionTimeout());
			return ut;
		} catch (SystemException e) {
			throw new TransactionException(e);
		}
	}
}

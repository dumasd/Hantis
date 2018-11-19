package com.thinkerwolf.hantis.session;

import com.thinkerwolf.hantis.transaction.DefaultTransactionDefinition;
import com.thinkerwolf.hantis.transaction.Transaction;
import com.thinkerwolf.hantis.transaction.TransactionDefinition;
import com.thinkerwolf.hantis.transaction.TransactionManager;

public class DefaultSessionFactory implements SessionFactory {

	private TransactionManager transactionManager;

	private SessionFactoryBuilder builder;

	public DefaultSessionFactory(SessionFactoryBuilder builder) {
		this.transactionManager = builder.getTransactionManager();
		this.builder = builder;
	}

	@Override
	public Session openSession() {
		return openSession(false);
	}

	@Override
	public Session openSession(boolean autoCommit) {
		TransactionDefinition td = new DefaultTransactionDefinition(autoCommit);
		DefaultSession session = new DefaultSession(transactionManager.getTransaction(td), builder);
		return session;
	}

    @Override
    public Transaction openTransaction() {
        TransactionDefinition td = new DefaultTransactionDefinition(false);
        return transactionManager.getTransaction(td);
    }

    @Override
    public Session openSessionWithoutTransaction() {
        DefaultSession session = new DefaultSession(null, builder);
        return session;
    }


}

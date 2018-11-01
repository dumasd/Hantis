package com.thinkerwolf.hantis.session;

import com.thinkerwolf.hantis.transaction.DefaultTransactionDefinition;
import com.thinkerwolf.hantis.transaction.Transaction;
import com.thinkerwolf.hantis.transaction.TransactionManager;

public class DefaultSessionFactory implements SessionFactory {


    private TransactionManager transactionManager;

    private SessionFactoryBuilder builder;

    private DefaultSessionFactory(SessionFactoryBuilder builder) {
        this.transactionManager = builder.getTransactionManager();
        this.builder = builder;
    }

    @Override
    public Session openSession() {
        Transaction transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());
        DefaultSession session = new DefaultSession();
        return session;
    }

    @Override
    public Session openSession(boolean autoCommit) {
        return null;
    }
}

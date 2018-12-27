package com.thinkerwolf.hantis.transaction.jdbc;

import com.thinkerwolf.hantis.transaction.TransactionManager;
import com.thinkerwolf.hantis.transaction.TransactionManagerFactory;

public class JdbcTransactionManagerFactory implements TransactionManagerFactory {


    @Override
    public Class<?> getObjectType() {
        return JdbcTransactionManager.class;
    }

    @Override
    public TransactionManager getObject() throws Exception {
        JdbcTransactionManager jdbcTransactionManager = new JdbcTransactionManager();
        return jdbcTransactionManager;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}

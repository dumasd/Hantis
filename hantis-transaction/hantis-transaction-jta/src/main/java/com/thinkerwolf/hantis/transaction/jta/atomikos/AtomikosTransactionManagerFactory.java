package com.thinkerwolf.hantis.transaction.jta.atomikos;

import com.thinkerwolf.hantis.transaction.TransactionManager;
import com.thinkerwolf.hantis.transaction.TransactionManagerFactory;

public class AtomikosTransactionManagerFactory implements TransactionManagerFactory {

    @Override
    public TransactionManager getObject() throws Exception {
        AtomikosTransactionManager manager = new AtomikosTransactionManager();
        return manager;
    }

    @Override
    public Class <?> getObjectType() {
        return AtomikosTransactionManager.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}

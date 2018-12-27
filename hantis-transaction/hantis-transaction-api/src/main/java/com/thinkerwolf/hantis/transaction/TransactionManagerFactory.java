package com.thinkerwolf.hantis.transaction;

import com.thinkerwolf.hantis.common.Factory;

public interface TransactionManagerFactory extends Factory<TransactionManager> {


    @Override
    TransactionManager getObject() throws Exception;

    @Override
    boolean isSingleton();
}

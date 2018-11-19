package com.thinkerwolf.hantis.transaction.jta;

import com.thinkerwolf.hantis.transaction.*;

import javax.transaction.UserTransaction;

public class JtaTransactionManager extends AbstractTransactionManager {

    private UserTransaction userTransaction;

    private TransactionManager transactionManager;



    @Override
    protected Transaction doGetTransaction(TransactionDefinition defination) {
        try {

        } catch (Exception e) {

        }
        return null;
    }

    @Override
    protected boolean isExistsTransaction(Transaction transaction) {
        return false;
    }

    @Override
    protected Object doSuspend(Transaction transaction) throws TransactionException {
        return null;
    }

    @Override
    protected void doBegin(Transaction transaction, TransactionDefinition definition) throws TransactionException {


    }

    @Override
    protected void doCommit(Transaction transaction) throws TransactionException {

    }

    @Override
    protected void doResume(Transaction transaction, Object suspendResources) throws TransactionException {

    }

    @Override
    protected void doRollback(Transaction transaction) throws TransactionException {

    }

    @Override
    protected void doClearAfterCompletion(Transaction transaction) throws TransactionException {

    }


}

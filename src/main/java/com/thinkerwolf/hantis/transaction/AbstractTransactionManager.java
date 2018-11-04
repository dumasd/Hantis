package com.thinkerwolf.hantis.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 事务管理器抽象(参考spring事务)
 *
 * @author wukai
 */
public abstract class AbstractTransactionManager implements TransactionManager {

    private static final AtomicInteger nameId = new AtomicInteger(1);
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private String name;

    public AbstractTransactionManager() {
        String name = getClass().getName() + "-" + nameId.getAndIncrement();
        this.name = name;
    }

    public AbstractTransactionManager(String name) {
        this.name = name;
    }

    @Override
    public Transaction getTransaction(TransactionDefinition definition) {
        // 获取一个Transaction
        Transaction transaction = doGetTransaction(definition);
        // 判断是否存在Transaction，存在判断传播行为
        boolean debugEnable = logger.isDebugEnabled();
        if (isExistsTransaction(transaction)) {

            return handleExistsTransaction(definition, transaction);
        }
        if (debugEnable) {
            logger.debug("The transaction does't exists");
        }
        // 不存在
        TransactionPropagationBehavior behavior = definition.getPropagationBehavior();
        if (behavior == TransactionPropagationBehavior.MANDATORY) {
            throw new TransactionException("There is not transaction because of MANDATORY");
        }
        if (behavior == TransactionPropagationBehavior.REQUIRED
                || behavior == TransactionPropagationBehavior.REQUIRES_NEW
                || behavior == TransactionPropagationBehavior.NESTED) {

            doBegin(transaction, definition);
            DefaultTransaction defaultTransaction = new DefaultTransaction(transaction, null);
            return defaultTransaction;
        }
        DefaultTransaction defaultTransaction = new DefaultTransaction(transaction, null);
        return defaultTransaction;
    }

    private Transaction handleExistsTransaction(TransactionDefinition definition, Transaction transaction) {
        TransactionPropagationBehavior behavior = definition.getPropagationBehavior();
        boolean debugEnable = logger.isDebugEnabled();
        if (debugEnable) {
            logger.debug("The tranaction exists-" + behavior.name());
        }
        if (behavior == TransactionPropagationBehavior.NEVER) {
            throw new TransactionException("There must have a transaction accodting to Never");
        }
        if (behavior == TransactionPropagationBehavior.REQUIRES_NEW) {
            // 新建事务，当前事务挂起
            SuspendResourceHolder holder = suspend(transaction);
            doBegin(transaction, definition);
            DefaultTransaction defaultTransaction = new DefaultTransaction(transaction, holder);
            return defaultTransaction;
        }

        if (behavior == TransactionPropagationBehavior.NOT_SUPPORTED) {
            // 挂起事务
            SuspendResourceHolder holder = suspend(transaction);
            DefaultTransaction defaultTransaction = new DefaultTransaction(transaction, holder);
            return defaultTransaction;
        }

        if (behavior == TransactionPropagationBehavior.NESTED) {
            //TODO 内嵌事务

        }

        // 支持当前事务
        DefaultTransaction defaultTransaction = new DefaultTransaction(transaction, null);
        return defaultTransaction;
    }

    protected abstract Transaction doGetTransaction(TransactionDefinition defination);

    /**
     * 事务是否存在
     *
     * @param transaction
     * @return
     */
    protected abstract boolean isExistsTransaction(Transaction transaction);

    /**
     * 挂起一个事务
     *
     * @param transaction
     */
    protected final SuspendResourceHolder suspend(Transaction transaction) {
        Object resource = doSuspend(transaction);
        SuspendResourceHolder holder = new SuspendResourceHolder(resource);
        return holder;
    }

    @Override
    public void commit(Transaction transaction) {
        try {
            DefaultTransaction dt = (DefaultTransaction) transaction;
            if (dt.isComplete()) {
                throw new TransactionException("The transaction is completed");
            }
            doCommit(dt.getTransaction());
        } catch (Throwable e) {
            // 回滚
            doRollback(transaction);
        } finally {
            clearAfterCommit(transaction);
        }
    }

    private void clearAfterCommit(Transaction transaction) {
        DefaultTransaction dt = (DefaultTransaction) transaction;
        doClearAfterCompletion(dt.getTransaction());
        if (dt.getSuspendResourceHolder() != null) {
            resume(dt.getTransaction(), dt.getSuspendResourceHolder());
        }
    }

    /**
     * 恢复一个事务
     *
     * @param transaction
     */
    protected final void resume(Transaction transaction, SuspendResourceHolder holder) {
        doResume(transaction, holder.getResource());
    }

    @Override
    public void rollback(Transaction transaction) {
        DefaultTransaction dt = (DefaultTransaction) transaction;
        doRollback(dt.getTransaction());
    }

    protected abstract Object doSuspend(Transaction transaction) throws TransactionException;

    protected abstract void doBegin(Transaction transaction, TransactionDefinition definition)
            throws TransactionException;

    protected abstract void doCommit(Transaction transaction) throws TransactionException;

    protected abstract void doResume(Transaction transaction, Object suspendResources) throws TransactionException;

    protected abstract void doRollback(Transaction transaction) throws TransactionException;

    protected abstract void doClearAfterCompletion(Transaction transaction) throws TransactionException;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class SuspendResourceHolder {
        private Object resource;

        public SuspendResourceHolder(Object resource) {
            this.resource = resource;
        }

        public Object getResource() {
            return resource;
        }

    }

}

package com.thinkerwolf.hantis.transaction;

/**
 * 事务管理器
 *
 * @author wukai
 */
public interface TransactionManager {

    Transaction getTransaction(TransactionDefinition defination);

    String getName();

    void commit(Transaction transaction);

    void rollback(Transaction transaction);

}

package com.thinkerwolf.hantis.transaction;

/**
 * 事务定义
 *
 * @author wukai
 */
public interface TransactionDefinition {

    TransactionIsolationLevel getIsolationLevel();

    TransactionPropagationBehavior getPropagationBehavior();

    Transaction newTransaction();


}

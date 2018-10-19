package com.thinkerwolf.jdbc.transaction;

/**
 * 事务管理器
 * 
 * @author wukai
 *
 */
public interface TransactionManager {

	Transaction getTransaction(TransactionDefinition defination);

	String getName();
}

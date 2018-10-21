package com.thinkerwolf.jdbc.transaction;

/**
 * 事务管理器抽象(参考spring事务)
 * 
 * @author wukai
 *
 */
public abstract class AbstractTransactionManager implements TransactionManager {

	private String name;

	@Override
	public Transaction getTransaction(TransactionDefinition defination) {
		// 获取一个Transaction
		Transaction transaction = doGetTransaction(defination);
		// 判断是否存在Transaction，存在判断传播行为
		if (isExistsTransaction(transaction)) {




		}


		// 不存在

		return null;
	}


	/*private Transaction handleExistsTransaction(TransactionDefinition definition, Transaction transaction) {
		TransactionPropagationBehavior behavior = definition.getPropagationBehavior();
		if (behavior == TransactionPropagationBehavior.MANDATORY) {
			throw new TransactionException("There must have a transaction accodting to MANDATORY" );
		}
		if (behavior == TransactionPropagationBehavior.REQUIRES_NEW) {
			// 新建事务，当前事务挂起


		}


	}*/



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
	public void suspend(Transaction transaction) {
		
	}

	/**
	 * 恢复一个事务
	 * 
	 * @param transaction
	 */
	public void resume(Transaction transaction) {

	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

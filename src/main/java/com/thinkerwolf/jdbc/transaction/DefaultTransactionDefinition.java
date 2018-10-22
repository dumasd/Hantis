package com.thinkerwolf.jdbc.transaction;

public class DefaultTransactionDefinition extends AbstractTransactionDefinition {

	public DefaultTransactionDefinition(TransactionIsolationLevel isolationLevel,
			TransactionPropagationBehavior propagationBehavior) {
		super(isolationLevel, propagationBehavior);
	}
	
	public DefaultTransactionDefinition() {
		super(TransactionIsolationLevel.READ_COMMITTED, TransactionPropagationBehavior.REQUIRED);
	}

	@Override
	public Transaction newTransaction() {
		return null;
	}

}

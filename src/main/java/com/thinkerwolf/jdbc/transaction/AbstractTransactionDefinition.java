package com.thinkerwolf.jdbc.transaction;

public abstract class AbstractTransactionDefinition implements TransactionDefinition {

	protected TransactionIsolationLevel isolationLevel;
	protected TransactionPropagationBehavior propagationBehavior;

	public AbstractTransactionDefinition(TransactionIsolationLevel isolationLevel,
			TransactionPropagationBehavior propagationBehavior) {
		this.isolationLevel = isolationLevel;
		this.propagationBehavior = propagationBehavior;
	}

	@Override
	public TransactionIsolationLevel getIsolationLevel() {
		return isolationLevel;
	}

	@Override
	public TransactionPropagationBehavior getPropagationBehavior() {
		return propagationBehavior;
	}

}

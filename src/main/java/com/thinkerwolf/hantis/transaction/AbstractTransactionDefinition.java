package com.thinkerwolf.hantis.transaction;

public abstract class AbstractTransactionDefinition implements TransactionDefinition {

	protected TransactionIsolationLevel isolationLevel;
	protected TransactionPropagationBehavior propagationBehavior;

	public AbstractTransactionDefinition(TransactionIsolationLevel isolationLevel,
			TransactionPropagationBehavior propagationBehavior) {
		if (isolationLevel == null || propagationBehavior == null) {
			throw new IllegalArgumentException("");
		}
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

package com.thinkerwolf.hantis.transaction;

public abstract class AbstractTransactionDefinition implements TransactionDefinition {

    private TransactionIsolationLevel isolationLevel;
    private TransactionPropagationBehavior propagationBehavior;
    private boolean autoCommit;

    public AbstractTransactionDefinition(TransactionIsolationLevel isolationLevel,
                                         TransactionPropagationBehavior propagationBehavior, boolean autoCommit) {
        if (isolationLevel == null || propagationBehavior == null) {
            throw new IllegalArgumentException("");
        }
        this.isolationLevel = isolationLevel;
        this.propagationBehavior = propagationBehavior;
        this.autoCommit = autoCommit;
    }

    @Override
    public TransactionIsolationLevel getIsolationLevel() {
        return isolationLevel;
    }

    @Override
    public TransactionPropagationBehavior getPropagationBehavior() {
        return propagationBehavior;
    }

    @Override
    public boolean isAutoCommit() {
        return autoCommit;
    }
}

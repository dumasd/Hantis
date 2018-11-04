package com.thinkerwolf.hantis.transaction;

public class DefaultTransactionDefinition extends AbstractTransactionDefinition {

    public DefaultTransactionDefinition() {
        this(false);
    }

    public DefaultTransactionDefinition(boolean autoCommit) {
        this(TransactionIsolationLevel.READ_COMMITTED, TransactionPropagationBehavior.REQUIRED, autoCommit);
    }

    public DefaultTransactionDefinition(TransactionIsolationLevel isolationLevel,
                                        TransactionPropagationBehavior propagationBehavior) {
        this(isolationLevel, propagationBehavior, false);
    }

    public DefaultTransactionDefinition(TransactionIsolationLevel isolationLevel,
                                        TransactionPropagationBehavior propagationBehavior, boolean autoCommit) {
        super(isolationLevel, propagationBehavior, autoCommit);
    }


}

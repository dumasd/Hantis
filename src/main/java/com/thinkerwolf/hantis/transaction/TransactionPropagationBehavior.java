package com.thinkerwolf.hantis.transaction;

/**
 * 事务传播行为
 *
 * @author wukai
 */
public enum TransactionPropagationBehavior {
    /**
     * 支持当前事务，如果没有事务，则新建事务 （默认）
     */
    REQUIRED(0),
    /**
     * 支持当前事务，如果没有事务，则以非事务进行
     */
    SUPPORTS(1),
    /**
     * 支持当前事务，如果没有事务，则抛出异常
     */
    MANDATORY(2),
    /**
     * 新建事务，如果当前事务存在，则挂起
     */
    REQUIRES_NEW(3),
    /**
     * 强制已非事务进行，如果当前存在事务，则挂起
     */
    NOT_SUPPORTED(4),
    /**
     * 以非事务方式执行，如果当前存在事务，则抛出异常
     */
    NEVER(5),
    /**
     * 嵌套事务
     */
    NESTED(6),;
    private int id;

    private TransactionPropagationBehavior(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}

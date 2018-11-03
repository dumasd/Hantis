package com.thinkerwolf.hantis.transaction;

import java.sql.Connection;

/**
 * 数据库隔离级别
 *
 * @author wukai
 */
public enum TransactionIsolationLevel {

    NONE(Connection.TRANSACTION_NONE),
    /**
     * 读未提交 (脏读、不可重复读、幻读会发生)
     */
    READ_UNCOMMITTED(Connection.TRANSACTION_READ_COMMITTED),
    /**
     * 读已提交 (不可重复读、幻读会发生，防止脏读)
     */
    READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),
    /**
     * 可重复读 (幻读会发生，防止脏读、不可重复读)
     */
    REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),
    /**
     * 序列化 (防止脏读、不可重复读、幻读)
     */
    SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE),;

    private int id;

    private TransactionIsolationLevel(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}

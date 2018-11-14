package com.thinkerwolf.hantis.executor;

import com.thinkerwolf.hantis.common.Param;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO 优化
 */
public class BatchExecutor extends AbstractExecutor {

    private Map<String, PreparedStatement> batchStatments = new ConcurrentHashMap<>();

    @Override
    public ExecutorType getType() {
        return ExecutorType.BATCH;
    }

    @Override
    protected int doUpdate(String sql, List<Param> params, Connection connection) {
        return execute(() -> {
            try {

                PreparedStatement ps = batchStatments.get(sql);
                if (ps == null) {
                    ps = new PreparedStatementBuilderImpl(connection, sql, params).build();
                    batchStatments.put(sql, ps);
                } else {
                    new PreparedStatementBuilderImpl(ps, params).build();
                }
                ps.addBatch();
                return DEFAULT_BATCH_UPDATE_RESULT;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void doBeforeCommit() throws SQLException {
        try {
            for (PreparedStatement ps : batchStatments.values()) {
                ps.executeBatch();
            }
        } catch (SQLException e) {
            throw new ExecutorException("Execute batch", e);
        } finally {
            batchStatments.clear();
        }
    }

    @Override
    public void doBeforeRollback() throws SQLException {
        try {
            for (PreparedStatement ps : batchStatments.values()) {
                ps.clearBatch();
            }
        } catch (SQLException e) {
            throw new ExecutorException("Clear batch", e);
        } finally {
            batchStatments.clear();
        }
    }

    @Override
    protected void doClose() {
        super.doClose();
        closeStatment();
    }

    @Override
    public void doAfterCommit() throws SQLException {
        super.doAfterCommit();
        closeStatment();
    }

    @Override
    public void doAfterRollback() throws SQLException {
        super.doAfterRollback();
        closeStatment();
    }

    private void closeStatment() {
        if (logger.isDebugEnabled()) {
            logger.debug("Close Batch executor statments.");
        }
        for (PreparedStatement ps : batchStatments.values()) {
            try {
                ps.clearBatch();
                ps.close();
            } catch (SQLException e) {
            }
        }
        batchStatments.clear();
    }
}

package com.thinkerwolf.hantis.executor;

import com.thinkerwolf.hantis.common.Param;
import com.thinkerwolf.hantis.sql.Sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO 优化
 */
public class BatchExecutor extends AbstractExecutor {

    private Map<String, PreparedStatement> batchStatments = new ConcurrentHashMap<>();

    private Map<String, BatchResult> batchResults = new ConcurrentHashMap<>();

    @Override
    public ExecutorType getType() {
        return ExecutorType.BATCH;
    }

    @Override
    protected int doUpdate(Sql sql, Connection connection) {
        return execute(() -> {
            String sqlString = sql.getSql();
            List<Param> params = sql.getParams();
            try {
                PreparedStatement ps = batchStatments.get(sqlString);
                if (ps == null) {
                    ps = new PreparedStatementBuilderImpl(connection, sqlString, params).build();
                    batchStatments.put(sqlString, ps);
                    BatchResult batchResult = new BatchResult(sqlString, params);
                    batchResults.put(sqlString, batchResult);
                }
                ps.addBatch();
                return DEFAULT_BATCH_UPDATE_RESULT;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    protected void doClose() {
        super.doClose();
        closeStatment();
    }

    @Override
    protected List<BatchResult> doFlushStatments(boolean isRollback) {
        List<BatchResult> results = new ArrayList <>();
        try {
            if (!isRollback) {
                for (String sql : batchStatments.keySet()) {
                    PreparedStatement ps = batchStatments.get(sql);
                    BatchResult result = batchResults.get(sql);
                    result.setUpdateCounts(ps.executeBatch());
                    results.add(result);
                }
            }
            return results;
        } catch (SQLException e) {
            throw new ExecutorException("Execute batch", e);
        } finally {
            closeStatment();
        }
    }

    private void closeStatment() {
        if (logger.isDebugEnabled()) {
            logger.debug("Close BatchExecutor statments.");
        }
        for (PreparedStatement ps : batchStatments.values()) {
            try {
                ps.clearBatch();
                ps.close();
            } catch (SQLException e) {
            }
        }
        batchStatments.clear();
        batchResults.clear();
    }


}

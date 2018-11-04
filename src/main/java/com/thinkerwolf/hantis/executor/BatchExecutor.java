package com.thinkerwolf.hantis.executor;

import com.thinkerwolf.hantis.common.Param;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO 优化
 */
public class BatchExecutor extends AbstractExecutor {

    private Map<Connection, Map<String, PreparedStatement>> batchStatments = new ConcurrentHashMap<>();

    @Override
    public ExecutorType getType() {
        return ExecutorType.BATCH;
    }

    @Override
    protected int doUpdate(String sql, List<Param> params, Connection connection) {
        return execute(() -> {
            try {

                Map<String, PreparedStatement> map = batchStatments.get(connection);
                if (map == null) {
                    map = new HashMap<>();
                    batchStatments.put(connection, map);
                }
                PreparedStatement ps = map.get(sql);
                if (ps == null) {
                    ps = new PreparedStatementBuilderImpl(connection, sql, params).build();
                    map.put(sql, ps);
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
            Map<String, PreparedStatement> pss = batchStatments.get(getConnection());
            for (PreparedStatement ps : pss.values()) {
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
            Map<String, PreparedStatement> pss = batchStatments.get(getConnection());
            for (PreparedStatement ps : pss.values()) {
                ps.clearBatch();
            }
        } catch (SQLException e) {
            throw new ExecutorException("Clear batch", e);
        } finally {
            batchStatments.clear();
        }
        batchStatments.clear();
    }
}

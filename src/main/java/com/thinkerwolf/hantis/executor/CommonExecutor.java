package com.thinkerwolf.hantis.executor;


import com.thinkerwolf.hantis.common.Param;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class CommonExecutor extends AbstractExecutor {

    @Override
    protected int doUpdate(String sql, List<Param> params, Connection connection) {
        PreparedStatementBuilder builder = new PreparedStatementBuilderImpl(connection, sql, params);
        return execute(() -> {
            try {
                PreparedStatement ps = builder.build();
                return ps.executeUpdate();
            } catch (Throwable e) {
                throw new ExecutorException("Update error", e);
            }
        });
    }

    @Override
    public ExecutorType getType() {
        return ExecutorType.COMMON;
    }


}

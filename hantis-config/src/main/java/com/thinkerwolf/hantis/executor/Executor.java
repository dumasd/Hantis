package com.thinkerwolf.hantis.executor;

import com.thinkerwolf.hantis.common.Param;
import com.thinkerwolf.hantis.session.Configuration;
import com.thinkerwolf.hantis.session.SessionFactoryBuilder;
import com.thinkerwolf.hantis.sql.Sql;

import javax.sql.CommonDataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * SQL Executor
 */
public interface Executor {


    int DEFAULT_BATCH_UPDATE_RESULT = Integer.MIN_VALUE + 1024;
    <T> List<T> queryForList(Sql sql, Class<T> clazz);

    <T> T queryForOne(Sql sql, Class<T> clazz);
    
    List<Map<String, Object>> queryForList(Sql sql);
    
    Map<String, Object> queryForOne(Sql sql);
    
    <T> List<T> queryForList(Sql sql, ResultSetListHandler<T> listHandler);

    int update(Sql sql);

    boolean execute(Sql sql);

    <T> T execute(StatementExecuteCallback<T> callback);

    ExecutorType getType();

    void setDataSource(CommonDataSource dataSource);

    void setSessionFactoryBuilder(SessionFactoryBuilder sessionFactoryBuilder);
    
    void close();

    List<BatchResult> flushStatments(boolean isRollback);

}

package com.thinkerwolf.hantis.executor;

import com.thinkerwolf.hantis.common.Param;
import com.thinkerwolf.hantis.session.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * sql 执行器
 */
public interface Executor {


    int DEFAULT_BATCH_UPDATE_RESULT = Integer.MIN_VALUE + 1024;
    <T> List<T> queryForList(String sql, Class<T> clazz);
    
    <T> List<T> queryForList(String sql, List<Param> params, Class<T> clazz);

    <T> T queryForOne(String sql, List<Param> params, Class<T> clazz);
    
    List<Map<String, Object>> queryForList(String sql);
    
    List<Map<String, Object>> queryForList(String sql, List<Param> params);
    
    Map<String, Object> queryForOne(String sql, List<Param> params);
    
    <T> List<T> queryForList(String sql, List<Param> params, ResultSetListHandler<T> listHandler);

    int update(String sql, List<Param> params);

    <T> T execute(StatementExecuteCallback<T> callback);

    void doBeforeCommit() throws SQLException;

    void doBeforeRollback() throws SQLException;

    void doAfterCommit() throws SQLException;

    void doAfterRollback() throws SQLException;

    ExecutorType getType();

    void setDataSource(DataSource dataSource);

    void setConfiguration(Configuration configuration);

    void close();

}

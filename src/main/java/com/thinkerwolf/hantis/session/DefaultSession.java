package com.thinkerwolf.hantis.session;

import com.thinkerwolf.hantis.executor.SqlExecutor;
import com.thinkerwolf.hantis.sql.SelectSqlNode;
import com.thinkerwolf.hantis.sql.Sql;
import com.thinkerwolf.hantis.transaction.Transaction;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DefaultSession implements Session {

    private Transaction transaction;

    private DataSource dataSource;

    private SqlExecutor executor;

    private SessionFactoryBuilder builder;

    public DefaultSession(Transaction transaction, SessionFactoryBuilder builder) {
        this.transaction = transaction;
        this.builder = builder;
        this.dataSource = builder.getDataSource();
        this.executor = new SqlExecutor();
        this.executor.setDataSource(dataSource);
        this.executor.setConfiguration(builder.getConfiguration());
    }

    @Override
    public void close() {
        try {
            transaction.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void commit() {
        try {
            transaction.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rollback() {
        try {
            transaction.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> List<T> selectList(String mapping) {
        return selectList(mapping, null);
    }

    @Override
    public <T> List<T> selectList(String mapping, Object parameter) {
        SelectSqlNode ssn = builder.getSelectSqlNode(mapping);
        Sql sql = new Sql(parameter);
        try {
            ssn.generate(sql);
        } catch (Throwable throwable) {
            return null;
        }
        return executor.queryForList(sql.getSql(), sql.getParams(), ssn.getReturnType());
    }

    @Override
    public <T> T selectOne(String mapping, Object parameter) {
        return null;
    }

    @Override
    public <T> T selectOne(String mapping) {
        return null;
    }

    @Override
    public <K, V> Map<K, V> selectMap(String mapping, Object parameter) {
        return null;
    }

    @Override
    public <K, V> Map<K, V> selectMap(String mapping) {
        return null;
    }

    @Override
    public int update(String mapping, Object parameter) {
        return 0;
    }

    @Override
    public int update(String mapping) {
        return 0;
    }
}

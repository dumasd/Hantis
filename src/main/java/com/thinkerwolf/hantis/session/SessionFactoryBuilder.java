package com.thinkerwolf.hantis.session;

import com.thinkerwolf.hantis.executor.Executor;
import com.thinkerwolf.hantis.sql.SelectSqlNode;
import com.thinkerwolf.hantis.sql.SqlNode;
import com.thinkerwolf.hantis.sql.UpdateSqlNode;
import com.thinkerwolf.hantis.transaction.TransactionManager;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author wukai
 */
public final class SessionFactoryBuilder {

    private String id;
    private DataSource dataSource;
    private Map<String, SqlNode> sqlNodeMap;
    private TransactionManager transactionManager;
    private Configuration configuration;
    private Executor executor;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, SqlNode> getSqlNodeMap() {
        return sqlNodeMap;
    }

    public void setSqlNodeMap(Map<String, SqlNode> sqlNodeMap) {
        this.sqlNodeMap = sqlNodeMap;
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public SelectSqlNode getSelectSqlNode(String mapping) {
        SqlNode sqlNode = getSqlNode(mapping);
        if (!(sqlNode instanceof SelectSqlNode)) {
            throw new RuntimeException("[" + mapping + "] isn't a select sql node");
        }
        return (SelectSqlNode) sqlNode;
    }

    public UpdateSqlNode getUpdateSqlNode(String mapping) {
        SqlNode sqlNode = getSqlNode(mapping);
        if (!(sqlNode instanceof UpdateSqlNode)) {
            throw new RuntimeException("[" + mapping + "] isn't a select sql node");
        }
        return (UpdateSqlNode) sqlNode;
    }

    public SqlNode getSqlNode(String mapping) {
        SqlNode sqlNode = sqlNodeMap.get(mapping);
        if (sqlNode == null) {
            throw new RuntimeException("[" + mapping + "] isn't found");
        }
        return sqlNode;
    }

    /**
     * 创建新的SessionFactory
     */
    public SessionFactory build() {
        SessionFactory sessionFactory = new DefaultSessionFactory(this);
        return sessionFactory;
    }


}

package com.thinkerwolf.hantis.session;

import com.thinkerwolf.hantis.executor.ExecutorType;
import com.thinkerwolf.hantis.orm.TableEntity;
import com.thinkerwolf.hantis.sql.SqlNode;

import javax.sql.CommonDataSource;
import java.util.Map;

/**
 * @author wukai
 */
public final class SessionFactoryBuilder {

    private String id;
    private CommonDataSource dataSource;
    private Map<String, SqlNode> sqlNodeMap;
    private Configuration configuration;
    private ExecutorType executorType;
    private Map<String, TableEntity<?>> entityMap;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CommonDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(CommonDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setSqlNodeMap(Map<String, SqlNode> sqlNodeMap) {
        this.sqlNodeMap = sqlNodeMap;
    }

    public Map<String, TableEntity<?>> getEntityMap() {
        return entityMap;
    }

    public void setEntityMap(Map<String, TableEntity<?>> entityMap) {
        this.entityMap = entityMap;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
    
	public ExecutorType getExecutorType() {
		return executorType;
	}

	public void setExecutorType(ExecutorType executorType) {
		this.executorType = executorType;
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

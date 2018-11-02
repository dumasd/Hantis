package com.thinkerwolf.hantis.session;

import java.util.Map;

import javax.sql.DataSource;

import com.thinkerwolf.hantis.sql.SelectSqlNode;
import com.thinkerwolf.hantis.sql.SqlNode;
import com.thinkerwolf.hantis.sql.UpdateSqlNode;
import com.thinkerwolf.hantis.transaction.TransactionManager;

/**
 * 
 * @author wukai
 * 
 */
public final class SessionFactoryBuilder {

	private String id;
	private DataSource dataSource;
	private Map<String, SqlNode> sqlNodeMap;
	private TransactionManager transactionManager;
	private Configuration configuration;

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

	public SelectSqlNode getSelectSqlNode(String mapping) {
		SqlNode sqlNode = sqlNodeMap.get(mapping);
		if (sqlNode == null) {
			throw new RuntimeException("[" + mapping + "] isn't found");
		}
		if (!(sqlNode instanceof SelectSqlNode)) {
			throw new RuntimeException("[" + mapping + "] isn't a select sql node");
		}
		return (SelectSqlNode) sqlNode;
	}

	public UpdateSqlNode getUpdateSqlNode(String mapping) {
		SqlNode sqlNode = sqlNodeMap.get(mapping);
		if (sqlNode == null) {
			throw new RuntimeException("[" + mapping + "] isn't found");
		}
		if (!(sqlNode instanceof UpdateSqlNode)) {
			throw new RuntimeException("[" + mapping + "] isn't a select sql node");
		}
		return (UpdateSqlNode) sqlNode;
	}

	/**
	 * 创建新的SessionFactory
	 */
	public SessionFactory build() {
		SessionFactory sessionFactory = new DefaultSessionFactory(this);
		return sessionFactory;
	}

}

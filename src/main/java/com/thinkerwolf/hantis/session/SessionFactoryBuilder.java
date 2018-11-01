package com.thinkerwolf.hantis.session;

import java.util.Map;

import javax.sql.DataSource;

import com.thinkerwolf.hantis.sql.SqlNode;
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

    /**
	 * 创建新的SessionFactory
	 */
	public SessionFactory build() {
        SessionFactory sessionFactory = new DefaultSessionFactory();



		return sessionFactory;
	}

}

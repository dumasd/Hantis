package com.thinkerwolf.hantis.session;

import java.util.Map;

import javax.sql.DataSource;

import com.thinkerwolf.hantis.sql.SqlNode;

/**
 * 
 * @author wukai
 * 
 */
public final class SessionFactoryBuilder {

	private String id;
	private DataSource dataSource;
	private Map<String, SqlNode> sqlNodeMap;

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

	/**
	 * 创建新的SessionFactory
	 */
	public SessionFactory build() {

		return null;
	}

}

package com.thinkerwolf.hantis.transaction.jta.atomikos;

import com.thinkerwolf.hantis.common.Disposable;
import com.thinkerwolf.hantis.common.Initializing;

import java.sql.Connection;
import java.sql.SQLException;

public class AtomikosDataSourceBean extends com.atomikos.jdbc.AtomikosDataSourceBean implements Initializing, Disposable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8039889604601505028L;

	private String resourceName;

	public String getResourceName() {
		return this.resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
		setUniqueResourceName(resourceName);
	}

    @Override
    public void afterPropertiesSet() throws Throwable {
        setMinPoolSize(2);
        setMaxPoolSize(4);
        //setConcurrentConnectionValidation(false);
        init();
    }

    @Override
    public void destory() throws Exception {
        close();
    }

    @Override
    public Connection getConnection() throws SQLException {
        int t = 1;
        t++;

        return super.getConnection();

    }
}

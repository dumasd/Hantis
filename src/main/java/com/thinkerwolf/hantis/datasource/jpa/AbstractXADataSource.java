package com.thinkerwolf.hantis.datasource.jpa;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.XAConnection;
import javax.sql.XADataSource;

import com.thinkerwolf.hantis.common.util.PropertyUtils;
import com.thinkerwolf.hantis.datasource.AbstractDataSource;

public abstract class AbstractXADataSource extends AbstractDataSource implements XADataSource {
	protected String xaDataSourceClassName;

	protected XADataSource xaDataSource;

	protected Properties xaProperties;

	public String getXaDataSourceClassName() {
		return xaDataSourceClassName;
	}

	public void setXaDataSourceClassName(String xaDataSourceClassName) {
		this.xaDataSourceClassName = xaDataSourceClassName;
	}

	public XADataSource getXaDataSource() {
		return xaDataSource;
	}

	public void setXaDataSource(XADataSource xaDataSource) {
		this.xaDataSource = xaDataSource;
	}

	public Properties getXaProperties() {
		return xaProperties;
	}

	public void setXaProperties(Properties xaProperties) {
		this.xaProperties = xaProperties;
	}

	@Override
	public XAConnection getXAConnection() throws SQLException {
		init();
		return doGetXAConnection();
	}

	private void init() throws SQLException {
		initializeDriver();
		if (xaDataSource == null) {
			synchronized (this) {
				if (xaDataSource == null && xaDataSourceClassName != null && xaDataSourceClassName.length() > 0) {
					try {
						Class<?> clazz = Class.forName(xaDataSourceClassName);
						xaDataSource = (XADataSource) clazz.newInstance();
						// 为xaDataSource设置属性
						if (xaProperties == null) {
							xaProperties = new Properties();
						}
						PropertyUtils.setProperties(xaDataSource, xaProperties);
					} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
						throw new SQLException(e);
					}
				}
			}
		}
	}

	@Override
	public XAConnection getXAConnection(String user, String password) throws SQLException {
		throw new UnsupportedOperationException("getXAConnection with user and password");
	}

	protected abstract XAConnection doGetXAConnection() throws SQLException;

	@Override
	protected Connection doGetConnection() throws SQLException {
		return getXAConnection().getConnection();
	}

	@Override
	protected Connection doGetConnection(String username, String password) throws SQLException {
		return getXAConnection().getConnection();
	}

}

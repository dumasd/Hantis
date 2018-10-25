package com.thinkerwolf.hantis.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.sql.DataSource;

public abstract class AbstractDataSource implements DataSource {

	private static Map<String, Driver> registriedDrivers = new ConcurrentHashMap<>();

	protected String driver;

	// protected boolean autoCommit;

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		throw new UnsupportedOperationException("getLogWriter");
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		throw new UnsupportedOperationException("setLogWriter");
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		throw new UnsupportedOperationException("setLogWriter");
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		if (iface.isInstance(this)) {
			return (T) this;
		}
		throw new SQLException(
				"DataSource of type [" + getClass().getName() + "] cannot be unwrapped as [" + iface.getName() + "]");
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return iface.isInstance(this);
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	// public boolean isAutoCommit() {
	// return autoCommit;
	// }
	//
	// public void setAutoCommit(boolean autoCommit) {
	// this.autoCommit = autoCommit;
	// }

	protected synchronized void initializeDriver() throws SQLException {
		Driver registriedDriver = registriedDrivers.get(driver);
		if (registriedDriver == null) {
			try {
				Class<?> driverClass = Class.forName(driver);
				Driver dri = (Driver) driverClass.newInstance();
				registriedDrivers.put(driver, dri);
				DriverManager.registerDriver(dri);
			} catch (Exception e) {
				throw new SQLException(e);
			}
		}
	}

	@Override
	public Connection getConnection() throws SQLException {
		initializeDriver();
		return doGetConnection();
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		initializeDriver();
		return doGetConnection(username, password);
	}

	protected abstract Connection doGetConnection() throws SQLException;

	protected abstract Connection doGetConnection(String username, String password) throws SQLException;
}

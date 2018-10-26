package com.thinkerwolf.hantis.datasource.jdbc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

class ProxyConnection implements InvocationHandler {

	private Connection realConnection;

	private Connection proxyConnection;

	private ConnectionPool pool;

	private static final Class<?>[] INTERFACES = new Class<?>[] { Connection.class };

	private static final String CLOSE = "close";

	public ProxyConnection(ConnectionPool pool, Connection realConnection) {
		this.pool = pool;
		this.realConnection = realConnection;
		this.proxyConnection = (Connection) Proxy.newProxyInstance(getClass().getClassLoader(), INTERFACES, this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String name = method.getName();
		if (CLOSE.equals(name)) {
			pool.retureObject(this);
			return null;
		} else {
			return method.invoke(realConnection, args);
		}
	}

	public Connection getConnection() {
		return proxyConnection;
	}

	public Connection getRealConnection() {
		return realConnection;
	}

	/**
	 * 检查Connection 能否使用
	 * 
	 * @return
	 */
	public boolean pingConnection() {
		Connection realConn = this.realConnection;
		try {
			if (realConn.isClosed()) {
				return false;
			}
		} catch (SQLException e) {
			return false;
		}

		boolean success = false;
		try {
			success = realConn.isValid(500);
		} catch (SQLException e) {
		}
		return success;
	}

}

package com.thinkerwolf.hantis.datasource.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

import com.thinkerwolf.hantis.datasource.GenericObjectPool;
import com.thinkerwolf.hantis.datasource.PoolableObjectFactory;

/**
 * Connection Pool
 * 
 * @author wukai
 *
 */
class ConnectionPool extends GenericObjectPool<ProxyConnection> {

	public ConnectionPool(int minNum, int maxNum, PoolableObjectFactory<ProxyConnection> objectFactory) {
		this(minNum, maxNum, 1, objectFactory);
	}

	public ConnectionPool(int minNum, int maxNum, int maxErrorNum,
			PoolableObjectFactory<ProxyConnection> objectFactory) {
		super(minNum, maxNum, maxErrorNum, objectFactory);
	}
	// private ProxyConnection createProxyConnection() throws SQLException {
	// Connection conn = DriverManager.getConnection(url, username, password);
	// ProxyConnection proxyConnection = new ProxyConnection(this, conn);
	// return proxyConnection;
	// }

	@Override
	public boolean checkObj(ProxyConnection t) throws Exception {
		return t.pingConnection();
	}

	@Override
	public synchronized void doClose() {
		for (Iterator<ProxyConnection> iter = freeObjs.iterator(); iter.hasNext();) {
			ProxyConnection conn = iter.next();
			iter.remove();
			Connection realConn = conn.getRealConnection();
			try {
				if (!realConn.getAutoCommit()) {
					realConn.rollback();
				}
				realConn.close();
			} catch (SQLException e) {
				// ingore
			}
		}
		for (Iterator<ProxyConnection> iter = activeObjs.iterator(); iter.hasNext();) {
			ProxyConnection conn = iter.next();
			iter.remove();
			Connection realConn = conn.getRealConnection();
			try {
				if (!realConn.getAutoCommit()) {
					realConn.rollback();
				}
				realConn.close();
			} catch (SQLException e) {
				// ingore
			}
		}
	}
}

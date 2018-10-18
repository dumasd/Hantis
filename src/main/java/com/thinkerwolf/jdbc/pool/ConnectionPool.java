package com.thinkerwolf.jdbc.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Connection Pool
 * 
 * @author wukai
 *
 */
public class ConnectionPool {
	/** pool缓存 */
	private static Map<String, ConnectionPool> poolCache = new ConcurrentHashMap<>();

	private int minConn;
	private int maxConn;
	private int maxErrorConn;
	private String url;
	private String username;
	private String password;
	private String poolName;

	private AtomicInteger errConn = new AtomicInteger();
	private volatile boolean close;

	private List<ProxyConnection> freeConns = new ArrayList<>();
	private List<ProxyConnection> activeConns = new ArrayList<>();

	public static ConnectionPool getOrCreatePool(String poolName, Properties props)
			throws ClassNotFoundException, SQLException {
		ConnectionPool pool = poolCache.get(poolName);
		if (pool != null) {
			return pool;
		}
		synchronized (poolCache) {
			if (poolCache.get(poolName) == null) {
				pool = new ConnectionPool();
				poolCache.putIfAbsent(poolName, pool);
				pool.poolName = poolName;
				pool.minConn = Integer.parseInt(props.getProperty("minConn", "2"));
				pool.maxConn = Integer.parseInt(props.getProperty("maxConn", "10"));
				pool.maxErrorConn = Integer.parseInt(props.getProperty("maxErrorConn", "2"));
				pool.url = props.getProperty("url");
				// pool.driver = props.getProperty("driver");
				pool.username = props.getProperty("username");
				pool.password = props.getProperty("password");
				pool.init();
			}
		}
		return pool;
	}

	private void init() throws SQLException {
		for (int i = 0; i < minConn; i++) {
			ProxyConnection conn = createProxyConnection();
			freeConns.add(conn);
		}
	}

	private ProxyConnection createProxyConnection() throws SQLException {
		Connection conn = DriverManager.getConnection(url, username, password);
		ProxyConnection proxyConnection = new ProxyConnection(this, conn);
		return proxyConnection;
	}

	private ProxyConnection getProxyConnection() throws SQLException {
		ProxyConnection conn = null;
		if (!freeConns.isEmpty()) {
			conn = freeConns.remove(0);
		} else {
			int curSize = activeConns.size();
			if (curSize < maxConn) {
				try {
					conn = createProxyConnection();
				} catch (SQLException e) {
					// try again
					if (errConn.incrementAndGet() < maxErrorConn) {
						conn = getProxyConnection();
					}
				}
			} else {
				// try again
				if (errConn.incrementAndGet() < maxErrorConn) {
					conn = getProxyConnection();
				}
			}
		}
		if (conn != null) {
			if (!conn.pingConnection()) {
				throw new SQLException("");
			}
			return conn;
		}
		throw new SQLException("");
	}

	public synchronized void pushProxyConnection(ProxyConnection conn) {
		activeConns.remove(conn);
		freeConns.add(conn);
	}

	public synchronized Connection getConnection() throws SQLException {
		errConn.set(0);
		return getProxyConnection().getProxyConnection();
	}

	// public static enum PoolType {
	// FIFO, FILO,
	// }

	public String getPoolName() {
		return poolName;
	}

	public synchronized void close() {
		if (close) {
			return;
		}
		close = true;
		for (Iterator<ProxyConnection> iter = freeConns.iterator(); iter.hasNext();) {
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
		for (Iterator<ProxyConnection> iter = activeConns.iterator(); iter.hasNext();) {
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

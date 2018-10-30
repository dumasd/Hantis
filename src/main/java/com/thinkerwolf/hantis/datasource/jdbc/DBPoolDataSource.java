package com.thinkerwolf.hantis.datasource.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.thinkerwolf.hantis.common.pool.PoolableObjectFactory;

/**
 * 缓存数据源
 * 
 * @author wukai
 *
 */
public class DBPoolDataSource extends AbstractJdbcDataSource {

	private int minConn = 2;

	private int maxConn = 20;

	private String poolName = "pooled";

	private ConnectionPool pool;

	public DBPoolDataSource() {
	}

	@Override
	protected Connection doGetConnection() throws SQLException {
		if (pool == null) {
			synchronized (this) {
				if (pool == null) {
					pool = new ConnectionPool(minConn, maxConn, new PoolableObjectFactory<ProxyConnection>() {
						@Override
						public ProxyConnection newObject() throws Exception {
							Connection conn = DriverManager.getConnection(url, username, password);
							ProxyConnection proxyConnection = new ProxyConnection(pool, conn);
							return proxyConnection;
						}
					});
				}
			}
		}
		try {
			return pool.borrowObject().getConnection();
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

	@Override
	protected Connection doGetConnection(String username, String password) throws SQLException {
		throw new UnsupportedOperationException("");
	}

	/*
	 * private Properties createProps(String username, String password) {
	 * Properties props = new Properties(); props.setProperty("driver", driver);
	 * props.setProperty("username", username); props.setProperty("password",
	 * password); props.setProperty("url", url); props.setProperty("minConn",
	 * String.valueOf(minConn)); props.setProperty("maxConn",
	 * String.valueOf(maxConn)); props.setProperty("poolName", poolName); return
	 * props; }
	 */

	public int getMinConn() {
		return minConn;
	}

	public void setMinConn(int minConn) {
		this.minConn = minConn;
	}

	public int getMaxConn() {
		return maxConn;
	}

	public void setMaxConn(int maxConn) {
		this.maxConn = maxConn;
	}

	public String getPoolName() {
		return poolName;
	}

	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}

	@Override
	protected void finalize() throws Throwable {
		// 被回收时强制将连接池关闭
		forceClose();
		super.finalize();
	}

	private void forceClose() {
		if (pool != null) {
			try {
				pool.close();
			} catch (Exception e) {
				// ingore
			}
		}
	}

}

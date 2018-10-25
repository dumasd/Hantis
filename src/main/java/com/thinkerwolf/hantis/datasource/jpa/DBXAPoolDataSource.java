package com.thinkerwolf.hantis.datasource.jpa;

import java.sql.SQLException;

import javax.sql.XAConnection;

import com.thinkerwolf.hantis.datasource.PoolableObjectFactory;

/**
 * 
 * @author wukai
 *
 */
public class DBXAPoolDataSource extends AbstractXADataSource {

	private XAConnectionPool pool;

	private int minConn = 1;

	private int maxConn = 2;

	public DBXAPoolDataSource() {

	}

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

	@Override
	protected XAConnection doGetXAConnection() throws SQLException {
		return borrowFromPool();
	}

	private XAConnection borrowFromPool() throws SQLException {
		try {
			return getPool().borrowObject().getProxyXAConnection();
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

	private XAConnectionPool getPool() throws SQLException {
		if (pool != null) {
			return pool;
		}
		synchronized (this) {
			if (pool == null) {
				pool = new XAConnectionPool(minConn, maxConn, new PoolableObjectFactory<ProxyXAConnection>() {
					@Override
					public ProxyXAConnection newObject() throws Exception {
						System.out.println("newObject");
						return new ProxyXAConnection(xaDataSource.getXAConnection());
					}
				});
			}
			return pool;
		}
	}

}

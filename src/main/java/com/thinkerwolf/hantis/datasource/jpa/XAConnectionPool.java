package com.thinkerwolf.hantis.datasource.jpa;

import java.util.Iterator;

import javax.sql.XAConnection;

import com.thinkerwolf.hantis.common.pool.GenericObjectPool;
import com.thinkerwolf.hantis.common.pool.PoolableObjectFactory;

class XAConnectionPool extends GenericObjectPool<ProxyXAConnection> {

	public XAConnectionPool(int minNum, int maxNum, int maxErrorNum,
			PoolableObjectFactory<ProxyXAConnection> objectFactory) {
		super(minNum, maxNum, maxErrorNum, objectFactory);
	}

	public XAConnectionPool(int minNum, int maxNum, PoolableObjectFactory<ProxyXAConnection> objectFactory) {
		super(minNum, maxNum, objectFactory);
	}

	@Override
	protected void doClose() throws Exception {
		super.doClose();
		for (Iterator<ProxyXAConnection> iter = freeObjs.iterator(); iter.hasNext();) {
			ProxyXAConnection conn = iter.next();
			iter.remove();
			XAConnection realConn = conn.getRealXAConnection();
			realConn.close();
		}
		for (Iterator<ProxyXAConnection> iter = freeObjs.iterator(); iter.hasNext();) {
			ProxyXAConnection conn = iter.next();
			iter.remove();
			XAConnection realConn = conn.getRealXAConnection();
			realConn.close();
		}
	}

	@Override
	public boolean checkObj(ProxyXAConnection t) throws Exception {
		return super.checkObj(t);
	}

}

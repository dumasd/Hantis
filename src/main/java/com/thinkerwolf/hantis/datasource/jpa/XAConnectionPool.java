package com.thinkerwolf.hantis.datasource.jpa;

import com.thinkerwolf.hantis.datasource.GenericObjectPool;
import com.thinkerwolf.hantis.datasource.PoolableObjectFactory;

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
	}

	@Override
	public boolean checkObj(ProxyXAConnection t) throws Exception {
		return super.checkObj(t);
	}

}

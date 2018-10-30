package com.thinkerwolf.hantis.datasource.jpa;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;

import javax.sql.XAConnection;

class ProxyXAConnection implements InvocationHandler {

	private XAConnection realXAConnection;

	private XAConnection proxyXAConnection;

	private static final Class<?>[] INTERFACES = new Class<?>[] { XAConnection.class };

	public ProxyXAConnection(XAConnection realXAConnection) {
		this.realXAConnection = realXAConnection;
		this.proxyXAConnection = (XAConnection) Proxy.newProxyInstance(getClass().getClassLoader(), INTERFACES, this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if ("close".equals(method.getName())) {
			return null;
		} else {
			return method.invoke(realXAConnection, args);
		}

	}

	public XAConnection getXAConnection() {
		return proxyXAConnection;
	}

	public XAConnection getRealXAConnection() {
		return realXAConnection;
	}
	
	public void realClose() {
		try {
			realXAConnection.close();
		} catch (SQLException e) {
		}
	}

}

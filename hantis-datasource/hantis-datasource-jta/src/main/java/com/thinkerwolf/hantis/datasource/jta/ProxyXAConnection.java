package com.thinkerwolf.hantis.datasource.jta;

import javax.sql.XAConnection;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;

class ProxyXAConnection implements InvocationHandler {

    private static final Class<?>[] INTERFACES = new Class<?>[]{XAConnection.class};
    private XAConnection realXAConnection;
    private XAConnection proxyXAConnection;
    private XAConnectionPool pool;

    public ProxyXAConnection(XAConnection realXAConnection, XAConnectionPool pool) {
        this.pool = pool;
        this.realXAConnection = realXAConnection;
        this.proxyXAConnection = (XAConnection) Proxy.newProxyInstance(getClass().getClassLoader(), INTERFACES, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("close".equals(method.getName())) {
            pool.retureObject(this);
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

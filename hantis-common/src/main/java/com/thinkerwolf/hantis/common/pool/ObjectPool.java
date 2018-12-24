package com.thinkerwolf.hantis.common.pool;

public interface ObjectPool<T> {

    T borrowObject() throws Exception;

    void addObject(T obj) throws Exception;

    void retureObject(T obj) throws Exception;

    void close() throws Exception;

    void setObjectFactory(PoolableObjectFactory<T> objectFactory);

    boolean checkObj(T obj) throws Exception;

}

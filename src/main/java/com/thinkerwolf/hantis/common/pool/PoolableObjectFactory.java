package com.thinkerwolf.hantis.common.pool;

public interface PoolableObjectFactory<T> {

    T newObject() throws Exception;

}

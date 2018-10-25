package com.thinkerwolf.hantis.datasource;

public interface PoolableObjectFactory<T> {

	T newObject() throws Exception;

}

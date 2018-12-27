package com.thinkerwolf.hantis.common;

public interface Factory<T> {

    T getObject() throws Exception;

    Class<?> getObjectType();

    boolean isSingleton();

}

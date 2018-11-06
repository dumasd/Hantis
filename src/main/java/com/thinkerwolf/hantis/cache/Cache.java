package com.thinkerwolf.hantis.cache;

/**
 * Cache
 */
public interface Cache {

    void putObject(Object key, Object value);

    Object getObject(Object key);

    Object removeObject(Object key);

    int getSize();

    void clear();


}

package com.thinkerwolf.hantis.cache;

import java.io.Serializable;

/**
 * Cache
 */
public interface Cache extends Serializable {

    void putObject(Object key, Object value);

    Object getObject(Object key);

    Object removeObject(Object key);

    int getSize();

    void clear();

}

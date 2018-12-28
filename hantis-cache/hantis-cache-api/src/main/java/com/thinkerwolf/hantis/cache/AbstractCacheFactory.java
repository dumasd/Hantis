package com.thinkerwolf.hantis.cache;

import com.thinkerwolf.hantis.common.AbstractPropertiesFactory;
import com.thinkerwolf.hantis.common.util.PropertyUtils;

public abstract class AbstractCacheFactory extends AbstractPropertiesFactory<Cache> implements CacheFactory {

    @Override
    public Cache getObject() throws Exception {
        Cache cache = doGetObject();
        if (cache != null && properties != null) {
            PropertyUtils.setProperties(cache, properties);
        }
        return cache;
    }

    protected abstract Cache doGetObject() throws Exception;


}

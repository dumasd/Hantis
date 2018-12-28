package com.thinkerwolf.hantis.cache.redis;

import com.thinkerwolf.hantis.cache.AbstractCacheFactory;
import com.thinkerwolf.hantis.cache.Cache;

public class RedisCacheFactory extends AbstractCacheFactory {

    @Override
    protected Cache doGetObject() throws Exception {
        RedisCache cache = new RedisCache();
        return cache;
    }

    @Override
    public Class <?> getObjectType() {
        return RedisCache.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}

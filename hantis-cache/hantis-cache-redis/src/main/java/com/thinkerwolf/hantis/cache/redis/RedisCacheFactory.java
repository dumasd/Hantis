package com.thinkerwolf.hantis.cache.redis;

import com.thinkerwolf.hantis.cache.Cache;
import com.thinkerwolf.hantis.cache.CacheFactory;

public class RedisCacheFactory implements CacheFactory {
    @Override
    public Cache getObject() throws Exception {
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

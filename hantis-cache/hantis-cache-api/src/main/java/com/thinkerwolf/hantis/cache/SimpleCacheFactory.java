package com.thinkerwolf.hantis.cache;

public class SimpleCacheFactory implements CacheFactory {
    @Override
    public Cache getObject() throws Exception {
        return new SimpleCache();
    }

    @Override
    public Class <?> getObjectType() {
        return SimpleCache.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}

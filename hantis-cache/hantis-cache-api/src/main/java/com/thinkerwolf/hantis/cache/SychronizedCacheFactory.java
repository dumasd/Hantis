package com.thinkerwolf.hantis.cache;

public class SychronizedCacheFactory implements CacheFactory {
    @Override
    public Cache getObject() throws Exception {
        return new SychronizedCache();
    }

    @Override
    public Class <?> getObjectType() {
        return SychronizedCache.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}

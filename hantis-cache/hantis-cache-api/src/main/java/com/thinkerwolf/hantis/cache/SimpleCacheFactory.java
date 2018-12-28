package com.thinkerwolf.hantis.cache;

public class SimpleCacheFactory extends AbstractCacheFactory {

    @Override
    protected Cache doGetObject() throws Exception {
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

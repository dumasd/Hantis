package com.thinkerwolf.hantis.cache;

public class SychronizedCacheFactory extends AbstractCacheFactory {

    @Override
    protected Cache doGetObject() throws Exception {
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

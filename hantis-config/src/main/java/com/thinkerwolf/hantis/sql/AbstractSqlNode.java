package com.thinkerwolf.hantis.sql;

import com.thinkerwolf.hantis.cache.Cache;

import java.util.List;

public abstract class AbstractSqlNode implements SqlNode {

    private List<SqlNode> children;
    private Cache cache;

    @Override
    public List<SqlNode> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<SqlNode> children) {
        this.children = children;
    }

    @Override
    public void setCache(Cache cache) {
        this.cache = cache;
    }

    @Override
    public Cache getCache() {
        return cache;
    }

    @Override
    public abstract boolean generate(Sql sql) throws Throwable;

}

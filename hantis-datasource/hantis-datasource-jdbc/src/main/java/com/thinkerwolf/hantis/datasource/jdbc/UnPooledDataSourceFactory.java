package com.thinkerwolf.hantis.datasource.jdbc;

import com.thinkerwolf.hantis.datasource.CommonDataSourceFactory;

import javax.sql.CommonDataSource;

public class UnPooledDataSourceFactory implements CommonDataSourceFactory {
    @Override
    public CommonDataSource getObject() throws Exception {
        DBUnpoolDataSource ds = new DBUnpoolDataSource();
        return ds;
    }

    @Override
    public Class <?> getObjectType() {
        return DBUnpoolDataSource.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}

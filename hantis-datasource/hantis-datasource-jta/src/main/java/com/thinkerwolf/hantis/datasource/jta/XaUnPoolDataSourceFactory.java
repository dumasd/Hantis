package com.thinkerwolf.hantis.datasource.jta;

import com.thinkerwolf.hantis.datasource.CommonDataSourceFactory;

import javax.sql.CommonDataSource;

public class XaUnPoolDataSourceFactory implements CommonDataSourceFactory {
    @Override
    public CommonDataSource getObject() throws Exception {
        DBXAUnpoolDataSource ds = new DBXAUnpoolDataSource();
        return ds;
    }

    @Override
    public Class <?> getObjectType() {
        return DBXAUnpoolDataSource.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}

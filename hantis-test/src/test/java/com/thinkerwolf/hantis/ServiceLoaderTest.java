package com.thinkerwolf.hantis;

import com.thinkerwolf.hantis.common.ServiceLoader;
import com.thinkerwolf.hantis.common.log.InternalLoggerFactory;
import com.thinkerwolf.hantis.common.log.jdk.JdkLoggerFactory;
import com.thinkerwolf.hantis.datasource.CommonDataSourceFactory;
import com.thinkerwolf.hantis.transaction.TransactionManagerFactory;
import org.junit.Test;

public class ServiceLoaderTest {

    @Test
    public void serviceLoader() throws Exception {
        InternalLoggerFactory.setDefaultLoggerFactory(new JdkLoggerFactory());


        ServiceLoader.getService("JDBC", TransactionManagerFactory.class);
        ServiceLoader.getService("POOL", CommonDataSourceFactory.class);

        ServiceLoader.getService("ATOMIKOS", TransactionManagerFactory.class);
        ServiceLoader.getService("ATOMIKOS", CommonDataSourceFactory.class);

    }

}

package com.thinkerwolf.hantis;

import com.thinkerwolf.hantis.common.ServiceLoader;
import com.thinkerwolf.hantis.common.log.InternalLoggerFactory;
import com.thinkerwolf.hantis.common.log.jdk.JdkLoggerFactory;
import com.thinkerwolf.hantis.datasource.CommonDataSourceFactory;
import com.thinkerwolf.hantis.transaction.TransactionManager;
import com.thinkerwolf.hantis.transaction.TransactionManagerFactory;
import org.junit.Test;

public class ServiceLoaderTest {

    @Test
    public void serviceLoader() throws Exception{
        InternalLoggerFactory.setDefaultLoggerFactory(new JdkLoggerFactory());
        TransactionManagerFactory factory1 = ServiceLoader.getService("ATOMIKOS", TransactionManagerFactory.class);

        CommonDataSourceFactory factory2 = ServiceLoader.getService("ATOMIKOS", CommonDataSourceFactory.class);

        System.out.println(factory1.getObject());
        System.out.println(factory2.getObject());
    }

}

package com.thinkerwolf.hantis.conf;

import java.util.concurrent.atomic.AtomicInteger;

import com.thinkerwolf.hantis.common.Disposable;
import com.thinkerwolf.hantis.session.Configuration;
import com.thinkerwolf.hantis.session.SessionFactoryBuilder;

public class ShutdownHook extends Thread {

    Configuration configuration;
    
    private static final AtomicInteger id = new AtomicInteger(0);
    
    public ShutdownHook(Configuration configuration) {
        this.configuration = configuration;
        this.setName("Hantis-shutdown-" + id.getAndIncrement());
    }

    @Override
    public void run() {
        for (SessionFactoryBuilder sfb : configuration.getAllSessionFactoryBuilder()) {
            if (sfb.getCache() != null) {
                sfb.getCache().clear();
            }

            if (sfb.getDataSource() instanceof Disposable) {

                try {
                    ((Disposable) sfb.getDataSource()).destory();
                } catch (Exception e) {
                    //
                }

            }
        }
    }
}

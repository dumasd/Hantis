package com.thinkerwolf.hantis.conf;

import com.thinkerwolf.hantis.common.Disposable;
import com.thinkerwolf.hantis.session.Configuration;
import com.thinkerwolf.hantis.session.SessionFactoryBuilder;

public class ShutdownHook extends Thread {

    Configuration configuration;
    
    public ShutdownHook(Configuration configuration) {
        this.configuration = configuration;
        this.setName("Hantis-shutdown");
    }

    @Override
    public void run() {
        for (SessionFactoryBuilder sfb : configuration.getAllSessionFactoryBuilder()) {
            sfb.close();
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

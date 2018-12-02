package com.thinkerwolf.hantis.conf;

import com.thinkerwolf.hantis.common.Disposable;
import com.thinkerwolf.hantis.session.Configuration;
import com.thinkerwolf.hantis.session.SessionFactoryBuilder;

public class ShutdownHook extends Thread {

    Configuration configuration;

    public ShutdownHook(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void run() {
        for (SessionFactoryBuilder sfb : configuration.getAllSessionFactoryBuilder()) {
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

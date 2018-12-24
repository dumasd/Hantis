package com.thinkerwolf.hantis.common.log.jdk;

import com.thinkerwolf.hantis.common.log.InternalLoggerFactory;
import com.thinkerwolf.hantis.common.log.Logger;

public class JdkLoggerFactory extends InternalLoggerFactory {

    public Logger createLogger(String name) {
        return new JdkLogger(java.util.logging.Logger.getLogger(name));
    }

    public Logger createLogger(Class<?> clazz) {
        return new JdkLogger(java.util.logging.Logger.getLogger(clazz.getName()));
    }
}

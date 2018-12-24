package com.thinkerwolf.hantis.common.log.slf4j;

import org.slf4j.LoggerFactory;

import com.thinkerwolf.hantis.common.log.InternalLoggerFactory;
import com.thinkerwolf.hantis.common.log.Logger;

public class Slf4jLoggerFactory  extends InternalLoggerFactory {


    public Logger createLogger(String name) {
        return new Slf4jLogger(LoggerFactory.getLogger(name));
    }

    public Logger createLogger(Class<?> clazz) {
        return new Slf4jLogger(LoggerFactory.getLogger(clazz));
    }
}

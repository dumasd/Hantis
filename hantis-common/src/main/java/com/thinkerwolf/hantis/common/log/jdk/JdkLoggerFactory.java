package com.thinkerwolf.hantis.common.log.jdk;

import com.thinkerwolf.hantis.common.io.Resource;
import com.thinkerwolf.hantis.common.io.Resources;
import com.thinkerwolf.hantis.common.log.InternalLoggerFactory;
import com.thinkerwolf.hantis.common.log.Logger;
import com.thinkerwolf.hantis.common.util.StringUtils;

import java.util.logging.LogManager;

public class JdkLoggerFactory extends InternalLoggerFactory {

    private static final String DEFAULT_CONFIG_FILE_LOCATION = "classpath:logging.properties";

    private String configFileLocation;

    public JdkLoggerFactory() {
        this(DEFAULT_CONFIG_FILE_LOCATION);
    }

    public JdkLoggerFactory(String configFileLocation) {
        this.configFileLocation = configFileLocation;
        loadConfig();
    }

    private void loadConfig() {
        String location = configFileLocation;
        if (StringUtils.isEmpty(location)) {
            location = DEFAULT_CONFIG_FILE_LOCATION;
        }
        Resource recource = Resources.getResource(location);
        try {
            if (recource.getInputStream() != null) {
                LogManager.getLogManager().readConfiguration(recource.getInputStream());
                System.out.println("LOGGING:Load configuration from " + location);
            } else {
                System.out.println("LOGGING:Can't find file " + location + ", use default configuration.");
            }
        } catch (Exception e) {
            System.err.println("LOGGING:Load configuration error:");
            System.err.println(e);
        }
    }

    public Logger createLogger(String name) {
        return new JdkLogger(java.util.logging.Logger.getLogger(name));
    }

    public Logger createLogger(Class<?> clazz) {
        return new JdkLogger(java.util.logging.Logger.getLogger(clazz.getName()));
    }
}

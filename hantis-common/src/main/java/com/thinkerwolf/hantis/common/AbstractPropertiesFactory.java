package com.thinkerwolf.hantis.common;

import java.util.Properties;

public abstract class AbstractPropertiesFactory<T> implements PropertiesFactory<T> {
    protected Properties properties;

    @Override
    public Properties getProperties() {
        return properties;
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}

package com.thinkerwolf.hantis.common;

import java.util.Properties;

public interface PropertiesFactory<T> extends Factory<T> {

    void setProperties(Properties properties);

    Properties getProperties();
}

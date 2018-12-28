package com.thinkerwolf.hantis.cache;

import com.thinkerwolf.hantis.common.PropertiesFactory;
import com.thinkerwolf.hantis.common.SLI;

import java.util.Properties;

@SLI("REDIS")
public interface CacheFactory extends PropertiesFactory<Cache> {
    
}

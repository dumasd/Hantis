package com.thinkerwolf.hantis;

import com.thinkerwolf.hantis.cache.Cache;
import com.thinkerwolf.hantis.cache.redis.RedisCache;
import org.junit.Test;

public class CacheTest {

    @Test
    public void redisCache() {
        Cache cache = new RedisCache();
        cache.putObject("key", "value");

        System.out.println(cache.getObject("key"));

        Cache cache1 = new RedisCache();
        cache1.putObject("key", "rdcats");

        System.out.println(cache.getObject("key"));
        System.out.println(cache1.getObject("key"));
    }

}

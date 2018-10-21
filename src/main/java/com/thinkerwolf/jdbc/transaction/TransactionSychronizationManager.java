package com.thinkerwolf.jdbc.transaction;

import java.util.HashMap;
import java.util.Map;

public class TransactionSychronizationManager {


    private static ThreadLocal<Map<Object, Object>> resources  = new ThreadLocal<>();


    public static Object getResource(Object key) {
        Map<Object, Object> map = resources.get();
        if (map == null) {
            return null;
        }
       return map.get(key);
    }

    public static void bindResource(Object key, Object value) {
        Map<Object, Object> map = resources.get();
        if (map == null) {
            map = new HashMap<>();
            resources.set(map);
        }
        map.put(key, value);
    }

}

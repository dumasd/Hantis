package com.thinkerwolf.hantis.transaction;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class TransactionSychronizationManager {

    private static ThreadLocal<Map<Object, Object>> resources = new ThreadLocal<>();

    private static ThreadLocal<Set<TransactionSychronization>> sychronizes = new ThreadLocal<>();

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

    public static Object unbindResource(Object key) {
        Map<Object, Object> map = resources.get();
        if (map != null) {
            return map.remove(key);
        }
        return null;
    }

    public static void activeSychronize() {
        if (sychronizes.get() == null) {
            sychronizes.set(new LinkedHashSet<>());
        }
    }

    public static boolean isActiveSychronize() {
        return sychronizes.get() != null;
    }

    /**
     * 同步资源
     *
     * @author wukai
     */
    public interface TransactionSychronization {
        void resume();

        void suspend();
    }

}

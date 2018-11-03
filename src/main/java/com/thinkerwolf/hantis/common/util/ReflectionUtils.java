package com.thinkerwolf.hantis.common.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReflectionUtils {

    private static Map<String, String> getterMethodNameMap = new ConcurrentHashMap<>();

    private static Map<String, String> setterMethodNameMap = new ConcurrentHashMap<>();

    private static Map<String, String> isterMethodNameMap = new ConcurrentHashMap<>();


    /**
     * Get method
     *
     * @param clazz
     * @param propertyName
     * @return
     */
    public static Method getGetterMethod(final Class<?> clazz, final String propertyName) {
        if (clazz == null || propertyName == null || propertyName.length() == 0) {
            throw new IllegalArgumentException("Target object is null");
        }
        if (propertyName == null || propertyName.length() == 0) {
            throw new IllegalArgumentException("PropertyName is empty");
        }
        String getterName = getPropertyGetterName(propertyName);
        Method m = getMethod(clazz, getterName);
        if (m == null) {
            String isterName = getPropertyIsterName(propertyName);
            m = getMethod(clazz, isterName);
        }
        return m;
    }

    /**
     * Set method
     *
     * @param clazz
     * @param propertyName
     * @param parameterTypes
     * @return
     */
    public static Method getSetterMethod(final Class<?> clazz, final String propertyName, Class<?>... parameterTypes) {
        if (clazz == null || propertyName == null || propertyName.length() == 0) {
            throw new IllegalArgumentException("Target object is null");
        }
        if (propertyName == null || propertyName.length() == 0) {
            throw new IllegalArgumentException("PropertyName is empty");
        }
        String setterName = getPropertySetterName(propertyName);
        return getMethod(clazz, setterName, parameterTypes);
    }

    /**
     * Set or get method suffix
     *
     * @param propertyName
     * @return
     */
    public static String getPropertySetOrGetSuffix(String propertyName) {
        if (propertyName == null || propertyName.length() == 0) {
            throw new IllegalArgumentException("PropertyName is empty");
        }
        char f = propertyName.charAt(0);
        return Character.toUpperCase(f) + propertyName.substring(1, propertyName.length());
    }

    public static String getPropertySetterName(String propertyName) {
        if (propertyName == null || propertyName.length() == 0) {
            throw new IllegalArgumentException("PropertyName is empty");
        }
        String name = setterMethodNameMap.get(propertyName);
        if (name == null) {
            name = "set" + getPropertySetOrGetSuffix(propertyName);
            setterMethodNameMap.put(propertyName, name);
        }
        return name;
    }

    public static String getPropertyIsterName(String propertyName) {
        if (propertyName == null || propertyName.length() == 0) {
            throw new IllegalArgumentException("PropertyName is empty");
        }
        String name = isterMethodNameMap.get(propertyName);
        if (name == null) {
            name = "is" + getPropertySetOrGetSuffix(propertyName);
            isterMethodNameMap.put(propertyName, name);
        }
        return name;
    }

    public static String getPropertyGetterName(String propertyName) {
        if (propertyName == null || propertyName.length() == 0) {
            throw new IllegalArgumentException("PropertyName is empty");
        }
        String name = getterMethodNameMap.get(propertyName);
        if (name == null) {
            name = "get" + getPropertySetOrGetSuffix(propertyName);
            getterMethodNameMap.put(propertyName, name);
        }
        return name;
    }


    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class is null");
        }
        if (methodName == null || methodName.length() == 0) {
            throw new IllegalArgumentException("MethodName is empty");
        }
        try {
            return clazz.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object callMethod(Object target, Method method, Object... args) {
        try {
            return method.invoke(target, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Obtain annotation
     *
     * @param clazz
     * @param annotationClass
     * @return
     */
    public static <T extends Annotation> T getAnnotation(Class<?> clazz, Class<T> annotationClass) {
        Class<?> c = clazz;
        while (c != null && c != Object.class) {
            T res = c.getAnnotation(annotationClass);
            if (res != null) {
                return res;
            } else {
                c = c.getSuperclass();
            }
        }
        return null;
    }

}

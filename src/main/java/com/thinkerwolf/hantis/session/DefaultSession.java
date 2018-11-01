package com.thinkerwolf.hantis.session;

import com.thinkerwolf.hantis.transaction.Transaction;

import java.util.List;
import java.util.Map;

public class DefaultSession implements Session {

    private Transaction transaction;

    @Override
    public void close() {

    }

    @Override
    public void commit() {

    }

    @Override
    public void rollback() {

    }

    @Override
    public <T> List<T> selectList(String mapping) {
        return null;
    }

    @Override
    public <T> List<T> selectList(String mapping, Object parameter) {
        return null;
    }

    @Override
    public <T> T selectOne(String mapping, Object parameter) {
        return null;
    }

    @Override
    public <T> T selectOne(String mapping) {
        return null;
    }

    @Override
    public <K, V> Map<K, V> selectMap(String mapping, Object parameter) {
        return null;
    }

    @Override
    public <K, V> Map<K, V> selectMap(String mapping) {
        return null;
    }

    @Override
    public int update(String mapping, Object parameter) {
        return 0;
    }

    @Override
    public int update(String mapping) {
        return 0;
    }
}

package com.thinkerwolf.hantis.session;

import java.io.Closeable;
import java.util.List;
import java.util.Map;

import com.thinkerwolf.hantis.transaction.Transaction;
import com.thinkerwolf.hantis.transaction.TransactionDefinition;

public interface Session extends Closeable, OrmSession {
	
	Transaction beginTransaction(TransactionDefinition transactionDefinition);
	
	Transaction beginTransaction();
	
    void commit();

    void rollback();

    <T> List<T> selectList(String mapping);

    <T> List<T> selectList(String mapping, Object parameter);

    <T> T selectOne(String mapping, Object parameter);

    <T> T selectOne(String mapping);

    <K, V> Map<K, V> selectMap(String mapping, Object parameter);

    <K, V> Map<K, V> selectMap(String mapping);

    int update(String mapping, Object parameter);

    int update(String mapping);

}

package com.thinkerwolf.hantis.session;

import java.util.List;

/**
 * 对象映射Session
 * 
 * @author wukai
 *
 */
public interface OrmSession {

    <T> List<T> getList(Class<T> clazz, Object parameter);

    <T> T get(Class<T> clazz, Object parameter);

	<T> int update(T entity);

	<T> int delete(T entity);

    <T> int create(T entity);
}

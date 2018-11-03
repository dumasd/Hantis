package com.thinkerwolf.hantis.executor;

import java.sql.ResultSet;

public interface ResultSetHandler<T> {

    T process(ResultSet rs) throws Throwable;

}

package com.thinkerwolf.hantis.orm;

public interface GenerateStrategy<T> {

    T autoGenerate();

    void compareAndSet(Object value);

}

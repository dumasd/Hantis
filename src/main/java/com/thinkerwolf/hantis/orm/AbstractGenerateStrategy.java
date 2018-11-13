package com.thinkerwolf.hantis.orm;

public abstract class AbstractGenerateStrategy<T> implements GenerateStrategy<T> {

    @Override
    public void compareAndSet(Object value) {

        doCompareAndSet((T) value);

    }


    protected abstract void doCompareAndSet(T value);
}

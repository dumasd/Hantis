package com.thinkerwolf.hantis.orm;

import java.util.concurrent.atomic.AtomicInteger;

public class IntegerGenerateStrategy extends AbstractGenerateStrategy<Integer> {

    private AtomicInteger atomicId;

    public IntegerGenerateStrategy(int num) {
        this.atomicId = new AtomicInteger(num);
    }

    @Override
    public Integer autoGenerate() {
        return atomicId.incrementAndGet();
    }

    @Override
    protected void doCompareAndSet(Integer value) {
        if (value > atomicId.get()) {
            atomicId.set(value);
        }
    }
}

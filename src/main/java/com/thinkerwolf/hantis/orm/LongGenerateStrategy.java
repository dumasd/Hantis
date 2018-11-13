package com.thinkerwolf.hantis.orm;

import java.util.concurrent.atomic.AtomicLong;

public class LongGenerateStrategy extends AbstractGenerateStrategy<Long> {

    private AtomicLong al;

    public LongGenerateStrategy(long startValue) {
        this.al = new AtomicLong();
        this.al.set(startValue);
    }

    @Override
    public Long autoGenerate() {
        return al.incrementAndGet();
    }

    @Override
    protected void doCompareAndSet(Long value) {
        if (value > al.get()) {
            al.set(value);
        }
    }
}

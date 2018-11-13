package com.thinkerwolf.hantis.orm;

import com.google.common.util.concurrent.AtomicDouble;

public class FloatGenerateStrategy extends AbstractGenerateStrategy<Float> {

    private AtomicDouble ad;

    private float delta;

    public FloatGenerateStrategy(float f, float delta) {
        this.ad = new AtomicDouble(f);
        this.delta = delta;
    }

    public FloatGenerateStrategy(float f) {
        this(f, 0.1f);
    }

    @Override
    public Float autoGenerate() {
        ad.addAndGet(delta);
        return ad.floatValue();
    }

    @Override
    protected void doCompareAndSet(Float value) {
        if (value > ad.doubleValue()) {
            ad.set(value);
        }
    }
}

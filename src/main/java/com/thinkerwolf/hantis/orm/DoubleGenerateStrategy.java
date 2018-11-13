package com.thinkerwolf.hantis.orm;


import com.google.common.util.concurrent.AtomicDouble;

public class DoubleGenerateStrategy extends AbstractGenerateStrategy<Double> {


    private AtomicDouble ad;

    private double delta = 0.1d;

    public DoubleGenerateStrategy(double d, double delta) {
        this.ad = new AtomicDouble(d);
        this.delta = delta;
    }

    public DoubleGenerateStrategy(double d) {
        this(d, 0.1d);
    }

    @Override
    public Double autoGenerate() {
        return ad.addAndGet(delta);
    }

    @Override
    protected void doCompareAndSet(Double value) {
        if (value > ad.doubleValue()) {
            ad.set(value);
        }
    }
}

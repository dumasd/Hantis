package com.thinkerwolf.hantis.common;

public class StopWatch {

    private long startMillis;

    private long endMillis;


    public static StopWatch start() {
        StopWatch sw = new StopWatch();
        sw.startMillis = System.currentTimeMillis();
        return sw;
    }

    public long end() {
        endMillis = System.currentTimeMillis();
        return endMillis - startMillis;
    }


}

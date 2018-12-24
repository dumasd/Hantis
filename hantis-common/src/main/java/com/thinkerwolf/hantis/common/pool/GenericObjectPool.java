package com.thinkerwolf.hantis.common.pool;

import com.thinkerwolf.hantis.common.log.InternalLoggerFactory;
import com.thinkerwolf.hantis.common.log.Logger;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GenericObjectPool<T> implements ObjectPool<T> {

    private static Logger logger = InternalLoggerFactory.getLogger(GenericObjectPool.class);
    protected int minNum;
    protected int maxNum;
    protected int maxErrorNum;
    protected List<T> freeObjs = new ArrayList<T>();
    protected List<T> activeObjs = new ArrayList<T>();
    private PoolableObjectFactory<T> objectFactory;
    private volatile boolean close;
    private ThreadLocal<AtomicInteger> errLocal = new ThreadLocal<AtomicInteger>() {
        protected AtomicInteger initialValue() {
            return new AtomicInteger(0);
        }
    };

    public GenericObjectPool(int minNum, int maxNum) {
        this(minNum, maxNum, 1, null);
    }

    public GenericObjectPool(int minNum, int maxNum, int maxErrorNum) {
        this(minNum, maxNum, maxErrorNum, null);
    }

    public GenericObjectPool(int minNum, int maxNum, PoolableObjectFactory<T> objectFactory) {
        this(minNum, maxNum, 1, objectFactory);
    }

    public GenericObjectPool(int minNum, int maxNum, int maxErrorNum, PoolableObjectFactory<T> objectFactory) {
        this.minNum = minNum;
        this.maxNum = maxNum;
        this.maxErrorNum = maxErrorNum;
        this.objectFactory = objectFactory;
        //init();
    }

    public void init() {
        try {
            for (int i = 0; i < minNum; i++) {
                addObject(objectFactory.newObject());
            }
        } catch (Exception e) {
            throw new ObjectPoolException(e);
        }

    }

    @Override
    public synchronized T borrowObject() throws Exception {


        errLocal.get().set(0);
        return getObject();
    }

    private T getObject() throws Exception {
        T t = null;
        if (!freeObjs.isEmpty()) {
            t = freeObjs.remove(0);
            activeObjs.add(t);
        } else {
            int curSize = activeObjs.size();
            if (curSize < maxNum) {
                try {
                    t = objectFactory.newObject();
                    activeObjs.add(t);
                } catch (Exception e) {
                    // try again
                    if (errLocal.get().incrementAndGet() < maxErrorNum) {
                        t = getObject();
                    }
                }
            } else {
                // try again
                if (errLocal.get().incrementAndGet() < maxErrorNum) {
                    t = getObject();
                }
            }
        }
        if (t != null) {
            if (!checkObj(t)) {
                throw new ObjectPoolException("The object is invalid");
            }
            return t;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("borrowObject > FreeNum#" + freeObjs.size() + " ActiveNum#" + activeObjs.size());
        }
        throw new ObjectPoolException("Can't get object from the pool");
    }

    @Override
    public synchronized void addObject(T obj) throws Exception {
        int curNum = freeObjs.size() + activeObjs.size();
        if (curNum + 1 > maxNum) {
            throw new ObjectPoolException("The pool is full");
        }
        freeObjs.add(obj);
    }

    @Override
    public synchronized void retureObject(T obj) throws Exception {
        if (!activeObjs.contains(obj)) {
            throw new ObjectPoolException("Returned object is not in the activeObjects");
        }
        if (freeObjs.contains(obj)) {
            throw new ObjectPoolException("Returned object is in the freeObjs");
        }
        activeObjs.remove(obj);
        freeObjs.add(obj);
        if (logger.isDebugEnabled()) {
            logger.debug("retureObject > FreeNum#" + freeObjs.size() + " ActiveNum#" + activeObjs.size());
        }
    }

    @Override
    public synchronized void close() throws Exception {
        if (close) {
            return;
        }
        close = true;
        doClose();
        if (logger.isDebugEnabled()) {
            logger.debug("Object pool is closed");
        }
    }

    protected void doClose() throws Exception {

    }

    public PoolableObjectFactory<T> getObjectFactory() {
        return objectFactory;
    }

    @Override
    public void setObjectFactory(PoolableObjectFactory<T> objectFactory) {
        this.objectFactory = objectFactory;
    }

    @Override
    public boolean checkObj(T t) throws Exception {
        return true;
    }

    public int getMinNum() {
        return minNum;
    }

    public void setMinNum(int minNum) {
        this.minNum = minNum;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public int getMaxErrorNum() {
        return maxErrorNum;
    }

    public void setMaxErrorNum(int maxErrorNum) {
        this.maxErrorNum = maxErrorNum;
    }

}

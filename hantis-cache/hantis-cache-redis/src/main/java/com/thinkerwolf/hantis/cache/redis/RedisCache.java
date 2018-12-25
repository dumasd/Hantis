package com.thinkerwolf.hantis.cache.redis;

import com.thinkerwolf.hantis.cache.Cache;
import com.thinkerwolf.hantis.common.log.InternalLoggerFactory;
import com.thinkerwolf.hantis.common.log.Logger;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Redis cache
 */
public class RedisCache implements Cache {

    private static final Logger logger = InternalLoggerFactory.getLogger(RedisCache.class);

    private static AtomicInteger REDIS_KEY_ID = new AtomicInteger();

    private byte[] redisKey = ("hantis-redis-" + REDIS_KEY_ID.incrementAndGet()).getBytes();

    private String host = Protocol.DEFAULT_HOST;

    private int port = Protocol.DEFAULT_PORT;

    private int timeout = Protocol.DEFAULT_TIMEOUT;

    private int database = Protocol.DEFAULT_DATABASE;

    private String password;

    private String clientName;

    private int maxIdle = GenericObjectPoolConfig.DEFAULT_MAX_IDLE;

    private int minIdle = GenericObjectPoolConfig.DEFAULT_MIN_IDLE;

    private int maxTotal = GenericObjectPoolConfig.DEFAULT_MAX_TOTAL;


    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private JedisPool jedisPool;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    @Override
    public void putObject(Object key, Object value) {
        lock.writeLock().lock();
        Jedis jedis = getJedisPool().getResource();
        try {
            // 序列化
            jedis.hset(redisKey, intToByteArray(key.hashCode()), KryoUtils.serialize(value));
        } catch (Exception e) {

        } finally {
            jedis.close();
            lock.writeLock().unlock();
        }
    }

    @Override
    public Object getObject(Object key) {
        lock.readLock().lock();
        Jedis jedis = getJedisPool().getResource();
        try {
            byte[] bs = jedis.hget(redisKey, intToByteArray(key.hashCode()));
            if (bs != null) {
                // 反序列化
                return KryoUtils.deserialize(bs);
            }
            return null;
        } catch (Exception e) {
            logger.error("Redis cache get error", e);
            return null;
        } finally {
            jedis.close();
            lock.readLock().unlock();
        }
    }

    @Override
    public Object removeObject(Object key) {
        lock.writeLock().lock();
        Jedis jedis = getJedisPool().getResource();
        try {
            byte[] field = intToByteArray(key.hashCode());
            byte[] bs = jedis.hget(redisKey, field);
            jedis.hdel(redisKey, field);
            if (bs != null) {
                return KryoUtils.deserialize(bs);
            }
            return null;
        } catch (Exception e) {
            logger.error("Redis cache remove error", e);
            return null;
        } finally {
            jedis.close();
            lock.writeLock().unlock();
        }
    }

    @Override
    public int getSize() {
        lock.readLock().lock();
        Jedis jedis = getJedisPool().getResource();
        try {
            return jedis.hlen(redisKey).intValue();
        } finally {
            jedis.close();
            lock.readLock().unlock();
        }
    }

    @Override
    public void clear() {
        lock.writeLock().lock();
        Jedis jedis = getJedisPool().getResource();
        try {
            Set <byte[]> keys = jedis.hkeys(redisKey);
            if (keys != null) {
                for (byte[] bs : keys) {
                    jedis.hdel(redisKey, bs);
                }
            }
        } finally {
            jedis.close();
            lock.writeLock().unlock();
        }
    }

    public static byte[] intToByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    private JedisPool getJedisPool() {
        if (jedisPool == null) {
            synchronized (lock) {
                try {
                    if (jedisPool == null) {
                        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
                        config.setMaxIdle(maxIdle);
                        config.setMinIdle(minIdle);
                        config.setMaxTotal(maxTotal);
                        jedisPool = new JedisPool(config, host, port, timeout, password, database);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Error connect redis [host:" + host + ",port:" + port + "]", e);
                }
            }
        }
        return jedisPool;
    }

}

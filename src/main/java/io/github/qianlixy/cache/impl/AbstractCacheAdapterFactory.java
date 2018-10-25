package io.github.qianlixy.cache.impl;

import java.io.IOException;

import io.github.qianlixy.cache.CacheAdapter;
import io.github.qianlixy.cache.CacheSerializable;
import net.rubyeye.xmemcached.MemcachedClient;
import redis.clients.jedis.JedisCluster;

/**
 * 缓存适配器抽象工厂
 * @author qianli_xy@163.com
 * @since 1.0.0
 * @param <T> 缓存客户端
 */
public abstract class AbstractCacheAdapterFactory<T> {
	
	protected T client;

    public void setClient(T client) {
        this.client = client;
    }

    /**
     * 方法描述: 使用指定缓存序列化器初始化缓存适配器<br>
     * 完成时间: 2018年10月16日 上午9:43:48<br>
     * 作者: dcy<br>
     *
     * @param sacheSerializer 可以为null，使用默认缓存序列化器
     * @return 缓存适配器
     * @throws IOException 构建缓存适配器时可能会发生该异常
     */
    public final CacheAdapter buildCacheAdapter(CacheSerializable sacheSerializer) throws IOException {
        return new WrapCacheAdapter(buildCacheAdapter(), sacheSerializer);
    }

    /**
     * 构建缓存适配器
     * 
     * @return 缓存适配器
     * @throws IOException 构建缓存适配器时可能会发生该异常
     */
    protected abstract CacheAdapter buildCacheAdapter() throws IOException;

    /**
     * 使用缓存客户端构建缓存适配器工厂
     * 
     * @param cacheClient 具体的缓存客户端对象，暂仅支持Memcached
     * @return 缓存适配器工厂
     */
    public static AbstractCacheAdapterFactory<?> buildFactory(Object cacheClient) {
        if (cacheClient instanceof MemcachedClient) {
            MemcachedCacheAdapterFactory factory = new MemcachedCacheAdapterFactory();
            factory.setClient((MemcachedClient) cacheClient);
            return factory;
        } else if (cacheClient instanceof JedisCluster) {
            return new RedisAdapterFactory((JedisCluster) cacheClient);
        }
        return null;
    }

}

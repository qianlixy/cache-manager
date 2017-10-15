package io.github.qianlixy.cache.impl;

import java.io.IOException;

import io.github.qianlixy.cache.CacheAdapter;
import net.rubyeye.xmemcached.MemcachedClient;

/**
 * 缓存适配器抽象工厂
 * @author qianli_xy@163.com
 * @since 1.0.0
 * @date 2017年10月9日 下午9:30:17
 * @param <T> 缓存客户端
 */
public abstract class AbstractCacheAdapterFactory<T> {
	
	protected T client;

	public void setClient(T client) {
		this.client = client;
	}
	
	/**
	 * 构建缓存适配器
	 * @return 缓存适配器
	 * @throws IOException
	 */
	public abstract CacheAdapter buildCacheAdapter() throws IOException;
	
	/**
	 * 使用缓存客户端构建缓存适配器工厂
	 * @param cacheClient 具体的缓存客户端对象，暂仅支持Memcached
	 * @return 缓存适配器工厂
	 */
	public static AbstractCacheAdapterFactory<?> buildFactory(Object cacheClient) {
		if(cacheClient instanceof MemcachedClient) {
			MemcachedCacheAdapterFactory factory = new MemcachedCacheAdapterFactory();
			factory.setClient((MemcachedClient) cacheClient);
			return factory;
		}
		return null;
	}

}

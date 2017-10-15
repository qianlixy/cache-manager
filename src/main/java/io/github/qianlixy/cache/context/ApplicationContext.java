package io.github.qianlixy.cache.context;

import java.util.HashMap;
import java.util.Map;

import io.github.qianlixy.cache.impl.AbstractCacheAdapterFactory;

/**
 * 应用上下文信息
 * @author qianli_xy@163.com
 * @since 1.0.0
 * @date 2017年10月14日 上午10:10:50
 */
public class ApplicationContext {
	
	/** 缓存适配器工厂key */
	public static final int KEY_CACHE_ADAPTER_FACTORY = 0x1;
	
	/** 一致性时间提供者key */
	public static final int KEY_CONSISTENT_TIME_PROVIDER = 0x2;
	
	/** 全局缓存有效期key */
	public static final int KEY_DEFAULT_CACHE_TIME = 0x3;
	
	private static Map<Integer, Object> context = new HashMap<>();
	
	public static void set(Integer key, Object value) {
		context.put(key, value);
	}
	
	public static Object get(Integer key) {
		return context.get(key);
	}
	
	/**
	 * 获取缓存适配器工厂对象
	 * @return
	 */
	public static AbstractCacheAdapterFactory<?> getCacheAdaperFactory() {
		return (AbstractCacheAdapterFactory<?>) context.get(KEY_CACHE_ADAPTER_FACTORY);
	}
	
	/**
	 * 获取一致性时间提供者
	 * @return
	 */
	public static ConsistentTimeProvider getConsistentTimeProvider() {
		return (ConsistentTimeProvider) context.get(KEY_CONSISTENT_TIME_PROVIDER);
	}
	
	/**
	 * 全局缓存有效期
	 * @return
	 */
	public static int getDefaultCacheTime() {
		return (int) context.get(KEY_DEFAULT_CACHE_TIME);
	}

}

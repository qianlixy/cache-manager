package io.github.qianlixy.cache.context;

/**
 * 缓存key的生成提供接口
 * @author qianli_xy@163.com
 * @since 1.0.0
 */
public interface CacheKeyProvider {

	/**
	 * 处理缓存key并返回
	 * @param key 缓存key
	 * @return 处理后的key
	 */
	String process(String key);
	
}

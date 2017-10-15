package io.github.qianlixy.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.qianlixy.cache.exception.ConsistentTimeException;

/**
 * <p>缓存客户端接口，目的在于透明化目前存在的各种缓存客户端，如Memcached、Redis </p>
 * <p>使用该接口适配其他缓存技术 </p>
 * @author qianli_xy@163.com
 * @since 1.7
 */
public interface CacheAdapter {
	
	/**
	 * 日志打印。子类使用该日志对象打印日志，便于日志打印管理
	 */
	Logger LOGGER = LoggerFactory.getLogger(CacheAdapter.class);

	/**
	 * 设置缓存（无时间限制，最长时间根据不同缓存客户端不同）
	 * @param key 缓存key
	 * @param value 缓存值
	 * @return true - 设置成功，false - 设置不成功
	 */
	boolean set(String key, Object value);
	
	/**
	 * 设置缓存
	 * @param key 缓存key
	 * @param value 缓存值
	 * @param time 缓存有效时间，单位：分钟
	 * @return true - 设置成功，false - 设置不成功
	 */
	boolean set(String key, Object value, int time);
	
	/**
	 * 获取缓存
	 * @param key 缓存key
	 * @return 缓存值
	 */
	Object get(String key);
	
	/**
	 * 删除缓存
	 * @param key 缓存key
	 * @return true - 删除成功，false - 删除失败 
	 */
	boolean remove(String key);
	
	/**
	 * <p>一致性时间戳， 用于判断缓存失效的重要参数 </p>
	 * <p>如果应用服务器是单机模式可以使用应用服务器的系统时间。否则需要使用一致性时间戳 </p>
	 * @return 时间戳。至少精确到毫秒，否则数据库被修改后缓存不能及时失效
	 * @throws ConsistentTimeException 获取时间可能抛出异常，将不再给源方法做缓存
	 */
	long consistentTime() throws ConsistentTimeException;
	
}

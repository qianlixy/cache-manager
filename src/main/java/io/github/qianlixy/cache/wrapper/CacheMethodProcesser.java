package io.github.qianlixy.cache.wrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.qianlixy.cache.CacheConfig;
import io.github.qianlixy.cache.CacheManager;
import io.github.qianlixy.cache.exception.CacheNotExistException;
import io.github.qianlixy.cache.exception.CacheOutOfDateException;
import io.github.qianlixy.cache.exception.ConsistentTimeException;
import io.github.qianlixy.cache.exception.ExecuteSourceMethodException;

/**
 * <p>定义源方法、缓存的包装接口</p>
 * <ol>
 *   <li>获取源方法Class、名称、参数</li>
 *   <li>执行源方法并获取返回值</li>
 *   <li>缓存源数据、获取缓存</li>
 * </ol>
 * 
 * @author qianli_xy@163.com
 * @since 1.0.0
 */
public interface CacheMethodProcesser {
	
	/**
	 * 便于子类打印日志。该日志打印对象绑定到{@link CacheManager}类上，便于日志管理
	 */
	Logger LOGGER = LoggerFactory.getLogger(CacheManager.class);

	/**
	 * 获取源方法的缓存
	 * @return 源方法的缓存（如果存在并且未失效）
	 * @throws CacheOutOfDateException 缓存超时失效异常
	 * @throws CacheNotExistException 缓存不存在异常
	 */
	Object getCache() throws CacheOutOfDateException, CacheNotExistException;
	
	/**
	 * 缓存源方法的返回值。缓存时间使用默认缓存时间{@link CacheConfig#defaultCacheTime}
	 * @param cache 源方法的返回值
	 * @throws ConsistentTimeException 一致性时间异常
	 * @see #putCache(Object, int)
	 * @see CacheConfig#defaultCacheTime
	 */
	void putCache(Object cache) throws ConsistentTimeException;
	
	/**
	 * 缓存源方法的返回值
	 * @param cache 源方法的返回值
	 * @param time 缓存有效时间，单位：分钟
	 * @throws ConsistentTimeException 一致性时间异常
	 * @see #putCache(Object)
	 */
	void putCache(Object cache, int time) throws ConsistentTimeException;
	
	/**
	 * 获取源方法的实例Class
	 * @return 源方法的实例Class
	 */
	Class<?> getTargetClass();
	
	/**
	 * <p>获取源方法的名称。形如：getMethodName</p>
	 * @return 源方法的名称
	 */
	String getMethodName();
	
	/**
	 * <p>获取源方法的参数，如果该方法没有参数，返回null</p>
	 * @return 源方法参数数组
	 */
	Object[] getArgs();
	
	/**
	 * <p>获取源方法的全名称，格式：[返回值类型] [类名].[方法名]([参数类型] ...)</p>
	 * <p>形如：java.lang.String java.lang.String.toString()</p>
	 * @return 源方法的全名称
	 */
	String getFullMethodName();

	/**
	 * <p>执行源方法，一个线程只执行源方法一次，防止对数据库造成额外压力</p>
	 * @return 返回源方法执行后的返回值（如果源方法没有返回值或者源方法返回值为null将返回null）
	 * @throws 会抛出源方法执行期间可能会出现的异常
	 */
	Object doProcess() throws ExecuteSourceMethodException;
	
	/**
	 * <p>执行源方法，如果源方法为查询方法，则将查询结果缓存。</p>
	 * <p>缓存时间为全局默认时间{@link CacheConfig#defaultCacheTime}</p>
	 * @return 源方法执行结果（如果存在返回值，不存在则为null）
	 * @throws Throwable 执行源方法遇到的异常
	 * @see #doProcessAndCache(int)
	 */
	Object doProcessAndCache() throws ConsistentTimeException, ExecuteSourceMethodException;
	
	/**
	 * <p>执行源方法，如果源方法为查询方法，则将查询结果缓存。</p>
	 * @param time 缓存有效时间，单位：分钟
	 * @return 源方法执行结果（如果存在返回值，不存在则为null）
	 * @throws Throwable 执行源方法遇到的异常
	 * @see #doProcessAndCache()
	 */
	Object doProcessAndCache(int time) throws ConsistentTimeException, ExecuteSourceMethodException;

	/**
	 * 获取拦截方法的缓存有效期
	 * @return 缓存有效期，单位：分钟
	 */
	int getCacheTime();

	/**
	 * 赋值拦截方法的缓存有效期
	 * @param cacheTime 缓存有效期，单位：分钟
	 */
	void setCacheTime(int cacheTime);
	
}

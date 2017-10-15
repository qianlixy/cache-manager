package io.github.qianlixy.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>缓存管理器接口，该框架的入口 </p>
 * <p>该接口比较松散，只为统一定义日志打印、缓存管理器行为。可不实现该接口 </p>
 * @author qianli_xy@163.com
 * @since 1.7
 */
public interface CacheManager {

	/** 日志打印。子类使用该日志对象打印日志，便于日志打印管理 */
	Logger LOGGER = LoggerFactory.getLogger(CacheManager.class);
	
	/**
	 * 缓存管理器的初始化
	 */
	void init() throws Exception;

	/**
	 * 对需要被缓存源方法的拦截入口，必须使用环绕通知
	 * @param joinPoint 切入点信息
	 * @return 返回切入点方法的返回值（如果该切入点方法满足拦截条件，返回值可能为缓存）
	 * @throws Throwable 拦截过程中出现异常将抛出，不影响事务处理
	 */
	Object doCache(ProceedingJoinPoint joinPoint) throws Throwable;

}
package io.github.qianlixy.cache.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.qianlixy.cache.wrapper.CacheMethodProcesser;

/**
 * 过滤器接口
 * @author qianli_xy@163.com
 * @since 1.0.0
 * @date 2017年10月14日 下午10:58:57
 */
public interface Filter {
	
	Logger LOGGER = LoggerFactory.getLogger(ConfigurableFilter.class);
	
	/**
	 * 过滤缓存，可对缓存进行相应操作。一旦返回则终止后面过滤器的执行。
	 * @param cacheProcesser 缓存包装类，获取源方法信息、缓存操作
	 * @param filterChain 过滤器链，向下一个过滤器传递执行
	 * @return 返回缓存数据
	 * @throws Throwable 执行期间遇到的异常信息
	 */
	Object doFilter(CacheMethodProcesser cacheProcesser, Filter filterChain) throws Throwable;
	
	/**
	 * 过滤器执行的优先级。该值越小，则优先级越高
	 * @return 执行的优先级
	 */
	public abstract int getOrder();
	
}

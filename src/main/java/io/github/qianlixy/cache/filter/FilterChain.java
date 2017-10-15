package io.github.qianlixy.cache.filter;

import java.util.List;

import io.github.qianlixy.cache.exception.NoFilterDealsException;
import io.github.qianlixy.cache.wrapper.CacheMethodProcesser;

/**
 * 过滤器链
 * @author qianli_xy@163.com
 * @since 1.0.0
 * @date 2017年10月14日 下午3:21:56
 */
public class FilterChain implements Filter {
	
	private static List<Filter> filters;
	
	private int filterIndex = 0;
	
	public Object doFilter(CacheMethodProcesser cacheProcesser, 
			Filter filterChain) throws Throwable {
		//过滤器为空，执行源方法并返回
		if(null == filters || filters.size() <= 0) {
			return cacheProcesser.doProcess();
		}
		//如果过滤器链执行完毕时还没有过滤器处理并返回数据，抛出异常
		if (filterIndex >= filters.size()) {
			throw new NoFilterDealsException();
		}
		Filter filter = FilterChain.filters.get(filterIndex);
		LOGGER.debug("Current thread [{}]'s filter index is {}", Thread.currentThread().toString(), filterIndex);
		filterIndex++;
		//执行下一个过滤器
		return filter.doFilter(cacheProcesser, filterChain);
	}

	public static void setFilters(List<Filter> filters) {
		FilterChain.filters = filters;
	}

	@Override
	public int getOrder() {
		return 0;
	}

}

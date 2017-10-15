package io.github.qianlixy.cache.filter;

import java.util.Collection;

import io.github.qianlixy.cache.wrapper.CacheMethodProcesser;

/**
 * 拦截到的源方法的过滤器，
 * @author qianli_xy@163.com
 * @since 1.0.0
 * @date 2017年10月14日 下午2:56:54
 */
public class MethodMatchFilter extends ConfigurableFilter<MethodMatchFilterConfig> {
	
	private MethodMatchFilterConfig filterConfig;
	
	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public void setConfig(MethodMatchFilterConfig config) {
		this.filterConfig = config;
	}
	
	@Override
	public Object doFilter(CacheMethodProcesser cacheProcesser, 
			Filter filterChain) throws Throwable {
		//如果参数匹配集合为空，将拦截到的源方法视为匹配成功
		if (null == filterConfig) {
			return filterChain.doFilter(cacheProcesser, filterChain);
		}
		Collection<MethodMatchFilterConfigBean> configBeans = filterConfig.getConfigBeans();
		if(null == configBeans || configBeans.size() <= 0) 
			return filterChain.doFilter(cacheProcesser, filterChain);
		
		//匹配方法
		for (MethodMatchFilterConfigBean config : configBeans) {
			if(config.isFrom() ? matchFrom(config) : matchMethod(config, 
					cacheProcesser.getTargetClass().getName(), cacheProcesser.getMethodName())) {
				//修改拦截方法对应的缓存时间
				if(config.getCacheTime() >= 0)
					cacheProcesser.setCacheTime(config.getCacheTime());
				//TODO 判断是否始终保持缓存不失效
				//匹配成功
				return filterChain.doFilter(cacheProcesser, filterChain);
			}
		}
		//匹配失败，执行源方法并返回数据，终止过滤器链
		return cacheProcesser.doProcess();
	}

	/**
	 * 匹配拦截方法
	 * @param config 匹配参数
	 * @param clazz 源方法class
	 * @param methodName 源方法名称
	 * @return 是否匹配成功
	 */
	public boolean matchMethod(MethodMatchFilterConfigBean config, String className, String methodName) {
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("The matching result is [{}]. Use [{}] to match [{}]", 
					config.getPattern().matcher(className + "." + methodName + "()").find(),
					config.getPattern(), className + "." + methodName + "()");
		}
		return config.getPattern().matcher(className + "." + methodName + "()").find();
	}

	/**
	 * 匹配线程栈方法
	 * @param config 匹配参数
	 * @return 是否匹配成功
	 */
	public boolean matchFrom(MethodMatchFilterConfigBean config) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		for (int i=0; i<stackTrace.length && i<filterConfig.getLevel(); i++) {
			StackTraceElement element = stackTrace[i];
			if(matchMethod(config, element.getClassName(), element.getMethodName())) return true;
		}
		return false;
	}
	
}

package io.github.qianlixy.cache.config;

import io.github.qianlixy.cache.filter.ConfigurableFilter;
import io.github.qianlixy.cache.filter.Filter;
import io.github.qianlixy.cache.wrapper.CacheMethodProcesser;

public class RequiredConfigFilterTestClass extends ConfigurableFilter<RequiredConfigTestClass> {

	private RequiredConfigTestClass config;

	@Override
	public Object doFilter(CacheMethodProcesser cacheProcesser, Filter filterChain) throws Throwable {
		return null;
	}

	@Override
	public void setConfig(RequiredConfigTestClass config) {
		this.config = config;
	}
	
	public RequiredConfigTestClass getConfig() {
		return this.config;
	}

	@Override
	public int getOrder() {
		return 0;
	}

}

package io.github.qianlixy.cache.filter;

import io.github.qianlixy.cache.CacheConfig;

/**
 * <p>可配置过滤器接口</p>
 * <p>泛型为{@link FilterConfig}时，{@link #setConfig(FilterConfig)}将在传入配置对象不为null时调用</p>
 * <p>泛型为{@link FilterRequiredConfig}时，{@link #setConfig(FilterConfig)}会调用，并且传入的对象不能为空</p>
 * @author qianli_xy@163.com
 * @since 1.0.0
 * @date 2017年10月14日 上午9:21:10
 */
public abstract class ConfigurableFilter<FC extends FilterConfig> implements Filter {

	/**
	 * 传入该过滤器所需要类型的配置对象
	 * @param config 配置对象，通过缓存配置参数获取{@link CacheConfig#getFilterConfigs()}
	 * @see CacheConfig#getFilterConfigs()
	 */
	public abstract void setConfig(FC config);
	
}

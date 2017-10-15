package io.github.qianlixy.cache.filter;

import io.github.qianlixy.cache.CacheConfig;

/**
 * <p>过滤器必须参数配置信息接口</p>
 * <p>过滤器实现类中的泛型。使用该接口子类做泛型，实例化过滤器实现类时将校验参数不能为null</p>
 * @author qianli_xy@163.com
 * @since 1.0.0
 * @date 2017年10月14日 下午3:38:21
 * @see ConfigurableFilter
 * @see CacheConfig#getFilterConfigs()
 */
public interface FilterRequiredConfig extends FilterConfig {

}

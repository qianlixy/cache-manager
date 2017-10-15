package io.github.qianlixy.cache.filter;

import java.util.Collection;

/**
 * <p>拦截方法匹配过滤器配置</p>
 * <p>通过给定的正则表达式匹配拦截方法路径，匹配通过进行缓存处理，否则执行源方法并返回 </p>
 * @author qianli_xy@163.com
 * @since 1.0.0
 * @date 2017年10月14日 下午10:09:17
 */
public class MethodMatchFilterConfig implements FilterConfig {

	/**
	 * 匹配来源方法时，查询线程栈的最高层级。默认为10
	 */
	private int level = 10;
	
	private Collection<MethodMatchFilterConfigBean> configBeans;

	/**
	 * 匹配来源方法时，查询线程栈的最高层级
	 * @return 层级数。默认为10
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * 匹配来源方法时，查询线程栈的最高层级
	 * @param level 层级数。默认为10
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	public Collection<MethodMatchFilterConfigBean> getConfigBeans() {
		return configBeans;
	}

	/**
	 * 匹配拦截源方法路径进行缓存处理。不设置该项配置将全部拦截
	 * @param configBeans 路径匹配的参数配置集合
	 */
	public void setConfigBeans(Collection<MethodMatchFilterConfigBean> configBeans) {
		this.configBeans = configBeans;
	}

	@Override
	public String toString() {
		return "MethodMatchFilterConfig [level=" + level + ", configBeans=" + configBeans + "]";
	}
	
}

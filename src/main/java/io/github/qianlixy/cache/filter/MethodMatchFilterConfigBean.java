package io.github.qianlixy.cache.filter;

import java.util.regex.Pattern;

/**
 * 拦截方法的匹配规则
 * @author qianli_xy@163.com
 * @since 1.0.0
 * @date 2017年10月14日 下午10:12:45
 */
public class MethodMatchFilterConfigBean {

	//通过正则表达式生成的匹配对象，用于方法路径的匹配
	private Pattern pattern;
	//匹配类型。为true时匹配线程栈中的方法，为false时只匹配拦截方法
	private boolean isFrom = false;
	//匹配通过的方法的缓存有效期。默认是全局默认缓存有效期
	private int cacheTime = -1;
	//是否保持缓存有效。
	private boolean isKeep = false;

	public MethodMatchFilterConfigBean() {}

	public MethodMatchFilterConfigBean(String pattern, 
			boolean isFrom, int cacheTime, boolean isKeep) {
		this.pattern = Pattern.compile(pattern);
		this.isFrom = isFrom;
		this.cacheTime = cacheTime;
		this.isKeep = isKeep;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = Pattern.compile(pattern);
	}

	public boolean isFrom() {
		return isFrom;
	}

	public void setFrom(boolean isFrom) {
		this.isFrom = isFrom;
	}

	public int getCacheTime() {
		return cacheTime;
	}

	public void setCacheTime(int cacheTime) {
		this.cacheTime = cacheTime;
	}

	public boolean isKeep() {
		return isKeep;
	}

	public void setKeep(boolean isKeep) {
		this.isKeep = isKeep;
	}

	@Override
	public String toString() {
		return "MethodMatchFilterConfigBean [pattern=" + pattern + ", isFrom=" + isFrom + ", cacheTime=" + cacheTime
				+ ", isKeep=" + isKeep + "]";
	}
	
}

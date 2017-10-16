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
	private boolean from = false;
	//匹配通过的方法的缓存有效期。默认是全局默认缓存有效期
	private int cacheTime = -1;
	//是否保持缓存有效。
	private boolean keep = false;

	public MethodMatchFilterConfigBean() {}

	public MethodMatchFilterConfigBean(String pattern, 
			boolean from, int cacheTime, boolean keep) {
		this.pattern = Pattern.compile(pattern);
		this.from = from;
		this.cacheTime = cacheTime;
		this.keep = keep;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = Pattern.compile(pattern);
	}

	public int getCacheTime() {
		return cacheTime;
	}

	public void setCacheTime(int cacheTime) {
		this.cacheTime = cacheTime;
	}

	public boolean isFrom() {
		return from;
	}

	public void setFrom(boolean from) {
		this.from = from;
	}

	public boolean isKeep() {
		return keep;
	}

	public void setKeep(boolean keep) {
		this.keep = keep;
	}

	@Override
	public String toString() {
		return "MethodMatchFilterConfigBean [pattern=" + pattern + ", from=" + from + ", cacheTime=" + cacheTime
				+ ", keep=" + keep + "]";
	}
	
}

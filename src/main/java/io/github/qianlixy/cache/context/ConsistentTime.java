package io.github.qianlixy.cache.context;

/**
 * 获取一致性时间
 * @author qianli_xy@163.com
 * @since 1.0.0
 */
public class ConsistentTime {
	
	private long time;
	
	public ConsistentTime(long time) {
		this.time = time;
	}
	
	/**
	 * 获取当前时间戳
	 * @return 当前时间戳
	 */
	public long getTime() {
		return time;
	}
	
	public void setTime(long time) {
		this.time = time;
	}
	
}

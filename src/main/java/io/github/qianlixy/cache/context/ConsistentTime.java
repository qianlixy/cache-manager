package io.github.qianlixy.cache.context;

/**
 * <p>一致性时间戳</p>
 * <p>格式规则是：ttttttttttiiiiiiii
 *   <ol>
 *     <li>前10位tttttttttt为unix时间戳</li>
 *     <li>后8位iiiiiiii为增量。位数不够向前补0</li>
 *   </ol>
 * 
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

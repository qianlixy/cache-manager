package io.github.qianlixy.cache.context;

import io.github.qianlixy.cache.exception.ConsistentTimeException;

/**
 * 一致性时间提供接口
 * @author qianli_xy@163.com
 * @since 1.0.0
 */
public abstract class ConsistentTimeProvider {

	/**
	 * 生成一致性时间
	 * @return 一致性时间
	 * @throws ConsistentTimeException 不能获取一致性时间时将抛出该异常
	 */
	public abstract ConsistentTime newInstance() throws ConsistentTimeException;
	
}

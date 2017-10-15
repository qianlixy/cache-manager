package io.github.qianlixy.cache.context;

import io.github.qianlixy.cache.exception.ConsistentTimeException;

/**
 * 一致性时间提供接口
 * @author qianli_xy@163.com
 * @since 1.0.0
 * @date 2017年10月14日 上午9:50:03
 */
public abstract class ConsistentTimeProvider {

	/**
	 * 生成一致性时间
	 * @return 一致性时间
	 * @throws ConsistentTimeException
	 */
	public abstract ConsistentTime newInstance() throws ConsistentTimeException;
	
}

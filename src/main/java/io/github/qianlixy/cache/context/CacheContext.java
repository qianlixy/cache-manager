package io.github.qianlixy.cache.context;

import java.util.Set;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>缓存的上下文信息接口</p>
 * <p>所提供信息如下：</p>
 * <ol>
 * 	 <li>所记录的所有表对应的最后修改时间</li>
 *   <li>当前拦截方法的静态唯一标示和动态唯一标示</li>
 *   <li>当前拦截方法使用的表名称</li>
 *   <li>当前拦截方法是否是查询方法</li>
 *   <li>当前拦截方法最后的查询时间</li>
 * </ol>
 * <p>拦截方法的静态标识是方法的定义描述，如本接口中的方法{@link #setQuery(boolean)}的静态标示是：
 * 	 <blockquote>
 * 	   <code>void com.yappam.commons.cache.context.CacheContext.setQuery(boolean isQuery)</code>
 *   </blockquote>
 * <p>拦截方法的动态标示是方法参数的不同使得方法的动态描述不同，如：
 *   <blockquote>
 *     <code>com.yappam.commons.cache.context.CacheContext.setQuery(true)</code>
 *     <code>com.yappam.commons.cache.context.CacheContext.setQuery(false)</code>
 *   </blockquote>
 * <p>静/动态唯一标示的意思是最终的标示是经过转换的。目前的默认处理是取标示的MD5值</p>
 * @author qianli_xy@163.com
 * @since 1.0.0
 * @see MD5CacheKeyProvider
 */
public interface CacheContext {
	
	Logger LOGGER = LoggerFactory.getLogger(CacheContext.class);

	/**
	 * 赋值当前缓存方法使用到的多个数据库表
	 * @param tables 当前缓存方法执行的SQL对应的table集合
	 */
	void setTables(Set<String> tables);
	
	/**
	 * 添加当前缓存方法使用的数据库表集合
	 * @param tables table集合
	 */
	void addTables(Set<String> tables);
	
	/**
	 * 获取当前缓存方法使用到的多个数据库表
	 * @return 当前缓存方法执行的SQL对应的table集合
	 */
	Set<String> getTables();
	
	/**
	 * 当前方法是否是查询方法，SQL中不存在一条DML语句就判定当前方法为查询方法。
	 * @return true - 是查询方法，false - 不是查询方法
	 */
	Boolean isQuery();
	
	/**
	 * 赋值当前方法是否是查询方法
	 * @param isQuery 当前方法是否是查询方法
	 */
	void setQuery(boolean isQuery);
	
	/**
	 * 获取当前方法最后的查询时间
	 * @return 当前方法最后的查询时间，不存在则返回0
	 */
	long getLastQueryTime();
	
	/**
	 * 赋值当前方法最后的查询时间戳
	 * @param lastQueryTime 当前方法最后的查询时间戳，一致性时间戳对象
	 * @see ConsistentTime
	 */
	void setLastQueryTime(ConsistentTime lastQueryTime);
	
	/**
	 * 注册一个拦截到的方法
	 * @param joinPoint 拦截的方法信息
	 * @param keyProvider 缓存key的生成策略
	 */
	void register(ProceedingJoinPoint joinPoint, CacheKeyProvider keyProvider);
	
	/**
	 * 赋值数据库表最后的修改时间戳
	 * @param table 数据库表
	 * @param time 一致性时间戳对象
	 * @see ConsistentTime
	 */
	void setTableLastAlterTime(String table, ConsistentTime time);
	
	/**
	 * 获取数据库表最后的修改时间戳
	 * @param table 数据库表名称
	 * @return 最后的修改时间戳
	 */
	long getTableLastAlterTime(String table);
	
	/**
	 * 获取当前方法的动态唯一标示
	 * @return 当前方法的动态唯一标示
	 */
	String getDynamicUniqueMark();
	
	/**
	 * 获取当前方法的静态唯一标示
	 * @return 当前方法的静态唯一标示
	 */
	String getStaticUniqueMark();
	
}

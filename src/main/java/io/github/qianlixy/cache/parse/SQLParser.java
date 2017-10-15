package io.github.qianlixy.cache.parse;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.qianlixy.cache.context.CacheContext;

/**
 * <p>SQL解析器 </p>
 * <p>SQL解析器采用对数据库连接池操作，获取SQL信息。可以实现该接口以适配不同数据库连接池 </p>
 * <p>解析器通过通知形式将解析结果保存在{@link CacheContext}实例中，供缓存管理器使用 </p>
 * @see CacheContext 需要的解析结果
 * @author qianli_xy@163.com
 * @since 1.7
 */
public interface SQLParser {
	
	/** 日志打印。子类使用该日志对象打印日志，便于日志打印管理 */
	Logger LOGGER = LoggerFactory.getLogger(SQLParser.class);
	
	/**
	 * 设置数据库连接池
	 * @param dataSource 数据库连接池
	 * @throws ClassCastException 不同SQL解析器需要不同的数据库连接池，错误的数据库连接池类型将抛出异常
	 */
	void setDataSource(DataSource dataSource) throws ClassCastException;
	
	/**
	 * <p>赋值缓存上下文对象，用于存放SQL解析结果</p>
	 * <p>自定义SQLParser时，不需要赋值上下文信息，由CacheManager初始化时赋值</p>
	 * @param context 缓存上下文对象
	 */
	void setCacheContext(CacheContext context);
	
}

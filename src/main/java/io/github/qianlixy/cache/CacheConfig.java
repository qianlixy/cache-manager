package io.github.qianlixy.cache;

import java.util.List;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;

import io.github.qianlixy.cache.filter.Filter;
import io.github.qianlixy.cache.filter.FilterConfig;
import io.github.qianlixy.cache.impl.AbstractCacheAdapterFactory;
import io.github.qianlixy.cache.parse.SQLParser;

/**
 * 缓存配置
 * 
 * @author qianli_xy@163.com
 * @since 1.0.0
 * @date 2017年10月9日 下午9:08:50
 */
public class CacheConfig {
	
	//缓存客户端
	private Object cacheClient;
	//缓存客户端抽象工厂
	private AbstractCacheAdapterFactory<?> cacheClientFactory;
	//数据库连接池。注意：暂支持DruidDataSource
	private DataSource dataSource;
	//SQL解析器
	private SQLParser SQLParser;
	//全局默认缓存时间，单位：分钟
	private int defaultCacheTime = 120;
	//过滤器集合
	private List<Filter> filters;
	//缓存过滤器配置集合
	private List<FilterConfig> filterConfigs;
	//是否管理缓存。无论是否管理缓存，都会拦截并解析SQL
	private boolean isManageCache = true; 

	public Object getCacheClient() {
		return cacheClient;
	}

	public void setCacheClient(Object cacheClient) {
		this.cacheClient = cacheClient;
	}

	public AbstractCacheAdapterFactory<?> getCacheClientFactory() {
		return cacheClientFactory;
	}

	public void setCacheClientFactory(AbstractCacheAdapterFactory<?> cacheClientFactory) {
		this.cacheClientFactory = cacheClientFactory;
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * 设置数据库连接池，用于生成SQLParser。
	 * 注意：暂支持DruidDataSource
	 * @param dataSource 数据库连接池
	 * @see DruidDataSource
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public SQLParser getSQLParser() {
		return SQLParser;
	}

	public void setSQLParser(SQLParser sQLParser) {
		SQLParser = sQLParser;
	}

	public int getDefaultCacheTime() {
		return defaultCacheTime;
	}

	public void setDefaultCacheTime(int defaultCacheTime) {
		this.defaultCacheTime = defaultCacheTime;
	}
	
	public List<Filter> getFilters() {
		return filters;
	}

	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}

	/**
	 * 缓存过滤器配置集合，由{@link CacheManager}统一管理分配
	 * @return 缓存过滤器配置集合
	 */
	public List<FilterConfig> getFilterConfigs() {
		return filterConfigs;
	}

	public void setFilterConfigs(List<FilterConfig> filterConfigs) {
		this.filterConfigs = filterConfigs;
	}

	public boolean isManageCache() {
		return isManageCache;
	}

	public void setManageCache(boolean isManageCache) {
		this.isManageCache = isManageCache;
	}
	
}

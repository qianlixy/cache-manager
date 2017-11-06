package io.github.qianlixy.cache;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;

import io.github.qianlixy.cache.context.ApplicationContext;
import io.github.qianlixy.cache.context.BeanCacheContext;
import io.github.qianlixy.cache.context.CacheContext;
import io.github.qianlixy.cache.context.CacheKeyProvider;
import io.github.qianlixy.cache.context.ConsistentTime;
import io.github.qianlixy.cache.context.ConsistentTimeProvider;
import io.github.qianlixy.cache.context.MD5CacheKeyProvider;
import io.github.qianlixy.cache.exception.CacheNotExistException;
import io.github.qianlixy.cache.exception.CacheOutOfDateException;
import io.github.qianlixy.cache.exception.ConsistentTimeException;
import io.github.qianlixy.cache.exception.ExecuteSourceMethodException;
import io.github.qianlixy.cache.exception.InitCacheManagerException;
import io.github.qianlixy.cache.exception.NoFilterDealsException;
import io.github.qianlixy.cache.filter.ConfigurableFilter;
import io.github.qianlixy.cache.filter.Filter;
import io.github.qianlixy.cache.filter.FilterChain;
import io.github.qianlixy.cache.filter.FilterConfig;
import io.github.qianlixy.cache.filter.FilterRequiredConfig;
import io.github.qianlixy.cache.filter.MethodMatchFilter;
import io.github.qianlixy.cache.impl.AbstractCacheAdapterFactory;
import io.github.qianlixy.cache.parse.DruidSQLParser;
import io.github.qianlixy.cache.wrapper.CacheMethodProcesser;
import io.github.qianlixy.cache.wrapper.DefaultCacheMethodProcesser;

/**
 * 默认的缓存管理器
 * 需要设置缓存配置{@link #cacheConfig}
 * @author qianli_xy@163.com
 * @since 1.0.0
 */
public class DefaultCacheManger implements CacheManager {
	
	//缓存配置信息
	protected CacheConfig cacheConfig;
	//缓存上下文信息
	protected CacheContext cacheContext;
	//缓存key生成提供者
	private CacheKeyProvider cacheKeyProvider;

	public void setCacheConfig(CacheConfig cacheConfig) {
		this.cacheConfig = cacheConfig;
	}

	@Override
	public void init() throws Exception {
		LOGGER.info("Cache manager is initializing");
		//初始化缓存客户端工厂对象
		initCahceClientFactory();
		
		//初始化缓存方法上下文信息
		cacheContext = new BeanCacheContext(cacheConfig.getCacheClientFactory().buildCacheAdapter());
		LOGGER.info("Cache manager initialize CacheContext instance by [{}]", BeanCacheContext.class.getName());
		
		//初始化SQLParser
		initSQLParser();
		
		//初始化全局上下文信息
		initApplicationContext();
		
		//初始化过滤器
		initFilters();
		
		//初始化缓存key生成策略
		cacheKeyProvider = new MD5CacheKeyProvider();
	}

	/**
	 * 初始化过滤器
	 * @throws InitCacheManagerException 过滤器类型为{@link ConfigurableFilter}并且泛型为{@link FilterRequiredConfig}或其子类时，
	 * 			{@link #cacheConfig}配置参数<code>filterConfigs</code>则必须包含泛型类型的配置参数。否则将抛出初始化异常
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void initFilters() throws InitCacheManagerException {
		List<Filter> filters = cacheConfig.getFilters();
		if (null == filters || filters.size() <= 0) {
			filters = new ArrayList<>();
			cacheConfig.setFilters(filters);
		}
		
		//添加默认过滤器。方法拦截过滤器
		filters.add(new MethodMatchFilter());
		
		//排序过滤器链
		Collections.sort(filters, new Comparator<Filter>() {
			@Override
			public int compare(Filter o1, Filter o2) {
				return o1.getOrder() - o2.getOrder();
			}
		});
		
		//判断filter类型。如果是ConfigurableFilter，初始化配置参数
		List<Class<?>> configClass = new ArrayList<>();
		if (null != cacheConfig.getFilterConfigs()) {
			cacheConfig.getFilterConfigs().forEach((config) -> configClass.add(config.getClass()));
		}
		for (Filter filter : filters) {
			LOGGER.info("Cache manager initialize filter list add instance [{}]", filter.getClass().getName());
			if (filter instanceof ConfigurableFilter<?>) {
				Class<FilterConfig> genericClass = 
						(Class<FilterConfig>) ((ParameterizedType) filter.getClass().getGenericSuperclass())
						.getActualTypeArguments()[0];
				//如果是必填配置接口，则检查是否存在相应类型的配置。不存在则抛出异常
				if(FilterRequiredConfig.class.isAssignableFrom(genericClass)
						&& !configClass.contains(genericClass)) {
					throw new InitCacheManagerException(genericClass + " is required");
				}
				if(configClass.contains(genericClass)) {
					//给filter赋值配置参数
					((ConfigurableFilter) filter).setConfig(cacheConfig.getFilterConfigs()
							.get(configClass.indexOf(genericClass)));
				}
			}
		}
		
		//绑定过滤器
		FilterChain.setFilters(filters);
	}

	/**
	 * 初始化缓存客户端工厂对象
	 * @throws InitCacheManagerException 初始化异常
	 */
	public void initCahceClientFactory() throws InitCacheManagerException {
		if(null == cacheConfig.getCacheClientFactory()) {
			if(null == cacheConfig.getCacheClient()) {
				throw new InitCacheManagerException("AbstractCacheClientFactory and CacheClientAdapter cannot be null at the same time");
			}
			AbstractCacheAdapterFactory<?> factory = AbstractCacheAdapterFactory.buildFactory(cacheConfig.getCacheClient());
			if(null == factory) {
				throw new InitCacheManagerException("Need to configurate AbstractCacheClientFactory instance because cache clients that are not supported");
			}
			cacheConfig.setCacheClientFactory(factory);
		}
		LOGGER.info("Cache manager initialize CahceClientFactory instance by [{}]", cacheConfig.getCacheClientFactory().getClass().getName());
	}

	/**
	 * 初始化SQLParser
	 * @throws InitCacheManagerException 初始化异常
	 */
	public void initSQLParser() throws InitCacheManagerException {
		if(null == cacheConfig.getSQLParser()) {
			if(null == cacheConfig.getDataSource()) {
				throw new InitCacheManagerException("SQLParser and DataSource cannot be null at the same time");
			}
			DruidSQLParser SQLParser = new DruidSQLParser();
			SQLParser.setDataSource(cacheConfig.getDataSource());
			cacheConfig.setSQLParser(SQLParser);
		}
		cacheConfig.getSQLParser().setCacheContext(cacheContext);
		LOGGER.info("Cache manager initialize SQLParser instance by [{}]", cacheConfig.getSQLParser().getClass().getName());
	}

	/**
	 * 初始化ApplicationContext
	 * @throws IOException 构建缓存适配器时可能出现的异常
	 */
	public void initApplicationContext() throws IOException {
		//赋值全局上下文缓存适配器工厂对象
		AbstractCacheAdapterFactory<?> cacheClientFactory = cacheConfig.getCacheClientFactory();
		ApplicationContext.set(ApplicationContext.KEY_CACHE_ADAPTER_FACTORY, cacheClientFactory);
		//赋值全局上下文一致性时间提供者
		CacheAdapter cacheAdapter = cacheClientFactory.buildCacheAdapter();
		ApplicationContext.set(ApplicationContext.KEY_CONSISTENT_TIME_PROVIDER, new ConsistentTimeProvider() {
			@Override
			public ConsistentTime newInstance() throws ConsistentTimeException {
				return new ConsistentTime(cacheAdapter.consistentTime());
			}
		});
		//赋值全局上下文默认缓存有效期
		ApplicationContext.set(ApplicationContext.KEY_DEFAULT_CACHE_TIME, cacheConfig.getDefaultCacheTime());
	}

	@Override
	public Object doCache(ProceedingJoinPoint joinPoint) throws Throwable {
		Date start = new Date();
		//注册拦截源方法
		cacheContext.register(joinPoint, cacheKeyProvider);
		//配置参数是否管理缓存为false，直接执行源代码并返回
		if(!cacheConfig.isManageCache()) {
			return joinPoint.proceed();
		}
		//生成源方法缓存操作包装类对象
		CacheMethodProcesser cacheProcesser = new DefaultCacheMethodProcesser(joinPoint, cacheContext);
		try {
			Boolean isQuery = cacheContext.isQuery();
			if (null == isQuery) {
				//未知拦截方法是查询或修改方法，直接执行源方法
				Object source = cacheProcesser.doProcess();
				isQuery = cacheContext.isQuery();
				if(null == isQuery) {
					//拦截不到SQL，直接返回source
					return source;
				}
			}
			//非查询方法不执行拦截器
			if(!isQuery) {
				return cacheProcesser.doProcess();
			}
			
			//拦截方法为查询方法时，依次调用过滤器
			FilterChain filterChain = new FilterChain();
			try {
				return filterChain.doFilter(cacheProcesser, filterChain);
			} catch (NoFilterDealsException e) {
				//没有缓存处理时，进行下面默认处理
				try {
					return cacheProcesser.getCache();
				} catch (CacheOutOfDateException | CacheNotExistException e1) {
					return cacheProcesser.doProcessAndCache();
				}
			}
		} catch(ExecuteSourceMethodException e) {
			throw e;
		} catch(Throwable th) {
			LOGGER.error("Occur exception while caching", th);
			return cacheProcesser.doProcess();
		} finally {
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("Cache manager execute time {}ms", new Date().getTime() - start.getTime());
			}
			start = null;
		}
	}
	
}

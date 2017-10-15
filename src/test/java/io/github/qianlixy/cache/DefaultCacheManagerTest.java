package io.github.qianlixy.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.qianlixy.cache.config.RequiredConfigFilterTestClass;
import io.github.qianlixy.cache.config.RequiredConfigTestClass;
import io.github.qianlixy.cache.exception.InitCacheManagerException;
import io.github.qianlixy.cache.filter.ConfigurableFilter;
import io.github.qianlixy.cache.filter.Filter;
import io.github.qianlixy.cache.filter.FilterConfig;
import io.github.qianlixy.cache.filter.FilterRequiredConfig;
import io.github.qianlixy.cache.filter.MethodMatchFilter;
import io.github.qianlixy.cache.filter.MethodMatchFilterConfig;
import io.github.qianlixy.cache.wrapper.CacheMethodProcesser;

public class DefaultCacheManagerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCacheManagerTest.class);
	
	private DefaultCacheManger manager = new DefaultCacheManger();
	
	@Test
	@SuppressWarnings("unchecked")
	public void testGetGeneric() {
		ConfigurableFilter<MethodMatchFilterConfig> filter = new MethodMatchFilter();
		Class<FilterConfig> entityClass = (Class<FilterConfig>) ((ParameterizedType) filter.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		LOGGER.debug(String.valueOf(entityClass));
		Class<?>[] interfaces = entityClass.getInterfaces();
		for (Class<?> clazz : interfaces) {
			if(FilterRequiredConfig.class.isAssignableFrom(clazz)) {
				fail();
			}
		}
	}
	
	@Test
	public void testInitFilters() {
		CacheConfig cacheConfig = new CacheConfig();
		List<FilterConfig> filterConfigs = new ArrayList<>();
		filterConfigs.add(new MethodMatchFilterConfig());
		cacheConfig.setFilterConfigs(filterConfigs);
		manager.setCacheConfig(cacheConfig);
		
		try {
			manager.initFilters();
		} catch (InitCacheManagerException e) {
			fail();
		}
		
		List<Filter> filters = new ArrayList<>();
		filters.add(new Filter() {
			@Override
			public Object doFilter(CacheMethodProcesser cacheProcesser, Filter filterChain) throws Throwable {
				return null;
			}

			@Override
			public int getOrder() {
				return 0;
			}
		});
		cacheConfig.setFilters(filters);
		
		try {
			manager.initFilters();
		} catch (InitCacheManagerException e) {
			fail();
		}
		
	}
	
	@Test
	public void testInitFilters_requiredConfig() {
		CacheConfig cacheConfig = new CacheConfig();
		manager.setCacheConfig(cacheConfig);
		List<Filter> filters = new ArrayList<>();
		cacheConfig.setFilters(filters);
		
		RequiredConfigFilterTestClass filterInstance = new RequiredConfigFilterTestClass();
		filters.add(filterInstance);
		
		RequiredConfigTestClass config = new RequiredConfigTestClass();
		List<FilterConfig> filterConfigs = new ArrayList<>();
		filterConfigs.add(config);
		cacheConfig.setFilterConfigs(filterConfigs);
		
		try {
			manager.initFilters();
		} catch (InitCacheManagerException e) {
			LOGGER.error(e.getMessage());
			fail();
		}
		
		assertEquals(config, filterInstance.getConfig());
	}
	
	@Test
	public void testInitFilters_filterOrder() throws InitCacheManagerException {
		List<Filter> filters = new ArrayList<>();
		Filter filter2 = new Filter() {
			@Override
			public int getOrder() {
				return 2;
			}
			@Override
			public Object doFilter(CacheMethodProcesser cacheProcesser, Filter filterChain) throws Throwable {
				return null;
			}
		};
		filters.add(filter2);
		Filter filter1 = new Filter() {
			@Override
			public int getOrder() {
				return 1;
			}
			@Override
			public Object doFilter(CacheMethodProcesser cacheProcesser, Filter filterChain) throws Throwable {
				return null;
			}
		};
		filters.add(filter1);
		CacheConfig cacheConfig = new CacheConfig();
		manager.setCacheConfig(cacheConfig );
		cacheConfig.setFilters(filters);
		
		manager.initFilters();
		
		assertEquals(filter1, filters.get(1));
		assertEquals(filter2, filters.get(2));
	}
}

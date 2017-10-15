package io.github.qianlixy.cache.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.github.qianlixy.cache.exception.NoFilterDealsException;
import io.github.qianlixy.cache.wrapper.CacheMethodProcesser;
import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
import net.sourceforge.groboutils.junit.v1.TestRunnable;

public class FilterChainTest {
	
	private Filter filter = new FilterChain();
	
	@Before
	public void before() {
		FilterChain.setFilters(null);
	}
	
	@Test
	public void testDoFilter_noFilter() throws Throwable {
		CacheMethodProcesser cacheProcesser = EasyMock.createMock(CacheMethodProcesser.class);
		EasyMock.expect(cacheProcesser.doProcess()).andReturn(null);
		EasyMock.replay(cacheProcesser);
		
		Filter filter = new FilterChain();
		Object result = filter.doFilter(cacheProcesser , filter);
		
		assertEquals(null, result);
		EasyMock.verify(cacheProcesser);
	}
	
	@Test
	public void testDoFilter_oneFilter() throws Throwable {
		List<Filter> filters = new ArrayList<>();
		Filter firstFilter = EasyMock.createMock(Filter.class);
		filters.add(firstFilter);
		FilterChain.setFilters(filters);
		
		CacheMethodProcesser cacheProcesser = EasyMock.createMock(CacheMethodProcesser.class);
		EasyMock.expect(firstFilter.doFilter(cacheProcesser, filter)).andReturn(null);
		EasyMock.replay(firstFilter);
		
		Object result = filter.doFilter(cacheProcesser , filter);
		
		assertEquals(null, result);
		EasyMock.verify(firstFilter);
	}
	
	@Test
	public void testDoFilter_multiFilter() throws Throwable {
		List<Filter> filters = new ArrayList<>();
		Filter testFilter = new Filter() {
			@Override
			public Object doFilter(CacheMethodProcesser cacheProcesser, Filter filterChain) throws Throwable {
				return filterChain.doFilter(cacheProcesser, filterChain);
			}

			@Override
			public int getOrder() {
				return 0;
			}
		};
		filters.add(testFilter);
		filters.add(testFilter);
		filters.add(testFilter);
		FilterChain.setFilters(filters);
		
		try {
			filter.doFilter(null, filter);
		} catch (NoFilterDealsException e) {
			assertTrue(true);
			return;
		}
		fail();
	}
	
	@Test
	public void testDoFilter_multiFilterWithMultiThread() {
		List<Filter> filters = new ArrayList<>();
		Filter testFilter = new Filter() {
			@Override
			public Object doFilter(CacheMethodProcesser cacheProcesser, Filter filterChain) throws Throwable {
				return filterChain.doFilter(cacheProcesser, filterChain);
			}

			@Override
			public int getOrder() {
				return 0;
			}
		};
		filters.add(testFilter);
		filters.add(testFilter);
		filters.add(testFilter);
		FilterChain.setFilters(filters);
		
		TestRunnable[] trs = new TestRunnable[10];
		for (int i = 0; i < trs.length; i++) {
			trs[i] = new TestRunnable() {
				@Override
				public void runTest() throws Throwable {
					try {
						Filter filter = new FilterChain();
						filter.doFilter(null, filter);
					} catch (NoFilterDealsException e) {
						Assert.assertTrue(true);
						return;
					}
					Assert.fail();
				}
			};
		}
		
		MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs);

		try {
			mttr.runTestRunnables();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
}

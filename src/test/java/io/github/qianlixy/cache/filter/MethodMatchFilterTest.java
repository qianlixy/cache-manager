package io.github.qianlixy.cache.filter;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.HashSet;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import io.github.qianlixy.cache.wrapper.CacheMethodProcesser;

public class MethodMatchFilterTest {
	
	private MethodMatchFilter methodMatchFilter = new MethodMatchFilter();
	
	private MethodMatchFilterConfig config;
	private CacheMethodProcesser cacheProcesser;
	private Filter filterChain;
	private Collection<MethodMatchFilterConfigBean> configBeans;
	
	@Before
	public void before() {
		config = new MethodMatchFilterConfig();
		configBeans = new HashSet<>();
		config.setConfigBeans(configBeans);
		methodMatchFilter.setConfig(config);
		cacheProcesser = EasyMock.createMock(CacheMethodProcesser.class);
		filterChain = EasyMock.createMock(Filter.class);
	}

	@Test
	//测试没有匹配规则时，匹配任何方法视为匹配成功
	public void testDoFilter_noHasConfigBeans() throws Throwable {
		EasyMock.expect(filterChain.doFilter(cacheProcesser, filterChain)).andReturn(null);
		EasyMock.replay(filterChain);
		
		Object result = methodMatchFilter.doFilter(cacheProcesser, filterChain);
		
		assertEquals(null, result);
		EasyMock.verify(filterChain);
		
		
	}
	
	@Test
	//测试匹配拦截方法
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testDoFilter_methodMatch() throws Throwable {
		configBeans.add(new MethodMatchFilterConfigBean("io.github.*.MethodMatchFilterTest", false, 100, false));
		Class clazz = MethodMatchFilterTest.class;
		EasyMock.expect(cacheProcesser.getTargetClass()).andReturn(clazz);
		EasyMock.expect(cacheProcesser.getMethodName()).andReturn("testDoFilter_methodMatch");
		cacheProcesser.setCacheTime(100);
		EasyMock.expectLastCall().once();
		EasyMock.expect(filterChain.doFilter(cacheProcesser, filterChain)).andReturn(null);
		EasyMock.replay(cacheProcesser, filterChain);
		
		Object result = methodMatchFilter.doFilter(cacheProcesser, filterChain);
		
		assertEquals(null, result);
		EasyMock.verify(cacheProcesser, filterChain);
	}
	
	@Test
	//测试匹配拦截方法失败时，执行源方法并返回
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testDoFilter_methodMatchFail() throws Throwable {
		configBeans.add(new MethodMatchFilterConfigBean("io.github.*.MethodMatchFilterTest.XXXXXX", false, 100, false));
		Class clazz = MethodMatchFilterTest.class;
		EasyMock.expect(cacheProcesser.getTargetClass()).andReturn(clazz);
		EasyMock.expect(cacheProcesser.getMethodName()).andReturn("testDoFilter_methodMatch");
		EasyMock.expect(cacheProcesser.doProcess()).andReturn(null);
		EasyMock.replay(cacheProcesser);
		
		Object result = methodMatchFilter.doFilter(cacheProcesser, filterChain);
		
		assertEquals(null, result);
		EasyMock.verify(cacheProcesser);
	}
	
	@Test
	//测试匹配线程栈方法
	public void testDoFilter_fromMatch() throws Throwable {
		configBeans.add(new MethodMatchFilterConfigBean("io.github.qianlixy.cache.filter.MethodMatchFilterTest", true, 100, false));
		cacheProcesser.setCacheTime(100);
		EasyMock.expectLastCall().once();
		EasyMock.expect(filterChain.doFilter(cacheProcesser, filterChain)).andReturn(null);
		EasyMock.replay(cacheProcesser, filterChain);
		
		Object result = methodMatchFilter.doFilter(cacheProcesser, filterChain);
		
		assertEquals(null, result);
		EasyMock.verify(cacheProcesser, filterChain);
	}
	
	@Test
	//测试匹配线程栈方法失败时，执行源方法并返回
	public void testDoFilter_fromMatchFail() throws Throwable {
		config.setLevel(3);
		configBeans.add(new MethodMatchFilterConfigBean("io.github.*.MethodMatchFilterTest", true, 100, false));
		EasyMock.expect(cacheProcesser.doProcess()).andReturn(null);
		EasyMock.replay(cacheProcesser);
		
		Object result = methodMatchFilter.doFilter(cacheProcesser, filterChain);
		
		assertEquals(null, result);
		EasyMock.verify(cacheProcesser);
	}
	
}

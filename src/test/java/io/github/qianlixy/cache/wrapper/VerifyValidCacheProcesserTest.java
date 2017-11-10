package io.github.qianlixy.cache.wrapper;

import java.io.IOException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.easymock.EasyMock;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.github.qianlixy.cache.CacheAdapter;
import io.github.qianlixy.cache.context.ApplicationContext;
import io.github.qianlixy.cache.context.CacheContext;
import io.github.qianlixy.cache.impl.AbstractCacheAdapterFactory;
import io.github.qianlixy.cache.impl.MemcachedCacheAdapterFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ApplicationContext.class)
public class VerifyValidCacheProcesserTest {
	
	@BeforeClass
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void beforeClass() throws IOException {
		PowerMockito.mockStatic(ApplicationContext.class);
		PowerMockito.when(ApplicationContext.getDefaultCacheTime()).thenReturn(300);
		
		CacheAdapter cacheAdapter = EasyMock.createMock(CacheAdapter.class);
		EasyMock.expect(cacheAdapter.get("testGetCache")).andReturn("cache");
		EasyMock.replay(cacheAdapter);
		
		AbstractCacheAdapterFactory adapterFactory = EasyMock.createMock(MemcachedCacheAdapterFactory.class);
		EasyMock.expect(adapterFactory.buildCacheAdapter()).andReturn(cacheAdapter);
		EasyMock.replay(adapterFactory);
		
		PowerMockito.when(ApplicationContext.getCacheAdaperFactory()).thenReturn(adapterFactory);
		PowerMockito.when(ApplicationContext.getConsistentTimeProvider()).thenReturn(null);
	}

	@Test
	public void testGetCache() throws IOException {
		ProceedingJoinPoint joinPoint = EasyMock.createMock(ProceedingJoinPoint.class);
		
		CacheContext cacheContext = EasyMock.createMock(CacheContext.class);
		EasyMock.expect(cacheContext.getDynamicUniqueMark()).andReturn("testGetCache");
		EasyMock.replay(cacheContext);
		
		VerifyValidCacheProcesser cacheProcesser = new VerifyValidCacheProcesser(joinPoint, cacheContext);
		CacheVerifier cacheVerifier = EasyMock.createMock(CacheVerifier.class);
		EasyMock.expect(cacheVerifier.isValidCache(cacheContext)).andReturn(true);
		EasyMock.replay(cacheVerifier);
		
		cacheProcesser.setCacheVerifier(cacheVerifier);
		cacheProcesser.getCache();
	}
}

package io.github.qianlixy.cache.wrapper;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.TimeoutException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import io.github.qianlixy.cache.CacheAdapter;
import io.github.qianlixy.cache.SpringContextBaseTest;
import io.github.qianlixy.cache.context.ApplicationContext;
import io.github.qianlixy.cache.context.CacheContext;
import io.github.qianlixy.cache.context.ConsistentTime;
import io.github.qianlixy.cache.context.ConsistentTimeProvider;
import io.github.qianlixy.cache.exception.ConsistentTimeException;
import io.github.qianlixy.cache.impl.AbstractCacheAdapterFactory;
import io.github.qianlixy.cache.impl.MemcachedAdapter;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
import net.sourceforge.groboutils.junit.v1.TestRunnable;

public class DefaultCacheMethodProcesserTest extends SpringContextBaseTest {
	
	private ConsistentTime consistentTime;
	
	@Autowired
	private MemcachedClient memcachedClient;
	
	@Before
	public void before() throws IOException {
		AbstractCacheAdapterFactory<Object> factory = new AbstractCacheAdapterFactory<Object>() {
			@Override
			public CacheAdapter buildCacheAdapter() throws IOException {
				try {
					memcachedClient.flushAll();
				} catch (TimeoutException | InterruptedException | MemcachedException e) {
					e.printStackTrace();
				}
				return new MemcachedAdapter(memcachedClient);
			}
		};
		ApplicationContext.set(ApplicationContext.KEY_CACHE_ADAPTER_FACTORY, factory);
		ApplicationContext.set(ApplicationContext.KEY_DEFAULT_CACHE_TIME, 120);
		consistentTime = new ConsistentTime(new Date().getTime());
		ApplicationContext.set(ApplicationContext.KEY_CONSISTENT_TIME_PROVIDER, new ConsistentTimeProvider() {
			@Override
			public ConsistentTime newInstance() throws ConsistentTimeException {
				return consistentTime;
			}
		});
	}

	@Test
	public void testDoProcess_queryMethod() throws Throwable {
		ProceedingJoinPoint joinPoint = buildJoinPoint();
		EasyMock.expect(joinPoint.proceed()).andReturn(null).once();
		
		CacheContext cacheContext = EasyMock.createMock(CacheContext.class);
		EasyMock.expect(cacheContext.isQuery()).andReturn(true).anyTimes();
		EasyMock.expect(cacheContext.getDynamicUniqueMark()).andReturn(new String("method_name")).anyTimes();
		HashSet<String> hashSet = new HashSet<String>(1);
		hashSet.add("t_user");
		EasyMock.expect(cacheContext.getTables()).andReturn(hashSet).anyTimes();
		cacheContext.setLastQueryTime(consistentTime);
		EasyMock.expectLastCall();
		EasyMock.expect(cacheContext.getTableLastAlterTime("t_user")).andReturn(consistentTime.getTime()).anyTimes();
		EasyMock.expect(cacheContext.getLastQueryTime()).andReturn(consistentTime.getTime() + 1000L).anyTimes();
		EasyMock.replay(joinPoint, cacheContext);
		
		TestRunnable[] trs = new TestRunnable[5];
		for (int i = 0; i < trs.length; i++) {
			trs[i] = new TestRunnable() {
				@Override
				public void runTest() throws Throwable {
					DefaultCacheMethodProcesser methodProcesser = new DefaultCacheMethodProcesser(joinPoint , cacheContext);
					methodProcesser.doProcessAndCache();
				}
			};
		}
		
		MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs);
		
		try {
			mttr.runTestRunnables();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		EasyMock.verify(joinPoint, cacheContext);
	}

	private ProceedingJoinPoint buildJoinPoint() {
		ProceedingJoinPoint joinPoint = EasyMock.createMock(ProceedingJoinPoint.class);
		Signature signature = new Signature() {
			@Override
			public String toShortString() {return null;}
			@Override
			public String toLongString() {return new String("method_name");}
			@Override
			public String getName() {return null;}
			@Override
			public int getModifiers() {return 0;}
			@Override
			public String getDeclaringTypeName() {return null;}
			@Override
			@SuppressWarnings("rawtypes")
			public Class getDeclaringType() {return null;}
		};
		
		EasyMock.expect(joinPoint.getSignature()).andReturn(signature).anyTimes();
		return joinPoint;
	}
	
	@Test
	public void testDoProcess_alterMethod() throws Throwable {
		ProceedingJoinPoint joinPoint = buildJoinPoint();
		EasyMock.expect(joinPoint.proceed()).andReturn(null).times(10);
		
		CacheContext cacheContext = EasyMock.createMock(CacheContext.class);
		EasyMock.expect(cacheContext.isQuery()).andReturn(false).anyTimes();
		EasyMock.replay(joinPoint, cacheContext);
		
		TestRunnable[] trs = new TestRunnable[10];
		for (int i = 0; i < trs.length; i++) {
			trs[i] = new TestRunnable() {
				@Override
				public void runTest() throws Throwable {
					DefaultCacheMethodProcesser methodProcesser = new DefaultCacheMethodProcesser(joinPoint , cacheContext);
					methodProcesser.doProcess();
				}
			};
		}
		
		MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs);
		
		try {
			mttr.runTestRunnables();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		EasyMock.verify(joinPoint, cacheContext);
	}
	
}

package io.github.qianlixy.cache.context;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.github.qianlixy.cache.exception.ConsistentTimeException;
import io.github.qianlixy.cache.impl.MemcachedAdapter;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class BeanCacheContextTest {

	private MemcachedClient memcachedClient;
	private BeanCacheContext beanCacheContext;
	
	@Resource
	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
		beanCacheContext = new BeanCacheContext(new MemcachedAdapter(memcachedClient));
		ProceedingJoinPoint joinPoint = EasyMock.createMock(ProceedingJoinPoint.class);
		Signature signature = EasyMock.createMock(Signature.class);
		EasyMock.expect(signature.getName()).andReturn("setMemcachedClient").anyTimes();
		EasyMock.expect(signature.toLongString()).andReturn("com.yappam.commons.cache.context.BeanCacheContextTest.setMemcachedClient()").anyTimes();
		EasyMock.replay(signature);
		EasyMock.expect(joinPoint.getSignature()).andReturn(signature).anyTimes();
		EasyMock.expect(joinPoint.getTarget()).andReturn(BeanCacheContextTest.class).anyTimes();
		EasyMock.expect(joinPoint.getArgs()).andReturn(null).anyTimes();
		EasyMock.replay(joinPoint);
		beanCacheContext.register(joinPoint , new MD5CacheKeyProvider());
	}
	
	@Before
	public void before() {
		try {
			memcachedClient.flushAll();
		} catch (TimeoutException | InterruptedException | MemcachedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testQuery() {
		assertEquals(null, beanCacheContext.isQuery());
		beanCacheContext.setQuery(true);
		assertEquals(true, beanCacheContext.isQuery());
	}
	
	@Test
	public void testTables() {
		assertEquals(null, beanCacheContext.getTables());
		Set<String> tables = new HashSet<>();
		tables.add("test");
		beanCacheContext.addTables(tables);
		assertEquals(1, beanCacheContext.getTables().size());
		beanCacheContext.setTables(new HashSet<>());
		assertEquals(0, beanCacheContext.getTables().size());
	}
	
	@Test
	public void testLastQueryTime() throws ConsistentTimeException {
		assertEquals(-1L, beanCacheContext.getLastQueryTime());
		long now = new Date().getTime();
		beanCacheContext.setLastQueryTime(new ConsistentTime(now));
		assertEquals(now, beanCacheContext.getLastQueryTime());
	}
	
	@Test
	public void testTableLastAlterTime() throws ConsistentTimeException {
		assertEquals(0, beanCacheContext.getTableLastAlterTime("t_user_test"));
		long now = new Date().getTime();
		beanCacheContext.setTableLastAlterTime("t_user_test", new ConsistentTime(now));
		assertEquals(now, beanCacheContext.getTableLastAlterTime("t_user_test"));
	}
}

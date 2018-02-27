package io.github.qianlixy.cache.wrapper;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.github.qianlixy.cache.SpringContextBaseTest;
import io.github.qianlixy.cache.context.ApplicationContext;
import io.github.qianlixy.cache.impl.MemcachedCacheAdapterFactory;
import net.rubyeye.xmemcached.MemcachedClient;

public class DistributedLockTest extends SpringContextBaseTest {
	
	private static final Logger logger = LoggerFactory.getLogger(DistributedLockTest.class);
	
	public static final String METHOD_FLAG = "METHOD_FLAG";

	@Autowired
	MemcachedClient client;
	
	@Before
	public void before() {
		MemcachedCacheAdapterFactory factory = new MemcachedCacheAdapterFactory();
		factory.setClient(client);
		ApplicationContext.set(ApplicationContext.KEY_CACHE_ADAPTER_FACTORY, factory);
	}
	
	@Test
	public void testLock() {
		DistributedLock lock = new DistributedLock();
		lock.lock(METHOD_FLAG);
		int times = 1;
		while(times <= 3) {
			logger.info("The thread id is {} loop {} times", Thread.currentThread().getId(), times);
			times ++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		lock.unlock(METHOD_FLAG);
	}
	
}

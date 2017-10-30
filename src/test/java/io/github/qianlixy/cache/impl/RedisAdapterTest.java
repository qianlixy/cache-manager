package io.github.qianlixy.cache.impl;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.github.qianlixy.cache.CacheAdapter;
import io.github.qianlixy.cache.exception.ConsistentTimeException;
import redis.clients.jedis.JedisCluster;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class RedisAdapterTest {

	private CacheAdapter cacheAdapter;
	
	@Resource
	public void setJedisCluter(JedisCluster jedisCluter) {
		cacheAdapter = new RedisAdapter(jedisCluter);
	}
	
	@Test
	public void test() {
		cacheAdapter.set("hw", "Hello, world");
		Assert.assertEquals(cacheAdapter.get("hw"), "Hello, world");
		cacheAdapter.remove("hw");
		Assert.assertEquals(cacheAdapter.get("hw"), null);
	}
	
	//@Test
	public void testSetWithExpireTime() throws InterruptedException {
		String key = "expireKey";
		String value = "expireValue";
		boolean set = cacheAdapter.set(key, value, 1);
		if(set) {
			Assert.assertEquals(cacheAdapter.get(key), value);
			Thread.sleep(1*1000*60);
			Assert.assertEquals(cacheAdapter.get(key), null);
			return;
		}
		Assert.fail();
	}
	
	@Test
	public void testConsistentTime() throws ConsistentTimeException {
		long old = 0L;
		for (int i = 0; i < 100; i++) {
			long consistentTime = cacheAdapter.consistentTime();
			Assert.assertTrue(consistentTime > old);
			old = consistentTime;
		}
	}
	
}

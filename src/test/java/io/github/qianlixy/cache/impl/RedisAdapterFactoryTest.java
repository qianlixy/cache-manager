package io.github.qianlixy.cache.impl;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.github.qianlixy.cache.CacheAdapter;
import io.github.qianlixy.cache.exception.ConsistentTimeException;
import redis.clients.jedis.JedisCluster;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration({"classpath:applicationContext.xml"})
public class RedisAdapterFactoryTest {
	
	@Autowired
	private JedisCluster jedisCluter;

	@Test
	public void testBuildCacheAdapter() throws IOException, ConsistentTimeException {
		AbstractCacheAdapterFactory<JedisCluster> redisFactory = new RedisAdapterFactory();
		redisFactory.setClient(jedisCluter);
		
		CacheAdapter cacheAdapter = redisFactory.buildCacheAdapter();
		
		System.out.println(cacheAdapter.consistentTime());
	}
}

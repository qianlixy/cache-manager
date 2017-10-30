package io.github.qianlixy.cache.impl;

import java.io.IOException;

import io.github.qianlixy.cache.CacheAdapter;
import redis.clients.jedis.JedisCluster;

public class RedisAdapterFactory extends AbstractCacheAdapterFactory<JedisCluster> {

	public RedisAdapterFactory() {
	}
	
	public RedisAdapterFactory(JedisCluster jedisCluster) {
		client = jedisCluster;
	}
	
	@Override
	public CacheAdapter buildCacheAdapter() throws IOException {
		return new RedisAdapter(client);
	}

}

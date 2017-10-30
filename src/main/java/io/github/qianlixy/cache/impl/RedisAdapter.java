package io.github.qianlixy.cache.impl;

import java.util.Collection;
import java.util.List;

import org.nustaq.serialization.FSTConfiguration;

import io.github.qianlixy.cache.CacheAdapter;
import io.github.qianlixy.cache.exception.ConsistentTimeException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class RedisAdapter implements CacheAdapter {
	
	private static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();
	
	private static volatile Jedis jedisNode;
	
	private JedisCluster jedisCluster;
	
	public RedisAdapter(JedisCluster jedisCluster) {
		this.jedisCluster = jedisCluster;
	}

	@Override
	public boolean set(String key, Object value) {
		try {
			return "OK".equals(jedisCluster.set(conf.asByteArray(key), conf.asByteArray(value)));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean set(String key, Object value, int time) {
		try {
			boolean result = set(key, value);
			if(result) {
				return 1L == jedisCluster.expire(conf.asByteArray(key), time);
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Object get(String key) {
		try {
			byte[] result = jedisCluster.get(conf.asByteArray(key));
			if(null == result) {
				return null;
			}
			return conf.asObject(result);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean remove(String key) {
		try {
			return jedisCluster.del(conf.asByteArray(key)) > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public long consistentTime() throws ConsistentTimeException {
		//TODO 并行情况下，可能存在不唯一的时间
		if(null == RedisAdapter.jedisNode) {
			Collection<JedisPool> nodes = jedisCluster.getClusterNodes().values();
			if(nodes.isEmpty()) {
				throw new ConsistentTimeException("Jedis pool is empty");
			}
			RedisAdapter.jedisNode = nodes.iterator().next().getResource();
		}
		try {
			List<String> time = RedisAdapter.jedisNode.time();
			return Long.valueOf(time.get(0) + time.get(1));
		} catch (Exception e) {
			throw new ConsistentTimeException(e);
		}
	}

}

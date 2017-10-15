package io.github.qianlixy.cache.impl;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import io.github.qianlixy.cache.CacheAdapter;
import io.github.qianlixy.cache.exception.ConsistentTimeException;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

public class MemcachedAdapter implements CacheAdapter {
	
	private static final String KEY_SYSTEM_TIME_ATOMICITY = "SYSTEM_TIME_ATOMICITY";
	
	protected MemcachedClient client;
	
	private static volatile InetSocketAddress oneSocket;
	
	public MemcachedAdapter(MemcachedClient client) {
		this.client = client;
		if(null == oneSocket) {
			synchronized (MemcachedAdapter.class) {
				if(null == oneSocket) {
					Collection<InetSocketAddress> servers = client.getAvailableServers();
					oneSocket = servers.iterator().next();
				}
			}
		}
	}
	
	@Override
	public boolean set(String key, Object value) {
		return set(key, value, 0);
	}

	@Override
	public boolean set(final String key, final Object value, final int time) {
		try {
			return client.set(key, time, value);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Object get(final String key) {
		try {
			return client.get(key);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean remove(final String key) {
		try {
			return client.delete(key);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public long consistentTime() throws ConsistentTimeException {
		try {
			Map<String, String> map = this.client.getStats().get(oneSocket);
			return Long.valueOf(map.get("time")) + client.incr(KEY_SYSTEM_TIME_ATOMICITY, 1);
		} catch (Exception e) {
			throw new ConsistentTimeException(e);
		}
	}

}
package io.github.qianlixy.cache.impl;

import java.util.concurrent.TimeoutException;

import io.github.qianlixy.cache.CasCacheAdapter;
import io.github.qianlixy.cache.exception.CacheOutOfDateException;
import net.rubyeye.xmemcached.CASOperation;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

public class MemcachedCasAdapter extends MemcachedAdapter implements CasCacheAdapter {
	
	public MemcachedCasAdapter(MemcachedClient client) {
		super(client);
	}

	@Override
	public boolean checkAndSet(String key, final Object value, final long oldCas) {
		try {
			return client.cas(KEY_CAS, new CASOperation<Object>() {

				@Override
				public int getMaxTries() {
					return 1;
				}

				@Override
				public Object getNewValue(long currentCAS, Object currentValue) {
					if(currentCAS == oldCas) {
						return value;
					}
					throw new CacheOutOfDateException();
				}
			});
		} catch (TimeoutException | InterruptedException | MemcachedException | CacheOutOfDateException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public long getCas() {
		try {
			return client.gets(KEY_CAS).getCas();
		} catch (TimeoutException | InterruptedException | MemcachedException e) {
			e.printStackTrace();
			return 0;
		}
	}

}

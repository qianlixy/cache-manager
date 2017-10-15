package io.github.qianlixy.cache.impl;

import java.io.IOException;

import io.github.qianlixy.cache.CacheAdapter;
import net.rubyeye.xmemcached.MemcachedClient;

public class MemcachedCacheAdapterFactory extends AbstractCacheAdapterFactory<MemcachedClient> {

	@Override
	public CacheAdapter buildCacheAdapter() throws IOException {
		return new MemcachedAdapter(client);
	}

}

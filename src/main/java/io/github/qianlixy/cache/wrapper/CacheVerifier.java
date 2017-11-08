package io.github.qianlixy.cache.wrapper;

import io.github.qianlixy.cache.context.CacheContext;

public interface CacheVerifier {

	boolean isValidCache(CacheContext cacheContext);
	
}

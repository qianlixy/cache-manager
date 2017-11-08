package io.github.qianlixy.cache.wrapper;

import java.io.IOException;

import org.aspectj.lang.ProceedingJoinPoint;

import io.github.qianlixy.cache.context.CacheContext;
import io.github.qianlixy.cache.exception.ConsistentTimeException;
import io.github.qianlixy.cache.exception.ExecuteSourceMethodException;

public class WrapAndLocalCacheProcesser extends VerifyValidCacheProcesser {
	
	private Object sourceData;
	private Object cachedData;

	public WrapAndLocalCacheProcesser(ProceedingJoinPoint joinPoint, 
			CacheContext cacheContext) throws IOException {
		super(joinPoint, cacheContext);
	}
	
	@Override
	public Object doProcess() throws ExecuteSourceMethodException {
		if(null != sourceData) {
			return unwrap(sourceData);
		}
		Object source = super.doProcess();
		sourceData = wrap(source);
		return source;
	}

	@Override
	public void putCache(Object source, int time) throws ConsistentTimeException {
		super.putCache(wrap(source), time);
	}

	@Override
	public Object getCache() {
		if(null != cachedData) {
			return unwrap(cachedData);
		}
		Object cache = super.getCache();
		cachedData = wrap(cache);
		return cache;
	}
	
	protected Object wrap(Object source) {
		return null == source ? new Null() : source;
	}
	
	protected Object unwrap(Object source) {
		return source instanceof Null ? null : source;
	}

}

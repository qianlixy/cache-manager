package io.github.qianlixy.cache.wrapper;

import java.io.IOException;

import org.aspectj.lang.ProceedingJoinPoint;

import io.github.qianlixy.cache.context.CacheContext;
import io.github.qianlixy.cache.exception.ExecuteSourceMethodException;

public class BlockCacheProcesser extends WrapAndLocalCacheProcesser {

	public BlockCacheProcesser(ProceedingJoinPoint joinPoint, 
			CacheContext cacheContext) throws IOException {
		super(joinPoint, cacheContext);
	}

	@Override
	public Object doProcess() throws ExecuteSourceMethodException {
		return super.doProcess();
	}
	
}

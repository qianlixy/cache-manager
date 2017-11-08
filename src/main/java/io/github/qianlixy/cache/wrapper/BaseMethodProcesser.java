package io.github.qianlixy.cache.wrapper;

import java.io.IOException;

import org.aspectj.lang.ProceedingJoinPoint;

import io.github.qianlixy.cache.context.CacheContext;

public abstract class BaseMethodProcesser implements CacheMethodProcesser {
	
	protected ProceedingJoinPoint joinPoint;
	protected CacheContext cacheContext;
	
	private String fullMethodName;

	public BaseMethodProcesser(ProceedingJoinPoint joinPoint, 
			CacheContext cacheContext) throws IOException {
		this.joinPoint = joinPoint;
		this.cacheContext = cacheContext;
		this.fullMethodName = joinPoint.toLongString();
	}
	
	@Override
	public Class<?> getTargetClass() {
		return joinPoint.getTarget().getClass();
	}

	@Override
	public String getMethodName() {
		return joinPoint.getSignature().getName();
	}

	@Override
	public Object[] getArgs() {
		return joinPoint.getArgs();
	}

	@Override
	public String getFullMethodName() {
		return fullMethodName;
	}

	@Override
	public CacheContext getCacheContext() {
		return cacheContext;
	}

	@Override
	public String toString() {
		return fullMethodName;
	}

}

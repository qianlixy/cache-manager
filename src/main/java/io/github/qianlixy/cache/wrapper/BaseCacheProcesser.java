package io.github.qianlixy.cache.wrapper;

import java.io.IOException;
import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;

import io.github.qianlixy.cache.CacheAdapter;
import io.github.qianlixy.cache.context.ApplicationContext;
import io.github.qianlixy.cache.context.CacheContext;
import io.github.qianlixy.cache.exception.ConsistentTimeException;
import io.github.qianlixy.cache.exception.ExecuteSourceMethodException;

public abstract class BaseCacheProcesser extends BaseMethodProcesser {

	protected CacheAdapter cacheAdapter;
	protected int cacheTime;
	
	public BaseCacheProcesser(ProceedingJoinPoint joinPoint, 
			CacheContext cacheContext) throws IOException {
		super(joinPoint, cacheContext);
		this.cacheTime = ApplicationContext.getDefaultCacheTime();
		this.cacheAdapter = ApplicationContext.getCacheAdaperFactory().buildCacheAdapter();
	}

	@Override
	public Object doProcess() throws ExecuteSourceMethodException {
		Date start = null;
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start execute source method [{}]", cacheContext.toString());
			start = new Date();
		}
		try {
			return joinPoint.proceed();
		} catch (Throwable e) {
			throw new ExecuteSourceMethodException(e);
		} finally {
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("Execute source method execute time {}ms", new Date().getTime() - start.getTime());
				start = null;
			}
		}
	}

	@Override
	public abstract Object getCache();

	@Override
	public void putCache(Object source) throws ConsistentTimeException {
		putCache(source, cacheTime);
	}

	@Override
	public void putCache(Object source, int time) throws ConsistentTimeException {
		String dynamicUniqueMark = cacheContext.getDynamicUniqueMark();
		LOGGER.debug("Put cache that time is {} on [{}]", time, dynamicUniqueMark);
		cacheAdapter.set(dynamicUniqueMark, source, time);
		cacheContext.setLastQueryTime(ApplicationContext.getConsistentTimeProvider().newInstance());
	}

	@Override
	public Object doProcessAndCache() throws ConsistentTimeException, ExecuteSourceMethodException {
		return doProcessAndCache(cacheTime);
	}

	@Override
	public Object doProcessAndCache(int time) throws ConsistentTimeException, ExecuteSourceMethodException {
		Object source = doProcess();
		Boolean isQuery = cacheContext.isQuery();
		if(null != isQuery && isQuery) {
			//只有在查询方法时才把源数据放在缓存中
			putCache(source, time);
		}
		return source;
	}

	@Override
	public int getCacheTime() {
		return cacheTime;
	}

	@Override
	public void setCacheTime(int cacheTime) {
		this.cacheTime = cacheTime;
	}

}

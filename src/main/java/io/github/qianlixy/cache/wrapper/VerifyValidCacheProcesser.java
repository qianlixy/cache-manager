package io.github.qianlixy.cache.wrapper;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.aspectj.lang.ProceedingJoinPoint;

import io.github.qianlixy.cache.context.CacheContext;

public class VerifyValidCacheProcesser extends BaseCacheProcesser implements CacheVerifier {

	private CacheVerifier cacheVerifier;

	public VerifyValidCacheProcesser(ProceedingJoinPoint joinPoint, 
			CacheContext cacheContext) throws IOException {
		super(joinPoint, cacheContext);
	}

	@Override
	public Object getCache() {
		LOGGER.debug("Get cache from client for method [{}]", cacheContext.toString());
		Object cache = cacheAdapter.get(cacheContext.getDynamicUniqueMark());
		if(null == cache) {
			return cache;
		}
		if(null == cacheVerifier) {
			cacheVerifier = this;
		}
		if(cacheVerifier.isValidCache(cacheContext)) {
			return cache;
		} else {
			return null;
		}
	}
	
	public void setCacheVerifier(CacheVerifier cacheVerifier) {
		this.cacheVerifier = cacheVerifier;
	}

	@Override
	public boolean isValidCache(CacheContext cacheContext) {
		//获取源方法关联表的修改时间
		Collection<String> methodTalbeMap = cacheContext.getTables();
		//找不到源方法对应的表，说明还没有查询过，返回null
		if(null == methodTalbeMap || methodTalbeMap.size() <= 0) {
			return false;
		}
		
		Set<Long> lastAlterTime = new HashSet<>();
		//判断缓存是否超时失效
		for (String table : methodTalbeMap) {
			long time = cacheContext.getTableLastAlterTime(table);
			if(time > 0) {
				lastAlterTime.add(time);
			}
		}
		if (lastAlterTime.size() > 0) {
			long lastQueryTime = cacheContext.getLastQueryTime();
			for (Long alterTime : lastAlterTime) {
				if(alterTime > lastQueryTime) {
					LOGGER.debug("Cached data is out of date on [{}]", cacheContext.toString());
					return false;
				}
			}
		}
		return true;
	}
	
}

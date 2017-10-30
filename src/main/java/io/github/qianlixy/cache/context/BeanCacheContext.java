package io.github.qianlixy.cache.context;

import java.util.HashSet;
import java.util.Set;

import io.github.qianlixy.cache.CacheAdapter;
import io.github.qianlixy.cache.exception.ConsistentTimeException;

/**
 * 缓存信息上下文，每个方法的上下文信息封装成一个bean进行操作
 * @author qianli_xy@163.com
 *
 */
public class BeanCacheContext extends DefaultCacheContext {
	
	private static final String NAMESPACE = BeanCacheContext.class.getName();
	
	public BeanCacheContext(CacheAdapter cacheAdapter) {
		super(cacheAdapter);
	}

	@Override
	public Boolean isQuery() {
		MethodContextBean contextBean = getMethodContextBean(false);
		return null == contextBean ? null : contextBean.getQuery();
	}

	@Override
	public void setQuery(boolean isQuery) {
		MethodContextBean contextBean = getMethodContextBean(true);
		contextBean.setQuery(isQuery);
		setMethodContextBean(contextBean);
	}
	
	@Override
	public void setTables(Set<String> tables) {
		MethodContextBean contextBean = getMethodContextBean(true);
		contextBean.setTables(tables);
		setMethodContextBean(contextBean);
	}

	@Override
	public void addTables(Set<String> newTables) {
		MethodContextBean contextBean = getMethodContextBean(true);
		Set<String> tables = contextBean.getTables();
		if(null == tables) {
			tables = new HashSet<>();
			contextBean.setTables(tables);
		}
		tables.addAll(newTables);
		setMethodContextBean(contextBean);
	}

	@Override
	public Set<String> getTables() {
		MethodContextBean contextBean = getMethodContextBean(false);
		return null == contextBean ? null : contextBean.getTables();
	}

	@Override
	public long getLastQueryTime() {
		MethodContextBean contextBean = getMethodContextBean(false);
		boolean isNull = null == contextBean || null == contextBean.getLastQueryTime();
		return isNull ? -1L : contextBean.getLastQueryTime();
	}

	@Override
	public void setLastQueryTime(ConsistentTime lastQueryTime) throws ConsistentTimeException {
		MethodContextBean contextBean = getMethodContextBean(true);
		contextBean.setLastQueryTime(lastQueryTime.getTime());
		setMethodContextBean(contextBean);
	}

	/*
	 * 从当前线程中获取方法上下文信息，如果没有再从缓存客户端获取。
	 * @param isNew 如果当前线程和缓存客户端都没有方法上下文信息时，是否重新new一个对象
	 * @return 方法上下文信息，如果不存在将返回null
	 */
	private MethodContextBean getMethodContextBean(boolean isNew) {
		MethodContextBean contextBean = get(NAMESPACE);
		if(null == contextBean) {
			contextBean = (MethodContextBean) cacheAdapter.get(getKeyWithNamespace());
			if(null == contextBean) {
				if(isNew) {
					contextBean = new MethodContextBean(get(STATIC_METHOD_FULL_NAME));
				} else {
					return null;
				}
			}
			set(NAMESPACE, contextBean);
		}
		return contextBean;
	}
	
	/*
	 * 保存方法上下文信息到当前线程中和缓存客户端
	 * @param contextBean 方法上下文信息
	 */
	private void setMethodContextBean(MethodContextBean contextBean) {
		if(null != get(NAMESPACE)) {
			set(NAMESPACE, contextBean);
		}
		cacheAdapter.set(getKeyWithNamespace(), contextBean);
	}
	
	private String getKeyWithNamespace() {
		return keyProvider.process(NAMESPACE + get(STATIC_METHOD_FULL_NAME));
	}

}

package io.github.qianlixy.cache.context;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.aspectj.lang.ProceedingJoinPoint;

import io.github.qianlixy.cache.CacheAdapter;
import io.github.qianlixy.cache.utils.UniqueMethodMarkUtil;

@SuppressWarnings("unchecked")
public class DefaultCacheContext implements CacheContext {
	
	protected static ThreadLocal<Map<Object, Object>> threadLocal = new ThreadLocal<>();
	
	/** 源方法的静态唯一标示key */
	protected String STATIC_UNIQUE_MARK = "STATIC_UNIQUE_MARK";
	/** 源方法的全路径名称key */
	protected String STATIC_METHOD_FULL_NAME = "STATIC_METHOD_FULL_NAME";
	/** 源方法的动态唯一标示key */
	protected String DYNAMIC_UNIQUE_MARK = "DYNAMIC_UNIQUE_MARK";
	/** 是否源方法是查询方法key */
	private String IS_QUERY_METHOD = "IS_QUERY_METHOD";
	/** 源方法的最后查询时间的映射key */
	private String LAST_QUERY_TIME = "LAST_QUERY_TIME";
	/** 数据库表的最后修改时间的映射key */
	private String LAST_ALTER_TIME = "LAST_ALTER_TIME";
	/** 源方法对应数据库表的映射key */
	private String METHOD_TABLES_MAP = "METHOD_TABLES_MAP";
	/** 保存在当前线程中方法类型的key */
	private String THREAD_LOCAL_IS_QUERY_METHOD = "THREAD_LOCAL_IS_QUERY_METHOD";
	
	protected CacheAdapter cacheAdapter;
	protected CacheKeyProvider keyProvider;

	public DefaultCacheContext(CacheAdapter cacheAdapter) {
		this.cacheAdapter = cacheAdapter;
	}

	@Override
	public void setTables(Set<String> tables) {
		Map<String, Set<String>> map = (Map<String, Set<String>>) cacheAdapter.get(METHOD_TABLES_MAP);
		if(null == map) {
			map = new HashMap<>();
		}
		map.put(get(STATIC_UNIQUE_MARK), tables);
		cacheAdapter.set(METHOD_TABLES_MAP, map);
	}
	
	@Override
	public void addTables(Set<String> newTables) {
		Set<String> tables = getTables();
		if(null == tables) {
			tables = new HashSet<>();
		}
		tables.addAll(newTables);
		setTables(tables);
	}

	@Override
	public Set<String> getTables() {
		Map<String, Set<String>> map = (Map<String, Set<String>>) cacheAdapter.get(METHOD_TABLES_MAP);
		if(null == map) {
			return null;
		}
		return map.get(get(STATIC_UNIQUE_MARK));
	}

	@Override
	public Boolean isQuery() {
		Object isQuery = get(threadLocal);
		if(null != isQuery) {
			return isQuery();
		}
		Map<String, Boolean> isQueryMap = (Map<String, Boolean>) cacheAdapter.get(IS_QUERY_METHOD);
		return null == isQueryMap ? null : isQueryMap.get(get(DYNAMIC_UNIQUE_MARK));
	}

	@Override
	public void setQuery(boolean isQuery) {
		set(THREAD_LOCAL_IS_QUERY_METHOD, isQuery);
		Map<String, Boolean> isQueryMap = (Map<String, Boolean>) cacheAdapter.get(IS_QUERY_METHOD);
		if(null == isQueryMap) {
			isQueryMap = new HashMap<>();
		}
		isQueryMap.put(get(DYNAMIC_UNIQUE_MARK), isQuery);
		cacheAdapter.set(IS_QUERY_METHOD, isQueryMap);
	}	

	@Override
	public long getLastQueryTime() {
		Map<String, Long> isQueryMap = (Map<String, Long>) cacheAdapter.get(LAST_QUERY_TIME);
		if(null == isQueryMap) {
			return 0L;
		}
		Long time = isQueryMap.get(get(DYNAMIC_UNIQUE_MARK));
		return null == time ? 0L : time;
	}

	@Override
	public void setLastQueryTime(ConsistentTime lastQueryTime) {
		Map<String, Long> isQueryMap = (Map<String, Long>) cacheAdapter.get(LAST_QUERY_TIME);
		if(null == isQueryMap) {
			isQueryMap = new HashMap<>();
		}
		isQueryMap.put(get(DYNAMIC_UNIQUE_MARK), lastQueryTime.getTime());
		cacheAdapter.set(LAST_QUERY_TIME, isQueryMap);
	}

	@Override
	public void register(ProceedingJoinPoint joinPoint, CacheKeyProvider keyProvider) {
		threadLocal.remove();
		String methodName = joinPoint.getSignature().toLongString();
		set(STATIC_METHOD_FULL_NAME, methodName);
		set(STATIC_UNIQUE_MARK, keyProvider.process(methodName));
		set(DYNAMIC_UNIQUE_MARK, keyProvider.process(UniqueMethodMarkUtil.uniqueMark(joinPoint)));
		this.keyProvider = keyProvider;
	}

	@Override
	public void setTableLastAlterTime(String table, ConsistentTime time) {
		Map<String, Long> isQueryMap = (Map<String, Long>) cacheAdapter.get(LAST_ALTER_TIME);
		if(null == isQueryMap) {
			isQueryMap = new HashMap<>();
		}
		isQueryMap.put(table, time.getTime());
		cacheAdapter.set(LAST_ALTER_TIME, isQueryMap);
	}

	@Override
	public long getTableLastAlterTime(String table) {
		Map<String, Long> isQueryMap = (Map<String, Long>) cacheAdapter.get(LAST_ALTER_TIME);
		if(null == isQueryMap) {
			return 0L;
		}
		Long time = isQueryMap.get(table);
		return null == time ? 0L : time;
	}

	protected void set(Object key, Object value) {
		get().put(key, value);
	}

	protected <T> T get(Object key) {
		return (T) get().get(key);
	}

	protected void remove(Object key) {
		get().remove(key);
	}

	private Map<Object, Object> get() {
		Map<Object, Object> map = threadLocal.get();
		if(null == map) {
			map = new HashMap<>();
			threadLocal.set(map);
		}
		return map;
	}

	@Override
	public String getDynamicUniqueMark() {
		return get(DYNAMIC_UNIQUE_MARK);
	}

	@Override
	public String getStaticUniqueMark() {
		return get(STATIC_UNIQUE_MARK);
	}

	@Override
	public String toString() {
		return get(STATIC_METHOD_FULL_NAME);
	}
	
}

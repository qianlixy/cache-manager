package io.github.qianlixy.cache.wrapper;

import java.io.IOException;
import java.util.Map;

import io.github.qianlixy.cache.CasCacheAdapter;
import io.github.qianlixy.cache.context.ApplicationContext;

public class DistributedLock {
	
	private static final String KEY_CACHE_MANAGER_LOCK_MAP = "CACHE_MANAGER_LOCK_MAP";
	
	private static final byte LOCK = 0x1;
	private static final byte UNLOCK = 0x2;
	
	private CasCacheAdapter casCacheAdapter;
	
	public DistributedLock() {
		try {
			casCacheAdapter = ApplicationContext.getCacheAdaperFactory().buildCasCacheAdapter();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void lock(String methodFlag) {
		long oldCas = casCacheAdapter.getCas();
		Map<String, Byte> map = (Map<String, Byte>) casCacheAdapter.get(KEY_CACHE_MANAGER_LOCK_MAP);
		Byte lockFlag = map.get(methodFlag);
		while(true) {
			if(lockFlag != null && lockFlag.byteValue() == LOCK) {
				map = (Map<String, Byte>) casCacheAdapter.get(KEY_CACHE_MANAGER_LOCK_MAP);
				lockFlag = (byte) map.get(methodFlag);
			} else {
				map.put(methodFlag, LOCK);
				if(casCacheAdapter.checkAndSet(KEY_CACHE_MANAGER_LOCK_MAP, map, oldCas)) {
					break;
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void unlock(String methodFlag) {
		Map<String, Byte> map = (Map<String, Byte>) casCacheAdapter.get(KEY_CACHE_MANAGER_LOCK_MAP);
		Byte lockFlag = map.get(methodFlag);
		if(lockFlag != null && lockFlag.byteValue() == LOCK) {
			map.put(methodFlag, UNLOCK);
			casCacheAdapter.set(KEY_CACHE_MANAGER_LOCK_MAP, map);
		}
	}

}

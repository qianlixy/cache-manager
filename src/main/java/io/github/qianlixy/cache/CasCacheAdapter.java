package io.github.qianlixy.cache;

public interface CasCacheAdapter extends CacheAdapter {
	
	String KEY_CAS = "memcached_adapter_cas_value";

	boolean checkAndSet(String key, Object value, long oldCas);
	
	long getCas();
	
}

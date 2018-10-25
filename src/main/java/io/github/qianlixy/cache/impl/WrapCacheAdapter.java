package io.github.qianlixy.cache.impl;

import io.github.qianlixy.cache.CacheAdapter;
import io.github.qianlixy.cache.CacheSerializable;
import io.github.qianlixy.cache.exception.CacheSerializeException;
import io.github.qianlixy.cache.exception.ConsistentTimeException;
import io.github.qianlixy.cache.serialize.ProtostuffCacheSerializer;
import io.github.qianlixy.cache.serialize.WrapCacheSerializer;

/**
 * 文件描述: 包装缓存适配器<br>
 * 版权所有: Copyright (c) 2018年10月15日 YAPPAM, LTD.CO<br>
 * 完成日期: 2018年10月15日 下午4:53:38<br>
 *
 * @author dcy
 * @since 1.1.0
 */
public class WrapCacheAdapter implements CacheAdapter {

    private CacheAdapter nestAdapter;

    private CacheSerializable cacheSerializer;

    /**
     * 构造方法
     */
    public WrapCacheAdapter(CacheAdapter cacheAdapter) {
        this(cacheAdapter, null);
    }

    /**
     * 构造方法
     */
    public WrapCacheAdapter(CacheAdapter cacheAdapter, CacheSerializable cacheSerializer) {
        this.nestAdapter = cacheAdapter;
        if (null != cacheSerializer) {
            this.cacheSerializer = new WrapCacheSerializer(cacheSerializer);
        } else {
            this.cacheSerializer = new WrapCacheSerializer(new ProtostuffCacheSerializer());
        }
    }

    /*
     * (non-Javadoc)
     * @see com.yappam.commons.cache.CacheAdapter#set(java.lang.String,
     * java.lang.Object)
     */
    @Override
    public boolean set(String key, Object value) {
        return set(key, value, 0);
    }

    /*
     * (non-Javadoc)
     * @see com.yappam.commons.cache.CacheAdapter#set(java.lang.String,
     * java.lang.Object, int)
     */
    @Override
    public boolean set(String key, Object value, int time) {
        try {
            return nestAdapter.set(key, cacheSerializer.serialize(value), time);
        } catch (CacheSerializeException e) {
            return nestAdapter.set(key, value, time);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.yappam.commons.cache.CacheAdapter#get(java.lang.String)
     */
    @Override
    public Object get(String key) {
        Object cache = nestAdapter.get(key);
        if (null == cache || !(cache instanceof byte[])) {
            return cache;
        }
        try {
            return cacheSerializer.deserialize((byte[]) cache, null);
        } catch(CacheSerializeException e) {
            return cache;
        } catch (ClassNotFoundException e) {
            LOGGER.warn("An exception occurred while get the cache. {}", e.getMessage());
            return cache;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * @see com.yappam.commons.cache.CacheAdapter#remove(java.lang.String)
     */
    @Override
    public boolean remove(String key) {
        return nestAdapter.remove(key);
    }

    /*
     * (non-Javadoc)
     * @see com.yappam.commons.cache.CacheAdapter#consistentTime()
     */
    @Override
    public long consistentTime() throws ConsistentTimeException {
        return nestAdapter.consistentTime();
    }

}

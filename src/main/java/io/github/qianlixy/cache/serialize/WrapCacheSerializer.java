package io.github.qianlixy.cache.serialize;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.qianlixy.cache.CacheSerializable;
import io.github.qianlixy.cache.exception.CacheSerializeException;

/**
 * 文件描述: <br>
 * 版权所有: Copyright (c) 2018年10月14日 YAPPAM, LTD.CO<br>
 * 完成日期: 2018年10月14日 下午10:13:58<br>
 *
 * @author Yappam
 * @since 1.1.0
 */
public class WrapCacheSerializer implements CacheSerializable {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final Class<?>[] NOT_SUPPORT = {Byte.class, Integer.class, Character.class, Long.class, Float.class,
            Double.class, Boolean.class, String.class};

    private CacheSerializable nestSerializer;

    private int classNameMaxLength = 3;

    /**
     * 构造方法
     * 
     * @param serializer 序列化工具
     */
    public WrapCacheSerializer(CacheSerializable serializer) {
        this.nestSerializer = serializer;
    }

    /**
     * 构造方法
     * 
     * @param serializer 序列化工具
     * @param classNameMaxLength
     */
    public WrapCacheSerializer(CacheSerializable serializer, int classNameMaxLength) {
        this.nestSerializer = serializer;
        this.classNameMaxLength = classNameMaxLength;
    }

    /*
     * (non-Javadoc)
     * @see com.yappam.commons.cache.CacheSerializable#serialize(java.lang.Object)
     */
    @Override
    public byte[] serialize(Object cache) throws NullPointerException {
        if (null == cache) {
            throw new NullPointerException("Cache must be not null");
        }
        if (ArrayUtils.contains(NOT_SUPPORT, cache.getClass())) {
            throw new CacheSerializeException(String.format("The cache type [%s] does not support", cache.getClass()));
        }
        if (cache instanceof byte[]) {
            return (byte[]) cache;
        }
        if (isCollection(cache)) {
            cache = new CollectionCacheWrapper<>(cache);
        }
        String className = cache.getClass().getName();
        String pattern = "%s%0" + classNameMaxLength + "d%s";
        String prefix = String.format(pattern, CACHE_SERIALIZION_PREFIX, className.length(), className);
        if (log.isDebugEnabled()) {
            log.debug("Generate cache prefix [{}] for [{}]", prefix, cache.toString());
        }
        return ArrayUtils.addAll(prefix.getBytes(), nestSerializer.serialize(cache));
    }

    /*
     * (non-Javadoc)
     * @see com.yappam.commons.cache.CacheSerializable#deserialize(byte[],
     * java.lang.Class)
     */
    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) throws ClassNotFoundException {
        if (!isSerialized(bytes)) {
            throw new CacheSerializeException();
        }
        int length = toIntFromArray(bytes, CACHE_SERIALIZION_PREFIX.length(), classNameMaxLength);
        int start = CACHE_SERIALIZION_PREFIX.length() + classNameMaxLength;
        byte[] classSerialization = ArrayUtils.subarray(bytes, start + length, bytes.length);
        clazz = Class.forName(new String(ArrayUtils.subarray(bytes, start, start + length)));
        Object cache = nestSerializer.deserialize(classSerialization, clazz);
        if (cache instanceof CollectionCacheWrapper<?>) {
            return ((CollectionCacheWrapper<?>) cache).getCollection();
        }
        return cache;
    }

    public boolean isSerialized(byte[] bytes) {
        if (bytes.length < CACHE_SERIALIZION_PREFIX.length()) {
            return false;
        }
        return CACHE_SERIALIZION_PREFIX.equals(toStringFromArray(bytes, 0, CACHE_SERIALIZION_PREFIX.length()));
    }

    /**
     * 方法描述: 是否是集合类型<br>
     * 完成时间: 2018年10月16日 下午3:58:42<br>
     * 作者: dcy<br>
     *
     * @param cache 缓存对象
     * @return 是否是集合类型
     */
    private boolean isCollection(Object cache) {
        return cache instanceof Map || cache instanceof Collection<?>;
    }

    private int toIntFromArray(byte[] bytes, int start, int length) {
        return Integer.parseInt(toStringFromArray(bytes, start, length));
    }

    private String toStringFromArray(byte[] bytes, int start, int length) {
        return new String(ArrayUtils.subarray(bytes, start, start + length));
    }

}

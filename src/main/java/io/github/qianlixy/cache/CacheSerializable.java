package io.github.qianlixy.cache;

import io.github.qianlixy.cache.exception.CacheSerializeException;

/**
 * 文件描述: 缓存序列化接口<br>
 * 版权所有: Copyright (c) 2018年10月14日 YAPPAM, LTD.CO<br>
 * 完成日期: 2018年10月14日 下午8:13:07<br>
 *
 * @author dcy
 * @since 1.2.0
 */
public interface CacheSerializable {

    /**
     * 序列化缓存的前缀
     */
    String CACHE_SERIALIZION_PREFIX = "[_C_S_]";

    /**
     * 方法描述: 序列化缓存<br>
     * 完成时间: 2018年10月14日 下午8:13:46<br>
     * 作者: dcy<br>
     *
     * @param cache 缓存对象
     * @return 序列化结果
     * @throws NullPointerException 缓存对象为null时将抛出该异常
     */
    byte[] serialize(Object cache) throws NullPointerException, CacheSerializeException;

    /**
     * 方法描述: 反序列化缓存<br>
     * 完成时间: 2018年10月14日 下午8:18:32<br>
     * 作者: dcy<br>
     *
     * @param bytes 缓存字节数组
     * @param clazz 缓存Class
     * @return 缓存对象
     * @throws ClassNotFoundException 找不到Class时抛出该异常
     */
    Object deserialize(byte[] bytes, Class<?> clazz) throws ClassNotFoundException, CacheSerializeException;
    
}

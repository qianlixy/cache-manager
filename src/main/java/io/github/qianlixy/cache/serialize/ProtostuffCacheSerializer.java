package io.github.qianlixy.cache.serialize;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.qianlixy.cache.CacheSerializable;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * 文件描述: Protostuff序列化实现类<br>
 * 版权所有: Copyright (c) 2018年10月14日 YAPPAM, LTD.CO<br>
 * 完成日期: 2018年10月14日 下午8:15:38<br>
 *
 * @author dcy
 * @since 1.1.0
 */
public class ProtostuffCacheSerializer implements CacheSerializable {

    static {
        System.setProperty("protostuff.runtime.collection_schema_on_repeated_fields", "true");
    }

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 无参构造工具
     */
    private static Objenesis objenesis = new ObjenesisStd(true);

    @Override
    public byte[] serialize(Object cache) {
        log.debug("The cache serializing");
        if (null == cache) {
            throw new NullPointerException("The cache cannot be null");
        }
        @SuppressWarnings("unchecked")
        Schema<Object> schema = (Schema<Object>) RuntimeSchema.getSchema(cache.getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            return ProtostuffIOUtil.toByteArray(cache, schema, buffer);
        } finally {
            buffer.clear();
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        @SuppressWarnings("unchecked")
        Schema<Object> schema = (Schema<Object>) RuntimeSchema.getSchema(clazz);
        Object cache = objenesis.newInstance(clazz);
        ProtostuffIOUtil.mergeFrom(bytes, cache, schema);
        return cache;
    }

}

package io.github.qianlixy.cache.serialize;

/**
 * 文件描述: 集合类缓存包装对象<br>
 * 版权所有: Copyright (c) 2018年10月16日 YAPPAM, LTD.CO<br>
 * 完成日期: 2018年10月16日 下午3:43:30<br>
 *
 * @author dcy
 * @since 1.1.0
 */
public class CollectionCacheWrapper<T> {

    /**
     * 包装集合
     */
    private T collection;

    /**
     * 构造方法
     */
    public CollectionCacheWrapper() {
    }

    /**
     * 构造方法
     *
     * @param collection
     */
    public CollectionCacheWrapper(T collection) {
        super();
        this.collection = collection;
    }

    /**
     * 获取collection
     *
     * @return collection
     */
    public T getCollection() {
        return collection;
    }

    /**
     * 设置collection
     *
     * @param collection collection
     */
    public void setCollection(T collection) {
        this.collection = collection;
    }

}

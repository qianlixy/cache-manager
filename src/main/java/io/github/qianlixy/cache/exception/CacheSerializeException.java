package io.github.qianlixy.cache.exception;

/**
 * 文件描述: 缓存序列化异常<br>
 * 版权所有: Copyright (c) 2018年10月16日 YAPPAM, LTD.CO<br>
 * 完成日期: 2018年10月16日 上午10:29:43<br>
 *
 * @author dcy
 * @since 1.1.0
 */
public class CacheSerializeException extends RuntimeException {

    private static final long serialVersionUID = -2422095022715196860L;

    /**
     * 构造方法
     */
    public CacheSerializeException() {
        super();
    }

    /**
     * 构造方法
     *
     * @param message 异常原因
     * @param cause 内部异常
     */
    public CacheSerializeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造方法
     *
     * @param message 异常原因
     */
    public CacheSerializeException(String message) {
        super(message);
    }

    /**
     * 构造方法
     *
     * @param cause 内部异常
     */
    public CacheSerializeException(Throwable cause) {
        super(cause);
    }

}

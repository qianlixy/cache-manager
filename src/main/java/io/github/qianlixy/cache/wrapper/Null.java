package io.github.qianlixy.cache.wrapper;

import java.io.Serializable;

/**
 * <p>空值，等于<code>null</code>。用于存储到缓存客户端。</p>
 * <p>一些源方法的查询结果为null，如果将结果null缓存到缓存客户端，
 * 		下次查询将不能判断该结果是否是源方法的查询结果。
 * 		所以使用	该类代替null值结果缓存于缓存客户端，
 * 		只有该缓存失效，才会再次执行源方法查询结果</p>
 * @author qianli_xy@163.com
 * @since 1.0.0
 */
public class Null implements Serializable {

	private static final long serialVersionUID = 5337602685364675807L;

}

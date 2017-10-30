package io.github.qianlixy.cache.context;

import java.io.Serializable;
import java.util.Set;

/**
 * <p>方法的上下文信息</p>
 * <p>包括方法是否是查询方法，最后查询时间，对应的表集合等</p>
 * @author qianli_xy@163.com
 * @since 1.0.0
 */
public class MethodContextBean implements Serializable {

	private static final long serialVersionUID = -6947524565304621260L;
	
	private String methodName;
	private Boolean query;
	private Long lastQueryTime;
	private Set<String> tables;
	
	public MethodContextBean(String methodName) {
		this.methodName = methodName;
	}

	public Boolean getQuery() {
		return query;
	}

	public void setQuery(Boolean query) {
		this.query = query;
	}

	public Long getLastQueryTime() {
		return lastQueryTime;
	}

	public void setLastQueryTime(Long lastQueryTime) {
		this.lastQueryTime = lastQueryTime;
	}

	public Set<String> getTables() {
		return tables;
	}

	public void setTables(Set<String> tables) {
		this.tables = tables;
	}

	@Override
	public String toString() {
		return "MethodContextInfo [methodName=" + methodName + ", query=" + query + ", lastQueryTime=" + lastQueryTime
				+ ", tables=" + tables + "]";
	}
	
}

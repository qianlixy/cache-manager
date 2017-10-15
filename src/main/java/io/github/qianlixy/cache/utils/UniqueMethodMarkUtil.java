package io.github.qianlixy.cache.utils;

import org.aspectj.lang.ProceedingJoinPoint;

import com.alibaba.fastjson.JSONObject;

public class UniqueMethodMarkUtil {
	
	private static final String NULL = "null";
	
	public static String paramValue(Object param) {
		if(null == param) return NULL;
		if(param.getClass().isPrimitive() || isWrapClass(param.getClass())) 
			return String.valueOf(param);
		if(param instanceof String) 
			return (String) param;
		return JSONObject.toJSONString(param);
	}
	
	private static boolean isWrapClass(Class<?> clazz) {
		try {
			return ((Class<?>) clazz.getField("TYPE").get(null)).isPrimitive();
		} catch (Exception e) {
			return false;
		}
	}
	
	public static final String uniqueMark(ProceedingJoinPoint pjp) {
		String methodName = pjp.getSignature().getName();
		Object[] params = pjp.getArgs();
		StringBuilder mark = new StringBuilder(pjp.getTarget().getClass().getName());
		mark.append(".").append(methodName).append("(");
		if (null != params) {
			for (Object val : params) {
				if (null == val || null == val.toString()) {
					mark.append(NULL).append(",");
				} else if (val.getClass().isArray()) {
					mark.append("[");
					int arrayLength = BaseDataTypeArrayUtil.arrayLength(val);
					for (int i=0; i<arrayLength; i++) {
						mark.append(BaseDataTypeArrayUtil.arrayValue(val, i)).append(",");
					}
					if (arrayLength > 0)
						mark.deleteCharAt(mark.length() - 1);
					mark.append("],");
				} else {
					mark.append(paramValue(val)).append(",");
				}
			}
			if (params.length > 0)
				mark.deleteCharAt(mark.length() - 1);
		}
		mark.append(")");
		return mark.toString();
	}
	
}

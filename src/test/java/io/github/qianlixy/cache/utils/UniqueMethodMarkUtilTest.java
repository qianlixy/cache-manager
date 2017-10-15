package io.github.qianlixy.cache.utils;

import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

public class UniqueMethodMarkUtilTest {
	
	@Test
	public void testParamValue() {
		assertEquals("null", UniqueMethodMarkUtil.paramValue(null));
		
		assertEquals("string", UniqueMethodMarkUtil.paramValue("string"));
		
		assertEquals("111111", UniqueMethodMarkUtil.paramValue(111111));
		
		assertEquals("111111", UniqueMethodMarkUtil.paramValue(new Integer(111111)));
		
		assertEquals("1111.11", UniqueMethodMarkUtil.paramValue(1111.11));
		
		assertEquals("true", UniqueMethodMarkUtil.paramValue(true));
		
		Map<String, Object> mapCase = new LinkedHashMap<>();
		mapCase.put("test1", "test1");
		mapCase.put("test2", 111);
		mapCase.put("test3", true);
		assertEquals("{\"test1\":\"test1\",\"test2\":111,\"test3\":true}", UniqueMethodMarkUtil.paramValue(mapCase));
	}

}

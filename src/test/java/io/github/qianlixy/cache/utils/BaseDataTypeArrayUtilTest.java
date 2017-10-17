package io.github.qianlixy.cache.utils;

import static org.junit.Assert.*;
import org.junit.Test;

import io.github.qianlixy.cache.utils.BaseDataTypeArrayUtil;

public class BaseDataTypeArrayUtilTest {

	@Test
	public void test() {
		Object intArray = new int[]{1, 2, 3};
		Object byteArray = new byte[]{1, 2, 3};
		Object shortArray = new short[]{1, 2, 3};
		Object longArray = new long[]{1L, 2L, 3L};
		Object floatArray = new float[]{1.0F, 2.0F, 3.0F};
		Object doubleArray = new double[]{1.0, 2.0, 3.0};
		Object booleanArray = new boolean[]{true, false, true};
		Object charArray = new char[]{'a', 'b', 'a'};
		Object stringArray = new String[]{"a", "b", "c"};
		
		assertEquals(2, BaseDataTypeArrayUtil.arrayValue(intArray, 1));
		assertEquals(new Byte("2").byteValue(), BaseDataTypeArrayUtil.arrayValue(byteArray, 1));
		assertEquals(new Short("2").shortValue(), BaseDataTypeArrayUtil.arrayValue(shortArray, 1));
		assertEquals(2L, BaseDataTypeArrayUtil.arrayValue(longArray, 1));
		assertEquals(2.0F, BaseDataTypeArrayUtil.arrayValue(floatArray, 1));
		assertEquals(2.0, BaseDataTypeArrayUtil.arrayValue(doubleArray, 1));
		assertEquals(false, BaseDataTypeArrayUtil.arrayValue(booleanArray, 1));
		assertEquals('b', BaseDataTypeArrayUtil.arrayValue(charArray, 1));
		assertEquals("b", BaseDataTypeArrayUtil.arrayValue(stringArray, 1));
		
		int length = BaseDataTypeArrayUtil.arrayLength(intArray);
		for(int i=0; i<length; i++) {
			assertEquals(i+1, BaseDataTypeArrayUtil.arrayValue(intArray, i));
		}
	}
	
}

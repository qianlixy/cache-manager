package io.github.qianlixy.cache.utils;

import java.lang.reflect.Array;

/**
 * 基本数据类型数组操作的工具类
 * @author qianli_xy@163.com
 * @since 1.0.0
 */
public class BaseDataTypeArrayUtil {

	/**
	 * 获取基本数据类型数组长度
	 * @param array 基本数据类型数组
	 * @return 数组长度
	 */
	public static int arrayLength(Object array) {
		switch (array.getClass().getName()) {
			case "[I" : return ((int[]) array).length;
			case "[B" : return ((byte[]) array).length;
			case "[S" : return ((short[]) array).length;
			case "[J" : return ((long[]) array).length;
			case "[F" : return ((float[]) array).length;
			case "[D" : return ((double[]) array).length;
			case "[Z" : return ((boolean[]) array).length;
			case "[C" : return ((char[]) array).length;
			default : return ((Object[]) array).length;
		}
	}
	
	/**
	 * 获取基本数据类型数组指定位置的值
	 * @param array 基本数据类型数组
	 * @param index 指定位置
	 * @return 基本数据类型值
	 */
	public static Object arrayValue(Object array, int index) {
		switch (array.getClass().getName()) {
			case "[I" : return Array.getInt(array, index);
			case "[B" : return Array.getByte(array, index);
			case "[S" : return Array.getShort(array, index);
			case "[J" : return Array.getLong(array, index);
			case "[F" : return Array.getFloat(array, index);
			case "[D" : return Array.getDouble(array, index);
			case "[Z" : return Array.getBoolean(array, index);
			case "[C" : return Array.getChar(array, index);
			default : return ((Object[]) array)[index];
		}
	}
}

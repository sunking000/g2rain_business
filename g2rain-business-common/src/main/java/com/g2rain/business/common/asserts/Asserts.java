package com.g2rain.business.common.asserts;

import java.util.Collection;
import java.util.function.Predicate;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.g2rain.business.common.enums.SubErrorCodeEnum;
import com.g2rain.business.common.exception.BussinessRuntimeException;

public class Asserts {

	public static <T> void asserts(Predicate<T> predicate, final T t, final String errorCode, String key,
			String subErrorCode) {
		if (predicate.test(t)) {
			BussinessRuntimeException bre = new BussinessRuntimeException(errorCode);
			if (StringUtils.isNotBlank(subErrorCode)) {
				bre.addSubError(subErrorCode, key, "");
			}

			throw bre;
		}
	}

	/**
	 * t 为null时抛出errorCode的异常
	 * 
	 * @param t
	 * @param errorCode
	 */
	public static <T> void assertNull(T t, String errorCode, String key) {
		asserts(item -> item == null, t, errorCode, key, SubErrorCodeEnum.NOT_NULL.name());
	}
	
	/**
	 * t 为null时抛出errorCode的异常
	 * 
	 * @param t
	 * @param errorCode
	 */
	public static <T> void assertNull(T t, String errorCode) {
		asserts(item -> item == null, t, errorCode, null, null);
	}

	/**
	 * t 为空时抛出errorCode的异常
	 * 
	 * @param t
	 * @param errorCode
	 */
	public static <T extends Collection<?>> void assertEmpty(T t, String errorCode, String key) {
		asserts(item -> CollectionUtils.isEmpty(t), t, errorCode, key, SubErrorCodeEnum.NOT_EMPTY.name());
	}

	/**
	 * t 为空时抛出errorCode的异常
	 * 
	 * @param t
	 * @param errorCode
	 */
	public static <T extends Collection<?>> void assertEmpty(T t, String errorCode) {
		asserts(item -> CollectionUtils.isEmpty(t), t, errorCode, null, null);
	}

	/**
	 * 判断字符串为空
	 * 
	 * @param string
	 * @param errorCode
	 */
	public static void assertBlank(String string, String errorCode, String key) {
		asserts(item -> StringUtils.isBlank(string), string, errorCode, key, SubErrorCodeEnum.NOT_BLANK.name());
	}

	/**
	 * 判断字符串为空
	 * 
	 * @param string
	 * @param errorCode
	 */
	public static void assertBlank(String string, String errorCode) {
		asserts(item -> StringUtils.isBlank(string), string, errorCode, null, null);
	}
}

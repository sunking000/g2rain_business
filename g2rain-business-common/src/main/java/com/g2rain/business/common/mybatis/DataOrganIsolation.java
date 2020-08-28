package com.g2rain.business.common.mybatis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DataOrganIsolation {
	/**
	 * 列名
	 * 
	 * @return
	 */
	String columnName() default "store_organ_id";

	/**
	 * 是否包含当前的organId
	 * 
	 * @return
	 */
	boolean containCurrent() default false;
}
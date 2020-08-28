package com.g2rain.business.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
	
	boolean organIdRequire() default false;
	
	boolean storeIdRequire() default false;

	boolean userIdRequire() default false;

	boolean memberIdRequire() default false;
}

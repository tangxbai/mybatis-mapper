package com.viiyue.plugins.mybatis.annotation.bean;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.viiyue.plugins.mybatis.Constants;

/**
 * Used to change the default result mapping, change the default result mapping
 * to its own mapping name, the default generated result mapping name is
 * {@link Constants#DEFAULT_RESULT_MAP}.
 * 
 * @author tangxbai
 * @since 1.0.0
 */
@Inherited
@Documented
@Target( ElementType.TYPE )
@Retention( RetentionPolicy.RUNTIME )
public @interface ResultMap {
	String value();
}

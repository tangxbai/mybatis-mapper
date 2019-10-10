package com.viiyue.plugins.mybatis.annotation.member;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to identify whether a field is a logically delete field. If it is, then
 * it will be carried by default when it is deleted or queried.
 * 
 * @author tangxbai
 * @since 1.1.0
 */
@Documented
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention( RetentionPolicy.RUNTIME )
public @interface LogicallyDelete {

	/**
	 * The value carried in the query
	 */
	String selectValue();

	/**
	 * Value set when logically deleted
	 */
	String deletedValue();

	/**
	 * The type of the logically delete field value
	 */
	Class<?> type() default String.class;

}

package com.viiyue.plugins.mybatis.annotation.bean;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.viiyue.plugins.mybatis.metadata.Entity;

/**
 * Specify the default sort attribute field
 * 
 * @author tangxbai
 * @since 1.1.0
 */
@Inherited
@Documented
@Target( ElementType.TYPE )
@Retention( RetentionPolicy.RUNTIME )
public @interface DefaultOrderBy {

	/**
	 * The default sort field, where {@code #pk} represents the current primary
	 * key and {@code #pk/number} is used to get the specified primary key when
	 * multiple primary keys appear.
	 * 
	 * @see Entity#getProperty(String)
	 */
	String value();

	/**
	 * Sort type, {@code true} for reverse order, {@code false} for positive order.
	 */
	boolean desc() default true;

}

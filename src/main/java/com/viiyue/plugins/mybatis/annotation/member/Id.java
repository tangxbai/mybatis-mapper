package com.viiyue.plugins.mybatis.annotation.member;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to identify whether a field is a primary key, supports multiple primary
 * keys, but by default the primary key of {@code primary=true} or the first
 * primary key is used as the default primary key.
 * 
 * @author tangxbai
 * @since 1.0.0
 */
@Documented
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention( RetentionPolicy.RUNTIME )
public @interface Id {
	
	/**
	 * Used to specify which primary key is the default primary key when
	 * multiple primary keys are present. If {@code primary=true} is not
	 * specified, the first loaded primary key will appear as the default
	 * primary key.
	 * 
	 * @since 1.1.0
	 */
	boolean primary() default false;
	
}

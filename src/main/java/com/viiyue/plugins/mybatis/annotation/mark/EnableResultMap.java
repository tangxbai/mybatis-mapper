package com.viiyue.plugins.mybatis.annotation.mark;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to identify whether a method on the mapper interface needs to generate a
 * result map
 * 
 * @author tangxbai
 * @since 1.0.0
 */
@Documented
@Target( ElementType.METHOD )
@Retention( RetentionPolicy.RUNTIME )
public @interface EnableResultMap {}

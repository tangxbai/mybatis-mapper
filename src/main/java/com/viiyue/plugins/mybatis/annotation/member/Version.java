package com.viiyue.plugins.mybatis.annotation.member;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.viiyue.plugins.mybatis.api.NextVersionProvider;
import com.viiyue.plugins.mybatis.api.defaults.version.DefaultNextVersionProvider;

/**
 * Optimistic lock annotation, used to identify whether a field is a version
 * field.
 * 
 * @author tangxbai
 * @since 1.1.0
 */
@Documented
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention( RetentionPolicy.RUNTIME )
public @interface Version {
	Class<? extends NextVersionProvider> nextVersion() default DefaultNextVersionProvider.class;
}

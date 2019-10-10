package com.viiyue.plugins.mybatis.annotation.bean;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.viiyue.plugins.mybatis.annotation.member.Column;

/**
 * <p>
 * Used to filter some unneeded properties, some common scenarios, such as
 * defining a basic parent class, subclasses want to inherit some properties of
 * the parent class, but do not need all properties, or you want to filter some
 * properties of the current class can also use this annotation.
 * 
 * <p>In addition, you can also refer to the {@link Column#ignore() ignore}
 * attribute of {@link Column @Column} annotation.
 *
 * @author tangxbai
 * @since 1.1.0
 */
@Documented
@Inherited
@Target( ElementType.TYPE )
@Retention( RetentionPolicy.RUNTIME )
public @interface Excludes {
	String [] value();
}

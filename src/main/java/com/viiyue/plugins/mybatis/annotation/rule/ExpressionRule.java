package com.viiyue.plugins.mybatis.annotation.rule;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.viiyue.plugins.mybatis.enums.ExpressionStyle;

/**
 * Define rules for generating SQL expression styles
 * 
 * <ul>
 * <li>{@link ExpressionStyle#SHORT SHORT} - id = #{id}
 * <li>{@link ExpressionStyle#JAVA_TYPE JAVA_TYPE} - id = #{id, javaType=Integer}
 * <li>{@link ExpressionStyle#DB_TYPE DB_TYPE} - id = #{id, jdbcType=INTEGER} 
 * <li>{@link ExpressionStyle#FULL FULL} - id = #{id, javaType=Integer, jdbcType=INTEGER, typeHandler=Handler.class} 
 * </ul>
 * 
 * @author tangxbai
 * @since 1.0.0
 * @see ExpressionStyle
 */
@Inherited
@Target( ElementType.TYPE )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface ExpressionRule {
	ExpressionStyle value();
}

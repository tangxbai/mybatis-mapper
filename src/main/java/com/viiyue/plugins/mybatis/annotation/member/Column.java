package com.viiyue.plugins.mybatis.annotation.member;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

import com.viiyue.plugins.mybatis.annotation.bean.Excludes;
import com.viiyue.plugins.mybatis.enums.Type;

/**
 * Description of the field column to add more setting information to the column
 * 
 * @author tangxbai
 * @since 1.1.0
 */
@Documented
@Target( { ElementType.FIELD, ElementType.METHOD } )
@Retention( RetentionPolicy.RUNTIME )
public @interface Column {

	/**
	 * Custom database field name, converted by field name by default.
	 */
	String name() default "";

	/**
	 * Whether to ignore the database column, only for a single field,
	 * multi-field ignore, please use {@link Excludes @Excludes} annotation on
	 * the class.
	 */
	boolean ignore() default false;

	/**
	 * Whether the database column is nullable
	 * 
	 * @since Java Persistence 1.0
	 */
	boolean nullable() default true;

	/**
	 * Whether the column is included in SQL INSERT statements generated by the
	 * persistence provider
	 * 
	 * @since Java Persistence 1.0
	 */
	boolean insertable() default true;

	/**
	 * Whether the column is included in SQL UPDATE statements generated by the
	 * persistence provider
	 * 
	 * @since Java Persistence 1.0
	 */
	boolean updatable() default true;

	/**
	 * JDBC type, default is {@link Type#UNDEFINED}
	 */
	Type jdcbType() default Type.UNDEFINED;

	/**
	 * JDBC type handler
	 */
	Class<? extends TypeHandler<?>> typeHandler() default UnknownTypeHandler.class;

}

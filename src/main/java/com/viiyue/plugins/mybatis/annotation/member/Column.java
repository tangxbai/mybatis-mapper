/**
 * Copyright (C) 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.viiyue.plugins.mybatis.annotation.member;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

import com.viiyue.plugins.mybatis.Constants;
import com.viiyue.plugins.mybatis.annotation.bean.Excludes;

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
	String name() default Constants.EMPTY;

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
	 * JDBC type, default is {@link JdbcType#UNDEFINED}
	 * 
	 * @since 1.0.0, Updated in 1.3.5
	 */
	JdbcType jdbcType() default JdbcType.UNDEFINED;

	/**
	 * JDBC type handler
	 */
	Class<? extends TypeHandler<?>> typeHandler() default UnknownTypeHandler.class;

}

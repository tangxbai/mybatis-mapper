/*-
 * Apache　LICENSE-2.0
 * #
 * Copyright (C) 2017 - 2019 mybatis-mapper
 * #
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ------------------------------------------------------------------------
 */
package com.viiyue.plugins.mybatis.annotation.member;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.ibatis.mapping.StatementType;

import com.viiyue.plugins.mybatis.Constants;
import com.viiyue.plugins.mybatis.api.AutoIncrementStatementProvider;
import com.viiyue.plugins.mybatis.api.GeneratedValueProvider;
import com.viiyue.plugins.mybatis.enums.AutoIncrement;

/**
 * Specify the primary key generation strategy. There should be only one primary
 * key generation strategy in the entire entity object. If other fields also
 * want to generate default values, then use
 * {@link GeneratedValue @GeneratedValue} annotation.
 * 
 * @author tangxbai
 * @since 1.1.0
 * @see GeneratedValue
 * @see GeneratedValueProvider
 */
@Documented
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention( RetentionPolicy.RUNTIME )
public @interface GeneratedKey {

	// Highest level
	
	/**
	 * <p>
	 * Whether to use the primary key generation strategy supported by JDBC, If
	 * this attribute is set to {@code true}, then other attributes will be
	 * <b>ignored</b>.
	 * 
	 * <p>This is the <b>highest level</b> setting.
	 */
	boolean useGeneratedKeys() default false;

	// Second level
	
	/**
	 * <p>
	 * If you choose to generate a value for the primary key in this way, then
	 * {@code useGeneratedKeys} must be set to false and other attributes will
	 * be automatically <b>ignored</b>.
	 * 
	 * <p>This is the <b>second level</b> setting
	 */
	Class<? extends GeneratedValueProvider> valueProvider() default GeneratedValueProvider.class;

	// All of the following attributes are at the lowest level setting

	/**
	 * Whether the indication is executed before or after SQL execution, {@code true}
	 * means that the operation is performed before sql is executed, and {@code false}
	 * is performed afterwards.
	 */
	boolean before() default false;

	/**
	 * For querying SQL statements that auto-increment the primary key value, you can
	 * use the enumeration provided by the plugin or write your own statement.
	 * 
	 * @see AutoIncrement
	 */
	String statement() default Constants.EMPTY;

	/**
	 * Statement type
	 */
	StatementType statementType() default StatementType.PREPARED;

	/**
	 * Used to implement your own business SQL statement
	 */
	Class<? extends AutoIncrementStatementProvider> statementProvider() default AutoIncrementStatementProvider.class;

}

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
package com.viiyue.plugins.mybatis.annotation.bean;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.viiyue.plugins.mybatis.Constants;
import com.viiyue.plugins.mybatis.enums.NameStyle;

/**
 * Entity table name annotation, Used to get the table metadata information.
 *
 * @author tangxbai
 * @since 1.0.0
 */
@Documented
@Inherited
@Target( ElementType.TYPE )
@Retention( RetentionPolicy.RUNTIME )
public @interface Table {

	/**
	 * Custom table name, if the value is null will try to use the name style to
	 * convert class name.
	 * 
	 * @see #name()
	 */
	String value() default Constants.EMPTY;

	/**
	 * Custom table name, if the value is null will try to use the name style to
	 * convert class name.
	 * 
	 * @see #value()
	 */
	String name() default Constants.EMPTY;

	/**
	 * The prefix attribute will be used to as the table name prefix when value
	 * attribute is null, the important is value attribute be null.
	 */
	String prefix() default Constants.EMPTY;

	/**
	 * The suffix attribute will be used to as the table name suffix when value
	 * attribute is null, the important is value attribute be null.
	 */
	String suffix() default Constants.EMPTY;

	/**
	 * Remove the specified first and last character text
	 */
	String [] trim() default {};

	/**
	 * Name style to used the name convert, the important is value attribute be
	 * null.
	 * 
	 * @since 1.1.0
	 */
	NameStyle style() default NameStyle.DEFAULT;

}

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

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

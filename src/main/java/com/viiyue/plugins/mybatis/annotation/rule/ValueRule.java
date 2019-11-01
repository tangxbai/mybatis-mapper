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
package com.viiyue.plugins.mybatis.annotation.rule;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.viiyue.plugins.mybatis.enums.ValueStyle;

/**
 * Define the rules for generating sql values
 * 
 * <ul>
 * <li>{@link ValueStyle#SHORT SHORT} - #{id}
 * <li>{@link ValueStyle#JAVA_TYPE JAVA_TYPE} - #{id, javaType=Integer}
 * <li>{@link ValueStyle#DB_TYPE DB_TYPE} - #{id, jdbcType=INTEGER} 
 * <li>{@link ValueStyle#FULL FULL} - #{id, javaType=Integer, jdbcType=INTEGER, typeHandler=Handler.class} 
 * </ul>
 * 
 * @author tangxbai
 * @since 1.0.0
 */
@Inherited
@Target( ElementType.TYPE )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface ValueRule {
	ValueStyle value();
}

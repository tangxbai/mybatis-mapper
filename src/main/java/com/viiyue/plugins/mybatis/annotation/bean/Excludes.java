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

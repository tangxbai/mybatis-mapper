/*-
 * ApacheLICENSE-2.0
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
package com.viiyue.plugins.mybatis.annotation.mark;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.viiyue.plugins.mybatis.Constants;

/**
 * Used to reference the specified method inside the existing provider, the
 * class type is the {@code type} attribute in 
 * {@link SelectProvider @SelectProvider},
 * {@link UpdateProvider @UpdateProvider},
 * {@link DeleteProvider @DeleteProvider},
 * {@link InsertProvider @InsertProvider}.
 * 
 * @author tangxbai
 * @since 1.1.0
 */
@Documented
@Target( ElementType.METHOD )
@Retention( RetentionPolicy.RUNTIME )
public @interface Reference {
	
	/**
	 * The method name is the name of the method inside the existing provider
	 */
	String method();

	/**
	 * <p>
	 * In order to achieve a richer reference effect, you can add a text prefix
	 * before the return value.
	 * 
	 * <p><b>Note:</b> 
	 * The prerequisite for this property to take effect is that
	 * the method return value must be a {@code String}
	 */
	String prepend() default Constants.EMPTY;

	/**
	 * <p>
	 * In order to achieve a richer reference effect, you can append a text
	 * content after the return value.
	 * 
	 * <p><b>Note: </b> 
	 * The prerequisite for this property to take effect is that
	 * the method return value must be a {@code String}
	 */
	String append() default Constants.EMPTY;
	
}

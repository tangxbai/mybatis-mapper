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
package com.viiyue.plugins.mybatis.template.handler.base;

import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.parsing.TokenHandler;
import org.apache.ibatis.session.Configuration;

import com.viiyue.plugins.mybatis.utils.ObjectUtil;

/**
 * Template syntax handler
 *
 * @author tangxbai
 * @since 1.1.0
 */
public class TemplateTokenHandler<T> implements TokenHandler {

	private final AbstractHandler<T> handler;
	private final Configuration configuration;
	private final SqlCommandType commandType;
	private final Class<?> modelBeanType;
	private final String original;
	private final T additionalParameter;

	public TemplateTokenHandler(
		AbstractHandler<T> handler, 
		Configuration configuration, 
		SqlCommandType commandType, 
		Class<?> modelBeanType,
		String original, 
		T additionalParameter ) {
		this.handler = handler;
		this.configuration = configuration;
		this.commandType = commandType;
		this.modelBeanType = modelBeanType;
		this.original = original;
		this.additionalParameter = additionalParameter;
	}

	@Override
	public String handleToken( String fragment ) {
		return handler.handle( this, fragment, additionalParameter );
	}

	public Configuration configuration() {
		return configuration;
	}

	public SqlCommandType commandType() {
		return commandType;
	}
	
	public SqlCommandType commandType( SqlCommandType defaultValue ) {
		return ObjectUtil.defaultIfNull( commandType, defaultValue );
	}

	public Class<?> modelBeanType() {
		return modelBeanType;
	}

	public String originalContent() {
		return original;
	}

}

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
package com.viiyue.plugins.mybatis.exceptions;

import com.viiyue.plugins.mybatis.utils.BuilderUtil;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * Mybatis-mapper exception
 * 
 * @author tangxbai
 * @since 1.0.0
 */
public class MybatisMapperException extends RuntimeException {

	private static final long serialVersionUID = -7698983984699492487L;

	public MybatisMapperException() {
		super();
	}
	
	public MybatisMapperException( String message ) {
		this( null, message, new Object[ 0 ] );
	}
	
	public MybatisMapperException( String message, Object ... formats ) {
		this( null, message, formats );
	}
	
	public MybatisMapperException( Throwable cause ) {
		this( cause, cause == null ? null : cause.getMessage() );
	}
	
	public MybatisMapperException( Throwable cause, String message ) {
		this( cause, message, new Object[ 0 ] );
	}
	
	public MybatisMapperException( Throwable cause, String message, Object ... formats ) {
		super( StringUtil.format( BuilderUtil.quote( message ), formats ), cause );
	}

}

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
package com.viiyue.plugins.mybatis.exceptions;

/**
 * Reflection exception, which may be thrown when performing various reflection
 * lookups.
 *
 * @author tangxbai
 * @since 1.1.0
 */
public class ReflectionException extends MybatisMapperException {

	private static final long serialVersionUID = -8313674034097145320L;

	public ReflectionException() {
		super();
	}
	
	public ReflectionException( String message ) {
		this( null, message, new Object[ 0 ] );
	}
	
	public ReflectionException( String message, Object ... formats ) {
		this( null, message, formats );
	}
	
	public ReflectionException( Throwable cause ) {
		this( cause, cause == null ? null : cause.getMessage() );
	}
	
	public ReflectionException( Throwable cause, String message ) {
		this( cause, message, new Object[ 0 ] );
	}
	
	public ReflectionException( Throwable cause, String message, Object ... formats ) {
		super( cause, message, formats );
	}

}

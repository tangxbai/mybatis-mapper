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
package com.viiyue.plugins.mybatis.utils;

import com.viiyue.plugins.mybatis.exceptions.ParameterValidateException;

/**
 * Assertion tool class for simple and convenient execution of judgment
 * operations
 *
 * @author tangxbai
 * @since 1.0.0
 */
public class Assert {
	
	private Assert() {}

	/**
	 * Assert that the target object must not be null
	 * 
	 * @param object target object
	 * @param message reject message
	 * @param formats formatting parameter
	 * @exception ParameterValidateException if the assertion does not pass, this exception will be thrown.
	 */
	public static void notNull( Object object, String message, Object ... formats ) {
		if ( object == null ) {
			throw new ParameterValidateException( message, formats );
		}
	}
	
	/**
	 * Assert that the target object must be null
	 * 
	 * @param object target object
	 * @param message reject message
	 * @param formats formatting parameter
	 * @exception ParameterValidateException if the assertion does not pass, this exception will be thrown.
	 */
	public static void isNull( Object object, String message, Object ... formats ) {
		if ( object != null ) {
			throw new ParameterValidateException( message );
		}
	}
	
	/**
	 * Assert that the target string cannot be blank text
	 * 
	 * @param text target string text
	 * @param message reject message
	 * @param formats formatting parameter
	 * @exception ParameterValidateException if the assertion does not pass, this exception will be thrown.
	 */
	public static void notBlank( String text, String message, Object ... formats ) {
		if ( StringUtil.isBlank( text ) ) {
			throw new ParameterValidateException( message, formats );
		}
	}
	
	/**
	 * Assert that the target string cannot be empty text
	 * 
	 * @param text target string text
	 * @param message reject message
	 * @param formats formatting parameter
	 * @exception ParameterValidateException if the assertion does not pass, this exception will be thrown.
	 */
	public static void notEmpty( String text, String message, Object ... formats ) {
		if ( StringUtil.isEmpty( text ) ) {
			throw new ParameterValidateException( message, formats );
		}
	}
	
	/**
	 * Assert that the target object cannot be empty
	 * 
	 * @param object target object
	 * @param message reject message
	 * @param formats formatting parameter
	 * @exception ParameterValidateException if the assertion does not pass, this exception will be thrown.
	 */
	public static void notEmpty( Object target, String message, Object ... formats ) {
		if ( ObjectUtil.isEmpty( target ) ) {
			throw new ParameterValidateException( message, formats );
		}
	}
	
	/**
	 * Assert that the target expression must be true
	 * 
	 * @param object target boolean expression
	 * @param message reject message
	 * @param formats formatting parameter
	 * @exception ParameterValidateException if the assertion does not pass, this exception will be thrown.
	 */
	public static void isTrue( boolean expression, String message, Object ... formats ) {
		if ( !expression ) {
			throw new ParameterValidateException( message, formats );
		}
	}
	
	/**
	 * Assert that the target expression must be false
	 * 
	 * @param object target boolean expression
	 * @param message reject message
	 * @param formats formatting parameter
	 * @exception ParameterValidateException if the assertion does not pass, this exception will be thrown.
	 */
	public static void isFalse( boolean expression, String message, Object ... formats ) {
		if ( expression ) {
			throw new ParameterValidateException( message, formats );
		}
	}
	
}

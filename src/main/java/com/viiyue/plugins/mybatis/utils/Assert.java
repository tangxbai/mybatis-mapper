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

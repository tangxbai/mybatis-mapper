package com.viiyue.plugins.mybatis.exceptions;

import com.viiyue.plugins.mybatis.utils.Assert;

/**
 * Parameter validate exception
 *
 * @author tangxbai
 * @since 1.1.0
 * @see Assert
 */
public class ParameterValidateException extends MybatisMapperException {

	private static final long serialVersionUID = -4259534987927427713L;

	public ParameterValidateException() {
		super();
	}
	
	public ParameterValidateException( String message ) {
		this( null, message, new Object[ 0 ] );
	}
	
	public ParameterValidateException( String message, Object ... formats ) {
		this( null, message, formats );
	}
	
	public ParameterValidateException( Throwable cause ) {
		this( cause, cause == null ? null : cause.getMessage() );
	}
	
	public ParameterValidateException( Throwable cause, String message ) {
		this( cause, message, new Object[ 0 ] );
	}
	
	public ParameterValidateException( Throwable cause, String message, Object ... formats ) {
		super( cause, message, formats );
	}

}

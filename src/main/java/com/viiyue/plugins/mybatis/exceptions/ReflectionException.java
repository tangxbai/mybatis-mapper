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

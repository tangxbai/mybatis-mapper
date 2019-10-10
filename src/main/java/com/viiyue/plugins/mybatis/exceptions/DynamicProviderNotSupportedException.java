package com.viiyue.plugins.mybatis.exceptions;

/**
 * The dynamic provider did not find the exception. When matching the specific
 * provider through the class, the exception is thrown if it does not exist.
 * 
 * @author tangxbai
 * @since 1.1.0
 */
public class DynamicProviderNotSupportedException extends MybatisMapperException {

	private static final long serialVersionUID = -1136328777222110190L;

	public DynamicProviderNotSupportedException() {
		super();
	}
	
	public DynamicProviderNotSupportedException( String message ) {
		this( null, message, new Object[ 0 ] );
	}
	
	public DynamicProviderNotSupportedException( String message, Object ... formats ) {
		this( null, message, formats );
	}
	
	public DynamicProviderNotSupportedException( Throwable cause ) {
		this( cause, cause == null ? null : cause.getMessage() );
	}
	
	public DynamicProviderNotSupportedException( Throwable cause, String message ) {
		this( cause, message, new Object[ 0 ] );
	}
	
	public DynamicProviderNotSupportedException( Throwable cause, String message, Object ... formats ) {
		super( cause, message, formats );
	}

}

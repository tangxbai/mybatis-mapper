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

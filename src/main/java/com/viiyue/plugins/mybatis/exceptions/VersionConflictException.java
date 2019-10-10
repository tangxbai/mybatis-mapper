package com.viiyue.plugins.mybatis.exceptions;

/**
 * Version conflict exception
 *
 * @author tangxbai
 * @since 1.1.0
 */
public class VersionConflictException extends MybatisMapperException {

	private static final long serialVersionUID = -2574983178122696584L;

	public VersionConflictException() {
		super();
	}
	
	public VersionConflictException( String message ) {
		super( message );
	}
	
}

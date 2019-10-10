package com.viiyue.plugins.mybatis.exceptions;

import com.viiyue.plugins.mybatis.utils.ObjectUtil;

/**
 * Parameter type mismatch exception
 *
 * @author tangxbai
 * @since 1.1.0
 */
public class TypeMismatchException extends MybatisMapperException {

	private static final long serialVersionUID = 5131649326844502972L;
	
	private final Class<?> type;
	
	public TypeMismatchException( Class<?> type, String message, Object ... formats ) {
		super( null, message, formats );
		this.type = type;
	}

	public Class<?> getType() {
		return ObjectUtil.defaultIfNull( type, Object.class );
	}

	@Override
	public String getMessage() {
		return super.getMessage() + " type must be <" + getType().getName() + ">";
	}

}

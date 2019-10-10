package com.viiyue.plugins.mybatis.template.handler.base;

import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.parsing.TokenHandler;
import org.apache.ibatis.session.Configuration;

import com.viiyue.plugins.mybatis.utils.ObjectUtil;

/**
 * Template syntax handler
 *
 * @author tangxbai
 * @since 1.1.0
 */
public class TemplateTokenHandler<T> implements TokenHandler {

	private final AbstractHandler<T> handler;
	private final Configuration configuration;
	private final SqlCommandType commandType;
	private final Class<?> modelBeanType;
	private final String original;
	private final T additionalParameter;

	public TemplateTokenHandler(
		AbstractHandler<T> handler, 
		Configuration configuration, 
		SqlCommandType commandType, 
		Class<?> modelBeanType,
		String original, 
		T additionalParameter ) {
		this.handler = handler;
		this.configuration = configuration;
		this.commandType = commandType;
		this.modelBeanType = modelBeanType;
		this.original = original;
		this.additionalParameter = additionalParameter;
	}

	@Override
	public String handleToken( String fragment ) {
		return handler.handle( this, fragment, additionalParameter );
	}

	public Configuration configuration() {
		return configuration;
	}

	public SqlCommandType commandType() {
		return commandType;
	}
	
	public SqlCommandType commandType( SqlCommandType defaultValue ) {
		return ObjectUtil.defaultIfNull( commandType, defaultValue );
	}

	public Class<?> modelBeanType() {
		return modelBeanType;
	}

	public String originalContent() {
		return original;
	}

}

package com.viiyue.plugins.mybatis.scripting.parser;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.MappedStatement;

import com.viiyue.plugins.mybatis.annotation.mark.Reference;
import com.viiyue.plugins.mybatis.utils.MethodUtil;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * Mapper bridging method, mainly used to package some auxiliary information.
 * 
 * @author tangxbai
 * @since 2019/10/07
 */
public final class BridgeMethod {

	/** Dynamic provider method */
	private Method providerMethod;
	
	/** Mapper interface method */
	private Method interfaceMethod;
	
	/** The text appended before the return value */
	private String prepend;
	
	/** Append text at the end of the return value */
	private String append;

	protected BridgeMethod( Method providerMethod, Method interfaceMethod ) {
		this( providerMethod, interfaceMethod, null );
	}

	protected BridgeMethod( Method providerMethod, Method interfaceMethod, Reference reference ) {
		this.providerMethod = providerMethod;
		this.interfaceMethod = interfaceMethod;
		if ( reference != null ) {
			this.prepend = reference.prepend();
			this.append = reference.append();
		}
	}

	public Method getProviderMethod() {
		return providerMethod;
	}

	public Method getInterfaceMethod() {
		return interfaceMethod;
	}

	public String getPrefix() {
		return prepend;
	}

	public String getSuffix() {
		return append;
	}

	public String getInterfaceMethodName() {
		return interfaceMethod.getName();
	}

	public String getProviderMethodName() {
		return providerMethod.getName();
	}

	public Class<?> getInterfaceMethodReturnType() {
		return providerMethod.getReturnType();
	}

	public Class<?> getProviderMethodReturnType() {
		return providerMethod.getReturnType();
	}

	public void useReference( Reference reference ) {
		if ( reference != null ) {
			this.prepend = reference.prepend();
			this.append = reference.prepend();
		}
	}

	public String getWrappedResult( Object returnValue ) {
		if ( returnValue == null ) {
			return null;
		}
		String result = returnValue.toString();
		if ( StringUtil.isNotEmpty( prepend ) ) {
			result = prepend + result;
		}
		if ( StringUtil.isNotEmpty( append ) ) {
			result += append;
		}
		return result;
	}

	public Object getReturnValue( Object instance, MappedStatement ms, Class<?> modelBeanType ) {
		Object returnValue = null;
		try {
			returnValue = MethodUtil.invoke( instance, providerMethod, ms );
		} catch ( Exception e ) {
			returnValue = MethodUtil.invoke( instance, providerMethod, ms, modelBeanType );
		}
		return returnValue;
	}

}

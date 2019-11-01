/**
 * Copyright (C) 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.viiyue.plugins.mybatis.utils;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import com.viiyue.plugins.mybatis.exceptions.ReflectionException;

/**
 * Method operation tool class
 *
 * @author tangxbai
 * @since 1.1.0
 */
public class MethodUtil extends MethodUtils {
	
	private MethodUtil () {}

	public static final String [] SETTER_PREFIX = { "set", "add" };
	public static final String [] GETTER_PREFIX = { "get", "read", "find", "is" };
	
	public static String getPropertyNameByReadMethod( Method readMethod ) {
		return getPropertyNameByReadMethod( readMethod.getName() );
	}
	
	public static String getPropertyNameByReadMethod( String methodName ) {
		return getPropertyName( methodName, GETTER_PREFIX );
	}
	
	public static String getPropertyNameByWriteMethod( Method writeMethod ) {
		return getPropertyNameByWriteMethod( writeMethod.getName() );
	}
	
	public static String getPropertyNameByWriteMethod( String methodName ) {
		return getPropertyName( methodName, SETTER_PREFIX );
	}
	
	public static String getPropertyName( String methodName, String ... prefixs ) {
		for ( String prefix : prefixs ) {
			if ( methodName.startsWith( prefix ) ) {
				return StringUtils.uncapitalize( methodName.substring( prefix.length() ) );
			}
		}
		return methodName;
	}
	
	public static Method findMethod( Class<?> type, boolean forceAccess, String methodName, Class<?> ... parameterTypes ) {
		Method method = null;
		if ( type == null || methodName == null ) {
			return method;
		}
		method = getAccessibleMethod( type, methodName, parameterTypes );
		if ( method == null ) {
			method = getMatchingMethod( type, methodName, parameterTypes );
		}
		makeAccessible( method, forceAccess );
		return method;
	}
	
	public static <T> T invoke( Object object, String methodName, Object ... arguments ) {
		return invoke( object, true, methodName, arguments );
	}

	public static <T> T invoke( Object object, boolean forceAccess, String methodName, Object ... arguments ) {
		if ( object == null || methodName == null ) {
			return null;
		}
		Method method = null;
		Class<?> [] parameterTypes = ClassUtil.toClass( arguments );
		if ( ObjectUtil.isEmpty( parameterTypes ) ) {
			method = findMethod( object.getClass(), forceAccess, methodName );
		} else {
			method = findMethod( object.getClass(), forceAccess, methodName, parameterTypes );
		}
		if ( method == null ) {
			return null;
		}
		return invoke( object, forceAccess, method, arguments );
	}
	
	public static <T> T invoke( Object object, Method method, Object ... arguments ) {
		return invoke( object, true, method, arguments );
	}
	
	public static <T> T invoke( Object object, boolean forceAccess, Method method, Object ... arguments ) {
		if ( object == null || method == null ) {
			return null;
		}
		try {
			makeAccessible( method, forceAccess );
			return ( T ) method.invoke( object, arguments );
		} catch ( Exception e ) {
			throw new ReflectionException( e.getMessage(), e );
		}
	}
	
	private static void makeAccessible( Method method, boolean forceAccess ) {
		if ( method != null && forceAccess && !method.isAccessible() ) {
			method.setAccessible( true );
		}
	}

}

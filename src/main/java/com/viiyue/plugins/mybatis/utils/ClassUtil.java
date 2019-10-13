/*-
 * ApacheLICENSE-2.0
 * #
 * Copyright (C) 2017 - 2019 mybatis-mapper
 * #
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ------------------------------------------------------------------------
 */
package com.viiyue.plugins.mybatis.utils;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.ClassUtils;
import org.apache.ibatis.io.Resources;

import com.viiyue.plugins.mybatis.exceptions.ReflectionException;

/**
 * Class reflection operation tool class
 * 
 * @author tangxbai
 * @since 1.1.0
 */
public class ClassUtil extends ClassUtils {
	
	private ClassUtil() {}
	
	private static final Set<Class<?>> commonTypes = new HashSet<Class<?>>();
	private static final Map<String, Class<?>> mapperInterfaceMappings = new HashMap<String, Class<?>>();
	
	static {
		commonTypes.add( String.class );
		commonTypes.add( Date.class );
		commonTypes.add( BigDecimal.class );
		commonTypes.add( BigInteger.class );
		commonTypes.add( Object.class );
		commonTypes.add( Map.class );
		commonTypes.add( HashMap.class );
		commonTypes.add( List.class );
		commonTypes.add( ArrayList.class );
		commonTypes.add( Iterator.class );
		commonTypes.add( Collection.class );
		commonTypes.add( ResultSet.class );
	}
	
	/**
	 * Determine whether the class type is a common type of mybatis. In some
	 * scenarios, we can use the class name for short.
	 * 
	 * @param type class type
	 * @return {@code true} is a common type of mybatis, {@code false} is not.
	 */
	public static boolean isCommonType( Class<?> type ) {
		return isPrimitiveOrWrapper( type ) || commonTypes.contains( type );
	}
	
	/**
	 * Gets a List of superclasses for the given class.
	 * 
	 * @param type the class to look up, may be {@code null}
	 * @return {@code List} of all superclasses, no object class type.
	 */
	public static List<Class<?>> getAllTypes( Class<?> type ) {
		List<Class<?>> classes = new LinkedList<Class<?>>();
		for ( ; type != Object.class; type = type.getSuperclass() ) {
			classes.add( type );
		}
		Collections.reverse( classes );
		return Collections.unmodifiableList( classes );
	}
	
	/**
	 * <p>Gets a {@code List} of all interfaces implemented by the given 
	 * class and its superclasses.</p>
	 * 
	 * @param type the class to look up, may be {@code null}
	 * @param excludes the type filter array
	 * @return the {@code List} of interfaces in order, empty {@code List} if null input.
	 */
	public static List<Class<?>> getAllInterfaces( Class<?> type, Class<?> ... excludes ) {
		List<Class<?>> interfaces = getAllInterfaces( type );
		if ( interfaces == null ) {
			return Collections.emptyList();
		}
		interfaces.removeAll( Arrays.asList( excludes ) );
		return Collections.unmodifiableList( interfaces );
	}
	
	/**
	 * Load a class object through the class full class path
	 * 
	 * @param classpath class full class path
	 * @return the class represented by class path
	 * @throws RuntimeException if the class is not found
	 */
	public static Class<?> forName( String classpath ) {
		Class<?> clazz = mapperInterfaceMappings.get( classpath );
		if ( clazz == null ) {
			try {
				clazz = Resources.classForName( classpath );
				mapperInterfaceMappings.put( classpath, clazz );
			} catch ( ClassNotFoundException e ) {
				throw new RuntimeException( e.getMessage(), e );
			}
		}
		return clazz;
	}
	
	/**
	 * Create a new instance by class type, if the parameter array is empty, 
	 * call the default constructor, otherwise call the constructor 
	 * of the corresponding parameter list.
	 * 
	 * @param <T> instance generic type
	 * @param type the class type
	 * @param arguments constructor parameter array, may be {@code null}
	 * @return a new instance object
	 */
	public static <T> T newInstance( Class<T> type, Object ... arguments ) {
		try {
			if ( ObjectUtil.isEmpty( arguments ) ) {
				return type.newInstance();
			}
			Constructor<T> constructor = type.getConstructor( toClass( arguments ) );
			return constructor.newInstance( arguments );
		} catch ( Exception e ) {
			throw new ReflectionException( e.getMessage(), e );
		}
	}
	
	/**
	 * Convert the text value to the specified type value
	 * 
	 * <pre>
	 * &#47;&#47; primitive/primitive wrapper
	 * Integer number = (Integer) simpleTypeConvert("1111", int.class);
	 * Integer number = (Integer) simpleTypeConvert("1111", Integer.class);
	 * 
	 * &#47;&#47; String
	 * String text = (String) simpleTypeConvert("text value", String.class);
	 * 
	 * &#47;&#47; Enumeration value
	 * YourEnum e = (YourEnum) simpleTypeConvert("ENUM_NAME", YourEnum.class);
	 * </pre>
	 * 
	 * @param value the text value
	 * @param valueType converted value type, support for String, primitive type, primitive wrapper and enum type.
	 * @return converted value
	 */
	public static Object simpleTypeConvert( String value, Class<?> valueType ) {
		if ( value == null || valueType == null || Objects.equals( valueType, String.class ) ) {
			return value;
		}
		if ( ObjectUtil.equalsAny( valueType, int.class, Integer.class ) ) {
			return Integer.parseInt( value );
		}
		if ( ObjectUtil.equalsAny( valueType, long.class, Long.class ) ) {
			return Long.parseLong( value );
		}
		if ( ObjectUtil.equalsAny( valueType, float.class, Float.class ) ) {
			return Float.parseFloat( value );
		}
		if ( ObjectUtil.equalsAny( valueType, double.class, Double.class ) ) {
			return Double.parseDouble( value );
		}
		if ( ObjectUtil.equalsAny( valueType, boolean.class, Boolean.class ) ) {
			return Boolean.parseBoolean( value ) || value.equalsIgnoreCase( "Y" ) || value.equalsIgnoreCase( "YES" );
		}
		if ( ObjectUtil.equalsAny( valueType, char.class, Character.class ) ) {
			return value.charAt( 0 );
		}
		if ( ObjectUtil.equalsAny( valueType, short.class, Short.class ) ) {
			return Short.parseShort( value );
		}
		if ( ObjectUtil.equalsAny( valueType, byte.class, Byte.class ) ) {
			return Byte.parseByte( value );
		}
		if ( valueType.isEnum() ) {
			for ( Enum<?> em : ( Enum<?> [] ) valueType.getEnumConstants() ) {
				if ( Objects.equals( em.name(), value ) ) {
					return em;
				}
			}
			throw new ReflectionException( "Enumeration object named #{0}# is not found in enumeration #{1}#", value, valueType.getName() );
		}
		return valueType.cast( value );
	}

}

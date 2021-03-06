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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Java entity bean tool class
 *
 * @author tangxbai
 * @since 1.1.0, Updated in 1.3.3
 */
public class BeanUtil {
	
	private BeanUtil() {}
	
	private static final Map<Class<?>, Set<Method>> methodMappgins = new ConcurrentHashMap<Class<?>, Set<Method>>();
	
//	It was removed in 1.3.3 and this method is no longer used.
//	private static final ThreadLocal<Map<String, Object>> propertyValueLocals = new ThreadLocal<Map<String,Object>>();
	
	/**
	 * Gets a collection of all non-null attribute names for the bean. This
	 * method will store all non-null values in the current thread variable. All
	 * non-null values can be obtained by {@link #getPropertyValues()}.
	 * 
	 * @param instance object instance
	 * @return attribute name set 
	 */
	public static Map<String, Object> getPropertiesIfNotNull( Object instance ) {
		if ( instance == null ) {
			return Collections.emptyMap();
		}
//		Removed in 1.3.3
//		Set<String> properties = null;
		Map<String, Object> propertyValues = null;
		Class<?> type = instance.getClass();
		Set<Method> methods = methodMappgins.get( type );
		if ( methods == null ) {
			synchronized ( methodMappgins ) {
				PropertyDescriptor [] descriptors = FieldUtil.getPropertyDescriptors( type );
				methods = new HashSet<Method>( descriptors.length );
//				Removed in 1.3.3
//				properties = new HashSet<String>( descriptors.length );
				propertyValues = new HashMap<String, Object>( descriptors.length );
				for ( PropertyDescriptor pd : descriptors ) {
					if ( ObjectUtil.isDifferent( "class", pd.getName() ) ) { // class attribute is belongs to Object
						Method getter = pd.getReadMethod();
						methods.add( getter );
						Object returnValue = MethodUtil.invoke( instance, true, getter );
						if ( returnValue != null ) {
							propertyValues.put( pd.getName(), returnValue );
//							Removed in 1.3.3
//							properties.add( pd.getName() );
						}
					}
				}
				methodMappgins.put( type, methods );
//				Removed in 1.3.3
//				propertyValueLocals.set( propertyValues );
				return propertyValues;
			}
		}
//		Removed in 1.3.3
//		properties = new HashSet<String>( methods.size() );
		propertyValues = new HashMap<String, Object>( methods.size() );
		for ( Method method : methods ) {
			Object returnValue = MethodUtil.invoke( instance, true, method );
			if ( returnValue != null ) {
				String propertyName = MethodUtil.getPropertyNameByReadMethod( method );
//				Removed in 1.3.3
//				properties.add( propertyName );
				propertyValues.put( propertyName, returnValue );
			}
		}
//		Removed in 1.3.3
//		propertyValueLocals.set( propertyValues );
		return propertyValues;
	}
	
	/**
	 * Copy a new entity object, and you can modify the copied property value
	 * 
	 * @param target target object instance
	 * @param properties modified property collection, similar to map key-value
	 * @return the copyed entity object
	 */
	public static <T> T copy( T target, Object ... properties ) {
		boolean hasModifyProperties = ObjectUtil.isNotEmpty( properties );
		Class<T> targetType = ( Class<T> ) target.getClass();
		Map<Object, Object> modify = hasModifyProperties ? MapUtil.newMap( properties ) : null;
		T newCopyed = ClassUtil.newInstance( targetType );
		for ( PropertyDescriptor pd : FieldUtil.getPropertyDescriptors( targetType ) ) {
			if ( ObjectUtil.isDifferent( "class", pd.getName() ) ) {
				Method getter = pd.getReadMethod();
				Method setter = pd.getWriteMethod();
				Assert.isFalse( getter == null || setter == null, "Not a standard JavaBean, no corresponding Getter/Setter method found" );
				Object returnValue = hasModifyProperties ? modify.get( pd.getName() ) : MethodUtil.<Object>invoke( target, true, getter );
				if ( returnValue != null ) {
					MethodUtil.<Void>invoke( newCopyed, true, setter, returnValue );
				}
			}
		}
		return newCopyed;
	}
	
//	----------------------------------------------------------------------------------------------------------------------------------------------
//	It was removed in 1.3.3 and this method is no longer used.                                                                                  |
//	--------------------------------------------------------------------------------------------------------------------------------------------|-
//	/**                                                                                                                                         |
//	 * Get all attribute value mappings that                                                                                                    |
//	 * {@link #getPropertiesIfNotNull(Object)} judged to be non-null                                                                            |
//	 *                                                                                                                                          |
//	 * @return all non-null attribute value mappings                                                                                            |
//	 * @deprecated                                                                                                                              |
//	 */                                                                                                                                         |
//	public static Map<String, Object> getPropertyValues() {                                                                                     |
//		Map<String, Object> map = propertyValueLocals.get();                                                                                    |
//		Assert.notNull( map, "Currently there is no local thread variable value, please call BeanUtil#getPropertiesIfNotNull(Object) first." ); |
//		propertyValueLocals.remove();                                                                                                           |
//		return map;                                                                                                                             |
//	}                                                                                                                                           |
//	----------------------------------------------------------------------------------------------------------------------------------------------

}

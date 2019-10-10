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
 * @since 1.1.0
 */
public class BeanUtil {
	
	private BeanUtil() {}
	
	private static final Map<Class<?>, Set<Method>> methodMappgins = new ConcurrentHashMap<Class<?>, Set<Method>>();
	private static final ThreadLocal<Map<String, Object>> propertyValueLocals = new ThreadLocal<Map<String,Object>>();
	
	/**
	 * Gets a collection of all non-null attribute names for the bean. This
	 * method will store all non-null values in the current thread variable. All
	 * non-null values can be obtained by {@link #getPropertyValues()}.
	 * 
	 * @param instance object instance
	 * @return attribute name set 
	 */
	public static Set<String> getPropertiesIfNotNull( Object instance ) {
		if ( instance == null ) {
			return Collections.emptySet();
		}
		Set<String> properties = null;
		Map<String, Object> propertyValues = null;
		Class<?> type = instance.getClass();
		Set<Method> methods = methodMappgins.get( type );
		if ( methods == null ) {
			synchronized ( methodMappgins ) {
				PropertyDescriptor [] descriptors = FieldUtil.getPropertyDescriptors( type );
				methods = new HashSet<Method>( descriptors.length );
				properties = new HashSet<String>( descriptors.length );
				propertyValues = new HashMap<String, Object>( descriptors.length );
				for ( PropertyDescriptor pd : descriptors ) {
					if ( ObjectUtil.isDifferent( "class", pd.getName() ) ) { // class attribute is belongs to Object
						Method getter = pd.getReadMethod();
						methods.add( getter );
						Object returnValue = MethodUtil.invoke( instance, true, getter );
						if ( returnValue != null ) {
							propertyValues.put( pd.getName(), returnValue );
							properties.add( pd.getName() );
						}
					}
				}
				methodMappgins.put( type, methods );
				propertyValueLocals.set( propertyValues );
				return properties;
			}
		}
		properties = new HashSet<String>( methods.size() );
		propertyValues = new HashMap<String, Object>( methods.size() );
		for ( Method method : methods ) {
			Object returnValue = MethodUtil.invoke( instance, true, method );
			if ( returnValue != null ) {
				String propertyName = MethodUtil.getPropertyNameByReadMethod( method );
				properties.add( propertyName );
				propertyValues.put( propertyName, returnValue );
			}
		}
		propertyValueLocals.set( propertyValues );
		return properties;
	}
	
	/**
	 * Copy a new entity object, and you can modify the copied property value
	 * 
	 * <pre>
	 * User user = new User();
	 * User copyedUser = copy(user, "attribute1", "value1", "attribute2", "value2", ...);
	 * </pre>
	 * 
	 * @param target target object instance
	 * @param properties modified property collection, similar to map key-value
	 * @return copyed entity object
	 */
	public static <T> T copy( T target, Object ... properties ) {
		boolean hasModifyProperties = ObjectUtil.isNotEmpty( properties );
		Class<T> targetType = ( Class<T> ) target.getClass();
		Map<String, Object> modify = hasModifyProperties ? MapUtil.newMap( properties ) : null;
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
	
	/**
	 * Get all attribute value mappings that
	 * {@link #getPropertiesIfNotNull(Object)} judged to be non-null
	 * 
	 * @return all non-null attribute value mappings
	 */
	public static Map<String, Object> getPropertyValues() {
		Map<String, Object> map = propertyValueLocals.get();
		Assert.notNull( map, "Currently there is no local thread variable value, please call BeanUtil#getPropertiesIfNotNull(Object) first." );
		propertyValueLocals.remove();
		return map;
	}

}

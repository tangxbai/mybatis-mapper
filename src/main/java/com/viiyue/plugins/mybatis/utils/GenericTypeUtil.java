package com.viiyue.plugins.mybatis.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple generic operation tool class
 *
 * @author tangxbai
 * @since 1.0.0
 */
public class GenericTypeUtil {
	
	private GenericTypeUtil() {}
	private static final Map<Class<?>, List<Class<?>>> caches = new ConcurrentHashMap<Class<?>, List<Class<?>>>( 128 );
	
	/**
	 * Get the generic type of the specified index in the class generic {@code List}
	 * 
	 * @param classType the class type
	 * @param index specified generic index
	 * @return the generic type of the specified generic index
	 */
	public static Class<?> getGenericType( Class<?> classType, int index ) {
		List<Class<?>> types = getGenericTypes( classType );
		Assert.isTrue( types.size() > 0, "Class {0} has no available generic parameters", classType.getName() );
		Assert.isTrue( index >= 0 && index < types.size() , "Generic type index out of range : {0} of {1}", index, types.size() );
		return types.get( index );
	}
	
	/**
	 * Get the generic type of the specified index in the interface generic list
	 * 
	 * @param interfaceType the interface type
	 * @param index specified generic index
	 * @return the generic type of the specified generic index
	 */
	public static Class<?> getInterfaceGenericType( Class<?> interfaceType, int index ) {
		List<Class<?>> types = getInterfaceGenericTypes( interfaceType );
		Assert.isTrue( types.size() > 0, "Interface {0} has no available generic parameters", interfaceType.getName() );
		Assert.isTrue( index >= 0 && index < types.size() , "Generic type index out of range : {0} of {1}", index, types.size() );
		return types.get( index );
	}
	
	/**
	 * Get a generic {@code List} of the specified interface type
	 * 
	 * @param interfaceType the interface type, must be an interface
	 * @return a generic {@code List} of the specified interface
	 */
	public static List<Class<?>> getInterfaceGenericTypes( Class<?> interfaceType ) {
		List<Class<?>> allGenericTypes = caches.get( interfaceType );
		if ( allGenericTypes == null ) {
			allGenericTypes = new LinkedList<Class<?>>();
			for ( Type genType : interfaceType.getGenericInterfaces() ) {
				allGenericTypes.addAll( getGenericTypes( genType ) );
			}
			caches.put( interfaceType, allGenericTypes );
		}
		return allGenericTypes;
	}
	
	/**
	 * Get a generic {@code List} of the specified class type
	 * 
	 * @param classType the class type
	 * @return a {@code List} of generic types
	 */
	public static List<Class<?>> getGenericTypes( Type classType ) {
		List<Class<?>> allGenericTypes = caches.get( classType );
		if ( allGenericTypes == null ) {
			allGenericTypes = new LinkedList<Class<?>>();
			if ( classType instanceof ParameterizedType ) {
				final Type [] params = ( ( ParameterizedType ) classType ).getActualTypeArguments();
				for ( Type type : params ) {
					allGenericTypes.add( ( Class<?> ) type );
				}
			}
		}
		return allGenericTypes;
	}
	
}

package com.viiyue.plugins.mybatis.utils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.viiyue.plugins.mybatis.exceptions.ReflectionException;

/**
 * <p>
 * Utilities for working with {@link Field}s by reflection. Adapted and
 * refactored from the dormant [reflect] Commons sandbox component.
 * 
 * <p>
 * The ability is provided to break the scoping restrictions coded by the
 * programmer. This can allow fields to be changed that shouldn't be. This
 * facility should be used with care.
 *
 * @author tangxbai
 * @since 1.1.0
 */
public class FieldUtil {

	private FieldUtil() {}

	public static <T> T readValue( Object instance, String filedName ) {
		try {
			return ( T ) FieldUtils.readField( instance, filedName, true );
		} catch ( IllegalAccessException e ) {
			throw new ReflectionException( e.getMessage(), e );
		}
	}

	public static List<Field> getAllFileds( Class<?> beanType ) {
		final List<Field> allFields = new ArrayList<>();
		List<Class<?>> allTypes = ClassUtil.getAllTypes( beanType );
		for ( Class<?> type : allTypes ) {
			Collections.addAll( allFields, type.getDeclaredFields() );
		}
		return Collections.unmodifiableList( allFields );
	}

	public static List<String> getAllFiledNames( Class<?> beanType ) {
		final List<String> allFieldNames = new ArrayList<>();
		List<Class<?>> allTypes = ClassUtil.getAllTypes( beanType );
		for ( Class<?> type : allTypes ) {
			for ( Field field : type.getDeclaredFields() ) {
				int modifier = field.getModifiers();
				if ( ( modifier & Modifier.STATIC ) == 0 && ( modifier & Modifier.FINAL ) == 0 ) {
					allFieldNames.add( field.getName() );
				}
			}
		}
		return Collections.unmodifiableList( allFieldNames );
	}

	public static PropertyDescriptor getDescriptor( Class<?> type, String fieldName ) {
		try {
			return new PropertyDescriptor( fieldName, type );
		} catch ( IntrospectionException e ) {
			throw new ReflectionException( e.getMessage(), e );
		}
	}

	public static PropertyDescriptor [] getPropertyDescriptors( Class<?> type ) {
		try {
			return Introspector.getBeanInfo( type ).getPropertyDescriptors();
		} catch ( IntrospectionException e ) {
			throw new ReflectionException( e.getMessage(), e );
		}
	}

}

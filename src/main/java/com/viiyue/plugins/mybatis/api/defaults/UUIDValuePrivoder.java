package com.viiyue.plugins.mybatis.api.defaults;

import java.util.Objects;
import java.util.UUID;

import com.viiyue.plugins.mybatis.api.GeneratedValueProvider;
import com.viiyue.plugins.mybatis.exceptions.TypeMismatchException;
import com.viiyue.plugins.mybatis.metadata.Property;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * Default UUID value provider
 *
 * @author tangxbai
 * @since 1.1.0
 * @see UUID#randomUUID()
 */
public class UUIDValuePrivoder implements GeneratedValueProvider {

	@Override
	public Object generatedValue( Property property ) {
		if ( Objects.equals( property.getJavaType(), String.class ) ) {
			return StringUtil.replace( UUID.randomUUID().toString(), "-", "" );
		}
		throw new TypeMismatchException( property.getJavaType(), "UUID field <{0}>", property.getName() );
	}

}

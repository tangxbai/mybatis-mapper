package com.viiyue.plugins.mybatis.api.defaults;

import java.util.Objects;

import com.viiyue.plugins.mybatis.api.GeneratedValueProvider;
import com.viiyue.plugins.mybatis.exceptions.TypeMismatchException;
import com.viiyue.plugins.mybatis.metadata.Property;
import com.viiyue.plugins.mybatis.utils.IdGenerator;

/**
 * Default snowflake id value provider
 *
 * @author tangxbai
 * @since 1.0.0
 * @see IdGenerator#nextId()
 */
public class SnowFlakeIdValueProvider implements GeneratedValueProvider {

	@Override
	public Object generatedValue( Property property ) {
		if ( Objects.equals( property.getJavaType(), Long.class ) ) {
			return IdGenerator.nextId();
		}
		throw new TypeMismatchException( property.getJavaType(), "Snowflake id field <0>", property.getName() );
	}

}

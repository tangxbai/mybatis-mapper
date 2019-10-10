package com.viiyue.plugins.mybatis.api;

import com.viiyue.plugins.mybatis.metadata.Property;

/**
 * Field value provider
 * 
 * @author tangxbai
 * @since 1.1.0
 */
public interface GeneratedValueProvider {
	Object generatedValue( Property property );
}

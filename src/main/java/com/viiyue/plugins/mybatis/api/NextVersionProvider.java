package com.viiyue.plugins.mybatis.api;

import com.viiyue.plugins.mybatis.metadata.Property;

/**
 * Optimistic lock value provider
 * 
 * @author tangxbai
 * @since 1.1.0
 */
public interface NextVersionProvider {
	Object nextVersion( Property property, Object currentVersion );
}

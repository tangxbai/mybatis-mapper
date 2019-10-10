package com.viiyue.plugins.mybatis.api.defaults.version;

import java.sql.Timestamp;

import com.viiyue.plugins.mybatis.api.NextVersionProvider;
import com.viiyue.plugins.mybatis.exceptions.ParameterValidateException;
import com.viiyue.plugins.mybatis.metadata.Property;

/**
 * <p>Default optimistic lock auto increment version number provider
 *
 * <p>
 * This class will be incremented by 1 each time based on the existing version
 * number
 *
 * @author tangxbai
 * @since 1.1.0
 */
public class DefaultNextVersionProvider implements NextVersionProvider {

	@Override
	public Object nextVersion( Property property, Object currentVersion ) {
		if ( currentVersion instanceof Short ) {
			return ( Short ) currentVersion + 1;
		}
		if ( currentVersion instanceof Integer ) {
			return ( Integer ) currentVersion + 1;
		}
		if ( currentVersion instanceof Long ) {
			return ( Long ) currentVersion + 1L;
		}
		if ( currentVersion instanceof Timestamp ) {
			return new Timestamp( System.currentTimeMillis() );
		}
		throw new ParameterValidateException(
			"The default optimistic lock version number only supports [ Short, Integer, Long, java.sql.Timestamp ]" 
		);
	}

}

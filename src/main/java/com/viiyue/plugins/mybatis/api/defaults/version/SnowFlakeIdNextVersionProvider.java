package com.viiyue.plugins.mybatis.api.defaults.version;

import com.viiyue.plugins.mybatis.api.NextVersionProvider;
import com.viiyue.plugins.mybatis.exceptions.ParameterValidateException;
import com.viiyue.plugins.mybatis.metadata.Property;
import com.viiyue.plugins.mybatis.utils.IdGenerator;

/**
 * <p>Default snowflake id optimistic lock value provider</p>
 * 
 * <p>
 * This class will generate a globally unique id value under the distinction of
 * data center id and machine id.
 *
 * @author tangxbai
 * @since 1.1.0
 * @see IdGenerator
 */
public class SnowFlakeIdNextVersionProvider implements NextVersionProvider {

	@Override
	public Object nextVersion( Property property, Object currentVersion ) {
		if ( currentVersion instanceof Long ) {
			return IdGenerator.nextId();
		}
		throw new ParameterValidateException( "Snowflake id version number can only be Long" );
	}

}

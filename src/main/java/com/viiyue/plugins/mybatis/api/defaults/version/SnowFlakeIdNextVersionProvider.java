/*-
 * ApacheLICENSE-2.0
 * #
 * Copyright (C) 2017 - 2019 mybatis-mapper
 * #
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ------------------------------------------------------------------------
 */
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

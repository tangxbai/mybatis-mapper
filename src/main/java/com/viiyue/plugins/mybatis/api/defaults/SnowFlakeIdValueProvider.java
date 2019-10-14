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

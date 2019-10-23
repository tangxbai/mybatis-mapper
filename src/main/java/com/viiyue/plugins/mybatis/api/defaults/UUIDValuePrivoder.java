/*-
 * Apache　LICENSE-2.0
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
import java.util.UUID;

import com.viiyue.plugins.mybatis.Constants;
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
			return StringUtil.replace( UUID.randomUUID().toString(), "-", Constants.EMPTY );
		}
		throw new TypeMismatchException( property.getJavaType(), "UUID field <{0}>", property.getName() );
	}

}

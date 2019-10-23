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
package com.viiyue.plugins.mybatis.template.builder;

import java.util.HashMap;

import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.utils.Assert;

/**
 * <p>
 * Database column field builder. Refer to {@link ColumnBuilder}
 * 
 * <p>Static template: <code>&#64;{this.column.property.xxx}</code> 
 *
 * <p><b>Note</b>: What is a {@code static} template? 
 * Is the template that will be compiled during the startup process.
 *
 * @author tangxbai
 * @since 1.1.0
 */
public class MappingBuilder extends HashMap<String, ColumnBuilder> {

	private static final long serialVersionUID = 1L;
	private final ColumnBuilder column;

	public MappingBuilder( Entity entity ) {
		super( 0 );
		this.column = new ColumnBuilder( entity );
	}

	@Override
	public ColumnBuilder get( Object key ) {
		Assert.notNull( key, "The property name cannot be null" );
		return column.apply( key.toString() );
	}

}

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
package com.viiyue.plugins.mybatis.condition;

import org.apache.ibatis.mapping.SqlCommandType;

import com.viiyue.plugins.mybatis.Constants;
import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.metadata.Property;
import com.viiyue.plugins.mybatis.utils.PropertyFilter;
import com.viiyue.plugins.mybatis.utils.StringAppender;

/**
 * Example conditional query 
 *
 * @author tangxbai
 * @since 1.1.0
 */
public final class SelectExample extends AbstractExample<SelectExample> {

	private final PropertyFilter filter;
	private final WhereExample<SelectExample> where;
	
	protected SelectExample( Entity entity ) {
		super( entity );
		this.where = new WhereExample<SelectExample>( this );
		this.filter = new PropertyFilter( entity, SqlCommandType.SELECT );
	}
	
	public SelectExample includes( String ... properties ) {
		this.filter.includes( properties );
		return this;
	}

	public SelectExample excludes( String ... properties ) {
		this.filter.excludes( properties );
		return this;
	}
	
	public WhereExample<SelectExample> when() {
		return where;
	}
	
	@Override
	protected String getWherePart( boolean isLogicallyDelete, boolean isDeleteValue ) {
		return where.getWherePart( isLogicallyDelete, isDeleteValue );
	}

	@Override
	protected String getQueryPart() {
		StringAppender columns = new StringAppender();
		for ( Property property : getProperties() ) {
			if ( filter.isEffective( property ) ) {
				columns.addDelimiter( Constants.SEPARATOR );
				columns.append( property.getColumn().getWrappedNameWithAlias() );
			}
		}
		return columns.toString();
	}

}

/**
 * Copyright (C) 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.viiyue.plugins.mybatis.condition;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.mapping.SqlCommandType;

import com.viiyue.plugins.mybatis.Constants;
import com.viiyue.plugins.mybatis.enums.ExpressionStyle;
import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.metadata.Property;
import com.viiyue.plugins.mybatis.template.builder.ColumnBuilder;
import com.viiyue.plugins.mybatis.utils.BeanUtil;
import com.viiyue.plugins.mybatis.utils.ObjectUtil;
import com.viiyue.plugins.mybatis.utils.PropertyFilter;
import com.viiyue.plugins.mybatis.utils.StringAppender;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * Example update conditions
 *
 * @author tangxbai
 * @since 1.1.0, Updated in 1.3.3
 */
public final class UpdateExample extends AbstractExample<UpdateExample> {

	private final StringAppender updates;
	private final ColumnBuilder column;
	private final WhereExample<UpdateExample> where;
	private final PropertyFilter filter;
	private final ExpressionStyle expressionStyle;
	
	private Set<String> bindNames;
	
	protected UpdateExample( Entity entity ) {
		super( entity );
		this.updates = new StringAppender();
		this.column = new ColumnBuilder( entity );
		this.where = new WhereExample<UpdateExample>( this );
		this.filter = new PropertyFilter( entity, SqlCommandType.UPDATE );
		this.expressionStyle = entity.getExpressionStyle();
	}
	
	public UpdateExample includes( String ... properties ) {
		this.filter.includes( properties );
		return this;
	}
	
	public UpdateExample excludes( String ... properties ) {
		this.filter.excludes( properties );
		return this;
	}

	public UpdateExample set( String ... properties ) {
		if ( ObjectUtil.isNotEmpty( properties ) ) {
			this.bindNames = new LinkedHashSet<String>( Arrays.asList( properties ) );
		}
		return this;
	}
	
	public UpdateExample values( Object ... values ) {
		if ( bindNames != null && ObjectUtil.isNotEmpty( values ) ) {
			int index = 0, size = values.length;
			for ( String name : bindNames ) {
				putParameter( name, index < size ? values[ index ++ ] : null );
			}
			// Updated in 1.3.3
			this.bindNames = null;
			this.filter.dynamic( getParameters() );
		}
		return this;
	}
	
	// Updated in 1.3.3
	public UpdateExample bind( Object modelInstance ) {
		Map<String, Object> bindValues = BeanUtil.getPropertiesIfNotNull( modelInstance );
		if ( ObjectUtil.isNotEmpty( bindValues ) ) { // Added in 1.3.3
			this.putParameters( bindValues );
			this.filter.dynamic( bindValues );
		}
		return this;
	}
	
	// Added in 1.3.1
	public UpdateExample bind( String property, Object value ) {
		if ( StringUtil.isNotEmpty( property ) ) {
			this.putParameter( property, value );
			this.filter.addDynamic( property, value );
		}
		return this;
	}
	
	public WhereExample<UpdateExample> when() {
		return where;
	}
	
	@Override
	public UpdateExample get() {
		return this;
	}

	@Override
	protected String getWherePart( boolean isLogicallyDelete, boolean isDeleteValue ) {
		return where.getWherePart( isLogicallyDelete, isDeleteValue );
	}
	
	@Override
	protected String getModifyPart() {
		for ( Property property : getProperties() ) {
			if ( filter.isEffective( property ) ) {
				this.updates.addDelimiter( Constants.SEPARATOR );
				this.updates.append( expressionStyle.format( column.apply( property ).alias( parmeterAlias ), property ) );
			}
		}
		if ( updates.isEmpty() ) {
			this.updates.append( "(Undefined)" );
		}
		return updates.toString();
	}

}

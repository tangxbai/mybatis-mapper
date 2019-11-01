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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.viiyue.plugins.mybatis.Constants;
import com.viiyue.plugins.mybatis.metadata.Column;
import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.metadata.Property;
import com.viiyue.plugins.mybatis.utils.StringAppender;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * Abstract Example conditional construction
 *
 * @author tangxbai
 * @since 1.1.0
 */
abstract class AbstractExample<T extends AbstractExample<T>> extends Example<T> {
	
	protected static final String parmeterAlias = "parameters";
	
	private final Entity entity;
	private final Map<String, Object> parameters = new HashMap<String, Object>();
	
	public AbstractExample( Entity entity ) {
		this.entity = entity;
	}
	
	// Return current instance by default
	@Override
	protected Example<T> build() {
		return this;
	}
	
	// Default is empty
	@Override
	protected String getModifyPart() {
		return StringUtil.EMPTY;
	}

	// Default is empty
	@Override
	protected String getWherePart() {
		return getWherePart( true, false );
	}
	
	// Default is empty
	@Override
	protected String getWherePart( boolean isLogicallyDelete, boolean isDeleteValue ) {
		return StringUtil.EMPTY;
	}
	
	// Return all columns by default
	@Override
	protected String getQueryPart() {
		StringAppender columns = new StringAppender();
		for ( Property property : getProperties() ) {
			columns.addDelimiter( Constants.SEPARATOR );
			columns.append( property.getColumn().getWrappedNameWithAlias() );
		}
		return columns.toString();
	}
	
	@Override
	public Map<String, Object> getParameters() {
		return parameters;
	}

	protected final Entity getEntity() {
		return entity;
	}

	protected final List<Property> getProperties() {
		return entity.getProperties();
	}

	protected final Column getColumn( String propertyName ) {
		return entity.getColumn( propertyName );
	}

	protected final Property getProperty( String propertyName ) {
		return entity.getProperty( propertyName );
	}

	protected final Property getPrimaryKey( int index ) {
		return entity.getPrimaryKey( index );
	}
	
	protected final boolean hasParameter( String key ) {
		return parameters.containsKey( key );
	}

	protected final Object getParameter( String key ) {
		return parameters.get( key );
	}

	protected final void putParameter( String key, Object value ) {
		if ( key != null ) {
			this.parameters.put( key, value );
		}
	}

	protected final void putParameters( Map<String, Object> parameters ) {
		if ( parameters != null ) {
			this.parameters.putAll( parameters );
		}
	}
	
}

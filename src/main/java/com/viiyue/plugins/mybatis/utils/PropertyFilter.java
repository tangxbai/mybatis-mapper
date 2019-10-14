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
package com.viiyue.plugins.mybatis.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.mapping.SqlCommandType;

import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.metadata.Property;
import com.viiyue.plugins.mybatis.metadata.info.GeneratedKeyInfo;

/**
 * Attribute filter
 *
 * @author tangxbai
 * @since 1.1.0
 */
public final class PropertyFilter {
	
	private final Entity entity;
	private final SqlCommandType commandType;

	private List<String> includes;
	private List<String> excludes;
	
	private Set<String> bindNames;
	private Map<String, Object> bindValues;
	
	public PropertyFilter( Entity entity, SqlCommandType commandType ) {
		this.entity = entity;
		this.commandType = commandType;
	}
	
	public void dynamic( Object parameterObject ) {
		dynamic( BeanUtil.getPropertiesIfNotNull( parameterObject ), BeanUtil.getPropertyValues() );
	}
	
	public void dynamic( Set<String> bindNames, Map<String, Object> bindValues ) {
		this.bindNames = bindNames;
		this.bindValues = bindValues;
	}
	
	public void includes( String includes ) {
		includes( StringUtil.split( StringUtil.replace( includes, " ", "" ), ',' ) );
	}
	
	public void includes( String ... includes ) {
		includes( Arrays.asList( includes ) );
	}
	
	public void includes( List<String> includes ) {
		this.excludes = null;
		this.includes = includes;
	}
	
	public void excludes( String excludes ) {
		excludes( StringUtil.split( StringUtil.replace( excludes, " ", "" ), ',' ) );
	}
	
	public void excludes( String ... excludes ) {
		excludes( Arrays.asList( excludes ) );
	}
	
	public void excludes( List<String> excludes ) {
		this.includes = null;
		this.excludes = excludes;
	}

	public boolean isEffective( Property property ) {
		// Is it insertable or is it updateable?
		if ( ( StatementUtil.isInsert( commandType ) && !property.isInsertable() ) || 
			 ( StatementUtil.isUpdate( commandType ) && !property.isUpdatable()  ) ) {
			return false;
		}
		
		// If the primary key is a auto-incrementing primary key, 
		// the primary key column may not appear in the SQL statement.
		if ( StatementUtil.isInsert( commandType ) && property.isPrimaryKey() && entity.hasGeneratedKeyInfo() ) {
			GeneratedKeyInfo info = entity.getGeneratedKeyInfo();
			if ( info.isGeneratedKeyProperty( property ) && info.isSelectGeneratedKey() ) {
				return false;
			}
		}
		final String propertyName = property.getName();
		
		// Include and exclude filtering
		if ( ( includes != null && !includes.contains( propertyName ) ) ||
			 ( excludes != null &&  excludes.contains( propertyName ) ) ) {
			return false;
		}
		
		// Dynamic attribute filtering
		boolean isEffective = bindNames == null || ( // No any attributes
			bindNames.contains( propertyName ) && ( 
				property.isNullbale() || ( // Allowed to be null
					bindValues != null && // Not allowed to be null, so the value must exist
					bindValues.get( propertyName ) != null 
				)
			)
		);
		return isEffective;
	}

}

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

import org.apache.ibatis.mapping.SqlCommandType;

import com.viiyue.plugins.mybatis.Constants;
import com.viiyue.plugins.mybatis.metadata.Column;
import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.metadata.Property;
import com.viiyue.plugins.mybatis.template.builder.base.TemplateBuilder;
import com.viiyue.plugins.mybatis.utils.BuilderUtil;
import com.viiyue.plugins.mybatis.utils.PropertyFilter;
import com.viiyue.plugins.mybatis.utils.StatementUtil;

/**
 * <p>
 * Databse column builder, this class method will be called by reflection after
 * the template syntax is parsed.
 * 
 * <p>Static template: <br>
 * <code>&#64;{this.columns}</code><br>
 * <code>&#64;{this.columns.xxx}</code>
 * </p>
 * 
 * <p><b>Note</b>: What is a {@code static} template? 
 * Is the template content that will be compiled during the startup process.
 *
 * @author tangxbai
 * @since 1.1.0
 */
public final class ColumnsBuilder extends TemplateBuilder {
	
	private final PropertyFilter filter;
	private boolean useNameAlias;
	private String prefix;

	public ColumnsBuilder( Entity entity, SqlCommandType commandType ) {
		super( entity );
		this.filter = new PropertyFilter( entity, commandType );
		this.useNameAlias = StatementUtil.isSelect( commandType );
	}
	
	public ColumnsBuilder prefix( String prefix ) {
		this.prefix = BuilderUtil.getWrappedPrefix( prefix );
		return this;
	}
	
	public ColumnsBuilder dynamic( Object parameterObject ) {
		this.filter.dynamic( parameterObject );
		return this;
	}
	
	public ColumnsBuilder include( String includes ) {
		this.filter.includes( includes );
		return this;
	}
	
	public ColumnsBuilder exclude( String excludes ) {
		this.filter.excludes( excludes );
		return this;
	}
	
	@Override
	protected void finalConfirmation( Entity entity ) {
		for ( Property property : entity.getProperties() ) {
			if ( filter.isEffective( property ) ) {
				Column column = property.getColumn();
				this.addDelimiter( Constants.SEPARATOR );
				this.append( prefix );
				this.append( useNameAlias ? column.getWrappedNameWithAlias() : column.getWrappedName() );
			}
		}
	}

}

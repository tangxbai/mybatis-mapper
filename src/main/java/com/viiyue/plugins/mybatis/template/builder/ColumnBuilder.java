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

import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.metadata.Property;
import com.viiyue.plugins.mybatis.template.builder.base.TemplateBuilder;
import com.viiyue.plugins.mybatis.utils.BuilderUtil;
import com.viiyue.plugins.mybatis.utils.ClassUtil;
import com.viiyue.plugins.mybatis.utils.ObjectUtil;

/**
 * <p>
 * Database column field builder, this class method will be called by reflection after
 * the template syntax is parsed.
 * 
 * <p>Static template: <br>
 * <code>&#64;{this.column.property.xxx}</code><br>
 * <code>&#64;{this.map.property.xxx}</code>
 * </p>
 * 
 * <p><b>Note</b>: What is a {@code static} template? 
 * Is the template content that will be compiled during the startup process.
 *
 * @author tangxbai
 * @since 1.1.0
 */
public final class ColumnBuilder extends TemplateBuilder {
	
	private boolean isAppendColumn = true;
	private Property property;
	private String alias;
	private String prefix;
	private String suffix;
	private String propertyName;
	
	public ColumnBuilder( Entity entity ) {
		super( entity );
	}
	
	public ColumnBuilder apply( String propertyName ) {
		return apply( getProperty( propertyName ) );
	}
	
	public ColumnBuilder apply( Property property ) {
		this.property = property;
		this.alias = null;
		this.prefix = null;
		this.suffix = null;
		this.propertyName = null;
		this.isAppendColumn = true;
		return this;
	}
	
	public ColumnBuilder prefix( String prefix ) {
		this.prefix = BuilderUtil.getWrappedPrefix( prefix ); 
		return this;
	}
	
	public ColumnBuilder suffix( String suffix ) {
		this.suffix = suffix; 
		return this;
	}
	
	public ColumnBuilder alias( String alias ) {
		this.alias = BuilderUtil.getPrefix( alias ); 
		return this;
	}
	
	public ColumnBuilder property( String propertyName ) {
		this.propertyName = propertyName; 
		return this;
	}
	
	public ColumnBuilder jdbcType() {
		if ( property != null ) {
			this.isAppendColumn = false;
			this.append( property.getColumn().getJdbcType() );
		}
		return this;
	}
	
	public ColumnBuilder javaType() {
		if ( property != null ) {
			this.isAppendColumn = false;
			final Class<?> javaType = property.getJavaType();
			this.append( ClassUtil.isCommonType( javaType ) ? javaType.getSimpleName() : javaType.getName() );
		}
		return this;
	}
	
	public ColumnBuilder property() {
		if ( property != null ) {
			this.isAppendColumn = false;
			this.append( alias );
			this.append( ObjectUtil.defaultIfNull( propertyName, property.getName() ) );
			this.append( suffix );
		}
		return this;
	}
	
	@Override
	protected void finalConfirmation( Entity entity ) {
		if ( isAppendColumn && property != null ) {
			this.append( prefix );
			this.append( property.getColumn().getWrappedName() );
			this.append( suffix );
		}
	}
	
	public Property getProperty() {
		return property;
	}
	
}

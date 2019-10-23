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

import com.viiyue.plugins.mybatis.enums.Setting;
import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.template.builder.base.TemplateBuilder;
import com.viiyue.plugins.mybatis.utils.BuilderUtil;

/**
 * <p>
 * Table builder, this class method will be called by reflection after
 * the template syntax is parsed.
 * 
 * <p>Static template: <code>&#64;{this.table.xxx}</code>
 * 
 * <p><b>Note</b>: What is a {@code static} template? 
 * Is the template content that will be compiled during the startup process.
 * 
 * @author tangxbai
 * @since 1.1.0
 */
public final class TableBuilder extends TemplateBuilder {
	
	private String tableAlias;
	
	public TableBuilder( Entity entity ) {
		super( entity, false );
	}

	public TableBuilder prefix( String prefix ) {
		this.append( BuilderUtil.getPrefix( prefix ) ); 
		return this;
	}
	
	public TableBuilder alias( String alias ) {
		this.tableAlias = Setting.ColumnStyle.getStyleValue( alias );
		return this;
	}

	@Override
	protected void finalConfirmation( Entity entity ) {
		this.append( entity.getWrappedTableName() );
		if ( tableAlias != null ) {
			this.append( " [as] " ).append( tableAlias );
		}
	}
	
}

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

import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.metadata.info.LogicallyDeleteInfo;
import com.viiyue.plugins.mybatis.template.builder.base.TemplateBuilder;
import com.viiyue.plugins.mybatis.template.handler.ExceptionHandler;
import com.viiyue.plugins.mybatis.utils.BuilderUtil;
import com.viiyue.plugins.mybatis.utils.StatementUtil;

/**
 * <p>
 * Logical delete builder, this class method will be called by reflection after
 * the template syntax is parsed.
 * 
 * <p>Static template: <code>&#64;{this.tryLogicallyDelete}</code>
 *
 * <p><b>Note</b>: What is a {@code static} template? 
 * Is the template content that will be compiled during the startup process.
 *
 * @author tangxbai
 * @since 1.1.0
 */
public final class LogicallyDeleteBuilder extends TemplateBuilder {

	private final SqlCommandType commandType;
	private String modifier;
	private String prefix;
	private boolean isDeletedValue = true;
	
	public LogicallyDeleteBuilder( Entity entity, SqlCommandType commandType ) {
		super( entity );
		this.commandType = commandType;
	}
	
	public LogicallyDeleteBuilder prefix( String prefix ) {
		this.prefix = BuilderUtil.getWrappedPrefix( prefix );
		return this;
	}
	
	public LogicallyDeleteBuilder useQueryValue() {
		this.isDeletedValue = false;
		return this;
	}
	
	public LogicallyDeleteBuilder useDeletedValue() {
		this.isDeletedValue = true;
		return this;
	}
	
	public LogicallyDeleteBuilder useWhereQuery() {
		this.modifier = "[where] ";
		return useQueryValue();
	}
	
	public LogicallyDeleteBuilder useAndQuery() {
		this.modifier = "[and] ";
		return useQueryValue();
	}
	
	public LogicallyDeleteBuilder useOrQuery() {
		this.modifier = "[or] ";
		return useQueryValue();
	}

	@Override
	protected void finalConfirmation( Entity entity ) {
		LogicallyDeleteInfo info = entity.getLogicallyDeleteInfo();
		
		// Logical deletion but not configured @LogicallyDelete annotation
		if ( info == null ) {
			if ( StatementUtil.isDelete( commandType ) ) {
				append( ExceptionHandler.wrapException(
					"You called the logical deletion method, but missing the @LogicallyDelete annotation in class ''{0}''",
					entity.getBeanType().getName() 
				));
			}
			return;
		}
		
		append( modifier );
		append( prefix );
		append( info.getColumn().getWrappedName() );
		append( " = " );
		append( info.getValueBy( isDeletedValue ) );
	}

}

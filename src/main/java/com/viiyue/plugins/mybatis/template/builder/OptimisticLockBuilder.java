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
package com.viiyue.plugins.mybatis.template.builder;

import com.viiyue.plugins.mybatis.enums.ValueStyle;
import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.metadata.Property;
import com.viiyue.plugins.mybatis.metadata.info.VersionInfo;
import com.viiyue.plugins.mybatis.template.builder.base.TemplateBuilder;
import com.viiyue.plugins.mybatis.utils.BuilderUtil;

/**
 * <p>
 * Database optimistic lock builder, this class method will be called by reflection after
 * the template syntax is parsed.
 * 
 * <p>Static template: <code>&#64;{this.tryOptimisticLock.xxx}</code>
 * 
 * <p><b>Note</b>: What is a {@code static} template? 
 * Is the template content that will be compiled during the startup process.
 * 
 * @author tangxbai
 * @since 1.1.0
 */
public final class OptimisticLockBuilder extends TemplateBuilder {

	private final VersionInfo info;
	private final ColumnBuilder column;
	private String prefix;
	private String modifier;

	public OptimisticLockBuilder( Entity entity ) {
		super( entity );
		this.info = entity.getVersionInfo();
		this.column = new ColumnBuilder( entity );
	}

	public OptimisticLockBuilder prefix( String prefix ) {
		this.prefix = BuilderUtil.getWrappedPrefix( prefix );
		return this;
	}
	
	/**
	 * @since 1.1.0
	 * @deprecated Please use {@link #useWhereQuery()} instead
	 */
	@Deprecated
	public OptimisticLockBuilder useWhere() {
		return useWhereQuery();
	}
	
	/** @since 1.1.2 */
	public OptimisticLockBuilder useWhereQuery() {
		this.modifier = "[where] ";
		return this;
	}
	
	/**
	 * @since 1.1.0
	 * @deprecated Please use {@link #useAndQuery()} instead
	 */
	@Deprecated
	public OptimisticLockBuilder useAnd() {
		return useAndQuery();
	}
	
	/** @since 1.1.2 */
	public OptimisticLockBuilder useAndQuery() {
		this.modifier = "[and] ";
		return this;
	}
	
	/**
	 * @since 1.1.0
	 * @deprecated Please use {@link #useOrQuery()} instead
	 */
	@Deprecated
	public OptimisticLockBuilder useOr() {
		return useOrQuery();
	}
	
	/** @since 1.1.2 */
	public OptimisticLockBuilder useOrQuery() {
		this.modifier = "[or] ";
		return this;
	}

	@Override
	protected void finalConfirmation( Entity entity ) {
		if ( entity.hasOptimisticLock() ) {
			Property property = info.getProperty();
			ValueStyle valueStyle = entity.getValueStyle();
			this.append( modifier );
			this.append( prefix ).append( info.getColumn().getWrappedName() );
			this.append( " = " );
			this.append( valueStyle.format( column.apply( property ) ) );
		}
	}

}

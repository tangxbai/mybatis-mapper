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

import static com.viiyue.plugins.mybatis.Constants.ORDER_BY_ASCENDING_WRAP;
import static com.viiyue.plugins.mybatis.Constants.ORDER_BY_DESCENDING_WRAP;

import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.metadata.info.OrderByInfo;
import com.viiyue.plugins.mybatis.template.builder.base.TemplateBuilder;
import com.viiyue.plugins.mybatis.utils.BuilderUtil;

/**
 * <p>
 * The default sort builder, this class method will be called by reflection after
 * the template syntax is parsed.
 * 
 * <p>
 * Static template: <code>&#64;{this.defaultOrderBy}</code>
 *
 * <p><b>Note</b>: What is a {@code static} template? 
 * Is the template content that will be compiled during the startup process.
 *
 * @author tangxbai
 * @since 1.1.0
 */
public final class OrderByBuilder extends TemplateBuilder {

	private String columnName;
	private String orderBy;
	private String prefix;
	private OrderByInfo info;

	public OrderByBuilder( Entity entity ) {
		super( entity );
		if ( entity.hasDefaultOrderByInfo() ) {
			this.info = entity.getOrderByInfo();
			this.orderBy = info.getOrderBy();
			this.columnName = info.getColumn().getWrappedName();
		}
	}

	public OrderByBuilder prefix( String prefix ) {
		this.prefix = BuilderUtil.getWrappedPrefix( prefix );
		return this;
	}

	public OrderByBuilder desc() {
		this.orderBy = ORDER_BY_DESCENDING_WRAP;
		return this;
	}

	public OrderByBuilder asc() {
		this.orderBy = ORDER_BY_ASCENDING_WRAP;
		return this;
	}

	@Override
	protected void finalConfirmation( Entity entity ) {
		if ( entity.hasDefaultOrderByInfo() ) {
			this.append( "[order by] " ).append( prefix ).append( columnName ).append( " " ).append( orderBy );
		}
	}

}

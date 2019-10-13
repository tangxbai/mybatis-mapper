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
package com.viiyue.plugins.mybatis.metadata.info;

import static com.viiyue.plugins.mybatis.Constants.ORDERS;

import com.viiyue.plugins.mybatis.annotation.bean.DefaultOrderBy;
import com.viiyue.plugins.mybatis.metadata.Column;
import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.metadata.Property;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * Default sort field description bean
 *
 * @author tangxbai
 * @since 1.1.0
 */
public final class OrderByInfo {

	private Property property;
	private final Entity entity;
	private final String propertyName;
	private final String orderBy;

	public OrderByInfo( Entity entity, DefaultOrderBy orderBy ) {
		this.entity = entity;
		this.propertyName = orderBy.value();
		this.orderBy = getOrderBy( orderBy.desc() );
	}

	public String getOrderBy() {
		return orderBy;
	}
	
	public Property getProperty() {
		if ( property == null ) {
			this.property = entity.getProperty( propertyName );
		}
		return property;
	}
	
	public Column getColumn() {
		return getProperty().getColumn();
	}

	@Override
	public String toString() {
		return StringUtil.toString( this, "[order] [by] %s %s", getColumn().getName(), getOrderBy() );
	}
	
	public static String getOrderBy( boolean isDescending ) {
		return ORDERS[ isDescending ? 3 : 2 ];
	}
	
	public static boolean isOrderBy( String orderBy ) {
		return orderBy != null && StringUtil.equalsAnyIgnoreCase( orderBy, ORDERS );
	}

}

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
package com.viiyue.plugins.mybatis.metadata.info;

import java.util.Objects;

import com.viiyue.plugins.mybatis.annotation.member.LogicallyDelete;
import com.viiyue.plugins.mybatis.metadata.Column;
import com.viiyue.plugins.mybatis.metadata.Property;
import com.viiyue.plugins.mybatis.utils.Assert;
import com.viiyue.plugins.mybatis.utils.ClassUtil;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * Logical deletion description bean
 *
 * @author tangxbai
 * @since 1.1.0
 */
public final class LogicallyDeleteInfo {

	private final Property property;
	private final Object selectValue;
	private final Object deletedValue;
	private final Class<?> valueType;

	public LogicallyDeleteInfo( Property property, LogicallyDelete logicallyDelete ) {
		this.property = property;
		this.valueType = logicallyDelete.type();
		Assert.isTrue( Objects.equals( String.class, valueType ) || ClassUtil.isPrimitiveOrWrapper( valueType ) || valueType.isEnum(),
			"Only primitive types, String, and Enum are supported" );
		this.selectValue = ClassUtil.simpleTypeConvert( logicallyDelete.selectValue(), valueType );
		this.deletedValue = ClassUtil.simpleTypeConvert( logicallyDelete.deletedValue(), valueType );
	}

	private boolean isText() {
		return Objects.equals( String.class, valueType ) || valueType.isEnum();
	}

	public Property getProperty() {
		return property;
	}

	public Column getColumn() {
		return property.getColumn();
	}

	public Class<?> getValueType() {
		return valueType;
	}

	public Object getSelectValue() {
		return isText() ? "'" + selectValue + "'" : selectValue;
	}

	public Object getDeletedValue() {
		return isText() ? "'" + deletedValue + "'" : deletedValue;
	}

	public boolean isLogicallDeleteProperty( Property property ) {
		return Objects.equals( this.property.getName(), property.getName() );
	}
	
	public Object getValueBy( boolean isDeletedValue ) {
		return isDeletedValue ? getDeletedValue() : getSelectValue();
	}

	@Override
	public String toString() {
		return StringUtil.toString( this, "%s, value = deleted(%s)/select(%s)", property.getName(), getDeletedValue(), getSelectValue() );
	}

}

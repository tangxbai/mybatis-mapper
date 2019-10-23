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
package com.viiyue.plugins.mybatis.metadata;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.viiyue.plugins.mybatis.metadata.info.ConditionalInfo;
import com.viiyue.plugins.mybatis.metadata.info.GeneratedValueInfo;
import com.viiyue.plugins.mybatis.utils.Assert;
import com.viiyue.plugins.mybatis.utils.FieldUtil;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * Model bean field description object
 *
 * @author tangxbai
 * @since 1.0.0
 */
public final class Property implements Comparable<Property> {
	
	private final Entity parent;
	private final Field field;
	private final String name;
	private final Method getter;
	private final Method setter;
	private final Class<?> javaType;
	
	private Column column;
	private boolean primary;
	private boolean primaryKey;
	private boolean ignore;
	private boolean nullbale;
	private boolean updatable;
	private boolean insertable;
	private int index;
	
	private ConditionalInfo conditionalInfo;
	private GeneratedValueInfo generatedValueInfo;
	
	public Property( Entity parent, Field field ) {
		Assert.notNull( parent, "Required parent entity object" );
		Assert.notNull( field, "Required property field" );
		this.parent = parent;
		this.field = field;
		this.name = field.getName();
		this.javaType = field.getType();
		PropertyDescriptor descriptor = FieldUtil.getDescriptor( parent.getBeanType(), field.getName() );
		this.getter = descriptor.getReadMethod();
		this.setter = descriptor.getWriteMethod();
		Assert.notNull( getter, "{0} is not a standard JavaBean, missing getter method", parent.getBeanName() );
		Assert.notNull( setter, "{0} is not a standard JavaBean, missing setter method", parent.getBeanName() );
	}
		
	// Setter

	protected void setColumn( Column column ) {
		this.column = column;
	}

	protected void setConditionalInfo( ConditionalInfo conditionalInfo ) {
		this.conditionalInfo = conditionalInfo;
	}
	
	protected void setGeneratedValueInfo( GeneratedValueInfo generatedValueInfo ) {
		this.generatedValueInfo = generatedValueInfo;
	}

	protected void setPrimary( boolean primary ) {
		this.primary = primary;
	}

	protected void setPrimaryKey( boolean primaryKey ) {
		this.primaryKey = primaryKey;
	}
	
	protected void setIgnore( boolean ignore ) {
		this.ignore = ignore;
	}
	
	protected void setNullbale( boolean nullbale ) {
		this.nullbale = nullbale;
	}

	protected void setUpdatable( boolean updatable ) {
		this.updatable = updatable;
	}

	protected void setInsertable( boolean insertable ) {
		this.insertable = insertable;
	}
	
	protected void setIndex( int index ) {
		this.index = index;
	}
	
	// Getter
	
	public Entity getParent() {
		return parent;
	}

	public Field getField() {
		return field;
	}
	
	public String getName() {
		return name;
	}
	
	public Method getGetter() {
		return getter;
	}
	
	public Method getSetter() {
		return setter;
	}

	public Class<?> getJavaType() {
		return javaType;
	}

	public Column getColumn() {
		return column;
	}
	
	public ConditionalInfo getConditionalInfo() {
		return conditionalInfo;
	}
	
	public GeneratedValueInfo getGeneratedValueInfo() {
		return generatedValueInfo;
	}

	public boolean isPrimary() {
		return primaryKey && primary;
	}

	public boolean isPrimaryKey() {
		return primaryKey;
	}
	
	public boolean isIgnore() {
		return ignore;
	}
	
	public boolean isNullbale() {
		return nullbale;
	}

	public boolean isUpdatable() {
		return updatable;
	}

	public boolean isInsertable() {
		return insertable;
	}
	
	public int getIndex() {
		return index;
	}

	// Helper
	
	public boolean isVisible() {
		return ignore == false;
	}
	
	public boolean isAnnotationPresent( Class<? extends Annotation> annotationType ) {
		return getAnnotation( annotationType ) != null;
	}
	
	public <T extends Annotation> T getAnnotation( Class<T> annotationType ) {
		T annotation = field.getAnnotation( annotationType );
		if ( annotation == null ) {
			annotation = getter.getAnnotation( annotationType );
		}
		if ( annotation == null ) {
			annotation = setter.getAnnotation( annotationType );
		}
		return annotation;
	}
	
	// Override
	
	@Override
	public int compareTo( Property o ) {
		return o == null ? -1 : Integer.compare( getIndex(), o.getIndex() );
	}
	
	@Override
	public String toString() {
		if ( column == null ) {
			return StringUtil.toString( this, "%s, primaryKey = %s", getName(), isPrimaryKey() );
		}
		return StringUtil.toString( this, "%s -> %s, primaryKey = %s", getName(), column.getName(), isPrimaryKey() );
	}
	
}

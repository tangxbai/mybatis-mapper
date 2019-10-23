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
package com.viiyue.plugins.mybatis.template.builder.base;

import com.viiyue.plugins.mybatis.metadata.Column;
import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.metadata.Property;
import com.viiyue.plugins.mybatis.utils.StringAppender;

/**
 * Template syntax parsing builder base object
 *
 * @author tangxbai
 * @since 1.1.0
 */
public abstract class TemplateBuilder extends StringAppender {

	/** Model bean parsing object */
	private final Entity entity;
	
	/** Check if any attributes are included */
	private final boolean checkProperties;

	/**
	 * Initializes a template builder object, which checks for the existence of
	 * the property by default.
	 * 
	 * @param entity entity parsing object
	 */
	public TemplateBuilder( Entity entity ) {
		this( entity, true );
	}

	/**
	 * Initialize a template builder object, which can be specified at the time
	 * of initialization to verify the property.
	 * 
	 * @param entity entity parsing object
	 * @param checkProperties whether to check the properties
	 */
	public TemplateBuilder( Entity entity, boolean checkProperties ) {
		this.entity = entity;
		this.checkProperties = checkProperties;
	}

	/**
	 * Target moden bean parsing object
	 * 
	 * @return the parsing object of the entity
	 */
	public final Entity getEntity() {
		return entity;
	}
	
	/**
	 * Find the corresponding database column object by property name
	 * 
	 * @param propertyName target property name
	 * @return the found database column object
	 */
	public final Column getColumn( String propertyName ) {
		return entity.getColumn( propertyName );
	}
	
	/**
	 * Get the property object by property name
	 * 
	 * @param propertyName target property name
	 * @return the found property object
	 */
	public final Property getProperty( String propertyName ) {
		return entity.getProperty( propertyName );
	}

	/**
	 * Nothing to do here, you can customize your own logic code.
	 * 
	 * @param entity entity bean parsing object
	 */
	protected abstract void finalConfirmation( Entity entity );
	
	/**
	 * Returns the final constructed string literal
	 * 
	 * @return the final string text
	 */
	public final String build() {
		if ( checkProperties == false || entity.hasAnyProperties() ) {
			finalConfirmation( entity );
		}
		String content = getAppender().toString();
		reset();
		return content;
	}

}

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
	 * @return entity parsing object
	 */
	public final Entity getEntity() {
		return entity;
	}
	
	/**
	 * Find the corresponding database column object by property name
	 * 
	 * @param propertyName target property name
	 * @return database column object found
	 */
	public final Column getColumn( String propertyName ) {
		return entity.getColumn( propertyName );
	}
	
	/**
	 * Get the property object by property name
	 * 
	 * @param propertyName target property name
	 * @return property object found
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
	 * @return final string text
	 */
	public final String build() {
		if ( checkProperties == false || entity.hasAnyProperties() ) {
			finalConfirmation( entity );
		}
		String content = getBuffer().toString();
		reset();
		return content;
	}

}

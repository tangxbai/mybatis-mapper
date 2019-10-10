package com.viiyue.plugins.mybatis.condition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.viiyue.plugins.mybatis.Constants;
import com.viiyue.plugins.mybatis.metadata.Column;
import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.metadata.Property;
import com.viiyue.plugins.mybatis.utils.StringAppender;
import com.viiyue.plugins.mybatis.utils.StringUtil;

abstract class AbstractExample<T extends AbstractExample<T>> extends Example<T> {
	
	protected static final String parmeterAlias = "parameters";
	
	private final Entity entity;
	private final Map<String, Object> parameters = new HashMap<String, Object>();
	
	public AbstractExample( Entity entity ) {
		this.entity = entity;
	}
	
	// Return current instance by default
	@Override
	protected Example<T> build() {
		return this;
	}
	
	// Default is empty
	@Override
	protected String getModifyPart() {
		return StringUtil.EMPTY;
	}

	// Default is empty
	@Override
	protected String getWherePart() {
		return getWherePart( true, false );
	}
	
	// Default is empty
	@Override
	protected String getWherePart( boolean isLogicallyDelete, boolean isDeleteValue ) {
		return StringUtil.EMPTY;
	}
	
	// Return all columns by default
	@Override
	protected String getQueryPart() {
		StringAppender columns = new StringAppender();
		for ( Property property : getProperties() ) {
			columns.addDelimiter( Constants.SEPARATOR );
			columns.append( property.getColumn().getWrappedNameWithAlias() );
		}
		return columns.toString();
	}
	
	@Override
	public Map<String, Object> getParameters() {
		return parameters;
	}

	protected final Entity getEntity() {
		return entity;
	}

	protected final List<Property> getProperties() {
		return entity.getProperties();
	}

	protected final Column getColumn( String propertyName ) {
		return entity.getColumn( propertyName );
	}

	protected final Property getProperty( String propertyName ) {
		return entity.getProperty( propertyName );
	}

	protected final Property getPrimaryKey( int index ) {
		return entity.getPrimaryKey( index );
	}
	
	protected final boolean hasParameter( String key ) {
		return parameters.containsKey( key );
	}

	protected final Object getParameter( String key ) {
		return parameters.get( key );
	}

	protected final void putParameter( String key, Object value ) {
		if ( key != null ) {
			this.parameters.put( key, value );
		}
	}

	protected final void putParameters( Map<String, Object> parameters ) {
		if ( parameters != null ) {
			this.parameters.putAll( parameters );
		}
	}
	
}

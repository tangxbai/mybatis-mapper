package com.viiyue.plugins.mybatis.condition;

import org.apache.ibatis.mapping.SqlCommandType;

import com.viiyue.plugins.mybatis.Constants;
import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.metadata.Property;
import com.viiyue.plugins.mybatis.utils.PropertyFilter;
import com.viiyue.plugins.mybatis.utils.StringAppender;

public final class SelectExample extends AbstractExample<SelectExample> {

	private final PropertyFilter filter;
	private final WhereExample<SelectExample> where;
	
	protected SelectExample( Entity entity ) {
		super( entity );
		this.where = new WhereExample<SelectExample>( this, entity );
		this.filter = new PropertyFilter( entity, SqlCommandType.SELECT );
	}
	
	public SelectExample includes( String ... properties ) {
		this.filter.includes( properties );
		return this;
	}

	public SelectExample excludes( String ... properties ) {
		this.filter.excludes( properties );
		return this;
	}
	
	public WhereExample<SelectExample> when() {
		return where;
	}
	
	@Override
	protected String getWherePart( boolean isLogicallyDelete, boolean isDeleteValue ) {
		return where.getWherePart( isLogicallyDelete, isDeleteValue );
	}

	@Override
	protected String getQueryPart() {
		StringAppender columns = new StringAppender();
		for ( Property property : getProperties() ) {
			if ( filter.isEffective( property ) ) {
				columns.addDelimiter( Constants.SEPARATOR );
				columns.append( property.getColumn().getWrappedNameWithAlias() );
			}
		}
		return columns.toString();
	}

}

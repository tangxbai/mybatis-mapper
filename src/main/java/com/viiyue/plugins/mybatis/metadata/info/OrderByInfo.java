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

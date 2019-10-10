package com.viiyue.plugins.mybatis.condition;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.mapping.SqlCommandType;

import com.viiyue.plugins.mybatis.Constants;
import com.viiyue.plugins.mybatis.enums.ExpressionStyle;
import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.metadata.Property;
import com.viiyue.plugins.mybatis.template.builder.ColumnBuilder;
import com.viiyue.plugins.mybatis.utils.BeanUtil;
import com.viiyue.plugins.mybatis.utils.ObjectUtil;
import com.viiyue.plugins.mybatis.utils.PropertyFilter;
import com.viiyue.plugins.mybatis.utils.StringAppender;

public final class UpdateExample extends AbstractExample<UpdateExample> {

	private final StringAppender updates;
	private final ColumnBuilder column;
	private final WhereExample<UpdateExample> where;
	private final PropertyFilter filter;
	private final ExpressionStyle expressionStyle;
	
	private Set<String> bindNames;
	
	protected UpdateExample( Entity entity ) {
		super( entity );
		this.updates = new StringAppender();
		this.column = new ColumnBuilder( entity );
		this.where = new WhereExample<UpdateExample>( this, entity );
		this.filter = new PropertyFilter( entity, SqlCommandType.UPDATE );
		this.expressionStyle = entity.getExpressionStyle();
	}
	
	public UpdateExample includes( String ... properties ) {
		this.filter.includes( properties );
		return this;
	}
	
	public UpdateExample excludes( String ... properties ) {
		this.filter.excludes( properties );
		return this;
	}

	public UpdateExample set( String ... properties ) {
		if ( ObjectUtil.isNotEmpty( properties ) ) {
			this.bindNames = new LinkedHashSet<String>( Arrays.asList( properties ) );
		}
		return this;
	}
	
	public UpdateExample values( Object ... values ) {
		if ( bindNames != null && ObjectUtil.isNotEmpty( values ) ) {
			int index = 0, size = values.length;
			for ( String name : bindNames ) {
				putParameter( name, index < size ? values[ index ++ ] : null );
			}
			this.filter.dynamic( bindNames, getParameters() );
		}
		return this;
	}
	
	public UpdateExample bind( Object modelInstance ) {
		Set<String> bindNames = BeanUtil.getPropertiesIfNotNull( modelInstance );
		Map<String, Object> bindValues = BeanUtil.getPropertyValues();
		this.bindNames = bindNames;
		this.putParameters( bindValues );
		this.filter.dynamic( bindNames, bindValues );
		return this;
	}
	
	public WhereExample<UpdateExample> when() {
		return where;
	}
	
	@Override
	protected String getWherePart( boolean isLogicallyDelete, boolean isDeleteValue ) {
		return where.getWherePart( isLogicallyDelete, isDeleteValue );
	}
	
	@Override
	protected String getModifyPart() {
		for ( Property property : getProperties() ) {
			if ( filter.isEffective( property ) ) {
				this.updates.addDelimiter( Constants.SEPARATOR );
				this.updates.append( expressionStyle.format( column.apply( property ).alias( parmeterAlias ), property ) );
			}
		}
		if ( updates.isEmpty() ) {
			updates.append( "(Undefined)" );
		}
		return updates.toString();
	}

}

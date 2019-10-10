package com.viiyue.plugins.mybatis.condition;

import static com.viiyue.plugins.mybatis.Constants.SEPARATOR;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.viiyue.plugins.mybatis.metadata.Column;
import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.template.handler.ExceptionHandler;
import com.viiyue.plugins.mybatis.utils.BuilderUtil;
import com.viiyue.plugins.mybatis.utils.StringAppender;

public final class AggregateFunctionExample extends AbstractExample<AggregateFunctionExample> {

	private final WhereExample<AggregateFunctionExample> where;
	
	private String functionName;
	private String keywordFunctionName;
	private String [] properties;
	private String totalAlias = "total";
	
	protected AggregateFunctionExample( Entity entity, String functionName, String ... properties ) {
		super( entity );
		this.properties = properties;
		this.functionName = functionName;
		this.keywordFunctionName = "[" + functionName + "]";
		this.where = new WhereExample<AggregateFunctionExample>( this, entity );
	}
	
	public AggregateFunctionExample totalAlias( String propertyName ) {
		this.totalAlias = getProperty( propertyName ).getColumn().getAlias();
		return this;
	}

	public WhereExample<AggregateFunctionExample> when() {
		return where;
	}
	
	public AggregateFunctionExample limit( int limit ) {
		this.where.limit( limit );
		return this;
	}
	
	public AggregateFunctionExample limit( int offset, int limit ) {
		this.where.limit( offset, limit );
		return this;
	}
	
	public AggregateFunctionExample orderBy( String propertyName, boolean isDescending ) {
		this.where.orderBy( propertyName, isDescending );
		return this;
	}
	
	public AggregateFunctionExample orderBy( String ... properties ) {
		this.where.orderBy( properties );
		return this;
	}
	
	public AggregateFunctionExample groupBy( String ... properties ) {
		this.where.groupBy( properties );
		return this;
	}
	
	@Override
	protected String getQueryPart() {
		StringAppender columns = new StringAppender();
		StringAppender functions = new StringAppender();
		Set<String> groupByProperties = where.getGroupByProperties();
		
		// count(xx) xx, count(yy) yy, ...
		for ( String propertyName : properties ) {
			// Only the count function can use an asterisk, 
			// and other conditions automatically filter the asterisk attribute.
			boolean isAsterisk = Objects.equals( propertyName, "*" );
			if ( !isAsterisk || Objects.equals( keywordFunctionName, "[count]" ) ) {
				functions.addDelimiter( SEPARATOR );
				functions.append( getFunction( isAsterisk, propertyName ) );
				groupByProperties.remove( propertyName );
			} else {
				functions.append( ExceptionHandler.wrapException( "The aggregate function " + functionName + "(*) is not allowed" ) );
			}
		}
		// id, name, ...
		for ( String groupByProperty : groupByProperties ) {
			columns.addDelimiter( SEPARATOR );
			columns.append( getProperty( groupByProperty ).getColumn().getWrappedName() );
		}
		if ( functions.hasContent() ) {
			columns.addDelimiter( SEPARATOR );
		}
		// id, name, count(xx) xx, count(yy) yy
		return columns.append( functions ).toString();
	}
	
	@Override
	protected String getWherePart( boolean isLogicallyDelete, boolean isDeleteValue ) {
		return where.getWherePart( isLogicallyDelete, isDeleteValue );
	}
	
	@Override
	public Map<String, Object> getParameters() {
		return where == null ? null : where.getParameters();
	}

	private String getFunction( boolean isAsterisk, String propertyName ) {
		String columnName = propertyName;
		String mappingName = totalAlias;
		if ( !isAsterisk ) {
			Column column = getColumn( propertyName );
			columnName = column.getWrappedName();
			mappingName = column.getAlias();
		}
		return keywordFunctionName + BuilderUtil.getAlias( "( " + columnName + " )", mappingName );
	}
	
}

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
package com.viiyue.plugins.mybatis.condition;

import static com.viiyue.plugins.mybatis.Constants.MYBATIS_PARAMETER_PATTERN;
import static com.viiyue.plugins.mybatis.Constants.SEPARATOR;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.viiyue.plugins.mybatis.Constants;
import com.viiyue.plugins.mybatis.enums.Template;
import com.viiyue.plugins.mybatis.enums.ValueStyle;
import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.metadata.Property;
import com.viiyue.plugins.mybatis.metadata.info.LogicallyDeleteInfo;
import com.viiyue.plugins.mybatis.metadata.info.OrderByInfo;
import com.viiyue.plugins.mybatis.template.TemplateHandler;
import com.viiyue.plugins.mybatis.template.builder.ColumnBuilder;
import com.viiyue.plugins.mybatis.template.handler.KeywordsHandler;
import com.viiyue.plugins.mybatis.utils.Assert;
import com.viiyue.plugins.mybatis.utils.ObjectUtil;
import com.viiyue.plugins.mybatis.utils.StringAppender;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * Example query condition construction
 *
 * @author tangxbai
 * @since 1.0.0
 */
public final class WhereExample<T extends AbstractExample<T>> extends AbstractExample<T> {

	private final T example;
	private final ColumnBuilder column;
	private final StringAppender condition = new StringAppender();
	private final Set<String> queryProperties = new HashSet<String>( 16 );
	private final Set<String> orderByProperties = new HashSet<String>( 8 );
	private final Set<String> groupByProperties = new HashSet<String>( 8 );
	
	private String delimiter;
	
	protected WhereExample( Entity entity ) {
		this( null, entity );
	}
	
	protected WhereExample( T example ) {
		this( example, example == null ? null : example.getEntity() );
	}

	private WhereExample( T example, Entity entity ) {
		super( entity );
		Assert.isTrue( example != null || entity != null, "Example and Entity cannot be null at the same time" );
		this.example = example;
		this.column = new ColumnBuilder( entity );
	}
	
	protected Set<String> getQueryProperties( Set<String> merge ) {
		return new HashSet<String>( this.queryProperties );
	}

	protected Set<String> getOrderByProperties() {
		return new HashSet<String>( this.orderByProperties );
	}

	protected Set<String> getGroupByProperties() {
		return new HashSet<String>( this.groupByProperties );
	}
	
	protected Map<String, Object> getOriginalParameters() {
		return super.getParameters();
	}

	private WhereExample<T> join( String delimiter ) {
		this.delimiter = delimiter;
		return this;
	}
	
	public WhereExample<T> and() {
		return join( " [and] " );
	}
	
	public WhereExample<T> or() {
		return join( " [or] " );
	}
	
	public WhereExample<T> isNull( String propertyName ) {
		return byTemplate( Template.IsNull, propertyName );
	}
	
	public WhereExample<T> notNull( String propertyName ) {
		return byTemplate( Template.NotNull, propertyName );
	}
	
	public WhereExample<T> lt( String propertyName, Object value ) {
		return lessThan( propertyName, value );
	}
	
	public WhereExample<T> lessThan( String propertyName, Object value ) {
		return byTemplate( Template.LessThan, propertyName, value );
	}
	
	public WhereExample<T> lte( String propertyName, Object value ) {
		return lessThanAndEqualTo( propertyName, value );
	}
	
	public WhereExample<T> lessThanAndEqualTo( String propertyName, Object value ) {
		return byTemplate( Template.LessThanAndEqualTo, propertyName, value );
	}
	
	public WhereExample<T> gt( String propertyName, Object value ) {
		return greaterThan( propertyName, value );
	}
	
	public WhereExample<T> greaterThan( String propertyName, Object value ) {
		return byTemplate( Template.GreaterThan, propertyName, value );
	}
	
	public WhereExample<T> gte( String propertyName, Object value ) {
		return greaterThanAndEqualTo( propertyName, value );
	}
	
	public WhereExample<T> greaterThanAndEqualTo( String propertyName, Object value ) {
		return byTemplate( Template.GreaterThanAndEqualTo, propertyName, value );
	}
	
	public WhereExample<T> eq( String propertyName, Object value ) {
		return equal( propertyName, value );
	}
	
	public WhereExample<T> equal( String propertyName, Object value ) {
		return byTemplate( Template.Equal, propertyName, value );
	}
	
	public WhereExample<T> neq( String propertyName, Object value ) {
		return notEqual( propertyName, value );
	}
	
	public WhereExample<T> notEqual( String propertyName, Object value ) {
		return byTemplate( Template.NotEqual, propertyName, value );
	}
	
	public WhereExample<T> like( String propertyName, String value ) {
		return byTemplate( Template.Like, propertyName, value );
	}
	
	public WhereExample<T> contains( String propertyName, String value ) {
		return byTemplate( Template.Like, propertyName, value );
	}
	
	public WhereExample<T> startsWith( String propertyName, String value ) {
		return byTemplate( Template.StartsWith, propertyName, value );
	}
	
	public WhereExample<T> notLike( String propertyName, String value ) {
		return byTemplate( Template.NotLike, propertyName, value );
	}
	
	public WhereExample<T> notContains( String propertyName, String value ) {
		return byTemplate( Template.NotLike, propertyName, value );
	}
	
	public WhereExample<T> endsWith( String propertyName, String value ) {
		return byTemplate( Template.EndsWith, propertyName, value );
	}
	
	public WhereExample<T> regexp( String propertyName, String value ) {
		return byTemplate( Template.Regexp, propertyName, value );
	}
	
	public WhereExample<T> notRegexp( String propertyName, String value ) {
		return byTemplate( Template.NotRegexp, propertyName, value );
	}
	
	public WhereExample<T> between( String propertyName, Object value1, Object value2 ) {
		return byTemplate( Template.Between, propertyName, value1, value2 );
	}
	
	public WhereExample<T> notBetween( String propertyName, Object value1, Object value2 ) {
		return byTemplate( Template.NotBetween, propertyName, value1, value2 );
	}
	
	public WhereExample<T> inRange( String propertyName, Object value1, Object value2 ) {
		return byTemplate( Template.InRange, propertyName, value1, value2 );
	}
	
	public WhereExample<T> overRange( String propertyName, Object value1, Object value2 ) {
		return byTemplate( Template.OverRange, propertyName, value1, value2 );
	}
	
	public WhereExample<T> in( String propertyName, Object ... values ) {
		return byTemplate( Template.In, propertyName, values );
	}
	
	public WhereExample<T> in( String propertyName, List<?> values ) {
		return byTemplate( Template.In, propertyName, values.toArray() );
	}
	
	public WhereExample<T> notIn( String propertyName, Object ... values ) {
		return byTemplate( Template.NotIn, propertyName, values );
	}
	
	public WhereExample<T> notIn( String propertyName, List<?> values ) {
		return byTemplate( Template.NotIn, propertyName, values.toArray() );
	}
	
	public WhereExample<T> byTemplate( String templateName, String propertyName, Object ... values ) {
		return byTemplate( Template.findOf( templateName ), propertyName, values );
	}
	
	public WhereExample<T> byTemplate( Template template, String propertyName, Object ... values ) {
		Assert.notNull( template, "Example template cannot be null" );
		Property property = getProperty( propertyName );
		ValueStyle valueStyle = getEntity().getValueStyle();
		this.queryProperties.add( propertyName );
		this.column.apply( property ).alias( parmeterAlias );
		this.condition.addDelimiter( delimiter );
		this.condition.append( template.format( column, valueStyle ) );
		if ( template.isNeedParameter() ) {
			for ( int i = 0, size = values.length; i < size; i ++ ) {
				putParameter( property.getName() + ( size > 1 ? i : Constants.EMPTY ), values[ i ] );
			}
		}
		return and();
	}
	
	public WhereExample<T> condition( String condition, Object ... values ) {
		Assert.notBlank( condition, "Example custom condition cannot be null or blank text" );
		this.condition.addDelimiter( delimiter );
		if ( ObjectUtil.isNotEmpty( values ) ) {
			List<Map<String, String>> parameters = TemplateHandler.processMybatisParameters( condition );
			condition = MYBATIS_PARAMETER_PATTERN.matcher( condition ).replaceAll( "$1{" + parmeterAlias + ".$2}" );
			int valueSize = values.length;
			int paramSize = parameters.size();
			this.condition.append( condition );
			for ( int i = 0; i < paramSize; i ++ ) {
				String property = Objects.toString( parameters.get( i ).get( "property" ), Constants.EMPTY );
				putParameter( property, i < valueSize ? values[ i ] : null );
				this.queryProperties.add( property );
			}
		} else {
			this.condition.append( condition );
		}
		return and();
	}
	
	public WhereExample<T> groupBy( String ... properties ) {
		this.condition.addDelimiter( " " ).append( "[group by]" );
		for ( int i = 0; i < properties.length; i ++ ) {
			this.condition.append( i > 0 ? SEPARATOR : " " );
			this.condition.append( getColumn( properties[ i ] ).getWrappedName() );
			this.groupByProperties.add( properties[ i ] );
		}
		return this;
	}
	
	public WhereExample<T> orderBy( String propertyName, boolean isDescending ) {
		this.condition.addDelimiter( " " ).append( "[order by] " );
		this.condition.append( getColumn( propertyName ).getWrappedName() );
		this.condition.append( " " );
		this.condition.append( OrderByInfo.getOrderBy( isDescending ) );
		this.orderByProperties.add( propertyName );
		return this;
	}
	
	public WhereExample<T> orderBy( String ... properties ) {
		this.condition.addDelimiter( " " ).append( "[order by]" );
		for ( int i = 0; i < properties.length; i ++ ) {
			String[] names = StringUtil.split( properties[ i ] );
			String orderBy = Constants.ORDER_BY_DESCENDING_WRAP;
			this.condition.append( i > 0 ? SEPARATOR : " " );
			this.condition.append( getColumn( names[ 0 ] ).getWrappedName() );
			this.condition.append( " " );
			this.orderByProperties.add( names[ 0 ] );
			if ( names.length >= 2 && OrderByInfo.isOrderBy( names[ 1 ] ) ) {
				orderBy = names[ 1 ];
				if ( !KeywordsHandler.isKeywordsWrap( orderBy ) ) {
					orderBy = "[" + orderBy + "]";
				}
			}
			this.condition.append( orderBy );
		}
		return this;
	}
	
	public WhereExample<T> limit( int limit ) {
		return join( " " ).condition( "[limit] #{limit}", limit );
	}
	
	public WhereExample<T> limit( int offset, int limit ) {
		return join( " " ).condition( "[limit] #{offset}, #{limit}", offset, limit );
	}
	
	public WhereExample<T> forUpdate() {
		return join( " " ).condition( "[for update]" );
	}
	
	@Override
	protected Example<T> build() {
		return ObjectUtil.defaultIfNull( example, this );
	}
	
	/**
	 * Merge parameters, fixed in 1.2.0+
	 * @since 1.2.0
	 */
	@Override
	public Map<String, Object> getParameters() {
		if ( example == null ) {
			return super.getParameters();
		}
		Map<String, Object> parameters = example.getParameters();
		parameters.putAll( super.getParameters() );
		return parameters;
	}

	@Override
	protected String getWherePart( boolean isLogicallyDelete, boolean isDeleteValue ) {
		String starting = "[where] ";
		boolean isStartWithKeywords = condition.startsWith( "[" );
		LogicallyDeleteInfo info = getEntity().getLogicallyDeleteInfo();
		if ( isLogicallyDelete && info != null ) {
			starting += info.getColumn().getWrappedName() + " = " + info.getValueBy( isDeleteValue );
			if ( condition.hasContent() ) {
				starting += Constants.SPACE;
				if ( !isStartWithKeywords ) {
					starting += "[and] ";
				}
			}
		} else if ( condition.isEmpty() || isStartWithKeywords ) {
			starting = null;
		}
		// 1. [where] ...
		// 2. [where] logically_delete = #{deletedValue} [and] ...
		// 3. [order by]/[group by]/[limit]/[keyword] ...
		return condition.prepend( starting ).toString();
	}

}

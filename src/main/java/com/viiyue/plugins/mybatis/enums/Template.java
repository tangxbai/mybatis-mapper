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
package com.viiyue.plugins.mybatis.enums;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.viiyue.plugins.mybatis.template.TemplateHandler;
import com.viiyue.plugins.mybatis.template.builder.ColumnBuilder;
import com.viiyue.plugins.mybatis.utils.Assert;

/**
 * Sql expression template object, you can easily format specific properties
 * into corresponding expression templates.
 * 
 * @author tangxbai
 * @since 1.0.0
 */
public enum Template {
	
	// No place holder arguments
	
	/** Output : <tt>column is null</tt> */
	IsNull( "@{column} [is null]", 0, false, "is_null" ),
	/** Output : <tt>column is not null</tt> */
	NotNull( "@{column} [is not null]", 0, false, "not_null" ),
	
	// Single place holder argument
	/** Output : <tt>column = #{property}</tt> */
	Equal( "@{column} = {0}", 1, true, "eq" ),
	/** Output : <tt>column &lt;&gt; #{property}</tt> */
	NotEqual( "@{column} <> {0}", 1, true, "not_equal", "neq" ),
	
	/** Output : <tt>column &gt; #{property}</tt> */
	GreaterThan( "@{column} > {0}", 1, true, "greater_than", "gt" ),
	/** Output : <tt>column &gt;= #{property}</tt> */
	GreaterThanAndEqualTo( "@{column} >= {0}", 1, true, "greater_than_and_equal_to", "gte" ),
	
	/** Output : <tt>column &lt; #{property}</tt> */
	LessThan( "@{column} < {0}", 1, true, "less_than", "lt" ),
	/** Output : <tt>column &lt;= #{property}</tt> */
	LessThanAndEqualTo( "@{column} <= {0}", 1, true, "less_than_and_equal_to", "lte" ),
	
	/** Output : <tt>column in ( #{inArguments} )</tt> */
	In( "@{column} [in] ( {0} )", 1, false ),
	/** Output : <tt>column not in ( #{inArguments} )</tt> */
	NotIn( "@{column} [not in] ( {0} )", 1, false, "not_in" ),
	
	/** Output : <tt>column like concat( '%', #{property}, '%' )</tt> */
	Like( "@{column} [like] [concat](''%'', {0}, ''%'')", 1, true, "contains" ),
	/** Output : <tt>column not like concat( '%', #{property}, '%' )</tt> */
	NotLike( "@{column} [not like] [concat](''%'', {0}, ''%'')", 1, true, "not_like", "not_contains" ),
	/** Output : <tt>column like concat( #{property}, '%' )</tt> */
	StartsWith( "@{column} [like] [concat]({0}, ''%'')", 1, true, "starts_with" ),
	/** Output : <tt>column like concat( '%', #{property} )</tt> */
	EndsWith( "@{column} [like] [concat](''%'', {0})", 1, true, "ends_with" ),
	
	/** Output : <tt>column regexp #{property}</tt> */
	Regexp( "@{column} [regexp] {0}", 1, true ),
	/** Output : <tt>column not regexp #{property}</tt> */
	NotRegexp( "@{column} [not regexp] {0}", 1, true, "not_regexp" ),
	
	// Multiple place holder arguments
	/** Output : <tt>column ( between #{property1} and #{property2} )</tt> */
	Between( "( @{column} [between] {0} [and] {1} )", 2, true ),
	/** Output : <tt>column ( not between #{property1} and #{property2} )</tt> */
	NotBetween( "( @{column} [not between] {0} [and] {1} )", 2, true, "not_between" ),
	/** Output : <tt>column &gt;= #{property1} and column &lt;= #{property2}</tt> */
	InRange( "( @{column} >= {0} [and] @{column} <= {1} )", 2, true, "in_range" ),
	/** Output : <tt>( column &gt; #{property1} and column &lt; #{property2} )</tt> */
	OverRange( "( @{column} < {0} [or] @{column} > {1} )", 2, true, "over_range" );
	
	private int size;
	private boolean isNeedParameter;
	private String template;
	private String[] aliases;
	
	// Aliases cache mappings
	static final Map<String, Template> caches = new ConcurrentHashMap<String, Template>();
	static {
		for ( Template template : values() ) {
			for ( String alias : template.aliases() ) {
				caches.put( alias.toLowerCase( Locale.ENGLISH ), template );
			}
		}
	}

	private Template( String template, int size, boolean isNeedParameter, String ... aliases ) {
		this.size = size;
		this.template = template;
		this.isNeedParameter = isNeedParameter;
		this.aliases = Arrays.copyOf( aliases, aliases.length + 1 );
		this.aliases[ aliases.length ] = name();
	}
	
	public int size() {
		return size;
	}
	
	public boolean isNeedParameter() {
		return isNeedParameter;
	}

	public String template () {
		return template;
	}
	
	public String [] aliases() {
		return aliases;
	}

	public String format( ColumnBuilder column, ValueStyle valueStyle ) {
		String template = TemplateHandler.processExpressionTemplate( this.template, column );
		if ( size > 0 ) {
			final Object [] args = new Object[ size ];
			for ( int i = 0; i < size; i ++ ) {
				if ( size > 1 ) {
					column.suffix( String.valueOf( i ) );
				}
				args[ i ] = valueStyle.format( column );
			}
			template = MessageFormat.format( template, args );
		}
		return template;
	}
	
	public static Template findOf( String name ) {
		Assert.notNull( name, "Template processor name cannot be null" );
		Template template = caches.get( name.toLowerCase( Locale.ENGLISH ) );
		Assert.notNull( template, "Template processor #" + name + "# does not support" );
		return template;
	}
	
}

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
package com.viiyue.plugins.mybatis.template;

import org.apache.ibatis.mapping.SqlCommandType;

import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.metadata.EntityParser;
import com.viiyue.plugins.mybatis.template.builder.ColumnBuilder;
import com.viiyue.plugins.mybatis.template.builder.ColumnsBuilder;
import com.viiyue.plugins.mybatis.template.builder.LogicallyDeleteBuilder;
import com.viiyue.plugins.mybatis.template.builder.MappingBuilder;
import com.viiyue.plugins.mybatis.template.builder.OptimisticLockBuilder;
import com.viiyue.plugins.mybatis.template.builder.OrderByBuilder;
import com.viiyue.plugins.mybatis.template.builder.TableBuilder;
import com.viiyue.plugins.mybatis.template.builder.UpdateSetBuilder;
import com.viiyue.plugins.mybatis.template.builder.ValuesBuilder;
import com.viiyue.plugins.mybatis.template.builder.WhereBuilder;

/**
 * The parsing builder factory of the template syntax for parsing the various
 * template syntaxes that appear in the template text.
 *
 * @author tangxbai
 * @since 1.1.0
 */
public final class TemplateBuilderFactory {
	
	/** Model bean parsing object */
	private final Entity entity;
	
	/** SQL statement command type */
	private final SqlCommandType commandType;

	/**
	 * Internal instantiation template factory object for parsing template
	 * syntax that appears in the template engine
	 * 
	 * @param modelBeanType model bean parsing object
	 * @param commandType SQL command type
	 */
	private TemplateBuilderFactory( Class<?> modelBeanType, SqlCommandType commandType ) {
		this.entity = EntityParser.getEntity( modelBeanType );
		this.commandType = commandType;
	}
	
	/**
	 * Instantiate a template factory object to parse the template syntax that
	 * appears in the template engine
	 * 
	 * @param modelBeanType model bean parsing object
	 * @param commandType SQL command type
	 * @return template builder factory
	 */
	public static TemplateBuilderFactory build( Class<?> modelBeanType, SqlCommandType commandType ) {
		return new TemplateBuilderFactory( modelBeanType, commandType );
	}
	
	/**
	 * <p>
	 * The template factory is used to get the current table object, which can
	 * be used in any scenario in theory, but it is generally used for static
	 * template compilation.
	 * 
	 * <p>
	 * In theory it can be used in any scenario, but it is usually used for
	 * static template compilation.
	 * 
	 * <pre>
	 * <code>&#64;&#123;this.table&#125;</code> -&gt; t_table_name
	 * <code>&#64;&#123;this.table.prefix("t")&#125;</code> -&gt; t.t_table_name
	 * <code>&#64;&#123;this.table.alias("alias")&#125;</code> -&gt; t_table_name [as] 'alias'
	 * </pre>
	 * 
	 * @return table builder
	 */
	public TableBuilder table() {
		return new TableBuilder( entity );
	}
	
	/**
	 * <p>
	 * The template factory is used to get the primary key, because there is no
	 * limit to the existence of a primary key in the plugin, so if you do not
	 * explicitly specify which one to use as the primary key, the first primary
	 * key is used by default.
	 * 
	 * <p>
	 * In theory it can be used in any scenario, but it is usually used for
	 * static template compilation.
	 * 
	 * <pre>
	 * <code>&#64;&#123;this.pk&#125;</code> -&gt; primary_key
	 * <code>&#64;&#123;this.pk.prefix("t")&#125;</code> -&gt; t.primary_key
	 * <code>&#64;&#123;this.pk.suffix("_xxx")&#125;</code> -&gt; t.primary_key_xxx
	 * <code>&#64;&#123;this.pk.alias("ttt")&#125;</code> -&gt; primary_key [as] 'ttt'
	 * </pre>
	 * 
	 * @return primary key column builder
	 */
	public ColumnBuilder pk() {
		return new ColumnBuilder( entity ).apply( entity.getDefaultPrimaryKey() );
	}
	
	/**
	 * <p>
	 * In the template factory, in the case of multiple primary keys, get the
	 * primary key of the specified subscript
	 * 
	 * <p>
	 * In theory it can be used in any scenario, but it is usually used for
	 * static template compilation.
	 * 
	 * <pre>
	 * <code>&#64;&#123;this.pk(Integer)&#125;</code> -&gt; primary_key
	 * <code>&#64;&#123;this.pk(Integer).prefix("t")&#125;</code> -&gt; t.primary_key
	 * <code>&#64;&#123;this.pk(Integer).suffix("_xxx")&#125;</code> -&gt; t.primary_key_xxx
	 * <code>&#64;&#123;this.pk(Integer).alias("ttt")&#125;</code> -&gt; primary_key [as] 'ttt'
	 * </pre>
	 * 
	 * @param index primary key index
	 * @return primary key column builder
	 */
	public ColumnBuilder pk( int index ) {
		return new ColumnBuilder( entity ).apply( entity.getPrimaryKey( index ) );
	}
	
	/**
	 * <p>
	 * Used in the template factory to get all the columns of the table
	 * 
	 * <p>
	 * In theory it can be used in any scenario, but it is usually used for
	 * static template compilation.
	 * 
	 * <pre>
	 * <code>&#64;&#123;this.columns&#125;</code> -&gt; id, name, column
	 * <code>&#64;&#123;this.columns.prefix("t")&#125;</code> -&gt; t.id, t.name, t.column
	 * <code>&#64;&#123;this.columns.dynamic(Object)&#125;</code> -&gt; id, name ( Select all non-null attributes )
	 * <code>&#64;&#123;this.columns.include("id")&#125;</code> -&gt; id
	 * <code>&#64;&#123;this.columns.include("id")&#125;</code> -&gt; name
	 * </pre>
	 * 
	 * @param index primary key index
	 * @return property column builder
	 */
	public ColumnsBuilder columns() {
		return new ColumnsBuilder( entity, commandType );
	}
	
	// ---------------------- How to get the column? ------------------------------
	
//	/**
//	 * <p>
//	 * The column builder object used to get the specified property name in the
//	 * template factory
//	 * 
//	 * <p>
//	 * In theory it can be used in any scenario, but it is usually used for
//	 * static template compilation.
//	 * 
//	 * <pre>
//	 * <code>&#64;&#123;this.column("yourPropertyName").prefix("t")&#125;</code> -&gt; t.your_property_name
//	 * <code>&#64;&#123;this.column("yourPropertyName").suffix("_xxx")&#125;</code> -&gt; t.your_property_name_xxx
//	 * <code>&#64;&#123;this.column("yourPropertyName").alias("ttt")&#125;</code> -&gt; t.your_property_name [as] 'ttt'
//	 * <code>&#64;&#123;this.column("yourPropertyName").property&#125;</code> -&gt; yourPropertyName
//	 * <code>&#64;&#123;this.column("yourPropertyName").jdbcType&#125;</code> -&gt; VARCHAR ( See the actual situation )
//	 * <code>&#64;&#123;this.column("yourPropertyName").javaType&#125;</code> -&gt; String ( See the actual situation )
//	 * </pre>
//	 * 
//	 * @param propertyName property name
//	 * @return property column builder
//	 */
//	// Method 1:
//	// Is it too complicated? ...
//	// Example: this.column( String ) -> this.column( 'propertyName' )
//	public ColumnBuilder column( String propertyName ) {
//		return new ColumnBuilder( entity ).apply( entity.getProperty( propertyName ) );
//	}
	
	/**
	 * <p>
	 * The column builder object used to get the specified property name in the
	 * template factory, but this method returns a map builder object
	 * 
	 * <p>
	 * In theory it can be used in any scenario, but it is usually used for
	 * static template compilation.
	 * 
	 * <pre>
	 * <code>&#64;&#123;this.column.yourPropertyName&#125;</code> -&gt; your_property_name
	 * <code>&#64;&#123;this.column.yourPropertyName.prefix("t")&#125;</code> -&gt; t.your_property_name
	 * <code>&#64;&#123;this.column.yourPropertyName.suffix("_xxx")&#125;</code> -&gt; t.your_property_name_xxx
	 * <code>&#64;&#123;this.column.yourPropertyName.alias("ttt")&#125;</code> -&gt; t.your_property_name [as] 'ttt'
	 * <code>&#64;&#123;this.column.yourPropertyName.property&#125;</code> -&gt; yourPropertyName
	 * <code>&#64;&#123;this.column.yourPropertyName.jdbcType&#125;</code> -&gt; VARCHAR ( See the actual situation )
	 * <code>&#64;&#123;this.column.yourPropertyName.javaType&#125;</code> -&gt; String ( See the actual situation )
	 * </pre>
	 * 
	 * <p>Note: This method is exactly the same as the {@link TemplateBuilderFactory#map() map()} method.
	 * 
	 * @return property column mapping builder object
	 * @see #map()
	 */
	// Method 2
	// It seems like this is OK?
	// Example: this.column.xxx -> this.column.propertyName
	public MappingBuilder column() {
		return new MappingBuilder( entity );
	}
	
	/**
	 * <p>
	 * The column builder object used to get the specified property name in the
	 * template factory, but this method returns a map builder object
	 * 
	 * <p>
	 * In theory it can be used in any scenario, but it is usually used for
	 * static template compilation.
	 * 
	 * <pre>
	 * <code>&#64;&#123;this.map.xxx&#125;</code> -&gt; xxx
	 * <code>&#64;&#123;this.map.loginName&#125;</code> -&gt; login_name
	 * <code>&#64;&#123;this.map.yourPropertyName&#125;</code> -&gt; your_property_name
	 * <code>&#64;&#123;this.map.yourPropertyName.prefix("t")&#125;</code> -&gt; t.your_property_name
	 * <code>&#64;&#123;this.map.yourPropertyName.suffix("_xxx")&#125;</code> -&gt; t.your_property_name_xxx
	 * <code>&#64;&#123;this.map.yourPropertyName.alias("ttt")&#125;</code> -&gt; t.your_property_name [as] 'ttt'
	 * <code>&#64;&#123;this.map.yourPropertyName.property&#125;</code> -&gt; yourPropertyName
	 * <code>&#64;&#123;this.map.yourPropertyName.jdbcType&#125;</code> -&gt; VARCHAR ( See the actual situation )
	 * <code>&#64;&#123;this.map.yourPropertyName.javaType&#125;</code> -&gt; String ( See the actual situation )
	 * </pre>
	 * 
	 * <p>Note: This method is exactly the same as the {@link TemplateBuilderFactory#column() column()} method.
	 * 
	 * @return property column mapping builder object
	 * @see #column()
	 */
	// Method 3:
	// Map or column? tangled ...
	// Ok, both of them are reserved and can be used normally.
	// Example: this.map.xxx -> this.map.propertyName
	public MappingBuilder map() {
		return new MappingBuilder( entity );
	}
	
	// ------------------------------ Get the column ------------------------------
	
	/**
	 * <p>
	 * The set value builder object used to generate the update statement in the
	 * template factory
	 * 
	 * <p>
	 * In theory it can be used in any scenario, but it is usually used for
	 * static template compilation.
	 * 
	 * <pre>
	 * <code>&#64;&#123;this.set&#125;</code> -&gt; id = &#35;&#123;id&#125;, name = &#35;&#123;name&#125;, nick_name = &#35;&#123;nickName&#125;
	 * <code>&#64;&#123;this.set.prefix("t")&#125;</code> -&gt; t.id = &#35;&#123;id&#125;, t.name = &#35;&#123;name&#125;, t.nick_name = &#35;&#123;nickName&#125;
	 * <code>&#64;&#123;this.set.alias("param")&#125;</code> -&gt; id = &#35;&#123;param.id&#125;, name = &#35;&#123;param.name&#125;, nick_name = &#35;&#123;param.nickName&#125;
	 * <code>&#64;&#123;this.set.include("id,name")&#125;</code> -&gt; id = &#35;&#123;param.id&#125;, name = &#35;&#123;param.name&#125;
	 * <code>&#64;&#123;this.set.exclude("id,name")&#125;</code> -&gt; nick_name = &#35;&#123;param.nickName&#125;
	 * <code>&#64;&#123;this.set.dynamic(Object)&#125;</code> -&gt; id = &#35;&#123;id&#125; ( Select all non-null attributes )
	 * </pre>
	 * 
	 * <p>
	 * Note: This method is exactly the same as the
	 * {@link TemplateBuilderFactory#column() column()} method.
	 * 
	 * @return set value builder object of update statement
	 */
	public UpdateSetBuilder set() {
		return new UpdateSetBuilder( entity, commandType );
	}
	
	/**
	 * <p>
	 * All assignment columns used to generate statements in the template
	 * factory
	 * 
	 * <p>
	 * In the actual situation, it may appear in the static template
	 * compilation. At this time, all the columns will be output, or it may
	 * appear in the dynamic template compilation, and the dynamic filtering
	 * needs to use the columns.
	 * 
	 * <pre>
	 * <code>&#64;&#123;this.values&#125;</code> -&gt; &#35;&#123;id&#125;, &#35;&#123;name&#125;, &#35;&#123;nickName&#125;
	 * <code>&#64;&#123;this.values.alias("param")&#125;</code> -&gt; &#35;&#123;param.id&#125;, &#35;&#123;param.name&#125;, &#35;&#123;param.nickName&#125;
	 * <code>&#64;&#123;this.values.dynamic(Object)&#125;</code> -&gt; &#35;&#123;param.name&#125; ( Select all non-null attributes )
	 * <code>&#64;&#123;this.values.include("id,name")&#125;</code> -&gt; &#35;&#123;id&#125;, &#35;&#123;name&#125;
	 * <code>&#64;&#123;this.values.exclude("id,name")&#125;</code> -&gt; &#35;&#123;nickName&#125;
	 * </pre>
	 * 
	 * @return statement values builder
	 */
	public ValuesBuilder values() {
		return new ValuesBuilder( entity, commandType );
	}
	
	/**
	 * <p>The where condition used to generate the statement in the template
	 * factory
	 * 
	 * <p>This build object can only be applied to dynamic template compilation.
	 * 
	 * <pre>
	 * <code>&#64;&#123;this.where(Object)&#125;</code> -&gt; [where] id = &#35;&#123;id&#125; [and] name = &#35;&#123;name&#125;
	 * </pre>
	 * 
	 * @param parameter dynamic parameter
	 * @return query condition builder object
	 */
	public WhereBuilder where( Object parameter ) {
		return new WhereBuilder( entity, commandType, parameter );
	}
	
	/**
	 * <p>
	 * Used to generate default sort fields in the template factory
	 * 
	 * <p>
	 * In theory it can be used in any scenario, but it is usually used for
	 * static template compilation.
	 * 
	 * <pre>
	 * <code>&#64;&#123;this.defaultOrderBy&#125;</code> -&gt; [order by] column [desc]
	 * <code>&#64;&#123;this.defaultOrderBy.prefix("t")&#125;</code> -&gt; [order by] t.column [desc]
	 * <code>&#64;&#123;this.defaultOrderBy.asc&#125;</code> -&gt; [order by] column [asc]
	 * <code>&#64;&#123;this.defaultOrderBy.desc&#125;</code> -&gt; [order by] column [desc]
	 * </pre>
	 * 
	 * @return order by builder object
	 */
	public OrderByBuilder defaultOrderBy() {
		return new OrderByBuilder( entity );
	}
	
	/**
	 * <p>
	 * Used to generate a logically delete builder object in a template factory, either
	 * in an update statement or in a query statement object.
	 * 
	 * <pre>
	 * <code>&#64;&#123;this.tryLogicallyDelete&#125;</code> -&gt; logically_delete = &#35;&#123;deleteValue&#125;
	 * <code>&#64;&#123;this.tryLogicallyDelete.prefix("t")&#125;</code> -&gt; t.logically_delete = &#35;&#123;deleteValue&#125;
	 * <code>&#64;&#123;this.tryLogicallyDelete.useWhereQuery&#125;</code> -&gt; [where] logically_delete = &#35;&#123;selectValue&#125;
	 * <code>&#64;&#123;this.tryLogicallyDelete.useAndQuery&#125;</code> -&gt; [and] logically_delete = &#35;&#123;selectValue&#125;
	 * <code>&#64;&#123;this.tryLogicallyDelete.useOrQuery&#125;</code> -&gt; [or] logically_delete = &#35;&#123;selectValue&#125;
	 * </pre>
	 * 
	 * @return logically delete builder object
	 */
	public LogicallyDeleteBuilder tryLogicallyDelete() {
		return new LogicallyDeleteBuilder( entity, commandType );
	}
	
	/**
	 * <p>
	 * Used to generate an optimistic lock builder object, only valid in the
	 * modification statement.
	 * 
	 * <pre>
	 * <code>&#64;&#123;this.tryOptimisticLock&#125;</code> -&gt; version = &#35;&#123;version&#125;
	 * <code>&#64;&#123;this.tryOptimisticLock.prefix("t")&#125;</code> -&gt; t.version = &#35;&#123;version&#125;
	 * <code>&#64;&#123;this.tryOptimisticLock.useWhereQuery&#125;</code> -&gt; [where] version = &#35;&#123;version&#125;
	 * <code>&#64;&#123;this.tryOptimisticLock.useAndQuery&#125;</code> -&gt; [and] version = &#35;&#123;version&#125;
	 * <code>&#64;&#123;this.tryOptimisticLock.useOrQuery&#125;</code> -&gt; [or] version = &#35;&#123;version&#125;
	 * </pre>
	 * 
	 * @return
	 */
	public OptimisticLockBuilder tryOptimisticLock() {
		return new OptimisticLockBuilder( entity );
	}
	
}

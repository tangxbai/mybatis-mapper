/**
 * Copyright (C) 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.viiyue.plugins.mybatis.metadata;

import static com.viiyue.plugins.mybatis.Constants.PRIMARY_KEY;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;

import com.viiyue.plugins.mybatis.enums.ExpressionStyle;
import com.viiyue.plugins.mybatis.enums.NameStyle;
import com.viiyue.plugins.mybatis.enums.Setting;
import com.viiyue.plugins.mybatis.enums.ValueStyle;
import com.viiyue.plugins.mybatis.metadata.info.GeneratedKeyInfo;
import com.viiyue.plugins.mybatis.metadata.info.LogicallyDeleteInfo;
import com.viiyue.plugins.mybatis.metadata.info.OrderByInfo;
import com.viiyue.plugins.mybatis.metadata.info.VersionInfo;
import com.viiyue.plugins.mybatis.utils.Assert;
import com.viiyue.plugins.mybatis.utils.ObjectUtil;
import com.viiyue.plugins.mybatis.utils.StringAppender;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * <p>
 * A metadata object for a database table that describes the association between
 * a Java entity bean and a database table.
 * 
 * <p>You can easily get this object through the tool class.
 * <pre>
 * &#47;&#47; Get the metadata of the table
 * Entity entity = EntityParser.getEntity(Bean.class);
 * 
 * &#47;&#47; Get the table name
 * String tableName = entity.getTableName();
 * 
 * &#47;&#47; Get the default primary key
 * Property primaryKey = entity.getDefaultPrimaryKey();
 * 
 * &#47;&#47; Get all primary keys
 * List&lt;Property&gt; primaryKeys = entity.getPrimaryKeys();
 * 
 * &#47;&#47; Get all column properties
 * List&lt;Property&gt; properties = entity.getProperties();
 * 
 * &#47;&#47; Or others...</pre>
 *
 * @author tangxbai
 * @since 1.0.0
 */
public final class Entity {
	
	// Basic info
	
	private Class<?> beanType;
	private String beanName;
	private String tableName;
	private String baseResultMap;
	
	// SQL styles
	
	private NameStyle nameStyle; // propertyName -> property_name
	private ValueStyle valueStyle; // #{id, javaType=String, jdbcType=VARCHAR, typeHandler=Handler}
	private ExpressionStyle expressionStyle; // id = #{id, javaType=String, jdbcType=VARCHAR, typeHandler=Handler}
	
	// Helper info
	
	private OrderByInfo orderByInfo;
	private VersionInfo versionInfo;
	private GeneratedKeyInfo generatedKeyInfo;
	private LogicallyDeleteInfo logicallyDeleteInfo;
	
	// Properties
	
	private Property defaultPrimaryKey;
	private final List<Property> primaryKeys = new ArrayList<Property>();
	private final List<Property> properties = new ArrayList<Property>();
	private final Map<String, Property> propertyMappings = new HashMap<String, Property>();
	
	// Constructor
	
	public Entity( Class<?> beanType ) {
		this.beanType = beanType;
		this.beanName = beanType.getSimpleName();
	}
	
	// Setter
	
	protected void setTableName( String tableName ) {
		this.tableName = tableName;
	}
	
	protected void setBaseResultMap( String baseResultMap ) {
		this.baseResultMap = baseResultMap;
	}
	
	protected void setNameStyle( NameStyle nameStyle ) {
		this.nameStyle = nameStyle;
	}
	
	protected void setValueStyle( ValueStyle valueStyle ) {
		this.valueStyle = valueStyle;
	}

	protected void setExpressionStyle( ExpressionStyle expressionStyle ) {
		this.expressionStyle = expressionStyle;
	}
	
	protected void setOrderByInfo( OrderByInfo orderByInfo ) {
		this.orderByInfo = orderByInfo;
	}
	
	protected void setGeneratedKeyInfo( GeneratedKeyInfo generatedKeyInfo ) {
		Assert.isNull( this.generatedKeyInfo, "A class can only contain one @GeneratedKey field" );
		this.generatedKeyInfo = generatedKeyInfo;
	}
	
	protected void setVersionInfo( VersionInfo versionInfo ) {
		Assert.isNull( this.versionInfo, "A class can only contain one @Version field" );
		this.versionInfo = versionInfo;
	}
	
	protected void setLogicallyDeleteInfo( LogicallyDeleteInfo logicallyDeleteInfo ) {
		Assert.isNull( this.logicallyDeleteInfo, "A class can only contain one @LogicallyDelete field" );
		this.logicallyDeleteInfo = logicallyDeleteInfo;
	}
	
	// Getter

	public Class<?> getBeanType() {
		return beanType;
	}
	
	public String getBeanName() {
		return beanName;
	}

	public String getTableName() {
		return tableName;
	}
	
	public String getWrappedTableName() {
		return Setting.ColumnStyle.getStyleValue( tableName );
	}

	public String getBaseResultMap() {
		return baseResultMap;
	}
	
	public NameStyle getNameStyle() {
		return nameStyle;
	}
	
	public ValueStyle getValueStyle() {
		return valueStyle;
	}

	public ExpressionStyle getExpressionStyle() {
		return expressionStyle;
	}
	
	public OrderByInfo getOrderByInfo() {
		return orderByInfo;
	}
	
	public VersionInfo getVersionInfo() {
		return versionInfo;
	}
	
	public GeneratedKeyInfo getGeneratedKeyInfo() {
		return generatedKeyInfo;
	}

	public LogicallyDeleteInfo getLogicallyDeleteInfo() {
		return logicallyDeleteInfo;
	}
	
	public List<Property> getProperties() {
		return Collections.unmodifiableList( properties );
	}
	
	public List<Property> getPrimaryKeys() {
		return Collections.unmodifiableList( primaryKeys );
	}
	
	public Map<String, Property> getPropertyMappings() {
		return Collections.unmodifiableMap( propertyMappings );
	}
	
	// Helper
	
	public boolean useResultMap() {
		return StringUtil.isNotBlank( baseResultMap );
	}
	
	public boolean hasAnyProperties() {
		return ObjectUtil.isNotEmpty( properties );
	}
	
	public boolean hasDefaultOrderByInfo() {
		return orderByInfo != null;
	}
	
	public boolean hasOptimisticLock() {
		return versionInfo != null;
	}
	
	public boolean hasGeneratedKeyInfo() {
		return generatedKeyInfo != null;
	}
	
	public Column getColumn( String propertyName ) {
		return getProperty( propertyName ).getColumn();
	}
	
	public Property getDefaultPrimaryKey() {
		// If you do not explicitly specify which primary key to use, 
		// the first primary key is used by default.
		if ( defaultPrimaryKey == null ) {
			this.defaultPrimaryKey = getPrimaryKey( 0 );
		}
		return defaultPrimaryKey;
	}
	
	public String getPrimaryKeyNames( String delimiter ) {
		StringAppender appender = new StringAppender();
		for ( Property property : primaryKeys ) {
			appender.addDelimiter( delimiter );
			appender.append( property.getName() );
		}
		return appender.toString();
	}
	
	public Property getPrimaryKey( int index ) {
		Assert.notEmpty( primaryKeys, "No primary key found, please configure @Id annotation to identify primary key." );
		Assert.isTrue( index >= 0 && index < primaryKeys.size(), "Primary key index out of range : {0} of {1}", index, primaryKeys.size() );
		return primaryKeys.get( index );
	}
	
	public Property getProperty( String propertyName ) {
		Assert.notNull( propertyName, "Target property name cannot be null" );
		// Primary key( '#pk' or '#pk/index' )
		if ( propertyName.equals( PRIMARY_KEY ) || propertyName.startsWith( PRIMARY_KEY + "/" ) ) {
			String [] indexs = propertyName.split( "/" ); // [ #pk ] or [ #pk, index ]
			return getPrimaryKey( indexs.length == 1 ? 0 : NumberUtils.toInt( indexs[ 1 ], 0 ) );
		}
		// Normal property
		Assert.notEmpty( propertyMappings, "Model bean {0} does not have any available properties ...", beanName );
		Property property = propertyMappings.get( propertyName );
		Assert.notNull( property, "Property #{0}# not found", propertyName );
		return property;
	}
	
	protected void addProperty( Property property ) {
		if ( property.isVisible() ) {
			this.properties.add( property );
			this.propertyMappings.put( property.getName(), property );
			this.addPrimaryKey( property );
			this.setDefaultPrimaryKey( property );
		}
	}
	
	private void addPrimaryKey( Property property ) {
		if ( property.isPrimaryKey() ) {
			this.primaryKeys.add( property );
		}
	}
	
	private void setDefaultPrimaryKey( Property property ) {
		if ( property.isPrimary() && this.defaultPrimaryKey == null ) {
			this.defaultPrimaryKey = property;
		}
	}
	
	protected Entity sorted() {
		Collections.sort( properties );
		Collections.sort( primaryKeys );
		return this;
	}
	
	// Override

	@Override
	public String toString() {
		return StringUtil.toString( this, "%s -> %s", getBeanName(), getTableName() );
	}
	
}

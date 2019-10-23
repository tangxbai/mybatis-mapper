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
package com.viiyue.plugins.mybatis.metadata;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.UnknownTypeHandler;

import com.viiyue.plugins.mybatis.Constants;
import com.viiyue.plugins.mybatis.annotation.bean.DefaultOrderBy;
import com.viiyue.plugins.mybatis.annotation.bean.Excludes;
import com.viiyue.plugins.mybatis.annotation.bean.ResultMap;
import com.viiyue.plugins.mybatis.annotation.bean.Table;
import com.viiyue.plugins.mybatis.annotation.member.Conditional;
import com.viiyue.plugins.mybatis.annotation.member.GeneratedKey;
import com.viiyue.plugins.mybatis.annotation.member.GeneratedValue;
import com.viiyue.plugins.mybatis.annotation.member.Id;
import com.viiyue.plugins.mybatis.annotation.member.Index;
import com.viiyue.plugins.mybatis.annotation.member.LogicallyDelete;
import com.viiyue.plugins.mybatis.annotation.member.Version;
import com.viiyue.plugins.mybatis.annotation.rule.ExpressionRule;
import com.viiyue.plugins.mybatis.annotation.rule.NamingRule;
import com.viiyue.plugins.mybatis.annotation.rule.ValueRule;
import com.viiyue.plugins.mybatis.enums.NameStyle;
import com.viiyue.plugins.mybatis.enums.Type;
import com.viiyue.plugins.mybatis.metadata.info.ConditionalInfo;
import com.viiyue.plugins.mybatis.metadata.info.GeneratedKeyInfo;
import com.viiyue.plugins.mybatis.metadata.info.GeneratedValueInfo;
import com.viiyue.plugins.mybatis.metadata.info.LogicallyDeleteInfo;
import com.viiyue.plugins.mybatis.metadata.info.OrderByInfo;
import com.viiyue.plugins.mybatis.metadata.info.VersionInfo;
import com.viiyue.plugins.mybatis.utils.Assert;
import com.viiyue.plugins.mybatis.utils.FieldUtil;
import com.viiyue.plugins.mybatis.utils.GenericTypeUtil;
import com.viiyue.plugins.mybatis.utils.LoggerUtil;
import com.viiyue.plugins.mybatis.utils.ObjectUtil;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * The model bean parses the helper class, which is used to transform the model
 * bean into a description object of the database table.
 *
 * @author tangxbai
 * @since 1.1.0
 */
public final class EntityParser {
	
	private static final Map<Class<?>, Entity> cached = new ConcurrentHashMap<Class<?>, Entity>( 64 );

	public static Entity getEntityFromInterface( Class<?> interfaceType ) {
		Assert.notNull( interfaceType, "Interface class type cannot be null" );
		Assert.isTrue( interfaceType.isInterface(), "Interface #{0}# class type must be an interface", interfaceType.getSimpleName() );
		Entity entity = cached.get( interfaceType );
		if ( entity == null ) {
			Class<?> modeBeanType = GenericTypeUtil.getInterfaceGenericType( interfaceType, 0 );
			entity = getEntity( modeBeanType );
			cached.put( interfaceType, entity );
		}
		return entity;
	}
	
	public static Entity getEntity( Class<?> beanType ) {
		Assert.notNull( beanType, "Class type cannot be null" );
		Assert.isFalse( beanType.isInterface(), "Database entity class type cannot be an interface" );
		Entity entity = cached.get( beanType );
		if ( entity == null ) {
			synchronized ( cached ) {
				entity = new Entity( beanType );
				
				// @NamingRule
				// Field name conversion rule
				NamingRule namingRule = beanType.getAnnotation( NamingRule.class );
				entity.setNameStyle( namingRule == null ? Constants.DEFAULT_NAME_STYLE : namingRule.value() );
				
				// @ValueRule
				// Database column generation style
				// For example : #{id, javaType=Long, jdbcType=BIGINT}
				ValueRule valueRule = beanType.getAnnotation( ValueRule.class );
				entity.setValueStyle( valueRule == null ? Constants.DEFAULT_VALUE_STYLE : valueRule.value() );
				
				// @ExpressionRule
				// Field assignment expression style
				// For example : id = #{id, javaType=Long, jdbcType=BIGINT}
				ExpressionRule expressionRule = beanType.getAnnotation( ExpressionRule.class );
				entity.setExpressionStyle( expressionRule == null ? Constants.DEFAULT_EXPRESSION_STYLE : expressionRule.value() );
				
				// @ResultMap
				// Base result map name
				ResultMap resultMap = beanType.getAnnotation( ResultMap.class );
				entity.setBaseResultMap( resultMap == null ? Constants.DEFAULT_RESULT_MAP : resultMap.value() );
				
				// Generate table name
				entity.setTableName( getTableName( entity, beanType ) );
				
				// @DefaultOrderBy
				// Default sort field
				DefaultOrderBy defaultOrderBy = beanType.getAnnotation( DefaultOrderBy.class );
				if ( defaultOrderBy != null ) {
					entity.setOrderByInfo( new OrderByInfo( entity, defaultOrderBy ) );
				}
				
				// Exclude unwanted fields
				Excludes excludes = beanType.getAnnotation( Excludes.class );
				String [] excludeArray = excludes == null ? new String [ 0 ] : excludes.value();
				
				// Convert fields to database field columns
				for ( Field field : FieldUtil.getAllFileds( beanType ) ) {
					if ( StringUtil.containsAny( field.getName(), excludeArray ) ) {
						continue;
					}
					if ( field.getType().isPrimitive() ) {
						LoggerUtil.log.warn( "### WARNING : "
							+ "You'd better not use the primitive type, "
							+ "because the primitive type will have a default value, "
							+ "and there will never be a null value." );
					}
					entity.addProperty( getProperty( entity, field ) );
				}
				
				// Cache the parsing result
				cached.put( beanType, entity.sorted() );
			}
		}
		return entity;
	}
	
	private static Property getProperty( Entity parent, Field field ) {
		boolean isPrimaryKey = false;
		Property property = new Property( parent, field );
		
		// @Conditional
		// Dynamic conditional expression for ...BySelective() (e.g. insertBySelective, updateBySelective)
		property.setConditionalInfo( ConditionalInfo.newInfo( property, property.getAnnotation( Conditional.class ) ) );
		
		// @Index
		// The order in which the fields appear
		Index index = property.getAnnotation( Index.class );
		property.setIndex( index == null ? 0 : index.value() );
		
		// @Id
		// Is it the primary key?
		Id id = property.getAnnotation( Id.class );
		if ( id != null ) {
			isPrimaryKey = true;
			property.setPrimaryKey( true );
			property.setPrimary( id.primary() );
		}
		
		// @Id
		// @GeneratedKey
		// Primary key generation strategy
		if ( isPrimaryKey ) {
			GeneratedKey generatedKey = property.getAnnotation( GeneratedKey.class );
			if ( generatedKey != null ) {
				parent.setGeneratedKeyInfo( new GeneratedKeyInfo( property, generatedKey ) );
			}
		}
		
		// @Provider
		// Generated value
		GeneratedValue generatedValue = property.getAnnotation( GeneratedValue.class );
		if ( generatedValue != null ) {
			property.setGeneratedValueInfo( new GeneratedValueInfo( generatedValue ) );
		}
		
		// @Version
		// Is it an optimistic lock field?
		Version version = property.getAnnotation( Version.class );
		if ( version != null ) {
			parent.setVersionInfo( new VersionInfo( property, version ) );
		}
		
		// @LogicalDelection
		// Is it a logical delete field?
		LogicallyDelete logicallyDelete = property.getAnnotation( LogicallyDelete.class );
		if ( logicallyDelete != null ) {
			parent.setLogicallyDeleteInfo( new LogicallyDeleteInfo( property, logicallyDelete ) );
		}
		
		// @Column
		// Field metadata configuration
		com.viiyue.plugins.mybatis.annotation.member.Column col = property.getAnnotation( com.viiyue.plugins.mybatis.annotation.member.Column.class );
		if ( col == null ) { // Default
			property.setIgnore( false );
			property.setNullbale( true );
			property.setUpdatable( true );
			property.setInsertable( true );
		} else { // Custom
			property.setIgnore( col.ignore() );
			property.setNullbale( col.nullable() );
			property.setUpdatable( col.updatable() );
			property.setInsertable( col.insertable() );
		}
		property.setColumn( getColumn( parent, property, col ) );
		return property;
	}
	
	private static Column getColumn( Entity parent, Property property, com.viiyue.plugins.mybatis.annotation.member.Column col ) {
		JdbcType jdbcType = null;
		NameStyle nameStyle = parent.getNameStyle();
		String columnAlias = nameStyle.convert( property.getName() );
		String columnName = columnAlias;
		Column column = new Column( parent, property );
		if ( col != null ) {
			if ( ObjectUtil.isDifferent( Type.UNDEFINED, col.jdcbType() ) ) {
				jdbcType = col.jdcbType().jdbcType();
			}
			if ( ObjectUtil.isDifferent( UnknownTypeHandler.class, col.typeHandler() ) ) {
				column.setTypeHandler( col.typeHandler() );
			}
			columnName = StringUtil.isBlank( col.name() ) ? columnAlias : col.name();
		}
		if ( jdbcType == null ) {
			jdbcType = Type.forJdbcType( property.getJavaType() );
		}
		column.setName( columnName );
		column.setAlias( columnAlias );
		column.setJdbcType( jdbcType );
		return column;
	}
	
	private static String getTableName( Entity entity, Class<?> beanType ) {
		String beanName = beanType.getSimpleName();
		NameStyle nameStyle = entity.getNameStyle();
		Table table = beanType.getAnnotation( Table.class );
		
		// By default, the class name is used for conversion.
		if ( table == null ) {
			return nameStyle.convert( beanName );
		}

		// If the default value is not empty, the default value is used as the table name.
		String tableName = StringUtil.firstNonBlank( table.value(), table.name() );
		if ( tableName != null ) {
			return tableName;
		}

		// Remove class name specific prefix or suffix
		if ( ObjectUtil.isNotEmpty( table.trim() ) ) {
			final String [] trims = table.trim();
			final int size = trims.length;
			// [ prefix, ? ]
			// First element is the prefix string text
			if ( size > 1 ) {
				String prefix = trims[ 0 ];
				if ( StringUtil.isNotEmpty( prefix ) && beanName.startsWith( prefix ) ) {
					beanName = beanName.substring( prefix.length() );
				}
			}
			// [ ?, suffix ]
			// Last element is the suffix string text
			String suffix = trims[ size - 1 ];
			if ( StringUtil.isNotEmpty( suffix ) && beanName.endsWith( suffix ) ) {
				beanName = beanName.substring( 0, beanName.length() - suffix.length() );
			}
		}
		
		// Convert class name to table name
		NameStyle style = table.style() == NameStyle.DEFAULT ? nameStyle : table.style();
		String converted = style.convert( beanName );
		return table.prefix() + converted + table.suffix();
	}
	
}

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
package com.viiyue.plugins.mybatis.scripting.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import com.viiyue.plugins.mybatis.Constants;
import com.viiyue.plugins.mybatis.metadata.Column;
import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.metadata.EntityParser;
import com.viiyue.plugins.mybatis.metadata.Property;
import com.viiyue.plugins.mybatis.utils.ClassUtil;
import com.viiyue.plugins.mybatis.utils.FieldUtil;
import com.viiyue.plugins.mybatis.utils.LoggerUtil;

/**
 * Result Map registry
 *
 * @author tangxbai
 * @since 1.1.0
 */
final class ResultMapRegistry {
	
	public static ResultMap getResultMap( 
		Configuration configuration, 
		Class<?> mapperInterface, 
		Class<?> modelBeanType, 
		Class<?> returnBeanType ) {
		
		Entity entity = EntityParser.getEntity( modelBeanType );
		String id = mapperInterface.getName() + "." + entity.getBaseResultMap();
		
		// The first time it is loaded, it will be automatically registered to the configuration, 
		// and it can be used directly next time.
		if ( configuration.getResultMapNames().contains( id ) ) {
			return configuration.getResultMap( id );
		}
		
		// Return null directly without any available properties
		if ( !entity.hasAnyProperties() ) {
			return null;
		}
		
		StopWatch monitor = StopWatch.createStarted();
		if ( LoggerUtil.isEnableCompilationLog() ) {
			LoggerUtil.log.debug( "Initialization the result map of <" + mapperInterface.getSimpleName() + "> ..." );
			LoggerUtil.log.debug( LoggerUtil.dividingLine );
			LoggerUtil.log.debug( "<resultMap id=\"" + id + "\" type=\"" + returnBeanType.getName() + "\">" );
		}
		
		// Create a basic result Map object for the mapper
		List<ResultMapping> resultMappings = new ArrayList<ResultMapping>();
		List<String> returnTypeProperties = FieldUtil.getAllFiledNames( returnBeanType );
		TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
		for ( Property property : entity.getProperties() ) {
			if ( returnTypeProperties.contains( property.getName() ) ) {
				ResultMapping.Builder builder = new ResultMapping.Builder( configuration, property.getName() );
				String nodeName = "result";
				Column column = property.getColumn();
				JdbcType jdbcType = column.getJdbcType();
				Class<?> javaType = property.getJavaType();
				
				// Basic 
				builder.column( column.getName() );
				builder.javaType( javaType );
				builder.jdbcType( jdbcType );
				
				// Primary key flag
				if ( property.isPrimaryKey() ) {
					nodeName = "id";
					builder.flags( Arrays.asList( ResultFlag.ID ) );
				}
				
				// Type handler
				Class<? extends TypeHandler<?>> typeHandler = column.getTypeHandler();
				if ( typeHandler != null ) {
					TypeHandler<?> handler = typeHandlerRegistry.getMappingTypeHandler( typeHandler );
					if ( handler == null ) {
						typeHandlerRegistry.register( typeHandler );
						handler = typeHandlerRegistry.getMappingTypeHandler( typeHandler );
					}
					builder.typeHandler( handler );
				}
				resultMappings.add( builder.build() );
				
				if ( LoggerUtil.isEnableCompilationLog() ) {
					String javaTypeText = ClassUtil.isCommonType( javaType ) ? javaType.getSimpleName() : javaType.getName();
					String typeHandlerText = typeHandler == null ? Constants.EMPTY : " typeHandler=\"" + typeHandler.getName() + "\"";
					LoggerUtil.log.debug( 
						"    <" + nodeName + 
						" property=\"" + property.getName() + "\"" + 
						" column=\"" + column.getName() + "\"" + 
						" javaType=\"" + javaTypeText + "\"" + 
						" jdbcType=\"" + jdbcType + "\"" + typeHandlerText + "/>" );
				}
			}
		}
		if ( LoggerUtil.isEnableCompilationLog() ) {
			LoggerUtil.log.debug( "</resultMap>" );
			LoggerUtil.log.debug( LoggerUtil.dividingLine );
			LoggerUtil.log.debug( "Time : " + LoggerUtil.getWatchTime( monitor ) );
			LoggerUtil.log.debug( LoggerUtil.dividingLine );
			LoggerUtil.log.debug( Constants.EMPTY );
		}
		ResultMap resultMap = new ResultMap.Builder( configuration, id, returnBeanType, resultMappings, true ).build();
		configuration.addResultMap( resultMap );
		return resultMap;
	}
	
}

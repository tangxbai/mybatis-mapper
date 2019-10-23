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
package com.viiyue.plugins.mybatis.scripting.parser;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;

import com.viiyue.plugins.mybatis.annotation.mark.EnableResultMap;
import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.metadata.EntityParser;
import com.viiyue.plugins.mybatis.metadata.info.GeneratedKeyInfo;
import com.viiyue.plugins.mybatis.utils.LoggerUtil;
import com.viiyue.plugins.mybatis.utils.StatementUtil;

/**
 * Provide some methods for refactoring MappedStatement
 *
 * @author tangxbai
 * @since 1.1.0
 * @see DynamicProviderRegistry
 */
public final class MappedStatementRegistry {
	
	private MappedStatementRegistry() {}

	private static boolean isRecycled = false;
	private DynamicProviderRegistry providerRegistry = new DynamicProviderRegistry();
	private Set<Class<?>> registered = new HashSet<Class<?>>();
	private Map<Class<?>, Map<String, Method>> interfaceMethodMappings = new HashMap<Class<?>, Map<String,Method>>();
	
	/**
	 * MappedStatementRegistry singleton mode
	 */
	private static class Holder {
		private static final MappedStatementRegistry registry = new MappedStatementRegistry();
	}
	
	/**
	 * Return a single instance object of {@link MappedStatementRegistry}
	 * 
	 * @return the MappedStatementRegistry instance
	 */
	public static final MappedStatementRegistry instance() {
		return Holder.registry;
	}
	
	/**
	 * Recycling resources and properly assisting the GC in garbage collection
	 */
	public void recycle() {
		statusCheck();
		isRecycled = true;
		providerRegistry.recycle();
		interfaceMethodMappings.clear();
		providerRegistry = null;
		registered = null;
		interfaceMethodMappings = null;
	}
	
	/**
	 * Register the mapper interface and scan it for initialization
	 * 
	 * @param configuration the mybatis configuration
	 * @param mapperInterface the mapper interface type
	 * @param modelBeanType model bean type in mapper generics
	 * @param returnBeanType mapper query method returns bean type
	 */
	public void registerMapper( 
		Configuration configuration, 
		Class<?> mapperInterface, 
		Class<?> modelBeanType, 
		Class<?> returnBeanType ) {
		statusCheck();
		if ( registered.contains( mapperInterface ) ) {
			return;
		}
		
		// Map the mapper method to the provider method
		// and cache it for direct query later
		providerRegistry.scanning( mapperInterface );
		
		// Register all methods of the mapper interface to quickly and directly
		// locate a method
		registerMapperMethod( mapperInterface );
		
		// Initialize the parsing object of the model bean
		// The first time the data is loaded will be cached, 
		// and the result will be obtained directly after the call.
		EntityParser.getEntity( modelBeanType );
		
		// If the result map is not loaded, 
		// the result map will be initialized automatically.
		ResultMapRegistry.getResultMap( configuration, mapperInterface, modelBeanType, returnBeanType );
		
		// Record the scanned mapper, which will be printed out by the log after
		// the program is started.
		LoggerUtil.addMapper( mapperInterface );
		registered.add( mapperInterface );
	}
	
	/**
	 * Bind the specific sql source for the annotation sql provider
	 * 
	 * @param ms the {@code MappedStatement} instance
	 * @param msObject the {@code MappedStatement} metadata information
	 * @param mapperInterface the mapper interface type
	 * @param modelBeanType model bean type in mapper generics
	 * @param namespace the mapper namespace
	 * @param methodName the mapper interface method
	 */
	public void bindSqlSource( 
		MappedStatement ms, 
		MetaObject msObject, 
		Class<?> mapperInterface, 
		Class<?> modelBeanType, 
		String namespace, 
		String methodName ) {
		statusCheck();
		SqlSource source = providerRegistry.getSqlSource( ms, mapperInterface, modelBeanType, namespace, methodName );
		if ( source != null ) {
			msObject.setValue( "sqlSource", source );
		}
	}
	
	/**
	 * Binding result mapping for sql source
	 * 
	 * @param ms the {@code MappedStatement} instance
	 * @param msObject the {@code MappedStatement} metadata information
	 * @param mapperInterface the mapper interface type
	 * @param modelBeanType model bean type in mapper generics
	 * @param returnBeanType mapper query method returns bean type
	 * @param namespace the mapper namespace
	 * @param methodName the mapper interface method
	 */
	public void bindResultMap( 
		MappedStatement ms, 
		MetaObject msObject, 
		Class<?> mapperInterface, 
		Class<?> modelBeanType, 
		Class<?> returnBeanType, 
		String namespace, 
		String methodName ) {
		statusCheck();
		if ( StatementUtil.isSelect( ms ) ) {
			Method method = getIntefaceMethod( mapperInterface, methodName );
			if ( method.isAnnotationPresent( EnableResultMap.class ) ) {
				ResultMap resultMap = ResultMapRegistry.getResultMap( ms.getConfiguration(), mapperInterface, modelBeanType, returnBeanType );
				if ( resultMap != null ) {
					msObject.setValue( "resultMaps", Arrays.asList( resultMap ) );
				}
			}
		}
	}
	
	/**
	 * Binding the primary key generator for the sql source
	 * 
	 * @param ms the {@code MappedStatement} instance
	 * @param msObject the {@code MappedStatement} metadata information
	 * @param mapperInterface the mapper interface type
	 * @param modelBeanType model bean type in mapper generics
	 * @param namespace the mapper namespace
	 * @param methodName the mapper interface method
	 */
	public void bindKeyGenerator( 
		MappedStatement ms, 
		MetaObject msObject, 
		Class<?> mapperInterface, 
		Class<?> modelBeanType, 
		String namespace, 
		String methodName ) {
		statusCheck();
		String selectKeyId = ms.getId() + SelectKeyGenerator.SELECT_KEY_SUFFIX;
		Configuration configuration = ms.getConfiguration();
		if ( !configuration.hasKeyGenerator( selectKeyId ) ) {
			Entity entity = EntityParser.getEntity( modelBeanType );
			if ( StatementUtil.isInsert( ms ) && entity.hasGeneratedKeyInfo() ) {
				// Find the mapper method object by method name
				Method method = getIntefaceMethod( mapperInterface, methodName );
				// Generate and register a key generator
				GeneratedKeyInfo generatedKeyInfo = entity.getGeneratedKeyInfo();
				KeyGeneratorRegistry registry = new KeyGeneratorRegistry( ms, entity, selectKeyId );
				KeyGenerator keyGenerator = registry.getKeyGenerator( method );
				configuration.addKeyGenerator( selectKeyId, keyGenerator );
				// Change key generator
				msObject.setValue( "keyGenerator", keyGenerator );
				msObject.setValue( "keyColumns", generatedKeyInfo.getKeyColumns() );
				msObject.setValue( "keyProperties", generatedKeyInfo.getKeyProperties() );
			}
		}
	}
	
	/**
	 * Instance status check
	 */
	public void statusCheck() {
		if ( isRecycled ) {
			throw new IllegalStateException( "The mybatis mapper registry has been recycled and cannot be accessed." );
		}
	}
	
	/**
	 * Get the cached interface method by specified method name
	 * 
	 * @param mapperInterface the mapper interface type
	 * @param methodName mapper method name
	 * @return the found mapper method
	 */
	public Method getIntefaceMethod( Class<?> mapperInterface, String methodName ) {
		statusCheck();
		Map<String, Method> methods = interfaceMethodMappings.get( mapperInterface );
		if ( methods != null ) {
			return methods.get( methodName );
		}
		return null;
	}
	
	/**
	 * Cache all visible methods of mapper, convenient to find specific methods
	 * directly through the interface class and method name
	 * 
	 * @param mapperInterface the mapper interface type
	 */
	private void registerMapperMethod( Class<?> mapperInterface ) {
		if ( interfaceMethodMappings.get( mapperInterface ) == null ) {
			Map<String, Method> methods = new HashMap<String, Method>();
			for ( Method method : mapperInterface.getMethods() ) {
				methods.put( method.getName(), method );
			}
			interfaceMethodMappings.put( mapperInterface, methods );
		}
	}
	
}

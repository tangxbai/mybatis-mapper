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
package com.viiyue.plugins.mybatis.scripting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;

import com.viiyue.plugins.mybatis.mapper.Marker;
import com.viiyue.plugins.mybatis.scripting.parser.MappedStatementRegistry;
import com.viiyue.plugins.mybatis.utils.BuilderUtil;
import com.viiyue.plugins.mybatis.utils.ClassUtil;
import com.viiyue.plugins.mybatis.utils.GenericTypeUtil;

/**
 * Mybatis mapper refactors the build object
 *
 * @author tangxbai
 * @since 1.1.0
 * @see MappedStatementRegistry
 */
public final class MyBatisMapperBuilder {
	
	public static final Class<?> marker = Marker.class;
	public static final MappedStatementRegistry registry = MappedStatementRegistry.instance();

	/**
	 * Selective refactoring of SQL sources loaded in mybatis
	 * 
	 * @param configuration mybatis core configuration
	 * @return the restructured configuration object
	 */
	public Configuration refactoring( Configuration configuration ) {
		// Status check
		// If the resource is recycled, it means that the whole operation 
		// has been completed and no further operations are required.
		registry.statusCheck();
		
		// Why do I need to copy a copy object here? 
		// Because the statement may be added to the MappedStatement later, 
		// if you do not copy a backup, adding an object in the loop will report 
		// java.util.ConcurrentModificationException
		Collection<MappedStatement> statements = configuration.getMappedStatements();
		List<Object> copyedStatements = new ArrayList<Object>( statements );
		for ( Object statementObject : copyedStatements ) {
			// MappedStatement id with short name
			// In fact, there is no substantive effect in the new version,
			// it may be to be compatible with the old version.
			if ( !( statementObject instanceof MappedStatement ) ) {
				statements.remove( statementObject ); // @See Configuration.StrictMap<V>.Ambiguity -> Line(850)
				continue;
			}
			
			// Only SQL scripts based on annotations are processed here,
			// other modes are not parsed here.
			MappedStatement ms = ( MappedStatement ) statementObject;
			SqlSource source = ms.getSqlSource();
			boolean isProviderSqlSource = source instanceof ProviderSqlSource; // Provider annotation sql source
			boolean isMapperSqlSource = source instanceof MyBatisMapperSqlSource; // Xml statement sql source
			if ( !( isProviderSqlSource || isMapperSqlSource ) ) {
				continue;
			}
			
			// Get the Mapper interface related information from the id of the MappedStatement
			String namespace = ms.getId();
			String classpath = BuilderUtil.getClasspath( namespace ); // since mybatis 3.0+
			String methodName = BuilderUtil.getMethodName( namespace );
			Class<?> mapperInterface = ClassUtil.forName( classpath );
			
			// Only handles interfaces marked by the Marker interface,
			// so users can write their own BaseMapper instead of just using the
			// default Mapper interface.
			if ( !isMapperMarked( mapperInterface ) ) continue;
			
			// Only handles interfaces marked by the Marker interface,
			// so users can write their own BaseMapper instead of just using the
			// default Mapper interface.
			MetaObject msObject = SystemMetaObject.forObject( ms );
			Class<?> modelBeanType = GenericTypeUtil.getInterfaceGenericType( mapperInterface, 0 );
			Class<?> returnBeanType = GenericTypeUtil.getInterfaceGenericType( mapperInterface, 1 );
			
			// If the Mapper interface has not been registered before, it will
			// be automatically registered, otherwise the interface will be skipped.
			registry.registerMapper( configuration, mapperInterface, modelBeanType, returnBeanType );
			
			// If it is already CommonMapperSqlSource, then you don't have to
			// deal with the sql source.
			if ( isProviderSqlSource ) {
				registry.bindSqlSource( ms, msObject, mapperInterface, modelBeanType, namespace, methodName );
			}
			
			// Continue to bind the result map and primary key generator
			registry.bindResultMap( ms, msObject, mapperInterface, modelBeanType, returnBeanType, namespace, methodName );
			registry.bindKeyGenerator( ms, msObject, mapperInterface, modelBeanType, namespace, methodName );
		}
		registry.recycle();
		return configuration;
	}
	
	/**
	 * Determine if the interface has been marked by the Marker interface
	 * 
	 * @param mapperInterface mapper interface type
	 * @return {@code true} is marked, {@code false} is not
	 */
	private boolean isMapperMarked( Class<?> mapperInterface ) {
		return ClassUtil.isAssignable( mapperInterface, Marker.class, false );
	}
	
}

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

import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.session.Configuration;

import com.viiyue.plugins.mybatis.Constants;
import com.viiyue.plugins.mybatis.api.GeneratedValueProvider;
import com.viiyue.plugins.mybatis.api.NextVersionProvider;
import com.viiyue.plugins.mybatis.condition.Condition;
import com.viiyue.plugins.mybatis.condition.Example;
import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.metadata.EntityParser;
import com.viiyue.plugins.mybatis.metadata.Property;
import com.viiyue.plugins.mybatis.metadata.info.GeneratedValueInfo;
import com.viiyue.plugins.mybatis.metadata.info.VersionInfo;
import com.viiyue.plugins.mybatis.template.TemplateHandler;
import com.viiyue.plugins.mybatis.utils.Assert;
import com.viiyue.plugins.mybatis.utils.BuilderUtil;
import com.viiyue.plugins.mybatis.utils.ClassUtil;
import com.viiyue.plugins.mybatis.utils.LoggerUtil;
import com.viiyue.plugins.mybatis.utils.MapUtil;
import com.viiyue.plugins.mybatis.utils.SingletonUtil;
import com.viiyue.plugins.mybatis.utils.StatementUtil;
import com.viiyue.plugins.mybatis.utils.StringAppender;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * Mybatis-mapper compiled SQL source, including provider annotations and XML
 * statement compilation.
 *
 * @author tangxbai
 * @since 1.1.0, 1.3.7(U)
 */
public final class MyBatisMapperSqlSource implements SqlSource {
	
	private static final String [] arrayParameterKeys = { "array", "inArguments" };
	private static final String [] modifyMethodPrefixs = { "update", "modify", "refresh", "edit" };
	
	// Basic
	
	private final SqlNode sqlNode;
	private final Configuration configuration;
	
	// Helper
	
	private final String namespace;
	private final String methodName;
	private final Entity entity;
	private final Class<?> modelBeanType;
	private final SqlCommandType commandType;
	private final boolean isNeedDynamicProcessing;
	private final boolean isFromXmlBuilder;
	
	// Temporary
	// 1.3.7 - fix bug: "column cannot be null"
	// private MetaObject metaObject;

	// Constructor
	
	public MyBatisMapperSqlSource( 
		SqlNode sqlNode,
		Configuration configuration,
		SqlCommandType commandType,
		Class<?> modelBeanType,
		String namespace,
		boolean isNeedDynamicProcessing,
		boolean isFromXmlBuilder ) {
		this.sqlNode = sqlNode;
		this.configuration = configuration;
		this.commandType = commandType;
		this.modelBeanType = modelBeanType;
		this.namespace = namespace;
		this.methodName = BuilderUtil.getMethodName( namespace );
		this.isNeedDynamicProcessing = isNeedDynamicProcessing;
		this.isFromXmlBuilder = isFromXmlBuilder;
		this.entity = EntityParser.getEntity( modelBeanType );
	}

	@Override
	public BoundSql getBoundSql( Object parameterObject ) {
		MetaObject metaObject = configuration.newMetaObject( parameterObject ); // 1.3.7 - fix bug: "column cannot be null"
		
		refactoringParameter( parameterObject, metaObject ); // 1.3.7 - fix bug: "column cannot be null"
		DynamicContext context = new DynamicContext( configuration, parameterObject );
		sqlNode.apply( context );
		
		// Original sql text
		String sqlText = context.getSql();
		
		// Process template sql to get the final complete sql text
		String compiledSqlText = processingDynamicTemplates( sqlText, sqlText, parameterObject );
		
		// Check for exception content
		TemplateHandler.checkException( compiledSqlText, namespace );
		
		// Construction the SQL parameter
		Map<String, Object> bindings = context.getBindings();
		SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder( configuration );
		Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
		SqlSource sqlSource = sqlSourceParser.parse( compiledSqlText, parameterType, bindings );

		// Get the final BoundSql object
		BoundSql boundSql = sqlSource.getBoundSql( parameterObject );
		for ( Map.Entry<String, Object> entry : bindings.entrySet() ) {
			boundSql.setAdditionalParameter( entry.getKey(), entry.getValue() );
		}
		
		// If optimistic locking is enabled, generate the next version value
		// under the appropriate conditions.
		if ( isSupportOtimisticLocking( parameterType ) ) {
			generatedNextVersionValue( boundSql, parameterObject, metaObject ); // 1.3.7 - fix bug: "column cannot be null"
		}
		return boundSql;
	}
	
//	Remote at 1.3.7 - fix bug: "column cannot be null"
//	/**
//	 * Get the metadata object of the input parameter object
//	 * 
//	 * @param parameterObject the input parameter object
//	 * @return the metadata object ot the input parameter object
//	 */
//	private MetaObject getMetaObject( Object parameterObject ) {
//		if ( metaObject == null ) {
//			this.metaObject = configuration.newMetaObject( parameterObject );
//		}
//		return metaObject;
//	}
	
	/**
	 * Check if optimistic locking is supported
	 * 
	 * @param parameterType the class type of parameter object
	 * @return the detection result, {@code true} is support for optimistic locking, {@code false} does not support.
	 */
	private boolean isSupportOtimisticLocking( Class<?> parameterType ) {
		return (
			StatementUtil.isUpdate( commandType ) && // Must be a modification operation
			entity.hasOptimisticLock() && // Optimistic lock @Version annotation must be enabled
			StringUtil.startsWith( methodName, modifyMethodPrefixs ) // Only support specific modified sql statements
		);
	}
	
	/**
	 * Selective reconstruction parameter
	 * 
	 * @param parameterObject the input parameter object
	 */
	private void refactoringParameter( Object parameterObject, MetaObject metaObject ) {
		if ( parameterObject == null ) return;
		if ( parameterObject instanceof Map ) {
			Map<String, Object> params = ( Map<String, Object> ) parameterObject;
			for ( String replacementKey : arrayParameterKeys ) {
				if ( params.containsKey( replacementKey ) ) {
					refactoringArrayParameter( params, replacementKey, ( Object [] ) params.get( replacementKey ) );
				}
			}
		} else if ( Objects.equals( parameterObject.getClass(), modelBeanType ) ) {
			generatedValue( parameterObject, metaObject );
		}
	}
	
	/**
	 * Refactor array parameters to avoid unsafe values
	 * 
	 * @param params the original Map parameter
	 * @param replacementKey the target replacement key
	 * @param arrays the object arrays
	 */
	private void refactoringArrayParameter( Map<String, Object> params, String replacementKey, Object [] arrays ) {
		if ( arrays != null ) {
			String param = null;
			StringAppender appender = new StringAppender();
			for ( int i = 0, len = arrays.length; i < len; i ++ ) {
				param = "VALUE_" + i;
				appender.addDelimiter( Constants.SEPARATOR );
				appender.append( "#{" + param + "}" ); // #{VALUE_0}, #{VALUE_1}, ...
				params.put( param, arrays[ i ] ); // VALUE_0, VALUE_1, ...
			}
			params.put( replacementKey, appender.toString() );
		}
	}
	
	/**
	 * Generate field constant values
	 * 
	 * @param parameterObject the input parameter object
	 * @since 1.0.0(A), 1.3.7(U)
	 */
	private void generatedValue( Object parameterObject, MetaObject metaObject ) {
		parameterObject = getEntityObject( parameterObject );
		if ( parameterObject == null ) {
			return;
		}
		for ( Property property : entity.getProperties() ) {
			GeneratedValueInfo generatedValueInfo = property.getGeneratedValueInfo();
			if ( generatedValueInfo != null && generatedValueInfo.isEffective( commandType ) ) {
				String propertyName = property.getName();
				if ( metaObject.hasGetter( propertyName ) ) {
					GeneratedValueProvider valueProvider = SingletonUtil.getSingleton( generatedValueInfo.getType() );
					Object generatedValue = valueProvider.generatedValue( property );
					metaObject.setValue( propertyName, generatedValue );
				}
			}
		}
	}
	
	/**
	 * Generate the next optimistic lock version value
	 * 
	 * @param boundSql the BoundSql object
	 * @param parameterObject the input parameter object
	 */
	private void generatedNextVersionValue( BoundSql boundSql, Object parameterObject, MetaObject metaObject ) {
		
		// Parameter name detection, 
		// version value will only be generated if the correct parameter name is included.
		VersionInfo versionInfo = entity.getVersionInfo();
		String parameterName = versionInfo.getParameterName();
		if ( !hasBindingsParameter( boundSql, parameterName ) ) {
			return;
		}
		
		// If optimistic locking is enabled, you must ensure that the optimistic
		// lock field value cannot be null.
		ErrorContext.instance().sql( boundSql.getSql() )
				.activity( "set the optimistic lock value" ).object( namespace )
				.message( "The optimistic lock version value cannot be null" );
		
		// Fixed in 1.2.0+
		// The specified entity object could not be found when there are multiple parameters
		parameterObject = getEntityObject( parameterObject );
		if ( parameterObject == null ) {
			return;
		}
		
		// Dynamically modify parameter values with mybatis reflection objects
		String proerptyName = versionInfo.getPropertyName();
		// MetaObject metaObject = getMetaObject( parameterObject ); // 
		if ( metaObject.hasGetter( proerptyName ) ) {
			Object currentVersion = metaObject.getValue( proerptyName );
			Assert.notNull( currentVersion, "The optimistic lock #{0}# cannot be null", proerptyName );
			NextVersionProvider versionProvider = SingletonUtil.getSingleton( versionInfo.getVersionProviderType() );
			Object nextVersion = versionProvider.nextVersion( versionInfo.getProperty(), currentVersion );
			boundSql.setAdditionalParameter( parameterName, nextVersion );
		}
	}
	
	/**
	 * Check if there are specific binding parameters in {@code BoundSql}
	 * 
	 * @param boundSql the BoundSql object
	 * @param parameterName the parameter name
	 * @return the detection result, {@code true} contains the specified parameters, {@code false} does not.
	 */
	private boolean hasBindingsParameter( BoundSql boundSql, String parameterName ) {
		for ( ParameterMapping parameterMapping : boundSql.getParameterMappings() ) {
			if ( Objects.equals( parameterMapping.getProperty(), parameterName ) ) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get the final use object to solve the problem of multi-parameter scenes
	 * 
	 * @param parameterObject the input parameter object
	 * @return the final entity object
	 * @since 1.2.0
	 */
	private Object getEntityObject( Object parameterObject ) {
		if ( parameterObject != null && parameterObject instanceof Map ) {
			for ( Object value : ( ( Map<String, Object> ) parameterObject ).values() ) {
				if ( value != null ) {
					Class<?> objectType = value.getClass();
					if ( Objects.equals( modelBeanType, objectType ) || ClassUtil.isAssignable( objectType, modelBeanType, false ) ) {
						return value;
					}
				}
			}
			return null;
		}
		return parameterObject;
	}
	
	/**
	 * Handling dynamic template syntax
	 * 
	 * @param original the original sql text
	 * @param compiled the compiled sql text
	 * @param parameterObject input parameter object
	 * @return the compiled sql text
	 */
	private String processingDynamicTemplates( String original, String compiled, Object parameterObject ) {
		if ( isFromXmlBuilder ) {
			compiled = TemplateHandler.processTextComments( compiled );
			if ( !isNeedDynamicProcessing ) {
				compiled = TemplateHandler.processKeyWordsTemplate( configuration, compiled );
			}
		}
		if ( isNeedDynamicProcessing ) {
			StopWatch monitor = StopWatch.createStarted();
			Object templateParameter = parameterObject;
			Object logPrintParameter = parameterObject;
			if ( parameterObject != null && parameterObject instanceof Example ) {
				Example<?> example = ( Example<?> ) parameterObject;
				templateParameter = MapUtil.newMap( "example", Condition.wrap( example ) ); // {{$.example.xxx}}
				logPrintParameter = example.getParameters();
			}
			compiled = TemplateHandler.processDynamicTemplate( configuration, commandType, compiled, modelBeanType, templateParameter ); // %{...}, For all
			compiled = TemplateHandler.processParameterTemplate( configuration, commandType, compiled, modelBeanType, templateParameter ); // {{...}}, For all
			compiled = TemplateHandler.processKeyWordsTemplate( configuration, compiled ); // [...], For all
			LoggerUtil.printRuntimeLog( namespace, original, compiled, logPrintParameter, monitor );
		}
		return compiled;
	}
	
}

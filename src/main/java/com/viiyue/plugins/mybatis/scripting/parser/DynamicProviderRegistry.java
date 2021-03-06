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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.LanguageDriverRegistry;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.StaticTextSqlNode;
import org.apache.ibatis.session.Configuration;

import com.viiyue.plugins.mybatis.Constants;
import com.viiyue.plugins.mybatis.annotation.mark.Reference;
import com.viiyue.plugins.mybatis.exceptions.DynamicProviderNotSupportedException;
import com.viiyue.plugins.mybatis.mapper.BaseMapper;
import com.viiyue.plugins.mybatis.mapper.Mapper;
import com.viiyue.plugins.mybatis.mapper.Marker;
import com.viiyue.plugins.mybatis.provider.DynamicProvider;
import com.viiyue.plugins.mybatis.scripting.MyBatisMapperSqlSource;
import com.viiyue.plugins.mybatis.template.TemplateHandler;
import com.viiyue.plugins.mybatis.utils.Assert;
import com.viiyue.plugins.mybatis.utils.ClassUtil;
import com.viiyue.plugins.mybatis.utils.LoggerUtil;
import com.viiyue.plugins.mybatis.utils.MethodUtil;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * <p>
 * For the ProviderSource generated by the annotation of the Mapper interface,
 * you can also implement your own DynamicProvider. You only need your Provider
 * class to inherit from DynamicProvider, and the plugin will automatically load
 * and process it.
 *
 * <pre>
 * &#47;&#47; Your own provider
 * public MyDynamicProvider extends DynamicProvider {
 *    
 *    &#47;&#47; The method name must be the same as the method name in the interface
 *    public String selectByMyCustom( MappedStatement ms ) {
 *        return "[select] &#64;{this.columns.excludes("id,createTime")} [from] &#64;{this.table} %{this.where($)}";
 *    }
 *    
 * }
 * 
 * &#47;&#47; Your mapper interface
 * public MyMapper extends Marker&lt;User, UserDTO, Long&gt; {
 * 
 *    &#64;ResultMapGenerator &#47;&#47; Automatically generate result maps, you can configure it yourself
 *    &#64;SelectProvider( type = MyDynamicProvider.class, method = DynamicProvider.dynamicSQL )
 *    List&lt;UserDTO&gt; selectByMyCustom( User query );
 * 
 * }</pre>
 *
 * @author tangxbai
 * @since 1.1.0
 */
final class DynamicProviderRegistry {

	private final StopWatch monitor = new StopWatch();
	private final Set<Class<?>> skiped = new HashSet<Class<?>>();
	private final Map<Class<?>, List<Class<?>>> allInterfaceMappings = new HashMap<Class<?>, List<Class<?>>>();
	private final Map<Class<DynamicProvider>, DynamicProvider> providers = new HashMap<Class<DynamicProvider>, DynamicProvider>();
	private final Map<Class<?>, Map<String, DynamicProvider>> interfaceProviderMappings = new HashMap<Class<?>, Map<String, DynamicProvider>>();
	
	private static final Map<String, SqlSource> sourceMappings = new HashMap<String, SqlSource>();
	
	/**
	 * Resource recycling, releasing stored temporary data.
	 */
	public void recycle() {
		skiped.clear();
		providers.clear();
		allInterfaceMappings.clear();
		interfaceProviderMappings.clear();
		sourceMappings.clear();
	}
	
	/**
	 * Scans all methods in the mapper interface, including all inherited parent
	 * interfaces, but the same interface will only scan once, and the next will
	 * not scan the same interface.
	 * 
	 * @param mapperInterface the mapper interface type
	 */
	public void scanning( Class<?> mapperInterface ) {
		// Time monitoring
		monitor.reset();
		monitor.start();
		if ( LoggerUtil.isEnableMapperScanLog() ) {
			LoggerUtil.log.debug( "Scan <" + mapperInterface.getSimpleName() + "> mapper interface ..." );
			LoggerUtil.log.debug( LoggerUtil.dividingLine );
		}
		// Get all the parent interfaces of the mapper interface
		List<Class<?>> allInterfaces = allInterfaceMappings.get( mapperInterface );
		if ( allInterfaces == null ) {
			allInterfaces = ClassUtil.getAllInterfaces( mapperInterface, Marker.class, Mapper.class, BaseMapper.class );
			allInterfaceMappings.put( mapperInterface, allInterfaces );
		}
		// Scan all parent interface methods separately
		int prefixLength = LoggerUtil.isEnableMapperScanLog() ? getInterfaceNameMaxLength( allInterfaces ) : 0;
		for ( Class<?> interfaceType : allInterfaces ) {
			scanInterfaceProviders( interfaceType, prefixLength );
		}
		if ( LoggerUtil.isEnableMapperScanLog() ) {
			LoggerUtil.log.debug( LoggerUtil.dividingLine );
			LoggerUtil.log.debug( "# Time : " + LoggerUtil.getWatchTime( monitor ) );
			LoggerUtil.log.debug( LoggerUtil.dividingLine );
			LoggerUtil.log.debug( Constants.EMPTY );
		}
	}
	
	/**
	 * Get the specified provider by mapper interface and method name
	 * 
	 * @param mapperInterface the mapper interface type
	 * @param methodName the last part of the mybatis statement id
	 * @return the dynamic provider instance
	 */
	public DynamicProvider getProvider( Class<?> mapperInterface, String methodName ) {
		List<Class<?>> allInterfaces = allInterfaceMappings.get( mapperInterface );
		if ( allInterfaces != null ) {
			for ( Class<?> interfaceType : allInterfaces ) {
				Map<String, DynamicProvider> providers = interfaceProviderMappings.get( interfaceType );
				if ( providers != null ) {
					DynamicProvider dynamicProvider = providers.get( methodName );
					if ( dynamicProvider != null ) {
						return dynamicProvider;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Get the replacement Sql source through the Provider of the Mapper
	 * 
	 * @param ms mapper sql statement
	 * @param mapperInterface mapper interface type
	 * @param modelBeanType mapper generic type of model bean
	 * @param namespace mapper statement namespace id
	 * @param methodName mapper annotation method
	 * @return the replaced sql source
	 */
	public SqlSource getSqlSource( MappedStatement ms, Class<?> mapperInterface, Class<?> modelBeanType, String namespace, String methodName ) {
		
		// If the sql source of the current namespace is cached, 
		// return the result directly.
		SqlSource sqlSource = sourceMappings.get( namespace );
		if ( sqlSource != null ) {
			return sqlSource;
		}
		
		// Find the provider by interface and method name, if the provider is null, do nothing.
		DynamicProvider provider = getProvider( mapperInterface, methodName );
		if ( provider == null ) {
			return null;
		}
		
		// Initialize error chain information
		ErrorContext.instance().activity( "get the sql template script" ).object( "method \"" + methodName + "\"" );

		// Find the specified method object from the method map
		BridgeMethod method = provider.getMethod( methodName );
		
		// Get method return type for generating different sql sources
		Class<?> returnType = method.getProviderMethodReturnType();
		
		// Invoke the provider method to get the return value
		// Example : Mapper.select() -> Provider.select()
		Object returnValue = method.getReturnValue( provider, ms, modelBeanType );
		
		// When the return value is void, 
		// it means to directly operate MappedStatement.
		if ( void.class.equals( returnType ) ) {
			return null;
		}
		
		// In other cases, there must be a return value.
		// The return value can be a String or SqlNode or an String array
		Assert.notNull( returnValue, "Provider method #{0}# cannot return null", methodName );
		
		// Create a new sql source from the dynamic method provider
		SqlSource newSqlSource = null;
		Configuration configuration = ms.getConfiguration();
		boolean isReturnStringArray = String[].class.equals( returnType );
		
		// SQL dynamic template or static sql text
		if ( String.class.equals( returnType ) || isReturnStringArray ) {
			String content = method.getWrappedResult( 
				isReturnStringArray 
				? StringUtil.join( ( String [] ) returnValue, " " ) 
				: returnValue.toString() 
			);
			if ( content.startsWith( "<script>" ) && content.endsWith( "</script>" ) ) {
				// In the low version, the method name word is misspelled. 
				// In order to be compatible with different versions, use other methods to get it.
				// ---------------------------------------------------------------------------------------
				// configuration.getDefaultScriptingLanuageInstance();  // Word misspelling, before mybatis 3.4.2
				// configuration.getDefaultScriptingLanguageInstance(); // Correct version, since mybatis 3.4.2+
				
				// Compatible with each version
				LanguageDriverRegistry languageRegistry = configuration.getLanguageRegistry();
				LanguageDriver languageDriver = languageRegistry.getDefaultDriver();
				
				// Create a sql source using the default language driver
				newSqlSource = languageDriver.createSqlSource( configuration, content, null ); // SQL script
			} else {
				monitor.reset();
				monitor.start();
				String compiled = content;
				SqlCommandType commandType = ms.getSqlCommandType();
				
				// Processing template syntax
				compiled = TemplateHandler.processStaticTemplate( configuration, commandType, content, modelBeanType );
				compiled = TemplateHandler.processKeyWordsTemplate( configuration, compiled );
				
				// Check whether the dynamic template syntax is included in
				// the sql script in advance to avoid real-time parsing in the sql source.
				boolean isNeedDynamicProcessing = TemplateHandler.isNeedDynamicProcessing( compiled );
				
				// Wrapped into sql node object
				StaticTextSqlNode sqlNode = new StaticTextSqlNode( compiled );
				newSqlSource = new MyBatisMapperSqlSource( sqlNode, configuration, commandType, modelBeanType,
						namespace, isNeedDynamicProcessing, false );
				
				// Print the compilation log, if no log is enabled, no
				// information will be printed.
				LoggerUtil.printCompilationLog( namespace, "provider", content, compiled,
						mapperInterface.getSimpleName() + "( " + provider.getClass().getSimpleName() + " )", monitor );
			}
		}
		
		// If the return value is SqlNode,
		// use MyBatis inner creator to process.
		else if ( SqlNode.class.equals( returnType ) ) {
			newSqlSource = new DynamicSqlSource( configuration, ( SqlNode ) returnValue );
		}
		
		// Only return values of String and SqlNode are supported
		else throw new DynamicProviderNotSupportedException(
			"The provider method return value only supports String, String array and SqlNode, but now returns <{0}>", 
			returnType.getName() 
		);
		
		// Cache compiled sql source for the next direct access
		if ( newSqlSource != null ) {
			sourceMappings.put( namespace, newSqlSource );
		}
		return newSqlSource;
	}
	
	/**
	 * Scan all methods in the interface and initialize the provider
	 * 
	 * @param interfaceType the mapper interface type
	 * @param prefixLength mapper name uniform length
	 */
	private void scanInterfaceProviders( Class<?> interfaceType, int prefixLength ) {
		if ( skiped.contains( interfaceType ) ) {
			printLog( interfaceType, prefixLength, "None" );
			return;
		}
		if ( interfaceProviderMappings.containsKey( interfaceType ) ) {
			printLog( interfaceType, prefixLength, "Cached, no need to scan again ..." );
			return;
		}
		Map<String, DynamicProvider> methodProviders = null;
		for ( final Method method : interfaceType.getDeclaredMethods() ) {
			Class<?> providerType = getSqlProviderAnnotationType( method );
			if ( providerType != null && ClassUtil.isAssignable( providerType, DynamicProvider.class, false ) ) {
				if ( methodProviders == null ) {
					methodProviders = new HashMap<String, DynamicProvider>();
				}
				Reference reference = method.getAnnotation( Reference.class );
				DynamicProvider provider = instanceProvider( providerType, reference, method );
				methodProviders.put( method.getName(), provider );
			}
		}
		if ( methodProviders != null ) {
			interfaceProviderMappings.put( interfaceType, methodProviders );
			printLog( interfaceType, prefixLength, String.valueOf( methodProviders.keySet() ) );
		} else {
			skiped.add( interfaceType );
			printLog( interfaceType, prefixLength, "None" );
		}
	}
	
	/**
	 * Create the provider instance
	 * 
	 * @param type the provider class type
	 * @param reference the method reference annotation
	 * @param interfaceMethod the mapper interface method
	 * @return the provider instance
	 */
	private DynamicProvider instanceProvider( Class<?> type, Reference reference, Method interfaceMethod ) {
		Class<DynamicProvider> providerType = ( Class<DynamicProvider> ) type;
		DynamicProvider provider = providers.get( providerType );
		if ( provider == null ) {
			provider = ClassUtil.<DynamicProvider>newInstance( providerType );
			providers.put( providerType, provider );
		}
		final Method providerMethod = findMethod( providerType, reference == null ? interfaceMethod.getName() : reference.method() );
		if ( providerMethod != null ) {
			provider.putMethod( new BridgeMethod( providerMethod, interfaceMethod, reference ) );
		}
		return provider;
	}
	
	/**
	 * <p>Choose the appropriate annotation to get the provider class type
	 * 
	 * <ul>
	 * <li>{@link SelectProvider @SelectProvider} - select sql provider
	 * <li>{@link UpdateProvider @UpdateProvider} - update sql provider
	 * <li>{@link DeleteProvider @DeleteProvider} - delete sql provider
	 * <li>{@link InsertProvider @InsertProvider} - insert sql provider
	 * </ul>
	 * 
	 * @param method the mapper interface method
	 * @return the sql provider class type
	 */
	private Class<?> getSqlProviderAnnotationType( Method method ) {
		SelectProvider sp = method.getAnnotation( SelectProvider.class );
		if ( sp != null ) {
			return sp.type();
		}
		UpdateProvider up = method.getAnnotation( UpdateProvider.class );
		if ( up != null ) {
			return up.type();
		}
		DeleteProvider dp = method.getAnnotation( DeleteProvider.class );
		if ( dp != null ) {
			return dp.type();
		}
		InsertProvider ip = method.getAnnotation( InsertProvider.class );
		if ( ip != null ) {
			return ip.type();
		}
		return null;
	}
	
	/**
	 * Find the method with the same name in the provider by the method name in the mapper interface
	 * 
	 * @param providerType the dynamic provider type
	 * @param methodName the mapper interface method name
	 * @return the method object of the same name in the provider
	 */
	private Method findMethod( Class<DynamicProvider> providerType, String methodName ) {
		// 1. Try to find a method with only one parameter
		// Example: yourMethod(MappedStatement ms)
		Method providerMethod = MethodUtil.getAccessibleMethod( providerType, methodName, MappedStatement.class );
		
		// 2. Try to find a method with two parameters
		// Example: yourMethod(MappedStatement ms, Class<?> modelBeanType)
		if ( providerMethod == null ) {
			providerMethod = MethodUtil.getAccessibleMethod( providerType, methodName, MappedStatement.class, Class.class );
		}
		return providerMethod;
	}
	
	/**
	 * Calculates the longest name length of all parent class interfaces
	 * inherited by the interface
	 * 
	 * @param interfaces all inherited interfaces
	 * @return the length of the interface name of the longest name
	 */
	private int getInterfaceNameMaxLength( List<Class<?>> interfaces ) {
		int maxLength = 0;
		for ( Class<?> type : interfaces ) {
			maxLength = Math.max( maxLength, type.getSimpleName().length() );
		}
		return maxLength;
	}
	
	/**
	 * Print load log
	 * 
	 * @param interfaceType the interface type
	 * @param prefixLength interface name length
	 * @param message log message
	 */
	private void printLog( Class<?> interfaceType, int prefixLength, String message ) {
		if ( prefixLength > 0 ) {
			LoggerUtil.log.debug( "# " + StringUtil.supplement( interfaceType.getSimpleName(), ' ', prefixLength ) + " -> " + message );
		}
	}

}

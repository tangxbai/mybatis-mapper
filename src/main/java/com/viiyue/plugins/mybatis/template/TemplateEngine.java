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
package com.viiyue.plugins.mybatis.template;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.FelEngineImpl;
import com.greenpineyu.fel.context.FelContext;
import com.viiyue.plugins.mybatis.Constants;
import com.viiyue.plugins.mybatis.template.builder.base.TemplateBuilder;
import com.viiyue.plugins.mybatis.template.function.BlankFunction;
import com.viiyue.plugins.mybatis.template.function.EqualFunction;
import com.viiyue.plugins.mybatis.template.function.NotBlankFunction;
import com.viiyue.plugins.mybatis.template.function.NotNullFunction;
import com.viiyue.plugins.mybatis.template.function.NullFunction;
import com.viiyue.plugins.mybatis.utils.CaseUtil;
import com.viiyue.plugins.mybatis.utils.MapUtil;

/**
 * The SQL script template engine parsing tool class, all the parts about the
 * template used in the plugin are parsed by this tool class.
 *
 * @author tangxbai
 * @since 1.0.0
 * 
 * @see NotBlankFunction
 * @see EqualFunction
 * @see NotBlankFunction
 * @see NotNullFunction
 * @see NullFunction
 * @see Constants#ENV_PROP_NAMES
 */
public final class TemplateEngine {

	/**
	 * Template engine singleton object
	 */
	private static final FelEngine engine;
	
	/**
	 * Template engine context environments
	 */
	private static final Map<Object, Object> environments;
	
//	private static final String paramsNameKey = "params";
//	private static final String translatedTemplateKey = "template";
//	private static final Pattern pramemterPattern = Pattern.compile( "(['\"]([^'\"].*?)['\"])" );
//	private static final Map<String, Expression> expressions = new ConcurrentHashMap<String, Expression>();
	
	private TemplateEngine() {}
	
	// Register the default available environment variables
	static {
		Properties properties = System.getProperties();
		List<String> propNames = Constants.ENV_PROP_NAMES;
		environments = new HashMap<Object, Object>( propNames.size() );
		for ( String propName : propNames ) {
			environments.put( CaseUtil.toCamelCase( propName, '.' ), properties.getProperty( propName ) );
		}
	}
	
	// Registration template helper function
	static {
		engine = new FelEngineImpl( context() );
		engine.addFun( new NullFunction() ); 	// isNull( ? )
		engine.addFun( new NotNullFunction() ); // isNotNull( ? )
		engine.addFun( new BlankFunction() );	// isBlank( ? )
		engine.addFun( new NotBlankFunction() );// isNotBlank( ? ) - default function
		engine.addFun( new EqualFunction() ); 	// equals( ?, ? )
	}
	
	/**
	 * Provide access to some system data
	 * 
	 * <p>Currently supported</p>
	 * <table border="1">
	 *     <tr>
	 *     		<th>Description</th>
	 *     		<th>Example</th>
	 *     		<th align="center">Value type</th>
	 *     		<th>Result style</th>
	 *     </tr>
	 *     <tr>
	 *     		<td>Generate UUID</td>
	 *     		<td>eval("system.uuid")</td>
	 *     		<td align="center">String</td>
	 *     		<td>30803fc4-c22a-11e9-9cb5-2a2ae2dbcce4</td>
	 *     </tr>
	 *     <tr>
	 *     		<td>Get a random number</td>
	 *     		<td>eval("system.random.nextInt(100)")</td>
	 *     		<td align="center">Integer</td>
	 *     		<td>0 - 100</td>
	 *     </tr>
	 *     <tr>
	 *     		<td>Get system time</td>
	 *     		<td>eval("system.now")</td>
	 *     		<td align="center">Date</td>
	 *     		<td>new Date()</td>
	 *     </tr>
	 *     <tr>
	 *     		<td>Get system millisecond time</td>
	 *     		<td>eval("system.systime")</td>
	 *     		<td align="center">Long</td>
	 *     		<td>4564646132487</td>
	 *     </tr>
	 * </table>
	 * 
	 * @return partial system data
	 */
	private static Map<Object, Object> systemContext() {
		Map<Object, Object> systemContext = new HashMap<Object, Object>( 4 );
		systemContext.put( "now", new Date() ); // now -> Date
		systemContext.put( "uuid", UUID.randomUUID().toString() ); // uuid -> 30803fc4-c22a-11e9-9cb5-2a2ae2dbcce4
		systemContext.put( "random", ThreadLocalRandom.current() ); // rundom.nexInt(maxValue)
		systemContext.put( "systime", System.currentTimeMillis() ); // systime -> 4564646132487
		return systemContext;
	}
	
	/**
	 * Generates a template context object that has environment variables and
	 * system help properties, and can automatically box array objects of
	 * similar key-value pairs in the parameters into a Map object and set them
	 * into the context object.
	 * 
	 * <p><b>Note</b>: The key must be a string, the value can be any type or even null.
	 * 
	 * <p>Call sample:
	 * <pre>
	 * 1. No parameters
	 * context() -&gt; { 
	 *     "env" : {}, 
	 *     "system" : {} 
	 * }
	 * 
	 * 2. Two parameters
	 * context("name" : "mybatis-mapper") -&gt; { 
	 *     "env" : {}, 
	 *     "system" : {}, 
	 *     "name" : "mybatis-mapper"
	 * }
	 * 
	 * 3. Infinite parameter
	 * content("key", Object, "name" : "mybatis-mapper", "description", "A powerful myabtis generic mapper plugin" ) -&gt; { 
	 *     "env" : {}, 
	 *     "system" : {}, 
	 *     "key" : Object,
	 *     "name" : "mybatis-mapper",
	 *     "description" : "A powerful myabtis generic mapper plugin"
	 * }</pre>
	 * 
	 * @param properties an array similar to a Map key-value pair
	 * @return full template context object
	 */
	public static FelContext context( Object ... properties ) {
		return context( MapUtil.newMap( properties ) );
	}
	
	/**
	 * Generates a template context object that has environment variables and
	 * system help properties, and set the Map parameter to the context.
	 * 
	 * @param metadata your own metadata
	 * @return full template context object
	 */
	public static FelContext context( Map<Object, Object> metadata ) {
		MapContext context = new MapContext();
		context.set( "env", environments );
		context.set( "system", systemContext() );
		context.putItems( metadata );
		return context;
	}
	
	/**
	 * Parse the target expression using the template engine
	 * 
	 * @param expression template expression
	 * @param properties an array similar to a Map key-value pair
	 * @return the value solved by the template engine
	 * @see #context(Object...)
	 * @see #eval(String, FelContext)
	 */
	public static Object eval( String expression, Object ... properties ) {
		return eval( expression, context( properties ) );
	}
	
	/**
	 * Parse the target expression using the template engine, and you must
	 * explicitly pass in the context object of the template engine
	 * 
	 * @param expression template expression
	 * @param properties an array similar to a Map key-value pair
	 * @return the value solved by the template engine
	 * @see #context(Map)
	 * @see #eval(String, FelContext)
	 */
	public static Object eval( String expression, Map<Object, Object> metadata ) {
		return eval( expression, context( metadata ) );
	}
	
	/**
	 * Parse the target expression using the template engine, and you must
	 * explicitly pass in the context object of the template engine
	 * 
	 * @param expression template expression
	 * @param properties an array similar to a Map key-value pair
	 * @return the value solved by the template engine
	 */
	public static Object eval( String expression, FelContext context ) {
		return getResolvedValue( context == null ? engine.eval( expression ) : engine.eval( expression, context ) );
	}
	
//	public static Object resolve( String content, Object ... properties ) {
//		return resolve( content, context( properties ) );
//	}
	
//	public static Object resolve( String content, Map<Object, Object> context ) {
//		return resolve( content, context( context ) );
//	}
	
//	public static Object resolve( String content, FelContext context ) {
//		Map<String, String> paramBindings = getParamBindings( paramsNameKey, content );
//		String template = paramBindings.remove( translatedTemplateKey );
//		FelContext finalContext = prepareContext( content, context, paramBindings );
//		Expression expression = getExpression( content, template, finalContext );
//		return getResolvedValue( expression == null ? engine.eval( content, context ) : expression.eval( context ) );
//	}

	/**
	 * Get the resolved value, if the return is the internal Builder object,
	 * then call the builder's getContent method to get the real content.
	 * 
	 * @param resolved resolved object value, it can be any object or a template Builder object.
	 * @return the resolved value, usually most of the time is String.
	 */
	private static Object getResolvedValue( Object resolved ) {
		if ( resolved == null ) return Constants.EMPTY;
		if ( resolved instanceof TemplateBuilder ) {
			return ( ( TemplateBuilder ) resolved ).build();
		}
		return resolved;
	}
	
//	/**
//	 * Prepare the context object and put the template parameter name into the
//	 * context object for the template engine to handle properly
//	 * 
//	 * @param content template content
//	 * @param context template context object
//	 * @param paramBindings parameter binding mapping
//	 * @return the final template context object
//	 */
//	private static FelContext prepareContext( String content, FelContext context, Map<String, String> paramBindings ) {
//		if ( context == null ) {
//			context = context( paramsNameKey, paramBindings );
//		} else if ( context.get( paramsNameKey ) == null ) {
//			context.set( paramsNameKey, paramBindings );
//		}
//		return context;	
//	}
	
//	/**
//	 * In fact, through testing, I found that the compilation will consume a lot
//	 * of time, and the efficiency of the post-compilation execution is almost
//	 * the same as the efficiency of the direct eval execution, so I temporarily
//	 * gave up the compilation.
//	 * 
//	 * @param content template content
//	 * @param template translated template string content
//	 * @param context template context object
//	 * @return compiled expression object
//	 */
//	private static Expression getExpression( String content, String template, FelContext context ) {
//		Expression expression = expressions.get( template );
//		if ( expression == null ) {
//			synchronized ( expressions ) {
//				expression = engine.compile( template, context );
//				Assert.notNull( expression, "Template compilation failed, the template is '{}', original input is '{}'", template, content );
//				expressions.put( template, expression );
//			}
//		}
//		return expression;
//	}
	
//	/**
//	 * The input text is parsed to get the fixed template content so that we can
//	 * cache the same kind of expression object
//	 * 
//	 * @param parameterName defined parameter name
//	 * @param input original input content
//	 * @return parameter template and parameter mapping object
//	 */
//	private static Map<String, String> getParamBindings( String parameterName, String input ) {
//		String paramName = null;
//		int index = 0; // String clipping start subscript
//		int paramIndex = 0; // Parameter number suffix
//		int initialCapacity = input.split( "\\)" ).length; // Just guessing
//		input = input.replaceAll( "\\s+", Constants.EMPTY ); // Remove extra white space
//		String newInput = input;
//		StringBuffer replacement = new StringBuffer();
//		Map<String, String> params = new HashMap<String, String>( initialCapacity + 1 );
//		Matcher matcher = pramemterPattern.matcher( input );
//		while ( matcher.find() ) {
//			paramName = "param" + ( paramIndex ++ );
//			params.put( paramName, matcher.group( 2 ) );
//			replacement.append( input.substring( index, matcher.start() ) );
//			replacement.append( parameterName ).append( "." ).append( paramName );
//			index = matcher.end();
//		}
//		if ( index > 0 && index <= input.length() - 1 ) {
//			replacement.append( input.substring( index ) ); // Last part
//			newInput = replacement.toString();
//		}
//		params.put( translatedTemplateKey, newInput );
//		return params;
//	}
	
}

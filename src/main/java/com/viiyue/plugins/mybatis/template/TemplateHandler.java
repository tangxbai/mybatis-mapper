package com.viiyue.plugins.mybatis.template;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.Configuration;

import com.viiyue.plugins.mybatis.Constants;
import com.viiyue.plugins.mybatis.enums.Setting;
import com.viiyue.plugins.mybatis.template.builder.ColumnBuilder;
import com.viiyue.plugins.mybatis.template.handler.DynamicTemplateHandler;
import com.viiyue.plugins.mybatis.template.handler.ExceptionHandler;
import com.viiyue.plugins.mybatis.template.handler.ExpressionHandler;
import com.viiyue.plugins.mybatis.template.handler.KeywordsHandler;
import com.viiyue.plugins.mybatis.template.handler.MybatisParameterHandler;
import com.viiyue.plugins.mybatis.template.handler.ParameterHandler;
import com.viiyue.plugins.mybatis.template.handler.StaticTemplateHandler;
import com.viiyue.plugins.mybatis.template.handler.base.AbstractHandler;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * Template syntax handler for parsing all template syntax that appears in text
 *
 * @author tangxbai
 * @since 1.1.0
 */
public final class TemplateHandler {
	
	/**
	 * Template exception handler
	 * 
	 * <p>
	 * <code>&lt;error&gt;message&lt;/error&gt;</code>
	 * </p>
	 */
	private static final ExceptionHandler exceptionHandler = new ExceptionHandler(); // <error>message</error>
	
	/**
	 * Expression template handler
	 * 
	 * <p>
	 * <code>&#64;{expression}</code>
	 * </p>
	 */
	private static final ExpressionHandler expressionHandler = new ExpressionHandler(); // @{expression}
	
	/**
	 * Expression template handler
	 * 
	 * <p>
	 * <code>[keywords]</code>
	 * </p>
	 */
	private static final KeywordsHandler keywordsHandler = new KeywordsHandler(); // [expression]
	
	/**
	 * Parameter template handler
	 * 
	 * <p>
	 * <code>{{parameter expression}}</code>
	 * </p>
	 * 
	 * <p>Internal value expression: </p>
	 * <ul>
	 * <li>{{env.xxx}} See {@link Constants#ENV_PROP_NAMES}</li>
	 * <li>{{system.xxx}} See {@link TemplateEngine#systemContext()}</li>
	 * </ul>
	 */
	private static final ParameterHandler parameterTemplateHandler = new ParameterHandler(); // {{expression}}
	
	/**
	 * Dynamic expression template handler
	 * 
	 * <p>
	 * <code>%{expression}</code>
	 * </p>
	 */
	private static final DynamicTemplateHandler dynamicTemplateHandler = new DynamicTemplateHandler(); // %{expression}
	
	/**
	 * Static expression template handler
	 * 
	 * <p>
	 * <code>&#64;{expression}</code>
	 * </p>
	 */
	private static final StaticTemplateHandler<Object> staticTemplateHandler = new StaticTemplateHandler<Object>(); // @{expression}
	
	/**
	 * Mybatis parameter expression template handler
	 * 
	 * <p>
	 * <code>#{propertyName, javaType=String, jdbcType=VARCHAR}</code>
	 * </p>
	 */
	private static final MybatisParameterHandler<Void> mybatisParameterHandler = new MybatisParameterHandler<Void>(); // #{property, jdbcType=?, javaType=?}
	
	/**
	 * All template handlers
	 */
	private static final AbstractHandler<?>[] allHandlers = { 
		exceptionHandler, 
		keywordsHandler, 
		expressionHandler, 
		parameterTemplateHandler, 
		dynamicTemplateHandler, 
		mybatisParameterHandler
	};
	
	/**
	 * Check if the template text contains exception information
	 * 
	 * <p>
	 * <code>&lt;error&gt;message&lt;/error&gt;</code>
	 * </p>
	 * 
	 * @param content template text content
	 * @param namespace statement node id
	 * @return 
	 */
	public static void checkException( String content, String namespace ) {
		exceptionHandler.parse( null, null, null, content, namespace );
	}
	
	/**
	 * Obtain the mapping of each part by parsing the mybatis parameter text
	 * 
	 * <p>
	 * <code>#{propertyName, javaType=String, jdbcType=VARCHAR}</code>
	 * </p>
	 * 
	 * @param content template content text
	 * @return mybatis parameter mappings
	 */
	public static List<Map<String, String>> processMybatisParameters( String content ) {
		return mybatisParameterHandler.parse( content );
	}
	
	/**
	 * Used to process style expression templates
	 * 
	 * <p>
	 * <code>&#64;{expression}</code>
	 * </p>
	 * 
	 * @param content template text content
	 * @param column column builder
	 * @return processed text
	 */
	public static String processExpressionTemplate( String content, ColumnBuilder column ) {
		return expressionHandler.parse( null, null, null, content, column );
	}
	
	/**
	 * Translate keyword conversions in text content. If you are using uppercase
	 * keywords, {@code [select]} will be translated into {@code [SELECT]}.
	 * 
	 * <p>
	 * <code>[keywords]</code>
	 * </p>
	 * 
	 * @param configuration mybatis core configuration object
	 * @param content template text content
	 * @return processed text
	 */
	public static String processKeyWordsTemplate( Configuration configuration, String content ) {
		return keywordsHandler.parse( configuration, null, null, content, Setting.KeywordsToUppercase.isEnable() );
	}
	
	/**
	 * Handling static template content in text
	 * 
	 * <p>
	 * <code>&#64;{expression}</code>
	 * </p>
	 * 
	 * @param configuration mybatis core configuration object
	 * @param statemntType statement command type
	 * @param content template text content
	 * @param modelBeanType database model bean type
	 * @return processed text
	 */
	public static String processStaticTemplate( Configuration configuration, SqlCommandType statemntType,
			String content, Class<?> modelBeanType ) {
		return staticTemplateHandler.parse( configuration, statemntType, modelBeanType, content, null );
	}
	
	/**
	 * The dynamic template content in the text is processed, and the dynamic
	 * translation is performed according to the parameters passed during the
	 * execution of the SQL.
	 * 
	 * <p>
	 * <code>%{expression}</code>
	 * </p>
	 * 
	 * @param configuration mybatis core configuration object
	 * @param statemntType statement command type
	 * @param content template text content
	 * @param modelBeanType database model bean type
	 * @param parameter dynamic parameter object
	 * @return processed text
	 */
	public static String processDynamicTemplate( Configuration configuration, SqlCommandType statemntType,
			String content, Class<?> modelBeanType, Object parameter ) {
		return dynamicTemplateHandler.parse( configuration, statemntType, modelBeanType, content, parameter );
	}
	
	/**
	 * Handling parameter value expressions in text
	 * 
	 * <p>
	 * <code>{{parameter expression}}</code>
	 * </p>
	 * 
	 * @param configuration mybatis core configuration object
	 * @param statemntType statement command type
	 * @param content template text content
	 * @param modelBeanType database model bean type
	 * @param parameter dynamic parameter object
	 * @return processed text
	 */
	public static String processParameterTemplate( Configuration configuration, SqlCommandType statemntType,
			String content, Class<?> modelBeanType, Object parameter ) {
		return parameterTemplateHandler.parse( configuration, statemntType, modelBeanType, content, parameter );
	}
	
	/**
	 * Remove all comments in the text, support multi-line text comments.
	 * 
	 * @param content template text content 
	 * @return clean text
	 */
	public static String processTextComments( String content ) {
		// Replace all comments with blanks
		content = Constants.TEXT_COMMENTS_PATTERN.matcher( content ).replaceAll( "$2" ); // Instead of replaceAll()
		// Remove the excess blanks
		content = StringUtil.normalizeSpace( content ); // Instead of replaceAll()
		return content;
	}
	
	/**
	 * Whether the dynamic template syntax is also included in the car template
	 * text
	 * 
	 * <p>
	 * <code>%{expression}</code>
	 * </p>
	 * 
	 * @param content template text content
	 * @return {@code true} means that the dynamic template syntax is included, {@code false} means no.
	 */
	public static boolean isNeedDynamicProcessing( String content ) {
		for ( AbstractHandler<?> handler : allHandlers ) {
			if ( handler.isDynamicHandler() && handler.canHandle( content ) ) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Detect if any template syntax that any handler can handle
	 * 
	 * @param content template text content
	 * @return {@code true} means it can be processed, {@code false} can't.
	 */
	public static boolean isTemplateContent( String content ) {
		for ( AbstractHandler<?> handler : allHandlers ) {
			if ( handler.canHandle( content ) ) {
				return true;
			}
		}
		return false;
	}
	
}

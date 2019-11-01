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
package com.viiyue.plugins.mybatis;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.scripting.xmltags.XMLScriptBuilder;
import org.apache.ibatis.session.Configuration;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.viiyue.plugins.mybatis.enums.Setting;
import com.viiyue.plugins.mybatis.exceptions.VersionConflictException;
import com.viiyue.plugins.mybatis.mapper.Marker;
import com.viiyue.plugins.mybatis.scripting.MyBatisMapperBuilder;
import com.viiyue.plugins.mybatis.scripting.MyBatisMapperSqlSource;
import com.viiyue.plugins.mybatis.template.TemplateHandler;
import com.viiyue.plugins.mybatis.utils.ClassUtil;
import com.viiyue.plugins.mybatis.utils.FieldUtil;
import com.viiyue.plugins.mybatis.utils.GenericTypeUtil;
import com.viiyue.plugins.mybatis.utils.LoggerUtil;
import com.viiyue.plugins.mybatis.utils.MethodUtil;
import com.viiyue.plugins.mybatis.utils.ObjectUtil;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * <p>
 * The default mybatis-mapper will only refactor the provider annotation mapper.
 * If you want to work in the xml block to achieve the same functionality, then
 * you must change the default language driver for mybatis to this class.
 * 
 * The current language driver not only supports the use of template syntax, but
 * also supports the various syntax of native xml, meaning that the two can be
 * mixed development.
 * 
 * <hr>
 * <p>In a pure mybatis environment, you can configure it like this:
 * <pre>
 * # mybatis.xml #
 * &lt;configuration&gt;
 * 
 *     &lt;!-- <b>Mybatis-mapper preference configuration</b> --&gt;
 *     &lt;properties resource=&quot;jdbc.properties&quot;&gt;
 *         &lt;property name=&quot;enableLogger&quot; value=&quot;true&quot;/&gt;
 *         &lt;property name=&quot;enableRuntimeLog&quot; value=&quot;true&quot;/&gt;
 *         &lt;property name=&quot;enableCompilationLog&quot; value=&quot;false&quot;/&gt;
 *         &lt;property name=&quot;enableKeywordsToUppercase&quot; value=&quot;true&quot;/&gt;
 *         &lt;property name=&quot;databaseColumnStyle&quot; value=&quot;#&quot;/&gt;
 *     &lt;/properties&gt;
 *     
 *     &lt;settings&gt;
 *         &lt;setting name=&quot;defaultScriptingLanguage&quot; value=&quot;com.viiyue.plugins.mybatis.MyBatisMapperLanguageDriver&quot;/&gt;
 *     &lt;/settings&gt;
 *     
 *     &lt;!-- It is generally recommended to configure the bean alias --&gt;
 *     &lt;!-- so that it is easier to write and the plugin works better. --&gt;
 *     &lt;typeAliases&gt;
 *         &lt;package name=&quot;your.model.classpath&quot;/&gt;
 *         &lt;package name=&quot;or.more&quot;/&gt;
 *     &lt;/typeAliases&gt;
 * &lt;/configuration&gt;</pre>
 * 
 * @author tangxbai
 * @since 1.1.0
 * @since mybatis 3.2.4
 * @see org.apache.ibatis.scripting.xmltags.XMLLanguageDriver
 */
public final class MyBatisMapperLanguageDriver extends XMLLanguageDriver {
	
	private final StopWatch monitor = new StopWatch();

	// Provide XMLScriptBuilder#parseDynamicTags method by reflection
	private static class Holder {
		private static final Method parseDynamicTags = MethodUtil.findMethod( 
			XMLScriptBuilder.class, true, "parseDynamicTags", XNode.class 
		);
	}
	
	@Override
	public SqlSource createSqlSource( Configuration configuration, XNode script, Class<?> parameterType ) {
		LoggerUtil.printBootstrapLog();
		// Read the global configuration, 
		// it will only take effect on the first call.
		Setting.copyPropertiesFromConfiguration( configuration );
		
		// Build XML script parsing object
		XMLScriptBuilder builder = new XMLScriptBuilder( configuration, script, parameterType ); // since mybase 3.2.4+
		XNode parent = script.getParent();
		if ( parent == null ) {
			return builder.parseScriptNode();
		}
		
		// If it is a normal mapper/statement node, 
		// continue to judge whether it conforms to the template syntax.
		String classpath = parent.getStringAttribute( "namespace" );
		if ( StringUtil.isBlank( classpath ) ) {
			return builder.parseScriptNode();
		}
		
		// The target mapper interface does not intervene if it does not
		// inherit the Marker interface.
		Class<?> mapperInterface = ClassUtil.forName( classpath );
		if ( !ClassUtil.isAssignable( mapperInterface, Marker.class, false ) ) {
			return builder.parseScriptNode();
		}
		
		// Record template syntax parsing time consumption start time
		this.monitor.reset();
		this.monitor.start();
		Node content = script.getNode();
		
		// Get the basic information of the statement from the node, 
		// only used as template syntax analysis.
		String methodName = script.getStringAttribute( "id" ); // Id is the method name
		String namespace = classpath + "." + methodName; // Namespace is the classpath
		Class<?> modelBeanType = GenericTypeUtil.getInterfaceGenericType( mapperInterface, 0 );
		Class<?> returnBeanType = GenericTypeUtil.getInterfaceGenericType( mapperInterface, 1 );
		SqlCommandType commandType = SqlCommandType.valueOf( script.getName().toUpperCase( Locale.ENGLISH ) );
		
		// The first time to register the Mapper interface, 
		// if there is no xml file corresponding to the interface, 
		// it will not be executed here, but if there is an xml file,
		// it will be executed first.
		MyBatisMapperBuilder.registry.registerMapper( configuration, mapperInterface, modelBeanType, returnBeanType );
		
		// Template syntax parsing of all text nodes of a statement
		StringBuffer originals = new StringBuffer();
		StringBuffer templates = new StringBuffer();
		nodeTemplateParaser( content, configuration, commandType, modelBeanType, originals, templates );
		
		// Print script template compilation log
		String template = templates.toString();
		if ( LoggerUtil.isEnableCompilationLog() ) {
			String resource = FieldUtil.<String>readValue( ErrorContext.instance(), "resource" );
			LoggerUtil.printCompilationLog( namespace, "<" + script.getName() + ">", originals.toString(), template, resource, monitor );
		}
		
		// Handling xml syntax nodes with mybatis' XMLScriptBuilder object
		SqlNode parsedSqlNode = getParasedSqlNode( builder, script, content );
		
		// Check whether the dynamic template syntax is included in
		// the sql script in advance to avoid real-time parsing in the sql source.
		boolean isNeedDynamicProcessing = TemplateHandler.isNeedDynamicProcessing( template );
		
		// After the parsing is completed, the sql source dedicated
		// to mybatis-mapper is generated. 
		// When the actual mapper method is actually called, 
		// some dynamic template syntax in some sql scripts will be parsed.
		return new MyBatisMapperSqlSource( parsedSqlNode, configuration, 
				commandType, modelBeanType, namespace, isNeedDynamicProcessing, true );
	}

	private void nodeTemplateParaser( 
		Node node,
		Configuration configuration, 
		SqlCommandType commandType, 
		Class<?> modelBeanType,
		StringBuffer originals, 
		StringBuffer templates ) {
		NodeList children = node.getChildNodes();
		for ( int i = 0, size = children.getLength(); i < size; i ++ ) {
			Node item = children.item( i );
			Short nodeType = item.getNodeType();
			if ( ObjectUtil.equalsAny( nodeType, Node.CDATA_SECTION_NODE, Node.TEXT_NODE ) ) {
				String compiled = null;
				String original = item.getTextContent();
				if ( StringUtil.isNotBlank( original ) ) {
					original = TemplateHandler.processTextComments( original );
					compiled = TemplateHandler.processStaticTemplate( configuration, commandType, original, modelBeanType );
					compiled = TemplateHandler.processKeyWordsTemplate( configuration, compiled );
					originals.append( original ).append( " " );
					templates.append( compiled ).append( " " );
				} else {
					compiled = Constants.EMPTY; // Space, \n, \t, \s
				}
				item.setNodeValue( compiled );
			} else if ( nodeType == Node.ELEMENT_NODE ) {
				String nodeName = "<#" + item.getNodeName() + " ";
				originals.append( nodeName );
				templates.append( nodeName );
				nodeTemplateParaser( item, configuration, commandType, modelBeanType, originals, templates );
				templates.append( ">" );
				originals.append( ">" );
			}
		}
	}
	
	private SqlNode getParasedSqlNode( XMLScriptBuilder builder, XNode script, Node content ) {
		Object contents = MethodUtil.invoke( builder, Holder.parseDynamicTags, script.newXNode( content ) );
		// since mybatis 3.4.6+
		if ( contents instanceof SqlNode ) {
			return ( SqlNode ) contents;
		}
		// since mybatis( 3.2.4 ~ 3.4.6 )
		if ( contents instanceof List ) {
			return new MixedSqlNode( ( List<SqlNode> ) contents );
		}
		throw new VersionConflictException( "Get the xml script parsing exception, it may be a version conflict" );
	}
	
}

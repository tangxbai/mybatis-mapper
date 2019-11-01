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
package com.viiyue.plugins.mybatis.enums;

import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import org.apache.ibatis.session.Configuration;

import com.viiyue.plugins.mybatis.template.TemplateHandler;
import com.viiyue.plugins.mybatis.utils.ObjectUtil;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * Plug-in common settings, configured with mybatis variable mode
 * 
 * @author tangxbai
 * @since 1.1.0
 */
public enum Setting {

	/**
	 * Whether to enable the print log mode, {@code false} does not print any
	 * logs.
	 */
	Logger( "enableLogger", "true" ),

	/**
	 * Whether to enable printing SQL running log, this configuration depends on
	 * whether {@code enableLogger} is enabled.
	 */
	RuntimeLog( "enableRuntimeLog", "true" ),

	/**
	 * Whether to enable printing SQL compilation log, this configuration
	 * depends on whether {@code enableLogger} is enabled.
	 */
	CompileLog( "enableCompilationLog", "true" ),

	/**
	 * Database keyword style, <code>&#35;</code> represents the keyword will be
	 * replaced with a specific keyword in real time, you can wrap it with other
	 * characters, similar to: `&#35;` -&gt; `id`.
	 * 
	 * <pre>
	 *  &#35;  -&gt; column
	 * `&#35;` -&gt; `column`
	 * [&#35;] -&gt; [column]
	 * left(&#35;)right -&gt; left(column)right
	 * </pre>
	 */
	ColumnStyle( "databaseColumnStyle", "#" ),

	/**
	 * Whether to enable keyword conversion to uppercase configuration, if this
	 * configuration is enabled, {@code [keywords]} will be converted to {@code [KEYWORDS]}.
	 */
	KeywordsToUppercase( "enableKeywordsToUppercase", "false" );

	private String varName;
	private String customValue;
	private String defaultValue;
	private static boolean isCopyed = false;

	private Setting( String varName, String defaultValue ) {
		this.varName = varName;
		this.defaultValue = defaultValue;
	}

	private void setCustomValue( String customValue ) {
		// Character escaping
		// The character text is automatically skipped when the template engine parses
		if ( this == ColumnStyle && TemplateHandler.isTemplateContent( customValue ) ) {
			customValue = "\\" + customValue;
		}
		this.customValue = customValue;
	}

	public String getName() {
		return varName;
	}

	public String getValue() {
		return ObjectUtil.defaultIfNull( customValue, defaultValue );
	}

	public String getStyleValue( String target ) {
		return target == null ? null : StringUtil.replace( getValue(), "#", target );
	}

	public int getIntValue() {
		return Integer.parseInt( getValue() );
	}

	public boolean isEnable() {
		return Boolean.parseBoolean( getValue() );
	}

	/**
	 * Copy the configuration information in mybatis to the current class and
	 * cache it so that it can be accessed directly later.
	 * 
	 * @param configuration mybatis core configuration
	 */
	public static final void copyPropertiesFromConfiguration( Configuration configuration ) {
		if ( isCopyed == false && configuration != null ) {
			Properties variables = configuration.getVariables();
			for ( Map.Entry<Object, Object> entry : variables.entrySet() ) {
				for ( Setting setting : values() ) {
					if ( Objects.equals( setting.getName(), entry.getKey() ) ) {
						Object defaultValue = entry.getValue();
						if ( defaultValue != null ) {
							setting.setCustomValue( defaultValue.toString() );
						}
					}
				}
			}
			isCopyed = true;
		}
	}

}

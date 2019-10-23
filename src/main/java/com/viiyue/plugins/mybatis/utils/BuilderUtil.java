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
package com.viiyue.plugins.mybatis.utils;

import com.viiyue.plugins.mybatis.Constants;
import com.viiyue.plugins.mybatis.enums.Setting;

/**
 * Some simple tool methods that builders need to use
 *
 * @author tangxbai
 * @since 1.1.0
 */
public class BuilderUtil {
	
	private BuilderUtil() {}
	
	public static final String getAlias( String name, String alias ) {
		return name + ( StringUtil.isNotEmpty( alias ) ? " [as] '" + alias + "'" : Constants.EMPTY );
	}
	
	public static String quote( String message ) {
		return StringUtil.isEmpty( message ) ? message : StringUtil.replace( message, "#", "\"" );
	}

	public static String getPrefix( String prefix ) {
		return prefix == null || prefix.endsWith( "." ) ? prefix : prefix.concat( "." );
	}
	
	public static String getWrappedPrefix( String prefix ) {
		return prefix == null ? null : getPrefix( Setting.ColumnStyle.getStyleValue( prefix ) );
	}
	
	public static String getRootVarName( String content ) {
		return content.contains( "." ) ? content.substring( 0, content.indexOf( "." ) ) : content;
	}
	
	public static String getClasspath( String namespace ) {
		return namespace.substring( 0, namespace.lastIndexOf( "." ) );
	}

	public static String getMethodName( String namespace ) {
		return namespace.substring( namespace.lastIndexOf( "." ) + 1 );
	}

	public static Class<?> getMapperInterfaceType( String namespace ) {
		return ClassUtil.forName( getClasspath( namespace ) );
	}
	
}

package com.viiyue.plugins.mybatis.utils;

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
		return name + ( StringUtil.isNotEmpty( alias ) ? " [as] '" + alias + "'" : "" );
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

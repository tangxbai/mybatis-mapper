/*-
 * ApacheLICENSE-2.0
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

import java.text.MessageFormat;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Some tool functions about {@code String}
 *
 * @author tangxbai
 * @since 1.1.0
 */
public class StringUtil extends StringUtils {
	
	private StringUtil () {}
	
	public static String[] toArray( String ... elements ) {
		return elements;
	}
	
	public static String placeholder( String placehoder, int length ) {
		return placeholder( placehoder, null, length );
	}
	
	public static String format( String text, Object ... formats ) {
		return ObjectUtil.isEmpty( formats ) ? text : MessageFormat.format( text, formats );
	}
	
	public static boolean startsWith( String text, String ... strs ) {
		if ( isEmpty( text ) || ArrayUtils.isEmpty( strs ) ) {
			return false;
		}
		for ( final String target : strs ) {
            if ( text.startsWith( target ) ) {
                return true;
            }
        }
		return false;
	}
	
	public static String placeholder( String placehoder, String delimiter, int length ) {
		int dlength = delimiter == null ? 0 : delimiter.length();
		int capacity = ( placehoder.length() + dlength ) * length - dlength;
		StringAppender appender = new StringAppender( capacity );
		for ( int i = 0; i < length; i ++ ) {
			appender.addDelimiter( delimiter );
			appender.append( placehoder );
		}
		return appender.toString();
	}
	
	public static String supplement( String text, char placeholder, int length ) {
		if ( text.length() < length ) {
			StringBuilder buffer = new StringBuilder( text );
			for ( int i = 0, current = text.length(); i < length - current; i ++ ) {
				buffer.append( placeholder );
			}
			return buffer.toString();
		}
		return text;
	}
	
	public static String toString( Object instance, String template, Object ... args ) {
		String className = instance.getClass().getSimpleName();
		String hashCode = Integer.toHexString( instance.hashCode() );
		String toString = className + "@" + hashCode;
		if ( template != null ) {
			toString = toString + "( " + String.format( template, args )  + " )";
		}
		return toString;
	}

}

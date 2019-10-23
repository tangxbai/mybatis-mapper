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

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Class name util, that you can use this class to get a class alias name, and
 * restore the Java property name to the database column name.
 *
 * @author tangxbai
 * @since 1.0.0
 */
public class CaseUtil {
	
	private CaseUtil() {}
	public static final char DEFAULT_DELIMITER = '_';

	public static String toUnderscore( String text ) {
		return toUnderscore( text, DEFAULT_DELIMITER );
	}
	
	public static String toCamelCase( String text ) {
		return toCamelCase( text, DEFAULT_DELIMITER );
	}

	public static String toUnderscore( String text, Character delimiter ) {
		if ( StringUtil.isBlank( text ) ) return EMPTY;
		if ( Character.isWhitespace( delimiter ) ) return text;
		final int len = text.length();
		final StringBuilder out = new StringBuilder( len );
		for ( int i = 0; i < len; i ++ ) {
			char c = text.charAt( i );
			if ( Character.isUpperCase( c ) ) {
				out.append( i != 0 ? delimiter : EMPTY ).append( Character.toLowerCase( c ) );
			} else {
				out.append( c );
			}
		}
		return out.toString();
	}

	public static String toCamelCase( String text, Character delimiter ) {
		if ( StringUtil.isBlank( text ) ) return EMPTY;
		if ( Character.isWhitespace( delimiter ) ) return text;
		final int len = text.length();
		final StringBuilder out = new StringBuilder( len );
		for ( int i = 0; i < len; i ++ ) {
			char c = text.charAt( i );
			if ( c == delimiter ) {
				if ( ++ i < len ) {
					out.append( Character.toUpperCase( text.charAt( i ) ) );
				}
			} else out.append( c );
		}
		return out.toString();
	}
	
}

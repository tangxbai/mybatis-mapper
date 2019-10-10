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

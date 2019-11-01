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
	
	/**
     * <p>Returns the first value in the array which is not empty (""),
     * {@code null} or whitespace only.</p>
     *
     * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
     *
     * <p>If all values are blank or the array is {@code null}
     * or empty then {@code null} is returned.</p>
     *
     * <pre>
     * StringUtils.firstNonBlank(null, null, null)     = null
     * StringUtils.firstNonBlank(null, "", " ")        = null
     * StringUtils.firstNonBlank("abc")                = "abc"
     * StringUtils.firstNonBlank(null, "xyz")          = "xyz"
     * StringUtils.firstNonBlank(null, "", " ", "xyz") = "xyz"
     * StringUtils.firstNonBlank(null, "xyz", "abc")   = "xyz"
     * StringUtils.firstNonBlank()                     = null
     * </pre>
     *
     * @param <T> the specific kind of CharSequence
     * @param values  the values to test, may be {@code null} or empty
     * @return the first value from {@code values} which is not blank, or {@code null} if there are no non-blank values
     * @since common-lang 3.9
     * @since mybatis-mapper 1.2.0
     */
	@SafeVarargs
	public static <T extends CharSequence> T firstNonBlank( final T ... values ) {
		if ( values != null ) {
			for ( final T val : values ) {
				if ( isNotBlank( val ) ) {
					return val;
				}
			}
		}
		return null;
	}

    /**
     * <p>Returns the first value in the array which is not empty.</p>
     *
     * <p>If all values are empty or the array is {@code null}
     * or empty then {@code null} is returned.</p>
     *
     * <pre>
     * StringUtils.firstNonEmpty(null, null, null)   = null
     * StringUtils.firstNonEmpty(null, null, "")     = null
     * StringUtils.firstNonEmpty(null, "", " ")      = " "
     * StringUtils.firstNonEmpty("abc")              = "abc"
     * StringUtils.firstNonEmpty(null, "xyz")        = "xyz"
     * StringUtils.firstNonEmpty("", "xyz")          = "xyz"
     * StringUtils.firstNonEmpty(null, "xyz", "abc") = "xyz"
     * StringUtils.firstNonEmpty()                   = null
     * </pre>
     *
     * @param <T> the specific kind of CharSequence
     * @param values  the values to test, may be {@code null} or empty
     * @return the first value from {@code values} which is not empty, or {@code null} if there are no non-empty values
     * @since common-lang 3.9
     * @since mybatis-mapper 1.2.0
     */
	@SafeVarargs
	public static <T extends CharSequence> T firstNonEmpty( final T ... values ) {
		if ( values != null ) {
			for ( final T val : values ) {
				if ( isNotEmpty( val ) ) {
					return val;
				}
			}
		}
		return null;
	}

}

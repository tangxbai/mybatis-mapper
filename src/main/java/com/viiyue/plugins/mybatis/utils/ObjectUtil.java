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

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;

/**
 * Operations on {@code Object}
 *
 * @author tangxbai
 * @since 1.1.0
 */
public class ObjectUtil extends ObjectUtils {
	
	private ObjectUtil () {}
	
	/**
	 * Determine whether an object is an empty object, if it is a string, then
	 * determine whether it is blank text.
	 * 
	 * @param object the target input object
	 * @return {@code true} if the input object is null, or the input object is an blank text.
	 * @see StringUtil#isBlank(CharSequence)
	 */
	public static boolean isBlank( Object object ) {
		return object == null || StringUtil.isBlank( object.toString() );
	}
	
	/**
	 * Determine whether an object is a non-empty object, 
	 * if it is a string, determine whether it is non-blank text.
	 * 
	 * @param object the target input object
	 * @return {@code true} if the input object is non-null, or the input object is a non-blank text.
	 * @see StringUtil#isNotBlank(CharSequence)
	 */
	public static boolean isNotBlank( Object object ) {
		return object != null && StringUtil.isNotBlank( object.toString() );
	}

	/**
	 * Determine if two objects are different
	 * 
	 * @param target the input object
	 * @param another the annother input object
	 * @return {@code true} if both objects are the same, {@code false} otherwise.
	 * @see java.util.Objects#equals(Object, Object)
	 */
	public static boolean isDifferent( Object target, Object another ) {
		return !Objects.equals( target, another );
	}

	/**
	 * <p>Compares given <code>target</code> to a Objects vararg of {@code elements}, or
	 * returning {@code true} if the <code>target</code> is equal to any of the <code>elements</code>.</p>
	 * 
     * <pre>
     * ObjectUtil.equalsAny(null, (Object[]) null) = false
     * ObjectUtil.equalsAny(null, null, null)    = true
     * ObjectUtil.equalsAny(null, "abc", "def")  = false
     * ObjectUtil.equalsAny("abc", null, "def")  = false
     * ObjectUtil.equalsAny("abc", "abc", "def") = true
     * ObjectUtil.equalsAny("abc", "ABC", "DEF") = false
     * </pre>
     * 
	 * @param target to compare, may be {@code null}.
	 * @param elements a vararg of objects, may be {@code null}.
	 * @return {@code true} if the target is equal to any other element of <code>elements</code>;
     * {@code false} if <code>elements</code> is null or contains no matches.
	 */
	public static boolean equalsAny( Object target, Object ... elements ) {
		if ( isNotEmpty( elements ) ) {
			for ( Object element : elements ) {
				if ( Objects.equals( target, element ) ) {
					return true;
				}
			}
		}
		return false;
	}

}

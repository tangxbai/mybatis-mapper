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

/**
 * Some auxiliary method extensions for {@code StringBuilder}
 *
 * @author tangxbai
 * @since 1.1.0
 */
public class StringAppender {

	/**
	 * Starting with 1.2.0, changed from {@link StringBuffer} to {@link StringBuilder}.
	 */
	private final StringBuilder builder;

	public StringAppender() {
		this( new StringBuilder() );
	}
	
	public StringAppender( int capacity ) {
		this( new StringBuilder( capacity ) );
	}

	public StringAppender( StringBuilder buffer ) {
		this.builder = buffer;
	}
	
	public boolean isEmpty() {
		return builder.length() == 0;
	}

	public boolean hasContent() {
		return builder.length() > 0;
	}
	
	public boolean startsWith( String content ) {
		if ( content != null ) {
			return builder.indexOf( content ) == 0;
		}
		return false;
	}
	
	// fixed in 1.2.0
	public boolean endsWith( String content ) {
		if ( content != null ) {
			return builder.lastIndexOf( content ) == builder.length() - content.length();
		}
		return false;
	}

	public StringAppender addDelimiter( String delimiter ) {
		if ( delimiter != null && hasContent() ) {
			this.builder.append( delimiter );
		}
		return this;
	}
	
	public final <T> StringAppender insert( int offset, T content ) {
		if ( content != null ) {
			this.builder.insert( offset, content );
		}
		return this;
	}

	public <T> StringAppender prepend( T content ) {
		if ( content != null ) {
			this.builder.insert( 0, content );
		}
		return this;
	}
	
	public StringAppender append( String content ) {
		if ( content != null ) {
			this.builder.append( content );
		}
		return this;
	}

	public StringAppender append( Object content ) {
		if ( content != null ) {
			this.builder.append( content );
		}
		return this;
	}
	
	public <T> StringAppender append( StringAppender appender ) {
		if ( appender != null && appender.hasContent() ) {
			this.builder.append( appender.getAppender() );
		}
		return this;
	}

	public StringAppender delete( int start, int end ) {
		this.builder.delete( start, end );
		return this;
	}

	public StringAppender reset() {
		this.builder.setLength( 0 );
		return this;
	}

	public StringBuilder getAppender() {
		return builder;
	}

	@Override
	public String toString() {
		return builder.toString();
	}

}

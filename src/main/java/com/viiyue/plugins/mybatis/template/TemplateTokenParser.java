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
package com.viiyue.plugins.mybatis.template;

import org.apache.ibatis.parsing.TokenHandler;

import com.viiyue.plugins.mybatis.utils.Assert;

/**
 * Since mybatis default parser needs to instantiate an object each time, 
 * it will eventually lead to more and more objects being created. 
 * In fact, we can extract the {@code Handler} and pass it in from the method, 
 * so that every time it is used. Create a parser object, you can refer to Spring's
 * {@code PropertyPlaceholderHelper}
 * 
 * @author tangxbai
 * @since 1.0.0
 * @see org.apache.ibatis.parsing.TokenHandler
 * @see org.apache.ibatis.parsing.GenericTokenParser
 * @see org.springframework.util.PropertyPlaceholderHelper
 */
public class TemplateTokenParser {

	private final String openToken;
	private final String closeToken;

	public TemplateTokenParser( String openToken, String closeToken ) {
		this.openToken = openToken;
		this.closeToken = closeToken;
	}

	public String parse( String text, TokenHandler handler ) {
		Assert.notNull( handler, "Token handler cannot be null" );
		final StringBuilder builder = new StringBuilder();
		final StringBuilder expression = new StringBuilder();
		if ( text != null && text.length() > 0 ) {
			char [] src = text.toCharArray();
			int offset = 0;
			// search open token
			int start = text.indexOf( openToken, offset );
			while ( start > -1 ) {
				if ( start > 0 && src[ start - 1 ] == '\\' ) {
					// this open token is escaped. remove the backslash and continue.
					builder.append( src, offset, start - offset - 1 ).append( openToken );
					offset = start + openToken.length();
				} else {
					// found open token. let's search close token.
					expression.setLength( 0 );
					builder.append( src, offset, start - offset );
					offset = start + openToken.length();
					int end = text.indexOf( closeToken, offset );
					while ( end > -1 ) {
						if ( end > offset && src[ end - 1 ] == '\\' ) {
							// this close token is escaped. remove the backslash and continue.
							expression.append( src, offset, end - offset - 1 ).append( closeToken );
							offset = end + closeToken.length();
							end = text.indexOf( closeToken, offset );
						} else {
							expression.append( src, offset, end - offset );
							offset = end + closeToken.length();
							break;
						}
					}
					if ( end == -1 ) {
						// close token was not found.
						builder.append( src, start, src.length - start );
						offset = src.length;
					} else {
						builder.append( handler.handleToken( expression.toString() ) );
						offset = end + closeToken.length();
					}
				}
				start = text.indexOf( openToken, offset );
			}
			if ( offset < src.length ) {
				builder.append( src, offset, src.length - offset );
			}
		}
		return builder.toString();
	}

}

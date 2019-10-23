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
package com.viiyue.plugins.mybatis.template.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.builder.ParameterExpression;
import org.apache.ibatis.parsing.TokenHandler;

import com.viiyue.plugins.mybatis.template.handler.base.AbstractHandler;

/**
 * Mybatis parameter handler
 * 
 * <p>
 * <code>#{propertyName, javaType=String, jdbcType=VARCHAR}</code>
 *
 * @author tangxbai
 * @since 1.1.0
 * @see org.apache.ibatis.builder.ParameterExpression.ParameterExpression
 */
public final class MybatisParameterHandler<T> extends AbstractHandler<T> {

	public MybatisParameterHandler() {
		super( "#{", "}", false );
	}

	public final List<Map<String, String>> parse( String content ) {
		MybatisParameterTokenHandler handler = new MybatisParameterTokenHandler();
		getParser().parse( content, handler );
		return handler.getParsingResult();
	}

	private class MybatisParameterTokenHandler implements TokenHandler {

		private final List<Map<String, String>> items = new ArrayList<Map<String, String>>();

		@Override
		public String handleToken( String content ) {
			try {
				this.items.add( new ParameterExpression( content ) );
			} catch ( BuilderException ex ) {
				throw ex;
			} catch ( Exception ex ) {
				throw new BuilderException( "Parsing error was found in mapping #{" + content + "}. "
						+ "Check syntax #{property|(expression), var1=value1, var2=value2, ...} ", ex );
			}
			return content;
		}

		public List<Map<String, String>> getParsingResult() {
			return items;
		}

	}

}

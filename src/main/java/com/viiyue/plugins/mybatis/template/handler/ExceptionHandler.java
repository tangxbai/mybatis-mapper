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
package com.viiyue.plugins.mybatis.template.handler;

import org.apache.ibatis.executor.ErrorContext;

import com.viiyue.plugins.mybatis.template.handler.base.AbstractHandler;
import com.viiyue.plugins.mybatis.template.handler.base.TemplateTokenHandler;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * Exception template handler
 * 
 * <p>
 * <code>&lt;error&gt;message&lt;/error&gt;</code>
 *
 * @author tangxbai
 * @since 1.1.0
 */
public final class ExceptionHandler extends AbstractHandler<String> {

	public ExceptionHandler() {
		super( "<error>", "</error>", false );
	}

	@Override
	public String handle( TemplateTokenHandler<String> handler, String fragment, String namespace ) {
		ErrorContext.instance().object( "<" + namespace + ">" ).sql( handler.originalContent() );
		throw new RuntimeException( fragment );
	}

	public static String wrapException( String exception, Object ... args ) {
		return "<error>" + StringUtil.format( exception, args ) + "</error>";
	}

}

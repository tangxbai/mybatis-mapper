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

import com.viiyue.plugins.mybatis.Constants;
import com.viiyue.plugins.mybatis.template.TemplateEngine;
import com.viiyue.plugins.mybatis.template.handler.base.AbstractHandler;
import com.viiyue.plugins.mybatis.template.handler.base.TemplateTokenHandler;

/**
 * Parameter template handler
 * 
 * <p><code>{{syntactic-sugar}}</code>
 *
 * @author tangxbai
 * @since 1.1.0
 */
public final class ParameterHandler extends AbstractHandler<Object> {

	public ParameterHandler() {
		super( "{{", "}}", true );
	}

	@Override
	public String handle( TemplateTokenHandler<Object> handler, String fragment, Object param ) {
		return TemplateEngine.eval( fragment, Constants.ROOT_PARAMETER_NAME, param ).toString();
	}

}

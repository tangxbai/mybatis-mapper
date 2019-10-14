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

import com.viiyue.plugins.mybatis.template.handler.base.AbstractTemplateHandler;

/**
 * Dymaic template handler
 * 
 * <p><code>%{expression}</code>
 *
 * @author tangxbai
 * @since 1.1.0
 */
public final class DynamicTemplateHandler extends AbstractTemplateHandler<Object> {

	public DynamicTemplateHandler() {
		super( "%{", "}", true );
	}

}

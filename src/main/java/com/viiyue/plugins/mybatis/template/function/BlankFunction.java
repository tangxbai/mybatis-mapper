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
package com.viiyue.plugins.mybatis.template.function;

import com.viiyue.plugins.mybatis.template.TemplateEngine;
import com.viiyue.plugins.mybatis.utils.ObjectUtil;

/**
 * <p>
 * Fel-engine extended null/blank judgment function, used to determine whether 
 * the given value is null or whether the text is blank.
 *
 * <ul>
 * <li>isBlank(null) - true
 * <li>isBlank("") - true
 * <li>isBlank("  ") - true
 * <li>isBlank("\t\n\s") - true
 * <li>isBlank(*) - false
 * </ul>
 *
 * @author tangxbai
 * @since 1.0.0
 * @see TemplateEngine
 */
public class BlankFunction extends AbstractFunction {

	public BlankFunction() {
		super( 1 );
	}

	@Override
	public String getName() {
		return "isBlank";
	}

	@Override
	public Object invoke( Object target ) {
		return ObjectUtil.isBlank( target );
	}

}

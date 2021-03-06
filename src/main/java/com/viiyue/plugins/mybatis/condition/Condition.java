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
package com.viiyue.plugins.mybatis.condition;

import com.greenpineyu.fel.FelEngine;
import com.viiyue.plugins.mybatis.template.TemplateEngine;

/**
 * <p>Conditional wrapper object for example
 * <p>This class will be parsed by fel-engine template syntax
 * 
 * <pre>
 * &#47;&#47; <b><code>$</code></b> is the calling parameter of mybatis
 * &#47;&#47; <b><code>example</code></b> is an instance of <code>Condition.wrap(Example)</code>
 * <code>{{$.example.columns}}</code> -&gt; id, name, ...
 * <code>{{$.example.updates}}</code> -&gt; id = #{id}, name = #{name}, ...
 * <code>{{$.example.where}}</code> -&gt; where id = #{id} and name = #{name} and logically_delete = <code>selectValue</code>
 * <code>{{$.example.where(true)}}</code> -&gt; where id = #{id} and name = #{name} and logically_delete = <code>deletedValue</code>
 * <code>{{$.example.where(false)}}</code> -&gt; where id = #{id} and name = #{name}
 * </pre>
 * 
 * @author tangxbai
 * @since 1.1.0
 * @see FelEngine
 * @see TemplateEngine
 */
public final class Condition {

	public final Example<?> example;

	private Condition( Example<?> example ) {
		this.example = example;
	}

	public static Condition wrap( Example<?> example ) {
		return new Condition( example.build() );
	}

	public String columns() {
		return example.getQueryPart();
	}

	public String updates() {
		return example.getModifyPart();
	}

	public String where() {
		return example.getWherePart();
	}
	
	public String where( boolean isLogicallyDelete ) {
		return example.getWherePart( isLogicallyDelete, true );
	}

}

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

import java.util.Objects;

/**
 * <p>
 * Fel-engine extends the equality judgment function to determine whether two
 * given objects are equal
 * 
 * <ul>
 * <li>equals(null, null) - true
 * <li>equals(null, *) - false
 * <li>equals(*, null) - false
 * <li>equals(" ", "\s") - false
 * <li>equals("mybatis-mapper", "mybatis-mapper") - true
 * </ul>
 *
 * @author tangxbai
 * @since 1.0.0
 * @see java.util.Objects#equals(Object, Object)
 */
public class EqualFunction extends AbstractFunction {
	
	public EqualFunction() {
		super( 2 );
	}

	@Override
	public String getName() {
		return "equals";
	}

	@Override
	public Object invoke( Object target, Object another ) {
		return Objects.equals( target, another );
	}

}

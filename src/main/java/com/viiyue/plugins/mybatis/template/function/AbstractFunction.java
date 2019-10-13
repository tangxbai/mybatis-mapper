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
package com.viiyue.plugins.mybatis.template.function;

import com.greenpineyu.fel.function.CommonFunction;
import com.viiyue.plugins.mybatis.utils.Assert;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * Fel-engine extended abstract function
 *
 * @author tangxbai
 * @since 1.0.0
 */
public abstract class AbstractFunction extends CommonFunction {
	
	private final int argumentNumber;
	
	public AbstractFunction( int argumentNumber ) {
		this.argumentNumber = argumentNumber;
	}

	@Override
	public final Object call( Object [] arguments ) {
		if ( argumentNumber > 0 ) {
			Assert.notEmpty( arguments, "Function #{0}(?)# arguments cannot be null", getName() );
			Assert.isTrue( arguments.length == argumentNumber, "The function #{0}({1})# must have {2} target parameters", getName(), StringUtil.placeholder( "?", ", ", arguments.length ), argumentNumber );
		}
		if ( argumentNumber == 0 ) return invoke();
		if ( argumentNumber == 1 ) return invoke( arguments [ 0 ] );
		if ( argumentNumber == 2 ) return invoke( arguments [ 0 ], arguments [ 1 ] );
		return invoke( arguments );
	}
	
	public Object invoke() {
		return null;
	}
	
	public Object invoke( Object target ) {
		return null;
	}
	
	public Object invoke( Object target, Object another ) {
		return null;
	}
	
	public Object invoke( Object [] arguments ) {
		return null;
	}

}

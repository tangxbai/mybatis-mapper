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

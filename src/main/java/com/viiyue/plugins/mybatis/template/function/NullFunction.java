package com.viiyue.plugins.mybatis.template.function;

import com.viiyue.plugins.mybatis.template.TemplateEngine;

/**
 * <p>
 * Fel-engine extended null judgment function, used to determine 
 * whether the given value is null.
 *
 * <ul>
 * <li>isNull(null) - true
 * <li>isNull(*) - true
 * </ul>
 *
 * @author tangxbai
 * @since 1.0.0
 * @see TemplateEngine
 */
public class NullFunction extends AbstractFunction {

	public NullFunction() {
		super( 1 );
	}

	@Override
	public String getName() {
		return "isNull";
	}

	@Override
	public Object invoke( Object target ) {
		return target == null;
	}

}

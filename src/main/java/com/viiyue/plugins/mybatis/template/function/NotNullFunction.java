package com.viiyue.plugins.mybatis.template.function;

import com.viiyue.plugins.mybatis.template.TemplateEngine;

/**
 * <p>
 * Fel-engine extended non-null judgment function, used to determine 
 * whether the given value is non-null.
 *
 * <ul>
 * <li>isNotNull(null) - false
 * <li>isNotNull(*) - true
 * </ul>
 *
 * @author tangxbai
 * @since 1.0.0
 * @see TemplateEngine
 */
public class NotNullFunction extends AbstractFunction {

	public NotNullFunction() {
		super( 1 );
	}

	@Override
	public String getName() {
		return "isNotNull";
	}

	@Override
	public Object invoke( Object target ) {
		return target != null;
	}

}

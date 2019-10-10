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

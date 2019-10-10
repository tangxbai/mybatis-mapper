package com.viiyue.plugins.mybatis.template.function;

import com.viiyue.plugins.mybatis.template.TemplateEngine;
import com.viiyue.plugins.mybatis.utils.ObjectUtil;

/**
 * <p>
 * Fel-engine extended non-null/non-blank judgment function, used to determine whether 
 * the given value is non-null or whether the text is blank.
 *
 * <ul>
 * <li>isNotBlank(null) - false
 * <li>isNotBlank("") - false
 * <li>isNotBlank("  ") - false
 * <li>isNotBlank("\t\n\s") - false
 * <li>isNotBlank(*) - true
 * </ul>
 *
 * @author tangxbai
 * @since 1.0.0
 * @see TemplateEngine
 */
public class NotBlankFunction extends AbstractFunction {

	public NotBlankFunction() {
		super( 1 );
	}

	@Override
	public String getName() {
		return "isNotBlank";
	}

	@Override
	public Object invoke( Object target ) {
		return ObjectUtil.isNotBlank( target );
	}

}

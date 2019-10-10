package com.viiyue.plugins.mybatis.template.handler;

import com.viiyue.plugins.mybatis.Constants;
import com.viiyue.plugins.mybatis.template.TemplateEngine;
import com.viiyue.plugins.mybatis.template.handler.base.AbstractHandler;
import com.viiyue.plugins.mybatis.template.handler.base.TemplateTokenHandler;

/**
 * Parameter template handler
 * 
 * <p><code>{{syntactic-sugar}}</code>
 *
 * @author tangxbai
 * @since 1.1.0
 */
public final class ParameterHandler extends AbstractHandler<Object> {

	public ParameterHandler() {
		super( "{{", "}}", true );
	}

	@Override
	public String handle( TemplateTokenHandler<Object> handler, String fragment, Object param ) {
		return TemplateEngine.eval( fragment, Constants.ROOT_PARAMETER_NAME, param ).toString();
	}

}

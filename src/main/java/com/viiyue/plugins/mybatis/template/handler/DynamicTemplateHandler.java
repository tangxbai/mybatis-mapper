package com.viiyue.plugins.mybatis.template.handler;

import com.viiyue.plugins.mybatis.template.handler.base.AbstractTemplateHandler;

/**
 * Dymaic template handler
 * 
 * <p><code>%{expression}</code>
 *
 * @author tangxbai
 * @since 1.1.0
 */
public final class DynamicTemplateHandler extends AbstractTemplateHandler<Object> {

	public DynamicTemplateHandler() {
		super( "%{", "}", true );
	}

}

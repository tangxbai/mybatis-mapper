package com.viiyue.plugins.mybatis.template.handler;

import com.viiyue.plugins.mybatis.template.handler.base.AbstractTemplateHandler;

/**
 * Static template handler
 * 
 * <p><code>&#64;{this.table}</code>
 *
 * @author tangxbai
 * @since 1.1.0
 */
public class StaticTemplateHandler<T> extends AbstractTemplateHandler<T> {

	public StaticTemplateHandler() {
		super( "@{", "}", false );
	}

}

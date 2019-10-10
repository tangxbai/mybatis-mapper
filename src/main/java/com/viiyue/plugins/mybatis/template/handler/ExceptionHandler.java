package com.viiyue.plugins.mybatis.template.handler;

import org.apache.ibatis.executor.ErrorContext;

import com.viiyue.plugins.mybatis.template.handler.base.AbstractHandler;
import com.viiyue.plugins.mybatis.template.handler.base.TemplateTokenHandler;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * Exception template handler
 * 
 * <p>
 * <code>&lt;error&gt;message&lt;/error&gt;</code>
 *
 * @author tangxbai
 * @since 1.1.0
 */
public final class ExceptionHandler extends AbstractHandler<String> {

	public ExceptionHandler() {
		super( "<error>", "</error>", false );
	}

	@Override
	public String handle( TemplateTokenHandler<String> handler, String fragment, String namespace ) {
		ErrorContext.instance().object( "<" + namespace + ">" ).sql( handler.originalContent() );
		throw new RuntimeException( fragment );
	}

	public static String wrapException( String exception, Object ... args ) {
		return "<error>" + StringUtil.format( exception, args ) + "</error>";
	}

}

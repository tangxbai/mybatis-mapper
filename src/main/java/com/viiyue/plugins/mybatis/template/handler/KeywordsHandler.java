package com.viiyue.plugins.mybatis.template.handler;

import java.util.Locale;

import com.viiyue.plugins.mybatis.template.handler.base.AbstractHandler;
import com.viiyue.plugins.mybatis.template.handler.base.TemplateTokenHandler;

/**
 * Keyword template handler
 * 
 * <p><code>[keywords]</code>
 *
 * @author tangxbai
 * @since 1.1.0
 */
public final class KeywordsHandler extends AbstractHandler<Boolean> {

	public KeywordsHandler() {
		super( "[", "]", true );
	}

	@Override
	public String handle( TemplateTokenHandler<Boolean> handler, String fragment, Boolean isUseUppercaseKeyWords ) {
		return isUseUppercaseKeyWords ? fragment.toUpperCase( Locale.ENGLISH ) : fragment;
	}
	
	public static boolean isKeywordsWrap( String content ) {
		return content == null ? false : content.startsWith( "[" ) && content.endsWith( "]" );
	}
	
}

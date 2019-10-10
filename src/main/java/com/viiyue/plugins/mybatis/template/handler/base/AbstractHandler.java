package com.viiyue.plugins.mybatis.template.handler.base;

import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.Configuration;

import com.viiyue.plugins.mybatis.template.TemplateTokenParser;

/**
 * An abstract template syntax processor that provides some basic judgment and
 * helper methods.
 *
 * @author tangxbai
 * @since 1.1.0
 */
public abstract class AbstractHandler<T> {

	private final String openToken;
	private final String closeToken;
	private final boolean isDynamicHandler;
	private final TemplateTokenParser parser;

	/**
	 * Instantiate the processor and indicate the start package text and end
	 * package text of the template, whether it is a dynamic processor or the
	 * like.
	 * 
	 * @param openToken open token text
	 * @param closeToken close token text
	 * @param isDynamicHandler Is it a dynamic processor?
	 */
	public AbstractHandler( String openToken, String closeToken, boolean isDynamicHandler ) {
		this.openToken = openToken;
		this.closeToken = closeToken;
		this.isDynamicHandler = isDynamicHandler;
		this.parser = new TemplateTokenParser( openToken, closeToken );
	}

	/**
	 * Returns the starting token of the handler
	 * 
	 * @return start token
	 */
	public final String getOpenToken() {
		return openToken;
	}

	/**
	 * Returns the end token of the handler
	 * 
	 * @return end token
	 */
	public final String getCloseToken() {
		return closeToken;
	}

	/**
	 * Wrap a new text with a start token and an end token
	 * 
	 * @param content a new content text
	 * @return wrapped text
	 */
	public final String warp( String content ) {
		return openToken + content + closeToken;
	}

	/**
	 * Is it a dynamic processor?
	 * 
	 * @return {@code true} is a dynamic processor, {@code false} is not.
	 */
	public final boolean isDynamicHandler() {
		return isDynamicHandler;
	}

	/**
	 * Detect if the target text can be processed
	 * 
	 * @param content template text content
	 * @return {@code true}  can handle the text, {@code false} does not work.
	 */
	public final boolean canHandle( String content ) {
		return content.contains( openToken ) && content.contains( closeToken );
	}

	/**
	 * Return to the handler's core parser
	 * 
	 * @return template parser
	 */
	public final TemplateTokenParser getParser() {
		return parser;
	}

	/**
	 * Core processor resolution method
	 * 
	 * @param configuration mybatis core configuration object
	 * @param commandType statement command type
	 * @param modelBeanType database model bean type
	 * @param content template text content
	 * @param additionalParameter additional parameter object
	 * @return processed text
	 */
	public final String parse( Configuration configuration, SqlCommandType commandType, Class<?> modelBeanType,
			String content, T additionalParameter ) {
		if ( canHandle( content ) ) {
			return parser.parse( content, new TemplateTokenHandler<T>( this, configuration, commandType, modelBeanType,
					content, additionalParameter ) );
		}
		return content;
	}

	/**
	 * The logical implementation of specific content processing, providing
	 * specific implementations by subclasses.
	 * 
	 * @param handler template token handler
	 * @param fragment template text content
	 * @param additionalParameter additional parameter object
	 * @return processed text
	 */
	public String handle( TemplateTokenHandler<T> handler, String fragment, T additionalParameter ) {
		return null;
	}

}

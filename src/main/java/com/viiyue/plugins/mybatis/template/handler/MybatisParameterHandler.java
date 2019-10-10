package com.viiyue.plugins.mybatis.template.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.builder.ParameterExpression;
import org.apache.ibatis.parsing.TokenHandler;

import com.viiyue.plugins.mybatis.template.handler.base.AbstractHandler;

/**
 * Mybatis parameter handler
 * 
 * <p>
 * <code>#{propertyName, javaType=String, jdbcType=VARCHAR}</code>
 *
 * @author tangxbai
 * @since 1.1.0
 * @see org.apache.ibatis.builder.ParameterExpression.ParameterExpression
 */
public final class MybatisParameterHandler<T> extends AbstractHandler<T> {

	public MybatisParameterHandler() {
		super( "#{", "}", false );
	}

	public final List<Map<String, String>> parse( String content ) {
		MybatisParameterTokenHandler handler = new MybatisParameterTokenHandler();
		getParser().parse( content, handler );
		return handler.getParsingResult();
	}

	private class MybatisParameterTokenHandler implements TokenHandler {

		private final List<Map<String, String>> items = new ArrayList<Map<String, String>>();

		@Override
		public String handleToken( String content ) {
			try {
				items.add( new ParameterExpression( content ) );
			} catch ( BuilderException ex ) {
				throw ex;
			} catch ( Exception ex ) {
				throw new BuilderException( "Parsing error was found in mapping #{" + content + "}. "
						+ "Check syntax #{property|(expression), var1=value1, var2=value2, ...} ", ex );
			}
			return null;
		}

		public List<Map<String, String>> getParsingResult() {
			return this.items;
		}

	}

}

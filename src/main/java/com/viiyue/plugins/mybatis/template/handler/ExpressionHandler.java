package com.viiyue.plugins.mybatis.template.handler;

import com.viiyue.plugins.mybatis.enums.ExpressionStyle;
import com.viiyue.plugins.mybatis.enums.Template;
import com.viiyue.plugins.mybatis.enums.ValueStyle;
import com.viiyue.plugins.mybatis.template.builder.ColumnBuilder;
import com.viiyue.plugins.mybatis.template.handler.base.TemplateTokenHandler;

/**
 * Style expression handler
 * 
 * @author tangxbai
 * @since 1.1.0
 * @see ExpressionStyle
 * @see Template
 * @see ValueStyle
 */
public final class ExpressionHandler extends StaticTemplateHandler<ColumnBuilder> {
	
	@Override
	public String handle( TemplateTokenHandler<ColumnBuilder> handler, String fragment, ColumnBuilder column ) {
		if ( fragment.equals( "column" ) ) {} 
		else if ( fragment.equals( "property" ) ) column.property();
		else if ( fragment.equals( "javaType" ) ) column.javaType();
		else if ( fragment.equals( "jdbcType" ) ) column.jdbcType();
		return column.build();
	}

}

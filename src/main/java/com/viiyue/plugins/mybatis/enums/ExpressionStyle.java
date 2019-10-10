package com.viiyue.plugins.mybatis.enums;

import org.apache.ibatis.type.TypeHandler;

import com.viiyue.plugins.mybatis.metadata.Property;
import com.viiyue.plugins.mybatis.template.TemplateHandler;
import com.viiyue.plugins.mybatis.template.builder.ColumnBuilder;

/**
 * SQL expression style
 * 
 * @author tangxbai
 * @since 1.0.0
 */
public enum ExpressionStyle {
	
	/** Example : id = #{id} */
	SHORT( "@{column} = ", ValueStyle.SHORT ),
	
	/** Example : id = #{id, jdbcType=INTEGER} */
	DB_TYPE( "@{column} = ", ValueStyle.JDBC_TYPE ),

	/** Example : id = #{id, javaType=Integer} */
	JAVA_TYPE( "@{column} = ", ValueStyle.JAVA_TYPE ),
	
	/** Example : id = #{id, javaType=Integer, jdbcType=INTEGER, typeHandler=Handler.class} */
	FULL( "@{column} = ", ValueStyle.FULL );

	private String pattern;
	private ValueStyle valueStyle;

	private ExpressionStyle( String pattern, ValueStyle valueStyle ) {
		this.pattern = pattern;
		this.valueStyle = valueStyle;
	}

	public String pattern() {
		return pattern( null );
	}
	
	public String pattern( Class<? extends TypeHandler<?>> typeHandler ) {
		return pattern + valueStyle.pattern( typeHandler );
	}

	@Override
	public String toString() {
		return pattern();
	}
	
	public String format( ColumnBuilder column, Property property ) {
		String finalPattern = pattern( property.getColumn().getTypeHandler() );
		return TemplateHandler.processExpressionTemplate( finalPattern, column );
	}
	
}

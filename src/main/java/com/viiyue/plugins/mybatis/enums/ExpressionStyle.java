/*-
 * Apache　LICENSE-2.0
 * #
 * Copyright (C) 2017 - 2019 mybatis-mapper
 * #
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ------------------------------------------------------------------------
 */
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

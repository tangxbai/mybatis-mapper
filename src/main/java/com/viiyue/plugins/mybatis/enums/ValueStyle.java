/*-
 * ApacheLICENSE-2.0
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

import com.viiyue.plugins.mybatis.template.TemplateHandler;
import com.viiyue.plugins.mybatis.template.builder.ColumnBuilder;

/**
 * SQL set value style
 * 
 * @author tangxbai
 * @since 1.0.0
 */
public enum ValueStyle {
	
	/** Example : #{id} */
	SHORT( "#{@{property}}" ),

	/** Example : #{id, jdbcType=INTEGER} */
	JDBC_TYPE( "#{@{property}, jdbcType=@{jdbcType}}" ),

	/** Example : #{id, javaType=Integer} */
	JAVA_TYPE( "#{@{property}, javaType=@{javaType}}" ),

	/** Example : #{id, javaType=Integer, jdbcType=INTEGER, typeHandler=Handler.class} */
	FULL( "#{@{property}, jdbcType=@{jdbcType}, javaType=@{javaType}" );

	private String pattern;

	private ValueStyle( String pattern ) {
		this.pattern = pattern;
	}

	public String pattern() {
		return pattern( null );
	}
	
	public String pattern( Class<? extends TypeHandler<?>> typeHandler ) {
		String newPattern = pattern;
		if ( this == FULL ) {
			if ( typeHandler != null ) {
				newPattern += ", typeHandler=" + typeHandler.getName();
			}
			newPattern += "}";
		}
		return newPattern;
	}
	
	@Override
	public String toString() {
		return pattern();
	}
	
	public String format( ColumnBuilder column ) {
		String finalPattern = pattern( column.getProperty().getColumn().getTypeHandler() );
		return TemplateHandler.processExpressionTemplate( finalPattern, column );
	}
	
}

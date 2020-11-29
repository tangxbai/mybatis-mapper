/**
 * Copyright (C) 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.viiyue.plugins.mybatis.enums;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.type.JdbcType;

import com.viiyue.plugins.mybatis.utils.ClassUtil;

/**
 * Corresponding configuration relationship between JDBC type and Java type
 * 
 * @author tangxbai
 * @since 1.0.0, Updated in 1.3.5
 */
public enum Type {

	STRING( String.class, JdbcType.VARCHAR ), // String -> VARCHAR
	CHAR( Character.class, JdbcType.CHAR ), // Character -> CHAR
	SMALLINT( Short.class, JdbcType.SMALLINT ), // Short -> SMALLINT
	BYTE( Byte.class, JdbcType.TINYINT ), // Byte -> TINYINT
	INTEGER( Integer.class, JdbcType.INTEGER ),
	BOOLEAN( Boolean.class, JdbcType.BIT ), // Boolean -> BIT
	BIGINT( Long.class, JdbcType.BIGINT ), // Long -> BIGINT
	FLOAT( Float.class, JdbcType.FLOAT ), // Float -> Float
	DOUBLE( Double.class, JdbcType.DOUBLE ), // Double -> DOUBLE
	BIGDECIMAL( BigDecimal.class, JdbcType.DECIMAL ), // BigDecimal -> DECIMAL
	TIME( Time.class, JdbcType.TIME ), // Time -> TIME
	DATE_SQL( java.sql.Date.class, JdbcType.DATE ), // java.sql.Date.class -> DATE
	DATE_UTIL( java.util.Date.class, JdbcType.DATE ), // java.util.Date.class -> DATE
	TIMESTAMP( Timestamp.class, JdbcType.TIMESTAMP ), // Timestamp -> TIMESTAMP
	
	CLOB( ClassUtil.substitute( "com.mysql.jdbc.Clob", "com.mysql.cj.jdbc.Clob" ), JdbcType.CLOB ), // Clob -> CLOB
	BLOB( ClassUtil.substitute( "com.mysql.jdbc.Blob", "com.mysql.cj.jdbc.Blob" ), JdbcType.BLOB ), // Blob -> BLOB
	
	ARRAY( Array.class, JdbcType.ARRAY ), // Array -> ARRAY
	STRUCT( Struct.class, JdbcType.STRUCT ), // Struct -> STRUCT
	CURSOR( ResultSet.class, JdbcType.CURSOR ); // ResultSet -> CURSOR
	
	// Type cache, convenient to get the types on later
	private static final Map<Class<?>, JdbcType> typeMappings;
	
	static {
		typeMappings = new HashMap<Class<?>, JdbcType>( 20 );
		for ( Type type : values() ) {
			typeMappings.put( type.javaType, type.jdbcType );
		}
	}
	
	private Class<?> javaType;
	private JdbcType jdbcType;

	private Type( Class<?> javaType, JdbcType jdbcType ) {
		this.javaType = javaType;
		this.jdbcType = jdbcType;
	}

	public Class<?> javaType() {
		return javaType;
	}

	public JdbcType jdbcType() {
		return jdbcType;
	}
	
	/**
	 * Java type is convert to the Jdbc type
	 * 
	 * @param javaType Java class type
	 * @return Jdbc type
	 */
	public static final JdbcType forJdbcType( Class<?> javaType ) {
		if ( javaType.isEnum() ) {
			return JdbcType.CHAR;
		}
		return typeMappings.getOrDefault( javaType, JdbcType.JAVA_OBJECT );
	}
	
}

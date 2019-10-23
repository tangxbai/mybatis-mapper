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

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.type.JdbcType;

import com.mysql.jdbc.Blob;
import com.mysql.jdbc.Clob;

/**
 * Corresponding configuration relationship between JDBC type and Java type
 * 
 * @author tangxbai
 * @since 1.0.0
 */
public enum Type {
	
	// Undefined
	UNDEFINED( void.class, JdbcType.UNDEFINED ),

	// String
	CHAR( String.class, JdbcType.CHAR ),
	VARCHAR( String.class, JdbcType.VARCHAR ),
	NVARCHAR( String.class, JdbcType.NVARCHAR ),
	LONGVARCHAR( String.class, JdbcType.LONGVARCHAR ),

	// Boolean
	BIT( Boolean.class, JdbcType.BIT ),
	BOOLEAN( Boolean.class, JdbcType.BOOLEAN ),
	
	// Byte
	TINYINT( Byte.class, JdbcType.TINYINT ),
	BINARY( Byte[].class, JdbcType.BINARY ),
	VARBINARY( Byte[].class, JdbcType.VARBINARY ),
	LONGVARBINARY( Byte[].class, JdbcType.LONGVARBINARY ),
	
	// Number 
	REAL( Float.class, JdbcType.REAL ),
	FLOAT( Double.class, JdbcType.FLOAT ),
	DOUBLE( Double.class, JdbcType.DOUBLE ),
	BIGINT( Long.class, JdbcType.BIGINT ),
	INTEGER( Integer.class, JdbcType.INTEGER ),
	SMALLINT( Short.class, JdbcType.SMALLINT ),
	DECIMAL( BigDecimal.class, JdbcType.DECIMAL ),
	NUMERIC( BigDecimal.class, JdbcType.NUMERIC ),
	
	// Date
	TIME( Time.class, JdbcType.TIME ),
	DATE_SQL( java.sql.Date.class, JdbcType.TIMESTAMP ),
	DATE_UTIL( java.util.Date.class, JdbcType.TIMESTAMP ),
	TIMESTAMP( Timestamp.class, JdbcType.TIMESTAMP ),
	
	// Big data
	CLOB( Clob.class, JdbcType.CLOB ),
	BLOB( Blob.class, JdbcType.BLOB ),
	ARRAY( Array.class, JdbcType.ARRAY ),
	STRUCT( Struct.class, JdbcType.STRUCT ),
	
	// since mybatis 3.4.0+
	// REF( Ref.class, JdbcType.REF ),
	// DATALINK( URL.class, JdbcType.DATALINK ),
	
	// Result set of cursor
	CURSOR( ResultSet.class, JdbcType.CURSOR );
	
	// Type cache, convenient to get the types on later
	private static final Map<Class<?>, JdbcType> javaTypes;
	private static final Map<JdbcType, Class<?>> jdbcTypes;
	
	static {
		javaTypes = new HashMap<Class<?>, JdbcType>();
		jdbcTypes = new HashMap<JdbcType, Class<?>>();
		for ( Type type : values() ) {
			javaTypes.put( type.javaType, type.jdbcType );
			jdbcTypes.put( type.jdbcType, type.javaType );
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
		if ( String.class.equals( javaType ) || javaType.isEnum() ) return JdbcType.VARCHAR;
		else if ( Boolean.class.equals( javaType ) ) return JdbcType.BIT;
		else if ( Double.class.equals( javaType ) ) return JdbcType.DOUBLE;
		else if ( BigDecimal.class.equals( javaType ) ) return JdbcType.DECIMAL;
		else if ( Byte[].class.equals( javaType ) ) return JdbcType.BINARY;
		else return ( javaTypes.containsKey( javaType ) ? javaTypes.get( javaType ) : JdbcType.UNDEFINED );
	}
	
	/**
	 * Jdbc type convert to the Java type,
	 * If is not exists on the types mapping, return string class as default
	 * 
	 * @param jdbcType Jdbc type
	 * @return Java class type
	 */
	public static final Class<?> forJavaType( JdbcType jdbcType ) {
		Class<?> javaType = jdbcTypes.get( jdbcType );
		return javaType == null ? String.class : javaType;
	}
	
}

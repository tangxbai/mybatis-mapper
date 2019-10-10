package com.viiyue.plugins.mybatis.utils;

import java.util.Objects;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;

/**
 * Mybatis statement tool class
 *
 * @author tangxbai
 * @since 1.1.0
 */
public class StatementUtil {

	private StatementUtil () {}
	
	public static boolean isInsert( MappedStatement ms ) {
		return is( ms, SqlCommandType.INSERT );
	}
	
	public static boolean isInsert( SqlCommandType type ) {
		return Objects.equals( type, SqlCommandType.INSERT );
	}
	
	public static boolean isUpdate( MappedStatement ms ) {
		return is( ms, SqlCommandType.UPDATE );
	}
	
	public static boolean isUpdate( SqlCommandType type ) {
		return Objects.equals( type, SqlCommandType.UPDATE );
	}
	
	public static boolean isSelect( MappedStatement ms ) {
		return is( ms, SqlCommandType.SELECT );
	}
	
	public static boolean isSelect( SqlCommandType type ) {
		return Objects.equals( type, SqlCommandType.SELECT );
	}
	
	public static boolean isDelete( MappedStatement ms ) {
		return is( ms, SqlCommandType.DELETE );
	}
	
	public static boolean isDelete( SqlCommandType type ) {
		return Objects.equals( type, SqlCommandType.DELETE );
	}
	
	private static boolean is( MappedStatement ms, SqlCommandType type ) {
		return ms == null ? false : Objects.equals( ms.getSqlCommandType(), type );
	}
	
}

package com.viiyue.plugins.mybatis.enums;

import com.viiyue.plugins.mybatis.annotation.member.GeneratedKey;

/**
 * Provide an enumeration object for partial query auto-incrementing primary
 * key. You can easily use the enumeration name to find the corresponding
 * statement. If you can't find it here, then you can only provide your own
 * statement text.
 * 
 * <table border="1">
 * <tr><th>Database</th><th>Statement</th></tr>
 * <tr><td>DB2</td><td>VALUES IDENTITY_VAL_LOCAL()</td></tr>
 * <tr><td>MYSQL</td><td>SELECT LAST_INSERT_ID()</td></tr>
 * <tr><td>SQLSERVER</td><td>VALUES IDENTITY_VAL_LOCAL()</td></tr>
 * <tr><td>CLOUDSCAPE</td><td>SELECT SCOPE_IDENTITY()</td></tr>
 * <tr><td>DERBY</td><td>VALUES IDENTITY_VAL_LOCAL()</td></tr>
 * <tr><td>HSQLDB</td><td>CALL IDENTITY()</td></tr>
 * <tr><td>SYBASE</td><td>SELECT @@IDENTITY</td></tr>
 * <tr><td>DB2_MF</td><td>SELECT IDENTITY_VAL_LOCAL() FROM SYSIBM.SYSDUMMY1</td></tr>
 * </table>
 * 
 * @author tangxbai
 * @since 1.1.0
 * @see GeneratedKey#statement()
 */
public enum AutoIncrement {

	DB2( "VALUES IDENTITY_VAL_LOCAL()" ), 
	MYSQL( "SELECT LAST_INSERT_ID()" ), 
	SQLSERVER( "SELECT SCOPE_IDENTITY()" ), 
	CLOUDSCAPE( "VALUES IDENTITY_VAL_LOCAL()" ), 
	DERBY( "VALUES IDENTITY_VAL_LOCAL()" ), 
	HSQLDB( "CALL IDENTITY()" ), 
	SYBASE( "SELECT @@IDENTITY" ), 
	DB2_MF( "SELECT IDENTITY_VAL_LOCAL() FROM SYSIBM.SYSDUMMY1" );

	private String statement;

	private AutoIncrement( String statement ) {
		this.statement = statement;
	}

	public String statement() {
		return statement;
	}

}

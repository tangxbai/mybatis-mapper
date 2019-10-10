package com.viiyue.plugins.mybatis.api;

import com.viiyue.plugins.mybatis.metadata.info.GeneratedKeyInfo;

/**
 * Auto-increment sql statement provider
 * 
 * <pre>
 * &#47;&#47; Oracle query auto-incrementing primary key
 * public class OracleAutoIncrementStatementProvider implements AutoIncrementStatementProvider {
 *     &#64;Override
 *     public String getAutoIncrementSqlStatement( GeneratedKeyInfo info ) {
 *         return "SELECT " + info.getTableName() + ".NEXTVAL FROM DUAL";
 *     }
 * }
 * 
 * &#47;&#47; Model bean
 * public class YourModelBean {
 *     &#64;Id
 *     &#64;GeneratedKey( statementProvider = OracleAutoIncrementStatementProvider.class )
 *     private Long id;
 * }
 * </pre>
 *
 * @author tangxbai
 * @since 1.1.0
 */
public interface AutoIncrementStatementProvider {
	String getAutoIncrementSqlStatement( GeneratedKeyInfo info );
}

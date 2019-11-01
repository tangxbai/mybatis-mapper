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

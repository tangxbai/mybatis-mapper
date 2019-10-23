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
package com.viiyue.plugins.mybatis.provider.special;

import org.apache.ibatis.mapping.MappedStatement;

import com.viiyue.plugins.mybatis.condition.WhereExample;
import com.viiyue.plugins.mybatis.mapper.special.LogicallyDeleteMapper;
import com.viiyue.plugins.mybatis.provider.DynamicProvider;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * {@link LogicallyDeleteMapper} interface sql provider implementation class
 *
 * @author tangxbai
 * @since 1.1.0
 */
public final class LogicallyDeleteProvider extends DynamicProvider {
	
	/**
	 * Refer to the {@link LogicallyDeleteMapper#logicallyDeleteAll()
	 * logicallyDeleteAll()} method of {@link LogicallyDeleteMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String logicallyDeleteAll( MappedStatement ms ) {
		return StringUtil.join( 
			"[update] @{this.table} ", 
			"[set] @{this.tryLogicallyDelete} ", 
			"@{this.tryLogicallyDelete.useWhereQuery}" 
		);
	}

	/**
	 * Refer to the {@link LogicallyDeleteMapper#logicallyDelete(Object)
	 * logicallyDelete(Object)} method of {@link LogicallyDeleteMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String logicallyDelete( MappedStatement ms ) {
		return StringUtil.join( 
			"[update] @{this.table} ",
			"[set] @{this.tryLogicallyDelete} ",
			"%{this.where($).tryLogicallyDeleteQuery}"
		);
	}

	/**
	 * Refer to the {@link LogicallyDeleteMapper#logicallyDeleteByPrimaryKey(java.io.Serializable)
	 * logicallyDeleteByPrimaryKey(Integer)} method of {@link LogicallyDeleteMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String logicallyDeleteByPrimaryKey( MappedStatement ms ) {
		return StringUtil.join(
			"[update] @{this.table} ", 
			"[set] @{this.tryLogicallyDelete} ", 
			"[where] @{this.pk} = #{pk} ", 
			"@{this.tryLogicallyDelete.useAndQuery}"
		);
	}

	/**
	 * Refer to the
	 * {@link LogicallyDeleteMapper#logicallyDeleteByPrimaryKeyGroup(java.io.Serializable...)
	 * logicallyDeleteByPrimaryKeyGroup(PK...)} method of
	 * {@link LogicallyDeleteMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String logicallyDeleteByPrimaryKeyGroup( MappedStatement ms ) {
		return StringUtil.join(
			"[update] @{this.table} ",
			"[set] @{this.tryLogicallyDelete} ",
			"[where] @{this.pk} [in] ( {{$.inArguments}} ) ",
			"@{this.tryLogicallyDelete.useAndQuery}"
		);
	}

	/**
	 * Refer to the
	 * {@link LogicallyDeleteMapper#logicallyDeleteByPrimaryKeyIndex(Integer, java.io.Serializable)
	 * logicallyDeleteByPrimaryKeyIndex(Integer, PK)} method of
	 * {@link LogicallyDeleteMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String logicallyDeleteByPrimaryKeyIndex( MappedStatement ms ) {
		return StringUtil.join(
			"[update] @{this.table} ", 
			"[set] @{this.tryLogicallyDelete} ",
			"[where] %{this.pk($.index)} = #{pk} ",
			"@{this.tryLogicallyDelete.useAndQuery}"
		);
	}

	/**
	 * Refer to the
	 * {@link LogicallyDeleteMapper#logicallyDeleteByPrimaryKeyIndexGroup(Integer, java.io.Serializable...)
	 * logicallyDeleteByPrimaryKeyIndexGroup(Integer, PK...)} method of
	 * {@link LogicallyDeleteMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String logicallyDeleteByPrimaryKeyIndexGroup( MappedStatement ms ) {
		return StringUtil.join(
			"[update] @{this.table} ", 
			"[set] @{this.tryLogicallyDelete} ", 
			"[where] %{this.pk($.index)} [in] ( {{$.inArguments}} ) ",
			"@{this.tryLogicallyDelete.useAndQuery}"
		);
	}
	
	/**
	 * Refer to the
	 * {@link LogicallyDeleteMapper#logicallyDeleteByExample(com.viiyue.plugins.mybatis.condition.WhereExample)
	 * logicallyDeleteByExample(WhereExample)} method of
	 * {@link LogicallyDeleteMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 * @see WhereExample
	 */
	public String logicallyDeleteByExample( MappedStatement ms ) {
		return "[update] @{this.table} [set] @{this.tryLogicallyDelete} {{$.example.where}}";
	}
	
}

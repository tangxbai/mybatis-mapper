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
package com.viiyue.plugins.mybatis.provider.base;

import org.apache.ibatis.mapping.MappedStatement;

import com.viiyue.plugins.mybatis.mapper.base.BaseSelectMapper;
import com.viiyue.plugins.mybatis.provider.DynamicProvider;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * {@link BaseSelectMapper} interface sql provider implementation class
 *
 * @author tangxbai
 * @since 1.0.0
 */
public final class BaseSelectProvider extends DynamicProvider {

	/**
	 * Refer to the {@link BaseSelectMapper#selectAll() selectAll()} method of
	 * {@link BaseSelectMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String selectAll( MappedStatement ms ) {
		return StringUtil.join( 
			"[select] @{this.columns} ", 
			"[from] @{this.table} ", 
			"@{this.tryLogicallyDelete.useWhereQuery} ", 
			"@{this.defaultOrderBy}"
		);
	}

	/**
	 * Refer to the {@link BaseSelectMapper#select(Object) select(Object)} method of
	 * {@link BaseSelectMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String select( MappedStatement ms ) {
		return StringUtil.join( 
			"[select] @{this.columns} ", 
			"[from] @{this.table} ", 
			"%{this.where($).tryLogicallyDeleteQuery} ", 
			"@{this.defaultOrderBy}"
		);
	}

	/**
	 * Refer to the
	 * {@link BaseSelectMapper#selectByPrimaryKey(java.io.Serializable)
	 * selectByPrimaryKey(PK)} method of {@link BaseSelectMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String selectByPrimaryKey( MappedStatement ms ) {
		return StringUtil.join( 
			"[select] @{this.columns} ", 
			"[from] @{this.table} ", 
			"[where] @{this.pk} = #{pk} ", 
			"@{this.tryLogicallyDelete.useAndQuery} ", 
			"@{this.defaultOrderBy}"
		);
	}

	/**
	 * Refer to the
	 * {@link BaseSelectMapper#selectByPrimaryKeyIndex(Integer, java.io.Serializable)
	 * selectByPrimaryKeyIndex(Integer, PK)} method of {@link BaseSelectMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String selectByPrimaryKeyIndex( MappedStatement ms ) {
		return StringUtil.join( 
			"[select] @{this.columns} ", 
			"[from] @{this.table} ", 
			"[where] %{this.pk($.index)} = #{pk} ", 
			"@{this.tryLogicallyDelete.useAndQuery} ", 
			"@{this.defaultOrderBy}" 
		);
	}

	/**
	 * Refer to the
	 * {@link BaseSelectMapper#selectByPrimaryKeyGroup(java.io.Serializable...)
	 * selectByPrimaryKeyGroup(PK...)} method of {@link BaseSelectMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String selectByPrimaryKeyGroup( MappedStatement ms ) {
		return StringUtil.join( 
			"[select] @{this.columns} ", 
			"[from] @{this.table} ", 
			"[where] @{this.pk} [in] ( {{$.inArguments}} ) ", 
			"@{this.tryLogicallyDelete.useAndQuery} ", 
			"@{this.defaultOrderBy}" 
		);
	}

	/**
	 * Refer to the
	 * {@link BaseSelectMapper#selectByPrimaryKeyIndexGroup(Integer, java.io.Serializable...)
	 * selectByPrimaryKeyIndexGroup(Integer, PK...)} method of {@link BaseSelectMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String selectByPrimaryKeyIndexGroup( MappedStatement ms ) {
		return StringUtil.join(
			"[select] @{this.columns} ",
			"[from] @{this.table} ",
			"[where] %{this.pk($.index)} [in] ( {{$.inArguments}} ) ",
			"@{this.tryLogicallyDelete.useAndQuery} ",
			"@{this.defaultOrderBy}"
		);
	}

}

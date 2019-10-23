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
package com.viiyue.plugins.mybatis.provider.base;

import org.apache.ibatis.mapping.MappedStatement;

import com.viiyue.plugins.mybatis.mapper.base.BaseUpdateMapper;
import com.viiyue.plugins.mybatis.provider.DynamicProvider;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * {@link BaseUpdateMapper} interface sql provider implementation class
 *
 * @author tangxbai
 * @since 1.0.0
 */
public final class BaseUpdateProvider extends DynamicProvider {

	/**
	 * Refer to the {@link BaseUpdateMapper#updateByPrimaryKey(Object)
	 * updateByPrimaryKey(Object)} method of {@link BaseUpdateMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String updateByPrimaryKey( MappedStatement ms ) {
		return StringUtil.join(
			"[update] @{this.table} ", 
			"[set] @{this.set.exclude('#pks')} ", 
			"[where] @{this.pk} = #{@{this.pk.property}} ", 
			"@{this.tryOptimisticLock.useAndQuery} ",
			"@{this.tryLogicallyDelete.useAndQuery}"
		);
	}

	/**
	 * Refer to the {@link BaseUpdateMapper#updateByPrimaryKeySelective(Object)
	 * updateByPrimaryKeySelective(Object)} method of {@link BaseUpdateMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String updateByPrimaryKeySelective( MappedStatement ms ) {
		return StringUtil.join(
			"[update] @{this.table} ", 
			"[set] %{this.set.exclude('#pks').dynamic($)} ", 
			"[where] @{this.pk} = #{@{this.pk.property}} ", 
			"@{this.tryOptimisticLock.useAndQuery} ",
			"@{this.tryLogicallyDelete.useAndQuery}"
		);
	}

	/**
	 * Refer to the {@link BaseUpdateMapper#updateByPrimaryKeyIndex(Integer, Object)
	 * updateByPrimaryKeyIndex(Integer, Object)} method of {@link BaseUpdateMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String updateByPrimaryKeyIndex( MappedStatement ms ) {
		return StringUtil.join(
			"[update] @{this.table} ", 
			"[set] @{this.set.exclude('#pks').alias('param')} ",
			"[where] %{this.pk($.index)} = #{%{this.pk($.index).alias('param').property}} ",
			"@{this.tryOptimisticLock.alias('param').useAndQuery} ",
			"@{this.tryLogicallyDelete.useAndQuery}"
		);
	}

	/**
	 * Refer to the {@link BaseUpdateMapper#updateByPrimaryKeyIndexSelective(Integer, Object)
	 * updateByPrimaryKeyIndexSelective(Integer, Object)} method of {@link BaseUpdateMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String updateByPrimaryKeyIndexSelective( MappedStatement ms ) {
		return StringUtil.join(
			"[update] @{this.table} ",
			"[set] %{this.set.exclude('#pks').alias('param').dynamic($.param)} ",
			"[where] %{this.pk($.index)} = #{%{this.pk($.index).alias('param').property}} ",
			"@{this.tryOptimisticLock.alias('param').useAndQuery} ",
			"@{this.tryLogicallyDelete.useAndQuery}"
		);
	}

}

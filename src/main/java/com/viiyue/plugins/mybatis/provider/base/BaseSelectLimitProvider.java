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

import com.viiyue.plugins.mybatis.mapper.base.BaseSelectLimitMapper;
import com.viiyue.plugins.mybatis.provider.DynamicProvider;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * {@link BaseSelectLimitMapper} interface sql provider implementation class
 *
 * @author tangxbai
 * @since 1.0.0
 */
public final class BaseSelectLimitProvider extends DynamicProvider {

	/**
	 * Refer to the {@link BaseSelectLimitMapper#selectByLimit(int, int) selectByLimit(int, int)} method of
	 * {@link BaseSelectLimitMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String selectByLimit( MappedStatement ms ) {
		return StringUtil.join(
			"[select] @{this.columns} ", 
			"[from] @{this.table} ", 
			"@{this.tryLogicallyDelete.useWhereQuery} ", 
			"@{this.defaultOrderBy} ", 
			"[limit] #{offset}, #{limit}"
		);
	}
	
}

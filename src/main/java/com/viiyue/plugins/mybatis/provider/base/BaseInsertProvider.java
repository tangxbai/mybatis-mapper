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
package com.viiyue.plugins.mybatis.provider.base;

import org.apache.ibatis.mapping.MappedStatement;

import com.viiyue.plugins.mybatis.mapper.base.BaseInsertMapper;
import com.viiyue.plugins.mybatis.provider.DynamicProvider;

/**
 * {@link BaseInsertMapper} interface sql provider implementation class
 *
 * @author tangxbai
 * @since 1.0.0
 */
public final class BaseInsertProvider extends DynamicProvider {

	/**
	 * Refer to the {@link BaseInsertMapper#insert(Object) insert(Object)} method of
	 * {@link BaseInsertMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String insert( MappedStatement ms ) {
		return "[insert into] @{this.table} ( @{this.columns} ) [values] ( @{this.values} )";
	}

	/**
	 * Refer to the {@link BaseInsertMapper#insertBySelective(Object) insertBySelective(Object)} method of
	 * {@link BaseInsertMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String insertBySelective( MappedStatement ms ) {
		return "[insert into] @{this.table} ( %{this.columns.dynamic($)} ) [values] ( %{this.values.dynamic($)} )";
	}
	
}

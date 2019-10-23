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

import com.viiyue.plugins.mybatis.condition.WhereExample;
import com.viiyue.plugins.mybatis.mapper.base.BaseExampleMapper;
import com.viiyue.plugins.mybatis.provider.DynamicProvider;

/**
 * {@link BaseExampleMapper} interface sql provider implementation class
 *
 * @author tangxbai
 * @since 1.0.0
 */
public final class BaseExampleProvider extends DynamicProvider {
	
	/**
	 * Refer to the
	 * {@link BaseExampleMapper#selectByExample(com.viiyue.plugins.mybatis.condition.Example)
	 * selectByExample(Example)} method of {@link BaseExampleMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String selectByExample( MappedStatement ms ) {
		return "[select] {{$.example.columns}} [from] @{this.table} {{$.example.where}}";
	}

	/**
	 * Refer to the
	 * {@link BaseExampleMapper#deleteByExample(com.viiyue.plugins.mybatis.condition.WhereExample)
	 * selectByExample(WhereExample)} method of {@link BaseExampleMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 * @see WhereExample
	 */
	public String deleteByExample( MappedStatement ms ) {
		return "[delete from] @{this.table} {{$.example.where(false)}}";
	}

	/**
	 * Refer to the
	 * {@link BaseExampleMapper#updateByExample(com.viiyue.plugins.mybatis.condition.Example)
	 * updateByExample(Example)} method of {@link BaseExampleMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String updateByExample( MappedStatement ms ) {
		return "[update] @{this.table} [set] {{$.example.updates}} {{$.example.where}}";
	}
	
}

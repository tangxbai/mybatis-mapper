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
package com.viiyue.plugins.mybatis.metadata.info;

import java.util.Objects;

import org.apache.ibatis.mapping.SqlCommandType;

import com.viiyue.plugins.mybatis.annotation.member.GeneratedValue;
import com.viiyue.plugins.mybatis.api.GeneratedValueProvider;

/**
 * Constant value generation description bean
 *
 * @author tangxbai
 * @since 1.1.0
 */
public final class GeneratedValueInfo {

	private final SqlCommandType when;
	private final Class<? extends GeneratedValueProvider> type;

	public GeneratedValueInfo( GeneratedValue generatedValue ) {
		this.when = generatedValue.when();
		this.type = generatedValue.provider();
	}

	public SqlCommandType getWhen() {
		return when;
	}

	public Class<? extends GeneratedValueProvider> getType() {
		return type;
	}
	
	public boolean isEffective( SqlCommandType commandType ) {
		return Objects.equals( when, commandType );
	}

}

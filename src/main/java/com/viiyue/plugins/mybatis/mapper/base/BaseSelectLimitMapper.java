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
package com.viiyue.plugins.mybatis.mapper.base;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.viiyue.plugins.mybatis.annotation.mark.EnableResultMap;
import com.viiyue.plugins.mybatis.mapper.Marker;
import com.viiyue.plugins.mybatis.provider.DynamicProvider;
import com.viiyue.plugins.mybatis.provider.base.BaseSelectLimitProvider;

/**
 * Basic limit api query method interface definition
 * 
 * @author tangxbai
 * @since 1.0.0
 * 
 * @param <DO> database entity type
 * @param <DTO> query data return entity type
 * @param <PK> primary key type, must be a {@link Serializable} implementation class.
 */
public interface BaseSelectLimitMapper<DO, DTO, PK extends Serializable> extends Marker<DO, DTO, PK> {

	/**
	 * Query list data in the specified index range
	 * 
	 * @param offset offset index, starting from 0
	 * @param limit limit number
	 * @return the result list
	 */
	@EnableResultMap
	@SelectProvider( type = BaseSelectLimitProvider.class, method = DynamicProvider.dynamicSQL )
	List<DTO> selectByLimit( @Param( "offset" ) int offset, @Param( "limit" ) int limit );

}

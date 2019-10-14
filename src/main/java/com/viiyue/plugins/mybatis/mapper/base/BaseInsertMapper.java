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

import org.apache.ibatis.annotations.InsertProvider;

import com.viiyue.plugins.mybatis.annotation.member.Column;
import com.viiyue.plugins.mybatis.mapper.Marker;
import com.viiyue.plugins.mybatis.provider.DynamicProvider;
import com.viiyue.plugins.mybatis.provider.base.BaseInsertProvider;

/**
 * Basic insert api method interface definition
 * 
 * @author tangxbai
 * @since 1.0.0
 * 
 * @param <DO> database entity type
 * @param <DTO> query data return entity type
 * @param <PK> primary key type, must be a {@link Serializable} implementation class.
 */
public interface BaseInsertMapper<DO, DTO, PK extends Serializable> extends Marker<DO, DTO, PK> {

	/**
	 * Insert new data, including all fields by default, unless you set the
	 * {@link Column @Column} inserttable to false to specify which fields can
	 * not be inserted.
	 * 
	 * @param param database entity object
	 * @return generated primary key value
	 */
	@InsertProvider( type = BaseInsertProvider.class, method = DynamicProvider.dynamicSQL )
	int insert( DO param );

	/**
	 * Insert new data, this method will filter the non-null attribute values in
	 * the object as insert columns, and will not use all columns unless each
	 * field value in your object is not null.
	 * 
	 * @param param database entity object
	 * @return generated primary key value
	 */
	@InsertProvider( type = BaseInsertProvider.class, method = DynamicProvider.dynamicSQL )
	int insertBySelective( DO param );

}

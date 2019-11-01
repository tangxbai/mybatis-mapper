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
package com.viiyue.plugins.mybatis.mapper.base;

import java.io.Serializable;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;

import com.viiyue.plugins.mybatis.annotation.member.Column;
import com.viiyue.plugins.mybatis.mapper.Marker;
import com.viiyue.plugins.mybatis.provider.DynamicProvider;
import com.viiyue.plugins.mybatis.provider.base.BaseUpdateProvider;

/**
 * Basic update api method interface definition
 * 
 * @author tangxbai
 * @since 1.0.0
 * 
 * @param <DO> database entity type
 * @param <DTO> query data return entity type
 * @param <PK> primary key type, must be a {@link Serializable} implementation class.
 */
public interface BaseUpdateMapper<DO, DTO, PK extends Serializable> extends Marker<DO, DTO, PK> {

	/**
	 * <p>
	 * Modify all columns of row data. If you don't want a field to be modified,
	 * use {@link Column @Column} annotation and set {@code updateable} to
	 * false.
	 * 
	 * <p>
	 * <b>Note</b>: Data that has been logically deleted cannot be modified. The
	 * number of rows affected is always 0.
	 * <p>
	 * 
	 * @param param databse entity object
	 * @return the number of rows affected
	 */
	@UpdateProvider( type = BaseUpdateProvider.class, method = DynamicProvider.dynamicSQL )
	int updateByPrimaryKey( DO param );

	/**
	 * <p>
	 * Selectively modify non-null fields in an object. If you don't want a
	 * field to be modified, use {@link Column @Column} annotation and set
	 * {@code updateable} to false.
	 * 
	 * <p>
	 * <b>Note</b>: Data that has been logically deleted cannot be modified. The
	 * number of rows affected is always 0.
	 * <p>
	 * 
	 * @param param databse entity object
	 * @return the number of rows affected
	 */
	@UpdateProvider( type = BaseUpdateProvider.class, method = DynamicProvider.dynamicSQL )
	int updateByPrimaryKeySelective( DO param );

	/**
	 * <p>
	 * In the case of multiple primary keys, the specified data is modified by
	 * specifying a primary key index.
	 * 
	 * <p>
	 * <b>Note</b>: Data that has been logically deleted cannot be modified. The
	 * number of rows affected is always 0.
	 * <p>
	 * 
	 * @param index the primary key index
	 * @param param database entity object
	 * @return the number of rows affected
	 */
	@UpdateProvider( type = BaseUpdateProvider.class, method = DynamicProvider.dynamicSQL )
	int updateByPrimaryKeyIndex( @Param( "index" ) Integer index, @Param( "param" ) DO param );

	/**
	 * <p>
	 * In the case of multiple primary keys, the specified data is selectively
	 * modified by specifying a primary key index.
	 * 
	 * <p>
	 * <b>Note</b>: Data that has been logically deleted cannot be modified. The
	 * number of rows affected is always 0.
	 * <p>
	 * 
	 * @param index the primary key index
	 * @param param database entity object
	 * @return the number of rows affected
	 */
	@UpdateProvider( type = BaseUpdateProvider.class, method = DynamicProvider.dynamicSQL )
	int updateByPrimaryKeyIndexSelective( @Param( "index" ) Integer index, @Param( "param" ) DO param );

}

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

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;

import com.viiyue.plugins.mybatis.mapper.Marker;
import com.viiyue.plugins.mybatis.mapper.special.LogicallyDeleteMapper;
import com.viiyue.plugins.mybatis.provider.DynamicProvider;
import com.viiyue.plugins.mybatis.provider.base.BaseDeleteProvider;

/**
 * Basic delete api method interface definition
 * 
 * @author tangxbai
 * @since 1.0.0
 * 
 * @param <DO> database entity type
 * @param <DTO> query data return entity type
 * @param <PK> primary key type, must be a {@link Serializable} implementation class.
 */
public interface BaseDeleteMapper<DO, DTO, PK extends Serializable> extends Marker<DO, DTO, PK> {

	/**
	 * <p>Delete all data in the table
	 * 
	 * <p>
	 * <b>Note</b>: This operation is irreversible and belongs to physical
	 * deletion. If you want to operate the data safely, please use the
	 * {@link LogicallyDeleteMapper#logicallyDeleteAll() logicallyDeleteAll()}
	 * method instead.
	 * </p>
	 * 
	 * @return the number of rows affected
	 */
	@DeleteProvider( type = BaseDeleteProvider.class, method = DynamicProvider.dynamicSQL )
	int deleteAll();

	/**
	 * <p>Selective deletion by entity object
	 * 
	 * <p>
	 * <b>Note</b>: This operation is irreversible and belongs to physical
	 * deletion. If you want to operate the data safely, please use the
	 * {@link LogicallyDeleteMapper#logicallyDelete(Object)
	 * logicallyDelete(Object)} method instead.
	 * </p>
	 * 
	 * @param param entity object
	 * @return the number of rows affected
	 */
	@DeleteProvider( type = BaseDeleteProvider.class, method = DynamicProvider.dynamicSQL )
	int delete( DO param );

	/**
	 * <p>Delete specified data by primary key
	 * 
	 * <p>
	 * <b>Note</b>: This operation is irreversible and belongs to physical
	 * deletion. If you want to operate the data safely, please use the
	 * {@link LogicallyDeleteMapper#logicallyDeleteByPrimaryKey(Serializable)
	 * logicallyDeleteByPrimaryKey(PK)} method instead.
	 * </p>
	 * 
	 * @param primaryKey primary key value
	 * @return the number of rows affected
	 */
	@DeleteProvider( type = BaseDeleteProvider.class, method = DynamicProvider.dynamicSQL )
	int deleteByPrimaryKey( PK primaryKey );

	/**
	 * <p>Batch delete by primary key array
	 * 
	 * <p>
	 * <b>Note</b>: This operation is irreversible and belongs to physical
	 * deletion. If you want to operate the data safely, please use the
	 * {@link LogicallyDeleteMapper#logicallyDeleteByPrimaryKeyGroup(Serializable...)
	 * logicallyDeleteByPrimaryKeyGroup(PK ...)} method instead.
	 * </p>
	 * 
	 * @param primaryKeys primary key array
	 * @return the number of rows affected
	 */
	@DeleteProvider( type = BaseDeleteProvider.class, method = DynamicProvider.dynamicSQL )
	int deleteByPrimaryKeyGroup( @Param( "inArguments" ) PK ... primaryKeys );

	/**
	 * <p>In the case of multiple primary keys, specific data is deleted by
	 * specifying the primary key index and primary key value.
	 * 
	 * <p>
	 * <b>Note</b>: This operation is irreversible and belongs to physical
	 * deletion. If you want to operate the data safely, please use the
	 * {@link LogicallyDeleteMapper#logicallyDeleteByPrimaryKeyIndex(Integer, Serializable)
	 * logicallyDeleteByPrimaryKeyIndex(Integer, PK)} method instead.
	 * </p>
	 * 
	 * @param index the primary key index
	 * @param primaryKey primary key value
	 * @return the number of rows affected
	 */
	@DeleteProvider( type = BaseDeleteProvider.class, method = DynamicProvider.dynamicSQL )
	int deleteByPrimaryKeyIndex( @Param( "index" ) Integer index, @Param( "pk" ) PK primaryKey );

	/**
	 * <p>Batch deletion by specifying a primary key index, suitable for scenarios
	 * with multiple primary keys.
	 * 
	 * <p>
	 * <b>Note</b>: This operation is irreversible and belongs to physical
	 * deletion. If you want to operate the data safely, please use the
	 * {@link LogicallyDeleteMapper#logicallyDeleteByPrimaryKeyIndexGroup(Integer, Serializable...)
	 * logicallyDeleteByPrimaryKeyIndexGroup(Integer, PK...)} method instead.
	 * </p>
	 * 
	 * @param index the primary key index
	 * @param primaryKeys primary key array
	 * @return the number of rows affected
	 */
	@DeleteProvider( type = BaseDeleteProvider.class, method = DynamicProvider.dynamicSQL )
	int deleteByPrimaryKeyIndexGroup( @Param( "index" ) Integer index, @Param( "inArguments" ) PK ... primaryKeys );

}

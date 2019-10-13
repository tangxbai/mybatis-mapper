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
package com.viiyue.plugins.mybatis.mapper.special;

import java.io.Serializable;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;

import com.viiyue.plugins.mybatis.condition.SelectExample;
import com.viiyue.plugins.mybatis.condition.WhereExample;
import com.viiyue.plugins.mybatis.mapper.Marker;
import com.viiyue.plugins.mybatis.mapper.base.BaseDeleteMapper;
import com.viiyue.plugins.mybatis.mapper.base.BaseExampleMapper;
import com.viiyue.plugins.mybatis.provider.DynamicProvider;
import com.viiyue.plugins.mybatis.provider.special.LogicallyDeleteProvider;

/**
 * Logically deletion api method interface definition
 * 
 * @author tangxbai
 * @since 1.1.0
 * 
 * @param <DO> database entity type
 * @param <DTO> query data return entity type
 * @param <PK> primary key type, must be a {@link Serializable} implementation class.
 */
public interface LogicallyDeleteMapper<DO, DTO, PK extends Serializable> extends Marker<DO, DTO, PK> {

	/**
	 * <p>Delete all data in the table
	 * 
	 * <p><b>Note</b>: 
	 * This operation is reversible, just modify the data state,
	 * and will not delete the data. If you want to completely delete the target
	 * data, please use {@link BaseDeleteMapper#deleteAll() deleteAll()}
	 * instead.
	 * </p>
	 * 
	 * @return the number of rows affected
	 */
	@DeleteProvider( type = LogicallyDeleteProvider.class, method = DynamicProvider.dynamicSQL )
	int logicallyDeleteAll();
	
	/**
	 * <p>Selective deletion by entity object
	 * 
	 * <p><b>Note</b>: 
	 * This operation is reversible, just modify the data state,
	 * and will not delete the data. If you want to completely delete the target
	 * data, please use {@link BaseDeleteMapper#delete(Object) delete(Object)}
	 * instead.
	 * </p>
	 * 
	 * @param param entity object
	 * @return the number of rows affected
	 */
	@DeleteProvider( type = LogicallyDeleteProvider.class, method = DynamicProvider.dynamicSQL )
	int logicallyDelete( DO param );

	/**
	 * <p>Delete specified data by primary key
	 * 
	 * <p><b>Note</b>: 
	 * This operation is reversible, just modify the data state,
	 * and will not delete the data. If you want to completely delete the target
	 * data, please use {@link BaseDeleteMapper#deleteByPrimaryKey(Serializable)
	 * deleteByPrimaryKey(PK)} instead.
	 * </p>
	 * 
	 * @param primaryKey primary key value
	 * @return the number of rows affected
	 */
	@DeleteProvider( type = LogicallyDeleteProvider.class, method = DynamicProvider.dynamicSQL )
	int logicallyDeleteByPrimaryKey( PK primaryKey );

	/**
	 * <p>Batch delete by primary key array
	 * 
	 * <p><b>Note</b>: 
	 * This operation is reversible, just modify the data state,
	 * and will not delete the data. If you want to completely delete the target
	 * data, please use {@link BaseDeleteMapper#deleteByPrimaryKeyGroup(Serializable...)
	 * deleteByPrimaryKeyGroup(PK...)} instead.
	 * </p>
	 * 
	 * @param primaryKeys primary key array
	 * @return the number of rows affected
	 */
	@DeleteProvider( type = LogicallyDeleteProvider.class, method = DynamicProvider.dynamicSQL )
	int logicallyDeleteByPrimaryKeyGroup( @Param( "inArguments" ) PK ... primaryKeys );

	/**
	 * <p>
	 * In the case of multiple primary keys, specific data is deleted by
	 * specifying the primary key index and primary key value.
	 * 
	 * <p><b>Note</b>: 
	 * This operation is reversible, just modify the data state,
	 * and will not delete the data. If you want to completely delete the target
	 * data, please use {@link BaseDeleteMapper#deleteByPrimaryKeyIndex(Integer, Serializable)
	 * deleteByPrimaryKeyIndex(Integer, PK)} instead.
	 * </p>
	 * 
	 * @param index the primary key index
	 * @param primaryKey primary key value
	 * @return the number of rows affected
	 */
	@DeleteProvider( type = LogicallyDeleteProvider.class, method = DynamicProvider.dynamicSQL )
	int logicallyDeleteByPrimaryKeyIndex( @Param( "index" ) Integer index, @Param( "pk" ) PK primaryKey );

	/**
	 * <p>
	 * Batch deletion by specifying a primary key index, suitable for scenarios
	 * with multiple primary keys.
	 * 
	 * <p><b>Note</b>: 
	 * This operation is reversible, just modify the data state,
	 * and will not delete the data. If you want to completely delete the target
	 * data, please use {@link BaseDeleteMapper#deleteByPrimaryKeyIndexGroup(Integer, Serializable...)
	 * deleteByPrimaryKeyIndexGroup(Integer, PK...)} instead.
	 * </p>
	 * 
	 * @param index the primary key index
	 * @param primaryKeys primary key array
	 * @return the number of rows affected
	 */
	@DeleteProvider( type = LogicallyDeleteProvider.class, method = DynamicProvider.dynamicSQL )
	int logicallyDeleteByPrimaryKeyIndexGroup( @Param( "index" ) Integer index, @Param( "inArguments" ) PK ... primaryKeys );
	
	/**
	 * <p>Delete data by Example object
	 * 
	 * <pre>
	 * Example example = Example.query( Bean.class );
	 * example
	 *     .gt( "total", 10 )
	 *     .lt( "time", new Date() )
	 *     .like( "name", "mybatis-mapper" )
	 *     .startsWith( "name", "mybatis-" )
	 *     .xxx();
	 * deleteByExample( example );
	 * </pre>
	 * 
	 * <p><b>Note</b>: 
	 * This operation is reversible, just modify the data state,
	 * and will not delete the data. If you want to completely delete the target
	 * data, please use {@link BaseExampleMapper#deleteByExample(WhereExample)
	 * deleteByExample(WhereExample)} instead.
	 * </p>
	 * 
	 * @param example the example object
	 * @return the number of rows affected
	 * @see WhereExample
	 */
	@DeleteProvider( type = LogicallyDeleteProvider.class, method = DynamicProvider.dynamicSQL )
	int logicallyDeleteByExample( WhereExample<SelectExample> example );
	
}

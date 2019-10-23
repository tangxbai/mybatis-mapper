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
package com.viiyue.plugins.mybatis.mapper.special;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.viiyue.plugins.mybatis.annotation.mark.EnableResultMap;
import com.viiyue.plugins.mybatis.annotation.mark.Reference;
import com.viiyue.plugins.mybatis.mapper.Marker;
import com.viiyue.plugins.mybatis.provider.DynamicProvider;
import com.viiyue.plugins.mybatis.provider.base.BaseSelectProvider;

/**
 * Some pessimistic lock api method interface definition
 * 
 * @author tangxbai
 * @since 1.1.0
 * 
 * @param <DO> database entity type
 * @param <DTO> query data return entity type
 * @param <PK> primary key type, must be a {@link Serializable} implementation class.
 */
public interface ForUpdateMapper<DO, DTO, PK extends Serializable> extends Marker<DO, DTO, PK> {

	/**
	 * <p>
	 * Query the data list by non-null attributes in the object, and will lock
	 * the eligible data.
	 * 
	 * <p><b>Node</b>: 
	 * If you have used logically deletion, it will automatically
	 * exclude the logically deleted data.
	 * <p>
	 * 
	 * @param param database entity object
	 * @return the result list
	 */
	@EnableResultMap
	@Reference( method = "select", append = " [for update]" )
	@SelectProvider( type = BaseSelectProvider.class, method = DynamicProvider.dynamicSQL )
	List<DTO> selectForUpdate( DO param );

	/**
	 * <p>
	 * Query the specified data by primary key, and will lock
	 * the eligible data.
	 * 
	 * <p><b>Node</b>: 
	 * If you have used logically deletion, it will automatically
	 * exclude the logically deleted data.
	 * <p>
	 * 
	 * @param parimaryKey primary key value
	 * @return specific primary key data
	 */
	@EnableResultMap
	@Reference( method = "selectByPrimaryKey", append = " [for update]" )
	@SelectProvider( type = BaseSelectProvider.class, method = DynamicProvider.dynamicSQL )
	DTO selectByPrimaryKeyForUpdate( PK parimaryKey );
	
	/**
	 * <p>
	 * In the case of multiple primary keys, query the specified data by
	 * specifying the primary key index and primary key value.
	 * And will lock the eligible data.
	 * 
	 * <p><b>Node</b>: 
	 * If you have used logically deletion, it will automatically
	 * exclude the logically deleted data.
	 * <p>
	 * 
	 * @param index the primary key index
	 * @param parimaryKey primary key value
	 * @return specific primary key data
	 */
	@EnableResultMap
	@Reference( method = "selectByPrimaryKeyIndex", append = " [for update]" )
	@SelectProvider( type = BaseSelectProvider.class, method = DynamicProvider.dynamicSQL )
	DTO selectByPrimaryKeyIndexForUpdate( @Param( "index" ) Integer index, @Param( "pk" ) PK parimaryKey );

	/**
	 * <p>
	 * Query data list by primary key array, and will lock
	 * the eligible data.
	 * 
	 * <p><b>Node</b>: 
	 * If you have used logically deletion, it will automatically
	 * exclude the logically deleted data.
	 * <p>
	 * 
	 * @param parimaryKeys primary key array
	 * @return the result list
	 */
	@EnableResultMap
	@Reference( method = "selectByPrimaryKeyGroup", append = " [for update]" )
	@SelectProvider( type = BaseSelectProvider.class, method = DynamicProvider.dynamicSQL )
	List<DTO> selectByPrimaryKeyGroupForUpdate( @Param( "inArguments" ) PK ... parimaryKeys );

	/**
	 * <p>
	 * In the case of multiple primary keys, query the data list by specifying
	 * the primary key index and the primary key array.
	 * And will lock the eligible data.
	 * 
	 * <p><b>Node</b>: 
	 * If you have used logically deletion, it will automatically
	 * exclude the logically deleted data.
	 * <p>
	 * 
	 * @param index the primary key index
	 * @param parimaryKeys primary key array
	 * @return the result list
	 */
	@EnableResultMap
	@Reference( method = "selectByPrimaryKeyIndexGroup", append = " [for update]" )
	@SelectProvider( type = BaseSelectProvider.class, method = DynamicProvider.dynamicSQL )
	List<DTO> selectByPrimaryKeyIndexGroupForUpdate( @Param( "index" ) Integer index, @Param( "inArguments" ) PK ... parimaryKeys );
	
}

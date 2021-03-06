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
package com.viiyue.plugins.mybatis.mapper.special;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.viiyue.plugins.mybatis.annotation.mark.EnableResultMap;
import com.viiyue.plugins.mybatis.condition.SelectExample;
import com.viiyue.plugins.mybatis.condition.WhereExample;
import com.viiyue.plugins.mybatis.mapper.Marker;
import com.viiyue.plugins.mybatis.provider.DynamicProvider;
import com.viiyue.plugins.mybatis.provider.special.RecycleBinProvider;

/**
 * Recycle Bin api method interface definition, 
 * providing some functions to recover logically deleted data.
 * 
 * @author tangxbai
 * @since 1.1.0
 * 
 * @param <DO> database entity type
 * @param <DTO> query data return entity type
 * @param <PK> primary key type, must be a {@link Serializable} implementation class.
 */
public interface RecycleBinMapper<DO, DTO, PK extends Serializable> extends Marker<DO, DTO, PK> {

	/**
	 * Query all data that has been logically deleted
	 * 
	 * @return logically deleted data list
	 */
	@EnableResultMap
	@SelectProvider( type = RecycleBinProvider.class, method = DynamicProvider.dynamicSQL )
	List<DTO> selectAllDeleted();

	/**
	 * Restore eligible deleted data
	 * 
	 * @param param filter parameter object
	 * @return the number of rows affected
	 */
	@UpdateProvider( type = RecycleBinProvider.class, method = DynamicProvider.dynamicSQL )
	int restore( DO param );

	/**
	 * Restore all deleted data
	 * 
	 * @return the number of rows affected
	 */
	@UpdateProvider( type = RecycleBinProvider.class, method = DynamicProvider.dynamicSQL )
	int restoreAllDeleted();

	/**
	 * Restore deleted data by primary key
	 * 
	 * @param primaryKey primary key value
	 * @return the number of rows affected
	 */
	@UpdateProvider( type = RecycleBinProvider.class, method = DynamicProvider.dynamicSQL )
	int restoreByPrimaryKey( PK primaryKey );

	/**
	 * Restore multiple specified data by primary key array
	 * 
	 * @param parimaryKeys primary key array
	 * @return the number of rows affected
	 */
	@UpdateProvider( type = RecycleBinProvider.class, method = DynamicProvider.dynamicSQL )
	int restoreByPrimaryKeyGroup( @Param( "inArguments" ) PK ... parimaryKeys );
	
	/**
	 * In the case of multiple primary keys, specify deleted data by specifying
	 * primary key index and primary key value restore.
	 * 
	 * @param index the primary key index
	 * @param parimaryKey primary key value
	 * @return the number of rows affected
	 */
	@UpdateProvider( type = RecycleBinProvider.class, method = DynamicProvider.dynamicSQL )
	int restoreByPrimaryKeyIndex( @Param( "index" ) Integer index, @Param( "pk" ) PK parimaryKey );

	/**
	 * In the case of multiple primary keys, specific data is restored by
	 * specifying a primary key index and a primary key array.
	 * 
	 * @param index the primary key index
	 * @param parimaryKeys primary key array
	 * @return the number of rows affected
	 */
	@UpdateProvider( type = RecycleBinProvider.class, method = DynamicProvider.dynamicSQL )
	int restoreByPrimaryKeyIndexGroup( @Param( "index" ) Integer index, @Param( "inArguments" ) PK ... parimaryKeys );

	/**
	 * Restore specified deleted data by custom Example query object
	 * 
	 * @param example query example object
	 * @return the number of rows affected
	 */
	@UpdateProvider( type = RecycleBinProvider.class, method = DynamicProvider.dynamicSQL )
	int restoreByExample( WhereExample<SelectExample> example );

}

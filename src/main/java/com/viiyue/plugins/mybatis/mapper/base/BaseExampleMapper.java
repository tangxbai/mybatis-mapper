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
package com.viiyue.plugins.mybatis.mapper.base;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.viiyue.plugins.mybatis.annotation.mark.EnableResultMap;
import com.viiyue.plugins.mybatis.condition.Example;
import com.viiyue.plugins.mybatis.condition.SelectExample;
import com.viiyue.plugins.mybatis.condition.UpdateExample;
import com.viiyue.plugins.mybatis.condition.WhereExample;
import com.viiyue.plugins.mybatis.mapper.Marker;
import com.viiyue.plugins.mybatis.mapper.special.LogicallyDeleteMapper;
import com.viiyue.plugins.mybatis.provider.DynamicProvider;
import com.viiyue.plugins.mybatis.provider.base.BaseExampleProvider;

/**
 * Basic example api method interface definition
 * 
 * @author tangxbai
 * @since 1.0.0
 * 
 * @param <DO> database entity type
 * @param <DTO> query data return entity type
 * @param <PK> primary key type, must be a {@link Serializable} implementation class.
 */
public interface BaseExampleMapper<DO, DTO, PK extends Serializable> extends Marker<DO, DTO, PK> {

	/**
	 * <p>Delete qualified data through the Example object
	 * 
	 * <pre>
	 * Example example = Example.query( Bean.class )
	 *     .gt( "total", 10 )
	 *     .lt( "time", new Date() )
	 *     .like( "name", "mybatis-mapper" )
	 *     .startsWith( "name", "mybatis-" )
	 *     .xxx();
	 * int rows = deleteByExample( example );
	 * </pre>
	 * 
	 * <p><b>Note</b>: This operation is irreversible and belongs to physical
	 * deletion. If you want to operate the data safely, please use the
	 * {@link LogicallyDeleteMapper#logicallyDeleteByExample(Example)
	 * logicallyDeleteByExample(Example)} method instead.
	 * </p>
	 * 
	 * @param example the example object
	 * @return the number of rows affected
	 * @see WhereExample
	 */
	@DeleteProvider( type = BaseExampleProvider.class, method = DynamicProvider.dynamicSQL )
	int deleteByExample( WhereExample<SelectExample> example );

	/**
	 * <p>Modify data with the Example object
	 * 
	 * <pre>
	 * Example example = Example.update( Bean.class );
	 * 
	 * &#47;&#47; 1. Direct setting
	 * example.set( "property1", "property2", "property3", ... ).values( "value1", "value2", "value3", ... );
	 * &#47;&#47; 2. Binding object
	 * example.bind( Bean );
	 * 
	 * &#47;&#47; Use conditional query chain
	 * example.when()
	 *     .gt( "total", 10 )
	 *     .lt( "time", new Date() )
	 *     .like( "name", "mybatis-mapper" )
	 *     .startsWith( "name", "mybatis-" )
	 *     .xxx();
	 * 
	 * int rows = updateByExample( example );
	 * </pre>
	 * 
	 * @param example the example object
	 * @return the number of rows affected
	 * @see UpdateExample
	 */
	@UpdateProvider( type = BaseExampleProvider.class, method = DynamicProvider.dynamicSQL )
	int updateByExample( Example<UpdateExample> example );
	
	/**
	 * <p>Query the list of qualified data through the Example object
	 * 
	 * <pre>
	 * Example example = null;
	 * 
	 * &#47;&#47; 1. Specify statistical fields
	 * example = Example.select( Bean.class )
	 *     .includes( "id", "name", ... )
	 *     .excludes( "name", ... )
	 *   .when()
	 *     .gt( "total", 10 )
	 *     .lt( "time", new Date() )
	 *     .like( "name", "mybatis-mapper" )
	 *     .startsWith( "name", "mybatis-" )
	 *     .xxx();
	 * 
	 * &#47;&#47; 2. Or directly query all columns
	 * example = Example.query( Bean.class );
	 *     .gt( "total", 10 )
	 *     .lt( "time", new Date() )
	 *     .like( "name", "mybatis-mapper" )
	 *     .startsWith( "name", "mybatis-" )
	 *     .xxx();
	 * 
	 * &#47;&#47; Get statistical results
	 * List&lt;DTO&gt; results = selectByExample( example );
	 * </pre>
	 * 
	 * @param example the example object
	 * @return the result list
	 * @see SelectExample
	 * @see WhereExample
	 */
	@EnableResultMap
	@SelectProvider( type = BaseExampleProvider.class, method = DynamicProvider.dynamicSQL )
	List<DTO> selectByExample( Example<SelectExample> example );
	
}

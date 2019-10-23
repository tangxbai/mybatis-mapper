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
package com.viiyue.plugins.mybatis.mapper;

import java.io.Serializable;

import com.viiyue.plugins.mybatis.mapper.special.AggregateFunctionMapper;
import com.viiyue.plugins.mybatis.mapper.special.ForUpdateMapper;
import com.viiyue.plugins.mybatis.mapper.special.LogicallyDeleteMapper;
import com.viiyue.plugins.mybatis.mapper.special.RecycleBinMapper;

/**
 * <p>
 * Basic mapper interface definition with special pessimistic locking operations
 * and logically deletion interfaces.
 * 
 * <p>
 * Added {@link ForUpdateMapper}, {@link LogicallyDeleteMapper}, and
 * {@link RecycleBinMapper} interfaces since 1.1.0
 * 
 * <ul>
 * <li><code>BASE</code> {@link BaseMapper} - Basic mapper interface
 * <li><code>U</code> {@link ForUpdateMapper} - Pessimistic lock interface
 * <li><code>U</code> {@link LogicallyDeleteMapper} - Logical delete interface
 * <li><code>U</code> {@link RecycleBinMapper} - Recycle Bin interface for recovering logically deleted data
 * </ul>
 * 
 * <p>
 * <b>Note</b>: Each api interface can be inherited independently
 * 
 * @author tangxbai
 * @since 1.1.0
 * 
 * @param <DO> database entity type
 * @param <DTO> query data return entity type
 * @param <PK> primary key type, must be a {@link Serializable} implementation class.
 */
public interface Mapper<DO, DTO, PK extends Serializable> 
extends Marker<DO, DTO, PK>,
		BaseMapper<DO, DTO, PK>,
		ForUpdateMapper<DO, DTO, PK>,
		LogicallyDeleteMapper<DO, DTO, PK>,
		RecycleBinMapper<DO, DTO, PK>,
		AggregateFunctionMapper<DO, DTO, PK> {
}

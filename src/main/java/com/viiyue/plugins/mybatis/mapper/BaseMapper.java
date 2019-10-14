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
package com.viiyue.plugins.mybatis.mapper;

import java.io.Serializable;

import com.viiyue.plugins.mybatis.mapper.base.BaseDeleteMapper;
import com.viiyue.plugins.mybatis.mapper.base.BaseExampleMapper;
import com.viiyue.plugins.mybatis.mapper.base.BaseInsertMapper;
import com.viiyue.plugins.mybatis.mapper.base.BaseSelectLimitMapper;
import com.viiyue.plugins.mybatis.mapper.base.BaseSelectMapper;
import com.viiyue.plugins.mybatis.mapper.base.BaseUpdateMapper;

/**
 * <p>
 * Basic mapper interface definition, does not contain special pessimistic
 * locking operations and logically deletion interfaces.
 * 
 * <ul>
 * <li><code>C</code> {@link BaseInsertMapper} - Data insertion interface
 * <li><code>U</code> {@link BaseUpdateMapper} - Data update interface
 * <li><code>D</code> {@link BaseDeleteMapper} - Data deletion interface
 * <li><code>R</code> {@link BaseSelectMapper} - Data query interface
 * <li><code>R</code> {@link BaseSelectLimitMapper} - Data limit query interface
 * <li><code>R</code> {@link BaseExampleMapper} - Example query interface
 * </ul>
 * 
 * <p>
 * <b>Note</b>: Each api interface can be inherited independently
 * 
 * @author tangxbai
 * @since 1.0.0
 * 
 * @param <DO> database entity type
 * @param <DTO> query data return entity type
 * @param <PK> primary key type, must be a {@link Serializable} implementation class.
 */
public interface BaseMapper<DO, DTO, PK extends Serializable> 
extends Marker<DO, DTO, PK>,
		BaseInsertMapper<DO, DTO, PK>,
		BaseDeleteMapper<DO, DTO, PK>,
		BaseUpdateMapper<DO, DTO, PK>,
		BaseSelectMapper<DO, DTO, PK>,
		BaseSelectLimitMapper<DO, DTO, PK>,
		BaseExampleMapper<DO, DTO, PK> {
}

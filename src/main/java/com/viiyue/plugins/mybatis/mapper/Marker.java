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

/**
 * The plugin identifies the interface, only the class that integrates the
 * interface will be processed by the plugin.
 * 
 * @author tangxbai
 * @since 1.1.0
 * 
 * @param <DO> database entity type
 * @param <DTO> query data return entity type
 * @param <PK> primary key type, must be a {@link Serializable} implementation class.
 */
public interface Marker<DO, DTO, PK extends Serializable> {}

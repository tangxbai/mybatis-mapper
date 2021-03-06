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
package com.viiyue.plugins.mybatis.exceptions;

import com.viiyue.plugins.mybatis.utils.ObjectUtil;

/**
 * Parameter type mismatch exception
 *
 * @author tangxbai
 * @since 1.1.0
 */
public class TypeMismatchException extends MybatisMapperException {

	private static final long serialVersionUID = 5131649326844502972L;
	
	private final Class<?> type;
	
	public TypeMismatchException( Class<?> type, String message, Object ... formats ) {
		super( null, message, formats );
		this.type = type;
	}

	public Class<?> getType() {
		return ObjectUtil.defaultIfNull( type, Object.class );
	}

	@Override
	public String getMessage() {
		return super.getMessage() + " type must be <" + getType().getName() + ">";
	}

}

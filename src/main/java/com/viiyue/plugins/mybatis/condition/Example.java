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
package com.viiyue.plugins.mybatis.condition;

import java.util.Map;

import com.viiyue.plugins.mybatis.metadata.EntityParser;

/**
 * Example condition query base class
 *
 * @author tangxbai
 * @since 1.0.0, Updated in 1.3.4
 */
public abstract class Example<T extends Example<T>> {
	
	// Added in 1.3.4 at 2020/11/24
	public abstract T get();
	
	protected abstract String getQueryPart();
	protected abstract String getModifyPart();
	protected abstract String getWherePart();
	protected abstract String getWherePart( boolean isLogicallyDelete, boolean isDeleteValue );
	protected abstract Example<T> build();
	public abstract Map<String, Object> getParameters();
	
	// Quick call

	public static final SelectExample select( Class<?> modelBeanType ) {
		return new SelectExample( EntityParser.getEntity( modelBeanType ) );
	}

	public static final WhereExample<SelectExample> query( Class<?> modelBeanType ) {
		return new WhereExample<SelectExample>( EntityParser.getEntity( modelBeanType ) );
	}

	public static final UpdateExample update( Class<?> modelBeanType ) {
		return new UpdateExample( EntityParser.getEntity( modelBeanType ) );
	}
	
	// Aggregate function
	
	public static final AggregateFunctionExample minimum( Class<?> modelBeanType, String ... properties ) {
		return new AggregateFunctionExample( EntityParser.getEntity( modelBeanType ), "min", properties );
	}
	
	public static final AggregateFunctionExample maximum( Class<?> modelBeanType, String ... properties ) {
		return new AggregateFunctionExample( EntityParser.getEntity( modelBeanType ), "max", properties );
	}
	
	public static final AggregateFunctionExample summation( Class<?> modelBeanType, String ... properties ) {
		return new AggregateFunctionExample( EntityParser.getEntity( modelBeanType ), "sum", properties );
	}
	
	public static final AggregateFunctionExample average( Class<?> modelBeanType, String ... properties ) {
		return new AggregateFunctionExample( EntityParser.getEntity( modelBeanType ), "avg", properties );
	}
	
	public static final AggregateFunctionExample count( Class<?> modelBeanType, String ... properties ) {
		return new AggregateFunctionExample( EntityParser.getEntity( modelBeanType ), "count", properties );
	}
	
}

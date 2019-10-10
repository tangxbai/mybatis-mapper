package com.viiyue.plugins.mybatis.condition;

import java.util.Map;

import com.viiyue.plugins.mybatis.metadata.EntityParser;

public abstract class Example<T extends Example<T>> {
	
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

package com.viiyue.plugins.mybatis.mapper.special;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.SelectProvider;

import com.viiyue.plugins.mybatis.annotation.mark.EnableResultMap;
import com.viiyue.plugins.mybatis.annotation.mark.Reference;
import com.viiyue.plugins.mybatis.condition.AggregateFunctionExample;
import com.viiyue.plugins.mybatis.condition.Example;
import com.viiyue.plugins.mybatis.mapper.Marker;
import com.viiyue.plugins.mybatis.provider.DynamicProvider;
import com.viiyue.plugins.mybatis.provider.base.BaseExampleProvider;

/**
 * <p>Aggregate function api method interface definition
 * 
 * <ul>
 * <li>max( ? ) - Calculate the maximum value of a column
 * <li>min( ? ) - Calculate the minimum value of a column
 * <li>avg( ? ) - Calculate the average of a column
 * <li>sum( ? ) - Get the total value of a single column
 * <li>count( ?|* ) - Returns the number of rows matching the specified criteria
 * </ul>
 * 
 * @author tangxbai
 * @since 1.1.0
 * 
 * @param <DO> database entity type
 * @param <DTO> query data return entity type
 * @param <PK> primary key type, must be a {@link Serializable} implementation class.
 */
public interface AggregateFunctionMapper<DO, DTO, PK extends Serializable> extends Marker<DO, DTO, PK> {

	/**
	 * <p>Query a single statistic through an aggregate function
	 * 
	 * <pre>
	 * &#47;&#47; The minimum value of the specified column
	 * Example.minimum( Bean.class, "propertyName" ).a().b();
	 * 
	 * &#47;&#47; The maximum value of the specified column
	 * Example.maximum( Bean.class, "propertyName" ).a().b();
	 * 
	 * &#47;&#47; The sum of the specified columns
	 * Example.summation( Bean.class, "propertyName" ).a().b();
	 * 
	 * &#47;&#47; Average of the specified column
	 * Example.average( Bean.class, "propertyName" ).a().b();
	 * 
	 * &#47;&#47; The number of rows in the specified column
	 * Example.count( Bean.class, "propertyName" ).a().b();
	 * Example.count( Bean.class, "*" ).a().b();
	 * </pre>
	 * 
	 * <p><b>Note</b>: Statistics single results cannot be used with {@code groupBy}. 
	 * If groupBy is used, the result may be different from your expectations.
	 * 
	 * <p><b>WARNING</b>: It is best not to use the {@code groupBy} function
	 * 
	 * @param example the example object
	 * @return the specified statistical result and uses {@link BigDecimal} to represent
	 */
	@Reference( method = "selectByExample" )
	@SelectProvider( type = BaseExampleProvider.class, method = DynamicProvider.dynamicSQL )
	BigDecimal selectStatisticByAggregateFunction( Example<AggregateFunctionExample> example );
	
	/**
	 * <p>Query a list of statistical results
	 * 
	 * <pre>
	 * &#47;&#47; The minimum value of the specified column
	 * Example.minimum( Bean.class, "propertyName1", "propertyName2", ... ).a().b();
	 * 
	 * &#47;&#47; The maximum value of the specified column
	 * Example.maximum( Bean.class, "propertyName1", "propertyName2", ... ).a().b();
	 * 
	 * &#47;&#47; The sum of the specified columns
	 * Example.summation( Bean.class, "propertyName1", "propertyName2", ... ).a().b();
	 * 
	 * &#47;&#47; Average of the specified column
	 * Example.average( Bean.class, "propertyName1", "propertyName2", ... ).a().b();
	 * 
	 * &#47;&#47; The number of rows in the specified column
	 * Example.count( Bean.class, "propertyName1", "propertyName2", ...  ).a().b();
	 * Example.count( Bean.class, "*", "propertyName1", "propertyName2", ...  ).totalAlias( "propertyName" ).a().b();
	 * Example.count( Bean.class, "*" ).totalAlias( "propertyName" ).a().b();
	 * </pre>
	 * 
	 * <p><b>Note</b>:<p>
	 * <ul>
	 * <li>If you call <tt>groupBy/orderBy/limit</tt> and so on, then don't call the when() condition method. 
	 * <li>If you want to use conditional functions and want to use <tt>groupBy/orderBy/limit</tt>, please use when() to access.
	 * </ul>
	 * 
	 * @param example the example object
	 * @return the list of statistical results
	 */
	@EnableResultMap
	@Reference( method = "selectByExample" )
	@SelectProvider( type = BaseExampleProvider.class, method = DynamicProvider.dynamicSQL )
	List<DTO> selectStatisticListByAggregateFunction( Example<AggregateFunctionExample> example );

}

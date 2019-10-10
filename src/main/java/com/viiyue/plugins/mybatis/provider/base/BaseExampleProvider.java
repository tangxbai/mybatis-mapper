package com.viiyue.plugins.mybatis.provider.base;

import org.apache.ibatis.mapping.MappedStatement;

import com.viiyue.plugins.mybatis.condition.WhereExample;
import com.viiyue.plugins.mybatis.mapper.base.BaseExampleMapper;
import com.viiyue.plugins.mybatis.provider.DynamicProvider;

/**
 * {@link BaseExampleMapper} interface sql provider implementation class
 *
 * @author tangxbai
 * @since 1.0.0
 */
public final class BaseExampleProvider extends DynamicProvider {
	
	/**
	 * Refer to the
	 * {@link BaseExampleMapper#selectByExample(com.viiyue.plugins.mybatis.condition.Example)
	 * selectByExample(Example)} method of {@link BaseExampleMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String selectByExample( MappedStatement ms ) {
		return "[select] {{$.example.columns}} [from] @{this.table} {{$.example.where}}";
	}

	/**
	 * Refer to the
	 * {@link BaseExampleMapper#deleteByExample(com.viiyue.plugins.mybatis.condition.WhereExample)
	 * selectByExample(WhereExample)} method of {@link BaseExampleMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 * @see WhereExample
	 */
	public String deleteByExample( MappedStatement ms ) {
		return "[delete from] @{this.table} {{$.example.where(false)}}";
	}

	/**
	 * Refer to the
	 * {@link BaseExampleMapper#updateByExample(com.viiyue.plugins.mybatis.condition.Example)
	 * updateByExample(Example)} method of {@link BaseExampleMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String updateByExample( MappedStatement ms ) {
		return "[update] @{this.table} [set] {{$.example.updates}} {{$.example.where}}";
	}
	
}

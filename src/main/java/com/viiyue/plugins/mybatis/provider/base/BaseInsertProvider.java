package com.viiyue.plugins.mybatis.provider.base;

import org.apache.ibatis.mapping.MappedStatement;

import com.viiyue.plugins.mybatis.mapper.base.BaseInsertMapper;
import com.viiyue.plugins.mybatis.provider.DynamicProvider;

/**
 * {@link BaseInsertMapper} interface sql provider implementation class
 *
 * @author tangxbai
 * @since 1.0.0
 */
public final class BaseInsertProvider extends DynamicProvider {

	/**
	 * Refer to the {@link BaseInsertMapper#insert(Object) insert(Object)} method of
	 * {@link BaseInsertMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String insert( MappedStatement ms ) {
		return "[insert into] @{this.table} ( @{this.columns} ) [values] ( @{this.values} )";
	}

	/**
	 * Refer to the {@link BaseInsertMapper#insertBySelective(Object) insertBySelective(Object)} method of
	 * {@link BaseInsertMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String insertBySelective( MappedStatement ms ) {
		return "[insert into] @{this.table} ( %{this.columns.dynamic( $ )} ) [values] ( %{this.values.dynamic( $ )} )";
	}
	
}

package com.viiyue.plugins.mybatis.provider.base;

import org.apache.ibatis.mapping.MappedStatement;

import com.viiyue.plugins.mybatis.mapper.base.BaseDeleteMapper;
import com.viiyue.plugins.mybatis.provider.DynamicProvider;

/**
 * {@link BaseDeleteMapper} interface sql provider implementation class
 *
 * @author tangxbai
 * @since 1.0.0
 */
public final class BaseDeleteProvider extends DynamicProvider {

	/**
	 * Refer to the {@link BaseDeleteMapper#deleteAll() deleteAll()} method of
	 * {@link BaseDeleteMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String deleteAll( MappedStatement ms ) {
		return "[delete from] @{this.table}";
	}

	/**
	 * Refer to the {@link BaseDeleteMapper#delete(Object) delete(Object)}
	 * method of {@link BaseDeleteMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String delete( MappedStatement ms ) {
		return "[delete from] @{this.table} %{this.where($)}";
	}

	/**
	 * Refer to the
	 * {@link BaseDeleteMapper#deleteByPrimaryKey(java.io.Serializable)
	 * deleteByPrimaryKey(java.io.Serializable)} method of
	 * {@link BaseDeleteMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String deleteByPrimaryKey( MappedStatement ms ) {
		return "[delete from] @{this.table} [where] @{this.pk} = #{pk}";
	}

	/**
	 * Refer to the
	 * {@link BaseDeleteMapper#deleteByPrimaryKeyGroup(java.io.Serializable...)
	 * deleteByPrimaryKeyGroup(PK...)} method of
	 * {@link BaseDeleteMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String deleteByPrimaryKeyGroup( MappedStatement ms ) {
		return "[delete from] @{this.table} [where] @{this.pk} [in] ( {{$.inArguments}} )";
	}

	/**
	 * Refer to the
	 * {@link BaseDeleteMapper#deleteByPrimaryKeyIndex(Integer, java.io.Serializable)
	 * deleteByPrimaryKeyIndex(Integer, PK)} method of {@link BaseDeleteMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String deleteByPrimaryKeyIndex( MappedStatement ms ) {
		return "[delete from] @{this.table} [where] %{this.pk($.index)} = #{pk}";
	}

	/**
	 * Refer to the
	 * {@link BaseDeleteMapper#deleteByPrimaryKeyIndexGroup(Integer, java.io.Serializable...)
	 * deleteByPrimaryKeyIndexGroup(Integer, PK...)} method of
	 * {@link BaseDeleteMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String deleteByPrimaryKeyIndexGroup( MappedStatement ms ) {
		return "[delete from] @{this.table} [where] %{this.pk($.index)} [in] ( {{$.inArguments}} )";
	}
	
}

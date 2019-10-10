package com.viiyue.plugins.mybatis.provider.base;

import org.apache.ibatis.mapping.MappedStatement;

import com.viiyue.plugins.mybatis.mapper.base.BaseUpdateMapper;
import com.viiyue.plugins.mybatis.provider.DynamicProvider;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * {@link BaseUpdateMapper} interface sql provider implementation class
 *
 * @author tangxbai
 * @since 1.0.0
 */
public final class BaseUpdateProvider extends DynamicProvider {

	/**
	 * Refer to the {@link BaseUpdateMapper#updateByPrimaryKey(Object)
	 * updateByPrimaryKey(Object)} method of {@link BaseUpdateMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String updateByPrimaryKey( MappedStatement ms ) {
		return StringUtil.join(
			"[update] @{this.table} ", 
			"[set] @{this.set.exclude( '#pks' )} ", 
			"[where] @{this.pk} = #{@{this.pk.property}} ", 
			"@{this.tryOptimisticLock.useAnd} ",
			"@{this.tryLogicallyDelete.useAndQuery}"
		);
	}

	/**
	 * Refer to the {@link BaseUpdateMapper#updateByPrimaryKeySelective(Object)
	 * updateByPrimaryKeySelective(Object)} method of {@link BaseUpdateMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String updateByPrimaryKeySelective( MappedStatement ms ) {
		return StringUtil.join(
			"[update] @{this.table} ", 
			"[set] %{this.set.exclude( '#pks' ).dynamic( $ )} ", 
			"[where] @{this.pk} = #{@{this.pk.property}} ", 
			"@{this.tryOptimisticLock.useAnd} ",
			"@{this.tryLogicallyDelete.useAndQuery}"
		);
	}

	/**
	 * Refer to the {@link BaseUpdateMapper#updateByPrimaryKeyIndex(Integer, Object)
	 * updateByPrimaryKeyIndex(Integer, Object)} method of {@link BaseUpdateMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String updateByPrimaryKeyIndex( MappedStatement ms ) {
		return StringUtil.join(
			"[update] @{this.table} ", 
			"[set] @{this.set.exclude( '#pks' ).alias( 'param' )} ",
			"[where] %{this.pk( $.index )} = #{%{this.pk( $.index ).alias( 'param' ).property}} ",
			"@{this.tryOptimisticLock.useAnd} ",
			"@{this.tryLogicallyDelete.useAndQuery}"
		);
	}

	/**
	 * Refer to the {@link BaseUpdateMapper#updateByPrimaryKeyIndexSelective(Integer, Object)
	 * updateByPrimaryKeyIndexSelective(Integer, Object)} method of {@link BaseUpdateMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String updateByPrimaryKeyIndexSelective( MappedStatement ms ) {
		return StringUtil.join(
			"[update] @{this.table} ",
			"[set] %{this.set.exclude( '#pks' ).alias( 'param' ).dynamic( $.param )} ",
			"[where] %{this.pk( $.index )} = #{%{this.pk( $.index ).alias( 'param' ).property}} ",
			"@{this.tryOptimisticLock.useAnd} ",
			"@{this.tryLogicallyDelete.useAndQuery}"
		);
	}

}

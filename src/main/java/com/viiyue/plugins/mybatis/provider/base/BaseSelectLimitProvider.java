package com.viiyue.plugins.mybatis.provider.base;

import org.apache.ibatis.mapping.MappedStatement;

import com.viiyue.plugins.mybatis.mapper.base.BaseSelectLimitMapper;
import com.viiyue.plugins.mybatis.provider.DynamicProvider;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * {@link BaseSelectLimitMapper} interface sql provider implementation class
 *
 * @author tangxbai
 * @since 1.0.0
 */
public final class BaseSelectLimitProvider extends DynamicProvider {

	/**
	 * Refer to the {@link BaseSelectLimitMapper#selectByLimit(int, int) selectByLimit(int, int)} method of
	 * {@link BaseSelectLimitMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String selectByLimit( MappedStatement ms ) {
		return StringUtil.join(
			"[select] @{this.columns} ", 
			"[from] @{this.table} ", 
			"@{this.tryLogicallyDelete.useWhereQuery} ", 
			"@{this.defaultOrderBy} ", 
			"[limit] #{offset}, #{limit}"
		);
	}
	
}

package com.viiyue.plugins.mybatis.metadata.info;

import java.util.Objects;

import org.apache.ibatis.mapping.SqlCommandType;

import com.viiyue.plugins.mybatis.annotation.member.GeneratedValue;
import com.viiyue.plugins.mybatis.api.GeneratedValueProvider;

/**
 * Constant value generation description bean
 *
 * @author tangxbai
 * @since 1.1.0
 */
public final class GeneratedValueInfo {

	private final SqlCommandType when;
	private final Class<? extends GeneratedValueProvider> type;

	public GeneratedValueInfo( GeneratedValue generatedValue ) {
		this.when = generatedValue.when();
		this.type = generatedValue.provider();
	}

	public SqlCommandType getWhen() {
		return when;
	}

	public Class<? extends GeneratedValueProvider> getType() {
		return type;
	}
	
	public boolean isEffective( SqlCommandType commandType ) {
		return Objects.equals( when, commandType );
	}

}

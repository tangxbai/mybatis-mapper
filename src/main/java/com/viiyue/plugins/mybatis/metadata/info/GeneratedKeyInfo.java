package com.viiyue.plugins.mybatis.metadata.info;

import java.util.Objects;

import org.apache.ibatis.mapping.StatementType;

import com.viiyue.plugins.mybatis.annotation.member.GeneratedKey;
import com.viiyue.plugins.mybatis.api.AutoIncrementStatementProvider;
import com.viiyue.plugins.mybatis.api.GeneratedValueProvider;
import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.metadata.Property;
import com.viiyue.plugins.mybatis.utils.ObjectUtil;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * Primary key generation policy description bean
 *
 * @author tangxbai
 * @since 1.1.0
 */
public final class GeneratedKeyInfo {

	private final Entity entity;
	private final Property property;
	private final Class<?> resultType;
	private final String statement;
	private final boolean useGeneratedKeys;
	private final boolean executeBefore;
	private final boolean selectGeneratedKey;
	private final StatementType statementType;
	private final Class<? extends GeneratedValueProvider> primaryKeyProvider;
	private final Class<? extends AutoIncrementStatementProvider> statementProvider;

	public GeneratedKeyInfo( Property property, GeneratedKey generatedKey ) {
		this.entity = property.getParent();
		this.property = property;
		this.resultType = property.getJavaType();
		this.statement = generatedKey.statement();
		this.useGeneratedKeys = generatedKey.useGeneratedKeys();
		this.executeBefore = generatedKey.before();
		this.statementType = generatedKey.statementType();
		this.primaryKeyProvider = generatedKey.valueProvider();
		this.statementProvider = generatedKey.statementProvider();
		this.selectGeneratedKey = setSelectGeneratedKey();
	}
	
	private boolean setSelectGeneratedKey() {
		return useGeneratedKeys || ( // 1. Use the generated primary key
			// 2. Only consider the part after the statement is executed
			executeBefore == false && (
				// 3. Cannot generate a primary key externally
				Objects.equals( GeneratedValueProvider.class, primaryKeyProvider ) ||
				// 4. Statement provider is not null or sql statement cannot be empty
				ObjectUtil.isDifferent( AutoIncrementStatementProvider.class, statementProvider ) ||
				StringUtil.isNotBlank( statement )
			)
		);
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	public String getTableName() {
		return entity.getTableName();
	}

	public Property getProperty() {
		return property;
	}

	public String getKeyColumn() {
		return property.getColumn().getName();
	}

	public String getKeyProperty() {
		return property.getName();
	}
	
	public String [] getKeyColumns() {
		return new String [] { getKeyColumn() };
	}

	public String [] getKeyProperties() {
		return new String [] { getKeyProperty() };
	}
	
	public Class<?> getResultType() {
		return resultType;
	}

	public String getStatement() {
		return statement;
	}

	public boolean isUseGeneratedKeys() {
		return useGeneratedKeys;
	}
	
	public boolean isGeneratedKeyProperty( Property property ) {
		return Objects.equals( getKeyProperty(), property.getName() );
	}

	public boolean isExecuteBefore() {
		return executeBefore;
	}
	
	public boolean isSelectGeneratedKey() {
		return selectGeneratedKey;
	}

	public StatementType getStatementType() {
		return statementType;
	}

	public Class<? extends GeneratedValueProvider> getPrimaryKeyProvider() {
		return primaryKeyProvider;
	}

	public Class<? extends AutoIncrementStatementProvider> getStatementProvider() {
		return statementProvider;
	}

}

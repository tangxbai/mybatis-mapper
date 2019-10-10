package com.viiyue.plugins.mybatis.scripting.parser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Lang;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.LanguageDriverRegistry;
import org.apache.ibatis.session.Configuration;

import com.viiyue.plugins.mybatis.annotation.member.GeneratedKey;
import com.viiyue.plugins.mybatis.api.AutoIncrementStatementProvider;
import com.viiyue.plugins.mybatis.api.GeneratedValueProvider;
import com.viiyue.plugins.mybatis.enums.AutoIncrement;
import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.metadata.info.GeneratedKeyInfo;
import com.viiyue.plugins.mybatis.utils.Assert;
import com.viiyue.plugins.mybatis.utils.ClassUtil;
import com.viiyue.plugins.mybatis.utils.ObjectUtil;

/**
 * <p>
 * Primary Key Generator Registry, build a primary key generation strategy for
 * the model bean primary key.
 *
 * <ul>
 * <li>{@link Jdbc3KeyGenerator} - get the automatically generated primary key
 * to create the result of this Statement object execution
 * <li>{@link SelectKeyGenerator} - insert data does not support the primary key automatic generation solution
 * <li>{@link PrimaryKeyGenerator} - mybatis-mapper's primary key generation strategy
 * </ul>
 *
 * @author tangxbai
 * @since 1.1.0
 */
final class KeyGeneratorRegistry {

	private final Entity entity;
	private final String selectKeyId;
	private final MappedStatement statement;
	private final Configuration configuration;
	private final GeneratedKeyInfo generatedKeyInfo;
	
	public KeyGeneratorRegistry( MappedStatement ms, Entity entity, String selectKeyId ) {
		this.statement = ms;
		this.entity = entity;
		this.selectKeyId = selectKeyId;
		this.configuration = ms.getConfiguration();
		this.generatedKeyInfo = entity.getGeneratedKeyInfo();
	}

	/**
	 * <p>
	 * Get the appropriate primary key generator, 
	 * configure with {@link GeneratedKey @GeneratedKey}.
	 * 
	 * <ul>
	 * <li>if {@link GeneratedKey#useGeneratedKeys() useGeneratedKeys}
	 * is set to true, then {@link Jdbc3KeyGenerator} is used.
	 * <li>if {@link GeneratedKey#valueProvider() valueProvider}
	 * is used, use {@link PrimaryKeyGenerator}.
	 * <li>if {@link GeneratedKey#statement() statement}/{@link GeneratedKey#statementProvider() statementProvider}
	 * is used, use {@link SelectKeyGenerator}.
	 * </ul>
	 * 
	 * @param method the mapper method
	 * @return the final key generator
	 */
	public KeyGenerator getKeyGenerator( Method method ) {
		// Level 1, the var(useGeneratedKeys) is the highest level.
		// Primary key generated using jdbc
		if ( generatedKeyInfo.isUseGeneratedKeys() ) {
			return new Jdbc3KeyGenerator();
		}
		// Level 2, dynamically generated primary key value is the second level.
		// Dynamically generate primary keys
		Class<? extends GeneratedValueProvider> primaryKeyProvider = generatedKeyInfo.getPrimaryKeyProvider();
		if ( ObjectUtil.isDifferent( GeneratedValueProvider.class, primaryKeyProvider ) ) {
			return new PrimaryKeyGenerator( generatedKeyInfo );
		}
		// Minimum level, Finally use the sql statement.
		// Select sql statement generator
		LanguageDriver languageDriver = getLanguageDriver( method );
		MappedStatement selectKeyMappedStatement = addSelectKeyMappedStatement( languageDriver );
		return new SelectKeyGenerator( selectKeyMappedStatement, generatedKeyInfo.isExecuteBefore() );
	}
	
	/**
	 * Create and register a {@code MappedStatement} object for the primary key
	 * query statement
	 * 
	 * @param languageDriver the specified language driver
	 * @return the select key {@code MappedStatement}
	 */
	private MappedStatement addSelectKeyMappedStatement( LanguageDriver languageDriver ) {
		String selectKeySqlStatement = getSelectKeySqlStatement();
		Assert.notEmpty( selectKeySqlStatement, "Select key sql statement cannot be empty text, "
				+ "you can use statementProvider or statement to generate select sql statement" );
		SqlSource sqlSource = languageDriver.createSqlSource( configuration, selectKeySqlStatement, ParamMap.class );
		
		MappedStatement.Builder statementBuilder = new MappedStatement.Builder( configuration, selectKeyId, sqlSource, SqlCommandType.SELECT )
		        .resource( statement.getResource() )
		        .statementType( generatedKeyInfo.getStatementType() )
		        .keyGenerator( new NoKeyGenerator() )
		        .keyProperty( generatedKeyInfo.getKeyProperty() )
		        .keyColumn( generatedKeyInfo.getKeyColumn() )
		        .databaseId( configuration.getDatabaseId() )
				.lang( languageDriver );
        statementBuilder.resultMaps( getStatementResultMaps() );
        statementBuilder.parameterMap( getStatementParameterMap() );
        
        MappedStatement statement = statementBuilder.build();
        configuration.addMappedStatement( statement );
		return statement;
	}
	
	/**
	 * Get the statement parameter Map, 
	 * you can refer to {@link MapperBuilderAssistant#getStatementParameterMap}.
	 * 
	 * @return the statement parameter Map
	 */
	private ParameterMap getStatementParameterMap() {
		List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();
		ParameterMap.Builder builder = new ParameterMap.Builder(
			configuration,
			selectKeyId + "-Inline",
			entity.getBeanType(),
			parameterMappings
	    );
		return builder.build();
	}
	
	/**
	 * Get the statement result Map {@code List},
	 * you can refer to {@link MapperBuilderAssistant#getStatementResultMaps}.
	 * 
	 * @return
	 */
	private List<ResultMap> getStatementResultMaps() {
		ResultMap.Builder resultMapBuilder = new ResultMap.Builder(
			configuration,
			selectKeyId + "-Inline",
			generatedKeyInfo.getResultType(),
			new ArrayList<ResultMapping>(),
			null
		);
		List<ResultMap> resultMaps = new ArrayList<ResultMap>();
		resultMaps.add( resultMapBuilder.build() );
		return resultMaps;
	}
	
	/**
	 * Get the select key sql statement
	 * 
	 * @return the select key sql statement
	 */
	private String getSelectKeySqlStatement() {
		// Generate sql statement by the provider
		Class<? extends AutoIncrementStatementProvider> providerType = generatedKeyInfo.getStatementProvider();
		if ( ObjectUtil.isDifferent( AutoIncrementStatementProvider.class, providerType ) ) {
			AutoIncrementStatementProvider statementProvider = ClassUtil.newInstance( providerType );
			return statementProvider.getAutoIncrementSqlStatement( generatedKeyInfo );
		}
		// Handwritten SQL statement
		// It may be the name of the enumeration or the sql statement
		String statement = generatedKeyInfo.getStatement();
		try {
			// Enumeration constant( MYSQL/DB2/SQLSERVER/CLOUDSCAPE/DERBY/HSQLDB/SYBASE/DB2_MF )
			AutoIncrement increment = AutoIncrement.valueOf( statement );
			statement = increment.statement();
		} catch ( Exception e ) {
			// Ignore, no related enumeration reference was found
		}
		// ALERT : this statement can only be a sql text
		return statement;
	}
	
	/**
	 * If the {@link org.apache.ibatis.annotations.Lang @Lang} annotation is
	 * configured, the configuration language driver for the Lang annotation is
	 * obtained. If there is no registration, it will be automatically
	 * registered. 
	 * Otherwise, the default language driver of the configuration center is obtained.
	 * 
	 * @param method the mapper method
	 * @return the specified language driver if a driver type is specified, 
	 * otherwise returns the default language driver.
	 */
	private LanguageDriver getLanguageDriver( Method method ) {
		Lang lang = method.getAnnotation( Lang.class );
		LanguageDriverRegistry languageRegistry = configuration.getLanguageRegistry();
		if ( lang != null ) {
			languageRegistry.register( lang.value() );
			return languageRegistry.getDriver( lang.value() );
		}
		return languageRegistry.getDefaultDriver();
	}
	
}

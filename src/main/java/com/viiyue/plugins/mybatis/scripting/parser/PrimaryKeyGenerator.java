package com.viiyue.plugins.mybatis.scripting.parser;

import java.sql.Statement;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import com.viiyue.plugins.mybatis.api.GeneratedValueProvider;
import com.viiyue.plugins.mybatis.api.defaults.SnowFlakeIdValueProvider;
import com.viiyue.plugins.mybatis.api.defaults.UUIDValuePrivoder;
import com.viiyue.plugins.mybatis.metadata.info.GeneratedKeyInfo;
import com.viiyue.plugins.mybatis.utils.SingletonUtil;

/**
 * <p>
 * Mybatis-mapper's primary key generator can only be manipulated before the sql
 * statement is executed, and can generate its own primary key value using
 * {@link GeneratedValueProvider}.
 * 
 * <p>The plugin provides two default build providers:
 * <ul>
 * <li>{@link SnowFlakeIdValueProvider} - Snowflake primary key id provider
 * <li>{@link UUIDValuePrivoder} - UUID primary key id provider
 * </ul>
 *
 * @author tangxbai
 * @since 1.1.0
 */
final class PrimaryKeyGenerator implements KeyGenerator {
	
	private final GeneratedKeyInfo generatedKeyInfo;
	
	public PrimaryKeyGenerator( GeneratedKeyInfo generatedKeyInfo ) {
		this.generatedKeyInfo = generatedKeyInfo;
	}

	@Override
	public void processBefore( Executor executor, MappedStatement ms, Statement stmt, Object parameter ) {
		// Generate a primary key value and 
		// replace the primary key value in the original object
		MetaObject metaParam = SystemMetaObject.forObject( parameter );
		String primaryKeyName = generatedKeyInfo.getKeyProperty();
		// The relevant attribute name must exist in the object and primary key must be null
		if ( metaParam.hasGetter( primaryKeyName ) && metaParam.getValue( primaryKeyName ) == null ) {
			Class<? extends GeneratedValueProvider> providerType = generatedKeyInfo.getPrimaryKeyProvider();
			GeneratedValueProvider provider = SingletonUtil.getSingleton( providerType );
			Object primaryKeyValue = provider.generatedValue( generatedKeyInfo.getProperty() );
			metaParam.setValue( primaryKeyName, primaryKeyValue );
		}
		
	}

	@Override
	public void processAfter( Executor executor, MappedStatement ms, Statement stmt, Object parameter ) {
		// ignore, do nothing
	}

}

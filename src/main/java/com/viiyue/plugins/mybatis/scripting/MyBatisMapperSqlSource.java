package com.viiyue.plugins.mybatis.scripting;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.session.Configuration;

import com.viiyue.plugins.mybatis.Constants;
import com.viiyue.plugins.mybatis.api.GeneratedValueProvider;
import com.viiyue.plugins.mybatis.api.NextVersionProvider;
import com.viiyue.plugins.mybatis.condition.Condition;
import com.viiyue.plugins.mybatis.condition.Example;
import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.metadata.EntityParser;
import com.viiyue.plugins.mybatis.metadata.Property;
import com.viiyue.plugins.mybatis.metadata.info.GeneratedValueInfo;
import com.viiyue.plugins.mybatis.metadata.info.VersionInfo;
import com.viiyue.plugins.mybatis.template.TemplateHandler;
import com.viiyue.plugins.mybatis.utils.Assert;
import com.viiyue.plugins.mybatis.utils.LoggerUtil;
import com.viiyue.plugins.mybatis.utils.MapUtil;
import com.viiyue.plugins.mybatis.utils.SingletonUtil;
import com.viiyue.plugins.mybatis.utils.StatementUtil;
import com.viiyue.plugins.mybatis.utils.StringAppender;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * Mybatis-mapper compiled SQL source, including provider annotations and XML
 * statement compilation.
 *
 * @author tangxbai
 * @since 1.1.0
 */
public final class MyBatisMapperSqlSource implements SqlSource {
	
	private static final String [] arrayParameterKeys = { "array", "inArguments" };
	
	// Basic
	
	private final SqlNode sqlNode;
	private final Configuration configuration;
	
	// Helper
	
	private final String namespace;
	private final Entity entity;
	private final Class<?> modelBeanType;
	private final SqlCommandType commandType;
	private final boolean isNeedDynamicProcessing;
	private final boolean isFromXmlBuilder;
	
	// Temporary
	
	private MetaObject metaObject;

	// Constructor
	
	public MyBatisMapperSqlSource( 
		SqlNode sqlNode,
		Configuration configuration,
		SqlCommandType commandType,
		Class<?> modelBeanType,
		String namespace,
		boolean isNeedDynamicProcessing,
		boolean isFromXmlBuilder ) {
		this.sqlNode = sqlNode;
		this.configuration = configuration;
		this.commandType = commandType;
		this.modelBeanType = modelBeanType;
		this.namespace = namespace;
		this.isNeedDynamicProcessing = isNeedDynamicProcessing;
		this.isFromXmlBuilder = isFromXmlBuilder;
		this.entity = EntityParser.getEntity( modelBeanType );
	}

	@Override
	public BoundSql getBoundSql( Object parameterObject ) {
		refactoringParameter( parameterObject );
		DynamicContext context = new DynamicContext( configuration, parameterObject );
		sqlNode.apply( context );
		
		// Original sql text
		String sqlText = context.getSql();
		
		// Process template sql to get the final complete sql text
		String compiledSqlText = processingDynamicTemplates( sqlText, sqlText, parameterObject );
		
		// Check for exception content
		TemplateHandler.checkException( compiledSqlText, namespace );
		
		// Construction the SQL parameter
		Map<String, Object> bindings = context.getBindings();
		SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder( configuration );
		Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
		SqlSource sqlSource = sqlSourceParser.parse( compiledSqlText, parameterType, bindings );

		// Get the final BoundSql object
		BoundSql boundSql = sqlSource.getBoundSql( parameterObject );
		for ( Map.Entry<String, Object> entry : bindings.entrySet() ) {
			boundSql.setAdditionalParameter( entry.getKey(), entry.getValue() );
		}
		
		// If optimistic locking is enabled, generate the next version value
		// under the appropriate conditions.
		if ( isSupportOtimisticLocking( parameterType ) ) {
			generatedNextVersionValue( boundSql, parameterObject );
		}
		return boundSql;
	}
	
	private MetaObject getMetaObject( Object parameterObject ) {
		if ( this.metaObject == null ) {
			this.metaObject = configuration.newMetaObject( parameterObject );
		}
		return this.metaObject;
	}
	
	private boolean isSupportOtimisticLocking( Class<?> parameterType ) {
		return (
			StatementUtil.isUpdate( commandType ) && // Must be a modification operation
			entity.hasOptimisticLock() && // Optimistic lock @Version annotation must be enabled
			Objects.equals( parameterType, modelBeanType ) && // The parameter type must be the same as the model bean type
			StringUtil.startsWith( namespace, "update", "modify", "edit" ) // Only support specific modified sql statements
		);
	}
	
	private void refactoringParameter( Object parameterObject ) {
		if ( parameterObject == null ) return;
		if ( parameterObject instanceof Map ) {
			Map<String, Object> params = ( Map<String, Object> ) parameterObject;
			for ( String replacementKey : arrayParameterKeys ) {
				if ( params.containsKey( replacementKey ) ) {
					refactoringArrayParameter( params, replacementKey, ( Object [] ) params.get( replacementKey ) );
				}
			}
		} else if ( Objects.equals( parameterObject.getClass(), modelBeanType ) ) {
			generatedValue( parameterObject );
		}
	}
	
	private void refactoringArrayParameter( Map<String, Object> params, String replacementKey, Object [] arrays ) {
		if ( arrays != null ) {
			String param = null;
			StringAppender appender = new StringAppender();
			for ( int i = 0, len = arrays.length; i < len; i ++ ) {
				param = "VALUE_" + i;
				appender.addDelimiter( Constants.SEPARATOR );
				appender.append( "#{" + param + "}" ); // #{VALUE_0}, #{VALUE_1}, ...
				params.put( param, arrays[ i ] ); // VALUE_0, VALUE_1, ...
			}
			params.put( replacementKey, appender.toString() );
		}
	}
	
	private void generatedValue( Object parameterObject ) {
		for ( Property property : entity.getProperties() ) {
			GeneratedValueInfo generatedValueInfo = property.getGeneratedValueInfo();
			if ( generatedValueInfo != null && generatedValueInfo.isEffective( commandType ) ) {
				String propertyName = property.getName();
				MetaObject metaObject = getMetaObject( parameterObject );
				if ( metaObject.hasGetter( propertyName ) ) {
					GeneratedValueProvider valueProvider = SingletonUtil.getSingleton( generatedValueInfo.getType() );
					Object generatedValue = valueProvider.generatedValue( property );
					metaObject.setValue( propertyName, generatedValue );
				}
			}
		}
	}
	
	private void generatedNextVersionValue( BoundSql boundSql, Object parameterObject ) {
		VersionInfo versionInfo = entity.getVersionInfo();
		String parameterName = versionInfo.getParameterName();
		if ( !hasBindingsParameter( boundSql, parameterName ) ) {
			return;
		}
		
		// If optimistic locking is enabled, you must ensure that the optimistic
		// lock field value cannot be null.
		ErrorContext.instance().sql( boundSql.getSql() )
				.activity( "set the optimistic lock value" ).object( namespace )
				.message( "The optimistic lock version value cannot be null" );
		
		// Dynamically modify parameter values with mybatis reflection objects
		String proerptyName = versionInfo.getPropertyName();
		MetaObject metaObject = getMetaObject( parameterObject );
		if ( metaObject.hasGetter( proerptyName ) ) {
			Object currentVersion = metaObject.getValue( proerptyName );
			Assert.notNull( currentVersion, "The optimistic lock #{0}# cannot be null", proerptyName );
			NextVersionProvider versionProvider = SingletonUtil.getSingleton( versionInfo.getVersionProviderType() );
			Object nextVersion = versionProvider.nextVersion( versionInfo.getProperty(), currentVersion );
			boundSql.setAdditionalParameter( parameterName, nextVersion );
		}
	}
	
	private boolean hasBindingsParameter( BoundSql boundSql, String parameterName ) {
		for ( ParameterMapping parameterMapping : boundSql.getParameterMappings() ) {
			if ( Objects.equals( parameterMapping.getProperty(), parameterName ) ) {
				return true;
			}
		}
		return false;
	}
	
	private String processingDynamicTemplates( String original, String compiled, Object parameterObject ) {
		if ( isFromXmlBuilder ) {
			compiled = TemplateHandler.processTextComments( compiled );
			if ( !isNeedDynamicProcessing ) {
				compiled = TemplateHandler.processKeyWordsTemplate( configuration, compiled );
			}
		}
		if ( isNeedDynamicProcessing ) {
			StopWatch monitor = StopWatch.createStarted();
			Object templateParameter = parameterObject;
			Object logPrintParameter = parameterObject;
			if ( parameterObject != null && parameterObject instanceof Example ) {
				Example<?> example = ( Example<?> ) parameterObject;
				templateParameter = MapUtil.newMap( "example", Condition.wrap( example ) ); // {{$.example.xxx}}
				logPrintParameter = example.getParameters();
			}
			compiled = TemplateHandler.processDynamicTemplate( configuration, commandType, compiled, modelBeanType, templateParameter ); // %{...}, For all
			compiled = TemplateHandler.processParameterTemplate( configuration, commandType, compiled, modelBeanType, templateParameter ); // {{...}}, For all
			compiled = TemplateHandler.processKeyWordsTemplate( configuration, compiled ); // [...], For all
			LoggerUtil.printRuntimeLog( namespace, original, compiled, logPrintParameter, monitor );
		}
		return compiled;
	}
	
}

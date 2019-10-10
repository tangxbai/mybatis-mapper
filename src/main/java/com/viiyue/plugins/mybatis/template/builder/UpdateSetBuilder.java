package com.viiyue.plugins.mybatis.template.builder;

import org.apache.ibatis.mapping.SqlCommandType;

import com.viiyue.plugins.mybatis.Constants;
import com.viiyue.plugins.mybatis.enums.ExpressionStyle;
import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.metadata.Property;
import com.viiyue.plugins.mybatis.metadata.info.VersionInfo;
import com.viiyue.plugins.mybatis.template.builder.base.TemplateBuilder;
import com.viiyue.plugins.mybatis.utils.BuilderUtil;
import com.viiyue.plugins.mybatis.utils.PropertyFilter;

/**
 * <p>
 * Modify the statement update settings builder, the template syntax will be
 * parsed in the sql execution process, and then call this class method by
 * reflection.
 * 
 * <p>Dynamic template: <code>%{this.set.xxx}</code>
 * 
 * <p><b>Note</b>: What is a {@code dynamic} template? 
 * Is the template content that will be compiled when sql is executed.
 *
 * @author tangxbai
 * @since 1.1.0
 */
public final class UpdateSetBuilder extends TemplateBuilder {
	
	private String alias;
	private String prefix;
	private final ColumnBuilder column;
	private final PropertyFilter filter;

	public UpdateSetBuilder( Entity entity, SqlCommandType commandType ) {
		super( entity );
		this.column = new ColumnBuilder( entity );
		this.filter = new PropertyFilter( entity, commandType );
	}
	
	public UpdateSetBuilder alias( String alias ) {
		this.alias = BuilderUtil.getPrefix( alias );
		return this;
	}
	
	public UpdateSetBuilder prefix( String prefix ) {
		this.prefix = BuilderUtil.getWrappedPrefix( prefix );
		return this;
	}
	
	public UpdateSetBuilder dynamic( Object parameterObject ) {
		this.filter.dynamic( parameterObject );
		return this;
	}
	
	public UpdateSetBuilder include( String includes ) {
		this.filter.includes( includes );
		return this;
	}
	
	public UpdateSetBuilder exclude( String excludes ) {
		this.filter.excludes( excludes );
		return this;
	}
	
	@Override
	protected void finalConfirmation( Entity entity ) {
		VersionInfo versionInfo = entity.getVersionInfo();
		ExpressionStyle expressionStyle = entity.getExpressionStyle();
		for ( Property property : entity.getProperties() ) {
			if ( filter.isEffective( property ) ) {
				this.column.apply( property ).prefix( prefix ).alias( alias );
				if ( versionInfo != null && versionInfo.isVersionProperty( property ) ) {
					this.column.property( versionInfo.getParameterName() ); // nextVersionValue
				}
				this.addDelimiter( Constants.SEPARATOR );
				this.append( expressionStyle.format( column, property ) );
			}
		}
	}

}

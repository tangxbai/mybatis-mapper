package com.viiyue.plugins.mybatis.template.builder;

import org.apache.ibatis.mapping.SqlCommandType;

import com.viiyue.plugins.mybatis.Constants;
import com.viiyue.plugins.mybatis.metadata.Column;
import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.metadata.Property;
import com.viiyue.plugins.mybatis.template.builder.base.TemplateBuilder;
import com.viiyue.plugins.mybatis.utils.BuilderUtil;
import com.viiyue.plugins.mybatis.utils.PropertyFilter;
import com.viiyue.plugins.mybatis.utils.StatementUtil;

/**
 * <p>
 * Databse column builder, this class method will be called by reflection after
 * the template syntax is parsed.
 * 
 * <p>Static template: <br>
 * <code>&#64;{this.columns}</code><br>
 * <code>&#64;{this.columns.xxx}</code>
 * </p>
 * 
 * <p><b>Note</b>: What is a {@code static} template? 
 * Is the template content that will be compiled during the startup process.
 *
 * @author tangxbai
 * @since 1.1.0
 */
public final class ColumnsBuilder extends TemplateBuilder {
	
	private String prefix;
	private boolean useNameAlias;
	private final PropertyFilter filter;

	public ColumnsBuilder( Entity entity, SqlCommandType commandType ) {
		super( entity );
		this.filter = new PropertyFilter( entity, commandType );
		this.useNameAlias = StatementUtil.isSelect( commandType );
	}
	
	public ColumnsBuilder prefix( String prefix ) {
		this.prefix = BuilderUtil.getWrappedPrefix( prefix );
		return this;
	}
	
	public ColumnsBuilder dynamic( Object parameterObject ) {
		this.filter.dynamic( parameterObject );
		return this;
	}
	
	public ColumnsBuilder include( String includes ) {
		this.filter.includes( includes );
		return this;
	}
	
	public ColumnsBuilder exclude( String excludes ) {
		this.filter.excludes( excludes );
		return this;
	}
	
	@Override
	protected void finalConfirmation( Entity entity ) {
		for ( Property property : entity.getProperties() ) {
			if ( filter.isEffective( property ) ) {
				Column column = property.getColumn();
				this.addDelimiter( Constants.SEPARATOR );
				this.append( prefix );
				this.append( useNameAlias ? column.getWrappedNameWithAlias() : column.getWrappedName() );
			}
		}
	}

}

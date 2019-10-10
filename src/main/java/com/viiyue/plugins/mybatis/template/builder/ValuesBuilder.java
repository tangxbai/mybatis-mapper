package com.viiyue.plugins.mybatis.template.builder;

import org.apache.ibatis.mapping.SqlCommandType;

import com.viiyue.plugins.mybatis.Constants;
import com.viiyue.plugins.mybatis.enums.ValueStyle;
import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.metadata.Property;
import com.viiyue.plugins.mybatis.template.builder.base.TemplateBuilder;
import com.viiyue.plugins.mybatis.utils.BuilderUtil;
import com.viiyue.plugins.mybatis.utils.PropertyFilter;

/**
 * <p>
 * Insert the statement value builder,, the template syntax will be
 * parsed in the sql execution process, and then call this class method by
 * reflection.
 *
 * <p>
 * Static template: <code>&#64;{this.values}</code><br>
 * Dynamic template: <code>%{this.values.dynamic($)}</code>
 * 
 * <p><b>Note</b>: What is a {@code static} template? 
 * Is the template content that will be compiled during the startup process.
 * {@code Dynamic} template is the template text that will be compiled and parsed during sql execution.
 * 
 * @author tangxbai
 * @since 1.0.0
 */
public final class ValuesBuilder extends TemplateBuilder {
	
	private String alias;
	private final ColumnBuilder column;
	private final PropertyFilter filter;

	public ValuesBuilder( Entity entity, SqlCommandType commandType ) {
		super( entity  );
		this.column = new ColumnBuilder( entity );
		this.filter = new PropertyFilter( entity, commandType );
	}
	
	public ValuesBuilder alias( String alias ) {
		this.alias = BuilderUtil.getPrefix( alias );
		return this;
	}
	
	public ValuesBuilder dynamic( Object parameterObject ) {
		this.filter.dynamic( parameterObject );
		return this;
	}
	
	public ValuesBuilder include( String includes ) {
		this.filter.includes( includes );
		return this;
	}
	
	public ValuesBuilder exclude( String excludes ) {
		this.filter.excludes( excludes );
		return this;
	}
	
	@Override
	protected void finalConfirmation( Entity entity ) {
		ValueStyle valueStyle = entity.getValueStyle();
		for ( Property property : entity.getProperties() ) {
			if ( filter.isEffective( property ) ) {
				this.addDelimiter( Constants.SEPARATOR );
				this.append( valueStyle.format( column.apply( property ).alias( alias ) ) );
			}
		}
	}

}

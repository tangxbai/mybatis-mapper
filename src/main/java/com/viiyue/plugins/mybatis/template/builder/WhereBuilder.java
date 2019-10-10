package com.viiyue.plugins.mybatis.template.builder;

import org.apache.ibatis.mapping.SqlCommandType;

import com.viiyue.plugins.mybatis.enums.ValueStyle;
import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.metadata.Property;
import com.viiyue.plugins.mybatis.metadata.info.ConditionalInfo;
import com.viiyue.plugins.mybatis.metadata.info.LogicallyDeleteInfo;
import com.viiyue.plugins.mybatis.template.builder.base.TemplateBuilder;
import com.viiyue.plugins.mybatis.utils.BuilderUtil;
import com.viiyue.plugins.mybatis.utils.PropertyFilter;

/**
 * <p>
 * Where condition builder, the template syntax will be
 * parsed in the sql execution process, and then call this class method by
 * reflection.
 * 
 * <p>Dynamic template: <code>%{this.where($).xxx}</code>
 * 
 * <p><b>Note</b>: What is a {@code dynamic} template? 
 * Is the template content that will be compiled when sql is executed.
 *
 * @author tangxbai
 * @since 1.0.0
 */
public final class WhereBuilder extends TemplateBuilder {

	private boolean isDeletedValue;
	private boolean isLogicallyDelete;
	private final ColumnBuilder column;
	private final PropertyFilter filter;
	
	private String prefix;
	private Object parameter;

	public WhereBuilder( Entity entity, SqlCommandType commandType, Object parameter ) {
		super( entity );
		this.column = new ColumnBuilder( entity );
		this.filter = new PropertyFilter( entity, commandType );
		this.parameter = parameter;
	}
	
	public WhereBuilder logicallyDelete( boolean isLogicallyDelete ) {
		this.isLogicallyDelete = isLogicallyDelete;
		return useQueryValue();
	}
	
	public WhereBuilder tryLogicallyDeleteQuery() {
		this.isLogicallyDelete = true;
		return useQueryValue();
	}
	
	public WhereBuilder useQueryValue() {
		this.isDeletedValue = false;
		return this;
	}
	
	public WhereBuilder useDeletedValue() {
		this.isDeletedValue = true;
		return this;
	}

	public WhereBuilder prefix( String prefix ) {
		this.prefix = prefix;
		return this;
	}
	
	public WhereBuilder include( String includes ) {
		this.filter.includes( includes );
		return this;
	}

	public WhereBuilder exclude( String excludes ) {
		this.filter.excludes( excludes );
		return this;
	}
	
	@Override
	protected void finalConfirmation( Entity entity ) {
		ValueStyle valueStyle = entity.getValueStyle();
		LogicallyDeleteInfo logicallyDeleteInfo = entity.getLogicallyDeleteInfo();
		if ( isLogicallyDelete && logicallyDeleteInfo != null ) {
			this.append( BuilderUtil.getWrappedPrefix( prefix ) );
			this.append( logicallyDeleteInfo.getColumn().getWrappedName() );
			this.append( " = " );
			this.append( logicallyDeleteInfo.getValueBy( isDeletedValue ) );
		}
		for ( Property property : entity.getProperties() ) {
			if ( logicallyDeleteInfo == null || !logicallyDeleteInfo.isLogicallDeleteProperty( property ) ) {
				if ( filter.isEffective( property ) ) {
					ConditionalInfo info = property.getConditionalInfo();
					if ( info.getTestValue( parameter ) ) {
						this.column.apply( property ).prefix( prefix );
						this.addDelimiter( " [and] " );
						this.append( info.getHolder().format( column, valueStyle ) );
					}
				}
			}
		}
		this.prepend( "[where] " );
	}

}

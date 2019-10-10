package com.viiyue.plugins.mybatis.template.builder;

import com.viiyue.plugins.mybatis.enums.ValueStyle;
import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.metadata.Property;
import com.viiyue.plugins.mybatis.metadata.info.VersionInfo;
import com.viiyue.plugins.mybatis.template.builder.base.TemplateBuilder;
import com.viiyue.plugins.mybatis.utils.BuilderUtil;

/**
 * <p>
 * Database optimistic lock builder, this class method will be called by reflection after
 * the template syntax is parsed.
 * 
 * <p>Static template: <code>&#64;{this.tryOptimisticLock.xxx}</code>
 * 
 * <p><b>Note</b>: What is a {@code static} template? 
 * Is the template content that will be compiled during the startup process.
 * 
 * @author tangxbai
 * @since 1.1.0
 */
public final class OptimisticLockBuilder extends TemplateBuilder {

	private final VersionInfo info;
	private final ColumnBuilder column;
	private String prefix;
	private String modifier;

	public OptimisticLockBuilder( Entity entity ) {
		super( entity );
		this.info = entity.getVersionInfo();
		this.column = new ColumnBuilder( entity );
	}

	public OptimisticLockBuilder prefix( String prefix ) {
		this.prefix = BuilderUtil.getWrappedPrefix( prefix );
		return this;
	}
	
	public OptimisticLockBuilder useWhere() {
		this.modifier = "[where] ";
		return this;
	}
	
	public OptimisticLockBuilder useAnd() {
		this.modifier = "[and] ";
		return this;
	}
	
	public OptimisticLockBuilder useOr() {
		this.modifier = "[or] ";
		return this;
	}

	@Override
	protected void finalConfirmation( Entity entity ) {
		if ( entity.hasOptimisticLock() ) {
			Property property = info.getProperty();
			ValueStyle valueStyle = entity.getValueStyle();
			this.append( modifier );
			this.append( prefix ).append( info.getColumn().getWrappedName() );
			this.append( " = " );
			this.append( valueStyle.format( column.apply( property ) ) );
		}
	}

}

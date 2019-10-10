package com.viiyue.plugins.mybatis.template.builder;

import java.util.HashMap;

import com.viiyue.plugins.mybatis.metadata.Entity;
import com.viiyue.plugins.mybatis.utils.Assert;

/**
 * <p>
 * Database column field builder. Refer to {@link ColumnBuilder}
 * 
 * <p>Static template: <code>&#64;{this.column.property.xxx}</code> 
 *
 * <p><b>Note</b>: What is a {@code static} template? 
 * Is the template that will be compiled during the startup process.
 *
 * @author tangxbai
 * @since 1.1.0
 */
public class MappingBuilder extends HashMap<String, ColumnBuilder> {

	private static final long serialVersionUID = 1L;
	private final ColumnBuilder column;

	public MappingBuilder( Entity entity ) {
		super( 0 );
		this.column = new ColumnBuilder( entity );
	}

	@Override
	public ColumnBuilder get( Object key ) {
		Assert.notNull( key, "The property name cannot ben null" );
		return column.apply( key.toString() );
	}

}

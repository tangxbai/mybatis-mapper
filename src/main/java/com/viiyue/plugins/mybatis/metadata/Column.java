package com.viiyue.plugins.mybatis.metadata;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.viiyue.plugins.mybatis.enums.Setting;
import com.viiyue.plugins.mybatis.utils.Assert;
import com.viiyue.plugins.mybatis.utils.BuilderUtil;
import com.viiyue.plugins.mybatis.utils.ObjectUtil;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * Database column description object
 *
 * @author tangxbai
 * @since 1.0.0
 */
public final class Column {

	private final Entity parent;

	private String name;
	private String alias;
	private Class<?> javaType;
	private JdbcType jdbcType;
	private Class<? extends TypeHandler<?>> typeHandler;
	
	public Column( Entity parent, Property property ) {
		Assert.notNull( parent, "Required parent entity object" );
		Assert.notNull( property, "Required property object" );
		this.parent = parent;
		this.javaType = property.getJavaType();
	}
	
	// Setter

	protected void setName( String name ) {
		this.name = name;
	}

	protected void setAlias( String alias ) {
		this.alias = alias;
	}

	protected void setJdbcType( JdbcType jdbcType ) {
		this.jdbcType = jdbcType;
	}

	protected void setTypeHandler( Class<? extends TypeHandler<?>> typeHandler ) {
		this.typeHandler = typeHandler;
	}

	// Getter

	public Entity getParent() {
		return parent;
	}

	public String getName() {
		return name;
	}
	
	public String getAlias() {
		return alias;
	}

	public Class<?> getJavaType() {
		return javaType;
	}

	public JdbcType getJdbcType() {
		return jdbcType;
	}

	public Class<? extends TypeHandler<?>> getTypeHandler() {
		return typeHandler;
	}
	
	// Helper
	
	public String getWrappedName() {
		return Setting.ColumnStyle.getStyleValue( name );
	}
	
	public String getWrappedNameWithAlias() {
		if ( ObjectUtil.isDifferent( name, alias ) ) {
			return BuilderUtil.getAlias( getWrappedName(), alias );
		}
		return Setting.ColumnStyle.getStyleValue( alias );
	}
	
	// Override
	
	@Override
	public String toString() {
		return StringUtil.toString( this, "%s, javaType = %s, jdbcType = %s", name, javaType.getSimpleName(), jdbcType.name() );
	}

}

package com.viiyue.plugins.mybatis.template;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.greenpineyu.fel.context.FelContext;
import com.greenpineyu.fel.context.Var;
import com.viiyue.plugins.mybatis.utils.ObjectUtil;

/**
 * Template engine context object
 *
 * @author tangxbai
 * @since 1.1.0
 */
public class MapContext extends HashMap<Object, Var> implements FelContext {

	private static final long serialVersionUID = -8186234167204073954L;

	public MapContext() {
		super();
	}

	public MapContext( Map<Object, Object> map ) {
		putItems( map );
	}

	@Override
	public Object get( String name ) {
		Var var = super.get( name );
		return var == null ? null : var.getValue();
	}

	@Override
	public void set( String name, Object value ) {
		Var var = getVar( name );
		if ( var != null ) {
			var.setValue( value );
		} else {
			super.put( name, new Var( name, value ) );
		}
	}

	@Override
	public Var getVar( String name ) {
		return super.get( name );
	}

	@Override
	public void setVar( Var var ) {
		super.put( var.getName(), var );
	}
	
	public void putItems( Map<Object, Object> map ) {
		if ( ObjectUtil.isNotEmpty( map ) ) {
			for ( Map.Entry<?, ?> entry : map.entrySet() ) {
				this.set( Objects.toString( entry.getKey(), "null" ), entry.getValue() );
			}
		}
	}
	
}

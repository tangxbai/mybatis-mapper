/**
 * Copyright (C) 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

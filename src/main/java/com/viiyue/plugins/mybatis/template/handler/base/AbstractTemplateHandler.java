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
package com.viiyue.plugins.mybatis.template.handler.base;

import java.util.Map;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.apache.ibatis.type.TypeException;

import com.viiyue.plugins.mybatis.Constants;
import com.viiyue.plugins.mybatis.template.TemplateBuilderFactory;
import com.viiyue.plugins.mybatis.template.TemplateEngine;
import com.viiyue.plugins.mybatis.utils.BuilderUtil;
import com.viiyue.plugins.mybatis.utils.ObjectUtil;

/**
 * An abstract generic template handler that handles most of the syntax at the
 * beginning of this, like <code>&#64;{this.table}</code>
 *
 * @author tangxbai
 * @since 1.1.0
 */
public abstract class AbstractTemplateHandler<T> extends AbstractHandler<T> {

	public AbstractTemplateHandler( String openToken, String closeToken, boolean isDynamicHandler ) {
		super( openToken, closeToken, isDynamicHandler );
	}

	@Override
	public String handle( TemplateTokenHandler<T> handler, String fragment, T param ) {
		String rootVarName = BuilderUtil.getRootVarName( fragment );
		SqlCommandType commandType = handler.commandType( SqlCommandType.UNKNOWN );
		Class<?> modelBeanType = getModenBeanType( handler, fragment, rootVarName );
		TemplateBuilderFactory factory = TemplateBuilderFactory.build( modelBeanType, commandType );
		Object resolved = TemplateEngine.eval( fragment, rootVarName, factory, Constants.ROOT_PARAMETER_NAME, param );
		return resolved.toString();
	}
	
	/**
	 * Get the actual type of the actual database model bean through the first
	 * word of the template syntax. If the first word is this, then return the
	 * current bean type directly. If it is other, look it up from the registry.
	 * 
	 * <pre>
	 * &#64;{this.table} -&gt; this -&gt; User
	 * &#64;{other.table} -&gt; other -&gt; {@link TypeAliasRegistry#resolveAlias(String) TypeAliasRegistry.resolveAlias("other")}
	 * </pre>
	 * 
	 * @param handler template token handler
	 * @param fragment template fragment content
	 * @param rootVarName the first word of the template fragement
	 * @return the actual database model bean
	 * @see TypeAliasRegistry
	 */
	private Class<?> getModenBeanType( TemplateTokenHandler<T> handler, String fragment, String rootVarName ) {
		if ( ObjectUtil.isDifferent( Constants.DEFUALT_SCOPE, rootVarName ) ) {
			TypeAliasRegistry typeAliasRegistry = handler.configuration().getTypeAliasRegistry();
			Map<String, Class<?>> typeAliases = typeAliasRegistry.getTypeAliases();
			if ( typeAliases.containsKey( rootVarName ) ) {
				return typeAliasRegistry.resolveAlias( rootVarName );
			}
			ErrorContext.instance().activity( handler.originalContent() ).object( warp( fragment ) );
			throw new TypeException( "Could not resolve type alias '" + rootVarName + "'" );
		}
		return handler.modelBeanType();
	}

}

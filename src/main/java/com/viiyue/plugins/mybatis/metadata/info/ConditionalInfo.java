package com.viiyue.plugins.mybatis.metadata.info;

import static com.viiyue.plugins.mybatis.Constants.DEFAULT_CONDITIONAL;
import static com.viiyue.plugins.mybatis.Constants.DEFAULT_CONDITIONAL_HOLDER;
import static com.viiyue.plugins.mybatis.Constants.DEFUALT_SCOPE;
import static com.viiyue.plugins.mybatis.Constants.ROOT_PARAMETER_NAME;

import java.util.Objects;

import com.viiyue.plugins.mybatis.annotation.member.Conditional;
import com.viiyue.plugins.mybatis.annotation.member.Conditional.Holder;
import com.viiyue.plugins.mybatis.metadata.Property;
import com.viiyue.plugins.mybatis.template.TemplateEngine;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * The conditional description bean that the java field takes effect in dynamic sql
 *
 * @author tangxbai
 * @since 1.1.0
 */
public final class ConditionalInfo {

	private final Property property;
	private final String conditional;
	private final Holder holder;
	
	private ConditionalInfo( Property property, Conditional conditional ) {
		this( property, conditional.value(), conditional.holder() );
	}
	
	private ConditionalInfo( Property property, String conditional, Holder holder ) {
		this.property = property;
		this.conditional = setConditional( conditional );
		this.holder = holder;
	}
	
	private String setConditional( String conditional ) {
		// function( this ) -> function( $.property )
		if ( conditional.contains( DEFUALT_SCOPE ) ) {
			conditional = StringUtil.replace( conditional, DEFUALT_SCOPE, ROOT_PARAMETER_NAME + "." + property.getName() );
		}
		// function( $.property )
		return conditional;
	}
	
	public Property getProperty() {
		return property;
	}

	public String getConditional() {
		return conditional;
	}

	public Holder getHolder() {
		return holder;
	}

	public boolean getTestValue( Object parameter ) {
		return Objects.equals( Boolean.TRUE, TemplateEngine.eval( getConditional(), ROOT_PARAMETER_NAME, parameter ) );
	}

	@Override
	public String toString() {
		return super.toString();
	}

	public static ConditionalInfo newInfo( Property property, Conditional conditional ) {
		if ( conditional == null ) {
			return new ConditionalInfo( property, DEFAULT_CONDITIONAL, DEFAULT_CONDITIONAL_HOLDER );
		}
		return new ConditionalInfo( property, conditional );
	}
	
}

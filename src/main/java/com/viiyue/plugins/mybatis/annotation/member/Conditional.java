package com.viiyue.plugins.mybatis.annotation.member;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.viiyue.plugins.mybatis.Constants;
import com.viiyue.plugins.mybatis.enums.Template;
import com.viiyue.plugins.mybatis.enums.ValueStyle;
import com.viiyue.plugins.mybatis.template.builder.ColumnBuilder;
import com.viiyue.plugins.mybatis.template.function.BlankFunction;
import com.viiyue.plugins.mybatis.template.function.EqualFunction;
import com.viiyue.plugins.mybatis.template.function.NotBlankFunction;
import com.viiyue.plugins.mybatis.template.function.NotNullFunction;
import com.viiyue.plugins.mybatis.template.function.NullFunction;

/**
 * Dynamic conditional judgment annotation
 * 
 * @author tangxbai
 * @since 1.1.0
 */
@Documented
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention( RetentionPolicy.RUNTIME )
public @interface Conditional {

	/**
	 * Conditional judgment expression for dynamic value
	 * 
	 * <p>1. {@link BlankFunction} - isBlank( Object )</p>
	 * <ul type="square">
	 * <li>isBlank( null ) -&gt; true</li>
	 * <li>isBlank( "" ) -&gt; true</li>
	 * <li>isBlank( " " ) -&gt; true</li>
	 * <li>isBlank( "\n\t\s" ) -&gt; true</li>
	 * <li>isBlank( "a" ) -&gt; false</li>
	 * <li>isBlank( "a b" ) -&gt; false</li>
	 * </ul>
	 * 
	 * <p>2. {@link NotBlankFunction} - isNotBlank( Object )</p>
	 * <ul type="square">
	 * <li>isNotBlank( null ) -&gt; false</li>
	 * <li>isNotBlank( "" ) -&gt; false</li>
	 * <li>isNotBlank( " " ) -&gt; false</li>
	 * <li>isNotBlank( "\n\t\s" ) -&gt; false</li>
	 * <li>isNotBlank( "a" ) -&gt; true</li>
	 * <li>isNotBlank( "a b" ) -&gt; true</li>
	 * </ul>
	 * 
	 * <p>3. {@link EqualFunction} - equals( Object, Object )</p>
	 * <ul type="square">
	 * <li>equals( null, null ) -&gt; true</li>
	 * <li>equals( "foo", null ) -&gt; false</li>
	 * <li>equals( null, "foo" ) -&gt; false</li>
	 * <li>equals( "foo", "foo" ) -&gt; true</li>
	 * </ul>
	 * 
	 * <p>4. {@link NullFunction} - isNull( Object )</p>
	 * <ul type="square">
	 * <li>isNull( null ) -&gt; true</li>
	 * <li>isNull( [others] ) -&gt; false</li>
	 * </ul>
	 * 
	 * <p>5. {@link NotNullFunction} - isNotNull( Object )</p>
	 * <ul type="square">
	 * <li>isNotNull( null ) -&gt; false</li>
	 * <li>isNotNull( [others] ) -&gt; true</li>
	 * </ul>
	 * </dl>
	 */
	String value() default Constants.DEFAULT_CONDITIONAL;

	/**
	 * Conditional expression holder, default is "{@code =}". 
	 * For example : column = #{property}
	 */
	Holder holder();

	/**
	 * Conditional judgment expression operator holder
	 *
	 * @author tangxbai
	 * @since 1.1.0
	 * @see Template
	 */
	enum Holder {

		/** @see Template#IsNull */
		IsNull( Template.IsNull ), 
		
		/** @see Template#NotNull */
		NotNull( Template.NotNull ), 
		
		/** @see Template#Equal */
		Equal( Template.Equal ), 
		
		/** @see Template#NotEqual */
		NotEqual( Template.NotEqual ), 
		
		/** @see Template#Like */
		Like( Template.Like ), 
		
		/** @see Template#NotLike */
		NotLike( Template.NotLike ), 
		
		/** @see Template#StartsWith */
		StartsWith( Template.StartsWith ),
		
		/** @see Template#EndsWith */
		EndsWith( Template.EndsWith ), 
		
		/** @see Template#Regexp */
		Regexp( Template.Regexp ), 
		
		/** @see Template#NotRegexp */
		NotRegexp( Template.NotRegexp ), 
		
		/** @see Template#GreaterThan */
		GreaterThan( Template.GreaterThan ), 
		
		/** @see Template#GreaterThanAndEqualTo */
		GreaterThanAndEqualTo( Template.GreaterThanAndEqualTo ), 
		
		/** @see Template#LessThan */
		LessThan( Template.LessThan ), 
		
		/** @see Template#LessThanAndEqualTo */
		LessThanAndEqualTo( Template.LessThanAndEqualTo );

		private Template template;

		private Holder( Template template ) {
			this.template = template;
		}

		public String format( ColumnBuilder column, ValueStyle valueStyle ) {
			return template.format( column, valueStyle );
		}

	}

}

package com.viiyue.plugins.mybatis.annotation.rule;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.viiyue.plugins.mybatis.enums.NameStyle;

/**
 * Define conversion rules between java names and databases
 * 
 * <ul>
 * <li>{@link NameStyle#DEFAULT DEFAULT} - propertyName -&gt; propertyName
 * <li>{@link NameStyle#LOWERCASE LOWERCASE} - propertyName -&gt; propertyname
 * <li>{@link NameStyle#UNDERLINE UNDERLINE} - propertyName -&gt; property_name
 * <li>{@link NameStyle#UNDERLINE_UPPERCASE UNDERLINE_UPPERCASE} - propertyName -&gt; PROPERTY_NAME
 * <li>{@link NameStyle#UPPERCASE UPPERCASE} - propertyName -&gt; PROPERTYNAME
 * </ul>
 * 
 * @author tangxbai
 * @since 1.0.0
 */
@Inherited
@Target( ElementType.TYPE )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface NamingRule {
	NameStyle value();
}

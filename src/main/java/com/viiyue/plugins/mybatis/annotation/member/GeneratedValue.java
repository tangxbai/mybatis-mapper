package com.viiyue.plugins.mybatis.annotation.member;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.ibatis.mapping.SqlCommandType;

import com.viiyue.plugins.mybatis.api.GeneratedValueProvider;
import com.viiyue.plugins.mybatis.api.defaults.SnowFlakeIdValueProvider;
import com.viiyue.plugins.mybatis.api.defaults.UUIDValuePrivoder;

/**
 * Used to generate a value when executing the sql statement. The plugin
 * provides two default value generation implementations, you can choose whether
 * to use them, or you can implement your own logic yourself.
 * 
 * <p>Default providers:
 * <ul>
 * <li>{@link SnowFlakeIdValueProvider} - Generate a globally unique sortable id of type {@link Long}
 * <li>{@link UUIDValuePrivoder} - Universally unique identifier of type {@link String}
 * </ul>
 * 
 * <p>Or you can implement your own logic like this:
 * 
 * <pre>
 * &#47;&#47; Define your implementation
 * public MyselfGeneatedValue implements ValueProvider {
 *    &#64;Override
 *    public Object generatedValue( Property property ) {
 *        &#47;&#47; Return the value you need
 *        return null;
 *    }
 * }
 * 
 * &#47;&#47; Then
 * public class YourModelBean {
 *     
 *     &#47;&#47; Shorthand
 *     &#64;GeneratedValue( MyselfGeneatedValue.class ) 
 *     &#47;&#47; Full definition
 *     &#47;&#47; &#64;GeneratedValue( provider = MyselfGeneatedValue.class, when = SqlCommandType.INSERT ) 
 *     private Long millis;
 *     
 * }
 * </pre>
 * 
 * @author tangxbai
 * @since 1.1.0
 */
@Documented
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention( RetentionPolicy.RUNTIME )
public @interface GeneratedValue {
	
	/**
	 * Generate default values for specific statement objects, which by default
	 * only take effect when data is inserted.
	 */
	SqlCommandType when() default SqlCommandType.INSERT;
	
	/**
	 * Value provider, defaults to the snowflake id provider.
	 */
	Class<? extends GeneratedValueProvider> provider() default SnowFlakeIdValueProvider.class;
	
}

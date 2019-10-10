package com.viiyue.plugins.mybatis.template.function;

import java.util.Objects;

/**
 * <p>
 * Fel-engine extends the equality judgment function to determine whether two
 * given objects are equal
 * 
 * <ul>
 * <li>equals(null, null) - true
 * <li>equals(null, *) - false
 * <li>equals(*, null) - false
 * <li>equals(" ", "\s") - false
 * <li>equals("mybatis-mapper", "mybatis-mapper") - true
 * </ul>
 *
 * @author tangxbai
 * @since 1.0.0
 * @see java.util.Objects#equals(Object, Object)
 */
public class EqualFunction extends AbstractFunction {
	
	public EqualFunction() {
		super( 2 );
	}

	@Override
	public String getName() {
		return "equals";
	}

	@Override
	public Object invoke( Object target, Object another ) {
		return Objects.equals( target, another );
	}

}

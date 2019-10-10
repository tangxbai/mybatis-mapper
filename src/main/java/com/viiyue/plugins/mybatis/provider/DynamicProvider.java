package com.viiyue.plugins.mybatis.provider;

import java.util.HashMap;
import java.util.Map;

import com.viiyue.plugins.mybatis.scripting.parser.BridgeMethod;
import com.viiyue.plugins.mybatis.utils.Assert;

/**
 * Dynamic provider base class, if you want to define your own provider, then
 * you are bound to inherit this class.
 *
 * @author tangxbai
 * @since 1.0.0
 */
public abstract class DynamicProvider {

	/**
	 * Provider placeholder method name : {@value}
	 * 
	 * @see #providerSQL()
	 */
	public static final String dynamicSQL = "dynamicProviderSQL";
	
	/**
	 * Method mapping used by the current subclass
	 */
	private final Map<String, BridgeMethod> subclassMethodMappings = new HashMap<String, BridgeMethod>();
	
	/**
	 * Useless, Just for myBatis to processing the provider SQL.
	 * 
	 * @return SQL placeholder string
	 */
	public final String dynamicProviderSQL() {
		return "<DynamicProviderSQL>";
	}
	
	/**
	 * Remember all the methods that will apply to the current Provider
	 * 
	 * @param method mapper mapping method
	 * @since 1.1.0
	 */
	public final void putMethod( BridgeMethod method ) {
		this.subclassMethodMappings.put( method.getInterfaceMethodName(), method );
	}
	
	/**
	 * Get the cache method by method name
	 * 
	 * @param interfaceMethodName the interface method name
	 * @return the cached method
	 */
	public final BridgeMethod getMethod( String interfaceMethodName ) {
		BridgeMethod method = subclassMethodMappings.get( interfaceMethodName );
		Assert.notNull( method, "The provider method #{0}# was not be found in class #{1}#", interfaceMethodName, getClass().getSimpleName() );
		return method;
	}
	
}

package com.viiyue.plugins.mybatis.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Simple singleton tool class
 *
 * @author tangxbai
 * @since 1.1.0
 */
public class SingletonUtil {

	private static final ReentrantLock locker = new ReentrantLock();
	private static final Map<Class<?>, Object> singletons = new ConcurrentHashMap<Class<?>, Object>();
	
	private SingletonUtil () {}
	
	public final static <T> T getSingleton( Class<T> beanType, Object ... arguments ) {
		Object singleton = singletons.get( beanType );
		if ( singleton == null ) {
			try {
				locker.lock();
				singleton = ClassUtil.newInstance( beanType, arguments );
				singletons.put( beanType, singleton );
			} finally {
				locker.unlock();
			}
		}
		return ( T ) singleton;
	}
	
}

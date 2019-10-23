/*-
 * Apache　LICENSE-2.0
 * #
 * Copyright (C) 2017 - 2019 mybatis-mapper
 * #
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ------------------------------------------------------------------------
 */
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
	
	public static final <T> T getSingleton( Class<T> beanType, Object ... arguments ) {
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

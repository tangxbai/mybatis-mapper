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
package com.viiyue.plugins.mybatis.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides utility methods and decorators for Map
 *
 * <pre>
 * &#47;&#47; Quickly create a Map
 * Map&lt;Object, Object&gt; map = MapUtil.newMap("key1", "value1", "key2", "value2", "key3", "value3", ...);
 * map -&gt; { "key1"="value1", "key2"="value2", "key3"="value3" }
 * 
 * &#47;&#47; Batch remove map keys
 * MapUtil.remove(map, "key1", "key2");
 * map -&gt; { "key3"="value3" }
 * </pre>
 *
 * @author tangxbai
 * @since 1.1.0
 */
public class MapUtil {
	
	private MapUtil () {}
	
	/**
	 * Batch remove map keys
	 * 
	 * @param mapObject the {@code Map} object
	 * @param keys {@code Map} key array
	 */
	public static void remove( Object mapObject, Object ... keys ) {
		remove( ( Map<?, ?> ) mapObject, keys );
	}
	
	/**
	 * Batch remove map keys
	 * 
	 * @param mapObject the {@code Map} object
	 * @param keys {@code Map} key array
	 */
	public static void remove( Map<?, ?> map, Object ... keys ) {
		if ( map != null ) {
			for ( Object key : keys ) {
				map.remove( key );
			}
		}
	}

	/**
	 * <p>
	 * Quickly create a Map using a similar key-value array. The array must be in
	 * pairs. If an odd number is present, the last array element is
	 * automatically ignored.
	 * 
	 * <pre>
	 * &#47;&#47; One parameter
	 * MapUtil.newMap("key1") -&gt; An empty {@code Map}
	 * &#47;&#47; Two parameters
	 * MapUtil.newMap("key1", *) -&gt; { "key1"=* }
	 * &#47;&#47; Odd parameters
	 * MapUtil.newMap("key1", *, "key2") -&gt; { "key1"=* }
	 * &#47;&#47; Even number of parameters
	 * MapUtil.newMap("key1", *, "key2", *, ...) -&gt; { "key1"=*, "key2"=*, ... }
	 * </pre>
	 * 
	 * @param <K> The map key type
	 * @param <V> The map value type
	 * @param properties similar to the key-value array parameter, may be {@code null}
	 * @return {@code null} if the key-value array parameters is null, otherwise a new map filled with data.
	 */
	public static <K, V> Map<K, V> newMap( Object ... properties ) {
		if ( properties == null ) {
			return null;
		}
		int size = properties.length;
		int times = size % 2 == 0 ? size : size - 1;
		final Map<K, V> newMap = new HashMap<K, V>( times / 2 );
		if ( size > 0 ) {
			for ( int i = 0; i < times; i += 2 ) {
				Object key = properties[ i ];
				if ( key != null ) {
					newMap.put( ( K ) key, ( V ) properties[ i + 1 ] );
				}
			}
		}
		return newMap;
	}

}

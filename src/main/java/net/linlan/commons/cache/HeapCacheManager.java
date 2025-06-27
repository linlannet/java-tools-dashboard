/**
 * Copyright 2020-2023 the original author or Linlan authors.
 *
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
 */
package net.linlan.commons.cache;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * the heap cache manager for project to use
 * Filename:HeapCacheManager.java
 * Desc:the heap cache manager
 *
 * @author Linlan
 * CreateTime:2020-08-17 6:32 PM
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class HeapCacheManager<T> implements CacheManager<T> {

    /**
     * the cache in ConcurrentMap to support heap
     */
    private ConcurrentMap<String, CacheObject> cache = new ConcurrentHashMap<>();

    /**
     * @param key    the key for get and use
     * @param data   the value of cache
     * @param expire the expire time
     */
    @Override
    public void put(String key, T data, long expire) {
        cache.put(key, new CacheObject(new Date().getTime(), expire, data));
    }

    /**
     * @param key the input key, the unique key
     * @return T, the value of cache
     */
    @Override
    public T get(String key) {
        CacheObject o = cache.get(key);
        if (o == null || new Date().getTime() >= o.getT1() + o.getExpire())
            return null;
        else {
            return (T) o.getD();
        }
    }

    /**
     * @param key the input key, the unique key
     */
    @Override
    public void remove(String key) {
        cache.remove(key);
    }

}

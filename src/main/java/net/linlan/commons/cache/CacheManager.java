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

/**
 * the Interface for cache operation
 * Filename:CacheManager.java
 * Desc: the cache manager for ehcache, heap, redis to implement
 *
 * @author Linlan
 * CreateTime:2020-08-17 6:24 PM
 *
 * @version 1.0
 * @since 1.0
 *
 */
public interface CacheManager<T> {

    /** put data to key with expire time
     * @param key the key for get and use
     * @param data the value of cache
     * @param expire the expire time
     */
    void put(String key, T data, long expire);

    /** get the cache by input key
     * @param key the input key, the unique key
     * @return T, the value of cache
     */
    T get(String key);

    /**remove the key and the value of cache
     * @param key the input key, the unique key
     */
    void remove(String key);
}

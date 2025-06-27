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

import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * the redis cache manager to implement CacheManager
 * Filename:RedisCacheManager.java
 * Desc:the redis cache manager to use redis template
 *
 * @author Linlan
 * CreateTime:2020-08-17 6:34 PM
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class RedisCacheManager<T> implements CacheManager<T> {

    /**
     * the redis template, need to autowired or set as a bean to access
     */
    private RedisTemplate<String, T> redisTemplate;

    /**
     * @param key    the key for get and use
     * @param data   the value of cache
     * @param expire the expire time
     */
    @Override
    public void put(String key, T data, long expire) {
        redisTemplate.boundValueOps(key).set(data, expire, TimeUnit.MILLISECONDS);
    }

    /**
     * @param key the input key, the unique key
     * @return T, the value of cache
     */
    @Override
    public T get(String key) {
        return redisTemplate.boundValueOps(key).get();
    }

    /**
     * @param key the input key, the unique key
     */
    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }

    /**set the redis template of redis cache manager
     * @param redisTemplate
     */
    public void setRedisTemplate(RedisTemplate<String, T> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}

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
 *
 * the class of Cache constants define
 * Filename:CacheConstants.java
 * Desc:the constants of cache manager, in this class to define constants
 *
 * @author Linlan
 * CreateTime:2020-08-17 6:39 PM
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class CacheConstants {


    /**
     * 默认过期时长，单位：秒 5秒
     */
    public final static long FIVE_SECOND_EXPIRE = 5;
    /**
     * 默认过期时长，单位：秒 20秒
     */
    public final static long TWENTY_SECOND_EXPIRE = 20;

    /**
     * 默认过期时长，单位：秒 60秒，1分钟
     */
    public final static long ONE_MINUTE_EXPIRE = 60;

    /**
     * 默认过期时长，单位：秒 10分钟
     */
    public final static long TEN_MINUTE_EXPIRE = 60 * 10;

    /**
     * 默认过期时长，单位：秒 1小时
     */
    public final static long ONE_HOURS_EXPIRE = 60 * 60;

    /**
     * 默认过期时长，单位：秒 1天
     */
    public final static long ONE_DAY_EXPIRE = 60 * 60 * 24;

    /**
     * 默认过期时长，单位：秒 30天，28天？
     */
    public final static long ONE_MONTH_EXPIRE = 60 * 60 * 24 * 30;

    /**
     * 不设置过期时长
     */
    public final static long NOT_EXPIRE = -1;

}

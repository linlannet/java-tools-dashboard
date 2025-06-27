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

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * the base cache object for commons to use
 * Filename:CacheObject.java
 * Desc:the base cache object for Ehcache, Heap to use
 *
 * @author Linlan
 * CreateTime:2020-08-17 6:22 PM
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class CacheObject implements Serializable {
    private long t1;
    private long expire;
    private byte[] d;

    public CacheObject(long t1, long expire, Object d) {
        this.t1 = t1;
        this.expire = expire;
        if (d != null) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            Hessian2Output ho = new Hessian2Output(os);
            try {
                ho.startMessage();
                ho.writeObject(d);
                ho.completeMessage();
                ho.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.d = os.toByteArray();
        }
    }

    public long getT1() {
        return t1;
    }

    public void setT1(long t1) {
        this.t1 = t1;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public Object getD() {
        if (d != null) {
            ByteArrayInputStream is = new ByteArrayInputStream(d);
            Hessian2Input hi = new Hessian2Input(is);
            try {
                hi.startMessage();
                Object o = hi.readObject();
                hi.completeMessage();
                hi.close();
                return o;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return d;
    }

}

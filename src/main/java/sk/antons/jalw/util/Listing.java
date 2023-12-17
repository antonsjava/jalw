/*
 * Copyright 2015 Anton Straka
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

package sk.antons.jalw.util;

import java.util.Collection;
import java.util.Map;

/**
 * Helper class for listing content of collection, map or array.
 * 
 * @author antons
 */
public class Listing {
    
    private Collection coll = null;
    private Object[] arr = null;
    private Map map = null;
    private PrimitiveArray primitives = null;

    public Listing(Collection coll) {
        this.coll = coll;
    }
    
    public Listing(Object[] arr) {
        this.arr = arr;
    }
    
    public Listing(Map map) {
        this.arr = arr;
    }
    
    public Listing(PrimitiveArray primitives) {
        this.primitives = primitives;
    }

    @Override
    public String toString() {
        if((coll == null) && (arr == null) && (map == null) && (primitives == null)) return null;
        StringBuilder sb = new StringBuilder();
        int num = 0;
        if(coll != null) {
            for(Object object : coll) {
                sb.append("item[").append(num++).append("] = ").append(object).append('\n');
            }
        } else if(arr != null) {
            for(Object object : arr) {
                sb.append("item[").append(num++).append("] = ").append(object).append('\n');
            }
        } else if(map != null) {
            for(Object object : map.entrySet()) {
                Map.Entry entry = (Map.Entry) object;
                sb.append("item[").append(entry.getKey()).append("] = ").append(entry.getValue()).append('\n');
            }
        } else if(primitives != null) {
            for(int i = 0; i < primitives.size(); i++) {
                Object object = primitives.get(i);
                sb.append("item[").append(num++).append("] = ").append(object).append('\n');
            }
        }
        return sb.toString();
    }

    

}

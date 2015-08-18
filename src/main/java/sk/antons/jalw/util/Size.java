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
 * Helper class for displaing size of coolection, map or array. 
 * 
 * @author antons
 */
public class Size {

    private Collection coll = null;
    private Object[] arr = null;
    private Map map = null;
    private PrimitiveArray primitives = null;
    
    public Size(Collection coll) {
        this.coll = coll;
    }
    
    public Size(Object[] arr) {
        this.arr = arr;
    }
    
    public Size(Map map) {
        this.map = map;
    }
    
    public Size(PrimitiveArray primitives) {
        this.primitives = primitives;
    }

    @Override
    public String toString() {
        if(coll != null) return String.valueOf(coll.size());
        else if(arr != null) return String.valueOf(arr.length);
        else if(map != null) return String.valueOf(map.size());
        else if(primitives != null) return String.valueOf(primitives.size());
        return null;
    }

    

}

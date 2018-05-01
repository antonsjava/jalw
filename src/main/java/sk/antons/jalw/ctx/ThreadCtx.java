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
package sk.antons.jalw.ctx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Thread context class. It enables to store some text informations in thread
 * local manner. 
 * 
 * It can be used in systems, where is possible to identify request processing 
 * chain from begen to end. 
 * 
 * Class enable to store thread some traces during execution flow and access 
 * them when necessary. Ussually in some error logging cases.
 * 
 * There is maximum limit of traces to be stored in context. 
 * 
 * @author antons
 */
public class ThreadCtx {
 

    private static ThreadLocal<Data> instance = new ThreadLocal<Data>();

    /**
     * Cleans current thread context. It should be used in the start pf processing chain.
     */
    public static void clean() {
        Data data = instance.get();
        if(data != null) data.clean(20);
        else instance.set(new Data(20));
    }
    
    /**
     * Cleans current thread context. It should be used in the start pf processing chain.
     * @param max sets maximum number of stored values for this thread. (default value is 20)
     */
    public static void clean(int max) {
        Data data = instance.get();
        if(data != null) data.clean(max);
        else instance.set(new Data(max));
    }
    
    /**
     * Add new value for this thread context. (If no maximum limit of stored values 
     * is exceeded.)
     * @param value - new added value 
     */
    public static void add(String value) {
        Data data = instance.get();
        if(data == null) {
            data = new Data(20);
            instance.set(data);
        }
        data.add(value);
    }
    
    /**
     * Returns current copy of thread context stored values.
     * @return thread context in list form
     */
    public static List<String> list() {
        Data data = instance.get();
        if(data == null) {
            data = new Data(20);
            instance.set(data);
        }
        return data.list();
    }
    
    /**
     * Returns current copy of thread context stored values.
     * @return thread context in string form
     */
    public static String string() {
        Data data = instance.get();
        if(data == null) {
            data = new Data(20);
            instance.set(data);
        }
        return data.toString();
    }
    
    /**
     * Returns true if current context is empty.
     * @return true is thread context is empty.
     */
    public static boolean isEmpty() {
        Data data = instance.get();
        if(data == null) return true;
        return data.isEmpty();
    }

    private static class Data {
        private int max = 20;
        private List<String> values = null;
 
        public Data(int max) {
            this.max = max;
            this.values = new ArrayList<String>(max);
        }
        
        public void clean() {
            values.clear();
        }
        
        public void clean(int max) {
            this.values.clear();
            this.max = max;
        }    

        public void add(String value) {
            if(values.size() > max) return;
            values.add(value);
        }

        public List<String> list() {
            return Collections.unmodifiableList(values);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Thread Context [\n");
            for(String value : values) {
                sb.append("  - [").append(value).append("]\n");
            }
            sb.append("]");
            return sb.toString();
        }
        
        public boolean isEmpty() {
            return values.isEmpty();
        }
    }
}

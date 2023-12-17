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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import sk.antons.jalw.Jalw;
import sk.antons.jalw.JalwLogger;

/**
 * Helper class for append like message formatting.
 * 
 * @author antons
 */
public class Appender {
    Jalw jawl = null;
    int level = 0;
    List objects = new ArrayList();

    /**
     * Used by Jalw implementation
     */
    public Appender(Jalw jawl, int level) {
        this.jawl = jawl;
        this.level = level;
    }

    /**
     * Appends object to be logged.
     * @param object - object to be logged
     * @return this instance
     */
    public Appender a(Object object) {
        objects.add(object);
        return this;
    }

    /**
     * Appends collection to be logged. Items will be logged separated by new line.
     * @param value list to be logged
     * @return this instance
     */
    public Appender list(Collection value) {
        objects.add(new Listing(value));
        return this;
    }

    /**
     * Appends map to be logged. Items will be logged separated by new line.
     * @param value map to be logged
     * @return this instance
     */
    public Appender list(Map value) {
        objects.add(new Listing(value));
        return this;
    }

    /**
     * Appends array to be logged. Items will be logged separated by new line.
     * @param value map to be logged
     * @return this instance
     */
    public Appender list(Object[] value) {
        objects.add(new Listing(value));
        return this;
    }

    /**
     * Appends array to be logged. Items will be logged separated by new line.
     * @param value map to be logged
     * @return this instance
     */
    public Appender list(boolean[] value) {
        objects.add(new Listing(new PrimitiveArray(value)));
        return this;
    }

    /**
     * Appends array to be logged. Items will be logged separated by new line.
     * @param value map to be logged
     * @return this instance
     */
    public Appender list(byte[] value) {
        objects.add(new Listing(new PrimitiveArray(value)));
        return this;
    }

    /**
     * Appends array to be logged. Items will be logged separated by new line.
     * @param value map to be logged
     * @return this instance
     */
    public Appender list(char[] value) {
        objects.add(new Listing(new PrimitiveArray(value)));
        return this;
    }

    /**
     * Appends array to be logged. Items will be logged separated by new line.
     * @param value map to be logged
     * @return this instance
     */
    public Appender list(short[] value) {
        objects.add(new Listing(new PrimitiveArray(value)));
        return this;
    }

    /**
     * Appends array to be logged. Items will be logged separated by new line.
     * @param value map to be logged
     * @return this instance
     */
    public Appender list(int[] value) {
        objects.add(new Listing(new PrimitiveArray(value)));
        return this;
    }

    /**
     * Appends array to be logged. Items will be logged separated by new line.
     * @param value map to be logged
     * @return this instance
     */
    public Appender list(long[] value) {
        objects.add(new Listing(new PrimitiveArray(value)));
        return this;
    }

    /**
     * Appends array to be logged. Items will be logged separated by new line.
     * @param value map to be logged
     * @return this instance
     */
    public Appender list(float[] value) {
        objects.add(new Listing(new PrimitiveArray(value)));
        return this;
    }

    /**
     * Appends array to be logged. Items will be logged separated by new line.
     * @param value map to be logged
     * @return this instance
     */
    public Appender list(double[] value) {
        objects.add(new Listing(new PrimitiveArray(value)));
        return this;
    }


    /**
     * Appends collection size to be logged. 
     * @param value collection size to be logged
     * @return this instance
     */
    public Appender size(Collection value) {
        objects.add(value == null?null:value.size());
        return this;
    }
    
    /**
     * Appends map size to be logged. 
     * @param value collection size to be logged
     * @return this instance
     */
    public Appender size(Map value) {
        objects.add(value == null?null:value.size());
        return this;
    }

    /**
     * Appends array size to be logged. 
     * @param value collection size to be logged
     * @return this instance
     */
    public Appender size(Object[] value) {
        objects.add(value == null?null:value.length);
        return this;
    }

    /**
     * Appends array size to be logged. 
     * @param value collection size to be logged
     * @return this instance
     */
    public Appender size(boolean[] value) {
        objects.add(value == null?null:value.length);
        return this;
    }

    /**
     * Appends array size to be logged. 
     * @param value collection size to be logged
     * @return this instance
     */
    public Appender size(byte[] value) {
        objects.add(value == null?null:value.length);
        return this;
    }

    /**
     * Appends array size to be logged. 
     * @param value collection size to be logged
     * @return this instance
     */
    public Appender size(char[] value) {
        objects.add(value == null?null:value.length);
        return this;
    }

    /**
     * Appends array size to be logged. 
     * @param value collection size to be logged
     * @return this instance
     */
    public Appender size(short[] value) {
        objects.add(value == null?null:value.length);
        return this;
    }

    /**
     * Appends array size to be logged. 
     * @param value collection size to be logged
     * @return this instance
     */
    public Appender size(int[] value) {
        objects.add(value == null?null:value.length);
        return this;
    }

    /**
     * Appends array size to be logged. 
     * @param value collection size to be logged
     * @return this instance
     */
    public Appender size(long[] value) {
        objects.add(value == null?null:value.length);
        return this;
    }

    /**
     * Appends array size to be logged. 
     * @param value collection size to be logged
     * @return this instance
     */
    public Appender size(float[] value) {
        objects.add(value == null?null:value.length);
        return this;
    }

    /**
     * Appends array size to be logged. 
     * @param value collection size to be logged
     * @return this instance
     */
    public Appender size(double[] value) {
        objects.add(value == null?null:value.length);
        return this;
    }

    /**
     * Ends message appending and realize logging.
     * @return original jalw instance
     */
    public Jalw log() {
        if(level == JalwLogger.TRACE) jawl.trace(this);
        else if(level == JalwLogger.DEBUG) jawl.debug(this);
        else if(level == JalwLogger.INFO) jawl.info(this);
        else if(level == JalwLogger.WARN) jawl.warn(this);
        else if(level == JalwLogger.ERROR) jawl.error(this);
        else if(level == JalwLogger.FATAL) jawl.fatal(this);
        return jawl;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Object object : objects) {
            sb.append(object);
        }
        return sb.toString();
    }

}

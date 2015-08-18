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
 * Helper class for Jalw static helper classes builder; 
 * 
 * @author antons
 */
public class Show {

    

    /**
     * Lists elements of provided object.
     * @param value - object to be listed
     * @return object listing.
     */
    public Listing list(Collection value) {
        return new Listing(value);
    }

    /**
     * Lists elements of provided object.
     * @param value - object to be listed
     * @return object listing.
     */
    public Listing list(Map value) {
        return new Listing(value);
    }

    /**
     * Lists elements of provided object.
     * @param value - object to be listed
     * @return object listing.
     */
    public Listing list(Object[] value) {
        return new Listing(value);
    }

    /**
     * Lists elements of provided object.
     * @param value - object to be listed
     * @return object listing.
     */
    public Listing list(boolean[] value) {
        return new Listing(new PrimitiveArray(value));
    }

    /**
     * Lists elements of provided object.
     * @param value - object to be listed
     * @return object listing.
     */
    public Listing list(byte[] value) {
        return new Listing(new PrimitiveArray(value));
    }

    /**
     * Lists elements of provided object.
     * @param value - object to be listed
     * @return object listing.
     */
    public Listing list(char[] value) {
        return new Listing(new PrimitiveArray(value));
    }

    /**
     * Lists elements of provided object.
     * @param value - object to be listed
     * @return object listing.
     */
    public Listing list(short[] value) {
        return new Listing(new PrimitiveArray(value));
    }

    /**
     * Lists elements of provided object.
     * @param value - object to be listed
     * @return object listing.
     */
    public Listing list(int[] value) {
        return new Listing(new PrimitiveArray(value));
    }

    /**
     * Lists elements of provided object.
     * @param value - object to be listed
     * @return object listing.
     */
    public Listing list(long[] value) {
        return new Listing(new PrimitiveArray(value));
    }

    /**
     * Lists elements of provided object.
     * @param value - object to be listed
     * @return object listing.
     */
    public Listing list(float[] value) {
        return new Listing(new PrimitiveArray(value));
    }

    /**
     * Lists elements of provided object.
     * @param value - object to be listed
     * @return object listing.
     */
    public Listing list(double[] value) {
        return new Listing(new PrimitiveArray(value));
    }


    /**
     * Shows size to be given object. 
     * @param value - object to be sized
     * @return text with size of the object or null
     */
    public String size(Collection value) {
        if(value == null) return null;
        return String.valueOf(value.size());
    }
    
    /**
     * Shows size to be given object. 
     * @param value - object to be sized
     * @return text with size of the object or null
     */
    public String size(Map value) {
        if(value == null) return null;
        return String.valueOf(value.size());
    }

    /**
     * Shows size to be given object. 
     * @param value - object to be sized
     * @return text with size of the object or null
     */
    public String size(Object[] value) {
        if(value == null) return null;
        return String.valueOf(value.length);
    }

    /**
     * Shows size to be given object. 
     * @param value - object to be sized
     * @return text with size of the object or null
     */
    public String size(boolean[] value) {
        if(value == null) return null;
        return String.valueOf(value.length);
    }

    /**
     * Shows size to be given object. 
     * @param value - object to be sized
     * @return text with size of the object or null
     */
    public String size(byte[] value) {
        if(value == null) return null;
        return String.valueOf(value.length);
    }

    /**
     * Shows size to be given object. 
     * @param value - object to be sized
     * @return text with size of the object or null
     */
    public String size(char[] value) {
        if(value == null) return null;
        return String.valueOf(value.length);
    }

    /**
     * Shows size to be given object. 
     * @param value - object to be sized
     * @return text with size of the object or null
     */
    public String size(short[] value) {
        if(value == null) return null;
        return String.valueOf(value.length);
    }

    /**
     * Shows size to be given object. 
     * @param value - object to be sized
     * @return text with size of the object or null
     */
    public String size(int[] value) {
        if(value == null) return null;
        return String.valueOf(value.length);
    }

    /**
     * Shows size to be given object. 
     * @param value - object to be sized
     * @return text with size of the object or null
     */
    public String size(long[] value) {
        if(value == null) return null;
        return String.valueOf(value.length);
    }

    /**
     * Shows size to be given object. 
     * @param value - object to be sized
     * @return text with size of the object or null
     */
    public String size(float[] value) {
        if(value == null) return null;
        return String.valueOf(value.length);
    }

    /**
     * Shows size to be given object. 
     * @param value - object to be sized
     * @return text with size of the object or null
     */
    public String size(double[] value) {
        if(value == null) return null;
        return String.valueOf(value.length);
    }


}

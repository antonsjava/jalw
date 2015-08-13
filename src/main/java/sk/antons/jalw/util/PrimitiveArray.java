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

/**
 * Helper class for manipulating primitive arrays.
 * @author antons
 */
public class PrimitiveArray {
    private boolean[] booleans = null;
    private byte[] bytes = null;
    private char[] chars = null;
    private short[] shorts = null;
    private int[] ints = null;
    private long[] longs = null;
    private float[] floats = null;
    private double[] doubles = null;

    public PrimitiveArray(boolean[] value) {this.booleans = value;}
    public PrimitiveArray(byte[] value) {this.bytes = value;}
    public PrimitiveArray(char[] value) {this.chars = value;}
    public PrimitiveArray(short[] value) {this.shorts = value;}
    public PrimitiveArray(int[] value) {this.ints = value;}
    public PrimitiveArray(long[] value) {this.longs = value;}
    public PrimitiveArray(float[] value) {this.floats = value;}
    public PrimitiveArray(double[] value) {this.doubles = value;}

    public int size() {
        if(booleans != null) return booleans.length;
        if(bytes != null) return bytes.length;
        if(chars != null) return chars.length;
        if(shorts != null) return shorts.length;
        if(ints != null) return ints.length;
        if(longs != null) return longs.length;
        if(floats != null) return floats.length;
        if(doubles != null) return doubles.length;
        return 0;
    }
    
    public Object get(int pos) {
        if(booleans != null) return booleans[pos];
        if(bytes != null) return bytes[pos];
        if(chars != null) return chars[pos];
        if(shorts != null) return shorts[pos];
        if(ints != null) return ints[pos];
        if(longs != null) return longs[pos];
        if(floats != null) return floats[pos];
        if(doubles != null) return doubles[pos];
        return null;
    }     
}

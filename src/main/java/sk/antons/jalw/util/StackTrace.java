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

import java.rmi.RemoteException;
import java.sql.SQLException;

/**
 * helper class for generating  exception stack trace.
 *
 * @author antons
 */
public class StackTrace {
    private int limit = 0;
    private Throwable throwable = null;

    public StackTrace(Throwable throwable, int limit) {
        this.limit = limit;
        this.throwable = throwable;
    }
    
    
    private String st() {
        if(throwable == null) return null;
        StringBuilder sb = new StringBuilder();
        Throwable t = throwable;
        while(t != null) {
            
            if(sb.length() > 0) sb.append("  Caused by: ");
            sb.append(t).append('\n');
            StackTraceElement[] elements = t.getStackTrace();
            if(elements != null) {
                for(int i = 0; i < elements.length; i++) {
                    if((limit > 0) && (i >= limit)) break;
                    sb.append("\tat ").append(elements[i]).append('\n');
                }
            }

            if(t instanceof SQLException) {
                t = ((SQLException)t).getNextException();
                if(t != null) {
                    if(sb.length() > 0) sb.append("  Caused by: ");
                    sb.append(t).append('\n');
                    elements = t.getStackTrace();
                    if(elements != null) {
                        for(int i = 0; i < elements.length; i++) {
                            if((limit > 0) && (i >= limit)) break;
                            sb.append("\tat ").append(elements[i]).append('\n');
                        }
                    }
                }
            }
            t = t.getCause();
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return st();
    }
    
    
}

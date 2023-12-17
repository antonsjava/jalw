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

package sk.antons.jalw;

/**
 * Factory class for creating Jalw instances for simple system out.
 * It create JalwLogger wrapper for external logging library and use them for 
 * creating Jalw instance. 
 * 
 *
 * @author antons
 */
public class SystemOutJalw {

    /**
     * Creates Jalw instance.
     * @param logger - external library logger instance
     * @return  new creates Jalw instance
     */
    public static Jalw jalw() {
        return new Jalw(new SystemJalwLogger());
    }
    
    private static class SystemJalwLogger implements JalwLogger {

        public boolean isTtraceEnabled() { return true; }
        public boolean isDebugEnabled() { return true; }
        public boolean isInfoEnabled() { return true; }
        public boolean isWarnEnabled() { return true; }
        public boolean isErrorEnabled() { return true; }
        public boolean isFatalEnabled() { return true; }

        public void trace(String msg) { System.out.println("TRC: " + msg); }
        public void debug(String msg) { System.out.println("DBG: " + msg); }
        public void info(String msg) { System.out.println("INF: " + msg); }
        public void warn(String msg) { System.out.println("WRN: " + msg); }
        public void error(String msg) { System.out.println("ERR: " + msg); }
        public void fatal(String msg) { System.out.println("FTL: " + msg); }

        
    }
}

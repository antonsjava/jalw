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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Factory class for creating Jalw instances for Java Sdk logging framework.
 * It create JalwLogger wrapper for external logging library and use them for 
 * creating Jalw instance. 
 * 
 *
 * @author antons
 */
public class SdkJalw {

    /**
     * Creates Jalw instance.
     * @param logger - external library logger instance
     * @return  new creates Jalw instance
     */
    public static Jalw jalw(Logger logger) {
        return new Jalw(new SdkJalwLogger(logger));
    }
    
    private static class SdkJalwLogger implements JalwLogger {

        private Logger logger = null;
        public SdkJalwLogger(Logger logger) {
            this.logger = logger;
        }         
        
        public boolean isTtraceEnabled() { return logger.isLoggable(Level.FINER); }
        public boolean isDebugEnabled() { return logger.isLoggable(Level.FINE); }
        public boolean isInfoEnabled() { return logger.isLoggable(Level.INFO); }
        public boolean isWarnEnabled() { return logger.isLoggable(Level.WARNING); }
        public boolean isErrorEnabled() { return logger.isLoggable(Level.SEVERE); }
        public boolean isFatalEnabled() { return logger.isLoggable(Level.SEVERE); }

        public void trace(String msg) { logger.log(Level.FINER, msg); }
        public void debug(String msg) { logger.log(Level.FINE, msg); }
        public void info(String msg) { logger.log(Level.INFO, msg); }
        public void warn(String msg) { logger.log(Level.WARNING, msg); }
        public void error(String msg) { logger.log(Level.SEVERE, msg); }
        public void fatal(String msg) { logger.log(Level.SEVERE, msg); }

        
    }
}

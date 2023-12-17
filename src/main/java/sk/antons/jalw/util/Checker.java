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

import sk.antons.jalw.Jalw;
import sk.antons.jalw.JalwLogger;

/**
 * Helper class for check Jalw methods.
 *
 * @author antons
 */
public class Checker {
    
    private Jalw jalw = null;
    private boolean condition = false;
    StringBuilder buffer = new StringBuilder();
    
    /**
     * Used by Jalw implementation
     */
    public Checker(Jalw jalw, boolean condition) {
        this.jalw = jalw;    
        this.condition = condition;    
    }

    /**
     * Appends object to Exception text.
     * @param object - object toString() to be appended to exception text
     * @return this instance
     */
    public Checker a(Object object) {
        if(condition) buffer.append(object);
        return this;
    }
    
    private void log(boolean stacktrace, int level) {
        if(!condition) return;
        String text = buffer.toString();
        IllegalStateException e = new IllegalStateException(text);
        if(level == JalwLogger.TRACE) {
            jalw.trace(text);
            if(stacktrace) jalw.trace(e);
        } else if(level == JalwLogger.DEBUG) {
            jalw.debug(text);
            if(stacktrace) jalw.debug(e);
        } else if(level == JalwLogger.INFO) {
            jalw.info(text);
            if(stacktrace) jalw.info(e);
        } else if(level == JalwLogger.WARN) {
            jalw.warn(text);
            if(stacktrace) jalw.warn(e);
        } else if(level == JalwLogger.ERROR) {
            jalw.error(text);
            if(stacktrace) jalw.error(e);
        } else if(level == JalwLogger.FATAL) {
            jalw.fatal(text);
            if(stacktrace) jalw.fatal(e);
        }
        jalw.end("check failed");
        throw e;
    }

    /**
     * Prints Exception to named level and throws exception. No stack trace logged.
     */
    public void trace() { trace(false); }
    /**
     * Prints Exception to named level and throws exception.
     * @param stacktrace - true is stack trace should be logged too
     */
    public void trace(boolean stacktrace) { log(stacktrace, JalwLogger.TRACE); }

    /**
     * Prints Exception to named level and throws exception. No stack trace logged.
     */
    public void debug() { debug(false); }
    /**
     * Prints Exception to named level and throws exception.
     * @param stacktrace - true is stack trace should be logged too
     */
    public void debug(boolean stacktrace) { log(stacktrace, JalwLogger.DEBUG); }

    /**
     * Prints Exception to named level and throws exception. No stack trace logged.
     */
    public void info() { info(false); }
    /**
     * Prints Exception to named level and throws exception.
     * @param stacktrace - true is stack trace should be logged too
     */
    public void info(boolean stacktrace) { log(stacktrace, JalwLogger.INFO); }


    /**
     * Prints Exception to named level and throws exception. No stack trace logged.
     */
    public void warn() { warn(false); }
    /**
     * Prints Exception to named level and throws exception.
     * @param stacktrace - true is stack trace should be logged too
     */
    public void warn(boolean stacktrace) { log(stacktrace, JalwLogger.WARN); }


    /**
     * Prints Exception to named level and throws exception. No stack trace logged.
     */
    public void error() { error(false); }
    /**
     * Prints Exception to named level and throws exception.
     * @param stacktrace - true is stack trace should be logged too
     */
    public void error(boolean stacktrace) { log(stacktrace, JalwLogger.ERROR); }


    /**
     * Prints Exception to named level and throws exception. No stack trace logged.
     */
    public void fatal() { fatal(false); }
    /**
     * Prints Exception to named level and throws exception.
     * @param stacktrace - true is stack trace should be logged too
     */
    public void fatal(boolean stacktrace) { log(stacktrace, JalwLogger.FATAL); }
    

    

    
}

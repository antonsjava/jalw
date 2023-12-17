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
 * Abstraction of real logger. Instances are used by Jalw to access real logging system. 
 * 
 * Interface defines following levels for logging
 * <ul>
 *   <li>trace</li>
 *   <li>debug</li>
 *   <li>info</li>
 *   <li>warn</li>
 *   <li>error</li>
 *   <li>fatal</li>
 * </ul>
 *
 * @author antons
 */
public interface JalwLogger {
    
    public static final int TRACE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int FATAL = 6;
    
    /**
     * Checks if Level is enabled. 
     * @return  true if level is enabled.
     */
    boolean isTtraceEnabled();
    /**
     * Checks if Level is enabled. 
     * @return  true if level is enabled.
     */
    boolean isDebugEnabled();
    /**
     * Checks if Level is enabled. 
     * @return  true if level is enabled.
     */
    boolean isInfoEnabled();
    /**
     * Checks if Level is enabled. 
     * @return  true if level is enabled.
     */
    boolean isWarnEnabled();
    /**
     * Checks if Level is enabled. 
     * @return  true if level is enabled.
     */
    boolean isErrorEnabled();
    /**
     * Checks if Level is enabled. 
     * @return  true if level is enabled.
     */
    boolean isFatalEnabled();
    
    /**
     * Logs message to this level.
     * @param msg - message to be logged
     */
    void trace(String msg);
    /**
     * Logs message to this level.
     * @param msg - message to be logged
     */
    void debug(String msg);
    /**
     * Logs message to this level.
     * @param msg - message to be logged
     */
    void info(String msg);
    /**
     * Logs message to this level.
     * @param msg - message to be logged
     */
    void warn(String msg);
    /**
     * Logs message to this level.
     * @param msg - message to be logged
     */
    void error(String msg);
    /**
     * Logs message to this level.
     * @param msg - message to be logged
     */
    void fatal(String msg);

    
    
}

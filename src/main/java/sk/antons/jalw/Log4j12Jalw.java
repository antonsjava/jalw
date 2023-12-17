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

//import org.apache.log4j.Logger;

/**
 * Factory class for creating Jalw instances for Log4j logging framework.
 * It create JalwLogger wrapper for external logging library and use them for 
 * creating Jalw instance. 
 * 
 *
 * @author antons
 */
public class Log4j12Jalw {

//     /**
//      * Creates Jalw instance.
//      * @param logger - external library logger instance
//      * @return  new creates Jalw instance
//      */
//     public static Jalw jalw(Logger logger) {
//         return new Jalw(new Log4j12JalwLogger(logger));
//     }
//     
//     private static class Log4j12JalwLogger implements JalwLogger {
// 
//         private Logger logger = null;
//         public Log4j12JalwLogger(Logger logger) {
//             this.logger = logger;
//         }         
//         
//         public boolean isTtraceEnabled() { return logger.isDebugEnabled(); }
//         public boolean isDebugEnabled() { return logger.isDebugEnabled(); }
//         public boolean isInfoEnabled() { return logger.isInfoEnabled(); }
//         public boolean isWarnEnabled() { return true; }
//         public boolean isErrorEnabled() { return true; }
//         public boolean isFatalEnabled() { return true; }
// 
//         public void trace(String msg) { logger.debug(msg); }
//         public void debug(String msg) { logger.debug(msg); }
//         public void info(String msg) { logger.info(msg); }
//         public void warn(String msg) { logger.warn(msg); }
//         public void error(String msg) { logger.error(msg); }
//         public void fatal(String msg) { logger.fatal(msg); }
// 
//         
//     }
}

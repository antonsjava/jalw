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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import sk.antons.jalw.util.Appender;
import sk.antons.jalw.util.Checker;
import sk.antons.jalw.util.Show;
import sk.antons.jalw.util.StackTrace;

/**
 * <p>Jalw stands for just another logging wrapper. And this describes it's functionality.
 * Instance of Jalw is created using some 'real' logging logger. This logger is used for 
 * real logging.</p> 
 * 
 * 
 * <p>Many projects uses many different logging frameworks. Slf4j comes with idea how to 
 * unify this. But if you miss some trace logging functionality maybe Jalw helps you.</p>  
 * 
 * 
 * <p>There are following levels mapped to real logging system
 * <ul>
 *   <li>trace</li>
 *   <li>debug</li>
 *   <li>info</li>
 *   <li>warn</li>
 *   <li>error</li>
 *   <li>fatal</li>
 * </ul></p>
 *
 * <p>The levels are mapped to real system using JalwLogger instance.  Jalw instances are 
 * usually created using some factory classes like SdkJalw. 
 * <pre>
 * Jalw jalw = SdkJalw.jalw(log)
 * </pre>
 * It is possible to create your own factory classes to use other logging systems.</p> 
 * 
 * <p>Jalw is lightweight object used for method logging. It brings some methods for 
 * tracing method start and end. 
 * <pre>
 *   Logger logger = Logger.getLogger(MyClass.class);
 *   ...
 *   private void aMethod(String text, int level) {
 *       Jalw jalw = SdkJalw.jalw(logger).start("aMethod", text, level);
 *   ...
 *       jalw.end();
 *   }
 * </pre>
 * It produces trace output 
 * <pre>
 *   >- aMethod [The text param value] [1]  {aMethod - ms: 0}
 *   <- aMethod {aMethod - ms: 0}
 * </pre>
 * </p>
 * 
 * <p>All outputs from Jalw has appended information about method name and time from 
 * creation of Jalw instance. As method is provided using start method. In this way 
 * it is possible to identify which method is source of message and find slow methods 
 * in log files/ </p>
 * 
 * <p>For each level there are some 'classical' logging methods defined
 * <ul>
 *   <li>xxx() - checks if level is enabled for logging</li>
 *   <li>xxx(Object o) - prints provided object</li>
 *   <li>xxx(Throwable o, int limit) - prints stack trace of exception. You can specify 
 *       how meny lines of stack trace will be printed for each exception</li>
 *   <li>xxx(String text, Object o...) - prints formated text in Slf4j like form</li>
 *   <li>xxxf(String text, Object o...) - prints formated text in prointf like form</li>
 *   <li>toXxx() - prints formated text in Appendable like form</li>
 * </ul></p>
 * 
 * <p>Jalw comes with possibility to log history of logging to some higher level. 
 * Methods historyToXxxx() prints previous messages to xxx level. Be carefull, if the 
 * messages was not really logged before and it prints toString() of modified object 
 * it prints it actual state.</p>
 * <p>Normally it is used in case of unexpected exceptions. There is 50 log event cache 
 * enabled by default. This is example of usage 
 * <pre>
 *   } catch (Exception e) {
 *      jalw.historyToError();
 *      jalw.error("Something strange happens").error(e);
 *   }
 * </pre>
 * </p>
 * 
 * <p>Some people like to check input condition of the method and throw exception 
 * if there is something wrong. If you don;t warry that it is IllegalStateException
 * you can use check() method for that
 * <pre>
 *   jalw.checkEmpty(list).a("The input list is empty.").error(true);
 * </pre>
 * This code throw IllegalStateException with text 'The input list is empty.' and
 * logs information about it to error. The parameter true causes also printing of 
 * the exception stacktrace.
 * </p>
 * 
 * @author antons
 */
public class Jalw {
    
    protected static final int TYPE_NONE = 0;
    protected static final int TYPE_SIMPLE = 1;
    protected static final int TYPE_SLF = 2;
    protected static final int TYPE_PRINTF = 3;
    protected static final int TYPE_TOSTRING = 4;
    
    protected JalwLogger logger = null;
    
    protected String methodName = null;
    protected Object[] params = null;
    
    protected List<Event> history = null;
    protected boolean historyNotInitiated = true;
    protected boolean historyEnabled = false;
    protected int historySize = 50;

    protected boolean noMethodInfo = false;
    
    protected long starttime = System.currentTimeMillis();
    
    /**
     * Construct Jalw instance. 
     * @param logger - abstraction of real logger. 
     */
    public Jalw(JalwLogger logger) {
        this.logger = logger;
    } 


    private void addMethodInfo(StringBuilder sb, long time) {
        if(noMethodInfo) return;
        sb.append("  {").append(methodName);
        sb.append(" - ms: ").append(time - this.starttime);
        sb.append('}');    
    }
    
    
    private String format(String msg, Object[] params) {
        if(params != null) {
            StringBuilder sb = new StringBuilder();
            int pos = 0;
            for(Object param : params) {
                int newpos = msg.indexOf("{}", pos);
                if(newpos < 0) break;
                sb.append(msg.substring(pos, newpos));
                sb.append(param);
                pos = newpos + 2;
            }
            sb.append(msg.substring(pos));
            msg = sb.toString();
        }
        return msg;
    }
    
    private String concat(Object[] params) {
        StringBuilder sb = new StringBuilder();
        if(params != null) {
            for(Object param : params) {
                sb.append(param);
            }
        }
        return sb.toString();
    }
    
    private String formatf(String msg, Object[] params) {
        if(params != null) {
            StringBuilder sb = new StringBuilder();
            Formatter f = new Formatter(sb);
            f.format(msg, params);
            msg = sb.toString();
        }
        return msg;
    }
    
    private String format(String msg, Object[] params, Object printable, long time, int type) {
        StringBuilder sb = new StringBuilder();
        if(type == TYPE_TOSTRING) sb.append(printable);
        else if(type == TYPE_SLF) sb.append(format(msg, params));
        else if(type == TYPE_PRINTF) sb.append(formatf(msg, params));
        else sb.append(msg);
        
        if(type != TYPE_NONE) addMethodInfo(sb, time);

        return sb.toString();
    }

    private void addEvent(int level, String msg, Object[] params, Object printable, int type) {
        boolean isLogEnabled = 
               ((level == JalwLogger.TRACE) && logger.isTtraceEnabled())
            || ((level == JalwLogger.DEBUG) && logger.isDebugEnabled())
            || ((level == JalwLogger.INFO) && logger.isInfoEnabled())
            || ((level == JalwLogger.WARN) && logger.isWarnEnabled())
            || ((level == JalwLogger.ERROR) && logger.isErrorEnabled())
            || ((level == JalwLogger.FATAL) && logger.isFatalEnabled())
            ;
        
        long time = System.currentTimeMillis();
        if(isLogEnabled) {
            String message = format(msg, params, printable, time, type);
            msg = message;
            type = TYPE_NONE;
            
            if(level == JalwLogger.TRACE) logger.trace(message);
            else if(level == JalwLogger.DEBUG) logger.debug(message);
            else if(level == JalwLogger.INFO) logger.info(message);
            else if(level == JalwLogger.WARN) logger.warn(message);
            else if(level == JalwLogger.ERROR) logger.error(message);
            else if(level == JalwLogger.FATAL) logger.fatal(message);
        }
       
        if(historyNotInitiated) withHistory();
        if(historyEnabled) {
            if(history.size() < historySize) {
                Event e = new Event();
                e.level = level;
                e.msg = msg;
                e.params = params;
                e.time = time;
                e.type = type;
                e.printable = printable;
                history.add(e);
            }
        }
    }

    private void methodInfo() {
        addEvent(JalwLogger.TRACE, null, params, new MethodInfo(), TYPE_TOSTRING);
    }
    
    private String history() {
        StringBuilder sb = new StringBuilder();
        sb.append(">> --- method history start - " + methodName + " -----------\n");
        for(Event event : history) {
            sb.append(">> --- " + format(event.msg, event.params, event.printable, event.time, event.type) + "\n" );
        }
        sb.append(">> --- method history end  - " + methodName + " -----------");
        return sb.toString();
    }
    

    private static class Event {
        public int level;
        public int type;
        long time = 0;
        
        public String msg;
        public Object[] params;
        
        public Object printable;
    }

    private class MethodInfo {
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(">- ").append(Jalw.this.methodName);
            if(Jalw.this.params != null) {
                for(Object param : Jalw.this.params) {
                    sb.append(" [").append(param).append("]");
                }
            }
            return sb.toString();
        }

        
    }

    /**
     * Prints jalw method history to named level.
     */
    public void historyToInfo() { if(warn()) logger.info(history()); }
    /**
     * Prints jalw method history to named level.
     */
    public void historyToWaen() { if(warn()) logger.warn(history()); }
    /**
     * Prints jalw method history to named level.
     */
    public void historyToError() { if(error()) logger.error(history()); }
    /**
     * Prints jalw method history to named level.
     */
    public void historyToFatal() { if(fatal()) logger.fatal(history()); }
    

    /**
     * Ensures the history storing for 50 log events. Called by default. 
     * @return this Jalw instance
     */
    public Jalw withHistory() {
        return withHistory(50);
    }

    /**
     * Ensures the history storing for specified number of events.
     * @param size - history buffer size
     * @return this Jalw instance
     */
    public Jalw withHistory(int size) {
        historyNotInitiated = false;
        historyEnabled = true;
        historySize = size;
        if(history == null) history = new ArrayList<Event>();
        return this;
    }
    
    /**
     * Stops storing of history events.
     * @return this Jalw instance
     */
    public Jalw withoutHistory() {
        historyNotInitiated = false;
        historyEnabled = false;
        return this;
    }
    
    /**
     * Stops adding method info to logged messages
     * @return this Jalw instance
     */
    public Jalw noMethodInfo() {
        noMethodInfo = true;
        return this;
    }
    
    /**
     * Logs start of the method to trace. It stores method name for other logging.
     * @param methodName - name of started methods
     * @return this Jalw instance
     */
    public Jalw start(String methodName) {
        this.methodName = methodName;
        methodInfo();
        return this;
    } 
    
    /**
     * Logs start of the method to trace. It stores method name for other logging
     * Also given parameter values are logged..
     * @param methodName - name of the started method
     * @param params - params of started method
     * @return this Jalw instance
     */
    public Jalw start(String methodName, Object... params) {
        this.methodName = methodName;
        this.params = params;
        methodInfo();
        return this;
    } 
    
    
    /**
     * Checks possibility of logging for named level.
     * @return true if logging is enabled.
     */
    public boolean trace() {
        return logger.isTtraceEnabled();
    }

    /**
     * Logs given object toString() to named level.
     * @param msg - Object to be logged
     * @return this Jalw instance
     */
    public Jalw trace(Object msg) {
        addEvent(JalwLogger.TRACE, null, null, msg, TYPE_TOSTRING);
        return this;
    }
    
    /**
     * Logs exception stack trace to named level. Also caused by exceptions are logged.
     * @param t - exceptions to be logged
     * @return this Jalw instance
     */
    public Jalw trace(Throwable t) {
        addEvent(JalwLogger.TRACE, null, null, new StackTrace(t, 0), TYPE_TOSTRING);
        return this;
    }
    
    /**
     * Logs exception stack trace to named level. Also caused by exceptions are logged.
     * Only the specified number of lines of each stack trace will be printed.
     * @param t - exceptions to be logged
     * @param limit - number of stack trace lines to be printed
     * @return this Jalw instance
     */
    public Jalw trace(Throwable t, int limit) {
        addEvent(JalwLogger.TRACE, null, null, new StackTrace(t, limit), TYPE_TOSTRING);
        return this;
    }
    
    /**
     * Logs message to named level. Message can specify parameters by substring '{}'. 
     * Parameters will be replaced by real values before logging.  Parameters will be 
     * mapped to real parameters by their order.
     * @param msg - message with parameters placeholders
     * @param params - real parameters. 
     * @return this Jalw instance
     */
    public Jalw trace(String msg, Object... params) {
        addEvent(JalwLogger.TRACE, msg, params, null, TYPE_SLF);
        return this;
    }
    
    /**
     * Logs message to named level. Message can specify parameters. 
     * The Formatter functionality is used for formating result message.
     * Parameters will be replaced by real values before logging.
     * @param msg - message in Formatter format
     * @param params - parameters for formatting message 
     * @return this Jalw instance
     */
    public Jalw tracef(String msg, Object... params) {
        addEvent(JalwLogger.TRACE, msg, params, null, TYPE_PRINTF);
        return this;
    }
 



    /**
     * Checks possibility of logging for named level.
     * @return true if logging is enabled.
     */
    public boolean debug() {
        return logger.isDebugEnabled();
    }
    
    /**
     * Logs given object toString() to named level.
     * @param msg - Object to be logged
     * @return this Jalw instance
     */
    public Jalw debug(Object msg) {
        addEvent(JalwLogger.DEBUG, null, null, msg, TYPE_TOSTRING);
        return this;
    }
    
    /**
     * Logs exception stack trace to named level. Also caused by exceptions are logged.
     * @param t - exceptions to be logged
     * @return this Jalw instance
     */
    public Jalw debug(Throwable t) {
        addEvent(JalwLogger.DEBUG, null, null, new StackTrace(t, 0), TYPE_TOSTRING);
        return this;
    }
    
    /**
     * Logs exception stack trace to named level. Also caused by exceptions are logged.
     * Only the specified number of lines of each stack trace will be printed.
     * @param t - exceptions to be logged
     * @param limit - number of stack trace lines to be printed
     * @return this Jalw instance
     */
    public Jalw debug(Throwable t, int limit) {
        addEvent(JalwLogger.DEBUG, null, null, new StackTrace(t, limit), TYPE_TOSTRING);
        return this;
    }
    
    /**
     * Logs message to named level. Message can specify parameters by substring '{}'. 
     * Parameters will be replaced by real values before logging.  Parameters will be 
     * mapped to real parameters by their order.
     * @param msg - message with parameters placeholders
     * @param params - real parameters. 
     * @return this Jalw instance
     */
    public Jalw debug(String msg, Object... params) {
        addEvent(JalwLogger.DEBUG, msg, params, null, TYPE_SLF);
        return this;
    }
    
    /**
     * Logs message to named level. Message can specify parameters. 
     * The Formatter functionality is used for formating result message.
     * Parameters will be replaced by real values before logging.
     * @param msg - message in Formatter format
     * @param params - parameters for formatting message 
     * @return this Jalw instance
     */
    public Jalw debugf(String msg, Object... params) {
        addEvent(JalwLogger.DEBUG, msg, params, null, TYPE_PRINTF);
        return this;
    }
 



    /**
     * Checks possibility of logging for named level.
     * @return true if logging is enabled.
     */
    public boolean info() {
        return logger.isInfoEnabled();
    }
    
    /**
     * Logs given object toString() to named level.
     * @param msg - Object to be logged
     * @return this Jalw instance
     */
    public Jalw info(Object msg) {
        addEvent(JalwLogger.INFO, null, null, msg, TYPE_TOSTRING);
        return this;
    }
    
    /**
     * Logs exception stack trace to named level. Also caused by exceptions are logged.
     * @param t - exceptions to be logged
     * @return this Jalw instance
     */
    public Jalw info(Throwable t) {
        addEvent(JalwLogger.INFO, null, null, new StackTrace(t, 0), TYPE_TOSTRING);
        return this;
    }
    
    /**
     * Logs exception stack trace to named level. Also caused by exceptions are logged.
     * Only the specified number of lines of each stack trace will be printed.
     * @param t - exceptions to be logged
     * @param limit - number of stack trace lines to be printed
     * @return this Jalw instance
     */
    public Jalw info(Throwable t, int limit) {
        addEvent(JalwLogger.INFO, null, null, new StackTrace(t, limit), TYPE_TOSTRING);
        return this;
    }
    
    /**
     * Logs message to named level. Message can specify parameters by substring '{}'. 
     * Parameters will be replaced by real values before logging.  Parameters will be 
     * mapped to real parameters by their order.
     * @param msg - message with parameters placeholders
     * @param params - real parameters. 
     * @return this Jalw instance
     */
    public Jalw info(String msg, Object... params) {
        addEvent(JalwLogger.INFO, msg, params, null, TYPE_SLF);
        return this;
    }
    
    /**
     * Logs message to named level. Message can specify parameters. 
     * The Formatter functionality is used for formating result message.
     * Parameters will be replaced by real values before logging.
     * @param msg - message in Formatter format
     * @param params - parameters for formatting message 
     * @return this Jalw instance
     */
    public Jalw infof(String msg, Object... params) {
        addEvent(JalwLogger.INFO, msg, params, null, TYPE_PRINTF);
        return this;
    }
 



    /**
     * Checks possibility of logging for named level.
     * @return true if logging is enabled.
     */
    public boolean warn() {
        return logger.isWarnEnabled();
    }
    
    /**
     * Logs given object toString() to named level.
     * @param msg - Object to be logged
     * @return this Jalw instance
     */
    public Jalw warn(Object msg) {
        addEvent(JalwLogger.WARN, null, null, msg, TYPE_TOSTRING);
        return this;
    }
    
    /**
     * Logs exception stack trace to named level. Also caused by exceptions are logged.
     * @param t - exceptions to be logged
     * @return this Jalw instance
     */
    public Jalw warn(Throwable t) {
        addEvent(JalwLogger.WARN, null, null, new StackTrace(t, 0), TYPE_TOSTRING);
        return this;
    }
    
    /**
     * Logs exception stack trace to named level. Also caused by exceptions are logged.
     * Only the specified number of lines of each stack trace will be printed.
     * @param t - exceptions to be logged
     * @param limit - number of stack trace lines to be printed
     * @return this Jalw instance
     */
    public Jalw warn(Throwable t, int limit) {
        addEvent(JalwLogger.WARN, null, null, new StackTrace(t, limit), TYPE_TOSTRING);
        return this;
    }
    
    /**
     * Logs message to named level. Message can specify parameters by substring '{}'. 
     * Parameters will be replaced by real values before logging.  Parameters will be 
     * mapped to real parameters by their order.
     * @param msg - message with parameters placeholders
     * @param params - real parameters. 
     * @return this Jalw instance
     */
    public Jalw warn(String msg, Object... params) {
        addEvent(JalwLogger.WARN, msg, params, null, TYPE_SLF);
        return this;
    }
    
    /**
     * Logs message to named level. Message can specify parameters. 
     * The Formatter functionality is used for formating result message.
     * Parameters will be replaced by real values before logging.
     * @param msg - message in Formatter format
     * @param params - parameters for formatting message 
     * @return this Jalw instance
     */
    public Jalw warnf(String msg, Object... params) {
        addEvent(JalwLogger.WARN, msg, params, null, TYPE_PRINTF);
        return this;
    }
 



    /**
     * Checks possibility of logging for named level.
     * @return true if logging is enabled.
     */
    public boolean error() {
        return logger.isErrorEnabled();
    }
    
    /**
     * Logs given object toString() to named level.
     * @param msg - Object to be logged
     * @return this Jalw instance
     */
    public Jalw error(Object msg) {
        addEvent(JalwLogger.ERROR, null, null, msg, TYPE_TOSTRING);
        return this;
    }
    
    /**
     * Logs exception stack trace to named level. Also caused by exceptions are logged.
     * @param t - exceptions to be logged
     * @return this Jalw instance
     */
    public Jalw error(Throwable t) {
        addEvent(JalwLogger.ERROR, null, null, new StackTrace(t, 0), TYPE_TOSTRING);
        return this;
    }
    
    /**
     * Logs exception stack trace to named level. Also caused by exceptions are logged.
     * Only the specified number of lines of each stack trace will be printed.
     * @param t - exceptions to be logged
     * @param limit - number of stack trace lines to be printed
     * @return this Jalw instance
     */
    public Jalw error(Throwable t, int limit) {
        addEvent(JalwLogger.ERROR, null, null, new StackTrace(t, limit), TYPE_TOSTRING);
        return this;
    }
    
    /**
     * Logs message to named level. Message can specify parameters by substring '{}'. 
     * Parameters will be replaced by real values before logging.  Parameters will be 
     * mapped to real parameters by their order.
     * @param msg - message with parameters placeholders
     * @param params - real parameters. 
     * @return this Jalw instance
     */
    public Jalw error(String msg, Object... params) {
        addEvent(JalwLogger.ERROR, msg, params, null, TYPE_SLF);
        return this;
    }
    
    /**
     * Logs message to named level. Message can specify parameters. 
     * The Formatter functionality is used for formating result message.
     * Parameters will be replaced by real values before logging.
     * @param msg - message in Formatter format
     * @param params - parameters for formatting message 
     * @return this Jalw instance
     */
    public Jalw errorf(String msg, Object... params) {
        addEvent(JalwLogger.ERROR, msg, params, null, TYPE_PRINTF);
        return this;
    }
 



    /**
     * Checks possibility of logging for named level.
     * @return true if logging is enabled.
     */
    public boolean fatal() {
        return logger.isFatalEnabled();
    }
    
    /**
     * Logs given object toString() to named level.
     * @param msg - Object to be logged
     * @return this Jalw instance
     */
    public Jalw fatal(Object msg) {
        addEvent(JalwLogger.FATAL, null, null, msg, TYPE_TOSTRING);
        return this;
    }
    
    /**
     * Logs exception stack trace to named level. Also caused by exceptions are logged.
     * @param t - exceptions to be logged
     * @return this Jalw instance
     */
    public Jalw fatal(Throwable t) {
        addEvent(JalwLogger.FATAL, null, null, new StackTrace(t, 0), TYPE_TOSTRING);
        return this;
    }
    
    /**
     * Logs exception stack trace to named level. Also caused by exceptions are logged.
     * Only the specified number of lines of each stack trace will be printed.
     * @param t - exceptions to be logged
     * @param limit - number of stack trace lines to be printed
     * @return this Jalw instance
     */
    public Jalw fatal(Throwable t, int limit) {
        addEvent(JalwLogger.FATAL, null, null, new StackTrace(t, limit), TYPE_TOSTRING);
        return this;
    }
    
    /**
     * Logs message to named level. Message can specify parameters by substring '{}'. 
     * Parameters will be replaced by real values before logging.  Parameters will be 
     * mapped to real parameters by their order.
     * @param msg - message with parameters placeholders
     * @param params - real parameters. 
     * @return this Jalw instance
     */
    public Jalw fatal(String msg, Object... params) {
        addEvent(JalwLogger.FATAL, msg, params, null, TYPE_SLF);
        return this;
    }
    
    /**
     * Logs message to named level. Message can specify parameters. 
     * The Formatter functionality is used for formating result message.
     * Parameters will be replaced by real values before logging.
     * @param msg - message in Formatter format
     * @param params - parameters for formatting message 
     * @return this Jalw instance
     */
    public Jalw fatalf(String msg, Object... params) {
        addEvent(JalwLogger.FATAL, msg, params, null, TYPE_PRINTF);
        return this;
    }
    



    /**
     * logs information about method end to trace.
     * @return this Jalw instance
     */
    public Jalw end() {
        String msg = "<- " + methodName ;
        addEvent(JalwLogger.TRACE, msg, null, null, TYPE_SIMPLE);
        return this;
    }
    
    /**
     * logs information about method end to trace.
     * @param msg - additional info about reason or place of method end
     * @return this Jalw instance
     */
    public Jalw end(Object msg) {
        String msg2 = "<- " + methodName + " [{}]";
        addEvent(JalwLogger.TRACE, msg2, new Object[]{msg}, null, TYPE_SLF);
        return this;
    }
    
    /**
     * logs information about method end to trace.
     * @param t - information about exception thrown from exception
     * @return this Jalw instance
     */
    public Jalw end(Throwable t) {
        String msg = "<- " + methodName + " [" + t + "]";
        addEvent(JalwLogger.TRACE, null, null, msg, TYPE_TOSTRING);
        return this;
    }
    
    /**
     * logs information about method end to trace.
     * @param msg - additional info about reason or place of method end
     * @param params - parameter for formatting message
     * @return this Jalw instance
     */
    public Jalw end(String msg, Object... params) {
        msg = "<- " + methodName + " [" + msg + "]";
        addEvent(JalwLogger.TRACE, msg, params, null, TYPE_SLF);
        return this;
    }
    
    /**
     * logs information about method end to trace.
     * @param msg - additional info about reason or place of method end
     * @param params - parameter for formatting message
     * @return this Jalw instance
     */
    public Jalw endf(String msg, Object... params) {
        msg = "<- " + methodName + " [" + msg + "]";
        addEvent(JalwLogger.TRACE, msg, params, null, TYPE_PRINTF);
        return this;
    }



    
    
    /**
     * Checks possibility of logging for named level.
     * @return true if logging is enabled.
     */
    public Appender toTrace() {
        return new Appender(this, JalwLogger.TRACE);
    }
    
    public Appender toDebug() {
        return new Appender(this, JalwLogger.DEBUG);
    }
    
    public Appender toInfo() {
        return new Appender(this, JalwLogger.INFO);
    }
    
    public Appender toWarn() {
        return new Appender(this, JalwLogger.WARN);
    }
    
    public Appender toError() {
        return new Appender(this, JalwLogger.ERROR);
    }
    
    public Appender toFatal() {
        return new Appender(this, JalwLogger.FATAL);
    }

    public Checker check(boolean condition) {
        return new Checker(this, condition);
    }
    
    public Checker checkNull(Object oblect) {
        return new Checker(this, oblect == null);
    }
    

    /**
     * Creates an factory instace for some helper functionalities. 
     * Like collection or array size of list.
     * 
     * It van be used like
     * <pre>
     *    jalv.debug("resulting lis is {}", Jalw.show().list(anList));
     * </pre>
     * @return Factory instance
     */
    public static Show show() {
        return new Show();
    }


    public Checker checkEmpty(Object oblect) {
        boolean condition = oblect == null;
        if(!condition && (oblect instanceof String)) condition = "".equals(oblect);
        else if(!condition && (oblect instanceof Map)) condition = ((Map)oblect).isEmpty();
        else if(!condition && (oblect instanceof Collection)) condition = ((Collection)oblect).isEmpty();
        else if(!condition && (oblect instanceof int[])) condition = ((int[])oblect).length == 0;
        else if(!condition && (oblect instanceof byte[])) condition = ((byte[])oblect).length == 0;
        else if(!condition && (oblect instanceof Object[])) condition = ((Object[])oblect).length == 0;
        return new Checker(this, condition);
    }
}

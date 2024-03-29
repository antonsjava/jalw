
# jalw

Jalw stands for just another logging wrapper. And this describes it's functionality.
Instance of Jalw is lightweight wrapper over some 'real' system logging logger. 
This logger is used for real logging. Jalw do not provide any configuration. It 
provides only API.

## Motivation

Many projects uses many different logging frameworks. Slf4j comes with idea how to 
unify this. But if you miss some trace logging functionality maybe Jalw helps you. 

I like to log trace information about method name and method input params. It is 
possible to configure some logging systems to provide this information - but is it 
slow and it is not possible to use it in production environments. 

Other way is to provide method name in each 'log' statement. 
```
  log.debug("::methodName - this is real log message");
```
But it requires some extra effort and it makes copy/paste anti-pattern more 
complicated. When you copy part of the code - you must rename the method also 
in log statements.


## Main features

Jalw helps you with following things.

 - Simplify adding method name information to each log statement. Method 
   name should be provided only once.
 - Provides execution time to each log statement. It simplify finding slow 
   methods.
 - Provides API for formating log messages independent from real logging API 
   used. So you can use your ussual log formatting also in projects where you 
   cannot use logging API you want. 
 - Allows you (in limited way) to log messages, which was not logged before 
   because log level was not allowed. It is usefull in case of some unexpected 
   exceptions.
 - allows you to throws RuntimeException when some method input condition is 
   reached.


## Wrapping

Jalw requires some real logging API for logging. Like Slf4j or ...
You can simply write your own wrapper over your logging API. Jalw brings 
JalwLogger interface, which abstracts the logging API. It requires two method 
for each log level 

 - boolean isXXXEnabled() - which returns true if level XXX is opened for logging.
 - void XXX(String message) - which logs message to XXX level

It simplifies requiremets om logging API, but force Jalw to format logged 
messages to string form. 

Jalw defines following levels of logging

 - trace
 - debug
 - info
 - warn
 - error
 - fatal

All this levels must be somehow mapped to real logging system using instance of 
JalwLogger and it is provided to Jalw for real logging. 

The Jalw provides several implementations for other logging systems. You can use them 
directly or as example for your own implementation 
 - SystemOutJalw https://github.com/antonsjava/jalw/blob/master/src/main/java/sk/antons/jalw/SystemOutJalw.java
 - CommonsJalw https://github.com/antonsjava/jalw/blob/master/src/main/java/sk/antons/jalw/CommonsJalw.java
 - SdkJalw https://github.com/antonsjava/jalw/blob/master/src/main/java/sk/antons/jalw/SdkJalw.java 
 - Log4j12Jalw (not provided anymore) https://github.com/antonsjava/jalw/blob/master/src/main/java/sk/antons/jalw/Log4j12Jalw.java 
(See maven chapter for dependence issues)

All Implementations brings JalwLogger implementation and one factory method for creation Jalw instances.


## Tracing

To trace method info it is necessary to provide method name to Jalw. You can log start of the method 
and of the method in following way
```java
  Logger logger = Logger.getLogger(MyClass.class);
  ...
  private void aMethod(String text, int level) {
    Jalw jalw = SdkJalw.jalw(logger).start("aMethod", text, level);
    if(text == null) {
      jalw.end("The text param is empty");
      return;
    }
  ...
    jalw.end();
  }
```
It produces trace output like 
```
  >- aMethod [The text param value] [1]  {aMethod - ms: 0}
  <- aMethod {aMethod - ms: 0}
```
or like
```
  >- aMethod [null] [1]  {aMethod - ms: 0}
  <- aMethod [The text param is empty] {aMethod - ms: 0}
```
So you can see start and end of the method execution in log file.

You can see text '{aMethod - ms: 0}' in each Jalw log statement automatically so 
it is not necessary to put method name there explicitly. You can find there also 
time from Jalw instance creation to execution of log statement. You can find slow
methods by some log file searching. 


## Logging

For each level you can use several ways for logging. You can use which one you want
Provided examples are for debug level, but it is same for all levels. 

You can ask if logging is enabled
```java
  if(jalw.debug()) {
  ...
  }  
```
You can log simple strings or any object toString().
```java
  jalw.debug("simple string");
  jalw.debug(aVariable);
```
You can log stack trace of exception or limited stack trace
```java
  jalw.debug(anException);
  jalw.debug(anException, 3);
```
You can log formated message using Slf4j format
```java
  jalw.debug("parameter text: {}, parameter index: {}". text, index);
```
You can log formated message using printf format
```java
  jalw.debugf("parameter text: %s, parameter index: %d". text, index);
```
You can log formated message using append like format
```java
  jalw.toDebug().a("parameter text: ").a(text).a(" parameter index: ").a(index).log();
```

## History

in some cases you want to log messages even they are actually not allowed to 
log out. For example you receive exception, you see it in log file because it is logged 
to error level, but you like to know input parameter of method, but it is logged to 
trace which was actually not allowed to log. 

Jalw stores some limited buffer of messages and allows you to print them to other level 
when it was originally logged. Following example shows such usage.
```java
  } catch (Exception e) {
     jalw.historyToError();
     jalw.error("Something strange happens").error(e);
  }
```
There is 50 message budffer enabled for each Jalw instance by default. You can change it in 
following way
```java
      Jalw jalw = SdkJalw.jalw(logger).withoutHistory().start("aMethod"); // no history
      Jalw jalw = SdkJalw.jalw(logger).withHistory(100).start("aMethod"); // change limit
```
If message was not logged because it was not enabled the messages are formated in time of 
logging the history. So all abjects are printed in state as they are in history log time. 
So history can differ from state as if it was logged normally.

## Thread context

In some cases you want to 'store' some traces of execution flow in thread. it is usefull 
when you have code with direct request processing. You can determine start of processing 
for context cleaning. And when you store some 'key' informations usefull for debugging 
application. 

This information is then prointed in historyToError methods or you can process it by your 
own way.

For example you have some REST API interface where you obtain some id of modified object
So you clean context store this information
```java
  jalw.contextClean();
  jalw.context("REST update.{}", path);
```
In other method you can map path to tech {db} id of object to be modified
```java
  jalw.context("DB content id: {}", object.getId());
```
Then in your exception handling code You van obtain previous information in you debug log.
```java
  } catch (Exception e) {
     jalw.historyToError();
  }
```
Or you can priunt it by yourself.
```java
  jalw.info("traces: {}", jalw.contextString());
```


## Input condition check 

If you like to check method input condition and throw an exception and you don;t mind that 
it is IllegalStateException. you can use something like
```java
  jalw.checkEmpty(list).a("The input list is empty.").error(true);
```
This is replacement for 
```java
  if((list == null) || list.isEmpty()) {
    IllegalStateException e = new IllegalStateException("The input list is empty.");
    jalw.error(e.toString());
    jalw.error(e)
    jalw.end()
    throw e;
  }
```
## Example

I hope it is clear what Jalw brings. So if you must use any log API you don't like 
and you are familiar with Jalw API I is necessary to write only one simple class 
and use it for instantiating Jalw. 

This is example for Liferay logging API
```java
import com.liferay.portal.kernel.log.Log;
import sk.antons.jalw.Jalw;
import sk.antons.jalw.JalwLogger;

public class LRJalw {
    public static Jalw jalw(Log logger) {
        return new Jalw(new LRJalwLogger(logger));
    }
    
    private static class LRJalwLogger implements JalwLogger {

        private Log logger = null;
        public LRJalwLogger(Log logger) {
            this.logger = logger;
        }         
        
        public boolean isTtraceEnabled() { return logger.isTraceEnabled(); }
        public boolean isDebugEnabled() { return logger.isDebugEnabled(); }
        public boolean isInfoEnabled() { return logger.isInfoEnabled(); }
        public boolean isWarnEnabled() { return logger.isWarnEnabled(); }
        public boolean isErrorEnabled() { return logger.isErrorEnabled(); }
        public boolean isFatalEnabled() { return logger.isErrorEnabled(); }

        public void trace(String msg) { logger.debug(msg); }
        public void debug(String msg) { logger.debug(msg); }
        public void info(String msg) { logger.info(msg); }
        public void warn(String msg) { logger.warn(msg); }
        public void error(String msg) { logger.error(msg); }
        public void fatal(String msg) { logger.error(msg); }

        
    }
}
```

## Maven usage

```
   <dependency>
      <groupId>io.github.antonsjava</groupId>
      <artifactId>jalw</artifactId>
      <version>LASTVERSION</version>
   </dependency>
```

Jalw has implementation dependences to external libs. They are marked as provided so 
if you want to use Slf4j you must add dependency for Slf4j in your own project. To avoid 
eny version problems exclude all dependences. 

```
   <dependency>
      <groupId>io.github.antonsjava</groupId>
      <artifactId>jalw</artifactId>
      <version>LASTVERSION</version>
      <exclusions>
        <exclusion>
          <groupId>*</groupId>
          <artifactId>*</artifactId>
        </exclusion>
      </exclusions>
   </dependency>
```


## OSGI usage (Karaf)

```
   bundle:install mvn:com.github.antonsjava/jalw/1.1
   bundle:start com.github.antonsjava.jalw/1.1.0
```


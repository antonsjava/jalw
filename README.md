
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

Jalw helps you with following things
 
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

Jalw requires some real logging API for logging. Like Slf4j or Log3j or ...
You can simply write your own wrapper over your logging API. Jalw brings 
JalwLogger interface, which abstracts the logging API. It requires two method 
for each log level 
 - boolean isXXXEnabled() - which returns true if level XXX is opened for logging.
 - void XXXE(String message) - which logs message to XXX level
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
 - Log4j12Jalw https://github.com/antonsjava/jalw/blob/master/src/main/java/sk/antons/jalw/Log4j12Jalw.java 
 - LogbackJalw https://github.com/antonsjava/jalw/blob/master/src/main/java/sk/antons/jalw/LogbackJalw.java 
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

## Maven usage

```
   <dependency>
      <groupId>com.github.antonsjava</groupId>
      <artifactId>jalw</artifactId>
      <version>1.0</version>
   </dependency>
```

Jalw has implementation dependences to external libs. They are marked as provided so if you wan to use 
Slf4j you must add dependency for Slf4j in your own project.


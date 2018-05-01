/*
 * 
 */
package sk.antons.jalw;

import java.util.Arrays;
import java.util.logging.Logger;

/**
 *
 * @author antons
 */
public class ContextTestik {

    private static void startMethod() {
        Logger logger = Logger.getLogger("sk.antons");
        Jalw jalw = null;
        jalw = SdkJalw.jalw(logger).withHistory().start("simple");
        jalw.contextClean().context("start");
        for(int i = 0; i < 100; i++) jalw.debug("simple message");
        jalw.info("done");
        jalw.context("after for loop");
        jalw.historyToError();
        jalw = SdkJalw.jalw(logger).withHistory().start("slsf");
        for(int i = 0; i < 100; i++)    jalw.debug("slsf message {} - {}", "here", 1);
        jalw.info("done");
        jalw.context("after second for loop");
        jalw.historyToError();
        jalw.contextClean();
        jalw = SdkJalw.jalw(logger).withHistory().start("printf");
        for(int i = 0; i < 100; i++)    jalw.debugf("printf message %s - %s", "here", 1);
        jalw.info("done");
        jalw.context("after third for loop");
        jalw.historyToError();

        jalw.toError().a("toto je ").a(logger).a(" - ").size(new String[]{}).log();
        jalw = SystemOutJalw.jalw().withHistory().start("create", "kkk", true);
        jalw.end("param je null");
        
        jalw.historyToError();

        //jalw.check(true).a("nieco sa po...:").info();
        jalw.checkEmpty(new byte[0]).a("nieco sa po...:byte ").a(123).info(true);
    }
        

    
    public static void main(String[] params) {
        startMethod();
    }
    
}

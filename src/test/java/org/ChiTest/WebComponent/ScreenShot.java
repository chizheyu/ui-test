package org.ChiTest.WebComponent;

import org.ChiTest.User.User;
import org.apache.log4j.Logger;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 * Created by dugancaii on 9/9/2014.
 */
public class ScreenShot extends TestWatcher {
    private User shotOwner;
    private String className;
    private static Logger log = Logger.getLogger("screen");
    private  DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public ScreenShot(User user){
        this.shotOwner = user;
    }
    @Override
    protected void failed(Throwable e, Description description) {
        log.info("Test failure, then start screenShot ");
        shotOwner.getPage().getScreenshot(description.getClassName()+ " ： "+description.getMethodName());
    }

}

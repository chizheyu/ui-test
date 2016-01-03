package org.ChiTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

public class MyWebDriverListener implements WebDriverEventListener {

    @Override
    public void afterChangeValueOf(WebElement arg0, WebDriver arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterClickOn(WebElement arg0, WebDriver arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterFindBy(By arg0, WebElement arg1, WebDriver arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterNavigateBack(WebDriver arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterNavigateForward(WebDriver arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterNavigateTo(String arg0, WebDriver arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterScript(String arg0, WebDriver arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeChangeValueOf(WebElement arg0, WebDriver arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeClickOn(WebElement arg0, WebDriver arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeFindBy(By arg0, WebElement arg1, WebDriver arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeNavigateBack(WebDriver arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeNavigateForward(WebDriver arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeNavigateTo(String arg0, WebDriver arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeScript(String arg0, WebDriver arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onException(Throwable arg0, WebDriver driver) {
        //todo 调试的时候就关闭好了
    /*
        java.util.Date currentTime  = new java.util.Date();
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
        String dataString = formatter.format(currentTime);
        java.io.File srcFile = ((org.openqa.selenium.TakesScreenshot)driver).getScreenshotAs(org.openqa.selenium.OutputType.FILE);
        try {

            File screenDir = new File(  "screenFile");
            screenDir.mkdirs();
            java.io.File screenShot = new java.io.File("screenFile",  dataString + ".png");
            org.apache.commons.io.FileUtils.copyFile(srcFile, screenShot);
        }
        catch(java.io.IOException e){
            System.out.println("error message " + e.getMessage());
        }
        */
        /*
         System.out.println("发生异常，原因为：" + arg0.getLocalizedMessage());
         System.out.println("发生异常，原因为：" + arg0.getMessage());
         System.out.println("发生异常，原因为：" + arg0.getSuppressed());
         System.out.println("发生异常，原因为：" + arg0.toString());
         System.out.println("发生异常，原因为：" + arg0.getStackTrace());
         System.out.println("发生异常，原因为：" + arg0.fillInStackTrace());
         */
        //System.out.println("截图以保存至：" + "C:\\Users\\dugancaii\\Desktop\\autoTestPic\\" + dataString + ".png");


    }

}

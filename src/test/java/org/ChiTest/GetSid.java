package org.ChiTest;

import dataReader.ConfigureInfo;
import org.ChiTest.Page.Page;
import org.ChiTest.User.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * Created by dugancaii on 10/16/2014.
 */
public class GetSid {
    private User zombieUser;
    private User ciwangUser;
    private Page zombiePage;
    private Page ciwangPage;
    private static String baseUrl;
    private static String loginUrl;

    @Before
    public void setUp() throws Exception {
        ConfigureInfo ConfigureInfo = new ConfigureInfo(true, true);
        zombieUser = ConfigureInfo.getZombieUser();
        ciwangUser = ConfigureInfo.getCiwangUser();
        loginUrl = ConfigureInfo.getLoginUrl();
        baseUrl = ConfigureInfo.getBaseUrl();
        zombiePage = zombieUser.getPage();
        ciwangPage = ciwangUser.getPage();
        zombiePage.navigate(loginUrl);

    }
    @Test
    public void getSidFromDifferentPlatform() throws InterruptedException {
        List<User> userList = new ArrayList<User>();
        List<String> sidList = new ArrayList<String>();
        userList.add(zombieUser);
        userList.add(ciwangUser);
        setSid(getSid(userList)) ;


    }
    public void setSid(List<String> sidList)  {
        Properties prop = new Properties();
        OutputStream output = null;

        try {

            output = new FileOutputStream("config.properties");

            // set the properties value
            prop.setProperty("stagingCookieValueZombie", sidList.get(0));
            prop.setProperty("stagingCookieValueCiwang", sidList.get(1));

            // save properties to project root folder
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    @Test
    public void getSpecificUserSid() throws InterruptedException {
        getSid(zombiePage,"ciwang","coding0620" );
        //getSid(zombiePage,"test5","123456" );
        //getSid(zombiePage,"czy123","123456" );
        /*
        getSid(ciwangPage,"coding_test60","123456" );
        getSid(zombiePage,"test163hello","123456" );
        getSid(zombiePage,"wangyiA163_-41","123456" );
        getSid(zombiePage,"wangyiA163_-45","123456" );
        getSid(zombiePage,"coding_test44","123456" );
        getSid(zombiePage,"coding_test","123456" );
        getSid(zombiePage,"coding_test46","123456" );
        getSid(zombiePage,"test6", "123456");
        getSid(ciwangPage,"wangyiA163_-31", "123456");
        getSid(zombiePage,"coding_test48@163.com", "123456");
        getSid(zombiePage,"czy", "123456");
        getSid(ciwangPage,"dugancai", "123456");

        getSid(ciwangPage,"coding48", "123456");
        getSid(ciwangPage,"wangyiA163_-40", "123456");
        getSid(ciwangPage,"codingTutorial", "123456");
        */
    }

    public List<String> getSid(List<User> userList) throws InterruptedException {
        List<String> sidList = new ArrayList<String>();
        Page loginPage = zombiePage;

        for(int i = 0; i<userList.size();i++ ) {
            loginPage.setDriver(new EventFiringWebDriver(new FirefoxDriver()));
            loginPage.navigate(loginUrl + "login", "#email");
            loginPage.clearAndSendKeys("#email", userList.get(i).getUserLoginName());
            loginPage.clearAndSendKeys("#password", userList.get(i).getUserPassword());
            loginPage.clickElement(".checkbox label");
            loginPage.clickElement(".button.large");
            Thread.sleep(2000);
            assertEquals("获取" + userList.get(i).getUserLoginName() + "的cookie失败",
                    baseUrl + "user", loginPage.getDriver().getCurrentUrl());
            Cookie cookie = loginPage.getDriver().manage().getCookieNamed("sid");
            System.out.println(userList.get(i).getUserLoginName() + "的sid的值为：" + cookie.getValue());
            sidList.add(cookie.getValue());
        }
        return sidList;
    }
    public String getSid(Page loginPage,  String userLoginName, String password) throws InterruptedException {
        loginPage.setDriver(new EventFiringWebDriver(new ChromeDriver()));
        loginPage.navigate(loginUrl, "#email");
        loginPage.clearAndSendKeys("#email", userLoginName);
        loginPage.clearAndSendKeys("#password", password);
        loginPage.clickElement(".checkbox .remember");
        loginPage.clickElement(".button.large");

        Cookie cookie = loginPage.getDriver().manage().getCookieNamed("sid");
        System.out.println(userLoginName + "的sid的值为：" + cookie.getValue());
        return cookie.getValue();
    }
    @After
    public void tearDown(){
        zombiePage.getDriver().quit();
        ciwangPage.getDriver().quit();
    }
}

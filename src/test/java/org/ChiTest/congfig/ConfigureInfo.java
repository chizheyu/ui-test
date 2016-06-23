package org.ChiTest.congfig;

import org.ChiTest.Page.Page;
import org.ChiTest.User.User;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by dugancaii on 8/15/2014.;
 */

public class ConfigureInfo {
    private org.openqa.selenium.support.events.EventFiringWebDriver zombieDriver;
    private org.openqa.selenium.support.events.EventFiringWebDriver ciwangDriver;
    private  DateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
    protected User zombieUser ;
    protected  User ciwangUser;
    private  String baseUrl;
    private  String loginUrl;
    private int zombieDriverType;
    protected static DesiredCapabilities dCaps;
    protected static ConfigFileReader configFileReader = new ConfigFileReader();
    public int getZomebieDriverType() {
        return zombieDriverType;
    }

    public ConfigureInfo(boolean isUseCiwangUser, int ciwangDriverType, boolean isUseZombieUser, int zomebieDriverType) throws ParseException {
        zombieUser = new User();
        ciwangUser = new User();

        baseUrl = configFileReader.getValue("baseUrl");
        loginUrl = configFileReader.getValue("loginUrl");

        Properties props = System.getProperties();
        if(props.getProperty("os.name").toLowerCase().contains("windows")) {
            System.setProperty("webdriver.chrome.driver", configFileReader.getValue("chromeDriverInWin"));
        }else {
            System.setProperty("webdriver.chrome.driver", configFileReader.getValue("chromeDriverInMac"));
        }

        zombieDriverType = zomebieDriverType ;
        if(isUseZombieUser){
            zombieDriver = new org.openqa.selenium.support.events.EventFiringWebDriver(getDriver(zomebieDriverType));
            zombieDriver.manage().window().maximize();
            zombieUser.setPage(new Page(zombieDriver.getCurrentUrl(), zombieDriver, baseUrl));
        }
        if(isUseCiwangUser) {
            ciwangDriver = new org.openqa.selenium.support.events.EventFiringWebDriver(getDriver(ciwangDriverType));
            ciwangDriver.manage().window().maximize();
            ciwangUser.setPage(new Page(ciwangDriver.getCurrentUrl(), ciwangDriver, baseUrl));
        }
    }

    public ConfigureInfo(boolean isUseCiwangUser, boolean isUseZombieUser ) throws ParseException {
       this(isUseCiwangUser, Integer.parseInt(configFileReader.getValue("ciwangDriver")), isUseZombieUser,
                Integer.parseInt(configFileReader.getValue("zombieDriver")));

    }
    public WebDriver getDriver(int driverType){
            switch (driverType) {
                case 1:
                    return new FirefoxDriver();
                case 2:
                    return new ChromeDriver();
                case 3:
                    dCaps = new DesiredCapabilities();
                    ArrayList<String> cliArgsCap = new ArrayList<String>();
                    cliArgsCap.add("--web-security=false");
                   // cliArgsCap.add("--ssl-certificates-path=Certificates.cer");
                    cliArgsCap.add("--ssl-protocol=any");
                    cliArgsCap.add("--ignore-ssl-errors=true");
                    dCaps.setCapability("takesScreenshot", true);
                    dCaps.setCapability(
                            PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
                    Properties props = System.getProperties();
                    if(props.getProperty("os.name").toLowerCase().contains("windows")) {
                        dCaps.setCapability(
                                PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, configFileReader.getValue("phantomJsInWin"));
                    }else {
                        dCaps.setCapability(
                                PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, configFileReader.getValue("phantomJsInMac"));
                    }
                   // dCaps.setCapability("phantomjs.page.settings.resourceTimeout",60);
                    dCaps.setJavascriptEnabled(true);
                    dCaps.setCapability(CapabilityType.SUPPORTS_ALERTS, true);
                    return new PhantomJSDriver(dCaps);
                default:
                    return null;
            }
    }


    public  String getBaseUrl() {
        return baseUrl;
    }

    public  String getLoginUrl() {
        return loginUrl;
    }

    public  User getCiwangUser() {
        return ciwangUser;
    }

    public  User getZombieUser() {
        return zombieUser;
    }
}

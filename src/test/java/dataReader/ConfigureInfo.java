package dataReader;

import org.ChiTest.Activity.Activity;
import org.ChiTest.MyWebDriverListener;
import org.ChiTest.Page.Page;
import org.ChiTest.User.User;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import reference.ConfigFileReader;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private  Cookie ciwangCookie;
    private  Cookie zombieCookie;
    private int zombieDriverType;
    private String filePath;
    protected static DesiredCapabilities dCaps;
    protected Activity dd;
    protected static ConfigFileReader configFileReader = new ConfigFileReader();
    public int getZomebieDriverType() {
        return zombieDriverType;
    }

    public ConfigureInfo(boolean isUseCiwangUser, int ciwangDriverType, boolean isUseZombieUser, int zomebieDriverType) throws ParseException {
        Date date = df.parse("2017-08-09");
        zombieUser = new User(configFileReader.getValue("zombieUserPostfix"), configFileReader.getValue("zombieUserName"), configFileReader.getValue("zombieUserPassword"), configFileReader.getValue("zombieProject"), configFileReader.getValue("zombieAvator") );
        zombieUser.setEmail(configFileReader.getValue("zombieEmail"));
        ciwangUser = new User(configFileReader.getValue("ciwangUserPostfix"), configFileReader.getValue("ciwangUserName"), configFileReader.getValue("ciwangUserPassword"), configFileReader.getValue("ciwangProject"), configFileReader.getValue("ciwangAvator"));
        ciwangUser.setEmail(configFileReader.getValue("ciwangEmail"));
        int plateformOption = Integer.parseInt(configFileReader.getValue("option"));
        if(plateformOption == 2){
            baseUrl = configFileReader.getValue("baseUrl");
            loginUrl = configFileReader.getValue("loginUrl");
            ciwangCookie = new Cookie(configFileReader.getValue("cookieNameCiwang"),configFileReader.getValue("cookieValueCiwang"), configFileReader.getValue("cookieZoneCiwang"), configFileReader.getValue("cookiePathCiwang"), date);
            zombieCookie = new Cookie(configFileReader.getValue("cookieNameZombie"),configFileReader.getValue("cookieValueZombie"), configFileReader.getValue("cookieZoneZombie"), configFileReader.getValue("cookiePathZombie"), date);
        }
        if(plateformOption == 1) {
            baseUrl = configFileReader.getValue("stagingBaseUrl");
            loginUrl = configFileReader.getValue("stagingLoginUrl");
            ciwangCookie = new Cookie(configFileReader.getValue("stagingCookieNameCiwang"),configFileReader.getValue("stagingCookieValueCiwang"), configFileReader.getValue("stagingCookieZoneCiwang"), configFileReader.getValue("stagingCookiePathCiwang"), date);
            zombieCookie = new Cookie(configFileReader.getValue("stagingCookieNameZombie"),configFileReader.getValue("stagingCookieValueZombie"), configFileReader.getValue("stagingCookieZoneZombie"), configFileReader.getValue("stagingCookiePathZombie"), date);
        }
        if(plateformOption == 3) {
            baseUrl = configFileReader.getValue("localBaseUrl");
            loginUrl = configFileReader.getValue("localLoginUrl");
            ciwangCookie = new Cookie(configFileReader.getValue("localCookieNameCiwang"),configFileReader.getValue("localCookieValueCiwang"), configFileReader.getValue("localCookieZoneCiwang"), configFileReader.getValue("localCookiePathCiwang"), date);
            zombieCookie = new Cookie(configFileReader.getValue("localCookieNameZombie"),configFileReader.getValue("localCookieValueZombie"), configFileReader.getValue("localCookieZoneZombie"), configFileReader.getValue("localCookiePathZombie"), date);
        }
       // System.setProperty("webdriver.chrome.driver","C:\\Users\\dugancaii\\.m2\\repository\\org\\seleniumhq\\selenium\\selenium-chrome-driver\\2.44.0\\selenium-chrome-driver-2.44.0.jar");
        Properties props = System.getProperties();
        if(props.getProperty("os.name").toLowerCase().contains("windows")) {
            System.setProperty("webdriver.chrome.driver", configFileReader.getValue("driverlocation"));
        }else {
            System.setProperty("webdriver.chrome.driver", configFileReader.getValue("driverlocationInMac"));
        }
       // System.setProperty("phantomjs.binary.path", "phantomjs-2.0.0-windows\\phantomjs-2.0.0-windows\\bin");
        zombieDriverType = zomebieDriverType ;
        if(isUseZombieUser){
            zombieDriver = new org.openqa.selenium.support.events.EventFiringWebDriver(getDriver(zomebieDriverType));
            zombieDriver.register(new MyWebDriverListener());
            //zombieDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            //zombieDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
            zombieDriver.manage().window().maximize();
            zombieUser.setPage(new Page(zombieDriver.getCurrentUrl(), zombieDriver, baseUrl));
        }
        if(isUseCiwangUser) {
            ciwangDriver = new org.openqa.selenium.support.events.EventFiringWebDriver(getDriver(ciwangDriverType));
            ciwangDriver.register(new MyWebDriverListener());
            //ciwangDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            //ciwangDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
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
                    cliArgsCap.add("--ssl-protocol=any");
                    cliArgsCap.add("--ignore-ssl-errors=true");
                    dCaps.setCapability("takesScreenshot", true);
                    dCaps.setCapability(
                            PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
                    Properties props = System.getProperties();
                    if(props.getProperty("os.name").toLowerCase().contains("windows")) {
                        dCaps.setCapability(
                                PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "phantomjs.exe");
                    }else {
                        dCaps.setCapability(
                                PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "/usr/local/share/phantomjs-1.9.8-linux-x86_64/bin/phantomjs");
                    }
                   // dCaps.setCapability("phantomjs.page.settings.resourceTimeout",60);
                    dCaps.setJavascriptEnabled(true);
                    dCaps.setCapability(CapabilityType.SUPPORTS_ALERTS, true);
                    return new PhantomJSDriver(dCaps);
                default:
                    return null;
            }
    }
    public   String getFilePath() {
        return filePath;
    }
    public int getPlaterformOption() {
        return Integer.parseInt(configFileReader.getValue("option"));
    }
    public  Cookie getCiwangCookie() {
        return ciwangCookie;
    }

    public  Cookie getZombieCookie() {
        return zombieCookie;
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

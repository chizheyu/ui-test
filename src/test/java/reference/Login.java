package reference;


import org.ChiTest.MyWebDriverListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.Console;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;


public class Login {
    private WebDriver driverBase;


    @Before
    public void setUp() throws Exception {
        driverBase = new FirefoxDriver();
        driverBase.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driverBase.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
        System.out.println("please choose platform :");
        System.out.println("1. staging ");
        System.out.println("2. production");
        Console console = System.console();

    }

    @Test
    public void testLogin() throws Exception {
        org.openqa.selenium.support.events.EventFiringWebDriver driver = new org.openqa.selenium.support.events.EventFiringWebDriver(driverBase);
        driver.register(new MyWebDriverListener());
        Navigation navigation= driver.navigate();
        navigation.to("http://www.coding.net");
        //验证alert框
        java.util.Set<Cookie> cookies = driver.manage().getCookies();

        Cookie[] allCookies = new Cookie[cookies.size()];
        cookies.toArray(allCookies);
        System.out.println("当前cookie集合的数量为：" + cookies.size());
        for (int i = 0; i < allCookies.length; i++) {
            System.out.println("第" + (i +1) + "个cookie的各项属性为：");
            System.out.println("cookie名称        -" + allCookies[i].getName());
            System.out.println("cookie值           -" + allCookies[i].getValue());
            System.out.println("cookie所在域     -" + allCookies[i].getDomain());
            System.out.println("cookie路径        -" + allCookies[i].getPath());
            System.out.println("cookie过期时间  -" + allCookies[i].getExpiry());
            System.out.println("");
        }
        assertNotNull(allCookies[0].getExpiry());


    }

    @After
    public void tearDown() throws Exception {
        driverBase.quit();

    }


}

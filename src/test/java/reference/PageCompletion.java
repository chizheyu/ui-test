package reference;

import org.ChiTest.Page.Page;
import org.ChiTest.User.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.ChiTest.MyWebDriverListener;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by dugancaii on 7/30/2014.
 * 检查页面完整性
 */
public class PageCompletion {
    private WebDriver driverBase;
    private org.openqa.selenium.support.events.EventFiringWebDriver driver;
    private WebDriver.Navigation navigation;
    private String mainUserName;
    protected User mainUser = new User("ciwang", "ciwang", "111222");
    private String baseUrl;
    private String loginUrl;
    private String hrefLink;
    private String srcLink;


    @Before
    public void setUp() throws Exception {
        driverBase = new FirefoxDriver();
        driverBase.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driverBase.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver = new org.openqa.selenium.support.events.EventFiringWebDriver(driverBase);
        driver.register(new MyWebDriverListener());
        navigation= driver.navigate();
        mainUserName = mainUser.getUserName();
        baseUrl = "https://staging.coding.net/";
        // baseUrl = "https://coding.net/";
        loginUrl = baseUrl + "login";
       // mainUser.autoLogin(driver,loginUrl);

        Thread.sleep(1000);
        //添加需要验证的元素，发冒泡，发task操作，发帖子，都要先做一遍
    }
    @Test
    public void testAll()throws Exception {
        String testUrl = baseUrl + "user";
        Set<String> url = new HashSet<String>();
        //研究下如何改造成
        //一个链接是否已测的list，和一个链接与冒泡对应的list
        /*
        Map<String, Checker> map;
        page = pages.get(0);
        Checker checker = map.get(page.getUrl());
        checker.check(page);
        */
        testPersonalCenter(baseUrl, url);


    }


    public void testPersonalCenter(String baseUrl, Set<String> url) throws Exception {
        String testUrl = baseUrl + "user";
        System.out.println(" ");
        System.out.println("测试的url " + testUrl);
        Page personalCenter = new Page(testUrl,driver);
        assertTrue("个人中心我的项目并没有出现",personalCenter.elementIsPresent("#project-list item", null, null));
        hrefLink = personalCenter.getLink("#project-list item");
        String projectName =  personalCenter.getElement("#project-list .project-name").getText();
        projectName.replace("/","/p/");
        projectName = baseUrl + "u/" + projectName;
        System.out.println("projectName: " + projectName);
        System.out.println("hrefLink: " + hrefLink);
        assertEquals("个人中心我的项目链接不正确",hrefLink, projectName);
        url.add(hrefLink);
        //assertTrue("个人中心我的项目图片链接不正确", personalCenter.getLink("#project-list img").contains(baseUrl + "/static/project_icon/"));

        url.add(personalCenter.testLinkAndPresent("section a:nth-child(1)", baseUrl + "user/projects", "个人中心我的项目'查看所有'"));
        url.add(personalCenter.testLinkAndPresent("section a:nth-child(2)", baseUrl + "user/projects/create", "个人中心我的项目'创建项目'"));

        assertTrue("个人中心动态中'我的项目'不存在", personalCenter.elementIsPresent("#project-activities .activity ", null, null));
        personalCenter.clickTabTestPresent("#project-activities .activity ", "个人中心动态中'关注的项目'不存在", ".activity.tabs div:nth-child(2)");
        personalCenter.clickTabTestPresent("#project-activities .activity ","个人中心动态中'关注的人'不存在",".activity.tabs div:nth-child(3)");

        testNavigationBar(personalCenter, url);
        assertTrue("个人中心动态中'冒泡编辑'不存在",personalCenter.elementIsPresent(".feed-editor", null, null));
        assertTrue("个人中心动态中'冒泡界面'不存在",personalCenter.elementIsPresent(".tweet-item", null, null));
        assertTrue("个人中心动态中'侧栏'不存在",personalCenter.elementIsPresent(".small.vertical.inverted", null, null));


    }
    public void testNavigationBar(Page personalCenter, Set<String> url ) throws Exception{
        System.out.println("测试导航栏");
        assertTrue("个人中心动态中'导航栏'不存在", personalCenter.elementIsPresent("#top-menu", null, null));
        url.add(personalCenter.testLinkAndPresent(".left.logo.item.ng-scope", baseUrl + "user", "导航栏的logo"));
        assertTrue("'导航栏'中的'搜索栏'不存在", personalCenter.elementIsPresent("#top-menu .search-input", null, null));
        //personalCenter.testLinkAndPresent("#top-menu .search-input",);
        // 完善导航栏测试



        assertTrue(personalCenter.elementIsPresent("#top-menu .right .dropdown", null, null));
        assertTrue(personalCenter.elementIsPresent(".left.logo.item.ng-scope", null, null));
        assertTrue(personalCenter.elementIsPresent(".left.logo.item.ng-scope", null, null));
    }

    @After
    public void tearDown() throws Exception {

    }
}

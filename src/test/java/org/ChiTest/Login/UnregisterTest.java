package org.ChiTest.Login;

import dataReader.ConfigureInfo;
import org.ChiTest.Email.Email;
import org.ChiTest.Page.Page;
import org.ChiTest.Project.Project;
import org.ChiTest.Topic.Topic;
import org.ChiTest.Topic.TopicFileReader;
import org.ChiTest.User.User;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;
import reference.ConfigFileReader;

/**
 * Created by dugancaii on 3/2/2015.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UnregisterTest {
    private ConfigFileReader fileReader;
    private User zombieUser;
    private org.openqa.selenium.support.events.EventFiringWebDriver driver;
    private Page loginPage;
    private static String baseUrl;
    private Email email163;
    private WebDriver.Navigation navigation;
    private static String loginUrl;

    @Before
    public void setUp() throws Exception {
        ConfigureInfo ConfigureInfo = new ConfigureInfo(false, true);
        zombieUser = ConfigureInfo.getZombieUser();
        fileReader = new ConfigFileReader();
        loginUrl = ConfigureInfo.getLoginUrl();
        baseUrl = ConfigureInfo.getBaseUrl();

        loginPage = zombieUser.getPage();
        /*
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setBrowserName("firefox");
        capabilities.setPlatform(Platform.WIN8_1);
        loginPage.setDriver(new EventFiringWebDriver(new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),capabilities)));
*/
        email163 = new Email(fileReader.getValue("emailName"),fileReader.getValue("postfix"),fileReader.getValue("password"));
        driver = loginPage.getDriver();
        navigation =  loginPage.getDriver().navigate();
        loginPage.navigate(loginUrl);


    }


    @Test
    public void test01_UnregisterRemainder() throws Exception {
        //到 coding 的主页 三个模块 和coding feedback 各个模块
                //收藏的人和分支

        //检查登录提示和登录框是否会出现
        TopicFileReader topicFileReader = new TopicFileReader();
        User unregister = new User("coding", "coding",null);
        unregister.setPage(loginPage);
        Project project = new Project(unregister, "Coding-Feedback");

        loginPage.navigate(project.getProjectCodeLink());
        loginPage.waitLoadingIconInvisible();
        assertNoneHint("项目代码页面");

        loginPage.navigate(project.getProjectCodeBranchLink());
        loginPage.waitLoadingIconInvisible();
        assertNoneHint("项目代码分支页面");

        loginPage.navigate(project.getProjectCodeLink() + "/tags");
        loginPage.waitLoadingIconInvisible();
        assertNoneHint("项目代码标签页面");

        loginPage.navigate(project.getProjectCodeLink() + "/pulls/open");
        loginPage.waitLoadingIconInvisible();
        assertNoneHint("项目代码pull Request页面");


        loginPage.navigate(project.getProjectCodeLink() + "/codeInsight/index");
        loginPage.waitLoadingIconInvisible();
        assertNoneHint("项目代码阅读页面");


        loginPage.navigate(project.getProjectCodeLink() + "/qc");
        loginPage.waitLoadingIconInvisible();
        assertNoneHint("项目的质量管理页面");

        loginPage.navigate(project.getProjectLink() + "/stargazers", ".public-project-view-watchers ");
        assertNoneHint("收藏项目的人页面");

        loginPage.navigate(project.getProjectLink()+"/watchers", ".public-project-view-watchers ");
        assertNoneHint("关注项目的人页面");

        loginPage.navigate(project.getProjectCodeLink()+"/forks");
        loginPage.waitLoadingIconInvisible();
        assertNoneHint("fork项目的人页面");

        loginPage.navigate(unregister.getHomePageLink()+"/projects", "#user-space");
        assertNoneHint("用户个人首页的项目页面");

        loginPage.navigate(unregister.getHomePageLink()+"/bubble", "#user-space");
        assertNoneHint("用户个人首页的冒泡页面");

        loginPage.navigate(unregister.getHomePageLink()+"/activity", "#user-space");
        assertNoneHint("用户个人首页的动态页面");


        loginPage.navigate(unregister.getBubblingLink(), ".bubble-detail");
        assertNoneHint("用户冒泡广场的页面");


        loginPage.navigate(project.getProjectLink() + "/topic");
        assertNoneHint("项目的讨论列表页面");
        if(!loginPage.elementIsPresent(topicFileReader.getItemInItemHome())){
            Topic topic = new Topic(project,"test", "ddddd",unregister);
            topic.createTopic();
            assertNoneHint("项目的某篇讨论页面");
        }else {
            loginPage.clickElement(topicFileReader.getTopicTitleInItemHome(), topicFileReader.getTopicContent());
            assertNoneHint("项目的某篇讨论页面");
        }

        loginPage.clickElement(".watch.button", ".small.modal.login.transition.visible.active");
        loginPage.assertElementPresent("未登录时，点击收藏没有出现弹框提示",  ".small.modal.login.transition.visible.active" );
    }
    public void assertNoneHint(String pageName){
        loginPage.assertElementNotPresent("未登录时，出现了登录提示"+ pageName,".outer");
        loginPage.assertElementNotPresent("未登录时，出现了登录框"+ pageName,".small.modal.login.transition.visible.active" );
    }
    @After
    public void tearDown() throws Exception {
        loginPage.getDriver().quit();
    }
}

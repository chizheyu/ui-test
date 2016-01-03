package org.ChiTest.User;

import dataReader.ConfigureInfo;
import org.ChiTest.Page.Page;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Cookie;

/**
 * Created by dugancaii on 2/5/2015.
 */
public class PersonalHomePageTest {
    private  static String loginUrl;
    protected  static User zombieUser;
    private static Page zombiePage;
    private static Cookie zombieCookie;
    private  static ConfigureInfo configureInfo;


    @BeforeClass
    public static void setUp() throws Exception {
        configureInfo = new ConfigureInfo(false,true);
        zombieUser = configureInfo.getZombieUser();
        zombiePage = zombieUser.getPage();
        zombieCookie = configureInfo.getZombieCookie();
        zombieUser.autoLogin(configureInfo.getLoginUrl(),zombieCookie );

    }
    @Test
    public void PersonalHomePage(){
        zombiePage.navigate(zombieUser.getHomePageLink(), ".coding.aligned.center.user-slogan");
        zombiePage.assertTextEquals("个人首页用户的 slogan 有误", "活在当下，自强不息", ".coding.aligned.center.user-slogan");
        zombiePage.assertTextEquals("个人首页用户的用户名有误", zombieUser.getUserName(), ".coding.aligned.center.user-name");
        zombiePage.assertAttributeContainWords("个人首页用户的用户头像有误", zombieUser.getAvatar(),".wide.column.info-column a img", "src");
        zombiePage.assertTextContainWords("个人首页用户的关注的人有误", "关注",".coding.aligned.center .gray");
        zombiePage.assertCountEquals("个人首页用户加入时间或最后活动时间消失", 2, ".user-created-at");
        zombiePage.assertTextContainWords("个人首页用户的个性后缀有误", "个性后缀："+zombieUser.getUserLoginName(),".user-global-key");

        zombiePage.assertElementPresent("个人首页的展示详细资料失效", ".user-more-info",5);
        zombiePage.assertCountEquals("个人首页的详细资料（性别，公司，工作，地区）缺失", 4, ".user-more-info .info-item");
        zombiePage.assertTextEquals("个人首页的项目栏消失", "项目",".wide.column.content-column .item.active" );
        zombiePage.assertTextEquals("个人首页的 TA参与的 项目栏消失", "TA 参与的",".user-projects  .menu.filter-menu .item.active " );
        zombiePage.assertElementPresent("个人首页的项目列表消失", ".projects .project-info .project-name",5);

        zombiePage.clickElement(".user-projects  .menu.filter-menu .item:nth-child(2)");
        zombiePage.waitForContentChange(".user-projects  .menu.filter-menu .active.item", "TA 参与的",10);
        zombiePage.assertTextEquals("个人首页的 TA收藏的 项目栏消失", "TA 收藏的",".user-projects  .menu.filter-menu .item.active " );
        zombiePage.assertElementPresent("个人首页的 TA收藏的 项目列表消失", ".projects .project-info .project-name",5);

        zombiePage.assertElementPresent("个人首页的 TA收藏的 项目列表消失", ".projects .project-info .project-name",5);
        zombiePage.clickElement(".wide.column.content-column .item:nth-child(2)");
        zombiePage.assertElementPresent("个人首页的 冒泡 列表消失", ".user-tweets ",10);

        zombiePage.clickElement(".wide.column.content-column .item:nth-child(3)");
        zombiePage.waitForElement(".bubble-topic-item",10);
        zombiePage.assertElementPresent("个人首页的 话题 列表消失", ".bubble-topic-item",5);

        zombiePage.clickElement(".wide.column.content-column .item:nth-child(4)");
        zombiePage.assertElementPresent("个人首页的 动态 列表消失", ".user-activities .activity ",10);
    }
    @AfterClass
    public static void tearDown(){
        zombiePage.getDriver().quit();
    }

}

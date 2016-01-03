package org.ChiTest.Friend;

import dataReader.ConfigureInfo;
import org.ChiTest.Email.Email;
import org.ChiTest.Email.EmailTest;
import org.ChiTest.Message.MessageTest;
import org.ChiTest.Notification.Notification;
import org.ChiTest.Notification.NotificationFileReader;
import org.ChiTest.Notification.NotificationTest;
import org.ChiTest.Page.PS;
import org.ChiTest.Page.Page;
import org.ChiTest.PageLink;
import org.ChiTest.User.User;
import org.ChiTest.WebComponent.UserNameCard;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.Cookie;

import java.text.ParseException;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by dugancaii on 9/3/2014.c
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FriendTest {
    protected  static User zombieUser ;
    protected  static User ciwangUser;
    private static Page zombiePage;
    private static Page ciwangPage;
    private static Cookie ciwangCookie;
    private  static Cookie zombieCookie;
    private static String fansInnerItemInitString;
    private static Logger friendLog;
    private static RelationShip zombieFollowCiwang;
    private static RelationShip ciwangFollowZombie;
    private static UserNameCard userNameCard;
    private static Set mailCheckList ;
    private NotificationTest notificationTest;
    private  NotificationFileReader notificationFileReader;
    private static FriendFileReader friendFileReader;
    public FriendTest()  {
        notificationTest = new NotificationTest();
        notificationFileReader = new NotificationFileReader();
    }

    @BeforeClass
    public  static void setup() throws Exception {
        friendLog = Logger.getLogger("friend_log");
        ConfigureInfo ConfigureInfo = new ConfigureInfo(true, true);

        zombiePage = ConfigureInfo.getZombieUser().getPage();
        ciwangPage = ConfigureInfo.getCiwangUser().getPage();
        /*
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setBrowserName("firefox");
        capabilities.setPlatform(Platform.WIN8_1);
        capabilities.setCapability("maxInstances", 3);
        zombiePage = new Page("dfd",new EventFiringWebDriver(new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities)) );

        DesiredCapabilities capability = DesiredCapabilities.firefox();
        capabilities.setBrowserName("firefox1");
        capabilities.setPlatform(Platform.WIN8_1);
        capabilities.setCapability("maxInstances", 2);
        ciwangPage = new Page("",new EventFiringWebDriver(new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capability)));
        */
        zombieUser = new User("wangyiA163_-40","coding500","123456",zombiePage,"Fruit-3.png");
        ciwangUser = new User("czy","coding404","123456",ciwangPage,"Fruit-2.png");

        zombieCookie = zombiePage.getStagingSid("95d1d6b5-4df2-4974-b2f3-d2f3d4862a61");
        ciwangCookie = ciwangPage.getStagingSid("62443776-8180-4dec-b245-93de294d0661");

        zombieUser.autoLogin(ConfigureInfo.getLoginUrl(), zombieCookie);
        ciwangUser.autoLogin(ConfigureInfo.getLoginUrl(), ciwangCookie);

        mailCheckList = new HashSet();

        friendLog.info("自动登录成功开始测试好友模块 ");
        friendFileReader = new FriendFileReader();

        fansInnerItemInitString = "粉丝 ()";
        userNameCard = new UserNameCard();
        zombieFollowCiwang = new RelationShip(zombieUser,ciwangUser);
        ciwangFollowZombie = new RelationShip(ciwangUser,zombieUser);
    }

    @Test
    public void test01_testTurningPageInFollowPage() throws InterruptedException {
        ciwangPage.testPageTurning(ciwangPage.getFriendsUrl());
        switchPageWithTab(ciwangPage);
    }

    @Test
    public void test02_testAddFollow() throws InterruptedException, ParseException {
        ciwangFollowZombie.cancelFollow();
        zombieFollowCiwang.cancelFollow();
        followSomeoneInFollowPage(ciwangFollowZombie);

        List<PageLink> friendLink = new ArrayList<>();
        Notification followUser = new Notification(notificationFileReader.getFriendIconClass(),ciwangUser
                ,ciwangUser.getUserName() + " 关注了你", zombieUser,friendLink);
        notificationTest.verifyForOneLinkFormat(followUser, "关注某人的通知", notificationFileReader.getSystemItemIcon());

        followSomeoneInFansPage(zombieUser,ciwangUser);

        mailCheckList.add(followUser.getContent());
        (new EmailTest()).checkEmail(zombiePage, mailCheckList, new Email("coding_test48","163.com","chi2014"));

    }
    @Test
    public void test04_testCancelFollow() throws InterruptedException, ParseException {
        ciwangFollowZombie.followSomeone();
        zombieFollowCiwang.followSomeone();
        cancelFollowInFansPage(ciwangUser, zombieUser);
        cancelFollowInFollowPage(zombieFollowCiwang);
    }


    @Test
    public void test03_testSendMessageToFollow() throws InterruptedException {
        MessageTest messageTest = new MessageTest();
        ciwangPage.refresh(ciwangPage.getFriendsUrl(), ".black.button");
        ciwangPage.clickElement(".black.button", "#send-message-modal");
        assertTrue("关注页面点击私信吗没有弹框",ciwangPage.elementIsPresent("#send-message-modal",5) );
        ciwangPage.clickElement("#send-message-modal .close");
        ciwangPage.getWaitTool().waitForJavaScriptCondition("return $(\"#send-message-modal\").css(\"display\") == \"none\" ", 5);
        ciwangPage.clickElement("#inner-menu .item:nth-child(3)");
        ciwangPage.getWaitTool().waitForJavaScriptCondition("return $(\"#relationship .header\").text()==\"我的粉丝\" ", 5);
        messageTest.sendHimMessageTest(zombieUser,ciwangUser,"hisMessage :"+ciwangPage.getMark(),".black.button");
    }

    public void switchPageWithTab(Page ciwangPage){
        ciwangPage.navigate(ciwangPage.getRelationCenterUrl(), friendFileReader.getFansInnerItem());

        ciwangPage.clickElement(friendFileReader.getFansInnerItem());
        ciwangPage.assertElementPresent("点击关系中心的 “粉丝” tab， 无法切换到粉丝页面 ", friendFileReader.getUserNameInNameCard(), 5);
        assertEquals("粉丝页面，页面标题有误", "我的粉丝", ciwangPage.getPageHeader());

        ciwangPage.clickElement(friendFileReader.getFriendsInnerItem());
        ciwangPage.assertElementPresent("点击关系中心的 “关注” tab， 无法切换到关注页面 ",friendFileReader.getUserNameInNameCard(),5);
        assertEquals("关注页面，页面标题有误", "我关注的人", ciwangPage.getPageHeader());
    }

    public int getFansCountFromInnerItem(Page fansPage){
        fansPage.waitForContentChange(friendFileReader.getFansInnerItem(), fansInnerItemInitString,5);
        return fansPage.getPageNum(friendFileReader.getFansInnerItem());
    }

    public void cancelFollowInFollowPage(RelationShip relationShip) throws InterruptedException {
        User fans = relationShip.getFans();
        User star = relationShip.getStar();

        assertNotEquals(fans.getUserName() + " 已经关注 " + star.getUserName() + " 相互取消关注的预先操作失败，关注功能无法测试",
                    -1, relationShip.cancelFollow());

        Page fansPage = fans.getPage();
        fansPage.refresh(fansPage.getFriendsUrl(), friendFileReader.getUserNameInNameCard());

        assertEquals("关系中心的关注页面，取消关注的用户, 刷新页面后又出现了,后端没有更新", -1,
                getUserCardNum(fansPage, star.getUserLoginName()));
    }

    public void cancelFollowInFansPage(User fans, User star) throws InterruptedException {
        Page starPage = star.getPage();
        starPage.refresh(starPage.getFansUrl(),friendFileReader.getUserNameInNameCard());
        int formerFansCount = getFansCountFromInnerItem(starPage);

        Page fansPage = fans.getPage();
        fansPage.navigate(fansPage.getFansUrl(), friendFileReader.getCancelFollowButton());

        int formerFollowsCount = getFollowerCountFromInnerItem(fansPage);
        String followsCountText = fansPage.getText( friendFileReader.getFriendsInnerItem() );
        userNameCard.unFollow(fansPage, getUserCardNum(fansPage, star.getUserLoginName()));
        fansPage.waitForContentChange(friendFileReader.getFriendsInnerItem(), followsCountText, 5);
        fansPage.assertNumInTextEquals("取消关注后，页面上显示的关注人数没有减一", formerFollowsCount - 1, friendFileReader.getFriendsInnerItem());
        assertEquals("点击取消关注按钮后，没有马上关注", "马上关注",
                fansPage.getText(fansPage.getElement(friendFileReader.getUserNameCard(),
                        getUserCardNum(fansPage, star.getUserLoginName())), friendFileReader.getFollowButton()));

        fansPage.refresh();
        assertEquals("关系中心的关注页面，取消关注某用户后，后端用户没更新", formerFollowsCount - 1, getFollowerCountFromInnerItem(fansPage));

        verifyCancelFollowInStarPage(starPage,fans, formerFansCount);
    }
    public void verifyCancelFollowInStarPage(Page starPage, User fans, int baseFansNumInPage) throws InterruptedException {
        starPage.refreshWithoutWait(starPage.getFansUrl());
        starPage.waitForElement(friendFileReader.getUserNameInNameCard(), PS.midWait);
        if(getUserCardNum(starPage, fans.getUserLoginName()) != -1){
            assertEquals("ciwang取消对zombie的关注后，zombie依然能够找粉丝里找到ciwang",1,2);
        }
        assertEquals("取消关注后，粉丝数没有减一", baseFansNumInPage - 1, getFansCountFromInnerItem(starPage) );
    }


    public void followSomeoneInFollowPage(RelationShip relationShip) throws InterruptedException, ParseException {
        User fans = relationShip.getFans();
        User star = relationShip.getStar();

        assertNotEquals(fans.getUserName() + " 已经关注 " + star.getUserName() + " 相互取消关注的预先操作失败，关注功能无法测试",
                  -1, relationShip.followSomeone());

        Notification followSomeOne = new Notification(notificationFileReader.getFriendIconClass(),fans,fans.getUserName() + " 关注了你",star);
        notificationTest.verifyForOneLinkFormat(followSomeOne,"某人被关注的通知 ",notificationFileReader.getSystemItemIcon());

        Page fansPage = fans.getPage();
        fansPage.refresh(friendFileReader.getUserNameInNameCard());
        assertEquals("关系中心的关注页面，刷新后, 消失了，后端未更新 ",fansPage.getAttribute(friendFileReader.getUserNameInNameCard(), "title"),star.getUserName());

        userNameCard.checkFollowedIcon(fansPage, fansPage.getElement(friendFileReader.getUserNameCard()));
        userNameCard.verifyJoinTime(fansPage, fansPage.getElement(friendFileReader.getUserNameCard()));

    }
    public void followSomeoneInFansPage(User fans, User star) throws InterruptedException {
        Page starPage = star.getPage();
        starPage.refresh(starPage.getFansUrl(),friendFileReader.getUserNameInNameCard());
        int baseFansNumInPage = getFansCountFromInnerItem(starPage);

        Page fansPage = fans.getPage();
        fansPage.navigate(fansPage.getFansUrl());

        int baseFollowsNumInPage = getFollowerCountFromInnerItem(fansPage);
        String baseFollowsTextNumInPage = fansPage.getText(friendFileReader.getFriendsInnerItem());

        userNameCard.follow(fansPage, getUserCardNum(fansPage,star.getUserLoginName()));

        fansPage.waitForContentChange( friendFileReader.getFriendsInnerItem(), baseFollowsTextNumInPage , 5);
        fansPage.assertNumInTextEquals("关系中心的粉丝页面，关注某用户后，页面左侧的关注数不正确，可能是关注的用户，没有在关注页面上出现", baseFollowsNumInPage + 1,
                friendFileReader.getFriendsInnerItem());

        userNameCard.checkFollowEachIcon(fansPage, fansPage.getElement(friendFileReader.getUserNameCard(), getUserCardNum(fansPage, star.getUserLoginName())));

        fansPage.refresh(friendFileReader.getFansInnerItem());
        fansPage.assertElementPresent("关系中心的粉丝页面，关注后，后端未更新操作", friendFileReader.getUserNameCard(),
                getUserCardNum(fansPage, star.getUserLoginName()), ".action span .exchange");

        starVerifyNewFollowedFans(starPage, fans, baseFansNumInPage);
    }
    public void starVerifyNewFollowedFans(Page starPage, User fans, int baseFansNumInPage) throws InterruptedException {
        starPage.refresh(starPage.getFansUrl(),friendFileReader.getUserNameInNameCard());
        if (getUserCardNum(starPage, fans.getUserLoginName()) == -1) {
            assertEquals("ciwang对zombie的关注后，zombie不能够找粉丝里找到ciwang", 1, 2);
        }
        assertEquals("被关注后，粉丝数没有加一", baseFansNumInPage + 1, getFansCountFromInnerItem(starPage) );
    }
    public int getFollowerCountFromInnerItem(Page page){
        page.waitForContentChange(friendFileReader.getFriendsInnerItem(), "关注 ()", 5);
        return page.getPageNum(friendFileReader.getFriendsInnerItem());
    }
    private int getUserCardNum(Page page, String uerLoginName) throws InterruptedException {
        return page.findItemBySign(uerLoginName,friendFileReader.getUserNameInNameCard(),page.getLinkContainFinder() );
    }
    @AfterClass
    public static void tearDown() throws Exception {
        zombiePage.getDriver().quit();
        ciwangPage.getDriver().quit();
    }

}

package org.ChiTest.Notification;

import dataReader.ConfigureInfo;
import org.ChiTest.Bubbling.Bubbling;
import org.ChiTest.LinksMap;
import org.ChiTest.Page.Page;
import org.ChiTest.PageLink;
import org.ChiTest.User.User;
import org.junit.*;
import org.openqa.selenium.Cookie;
import org.ChiTest.WebComponent.ScreenShot;

import java.text.ParseException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by dugancaii on 8/18/2014.
 */
public class NotificationTest {
    private static Page zombiePage;
    private static User zombieUser;
    private static User ciwangUser;
    private static Cookie ciwangCookie;
    private  static Cookie zombieCookie;
    private NotificationFileReader notificationFileReader;
    private LinksMap linksMap;
    public NotificationTest()  {
        notificationFileReader = new NotificationFileReader();
        linksMap = LinksMap.getInstance();
    }
    @BeforeClass
    public static void setUp() throws Exception {
        ConfigureInfo ConfigureInfo = new ConfigureInfo(true,true);
        zombieUser = ConfigureInfo.getZombieUser();
        ciwangUser = ConfigureInfo.getCiwangUser();
        zombiePage = ConfigureInfo.getZombieUser().getPage();
        ciwangCookie = ConfigureInfo.getCiwangCookie();
        zombieCookie = ConfigureInfo.getZombieCookie();
        zombieUser.autoLogin(ConfigureInfo.getLoginUrl(),zombieCookie );
        ciwangUser.autoLogin( ConfigureInfo.getLoginUrl(),ciwangCookie);
    }
    @Rule
    public ScreenShot screenShotZombie = new ScreenShot(zombieUser);
    @Rule
    public ScreenShot screenShotCiwang = new ScreenShot(ciwangUser);

    @Test
    public void test01_unreadNotificationTest() throws InterruptedException, ParseException {
        zombiePage.navigate(zombieUser.getUserNotificationLink(), notificationFileReader.getReadAll());
        zombiePage.checkAlert(notificationFileReader.getReadAll(), "标记所有通知为已读的按钮或alert弹框有问题，建议确认和取消都试下",
                "这将会把您所有接收到的通知标记为已读（包括没有在本页显示的），确定继续？");
        assertTrue("准备测试时，标记所有通知为已读的按钮失效", zombiePage.waitElementInvisible(notificationFileReader.getUnreadCount()));
        Bubbling bubbling = new Bubbling(ciwangUser, "hello @zombie_debug " + zombiePage.getMark());
        bubbling.sendBubbling();
        bubbling.setContent("bye @zombie_debug " + zombiePage.getMark());
        bubbling.sendBubblingWithTimeInterval();

        zombiePage.refresh(notificationFileReader.getNotificationContent());
        int unReadCountInTotalPage = zombiePage.getPageNum(notificationFileReader.getUnreadCount());
        int unReadCountInUnreadPage = getUnreadNumber(zombiePage, notificationFileReader.getUnreadItemIcon(),zombiePage.getText(notificationFileReader.getActiveItemText()));
        assertEquals("所有通知页面和未读通知页面的数值不一致",unReadCountInTotalPage, unReadCountInUnreadPage);

        int unReadCountInAtPage = getUnreadNumber(zombiePage, notificationFileReader.getAtSomeoneItemIcon(),zombiePage.getText(notificationFileReader.getActiveItemText()));
        assertEquals("提到我的通知页面和未读通知页面的数值不一致",unReadCountInAtPage, unReadCountInUnreadPage);

        int unReadCountAfterClick = getNumberAfterClick(zombiePage);
        assertEquals("提到我的通知页面点击未读通知后，未读通知数没有减一",unReadCountInAtPage - 1, unReadCountAfterClick);
        assertEquals("所有通知页面点击未读通知后，未读通知数没有减一",getUnreadNumber(zombiePage,  notificationFileReader.getTotalItemIcon(),zombiePage.getText(notificationFileReader.getActiveItemText())), unReadCountAfterClick);
        assertEquals("未读通知页面点击未读通知后，未读通知数没有减一",getUnreadNumber(zombiePage,  notificationFileReader.getUnreadItemIcon(),zombiePage.getText(notificationFileReader.getActiveItemText())), unReadCountAfterClick);

        String notificationTitle = zombiePage.getText(notificationFileReader.getPageTitle());
        zombiePage.clickElement(notificationFileReader.getReadThisPage());
        zombiePage.waitForContentChange(notificationFileReader.getPageTitle(),notificationTitle,5);
        assertFalse("未读通知页面标记本页未读的功能失效", zombiePage.elementIsPresent(notificationFileReader.getUnreadCount(), 5));
        zombiePage.refresh(notificationFileReader.getNoThing());
        assertEquals("未读通知页面在没有通知的情况下，未显示‘无数据’", zombiePage.getText(notificationFileReader.getNoThing()), "无数据");
    }
    public int getNumberAfterClick(Page zombiePage){
        String notificationTitle = zombiePage.getText(notificationFileReader.getPageTitle());
        zombiePage.clickElement(notificationFileReader.getNotificationTime());
        zombiePage.waitForContentChange(notificationFileReader.getPageTitle(),notificationTitle,5);
        return zombiePage.getPageNum(notificationFileReader.getUnreadCount());
    }
    public int  getUnreadNumber(Page zombiePage, String selectorForItem, String itemText){
        zombiePage.clickElement(selectorForItem);
        zombiePage.getWaitTool().waitForJavaScriptCondition("return $(\""+notificationFileReader.getActiveItemText()+"\").text().indexOf(\""+ itemText +"\") == -1",5);
        return zombiePage.getPageNum(notificationFileReader.getUnreadCount());
    }

    public void verifyForOneLinkFormat(Notification notification, String messageHint, String classificationSelector) throws  InterruptedException {
        Page page = notification.getReceiver().getPage();
        page.refresh(notification.getReceiver().getUserNotificationLink(), notificationFileReader.getNotificationContent());
        page.assertTextEqualsOneFromThreeExpect(messageHint + "通知时间出错", "几秒前", "几秒内", "1分钟前", notificationFileReader.getNotificationTime(),0);
        assertTrue(messageHint + "通知的图标显示有误 ", page.getElement(notificationFileReader.getNotificationIcon()).getAttribute("class").contains(notification.getIcon()));
        assertEquals(messageHint + "通知的发送者的名字出错", notification.getSponsor().getUserName(), page.getText(notificationFileReader.getNotificationSponsor()));
        page.checkUserSpaceLink(notification.getSponsor().getUserName(), notificationFileReader.getNotificationSponsor(), notification.getSponsor().getHomePageLink());
        assertEquals(messageHint+ "通知内容出错", notification.getContent(),page.getText(notificationFileReader.getNotificationContent()).trim());
        String activeItemText = page.getText(notificationFileReader.getActiveItemText());
        page.clickElement(classificationSelector);
        page.waitForContentChange(notificationFileReader.getActiveItemText(),activeItemText,10);
        assertEquals(messageHint+ "通知分类出错", notification.getContent(),page.getText(notificationFileReader.getNotificationContent()).trim());
    }

    public void verifyForMultiLinkFormat(Notification notification, String messageHint, String classificationSelector) throws  InterruptedException {
        Page page = notification.getReceiver().getPage();
        verifyForOneLinkFormat(notification, messageHint,classificationSelector);
        List<PageLink> pageLinks = notification.getPageLinks();
        //将通知改造成 多个link的模式
        for(int i = 0; i < pageLinks.size(); i++) {
            assertTrue("通知的第"+ (i+2) +"个链接可能出错", page.getLink(pageLinks.get(i).getLinkSelector()).startsWith(pageLinks.get(i).getLinkContent()));
            linksMap.addLink(pageLinks.get(i).getLinkContent(), pageLinks.get(i));
        }

    }

    @AfterClass
    public static void tearDown(){
        zombiePage.getDriver().quit();
        ciwangUser.getPage().getDriver().quit();
    }

}

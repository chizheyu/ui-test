package org.ChiTest.Message;

import dataReader.ConfigureInfo;
import org.ChiTest.Page.Page;
import org.ChiTest.User.User;
import org.ChiTest.WebComponent.MarkDownInputBox;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.ChiTest.WebComponent.ScreenShot;

import javax.naming.directory.NoSuchAttributeException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by dugancaii on 8/1/2014.
 * 私信测试
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MessageTest {
    protected  static User zombieUser;
    protected  static User ciwangUser;
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    private static String baseUrl;
    private  static Cookie ciwangCookie;
    private static  Cookie zombieCookie;
    private Date creatTime;
    private static Message sessionZombieToCiwang;
    private static Message sessionCiwangToZombie;
    private static Page ciwangPage;
    private static Page zombiePage;
    //设置日期格式
    private static MessageFileReader messageFileReader;
    private static  ConfigureInfo configureInfo;
    public MessageTest() {
        messageFileReader = new MessageFileReader();
    }

    @BeforeClass
    public static void setUp() throws Exception {
        configureInfo = new ConfigureInfo(true, true);
        zombieUser = configureInfo.getZombieUser();
        ciwangUser = configureInfo.getCiwangUser();

        baseUrl = configureInfo.getBaseUrl();

        ciwangCookie = configureInfo.getCiwangCookie();
        zombieCookie = configureInfo.getZombieCookie();

        zombieUser.autoLogin(configureInfo.getLoginUrl(),zombieCookie );
        ciwangUser.autoLogin(configureInfo.getLoginUrl(),ciwangCookie);

        ciwangPage = ciwangUser.getPage();
        zombiePage = zombieUser.getPage();

        //若已有这样的session，则点击，让其未读数减去
        sessionZombieToCiwang = new Message(ciwangUser, zombieUser);
        sessionCiwangToZombie = new Message(zombieUser,ciwangUser);

        messageFileReader = new MessageFileReader();
        int sessionNum =  findSessionBySenderName(sessionZombieToCiwang);
        if(sessionNum != -1){
            sessionZombieToCiwang.getReceiver().getPage().clickElement(".event .content ", sessionNum);
        }
        sessionNum =  findSessionBySenderName(sessionCiwangToZombie);
        if(sessionNum != -1){
            sessionCiwangToZombie.getReceiver().getPage().clickElement(".event .content ", sessionNum);
        }
    }
    @Rule
    public ScreenShot screenShotZombie = new ScreenShot(zombieUser);
    @Rule
    public ScreenShot screenShotCiwang = new ScreenShot(ciwangUser);


    @Test
    public void test01_SendMessageAndVerifyMessage() throws InterruptedException, ParseException, NoSuchAttributeException {
        MarkDownInputBox markDownInputBox = new MarkDownInputBox(messageFileReader.getMessageMarkDownBox());
        ciwangUser.setMessageNotReadBaseNum(getNotReadNumber(ciwangUser));
        ciwangUser.setMessageInSessionBaseNum(getSessionNum(sessionZombieToCiwang));

        creatTime = sendMessage(ciwangUser, zombieUser, markDownInputBox);

        ciwangUser.setMessageInSessionNum(getSessionNum(sessionZombieToCiwang));
        assertEquals("会话的对话数量没有加1",ciwangUser.getMessageInSessionBaseNum() + 1, ciwangUser.getMessageInSessionNum() );//验证发来的信息
        testReplyMessage(ciwangUser, zombieUser, creatTime,markDownInputBox);
    }

    @Test
    public void test02_SendMessageAndVerifyMessageInSpecificSession() throws InterruptedException, ParseException, NoSuchAttributeException {
        MarkDownInputBox markDownInputBox = new MarkDownInputBox(messageFileReader.getMessageMarkDownBoxInSession());
        zombieUser.setMessageNotReadBaseNum(getNotReadNumber(zombieUser));
        creatTime = replayMessage(zombieUser,ciwangUser,markDownInputBox);
        testReplyMessage(zombieUser, ciwangUser, creatTime,markDownInputBox);
    }
    @Test
    public void test03_SendSampleMessageAndDeleteMessage() throws ParseException, InterruptedException {
        Message sampleMessage = new Message(zombieUser,ciwangUser, "https://www.baidu.com");
        for(int i = 0; i < 3; i++) {
            sampleMessage.sendSampleMessage();
        }
        sampleMessage.verifySampleMessage();

        deleteMessage(sessionCiwangToZombie);
    }

    @Test
    public void test04_PageTurning(){
        testMessagePageTurning(ciwangUser, zombieUser);
    }
    @Test
    public void test05_MessageRefresh(){
        Message message = new Message(ciwangUser, zombieUser, "testRefresh");
        ciwangPage.refresh(ciwangUser.getMessageLinkWithSomeone(zombieUser), messageFileReader.getMessageContentInSessionPage());
        message.sendSampleMessage();
        ciwangPage.clickElement(messageFileReader.getMessageRefreshIconInSessionPage());
        ciwangPage.waitLoadingIconInvisible();
        assertEquals("私信自动刷新按钮，刷新失败", message.getContent(), ciwangPage.getText(messageFileReader.getMessageContentInSessionPage()));
    }
    public void testMessagePageTurning(User receiver, User sender){
        Page receiverPage = receiver.getPage();
        receiverPage.navigate(receiver.getAllUserMessageLink(), ".event");
        List<WebElement> sessions = receiverPage.getElements(".event");
        int sessionNum = -1;
        for(int i = 0; i < sessions.size();i++){
            if(Integer.parseInt(sessions.get(i).findElement(By.cssSelector(".date span")).getText())>11){
                sessionNum = i;
                break;
            }
        }
        if (sessionNum == -1) {
            return;
        }
        receiverPage.clickElement(sessions.get(sessionNum), ".page span");
        receiverPage.testPageTurning(receiver.getMessageLinkWithSomeone(sender));
    }
    public void sendHimMessageTest(User receiver, User sender, String content,String messageButtonSelector) throws InterruptedException {
        Page messagePage = sender.getPage();
        messagePage.clickElement(messageButtonSelector);
        messagePage.sendKeys(messageFileReader.getMessageContentInput(),content );
        messagePage.clickElement(messageFileReader.getMessageConfirmButtonInBox());
        messagePage.verifyHint("发送成功！");
        Page receiverPage = receiver.getPage();
        receiverPage.navigate(receiver.getAllUserMessageLink(),
                messageFileReader.getMessageSenderNameInSessionList());

        assertEquals("私信的发送者有误",sender.getUserName(),receiverPage.getText(messageFileReader.getMessageSenderNameInSessionList()));
        assertEquals("会话显示的最后一条信息内容不正确，关系中心的“给他私信”功能有误", content, receiverPage.getText(".content .summary span").trim());
    }


    public static int findSessionBySenderName(Message session) throws InterruptedException {
        Page page = session.getReceiver().getPage();
        page.refresh(session.getReceiver().getAllUserMessageLink(),messageFileReader.getMessageSendButtonInMessagePage());
        if(!page.elementIsPresent(".event .content ")){
            return -1;
        }
        return page.findItemInOnePageByTextEquals(messageFileReader.getMessageSenderNameInSessionList(), session.getSender().getUserName() );

    }
    public int getSessionNum(Message session) throws InterruptedException {
        int  elementNUm =  findSessionBySenderName(session);
        if(elementNUm != -1){
            return Integer.parseInt(session.getReceiver().getPage().getText(".date span",elementNUm));
        }
        else{
            return 0;
        }
    }
    public int getNotReadNumber(User user) throws InterruptedException {
        Page userPage = user.getPage();
        userPage.refresh(baseUrl + "user/messages/basic", "#inner-menu .item:nth-child(3)");
        userPage.getWaitTool().waitForJavaScriptCondition("return $(\"#inner-menu .item:nth-child(3)\").text().length == 6",5);
        return userPage.getPageNum("#inner-menu .item:nth-child(3)");
    }
    public void deleteMessage( Message session) throws InterruptedException {
        Page messagePage = session.getReceiver().getPage();
        User receiver = session.getReceiver();
        messagePage.refresh(receiver.getAllUserMessageLink(), "#inner-menu .item:nth-child(3)");
        messagePage.getWaitTool().waitForJavaScriptCondition("return $(\"#inner-menu .item:nth-child(3)\").text().length == 6",5);
        int messageNotReadNumBeforeDelete = messagePage.getPageNum("#inner-menu .item:nth-child(3)", "[^0-9]|/");
        messagePage.moveToElement(".event .content");
        messagePage.checkAlert(".event .remove","删除会话的提示有误","这将删除你和 " + session.getSender().getUserName() +" 的所有会话，确定继续？");
        messagePage.getWaitTool().waitForJavaScriptCondition("return $(\"#inner-menu .item:nth-child(3)\").text() != \"未读 ("+ messageNotReadNumBeforeDelete +")\"",10);
        assertEquals("删除和某人的私信会话后，页面未读私信数变化出错，删除一个既有未读私信又有已读私信的会话可重现",
                messagePage.getPageNum("#inner-menu .item:nth-child(3)", "[^0-9]|-|/") + receiver.getMessageReceivedNum(), messageNotReadNumBeforeDelete);
        receiver.resetMessageReceivedNum();
    }

    public void verifyReceiverAlert(Page messagePage){
        messagePage.clickElement(".messages  .button",messageFileReader.getMessageReceiverNameInput());
        messagePage.clearAndSendKeys(messageFileReader.getMessageReceiverNameInput(), "ciwang");
        messagePage.clickElement(messageFileReader.getMessageConfirmButtonInBox());
        messagePage.verifyHint("请输入私信内容！");

        messagePage.clearAndSendKeys(messageFileReader.getMessageReceiverNameInput(), "zombieTestAndTest");
        messagePage.sendKeys(messageFileReader.getMessageContentInput(), "hello");
        messagePage.clickElement(messageFileReader.getMessageConfirmButtonInBox());
        messagePage.verifyHint("无法找到用户:zombieTestAndTest");
    }


    public Date sendMessage(User receiver, User sender, MarkDownInputBox markDownInputBox) throws InterruptedException, ParseException {
        Page messagePage = sender.getPage();
        messagePage.navigate(messagePage.getBaseUrl() + "user","#top-menu .inbox.item .mail");
        messagePage.clickElement("#top-menu .inbox.item .mail",".messages  .button");

        markDownInputBox.cancelAndCloseBox(messagePage, ".messages  .button", messageFileReader.getMessageBoxCancelButton(), "发送框'取消'按钮失灵");
        markDownInputBox.cancelAndCloseBox(messagePage, ".messages  .button", messageFileReader.getMessageBoxCloseButton(), "发送框'关闭'按钮失灵");

        verifyReceiverAlert(messagePage);

        messagePage.clearAndSendKeys(messageFileReader.getMessageReceiverNameInput(), "@ciwa");
        assertEquals("私信接收者输入框里@用户的提示不正确", "ciwang", messagePage.getAttribute(".cur", "data-value"));

        messagePage.clearAndSendKeys(messageFileReader.getMessageReceiverNameInput(), receiver.getUserName());
        markDownInputBox.sendEmojiThroughButton(messagePage,
                ".menu.ui.transition.visible .tab:nth-child(2)");
        markDownInputBox.sendMarkdownContent(messagePage, sender, null,markDownInputBox.getNormalImageSender());
        markDownInputBox.testMarkDownPreview(messagePage,messageFileReader.getMessagePreviewContent());

        messagePage.clickElement(messageFileReader.getMessageConfirmButtonInBox());
        messagePage.waitElementInvisible(".loading.icon");
        messagePage.verifyHint("发送成功！");

        receiver.addMessageReceivedNum();

        return  (new Date());
    }
    public void testReplyMessage(User receiver,User sender, Date createTime, MarkDownInputBox markDownInputBox) throws InterruptedException, ParseException,NoSuchAttributeException {
        Page messagePage = receiver.getPage();
        messagePage.refresh(receiver.getAllUserMessageLink(),"#inner-menu .item:nth-child(3)" );

        messagePage.getWaitTool().waitForJavaScriptCondition("return $(\"#inner-menu .item:nth-child(3)\").text().length == 6", 5);
        Thread.sleep(1500);
        receiver.setMessageNotReadNum(messagePage.getPageNum("#inner-menu .item:nth-child(3)"));
        assertEquals("私信未读计数有误", receiver.getMessageNotReadNum(),receiver.getMessageNotReadBaseNum() + receiver.getMessageReceivedNum());

        assertEquals("私信的发送者有误",sender.getUserName(),messagePage.getText(messageFileReader.getMessageSenderNameInSessionList()));
        //assertEquals("私信发送者的链接有误", sender.getHomePageLink(), messagePage.getElement(messageFileReader.getMessageSenderNameInSessionList()).getAttribute("href"));
        assertTrue("私信的创建时间有误",messagePage.verifyCreateTime(new Date(),".content time","yyyy-MM-dd HH:mm:ss"));
        messagePage.clickElement("#inner-menu .item:nth-child(3)");
        assertFalse("未读页面有已读私信", messagePage.elementIsPresent(".event[class='event ng-scope read']", 2));


        messagePage.assertTextContainWords("会话显示的最后一条信息内容不正确",
                "[+1] [听音乐] "+markDownInputBox.getShortForBaseContent()+" [图片] "+sender.getMessageMark()+"@coding [代码]",
                ".content .summary span");
        String unreadMessageCountText = messagePage.getText("#inner-menu .item:nth-child(3)");
        //点击进入会话页面
        messagePage.clickElement(".event .content ",".message-text");
        receiver.resetMessageReceivedNum();
        messagePage.waitForContentChange("#inner-menu .item:nth-child(3)", unreadMessageCountText,20);
        assertTrue("私信回复页面的创建时间有误",messagePage.comparePageTime(createTime, ".list.messages time"));

        markDownInputBox.verifyMarkdownContent(messagePage,"[正文]", messageFileReader.getMessageContentInSessionPage(),false);

        receiver.setMessageNotReadNum(messagePage.getPageNum("#inner-menu .item:nth-child(3)"));
        Thread.sleep(1500);
        assertEquals("点击进入会话页面, 私信未读计数有误 原来的未读计数为"+ unreadMessageCountText , receiver.getMessageNotReadNum(),receiver.getMessageNotReadBaseNum());
        messagePage.clickElement("#inner-menu .ng-binding");
        messagePage.getWaitTool().waitForJavaScriptCondition("return $(\".messages h3\").text().indexOf(\"未读私信\") != -1",5);
        List<WebElement> sessions = messagePage.getElements(".event.ng-scope");
        if (sessions == null )       {
            assertEquals("未显示未读私信的提示信息","没有未读私信：）",messagePage.getElement(".nothing.ng-binding").getText());
        }
        else{
            for (WebElement session : sessions) {
                assertTrue("未读私信页面出现已读私信", session.getAttribute("class").equals("event ng-scope"));
            }
        }
        System.out.println("私信验证完毕");
    }


    public Date replayMessage(User receiver ,User sender,MarkDownInputBox markDownInputBox) throws InterruptedException, ParseException {
        Page messagePage = sender.getPage();
        messagePage.navigate(baseUrl + "user/messages/history/" + receiver.getUserLoginName(), ".form textarea");

        markDownInputBox.sendEmojiThroughButton(messagePage,  ".transition.visible .tab:nth-child(2)");
        markDownInputBox.sendMarkdownContent(messagePage,sender,null,markDownInputBox.getNormalImageSender());
        markDownInputBox.testMarkDownPreview(messagePage, messageFileReader.getMessagePreviewContentInSession());

        int messageCount = messagePage.getElements(".detail").size();
        messagePage.backToTop();
        messagePage.clickElement(".positive ");
        messagePage.getWaitTool().waitForJavaScriptCondition("return $('.send').text()!=\"发送\"",5);
        messagePage.getWaitTool().waitForItemCountChange(".detail",messageCount,5);
        assertTrue("在回复私信页面发送私信出错", messagePage.getText(".detail").contains(sender.getMessageMark()) );
        messagePage.refresh(".messages .date");
        assertTrue("在回复私信页面发送私信出错", messagePage.getText(".detail").contains(sender.getMessageMark()));
        receiver.addMessageReceivedNum();
        return  new Date();
    }

    @AfterClass
    public static void tearDown(){
        ciwangUser.getPage().getDriver().quit();
        zombieUser.getPage().getDriver().quit();
    }
}

package org.ChiTest.Bubbling;

import dataReader.ConfigureInfo;
import org.ChiTest.Email.EmailTest;
import org.ChiTest.Friend.FriendFileReader;
import org.ChiTest.Friend.RelationShip;
import org.ChiTest.Notification.Notification;
import org.ChiTest.Notification.NotificationFileReader;
import org.ChiTest.Notification.NotificationTest;
import org.ChiTest.Page.PS;
import org.ChiTest.Page.Page;
import org.ChiTest.PageLink;
import org.ChiTest.User.User;
import org.ChiTest.WebComponent.MarkDownInputBox;
import org.ChiTest.WebComponent.SimpleCommentBox;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.experimental.categories.Category;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.ChiTest.WebComponent.ScreenShot;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BubblingTest {
    private  String specialSign = "<>《》~,.`1!@#$%&()_+|-='；‘、、/-+-+。，\\、：“‘”’——……+=-|！@#￥$%……&*（）{}【】？\"";
    protected  static User zombieUser ;
    protected  static User ciwangUser;
    private static Page zombiePage;
    private static Page ciwangPage;
    private  static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    private  static String baseUrl;
    private static  String autoLink;
    private static Cookie ciwangCookie;
    private  static Cookie zombieCookie;
    private static SimpleCommentBox firstCommentBox;
    private static String bubblingMark;
    private static NotificationTest notificationTest;
    private static NotificationFileReader notificationFileReader;
    private static MarkDownInputBox bubblingSendBox;
    private BubblingFileReader bubblingFileReader;
    private static BubblingOperator zombieBubblingOperator;
    private static String chinesePrefix = "早上起来，老婆在我的书堆上放了一本这个...";
    private static Logger log = Logger.getLogger("bubblingTest");
    public  static int notificationMark = (int )(new Date()).getTime()/(1000000);
    public  BubblingTest() throws ParseException {
        bubblingFileReader = new BubblingFileReader();
    }

    @BeforeClass()
    public  static void setup() throws Exception {
        ConfigureInfo ConfigureInfo = new ConfigureInfo(true, true);
        if(2 == ConfigureInfo.getPlaterformOption()  ){
            System.out.println("do not use coding.net to do test");
            return;
        }
        zombieUser = ConfigureInfo.getZombieUser();
        ciwangUser = ConfigureInfo.getCiwangUser();

        baseUrl = ConfigureInfo.getBaseUrl();

        zombiePage = zombieUser.getPage();
        ciwangPage = ciwangUser.getPage();

        ciwangCookie = ConfigureInfo.getCiwangCookie();
        zombieCookie = ConfigureInfo.getZombieCookie();

        zombieUser.autoLogin(ConfigureInfo.getLoginUrl(),zombieCookie );
        ciwangUser.autoLogin( ConfigureInfo.getLoginUrl(),ciwangCookie);
        bubblingMark = df.format(new Date());

        zombieBubblingOperator = new BubblingOperator(zombiePage);
        firstCommentBox = new SimpleCommentBox(".bubble-form.ui.small.form",".comment-item" );
        bubblingSendBox = new MarkDownInputBox(".feed-editor",".bubble-item",ciwangUser);
        autoLink = "https://coding.net/u/coding/p/Coding-Feedback/topic";

        notificationTest = new NotificationTest();
        notificationFileReader = new NotificationFileReader();
    }

    @Test
    public void test01_BubblingTooFastAndSendWithKeyBoard() throws InterruptedException, ParseException {
        RelationShip zombieFollowCiwang = new RelationShip(zombieUser, ciwangUser);
        RelationShip ciwangFollowZombie = new RelationShip(ciwangUser, zombieUser);
        zombieFollowCiwang.followSomeone();
        ciwangFollowZombie.followSomeone();

        Bubbling bubbling =  new Bubbling(zombieUser ,"h" + zombiePage.getMark());
        bubbling.sendBubblingWithTimeInterval();
        zombiePage.verifyHint("发表成功");


        zombieBubblingOperator.sendBubblingContentToInputBox("h");
        zombieBubblingOperator.clickBubblingSendButton();
        assertTrue("冒泡太快的提示可能消失  "  , zombiePage.verifyHint("冒泡太快了，歇歇吧"));
        Thread.sleep(10000);

        assertTrue("冒泡发送框旁边的头像消失了", zombiePage.elementIsPresent("#tweet-list .avatar img", 5));
        zombieBubblingOperator.sendBubblingContentToInputBox("hello coding12" + df.format(new Date()));
        int bubblingCount = zombiePage.getElementCount(bubblingFileReader.getBubblingTextContent());
        zombiePage.getBuilder().keyDown(Keys.CONTROL).sendKeys(Keys.ENTER).perform();
        zombiePage.getBuilder().keyUp(Keys.CONTROL).sendKeys(Keys.NULL).perform();
        zombiePage.waitForItemCountChange(bubblingFileReader.getBubblingTextContent(), bubblingCount,10);
        zombiePage.assertCountEquals("用按键发冒泡不成功", bubblingCount + 1, bubblingFileReader.getBubblingTextContent());
    }
    @Test
    public void test03_AutoAndManualRefresh() throws InterruptedException, IOException {
        Thread.sleep(10000);
        ciwangPage.navigate(ciwangPage.getBubblingUrl());
        Bubbling bubbling = new Bubbling(zombieUser,"hello coding12" + df.format(new Date()) );
        bubbling.sendBubbling();

        String bubblingText = ciwangPage.getText(bubblingFileReader.getBubblingTextContent());
        ciwangPage.clickElement(".refresh.icon");
        ciwangPage.waitLoadingIconInvisible();
        ciwangPage.assertTextNotEquals("手动刷新失败", bubblingText, bubblingFileReader.getBubblingTextContent());
    }
    @Category(BaseBubbling.class)
    @Test
    public void test04_RepeatBubbling() throws InterruptedException {
        log.info("测试重复发冒泡");
        Thread.sleep(10000);
        zombiePage.navigate(zombiePage.getBubblingUrl());
        Bubbling bubbling = new Bubbling(zombieUser,df.format(new Date()) );
        bubbling.sendBubbling();

        Thread.sleep(10000);
        zombieBubblingOperator.sendBubblingContentToInputBox(bubbling.getContent());
        zombieBubblingOperator.clickBubblingSendButton();
        assertTrue("不能重复发布冒泡的提示可能出错 errorUrl "  , zombiePage.verifyHint("不要重复发布冒泡哦"));
    }
    @Rule
    public ScreenShot screenShotZombie = new ScreenShot(zombieUser);
    @Rule
    public ScreenShot screenShotCiwang = new ScreenShot(ciwangUser);
    @Test
    public void test02_SensitiveAndFilterWords() throws InterruptedException {
        zombiePage.navigate(zombiePage.getBubblingUrl(),".feed-editor textarea" );
        zombieBubblingOperator.sendBubblingContentToInputBox("\nFUCK\n" );
        zombieBubblingOperator.clickBubblingSendButton();
        assertTrue("冒泡过滤敏感字符如\"fuck\"的提示或功能失败 "  , zombiePage.verifyHint("内容中包含敏感字符\"fuck\""));

        zombieBubblingOperator.sendBubblingContentToInputBox("\nfuck\n" );
        zombieBubblingOperator.clickBubblingSendButton();
        assertTrue("冒泡过滤敏感字符如\"fuck\"的提示或功能失败 "  , zombiePage.verifyHint("内容中包含敏感字符\"fuck\""));
    }
    @Category(AdvancedBubbling.class)
    @Test
    public void test05_BubblingFavour() throws InterruptedException {
        Bubbling bubbling = new Bubbling(ciwangUser, "bubblingFavour"+df.format(new Date()));
        bubbling.sendBubbling();
        int favourBaseNum = Integer.parseInt( ciwangPage.getText(bubblingFileReader.getBubblingFavorCount()));
        favourOperatorTest(ciwangPage, favourBaseNum + 1);
        favourOperatorTest(ciwangPage, favourBaseNum);
        fastClickBubblingFavour(ciwangPage);
    }
    @Test
    public void test06_1_PreviewAndPostBubbling() throws ParseException, InterruptedException {
        zombiePage.navigate(zombiePage.getBubblingUrl(),".feed-editor");
        zombiePage.backToTop();
        bubblingSendBox.sendEmojiThroughButton(zombiePage, ".transition.visible .tab:nth-child(2)");
        bubblingSendBox.sendKeys(zombiePage, chinesePrefix);
        bubblingSendBox.getNormalImageSender().sendImgInInput(zombiePage, "#tweet-list .insert_img");
        bubblingSendBox.getNormalImageSender().sendImgInInput(zombiePage, "#tweet-list .insert_img");
        bubblingSendBox.verifyCanAtSomeoneHint(zombiePage);
        bubblingSendBox.verifyEmojiHint(zombiePage, ":+1");
        bubblingSendBox.verifyEmojiHint(zombiePage, ":听音乐");
        emojiAndPicturePreviewTest(bubblingSendBox);

        zombiePage.backToTop();
        bubblingSendBox.clickMDConfirmButton(zombiePage);
        zombiePage.waitLoadingIconInvisible();

        bubblingThumbnailTest();
        verifyBubblingPictureAndEmoji();
        bubblingPictureViewTest();

        zombiePage.navigate(zombieUser.getUserHomePageBubblingLink(), bubblingFileReader.getImageInBubbling());
        bubblingThumbnailTest();
        bubblingPictureViewTest();
    }

    @Test
    public void test06_0_PreviewAndPushBubbling() throws Exception {
        zombiePage.navigate(zombiePage.getBubblingUrl(),".feed-editor");
        bubblingSendBox.clearAndSendKeys(zombiePage, specialSign +
                "\nhttps://coding.net/u/coding/p/Coding-Feedback/topic。。。" +
                "\nhttps://coding.net/u/coding/p/Coding-Feedback/topic可以" +
                "\n*  Coding.net有以下主要功能\n*Coding，让开发更简单*\n_Coding，让开发更简单_\n" +
                "**Coding，让开发更简单**\n__Coding，让开发更简单__\n" +
                "[超强大的云开发平台Coding](http://coding.net)\n" +
                "- Red\n\n1. Red\n\n>>这是二级引用\n<html>\n## h2 " + df.format(new Date()));
        bubblingSendBox.clickCodeIcon(zombiePage);
        bubblingSendBox.sendKeys(zombiePage, "#include <stdio.h>");

        bubblingPreviewTest(bubblingSendBox);

        bubblingSendBox.clickMDConfirmButton(zombiePage, bubblingFileReader.getBubblingItem());
        verifyBubblingInfo();
    }
    private void bubblingPictureViewTest() throws InterruptedException {
        zombiePage.clickElement(bubblingFileReader.getImageInBubbling(), bubblingFileReader.getGalleryImage());
        zombiePage.assertElementPresent("冒泡中的图片点击预览失效", bubblingFileReader.getGalleryImage(),5);
        String imgUrl = zombiePage.getAttribute(bubblingFileReader.getGalleryImage(),"src");
        zombiePage.assertAttributeContainWords("冒泡中的图片点击预览后显示图片 url 有误", zombiePage.getImgStoreUrl(), bubblingFileReader.getGalleryImage(),"src");
        zombiePage.clickElement(bubblingFileReader.getNextImageIconInGallery() );
        zombiePage.waitForAttributeChange(bubblingFileReader.getGalleryImage(),"src", imgUrl,5);
        zombiePage.assertAttributeNotEquals("冒泡中的图片点击预览点击下一页可能无效", imgUrl ,bubblingFileReader.getGalleryImage(),"src" );
        imgUrl = zombiePage.getAttribute(bubblingFileReader.getGalleryImage(),"src");
        zombiePage.sendKeys(Keys.ARROW_LEFT);
        zombiePage.waitForAttributeChange(bubblingFileReader.getGalleryImage(),"src", imgUrl,5);
        zombiePage.assertAttributeNotEquals("冒泡中的图片点击预览按左箭头看下一页可能无效", imgUrl ,bubblingFileReader.getGalleryImage(),"src" );

        zombiePage.clickElement(".gallery .icon.close");
        zombiePage.assertElementNotPresent("冒泡中的图片点击预览无法关闭", bubblingFileReader.getGalleryImage());

        zombiePage.clickElement(bubblingFileReader.getImageInBubbling(), bubblingFileReader.getGalleryImage());
        zombiePage.sendKeys(Keys.ESCAPE);
        Thread.sleep(1000);
        zombiePage.assertElementNotPresent("冒泡中的图片按ESC键无法关闭预览", bubblingFileReader.getGalleryImage());
    }
    public void emojiAndPicturePreviewTest(MarkDownInputBox markDownInputBox){
        markDownInputBox.clickPreviewIcon(zombiePage);
        zombiePage.waitForElement( "#tweet-list .markdown.preview-content .monkey", 15);
        zombiePage.assertAttributeEquals("预览时markdown里面的洋葱候的表情显示不正确", zombiePage.getBaseUrl() + "static/coding-emotions/coding-emoji-28.png",
                "#tweet-list .markdown.preview-content .monkey", "src");
        zombiePage.assertElementPresent("冒泡中上传图片无法再预览中显示或图片上传不成功",".preview-content .bubble-markdown-image",5);
        zombiePage.assertAttributeNotEquals("图片预览不正确","https://coding.net/static/no-pic.png", ".feed-editor .preview img","src");
        zombiePage.assertTextContainWords("冒泡中的图片前的字符消失 ",  chinesePrefix, "#tweet-list .markdown.preview-content");
        zombiePage.assertTextEquals("预览时@对象不正确", "@" + markDownInputBox.getAtSomeOne().getUserName(), ".at-someone");

        markDownInputBox.clickUnPreviewIcon(zombiePage);
        zombiePage.assertElementNotPresent("冒泡取消预览失败",markDownInputBox.getMDPreviewSelector());
    }
    public void bubblingPreviewTest(MarkDownInputBox markDownInputBox){
        markDownInputBox.clickPreviewIcon(zombiePage);
        zombiePage.getDriver().executeScript("window.scrollTo(0,0)");
        zombiePage.waitForElement(markDownInputBox.getMDPreviewSelector(), 5);
        zombiePage.getElement(markDownInputBox.getMDPreviewSelector()).findElement(By.linkText(autoLink));
        zombiePage.assertTextEquals("冒泡里的第二条链接(https开头)不正确",autoLink,markDownInputBox.getMDPreviewSelector() +" .auto-link",1);
        zombiePage.assertElementPresent("冒泡预览代码失效",".feed-editor .preview pre");
        zombiePage.assertElementPresent("冒泡预览markdown出现问题", ".feed-editor .preview h2");
        zombiePage.assertTextNotContainWords("冒泡不会过滤html标签 ", "html", ".feed-editor .preview", 0);
        markDownInputBox.clickUnPreviewIcon(zombiePage);
        zombiePage.waitElementInvisible(".feed-editor .preview  pre");
        zombiePage.assertElementNotPresent("冒泡预览没有消失", ".feed-editor .preview  pre" );
    }
    public void bubblingThumbnailTest() {
        try {
            Thread.sleep(4000);//图片没有加载完，直接硬等待好了
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        zombiePage.moveToElement(".bubble-item ");
        if(zombiePage.waitForElement(" .fancy-overflow-modal ",2) == null) {
            zombiePage.moveToElement(".bubble-item ");
        }
        assertTrue("显示完整冒泡的薄膜消失了", zombiePage.elementIsPresent(".bubble-item ", 0, " .fancy-overflow-modal "));

        zombiePage.clickElement(".fancy-overflow-modal ");
        zombiePage.assertElementNotPresent("点击冒泡薄膜后未消失",".fancy-overflow-modal " );
    }
    public void verifyBubblingPictureAndEmoji(){
        zombiePage.assertTextContainWords("冒泡中的图片前的字符 ["+chinesePrefix+"]消失 ",chinesePrefix ," .bubble-item .markdown ");
        assertTrue("冒泡的图片显示不正确  " , zombiePage.getAttribute(bubblingFileReader.getImageInBubbling(),"src" ).contains("https://dn-coding-net-tweet.qbox.me/"));
        assertEquals("markdown里面的洋葱候的表情显示不正确", zombiePage.getAttribute(".bubble-item .monkey", "src"),
                zombiePage.getBaseUrl() + "static/coding-emotions/coding-emoji-28.png");
        assertEquals("冒泡无法发送表情", zombiePage.getAttribute(".bubble-item .emoji", "src"),
                zombiePage.getBaseUrl() + "static/emojis/+1.png");
        assertEquals("@某个人出错 "  , "@" + ciwangUser.getUserName(), zombiePage.getText(".bubble-item .at-someone"));
        assertEquals("@对象的链接有误 " , ciwangUser.getHomePageLink(), zombiePage.getAttribute(".bubble-item .at-someone","href"));
    }
    public void verifyBubblingInfo(){
        assertEquals("冒泡作者的名字不正确  " , zombieUser.getUserName(),zombiePage.getText(".bubble-item  .info a[bo-text*=\"name\"]"));
        assertTrue("冒泡 markdown 不能显示代码 ", zombiePage.elementIsPresent(".bubble-item pre", 2));
        assertTrue("冒泡 markdown 不能显示标题 " ,  zombiePage.elementIsPresent(".bubble-item h2", 2));
        assertEquals("冒泡 markdown 不能显示列表 ", "Red", zombiePage.getText(".bubble-item ul li", 1));
        assertEquals("冒泡 markdown 不能显示斜体 " ,"Coding，让开发更简单", zombiePage.getText(".bubble-item li em") );
        assertEquals("冒泡 markdown 不能显示加粗 ", "Coding，让开发更简单", zombiePage.getText(".bubble-item li strong"));
        assertEquals("冒泡 markdown 不能显示列表 " ,"Red", zombiePage.getText(".bubble-item li", 1));
        assertEquals("冒泡中链接的地址不正确 " ,"http://coding.net/", zombiePage.getAttribute(".bubble-item li a", "href"));
        assertEquals("冒泡 markdown中的超链接显示不正确 " ,"超强大的云开发平台Coding", zombiePage.getText(".bubble-item li a"));
        assertEquals("冒泡 markdown中的引用显示不正确 " ,"这是二级引用", zombiePage.getText(".bubble-item blockquote blockquote p "));
        assertNotNull("冒泡 markdown中的超链接显示不正确 "  , zombiePage.getElement(By.linkText(autoLink)));
        assertTrue("冒泡中的字符有误 except:" +specialSign+" actual: "+zombiePage.getText(" .bubble-item .markdown") , zombiePage.getText(" .bubble-item .markdown ").contains(specialSign));
    }
    @Test
    public void test07_0_BubblingUpdateInBackend() throws InterruptedException {
        Bubbling bubbling = new Bubbling(zombieUser, df.format(new Date()) + bubblingMark );
        bubbling.sendBubblingWithTimeInterval();

        ciwangPage.refresh(ciwangPage.getBubblingUrl(), bubblingFileReader.getBubblingTextContent());
        assertTrue("测试评论前所发的冒泡可能没有在后端更新 " + ciwangPage.getText(bubblingFileReader.getBubblingTextContent()),
                ciwangPage.getText(bubblingFileReader.getBubblingTextContent()).contains(bubblingMark));
    }

    @Test
    public void test07_1_SensitiveWordInComment()  {
        ciwangPage.refresh(ciwangPage.getBubblingUrl(), bubblingFileReader.getBubblingTextContent());
        firstCommentBox.sendItemWithoutCheck(ciwangPage,"你妈");
        assertNotNull("冒泡评论中的敏感词过滤失败，或该提示没有出现  ", ciwangPage.verifyHint("内容中包含敏感字符\"你妈\""));
        assertFalse("发送失败后，加载图标一直在 ", ciwangPage.getAttribute(".bubble-item  form", "class").contains("loading"));
    }

    @Test
    public void test07_2_CommentCountInBubblingInfo()  {
        ciwangPage.refresh(ciwangPage.getBubblingUrl(), bubblingFileReader.getBubblingTextContent());
        String commentBaseNumText = ciwangPage.getText(bubblingFileReader.getBubblingCommentCount());

        firstCommentBox.sendItemWithoutCheck(ciwangPage,df.format(new Date()));
        ciwangPage.waitForContentChange(bubblingFileReader.getBubblingCommentCount(), commentBaseNumText, 10);

        assertEquals("发送评论后冒泡信息上的评论数没有增加1  ", Integer.parseInt(commentBaseNumText) + 1,
                Integer.parseInt(ciwangPage.getText(bubblingFileReader.getBubblingCommentCount())));
        zombiePage.refresh(".comment-item");
        assertEquals("评论的数值显示不正确或评论并没有在后端更新  "  , Integer.parseInt(commentBaseNumText) + 1 ,
                Integer.parseInt( zombiePage.getText(bubblingFileReader.getBubblingCommentCount())));
    }
    @Test
    public void test07_4_sendCommentWithKeyboard(){
        ciwangPage.refresh(ciwangPage.getBubblingUrl(), bubblingFileReader.getBubblingTextContent());
        firstCommentBox.sendContentToInputBox(ciwangPage, df.format(new Date()));
        ciwangPage.getBuilder().keyDown(Keys.CONTROL).sendKeys(Keys.ENTER).perform();
    }
    @Test
    public void test07_3_sendRepeatComment() throws InterruptedException {
        ciwangPage.refresh(ciwangPage.getBubblingUrl(), bubblingFileReader.getBubblingTextContent());
        String commentMark = df.format(new Date());
        firstCommentBox.sendItem(ciwangPage, commentMark);
        firstCommentBox.sendItemWithoutCheck(ciwangPage, commentMark);
        assertNotNull("屏蔽重复发布评论失效或该提示消失或按键发送评论失败", ciwangPage.verifyHint("不要重复发布评论哦"));
    }
    //ciwangPage.highLightElemnt(".detail  .remove ");
    @Test
    public void test07_5_removeComment(){
        ciwangPage.refresh(ciwangPage.getBubblingUrl(), bubblingFileReader.getBubblingTextContent());
        String commentCountText = ciwangPage.getText(bubblingFileReader.getBubblingCommentCount());
        firstCommentBox.removeCommentWithBackTest(ciwangPage, 0);
        assertEquals("删除评论后评论数没有减1  " , Integer.parseInt(commentCountText) - 1,
                Integer.parseInt(ciwangPage.getText(bubblingFileReader.getBubblingCommentCount())));
    }
    @Test
    public void test07_6_MarkdownFeatureInComment() throws InterruptedException {
        String commentContent = "<h2>ddd</h2> 你好 。，、：“‘”’——……+=-|\\\"！@#￥$%……^&*（）{}【】？~··";
        Bubbling bubbling = new Bubbling(ciwangUser, df.format(new Date()) + bubblingMark );
        bubbling.sendBubbling();

        zombiePage.refresh(zombiePage.getBubblingUrl(), bubblingFileReader.getBubblingTextContent());
        firstCommentBox.sendContentToInputBox(zombiePage, "@");
        zombiePage.assertTextEquals("没有评论时@出来的第一个不是冒泡作者" ,ciwangUser.getUserName(),".cur");

        firstCommentBox.sendContentToInputBox(zombiePage,":+1");
        assertEquals("发冒泡评论时表情提示出错  "  ,zombiePage.getText(".cur"), "+1");
        firstCommentBox.sendItem(zombiePage, ":+1:");
        assertEquals("评论中的冒泡emoji表情不正确  "  ,baseUrl + "static/emojis/+1.png",zombiePage.getAttribute(".detail .emotion","src"));

        ciwangPage.refresh(ciwangPage.getBubblingUrl(), bubblingFileReader.getBubblingTextContent());
        firstCommentBox.sendContentToInputBox(ciwangPage, "@");
        ciwangPage.assertTextEquals("有评论时@出来的第一个不是评论者" ,zombieUser.getUserName(),".cur");
        assertTrue("有评论时@不出来自己关注的对象" ,   ciwangPage.getElementCount(".atwho-view-ul li")>1 );

        firstCommentBox.sendItem(ciwangPage,  "@"+zombieUser.getUserName());
        ciwangPage.assertLinkAndTextEquals("冒泡评论中的@某人", "@"+zombieUser.getUserName(),zombieUser.getHomePageLink(), ".detail .at-someone" );

        firstCommentBox.sendItem(ciwangPage,
                "https://coding.net/u/coding/p/Coding-Feedback/topic \n## h2 " + commentContent + bubblingMark);
        ciwangPage.assertLinkAndTextEquals("冒泡评论中的链接", autoLink,autoLink, ".detail .auto-link" );
        assertTrue("冒泡的评论中markdown未转义  "  , ciwangPage.getText(".detail").contains("## h2"));
        assertTrue("冒泡的评论中html未被转义或有字符没被过滤  "  ,ciwangPage.getText(".detail").contains(commentContent));
    }

    @Test
    public void test07_7_ReplyBubblingComments() throws InterruptedException, ParseException {
        Bubbling bubbling = new Bubbling(ciwangUser, "bubblingFavour"+df.format(new Date()));
        bubbling.sendBubbling();
        firstCommentBox.sendItem(ciwangPage, bubblingMark);
        zombiePage.refresh(zombiePage.getBubblingUrl(), ".comment-item");
        firstCommentBox.replyComment(zombiePage, 0);
    }
    @Test
    public void test09_TopControl() throws InterruptedException {
        ciwangPage.navigate(ciwangPage.getBubblingUrl());
        ciwangPage.executeScript("window.scrollTo(0,3000)");
        ciwangPage.getWaitTool().waitForJavaScriptCondition(ciwangPage.getDriver(), "return window.scrollY != 0",2);
        ciwangPage.clickElement("#topcontrol");
        ciwangPage.getWaitTool().waitForJavaScriptCondition(ciwangPage.getDriver(), "return window.scrollY == 0", 2);
    }

    @Test
    public void test11_HotUserAndHotBubbling() throws InterruptedException {
        zombiePage.navigate(zombiePage.getBubblingUrl(),".tabular.menu");
        zombiePage.clickElement(" .tabular.menu a:nth-child(2)");
        zombiePage.clickElement(" .tabular.menu a:nth-child(2)");
        zombiePage.waitForElement(".bubble-detail",5);
        assertTrue("热门冒泡无冒泡输出", zombiePage.elementIsPresent(".bubble-detail",5));
        assertTrue("活跃用户数量不对",  zombiePage.getElementCount(".user-item") <= 20 && 1 <= zombiePage.getElementCount(".user-item"));
    }
    @Test
    public void test12_ShowMoreComment() throws InterruptedException, ParseException {
        Bubbling bubbling = new Bubbling(ciwangUser, "bubblingFavour"+df.format(new Date()));
        bubbling.sendBubbling();
        zombiePage.navigate(zombiePage.getBubblingUrl(),".comment-item");
        for (int i = 0; i <6; i++) {
            firstCommentBox.sendItem(zombiePage, ciwangPage.getMark());
        }
        testShowMoreComment(zombiePage, zombiePage.getBubblingUrl(),
                "冒泡广场冒泡加载更多评论失败或加载后的评论数数目不对，即加载前获得的评论数和加载后实际得到的评论数不同");
        testShowMoreComment(ciwangPage,ciwangUser.getUserHomePageBubblingLink(),
                "个人主页加载更多评论失败或加载后的评论数数目不对，即加载前获得的评论数和加载后实际得到的评论数不同");
        testShowMoreComment(zombiePage, zombiePage.getHotBubblingUrl() ,
                "冒泡广场热门冒泡加载更多评论失败或加载后的评论数数目不对，即加载前获得的评论数和加载后实际得到的评论数不同");
    }

    @Test
    public void test13_ShowMoreBubbling() throws InterruptedException, ParseException {
        testShowMoreBubbling(zombiePage, zombiePage.getBubblingUrl(),"冒泡广场加载更多失败或加载重复");
        testShowMoreBubbling(zombiePage, zombieUser.getUserHomePageBubblingLink(),"个人首页加载更多失败或加载重复");
    }
    @Test
    public void test14_BubblingCreateTimeAndRemove() throws Exception{
        Bubbling bubbling = new Bubbling(zombieUser,bubblingMark + df.format(new Date()));
        bubbling.sendBubbling();
        bubblingSendBox.removeCommentWithBackTest(zombiePage, 0, "泡泡");
    }
    @Test
    public void test15_Notification() throws Exception {
        EmailTest emailTest = new EmailTest();
        Set mailCheckList = new HashSet();
        List<PageLink> bubblingPageLinks = new ArrayList<PageLink>();
        Bubbling bubbling = new Bubbling(ciwangUser, "@"+zombieUser.getUserName()+" "+notificationMark);
        bubbling.sendBubbling();
        bubblingPageLinks.add(new PageLink(notificationFileReader.getNotificationSecondLink(), bubbling.getContent(),
                bubblingFileReader.getBubblingTextContent(), bubbling.getBubblingDetailUrl()));
        Notification atSomeoneInBubbling = new Notification(notificationFileReader.getAtSomeoneIconClass(),
                bubbling.getSender(),bubbling.getSender().getUserName()+" 在冒泡 "+bubbling.getContent()+" 中提到了你", zombieUser,bubblingPageLinks);
        notificationTest.verifyForMultiLinkFormat(atSomeoneInBubbling, "冒泡中@某人的通知", notificationFileReader.getAtSomeoneItemIcon());

        firstCommentBox.sendItem(ciwangPage, "@"+ zombieUser.getUserName()+" hi");
        Notification atSomeoneInBubblingComment = new Notification(notificationFileReader.getAtSomeoneIconClass(),ciwangUser,
                atSomeoneInBubbling.getContent() + " :@"+zombieUser.getUserName()+" hi",
                zombieUser, bubblingPageLinks);
        notificationTest.verifyForMultiLinkFormat(atSomeoneInBubblingComment, "冒泡的评论中@某人的通知", notificationFileReader.getAtSomeoneItemIcon());

        Bubbling zombieBubbling = new Bubbling(zombieUser, "nol"+notificationMark);
        zombieBubbling.sendBubbling();
        ciwangPage.refresh(bubblingFileReader.getBubblingTextContent());
        firstCommentBox.sendItem(ciwangPage, "commentTest");
        bubblingPageLinks = new ArrayList<PageLink>();
        bubblingPageLinks.add(new PageLink(notificationFileReader.getNotificationSecondLink(), zombieBubbling.getContent(),
                bubblingFileReader.getBubblingTextContent(), zombieBubbling.getBubblingDetailUrl()));
        Notification BubblingCommentWithoutAt = new Notification(notificationFileReader.getCommentIconClass(),ciwangUser,
                ciwangUser.getUserName()+" 回复了你的冒泡 "+zombieBubbling.getContent()+"：commentTest",zombieUser,  bubblingPageLinks);
        notificationTest.verifyForMultiLinkFormat(BubblingCommentWithoutAt, "冒泡中回复某人（不@）的通知", notificationFileReader.getCommentItemIcon());

        favorBubbling(ciwangPage);
        Notification favorBubbling = new Notification(notificationFileReader.getFavorIconClass(),ciwangUser,
                ciwangUser.getUserName()+" 赞了你的冒泡 "+zombieBubbling.getContent(),zombieUser,bubblingPageLinks );
        notificationTest.verifyForMultiLinkFormat(favorBubbling, "冒泡中点赞的通知", notificationFileReader.getCommentItemIcon());

        mailCheckList.add(atSomeoneInBubbling.getContent());
        mailCheckList.add(atSomeoneInBubblingComment.getContent());
        mailCheckList.add(BubblingCommentWithoutAt.getContent());
        mailCheckList.add(favorBubbling.getContent());
        Thread.sleep(5000);
        emailTest.checkEmail(zombiePage,mailCheckList);
    }


    @Test
    public void test10_UserAvatarAndBubbleDetails() throws InterruptedException, ParseException {
        Bubbling bubbling = new Bubbling(ciwangUser, df.format(new Date())+bubblingMark);
        bubbling.sendBubbling();

        FriendFileReader friendFileReader = new FriendFileReader();
        zombiePage.refresh(zombiePage.getBubblingUrl(), ".info .chat.outline ");
        assertTrue("点击评论图标，没有到相应的冒泡详情网页", zombiePage.clickElement(".info .chat.outline ", ".center.user-name")) ;
        zombiePage.assertTextContainWords("点击评论图标，进入相应的冒泡详情网页，但冒泡有误",
                bubblingMark, bubblingFileReader.getBubblingTextContent());
        if(!zombiePage.elementIsPresent(friendFileReader.getFollowButton())) {
            zombiePage.clickElement(friendFileReader.getCancelFollowButton(), friendFileReader.getFollowButton());
        }
        zombiePage.clickElement(friendFileReader.getFollowButton());
        assertTrue("冒泡详情页关注别人失败", zombiePage.elementIsPresent(friendFileReader.getCancelFollowButton(), 5));
    }
    @Test
    public void test16_AdBannerAndFriendCircle() throws InterruptedException {
        zombiePage.refresh(zombieUser.getUserCenterLink(), ".activity.tabs");
        zombiePage.clickElement(".public-pages .item[href*='/pp']", ".multi-ad-image");
        assertTrue("冒泡中心广告栏消失了或导航栏上的冒泡按钮无法点击", zombiePage.elementIsPresent(".multi-ad-image",14));
        /*
        zombiePage.refresh(zombieUser.getUserCenterLink(), ".activity.tabs");
        zombiePage.clickElement(".public-pages .item[href='/projects']", ".multi-ad-image");
        assertTrue("项目广场广告栏消失了", zombiePage.elementIsPresent(".multi-ad-image",14));
        */
    }
    @Test
    public void test08_ShowUserInfoOnGravatar(){
        Bubbling bubbling = new Bubbling(zombieUser,"bubbling card test"+zombiePage.getMark());
        bubbling.sendBubbling();

        ciwangPage.refresh(ciwangPage.getBubblingUrl(), ".bubble-item .heart");
        favorBubbling(ciwangPage);
        firstCommentBox.sendItem(ciwangPage,"comment");

        zombiePage.refresh(zombiePage.getBubblingUrl(), ".bubble-item .avatar ");
        zombiePage.moveToElement(".bubble-item .avatar ");
        zombiePage.assertElementPresent("鼠标停留在冒泡发送者头像上没有出现用户信息", bubblingFileReader.getUserHoverCard(), 15);
        zombiePage.moveToElement(".multi-ad-image");
        zombiePage.waitElementInvisible(bubblingFileReader.getUserNameInHoverCard());

        zombiePage.moveToElement( ".likes  a" );
        zombiePage.assertElementPresent( "鼠标停留在点赞者的头像上没有出现用户信息" ,bubblingFileReader.getUserHoverCard(), 15);

        zombiePage.moveToElement(".multi-ad-image");
        zombiePage.waitForContentChange(bubblingFileReader.getUserNameInHoverCard(), zombieUser.getUserName(), 5);
        zombiePage.moveToElement( ".comment-item  .avatar" );
        zombiePage.assertElementPresent( "鼠标停留在冒泡评论者头像上没有出现用户信息" ,bubblingFileReader.getUserHoverCard(), 15);

        zombiePage.assertCountEquals("用户名片上没有关注，粉丝，冒泡",3,bubblingFileReader.getFirstUserHoverCardInfo());
        zombiePage.assertTextEquals("用户名片上用户名字不对", ciwangUser.getUserName(), bubblingFileReader.getUserNameInHoverCard());
        zombiePage.assertElementPresent( "鼠标停留在冒泡评论者头像上没有出现用户信息" ,bubblingFileReader.getUserAvatarImgInHoverCard(), 5);
        zombiePage.assertElementPresent("名片上面的性别图标消失", ".user-hover-card.active .user-sex.icon");
        zombiePage.assertCountEquals("用户名片上没有去关注和私信",2,bubblingFileReader.getSmallButtonInHoverCard());
        zombiePage.clickElement(bubblingFileReader.getUserNameInHoverCard(),".coding.user-name.center");
        zombiePage.assertTextEquals("名片上的连接不能到个人首页", ciwangUser.getUserName() , ".coding.user-name.center" );
    }

    public void testShowMoreComment(Page page,String testUrl, String errorMessage) throws InterruptedException {
        page.refresh(testUrl, ".bubble-item  ");
        int commentNum = -1;
        int commentCount;
        if(!page.elementIsPresent(".bubble-comments .down.more ", PS.midWait)){
            assertEquals(errorMessage + "该页面可能没有加载更多评论元素",1,2);
        }
        List<WebElement> tweets = page.getElements(".bubble-item  ");
        long sleepMills = 500;
        for (int i = 0; i < tweets.size(); i++ ){
           if(page.elementIsPresent(tweets.get(i), ".down.more", sleepMills)) {
               commentNum = i;
               break;
           }
        }
        commentCount = page.getPageNum(".bubble-item ",commentNum, bubblingFileReader.getBubblingCommentCount() );
        assertEquals("冒泡评论显示了'加载全部评论'的时候，评论数超不足5条或多于5条",
                5, page.getElementCount(".bubble-item ", commentNum, ".comment-item"));
        page.clickChildElement(".bubble-item ", commentNum, ".down.more");
        page.waitLoadingIconInvisible();
        page.waitForElementDisappear(".bubble-item",commentNum ,".down.more",PS.shortWait);
        page.waitForElementCountChange(".bubble-comments ", commentNum, ".bubble-comment ",5,PS.shortWait);
        assertEquals(errorMessage, commentCount, page.getPageNum(".bubble-item ",commentNum, bubblingFileReader.getBubblingCommentCount()));
    }
    public void testShowMoreBubbling(Page page,String testUrl, String errorMessage) throws ParseException, InterruptedException {
        page.navigate(testUrl, ".bubble-item ");
        page.clickElement(".show-more a");
        page.waitForItemCountChange(".bubble-item ", 20, 6);
        assertTrue(errorMessage, page.getElements(".bubble-item ").size() >= 21);

        if(!page.getText(bubblingFileReader.getBubblingTextContent(), 19).equals("")) {
            assertNotEquals(errorMessage, page.getText(bubblingFileReader.getBubblingTextContent(), 19), page.getText(bubblingFileReader.getBubblingTextContent(), 20));
            assertNotEquals(errorMessage, page.getText(bubblingFileReader.getBubblingTextContent(), 0), page.getText(bubblingFileReader.getBubblingTextContent(), 20));
        }
    }

    //这两行当初写的好辛苦，舍不得删
    //int width = (int) (zombiePage.getElement(".bubble-item .bubble-detail ").getSize().getWidth());
    //zombiePage.getBuilder().moveToElement(zombiePage.getElement(".bubble-item .bubble-detail "),(int)(width*0.7),(int)(height*0.7)).build().perform();
    public int getLikeUserCountInFirstBubbling(Page ciwangPage){
        int likeUserNum;
        if(ciwangPage.elementIsPresent(".bubble-item .likes .image ",2) ){
            likeUserNum = ciwangPage.getElementCount(ciwangPage.getElement(".bubble-item .likes ",0),".image");
        }else{
            likeUserNum = 0;
        }
        return likeUserNum;
    }

    public void fastClickBubblingFavour(Page ciwangPage) throws InterruptedException {
        ciwangPage.refresh(ciwangPage.getBubblingUrl(),".bubble-item .heart");
        int favourNum;
        int favourBaseNum = Integer.parseInt( ciwangPage.getText(bubblingFileReader.getBubblingFavorCount()));
        assertEquals("用户的点赞数和点赞用户不一致  "  , getLikeUserCountInFirstBubbling(ciwangPage), favourBaseNum);

        for(int i = 0; i < 3; i++) {
            favorBubbling(ciwangPage);
        }
        ciwangPage.getWaitTool().waitForJavaScriptCondition("return $('.bubble-item .heart').attr(\"class\").indexOf(\"red\") != -1" ,2 );
        favourNum = Integer.parseInt(ciwangPage.getText(bubblingFileReader.getBubblingFavorCount()));
        assertTrue("用户的点赞数和点赞用户不一致  "  , Math.abs(getLikeUserCountInFirstBubbling(ciwangPage)- favourNum) <=1);
        assertTrue("点赞或取消赞测试有误  "  ,  Math.abs(favourBaseNum- favourNum) <=1);

        ciwangPage.refresh(".bubble-item .heart");
        favourNum = Integer.parseInt(ciwangPage.getText(bubblingFileReader.getBubblingFavorCount()));
        assertTrue("后端点赞或取消赞测试有误，即页面刷新后点赞操作会消失",  Math.abs(favourBaseNum- favourNum) <=1);
        assertEquals("用户的点赞数和点赞用户不一致", getLikeUserCountInFirstBubbling(ciwangPage), favourNum);
    }
    public void favorBubbling(Page page){
        int favourBaseCount = Integer.parseInt(page.getText(bubblingFileReader.getBubblingFavorCount()));
        String favourBaseCountText = page.getText(bubblingFileReader.getBubblingFavorCount());
        page.clickElement(".bubble-item .heart");
        page.waitForContentChange(bubblingFileReader.getBubblingFavorCount(), favourBaseCountText, 3);
        page.waitForItemCountChange(".bubble-item:nth-child(3)  .likes a", favourBaseCount, 7);
    }
    public void assertFavourCount(Page page, int assertFavourCount){
        int favourCount = Integer.parseInt(page.getText(bubblingFileReader.getBubblingFavorCount()));
        assertEquals("点赞或取消赞 用户的点赞数和点赞用户不一致  " ,  favourCount , getLikeUserCountInFirstBubbling(page));
        assertEquals("点赞或取消赞测试有误  ", assertFavourCount, favourCount);
    }
    public void favourOperatorTest(Page ciwangPage, int favourCountAdded){
        favorBubbling(ciwangPage);
        assertFavourCount(ciwangPage, favourCountAdded);
        ciwangPage.refresh(bubblingFileReader.getBubblingFavorCount());
        assertFavourCount(ciwangPage, favourCountAdded);
    }
    @AfterClass
    public static void tearDown() throws Exception {
        zombiePage.getDriver().quit();
        ciwangPage.getDriver().quit();
    }
}
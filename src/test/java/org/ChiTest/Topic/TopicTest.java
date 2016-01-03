package org.ChiTest.Topic;


import dataReader.ConfigureInfo;
import org.ChiTest.Activity.ActivityFileReader;
import org.ChiTest.Activity.AdvancedActivity;
import org.ChiTest.AdvancedTask.AdvancedTaskFileReader;
import org.ChiTest.Email.Email;
import org.ChiTest.Email.EmailTest;
import org.ChiTest.Notification.AdvancedNotificationTest;
import org.ChiTest.Notification.Notification;
import org.ChiTest.Notification.NotificationFileReader;
import org.ChiTest.Notification.NotificationTest;
import org.ChiTest.Page.Page;
import org.ChiTest.PageLink;
import org.ChiTest.Project.Project;
import org.ChiTest.Project.ProjectFileReader;
import org.ChiTest.User.User;
import org.ChiTest.WebComponent.MarkDownInputBox;
import org.ChiTest.WebComponent.ScreenShot;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.Cookie;

import java.text.ParseException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by dugancaii on 10/7/2014.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TopicTest {
    protected  static User zombieUser;
    protected  static User ciwangUser;
    private static Page ciwangPage;
    private static Page zombiePage;
    private static Cookie zombieCookie;
    private static Cookie ciwangCookie;
    private static TopicFileReader topicFileReader;
    private ActivityFileReader activityFileReader;
    private NotificationFileReader notificationFileReader;
    private ProjectFileReader projectFileReader;
    private NotificationTest notificationTest;
    private static Project topicProject;
    private  static ConfigureInfo configureInfo;
    private static String specialSign;
    public static String topicTitle  = "<script>topic</script> https://www.baidu.com 嘿嘿";
    private static Topic topic;
    private static EmailTest emailTest;
    private static AdvancedTaskFileReader advancedTaskFileReader;
    private static MarkDownInputBox topicCommentBox;
    private static Set mailCheckList;
    static AdvancedActivity advancedActivity;
    private static Logger log = Logger.getLogger("topicTest");
    public TopicTest()  {
        topicFileReader = new TopicFileReader();
        notificationFileReader = new NotificationFileReader();
        projectFileReader = new  ProjectFileReader();
        activityFileReader = new ActivityFileReader();
        notificationTest = new NotificationTest();
    }
    @BeforeClass
    public static void setUp() throws Exception {
        topicFileReader = new TopicFileReader();
        configureInfo = new ConfigureInfo(true,true);

        zombiePage = configureInfo.getZombieUser().getPage();
        ciwangPage = configureInfo.getCiwangUser().getPage();

        zombieUser = new User("coding_test","Z_topic_test","123456",zombiePage,"Fruit-3.png");
        ciwangUser = new User("coding_test46","C_topic_test","123456",ciwangPage);

        zombieCookie = zombiePage.getStagingSid("c8419ba1-f1de-4315-ad4f-aabd17b08b6d");
        ciwangCookie = ciwangPage.getStagingSid("d5ce6138-32db-4d32-9bc0-d08e66ed6011");

        zombieUser.autoLogin(configureInfo.getLoginUrl(),zombieCookie );
        ciwangUser.autoLogin(configureInfo.getLoginUrl(), ciwangCookie);

        emailTest = new EmailTest(new Email("coding_test47","163.com","chi2014"));
        specialSign = "<>《》~,.`1!@#$%&()_+|-='；‘、、/-+-+。，、：“‘”’——……+=-|\\！@#￥$%……&*（）{}【】？\"";
        //topicProject = new Project(zombieUser,topicFileReader.getProjectName() ,"this is a test ","private", true, "MIT License", "Java" );
        topicProject = new Project(zombieUser,topicFileReader.getProjectName()+ciwangPage.getMark() ,"this is a test ","private", true, "MIT License", "Java" );
        topicProject.createFixtureProject();
        topicProject.addProjectMember(ciwangUser);
        topic= new Topic(topicProject,topicTitle,  "this topic has edit <htlm>sdf<html>\n 貌似要编辑的长一点才会出现loading图案"  ,zombieUser);
        advancedTaskFileReader = new AdvancedTaskFileReader();
        advancedActivity = new AdvancedActivity(ciwangUser,zombieUser,topicProject);
        mailCheckList = new HashSet();
        topicCommentBox = new MarkDownInputBox(topicFileReader.getCommentMDbox(),topicFileReader.getTopicComment() );
    }
    @Rule
    public ScreenShot screenShotZombie = new ScreenShot(zombieUser);
    @Rule
    public ScreenShot screenShotCiwang = new ScreenShot(ciwangUser);

    @Test
    public void test01_1_topicDraft() throws InterruptedException {
        Page zombiePage = topicProject.getCreator().getPage();
        zombiePage.navigate(topicProject.getProjectLink(), topicFileReader.getAddTopicInProjectHome());
        assertTrue("项目首页右侧项目讨论中创建讨论跳转失败，请手动点击确认",
                zombiePage.clickElement(topicFileReader.getAddTopicInProjectHome(), topicFileReader.getAddTopic()) );
        zombiePage.clickElement(topicFileReader.getCancelReleaseTopic());
        zombiePage.clickElement(topicFileReader.getAddTopic(), topicFileReader.getTopicTitleInput());
        zombiePage.sendKeys(topicFileReader.getTopicContentInput(),"test title");
        Thread.sleep(3000);
        zombiePage.checkAlert(topicFileReader.getCancelReleaseTopic(), "取消编辑后弹出弹框，点击取消或点击确认跳转出错",
                "本地草稿内容将会清空，确认离开？");
        assertTrue("项目首页左侧的“讨论”的icon图标消失",zombiePage.elementIsPresent("#context-menu .chat.icon",5) );
        assertTrue("项目首页左侧讨论按钮点击后无法跳转到讨论首页，请手动点击确认",zombiePage.clickElement("#context-menu .item:nth-child(3)", topicFileReader.getAddTopic()));

        zombiePage.clickElement(topicFileReader.getAddTopic(), topicFileReader.getTopicTitleInput());
        zombiePage.clearAndSendKeys(topicFileReader.getTopicTitleInput(), topic.getTopicTitle());
        zombiePage.clearAndSendKeys(topicFileReader.getTopicContentInput(), "测试本地保存");
        zombiePage.waitForElement(".ok.sign.icon", 25);

        zombiePage.checkAlert(topicFileReader.getMyTopicCount(), "取消编辑后弹出弹框，点击取消或点击确认跳转出错",
                "你的内容已经保存到本地，确认离开？");

        zombiePage.clickElement(topicFileReader.getAddTopic());
        zombiePage.waitForElement(topicFileReader.getTopicContentInput(),10);
        zombiePage.assertAttributeContainWords("本地保存可能失效","测试本地保存", topicFileReader.getTopicContentInput(),"value");
        zombiePage.clickElement(topicFileReader.getReleaseTopicButton(),topicFileReader.getNewTopicDetailWaitElement());
    }
    @Test
    public void test08_0_watchTopic() throws InterruptedException {
        Topic watchedTopic = new Topic(topicProject, "watchTopic", "just topic", zombieUser);
        //todo ciwang 评论讨论 检查 zombie 有没有收到通知
        watchedTopic.createTopic();
        zombiePage.assertIconAndText("讨论模块，我关注的 分类", topicFileReader.getMyWatchTopicCount(), "我关注的 (0)", "unhide");
        int originWatchCount = zombiePage.getPageNum(topicFileReader.getMyWatchTopicCount());
        watchTopic(zombiePage);
        zombiePage.assertNumInTextEquals("添加讨论关注后，我的关注没有加1", originWatchCount + 1, topicFileReader.getMyWatchTopicCount());
        zombiePage.assertIconAndText("点击关注按钮后，取消关注按钮", topicFileReader.getWatchButton(), "取消关注", "hide");
        zombiePage.assertTextEquals("关注的人数不对","1",topicFileReader.getWatchCountNextToButton());
        zombiePage.backToTop();
        /*
        //todo 再看，实在不行就发长讨论吧
        int scrollY = zombiePage.getScrollY();
        zombiePage.getDriver().manage().window().setSize(new Dimension(700,400));
        zombiePage.clickElement(topicFileReader.getWatchCountNextToButton());
        assertNotEquals("点击取消关注旁的数字，没有跳转到关注者列表", scrollY, zombiePage.getScrollY());
        zombiePage.getDriver().manage().window().maximize();
        */

        zombiePage.assertTextEquals("添加关注后，关注者列表的 header 有误","关注者（1）", topicFileReader.getWatchUserHeader());
        zombiePage.assertAttributeEquals("关注列表中的关注者有误", zombieUser.getUserName(), topicFileReader.getWatchUser() ,"title");
        zombiePage.moveToElement(topicFileReader.getWatchUser());
        zombiePage.assertElementPresent("放到讨论关注者头像上没有出现名片", ".user-hover-card");

        myWatchClassify(watchedTopic);
        cancelWatch(originWatchCount);
        notification(watchedTopic);
    }
    public void notification(Topic watchedTopic){
        topicProject.addProjectMember(ciwangUser);
        ciwangPage.navigate(watchedTopic.getTopicUrl(),topicFileReader.getCommentMDbox());
        watchTopic(ciwangPage);
        zombiePage.navigate(watchedTopic.getTopicUrl(),topicFileReader.getCommentMDbox());

        topicCommentBox.sendItemWithKeyboard(zombiePage,topicCommentBox.getSendWithKeyboardsContent());
        AdvancedNotificationTest advancedNotificationTest = new AdvancedNotificationTest(ciwangUser,zombieUser, watchedTopic.getProject());
        advancedNotificationTest.constructNotificationForTopicWatch(topicCommentBox, watchedTopic, mailCheckList, zombieUser);
        advancedNotificationTest.checkTaskNotification(ciwangPage);


    }
    public void watchTopic(Page page){
        page.clickElementWithTextChange(topicFileReader.getWatchButton(),".hide.icon" ,
                topicFileReader.getMyWatchTopicCount());
    }



    public void myWatchClassify(Topic watchTopic){
        zombiePage.clickElement(topicFileReader.getMyWatchTopicCount(),topicFileReader.getItemInItemHome());
        zombiePage.assertTextEquals("点击我关注的分类，已关注的讨论却没出现", watchTopic.getTopicTitle(),
                topicFileReader.getTopicTitleInItemHome());
        zombiePage.assertTextEquals("我关注的分类 header 有误", "我关注的 (1)", topicFileReader.getMyWatchTopicCount());
        zombiePage.clickElement(topicFileReader.getTopicTitleInItemHome(),".hide.icon");
        zombiePage.assertElementPresent("关注列表似乎没有在后台更新", topicFileReader.getWatchUserHeader());
    }

    public void cancelWatch(int watchCount){
        zombiePage.clickElementWithTextChange(topicFileReader.getWatchButton(),".unhide.icon" ,
                topicFileReader.getMyWatchTopicCount());
        zombiePage.assertNumInTextEquals("取消讨论关注后，我的关注没有减1", watchCount, topicFileReader.getMyWatchTopicCount());
        zombiePage.assertIconAndText("点击取消关注按钮后，关注按钮",topicFileReader.getWatchButton(), "关注", "unhide" );
        zombiePage.assertElementNotPresent("点击取消关注按钮后，关注的人数不对", topicFileReader.getWatchCountNextToButton());
        zombiePage.assertElementNotPresent("点击取消关注按钮后，关注列表没有减少",topicFileReader.getWatchUserHeader());

        zombiePage.clickElement(topicFileReader.getMyWatchTopicCount(),topicFileReader.getItemInItemHome());
        zombiePage.assertTextEquals("我关注的分类 header 有误", "我关注的 (0)", topicFileReader.getMyWatchTopicCount());
    }


     @Test
    public void test09_0_createTopicAlert(){
        zombiePage.navigate(topicProject.getProjectTopicHomeLink(), topicFileReader.getAddTopic());
        zombiePage.clickElement(topicFileReader.getAddTopic(), topicFileReader.getTopicTitleInput());
        zombiePage.clearAndSendKeys(topicFileReader.getTopicTitleInput(), topic.getTopicTitle());
        zombiePage.clickElement(topicFileReader.getReleaseTopicButton());
        zombiePage.verifyHint("讨论内容不能为空！");

        zombiePage.sendKeys(topicFileReader.getTopicContentInput(),"你妈");
        zombiePage.clickElement(topicFileReader.getReleaseTopicButton());
        assertTrue("讨论中可以发敏感词\"你妈\"，或敏感词的提示消失", zombiePage.verifyHint("内容中包含敏感字符\"你妈\""));
        zombiePage.clearAndSendKeys(topicFileReader.getTopicContentInput(),"......");
        zombiePage.clickElement(topicFileReader.getReleaseTopicButton());
        if(zombiePage.elementIsPresent(".ok.sign.icon")&&zombiePage.isChromeDriver()) {
            zombiePage.getDriver().switchTo().alert().accept();
        }
    }
    @Test
    public void test01_2_TopicCreateAndActivity() throws InterruptedException {
        Topic topic= new Topic(topicProject,topicTitle+zombiePage.getMark(),
                "this topic has edit <htlm>sdf<html>\n 貌似要编辑的长一点才会出现loading图案"  ,zombieUser);
        topic.createTopic();
        topic.setTopicUrl(zombiePage.getCurrentUrl());

        zombiePage.refresh(topicProject.getProjectTopicHomeLink(), topicFileReader.getTopicTitleInItemHome());
        int topicNum = zombiePage.findItemInOnePageByTextEquals( topicFileReader.getTopicTitleInItemHome(),topic.getTopicTitle());
        assertTrue("讨论首页讨论的创建者的头像不对", zombiePage.checkImage(".topics .image.avatar", topicNum, zombieUser.getAvatar()));
        zombiePage.assertTextEqualsOneFromThreeExpect("讨论首页显示的讨论时间不对或作者不对或回应条数不对，或者多了某些多余的字",
                zombieUser.getUserName() + " 发布于 " + "几秒前，有0条回应", zombieUser.getUserName() + " 发布于 " + "1分钟前，有0条回应"
                ,  zombieUser.getUserName() + " 发布于 " + "几秒内，有0条回应",topicFileReader.getTopicTime(), topicNum);

        zombiePage.clickElement(topicFileReader.getTopicTitleInItemHome(), topicNum, topicFileReader.getNewTopicDetailWaitElement());
        assertTrue("讨论详情页的创建者的头像不对", zombiePage.checkImage(".topic-title  .image", zombieUser.getAvatar()));
        zombiePage.assertLinkAndTextEquals("讨论详情页讨论作者", zombieUser.getUserName(), zombieUser.getHomePageLink(), topicFileReader.getTopicOwnerInTopicDetailPage());
        zombiePage.assertTextEquals("讨论详情页的讨论标题不对", topic.getTopicTitle(), topicFileReader.getTopicTitleInDetailPage());
        zombiePage.assertTextEqualsOneFromThreeExpect("讨论详情页讨论时间出错", "几秒内", "几秒前", "1分钟前",
                topicFileReader.getTopicTimeInTopicDetailPage(), 0);
        zombiePage.assertElementPresent("讨论详情页讨论时间图标不见", topicFileReader.getTopicTimeInTopicDetailPage(), 0, ".icon.time");
        zombiePage.assertTextEquals("讨论详情页没有评论时，提示语句出错", "还没人评论，来一发？", topicFileReader.getEmptyTopicComments());

        zombiePage.navigate(topicProject.getProjectLink(), topicFileReader.getTopicTitleInProjectHome());
        zombiePage.getWaitTool().waitForContentNotNull(topicFileReader.getTopicTitleInProjectHome(), 10);
        zombiePage.assertTextEquals("项目首页的项目讨论列表中显示的讨论的标题不正确", topic.getTopicTitle(), topicFileReader.getTopicTitleInProjectHome());
        zombiePage.assertLinkEquals("点击项目首页的项目讨论的标题无法跳转到讨论详情页", topic.getTopicUrl(), topicFileReader.getTopicTitleInProjectHome());
        zombiePage.assertLinkAndTextEquals("项目首页的项目讨论的查看所有讨论","查看所有讨论",topic.getProject().getProjectTopicHomeLink().replace("/all",""),".right.more[href*='topic']");

        advancedActivity.constructCreateTopicInPrivateProjectActivity(topic, activityFileReader.getTopicChatIconClass() );
        advancedActivity.checkActivity(zombiePage,activityFileReader.getTopicActivityTagInProjectHomePage());
    }
    @Test
    public void test01_3_TopicCreateMD() throws InterruptedException {
        zombiePage.navigate(topicProject.getProjectTopicHomeLink(), topicFileReader.getAddTopic());
        zombiePage.clickElement(topicFileReader.getAddTopic(), topicFileReader.getTopicTitleInput());
        zombiePage.clearAndSendKeys(topicFileReader.getTopicTitleInput(), topic.getTopicTitle());
        zombiePage.clearAndSendKeys(topicFileReader.getTopicContentInput(), ":+1");
        zombiePage.assertElementPresent("讨论正文中没有出现emoji提示", ".atwho-view-ul .cur");
        zombiePage.sendKeys(topicFileReader.getTopicContentInput(), "\n\n## h2\n");
        zombiePage.sendKeys(topicFileReader.getTopicContentInput(), "\n@" + ciwangUser.getUserName());
        zombiePage.assertElementPresent("讨论正文中没有出现@提示", " .cur[data-value*='" + ciwangUser.getUserName() + "']", 4);

        zombiePage.sendKeys(topicFileReader.getTopicContentInput(), "\nhttp://www.baidu.com/s?wd=%E5%8F%B0%E5%8C%97101&rsv_spt=1&issp=1&f=8&rsv_bp=0&ie=utf-8&tn=baiduhome_pg\n");
        zombiePage.sendKeys(topicFileReader.getTopicContentInput(), "<html>topic</html>\n");
        zombiePage.sendKeys(topicFileReader.getTopicContentInput(), specialSign);
        zombiePage.clickElement(topicFileReader.getReleaseTopicButton());
        zombiePage.waitElementDisappear(".loading",10);
        zombiePage.elementIsPresent(topicFileReader.getNewTopicDetailWaitElement(),5);
        topic.setTopicUrl(zombiePage.getCurrentUrl());
        AdvancedNotificationTest advancedNotificationTest = new AdvancedNotificationTest(ciwangUser,zombieUser, topic.getProject());
        advancedNotificationTest.constructAtSomeoneInTopicContent(topic,zombieUser, mailCheckList);
        advancedNotificationTest.checkTaskNotification(ciwangPage);

        zombiePage.navigate(topicProject.getProjectTopicHomeLink(), topicFileReader.getTopicTitleInItemHome());
        int topicNum = zombiePage.findItemBySign(topic.getTopicTitle(), topicFileReader.getTopicTitleInItemHome(), zombiePage.getItemEqualsFinder());
        if(topicNum!= -1) {
            zombiePage.moveToElement(topicFileReader.getItemInItemHome(), topicNum);
        }
        previewVerify(zombiePage, topicFileReader.getTopicPreviewButton() , topicNum,"讨论首页（topic/all）加载讨论预览时，加载图标消失");
        zombiePage.clickElement(".up.icon");
        assertTrue("讨论首页的预览无法收回", zombiePage.waitElementDisappear(topicFileReader.getEmotionEmoji()));
        zombiePage.clickElement(topicFileReader.getTopicTitleInItemHome(), topicNum, topicFileReader.getNewTopicDetailWaitElement());
        topicContentVerify(zombiePage, "讨论正文", topicFileReader.getTopicContent(),true);
        testAutoLink(zombiePage, topicFileReader.getAutoLink(), "#kw", "value", "台北101");
    }

    @Test(timeout = 300000)
    public void test02_editTopicThenDeleteTopic() throws InterruptedException, ParseException {
        String newTitle =  zombiePage.getMark();
        topic.setTopicTitle(newTitle);
        topic.createTopic();

        topic.editTopic(zombiePage, "new title", "this topic has edit");
        topic.setTopicTitle(newTitle + "new title");
        topic.setTopicContent("this topic has edit");
        verifyEditedTopic(zombiePage, topic);

        topic.deleteTopic();

        advancedActivity.constructEditAndDeleteTopicInPrivateProjectActivity(topic, activityFileReader.getTopicChatIconClass());
        advancedActivity.checkActivity(zombiePage,activityFileReader.getTopicActivityTagInProjectHomePage());

    }
    @Test(timeout = 300000)
    public void test03_topicCountInTopicHomeAndPermission () throws InterruptedException {
        Topic topic = new Topic(topicProject,"zombie的讨论"+ zombiePage.getMark(),"啦啦啦我是讨论",zombieUser);
        topic.setTopicTitle(topicTitle +zombiePage.getMark());
        zombiePage.navigate(topicProject.getProjectTopicHomeLink(),topicFileReader.getAddTopic());
        int topicCount = zombiePage.getPageNum(topicFileReader.getAllTopicCount());
        int myTopicCount = zombiePage.getPageNum(topicFileReader.getMyTopicCount());
        String topicCountText = zombiePage.getText(topicFileReader.getAllTopicCount());
        topic.createTopic();
        zombiePage.assertNumInTextEquals("发布讨论之后，所有讨论没有加1", topicCount + 1,topicFileReader.getAllTopicCount());
        zombiePage.assertNumInTextEquals("自己发布讨论之后，自己的讨论数没有加1",myTopicCount + 1, topicFileReader.getMyTopicCount());
        ciwangPage.navigate(zombiePage.getCurrentUrl(),topicFileReader.getAddTopic());

        ciwangPage.assertElementNotPresent("ciwang进入zombie创建的讨论后，竟然可以看到删除和编辑讨论的按钮！！",topicFileReader.getDeleteButton());
        topic.deleteTopic();
        zombiePage.assertNumInTextEquals("删除讨论之后，所有讨论没有减1",topicCount,topicFileReader.getAllTopicCount());
        zombiePage.assertNumInTextEquals("删除自己的讨论之后，所有讨论没有减1",myTopicCount, topicFileReader.getMyTopicCount());

        Topic ciwangTopic = new Topic(topicProject,"ciwang的讨论"+ zombiePage.getMark(),"啦啦啦我是讨论",ciwangUser);
        ciwangTopic.createTopic();
        zombiePage.refresh();
        zombiePage.waitForContentChange(topicFileReader.getAllTopicCount(),topicCountText, 5 );
        zombiePage.assertNumInTextEquals("ciwang发布讨论之后，zombie的页面上所有讨论没有加1", topicCount + 1,topicFileReader.getAllTopicCount());
        zombiePage.assertNumInTextEquals("ciwang发布讨论之后，zombie的页面上我的的讨论数有变化",myTopicCount, topicFileReader.getMyTopicCount());

        topicCountText = zombiePage.getText(topicFileReader.getAllTopicCount());
        ciwangTopic.deleteTopic();
        zombiePage.refresh();
        zombiePage.waitForContentChange(topicFileReader.getAllTopicCount(),topicCountText, 5 );
        zombiePage.assertNumInTextEquals("ciwang删除讨论之后，，zombie的页面上所有讨论没有减1", topicCount,topicFileReader.getAllTopicCount());
        zombiePage.assertNumInTextEquals("ciwang删除自己的讨论之后，zombie的页面上我的的讨论数有变化",myTopicCount, topicFileReader.getMyTopicCount());
    }

    @Test
    public void test04_0_topicCommentAndActivity() throws InterruptedException, ParseException {
        topic.setTopicTitle("comment test" +  zombiePage.getMark());
        topic.setTopicContent("this topic for comment test");
        topic.createTopic();
        String topicCommentTest = "动态测试 ## h2 https://www.baidu.com";
        MarkDownInputBox topicCommentBox = new MarkDownInputBox(topicFileReader.getCommentMDbox(),topicFileReader.getTopicComment() );
        topicCommentBox.sendItemWithoutCheck(zombiePage, "你妈");
        assertTrue("讨论评论中敏感词过滤失败", zombiePage.verifyHint("内容中包含敏感字符\"你妈\""));
        topicCommentBox.sendItem(zombiePage, topicCommentTest);
        Thread.sleep(2000);
        zombiePage.navigate(zombieUser.getUserNotificationLink(), notificationFileReader.getNotificationContent());
        zombiePage.assertTextNotContainWords("给自己的讨论评论收到了通知", topicCommentTest, notificationFileReader.getNotificationContent());

        ciwangPage.refresh(topic.getTopicUrl(), topicFileReader.getCommentTime());
        topicCommentBox.replyCommentForTopic(ciwangPage, 0, ".reply ");
        ciwangPage.moveToElement(topicFileReader.getCommentTime());
        ciwangPage.assertElementNotPresent("其他用户貌似有了删除自己的讨论评论的权力", topicFileReader.getRemoveComment(), 2);
        assertEquals("ciwang在zombie的讨论中回复zombie的评论，评论标题的全部评论数没有更新", "评论 （2）",
                "评论 " + ciwangPage.getText(topicFileReader.getCommentCountInCommentHeader()));
        ciwangPage.assertNumInTextEquals("ciwang在zombie的讨论中回复zombie的评论，讨论信息中的全部评论数没有更新",
                2, topicFileReader.getCommentCountInTopicInfo());

        ciwangPage.refresh(topicFileReader.getCommentContent());
        ciwangPage.assertElementPresent("讨论正文显示的讨论的全部评论数旁的聊天图标消失", topicFileReader.getCommentCountInTopicInfo(), ".chat.outline.icon");
        ciwangPage.assertNumInTextEquals("ciwang在zombie的讨论中回复zombie的评论，讨论信息中的全部评论数没有更新",
                2, topicFileReader.getCommentCountInTopicInfo());
        ciwangPage.assertNumInTextEquals("评论完刷新后，评论标题的全部评论数没有更新", 2, topicFileReader.getCommentCountInCommentHeader());
        ciwangPage.checkAvatar(topicFileReader.getCommentAvatar() + " img", topicFileReader.getCommentAvatar(), zombieUser);
        ciwangPage.assertLinkAndTextEquals("讨论评论上显示的用户", zombieUser.getUserName(), zombieUser.getHomePageLink(), topicFileReader.getCommentCreatorName());
        ciwangPage.assertTextContainsOneFromThreeExpect("讨论评论上显示的评论时间有误", "几秒内", "几秒前", "1分钟前", topicFileReader.getCommentTime(), 0);
        ciwangPage.assertElementPresent("讨论评论上显示的评论时间图标消失", topicFileReader.getCommentTime(),".time.icon");

        topicCommentBox.sendInfoWithKeyboard(ciwangPage, topicFileReader.getCommentContent());
        topicCommentBox.removeCommentForTopic(ciwangPage, 1, "评论", ".gray.remove");

        zombiePage.refresh(topic.getTopicUrl(),topicFileReader.getCommentContent());
        zombiePage.assertCountEquals("ciwang在zombie的讨论中删除自己的评论失败", 2, topicFileReader.getCommentContent());

        AdvancedNotificationTest advancedNotificationTest = new AdvancedNotificationTest(ciwangUser,zombieUser, topic.getProject());
        advancedNotificationTest.constructCiwangNotificationForTopicComment(topicCommentBox, topic, mailCheckList);
        advancedNotificationTest.checkTaskNotification(zombiePage);

        advancedActivity.constructTopicCommentInPrivateProjectActivity(topic, topicCommentTest,activityFileReader.getCommentIconClass() );
        advancedActivity.checkActivity(zombiePage,activityFileReader.getTopicActivityTagInProjectHomePage());
    }
    @Test
    public void test04_1_topicCommentMD() throws InterruptedException {
        topic.setTopicTitle("comment test" +  zombiePage.getMark());
        topic.setTopicContent("this topic for comment test");
        topic.createTopic();
        topicCommentBox.sendMarkdownContent(zombiePage, zombieUser, ciwangUser, topicCommentBox.getComplicateImageSender());
        topicCommentBox.testMarkDownPreview(zombiePage, topicCommentBox.getMDPreviewSelector());
        topicCommentBox.clickMDConfirmButton(zombiePage);
        topicCommentBox.verifyMarkdownContent(zombiePage, "[正文]", topicFileReader.getCommentContent(), true);
    }


    public void commentTopic(Page userPage, Topic topic, String commentContent) throws InterruptedException {
        userPage.setPageUrl(topic.getProject().getProjectTopicHomeLink());
        int topicNum = topic.findTopicInTopicHomeByTitle(userPage);
        if(topicNum == -1){
            return;
        }
        assertTrue("讨论首页点击讨论帖子，不能跳转到项目讨论详情页,或没找到评论输入框",userPage.clickElement(topicFileReader.getTopicTitleInItemHome(),topicNum, topicFileReader.getCommentInput()));
        sendComment(userPage, commentContent);
    }
    public void sendComment(Page zombiePage, String commentContent){
        zombiePage.sendKeys(topicFileReader.getCommentInput(),commentContent);
        zombiePage.clickElement(topicFileReader.getCommentButton());
        assertTrue("评论没有发送成功", zombiePage.waitElementDisappear(topicFileReader.getEmptyTopicComments()));
    }

    @Test(timeout = 300000)
    public void test05_topicSort() throws InterruptedException {
        String firstCreateTopicTitle =  "first"+zombiePage.getMark();
        String secondCreateTopicTitle =  "second"+zombiePage.getMark();

        topic.setTopicTitle(firstCreateTopicTitle);
        topic.setTopicContent("this is first topic");
        topic.createTopic( );

        topic.setTopicTitle(secondCreateTopicTitle);
        topic.setTopicContent("this is second topic");
        topic.createTopic();
        checkSort(zombiePage,topicFileReader.getSortTopicByTime(), secondCreateTopicTitle, firstCreateTopicTitle, "按发布时间");

        topic.setTopicTitle(firstCreateTopicTitle);
        commentTopic(zombiePage, topic,"只是一个评论" );
        checkSort(zombiePage,topicFileReader.getSortTopicByComment(), secondCreateTopicTitle, firstCreateTopicTitle, "按最后回答" );
        verifyPagingPresent(topicFileReader.getAllTopicCount(), 11);

    }
    private void verifyPagingPresent(String itemCountSelector, int pagingCount) throws InterruptedException {
        int topicCount = zombiePage.getPageNum(itemCountSelector);
        if(topicCount < pagingCount){
            Topic topic1 = new Topic(topicProject,"sdfs","分页填充物",zombieUser);
            for(int i=0; i< (pagingCount - topicCount); i++){
                topic1.setTopicTitle("page "+ i);
                topic1.createTopic();
            }
            zombiePage.clickElement("#context-menu .big.chat", topicFileReader.getItemInItemHome());
        }
        assertTrue("讨论主页的分页消失了",zombiePage.elementIsPresent(".cg .page.active",5));

    }
    @Test(timeout = 300000)
    public void test06_topicNotification() throws ParseException, InterruptedException {
        emailTest.checkEmail(zombiePage, mailCheckList);
        Project topicProject = new Project(zombieUser, topicFileReader.getPublicProjectName(),"public" );
        topicProject.createFixtureProject();
        Topic topic = new Topic(topicProject, "公有创建讨论"+ciwangPage.getMark(), "ddddd", ciwangUser );
        topic.deleteAllTopic();
        topic.createTopic();

        List<PageLink> pageLinks = new LinkedList<PageLink>();
        pageLinks.add(new PageLink(notificationFileReader.getNotificationSecondLink(), topicProject.getProjectName(),
                projectFileReader.getPublicProjectTitle(), topicProject.getProjectLink()));
        pageLinks.add(new PageLink(notificationFileReader.getNotificationThirdLink(), topic.getTopicTitle(),
                topicFileReader.getTopicTitleInDetailPage(), topic.getTopicOwner().getPage().getDriver().getCurrentUrl()));

        Notification topicCreateInPublicProjectNotification = new Notification(notificationFileReader.getChatIconClass(), ciwangUser,
                ciwangUser.getUserName()+ " 在项目 "+ topicProject.getProjectName() + " 中创建了新的讨论 "+topic.getTopicTitle(), zombieUser, pageLinks);
        notificationTest.verifyForMultiLinkFormat(topicCreateInPublicProjectNotification,
                topic.getTopicTitle(), notificationFileReader.getSystemItemIcon());
        mailCheckList.add(ciwangUser.getUserName() + " 在项目 " + "public" + " 中创建了新的讨论 " + topic.getTopicTitle());
        emailTest.checkEmail(zombiePage, mailCheckList);
    }



    public void testAutoLink(Page page, String selectorForAutoLink,String verifyItem, String attribute ,  String keyWord) throws InterruptedException {
        page.clickElement(selectorForAutoLink);
        page.getDriver().switchTo().window((String )page.getDriver().getWindowHandles().toArray()[1]);
        assertEquals("自动链接" + selectorForAutoLink + "出错", keyWord, page.getAttribute(verifyItem, attribute));
        page.getDriver().switchTo().window((String )page.getDriver().getWindowHandles().toArray()[1]).close();
        page.getDriver().switchTo().window((String )page.getDriver().getWindowHandles().toArray()[0]);
        page.refresh();
    }

    public void checkSort(Page zombiePage,String sortButtonSelector, String frontTopicTitle, String backTopicTitle,String sortType) throws InterruptedException {
        zombiePage.navigate(topic.getProject().getProjectTopicHomeLink(), sortButtonSelector);
        zombiePage.waitLoadingIconInvisible();
        zombiePage.clickElement(sortButtonSelector);
        if(zombiePage.elementIsPresent(".ascending .sort")){
            zombiePage.clickElement(sortButtonSelector);
        }
        int frontTopicNum = zombiePage.findItemBySign(frontTopicTitle, topicFileReader.getTopicTitleInItemHome(),zombiePage.getItemEqualsFinder()  );
        int backTopicNum = zombiePage.findItemBySign(backTopicTitle, topicFileReader.getTopicTitleInItemHome(),zombiePage.getItemEqualsFinder() );
        assertTrue("讨论首页"+sortType+"排序失败，请分别尝试正序和倒序以重现", frontTopicNum< backTopicNum);
        zombiePage.clickElement(sortButtonSelector);
        zombiePage.waitLoadingIconInvisible();
        frontTopicNum = zombiePage.findItemBySign(frontTopicTitle, topicFileReader.getTopicTitleInItemHome(),zombiePage.getItemEqualsFinder() );
        backTopicNum = zombiePage.findItemBySign(backTopicTitle, topicFileReader.getTopicTitleInItemHome(),zombiePage.getItemEqualsFinder() );
        assertTrue("讨论首页"+sortType+"排序失败，请分别尝试正序和倒序以重现", frontTopicNum > backTopicNum);
    }
    public void verifyEditedTopic(Page zombiePage, Topic topic) throws InterruptedException {
        int topicNum =  topic.findTopicInTopicHomeByTitle(zombiePage);
        if(topicNum == -1){
            return;
        }
        zombiePage.clickElement(topicFileReader.getTopicTitleInItemHome(), topicNum,topicFileReader.getNewTopicDetailWaitElement());
        assertEquals("编辑后，讨论详情页（如：u/test5/p/testCreate/topic/2488）的讨论标题不对", zombiePage.getText(topicFileReader.getTopicTitleInDetailPage()), topic.getTopicTitle());
        assertEquals("编辑后，讨论详情页（如：u/test5/p/testCreate/topic/2488）的讨论内容不对", zombiePage.getText(topicFileReader.getTopicContent()), topic.getTopicContent());
    }


    public void previewVerify(Page zombiePage, String previewButtonSelector,int previewButtonNum,String message ) {
        if(!zombiePage.clickElement(previewButtonSelector, previewButtonNum)){
            log.error("讨论预览的按钮不可点击，或不可见，在讨论首页或详情页");
        }
        if(zombiePage.waitForElement(".loading.icon",3)==null){
            log.error(message);
        }
       assertNotNull("讨论正文预览没有出现",zombiePage.waitForElement(topicFileReader.getTopicContent(), 5));
       topicContentVerify(zombiePage,"讨论正文预览", topicFileReader.getTopicContent(),false);
    }
    public void topicContentVerify(Page zombiePage,String message ,String topicPreviewContent,boolean showAt )  {
        if(showAt) {
            assertTrue(message + "没有出现@支持", zombiePage.elementIsPresent(topicFileReader.getAtSomeone(), 3));
        }
        assertTrue(message +"没有出现emoji图案", zombiePage.elementIsPresent(topicFileReader.getEmotionEmoji(), "src",
                zombiePage.getBaseUrl() + "static/emojis/+1.png"));
        assertTrue(message +"的markdown（## h2）没有出现", zombiePage.elementIsPresent(topicFileReader.getMarkDown(),5));
        assertFalse(message+"的出现html", zombiePage.getText(topicFileReader.getTopicContent()).contains("<html>"));
        assertTrue(message+"的没有出现自动链接", zombiePage.elementIsPresent(topicFileReader.getAutoLink(),5));
        assertTrue(message + "的某些标点符号被过滤，测试标点符号如下 " + specialSign, zombiePage.getText(topicPreviewContent).contains(specialSign));

    }
    @AfterClass
    public static void tearDown() throws InterruptedException {
        topicProject.deleteProject();
        zombiePage.getDriver().quit();
        ciwangPage.getDriver().quit();

    }

}

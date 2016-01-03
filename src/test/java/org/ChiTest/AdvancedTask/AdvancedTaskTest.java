package org.ChiTest.AdvancedTask;

import classForComplicatedData.Urgency;
import dataReader.ConfigureInfo;
import org.ChiTest.Activity.ActivityFileReader;
import org.ChiTest.Activity.AdvancedActivity;
import org.ChiTest.Email.Email;
import org.ChiTest.Email.EmailTest;
import org.ChiTest.Notification.AdvancedNotificationTest;
import org.ChiTest.Page.Page;
import org.ChiTest.Project.Project;
import org.ChiTest.User.User;
import org.ChiTest.WebComponent.CommentBox;
import org.ChiTest.WebComponent.MarkDownInputBox;
import org.ChiTest.WebComponent.ScreenShot;
import org.ChiTest.WebComponent.TaskCommentBox;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by dugancaii on 12/3/2014.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdvancedTaskTest {
    private static Page zp;
    private static User zombieUser;
    private static User ciwangUser;
    private static Cookie ciwangCookie;
    private static Page cp;
    private  static Cookie zombieCookie;
    private  static Logger log = Logger.getLogger("AdvancedTaskLog");
    private static Project zombieProject;
    private static  AdvancedTask taskOpreator;
    private static AdvancedTaskFileReader advancedTaskFileReader;
    private static String commentContent;
    private static String commentToZombieContent;
    private static String atSomeoneContent;
    private static String deleteCommentContent;
    private static String taskForComment;
    private static AdvancedActivity advancedActivity;
    private static AdvancedNotificationTest advancedNotificationTest;
    private static AdvancedTaskChecker advancedTaskChecker;
    private static String taskContentForDescriptionTest;
    private static String taskEditDescriptionContent;
    private static Set mailCheckList;
    private static MarkDownInputBox taskDescriptionEditor;
    private static TaskCommentBox firstCommentBox;
    private static ActivityFileReader activityFileReader;
    private static EmailTest emailTest;
    public AdvancedTaskTest(){
        advancedTaskFileReader = new AdvancedTaskFileReader();
    }

    @BeforeClass
    public static void  setUp() throws Exception {
        ConfigureInfo ConfigureInfo = new ConfigureInfo(true, true);

        zp = ConfigureInfo.getZombieUser().getPage();
        cp =  ConfigureInfo.getCiwangUser().getPage();
        zombieUser = new User("wangyiA163_-31","wangyiTask","123456",zp,"Fruit-3.png");
        ciwangUser = new User("test6","testZp_task","123456",cp);

        zombieCookie = zp.getStagingSid("433a51bb-8ca6-4edc-9a7f-4dd42a654fe0");
        ciwangCookie = cp.getStagingSid("fadd8474-af0c-4f46-84d5-dfa727334f5f");
        emailTest = new EmailTest(new Email("coding_test34", "163.com","chi2014"));
        zombieUser.autoLogin(ConfigureInfo.getLoginUrl(), zombieCookie);
        ciwangUser.autoLogin(ConfigureInfo.getLoginUrl(), ciwangCookie);

        activityFileReader = new ActivityFileReader();
        advancedTaskFileReader = new AdvancedTaskFileReader();

        //zombieProject = new Project(zombieUser, "TaskPrivate", "private");
        zombieProject = new Project(zombieUser, "Task"+cp.getMark(), "private");
      //  zombieProject.deleteAllProject();
        zombieProject.createFixtureProject();
        advancedActivity = new AdvancedActivity(ciwangUser,zombieUser,zombieProject);
        advancedNotificationTest = new AdvancedNotificationTest(ciwangUser,zombieUser,zombieProject);
        deleteCommentContent = "comment delete";
        commentContent = "zombie comment";
        commentToZombieContent = "comment to zombie";
        taskContentForDescriptionTest = "description Test";
        taskEditDescriptionContent = "have edited";

        atSomeoneContent = "@"+ zombieUser.getUserName() +" atSomeOne";  //  ## h2 ok www.baidu.com
        advancedTaskChecker = new AdvancedTaskChecker(zombieProject);
        mailCheckList = new HashSet();
        taskOpreator = new AdvancedTask();
        taskForComment = "taskForComment";
        taskDescriptionEditor = new MarkDownInputBox(advancedTaskFileReader.getTaskDescriptionMDInDetailPage());
        zombieProject.addProjectMember( ciwangUser);
        firstCommentBox = new TaskCommentBox(advancedTaskFileReader.getTaskCommentBoxInTaskList(),
                advancedTaskFileReader.getTaskCommentItem());
    }
    @Rule
    public ScreenShot screenShotZombie = new ScreenShot(zombieUser);
    @Rule
    public ScreenShot screenShotCiwang = new ScreenShot(ciwangUser);

    @Test(timeout = 300000)
    public void test07_1_commentInListForMD() throws ParseException {
        taskOpreator.deleteAllTasks(zombieProject);
        zp.refresh(zombieProject.getProjectTaskLink(), advancedTaskFileReader.getTaskContentInput());
        zp.waitElementInvisible(advancedTaskFileReader.getTaskLoadingIcon());
        taskOpreator.sendSimpleTask(zp, ciwangUser.getUserName(), "commentInListForMD", zombieProject.getProjectTaskLink());
        emptyCommentTest(cp);
        cp.assertIconAndText("任务列表的'查看详情'", advancedTaskFileReader.getTaskLookDetails(), "查看详情", "info");
        assertEquals("任务评论输入框中的输入提示有误", "在此输入评论内容（Ctrl+Enter）",
                cp.getAttribute(advancedTaskFileReader.getTaskCommentInputInTaskList(), "placeholder"));

        verifyCommentMDFeature(cp, zombieUser, ciwangUser);
    }

    @Test(timeout = 600000)
    public void test07_2_commentInTaskList() throws InterruptedException, ParseException {
        AdvancedTask advancedTaskForCommentInTaskList = new AdvancedTask();
        zp.navigate(zombieProject.getProjectTaskLink());
        zp.waitElementInvisible(advancedTaskFileReader.getTaskLoadingIcon());
        advancedTaskForCommentInTaskList.setContent(taskForComment);
        taskOpreator.sendSimpleTask(zp, ciwangUser.getUserName(), advancedTaskForCommentInTaskList.getContent(), zombieProject.getProjectTaskLink());
        zp.clickElement(advancedTaskFileReader.getTaskCommentButtonInTaskList(),advancedTaskFileReader.getEmptyCommentList());
        advancedTaskForCommentInTaskList.setTaskUrl(zp.getLink(advancedTaskFileReader.getTaskLookDetails()));

        cp.refresh(zombieProject.getProjectTaskLink(), advancedTaskFileReader.getTaskItem());
        cp.clickElement(advancedTaskFileReader.getTaskCommentButtonInTaskList(),advancedTaskFileReader.getEmptyCommentList());

        firstCommentBox.sendItemWithKeyboard(cp, deleteCommentContent);
        firstCommentBox.removeComment(cp, 0);

        firstCommentBox.sendItemWithKeyboard(zp, commentContent);
        refreshCommentInTaskList(cp);
        firstCommentBox.replyComment(cp, 0);
        firstCommentBox.sendItemWithKeyboard(cp, atSomeoneContent);

        showMoreCommentButtonAndCommentCount(firstCommentBox,cp);

        advancedNotificationTest.constructNotificationMapAfterComment(commentToZombieContent,
                atSomeoneContent, advancedTaskForCommentInTaskList, mailCheckList);
        advancedNotificationTest.checkTaskNotification(zp);

        advancedActivity.constructMapAfterCommentTest(commentToZombieContent,
                atSomeoneContent, deleteCommentContent, advancedTaskForCommentInTaskList);
        advancedActivity.checkTaskActivity(zp, 0, 0);
        emailTest.checkEmail(zp,mailCheckList);
    }
    private void refreshCommentInTaskList(Page cp){
        cp.clickElement(advancedTaskFileReader.getTaskCommentButtonInTaskList());
        cp.waitElementInvisible(advancedTaskFileReader.getTaskCommentInputInTaskList());
        cp.clickElement(advancedTaskFileReader.getTaskCommentButtonInTaskList(), advancedTaskFileReader.getTaskCommentInputInTaskList());

    }

    private void emptyCommentTest(Page cp){
        cp.navigate(zombieProject.getProjectTaskLink(), advancedTaskFileReader.getTaskItem());
        assertTrue("任务列表的评论图标消失", cp.elementIsPresent(advancedTaskFileReader.getTaskCommentButtonInTaskList() + " .chat.outline.icon", 3));
        assertEquals("任务列表中的任务在没有评论时，显示有误", "0 条评论", cp.getText(advancedTaskFileReader.getTaskCommentButtonInTaskList() + " span"));

        cp.clickElement(advancedTaskFileReader.getTaskCommentButtonInTaskList(), advancedTaskFileReader.getEmptyCommentList());
        assertTrue("任务列表中无评论时，没有咖啡图案", cp.elementIsPresent(advancedTaskFileReader.getEmptyCommentList() + " .icon.coffee", 2));
        assertEquals("任务列表中无评论时，没有尚无评论的提示", "尚无评论，速速抢个先手吧", cp.getText(advancedTaskFileReader.getEmptyCommentList()));
        assertEquals("点击任务列表中的任务评论，没有显示收起评论", "收起评论", cp.getText(advancedTaskFileReader.getTaskCommentButtonInTaskList()));
    }


    private void showMoreCommentButtonAndCommentCount(CommentBox commentBox,Page cp){
        for(int i = 0; i < 3; i++){
            commentBox.sendItemWithKeyboard(cp, "comment " + i);
        }
        assertEquals("任务中查看全部评论提示有误", "查看全部 6 条评论", cp.getText(advancedTaskFileReader.getTaskShowMoreComment()));

        cp.clickElement(advancedTaskFileReader.getTaskCommentButtonInTaskList());
        cp.waitElementInvisible(advancedTaskFileReader.getTaskCommentInputInTaskList());
        assertEquals("收起评论后显示的评论数目不对", "6 条评论", cp.getText(advancedTaskFileReader.getTaskCommentButtonInTaskList()));
        cp.clickElement(advancedTaskFileReader.getTaskCommentButtonInTaskList(), " .double.angle.right.icon");
        taskOpreator.changeExecutive(cp, zombieUser.getUserName());
        commentBox.sendItemWithKeyboard(cp, "comment to zombie");

        assertTrue("点击查看全部任务评论，没有跳转到任务详情页",
                cp.clickElement(advancedTaskFileReader.getTaskShowMoreComment() + " .double.angle.right.icon", advancedTaskFileReader.getTaskDetailActivity()));
    }
    public void verifyCommentMDFeature(Page page, User atUser, User reviewer) throws  ParseException {
        page.sendKeys(advancedTaskFileReader.getTaskCommentInputInTaskList(), ":+1");
        page.clickElement(".cur");
        assertEquals("表情的提示不正确", ":+1:", page.getAttribute(".cur", "data-value"));
        MarkDownInputBox taskCommentBoxInTaskList =  new MarkDownInputBox(advancedTaskFileReader.getTaskCommentBoxInTaskList());
        taskCommentBoxInTaskList.setAtSomeOne(atUser);
        taskCommentBoxInTaskList.verifyCanAtSomeoneHint(page, advancedTaskFileReader.getTaskCommentInputInTaskList());

        firstCommentBox.sendItemWithKeyboard(page, taskCommentBoxInTaskList.getBaseTestContent());
        assertEquals("任务评论者有误", reviewer.getUserName(), page.getText(advancedTaskFileReader.getTaskCommentAuthor()));
        page.assertTextEqualsOneFromThreeExpect("任务评论的时间有误", "几秒前", "几秒内", "1分钟前", advancedTaskFileReader.getTaskCommentTime(), 0);

        assertTrue("任务评论中的表情符号显示不正确，应该显示为[+1]", page.getText(advancedTaskFileReader.getTaskCommentContentInTaskList()).contains("[+1]"));
        assertTrue("任务评论中的特殊符号显示不正确", page.getText(advancedTaskFileReader.getTaskCommentContentInTaskList()).contains(taskCommentBoxInTaskList.getSpecialSign()));
        assertTrue("任务评论中html和markdown没过滤,应显示## h2 ok", page.getText(advancedTaskFileReader.getTaskCommentContentInTaskList()).contains("## h2 ok"));

        assertEquals("任务评论中的@的对象的链接不正确 ", atUser.getHomePageLink(), page.getLink(advancedTaskFileReader.getTaskCommentContentInTaskList() + " a"));
        assertEquals("任务评论中的@的对象不正确 ", "@" + atUser.getUserName(), page.getText(advancedTaskFileReader.getTaskCommentContentInTaskList() + " a"));

        assertEquals("任务评论中的里面的链接文本不正确", taskCommentBoxInTaskList.getHttpLinkText(), page.getText(advancedTaskFileReader.getTaskCommentContentInTaskList() + " a", 1));
        assertEquals("任务评论中的里面的链接不正确", taskCommentBoxInTaskList.getHttpLink(), page.getLink(advancedTaskFileReader.getTaskCommentContentInTaskList() + " a", 1));
        assertEquals("任务评论中的的链接(https开头)文本不正确", taskCommentBoxInTaskList.getHttpsLinkText(), page.getText(advancedTaskFileReader.getTaskCommentContentInTaskList() + " a", 2));
        assertEquals("任务评论中的的链接(https开头)不正确", taskCommentBoxInTaskList.getHttpsLink(), page.getLink(advancedTaskFileReader.getTaskCommentContentInTaskList() + " a", 2));
    }

    public String getTaskLinkInTaskList(Page cp){
        return getTaskLinkInTaskList(cp, 0);
    }
    public String getTaskLinkInTaskList(Page cp, int i){
        return cp.getLink(advancedTaskFileReader.getTaskTimeInTaskList(),i);
    }


    @Test(timeout = 300000)
    public void test05_0_commentMDBoxInDetailPage() throws ParseException, InterruptedException {
        AdvancedTask advancedTaskForCommentInTaskDetailPage = advancedTaskChecker.sendStandardTaskInProjectPage( zombieUser, ciwangUser,zombieProject);
        String taskLinkUrl = getTaskLinkInTaskList(zp);
        String taskCommentLinkUrl = taskLinkUrl + "/comment";
        advancedTaskForCommentInTaskDetailPage.setTaskUrl(taskLinkUrl);

        cp.navigate(taskCommentLinkUrl, advancedTaskFileReader.getTaskTimeInDetailPage());
        advancedTaskChecker.verifyTaskContentInDetailPage(cp, advancedTaskForCommentInTaskDetailPage);
        advancedTaskForCommentInTaskDetailPage.setContent(cp, taskForComment);

        MarkDownInputBox commentMDBox = new MarkDownInputBox(advancedTaskFileReader.getTaskCommentMDBoxInDetailPage(), advancedTaskFileReader.getTaskDetailActivity() );
        zp.navigate(taskCommentLinkUrl, advancedTaskFileReader.getTaskCommentMDBoxInDetailPage());

        commentMDBox.sendItem(zp, "for comment");
        cp.refresh(advancedTaskFileReader.getTaskCommentContentInDetailPage());
        commentMDBox.replyComment(cp, 0);

        commentMDBox.sendItem(cp, deleteCommentContent);
        commentMDBox.removeCommentWithBackTest(cp, 2);
        Thread.sleep(2000);
        commentMDBox.sendItem(cp, commentContent);
        Thread.sleep(2000);
        commentMDBox.sendItem(cp, atSomeoneContent);

        filterActivityInTaskDetailPage(cp, taskLinkUrl);

        advancedNotificationTest.constructNotificationMapAfterComment(commentContent,
                atSomeoneContent, advancedTaskForCommentInTaskDetailPage, mailCheckList);
        advancedNotificationTest.checkTaskNotification(zp);
        advancedActivity.constructMapAfterCommentTest(commentContent,
                atSomeoneContent, deleteCommentContent, advancedTaskForCommentInTaskDetailPage);
        advancedActivity.checkTaskActivity(zp, 0, 0);
        emailTest.checkEmail(zp, mailCheckList);
    }
    @Test(timeout = 600000)
    public void test05_2_commentBoxMDFeatureInDetailPage() throws ParseException {
        cp.refresh(zombieProject.getProjectTaskLink(),advancedTaskFileReader.getTaskItem());
        forwardTaskDetailPage(cp,0);

        MarkDownInputBox commentMDBox = new MarkDownInputBox(advancedTaskFileReader.getTaskCommentMDBoxInDetailPage());
        int activityCount = cp.getElementCount(advancedTaskFileReader.getTaskDetailActivity());
        commentMDBox.sendMarkdownContent(cp, ciwangUser, zombieUser, commentMDBox.getComplicateImageSender());
        commentMDBox.testMarkDownPreview(cp, advancedTaskFileReader.getTaskCommentMDBoxPreviewContentInDetailPage());
        cp.clickElement(commentMDBox.getMarkDownConfirmSelector());
        cp.waitForItemCountChange(advancedTaskFileReader.getTaskDetailActivity(), activityCount, 15);
        commentMDBox.verifyMarkdownContent(cp, "正文", advancedTaskFileReader.getTaskDetailActivity() + ":nth-child(" + (activityCount + 1) + ")", true);
        commentMDBox.sendInfoWithKeyboard(cp, advancedTaskFileReader.getTaskCommentContentInDetailPage());
    }

    @Test(timeout = 300000)
    public void test05_3_MDFeatureShowInActivity() throws ParseException {
        cp.navigate(zombieProject.getProjectTaskLink(),advancedTaskFileReader.getTaskItem());
        forwardTaskDetailPage(cp,0);
        MarkDownInputBox commentMDBox = new MarkDownInputBox(advancedTaskFileReader.getTaskCommentMDBoxInDetailPage(), zombieUser);
        commentMDBox.sendMarkDownContentForActivity(cp, commentMDBox.getComplicateImageSender());
        cp.navigate(ciwangUser.getUserCenterLink(), activityFileReader.getActivityContent());
        cp.assertTextContainWords("MD的特殊元素如图片等在动态中显示不正确",
                "[听音乐] [图片] @" + commentMDBox.getAtSomeOne().getUserName() + " https://www.baidu.com [代码]", activityFileReader.getActivityContent());
    }


    @Test(timeout = 300000)
    public void test04_projectHomeTaskWarpper() throws InterruptedException {
        AdvancedTask projectTask =  advancedTaskChecker.sendStandardTaskInProjectPage(ciwangUser, zombieUser,zombieProject);
        cp.clickElement(advancedTaskFileReader.getTaskCommentButtonInTaskList(),
                advancedTaskFileReader.getTaskCommentInputInTaskList());
        firstCommentBox.sendItemWithKeyboard(cp, "评论一下");
        projectTask.setTaskUrl(getTaskLinkInTaskList(cp));
        zp.navigate(zombieProject.getProjectLink(), advancedTaskFileReader.getTaskItem());
        zp.assertIconAndTextAndLink("项目首页的创建任务", "创建任务", "add",
                zombieProject.getProjectTaskLink(), ".project.tasks .header a");

        zp.assertLinkAndTextEquals("项目首页的查看我的任务", "查看我的任务",
                zombieProject.getProjectTaskLink(zombieUser.getUserLoginName()),".project.tasks  .right.more" );
        zp.assertIconAndTextAndLink("项目首页的任务评论", "1 条评论", "chat.outline",
                projectTask.getTaskUrl(),".project.tasks a[target='_self']" );

        assertEquals("发送任务后，任务列表中任务内容不对", projectTask.getContent(), zp.getAttribute(advancedTaskFileReader.getTaskContentInTaskList(), "value"));
        assertEquals("发送任务后，任务的创建者有误", projectTask.getCreator().getUserName(), zp.getText(advancedTaskFileReader.getTaskCreatorNameInTaskList()));

        taskOpreator.changePriority(zp,Urgency.PRIOR);
        zp.refresh(advancedTaskFileReader.getTaskDeadlineInTaskList());
        taskOpreator.changeDeadline(zp,advancedTaskFileReader.getDeadlineButtonForTomorrow());
        assertEquals("任务列表中截至日期无法改成明天","明天", zp.getText(advancedTaskFileReader.getTaskDeadlineInTaskList() + ".tomorrow"));
        assertEquals("任务列表中正常处理的任务的灯的数目不对",Urgency.PRIOR.getValue(),
                advancedTaskChecker.getPriorityLightCount(0, advancedTaskFileReader.getTaskPriorityButtonInTaskList(),advancedTaskFileReader.getTaskPriorityIcon(),zp));

        zp.clickElement(advancedTaskFileReader.getTaskFinishButton());
        waitOperator(zp);
        assertTrue("完成任务后任务没有打钩", zp.elementIsPresent(advancedTaskFileReader.getTaskFinishButton() + ".done", 5));

        zp.clickElement(advancedTaskFileReader.getTaskFinishButton());
        waitOperator(zp);
        assertFalse("完成任务后任务没有打钩", zp.elementIsPresent(advancedTaskFileReader.getTaskFinishButton() + ".done", 2));

        //删除这个任务不然会影响其他测试
        zp.clickElement(".project.tasks  .right.more",advancedTaskFileReader.getTaskItem());
        taskOpreator.removeTask(zp,0);
    }




    @Test(timeout = 300000)
    public void test03_2_MDFeatureInTaskDescription() throws ParseException {
        cp.navigate(zombieProject.getProjectTaskLink(), advancedTaskFileReader.getTaskContentInput());
        MarkDownInputBox taskDescriptionMDBox = new MarkDownInputBox(advancedTaskFileReader.getTaskDescriptionMDBox());
        cp.sendKeys(advancedTaskFileReader.getTaskContentInput(),"ddd");
        taskDescriptionMDBox.sendMarkdownContent(cp, ciwangUser, zombieUser,taskDescriptionMDBox.getComplicateImageSender());
        taskDescriptionMDBox.testMarkDownPreview(cp, advancedTaskFileReader.getTaskDescriptionMDBoxPreviewInTaskList());
        cp.clearAndSendKeys(advancedTaskFileReader.getTaskContentInput(), taskContentForDescriptionTest);
        int taskCount = cp.getElementCount(advancedTaskFileReader.getTaskTimeInTaskList());
        cp.clickElement(advancedTaskFileReader.getTaskSendConfirmButton());
        cp.waitForItemCountChange(advancedTaskFileReader.getTaskTimeInTaskList(), taskCount, 10);
        cp.clickElement(advancedTaskFileReader.getTaskDescriptionButton(), advancedTaskFileReader.getTaskDescriptionInTaskList());
        cp.waitLoadingIconInvisible();
        taskDescriptionMDBox.verifyMarkdownContent(cp,"正文", advancedTaskFileReader.getTaskDescriptionInTaskList(), true);

        forwardTaskDetailPage(cp, 0);
        taskDescriptionMDBox.verifyMarkdownContent(cp,"正文", advancedTaskFileReader.getTaskDescriptionInTaskList(), true);
    }
    @Test(timeout = 300000)
    public void test03_3_supplyDescription(){
        taskOpreator.sendSimpleTask(cp, zombieUser.getUserName(), "task description", zombieProject.getProjectTaskLink());
        assertEquals("已发送的没有描述的任务没有出现添加描述按钮", "添加描述",cp.getText(advancedTaskFileReader.getTaskItem(), 0,
                advancedTaskFileReader.getTaskDescriptionButton()));

        forwardTaskDetailPage(cp, 0);
        cp.clickElement(advancedTaskFileReader.getTaskDescriptionSupplyButtonInDetailPage(),
                advancedTaskFileReader.getTaskDescriptionMDInDetailPage());
        cp.assertElementPresent("任务详情页补充描述按钮失效", advancedTaskFileReader.getTaskDescriptionMDInDetailPage() );
        editDescriptionInDetailPageTest(taskEditDescriptionContent);
    }


    @Test(timeout = 300000)
    public void test03_0_TaskDescriptionTest() throws ParseException {
        cp.navigate(zombieProject.getProjectTaskLink(), advancedTaskFileReader.getTaskContentInput());
        taskOpreator.sendSimpleTaskWithDescription(cp, taskContentForDescriptionTest,"task description");
        cp.assertElementPresent("已发送的有描述的任务没有出现描述按钮", advancedTaskFileReader.getTaskItem(), 0,
                advancedTaskFileReader.getTaskDescriptionButton());
        cp.assertIconAndText("已发送的带描述的任务,描述", advancedTaskFileReader.getTaskDescriptionButton(),
                "描述", "rotate180");
        cp.clickElement(advancedTaskFileReader.getTaskDescriptionButton(), advancedTaskFileReader.getTaskDescriptionInTaskList());
        cp.assertElementPresent("任务列表页无法打开描述", advancedTaskFileReader.getTaskDescriptionInTaskList());
        cp.clickElement(advancedTaskFileReader.getTaskDescriptionButton());
        cp.assertElementNotPresent("任务列表页无法收起描述", advancedTaskFileReader.getTaskDescriptionInTaskList());

        String taskLink = cp.getLink(advancedTaskFileReader.getTaskTimeInTaskList());
        forwardTaskDetailPage(cp, 0);

        cp.assertElementPresent("任务详情页的任务描述图标消失", advancedTaskFileReader.getTaskDescriptionIcon());
        cp.clickElement(advancedTaskFileReader.getTaskDescriptionEditButton(), advancedTaskFileReader.getTaskDescriptionMDInDetailPage());
        cp.assertElementPresent("任务详情页点击编辑描述，任务描述框没有打开",advancedTaskFileReader.getTaskDescriptionEditButton(),5);
        cp.clickElement(advancedTaskFileReader.getTaskDescriptionEditButton(), advancedTaskFileReader.getTaskDescriptionContentInDetailPage());
        cp.assertElementPresent("任务详情页取消编辑描述，任务描述框没有关闭",advancedTaskFileReader.getTaskDescriptionEditButton(),5);
        cp.clickElement(advancedTaskFileReader.getTaskDescriptionEditButton(), advancedTaskFileReader.getTaskDescriptionMDInDetailPage());

        editDescriptionInDetailPageTest(taskEditDescriptionContent);

        advancedActivity.constructEditDescriptionActivity(taskLink, taskContentForDescriptionTest, taskEditDescriptionContent);
        advancedActivity.checkTaskActivity(zp,0,0);
    }
    private void editDescriptionInDetailPageTest(String inputDescription){
        cp.clearAndSendKeys(taskDescriptionEditor.getBoxInputSelector(), inputDescription);
        taskDescriptionEditor.clickMDConfirmButton(cp);
        cp.assertTextContainWords("任务描述编辑的内容消失", inputDescription, advancedTaskFileReader.getTaskDescriptionContentInDetailPage());
        cp.refresh( advancedTaskFileReader.getTaskDescriptionIcon());
        assertActivityInTaskDetailPage(ciwangUser, "任务详情页修改任务描述", 1, "更新了任务描述", advancedTaskFileReader.getTaskDescriptionIcon());
    }

    public void forwardTaskDetailPage(Page cp, int taskNum){
        cp.navigate(getTaskLinkInTaskList(cp, taskNum), advancedTaskFileReader.getTaskCommentMDBoxInDetailPage());
    }
    public void assertActivityInTaskDetailPage(User sponsor, String message, int itemNum,String action, String selectorForIcon){
        Page cp = sponsor.getPage();
        message = "任务详情页" + message;
        cp.waitForElement(advancedTaskFileReader.getTaskDetailActivity() + ":nth-child(" + (itemNum + 1) + ")", 10);
        cp.assertElementPresent(message + "动态图标", advancedTaskFileReader.getTaskDetailActivity(), itemNum, selectorForIcon);
        cp.assertTextEqualsOneFromThreeExpect(message + "动态", sponsor.getUserName() + " " + action + " - 几秒前"
                , sponsor.getUserName() + " " + action + " - 几秒内", sponsor.getUserName() + " " + action + " - 1分钟前", advancedTaskFileReader.getTaskDetailActivity(), itemNum);
    }
    public void removeTaskInDetailPage(Page cp)  {
        String processingTaskCountText = cp.getText(advancedTaskFileReader.getTaskInnerMenuIndex());
        cp.assertTextEquals("任务详情页的删除按钮", "删除", advancedTaskFileReader.getTaskRemoveButtonIndetailPage());
        cp.checkAlert(advancedTaskFileReader.getTaskRemoveButtonIndetailPage(),
                "任务详情页的删除有误", "确认删除该任务？");
        cp.waitForElement(advancedTaskFileReader.getTaskContentInput(),6);
        cp.waitForContentChange(advancedTaskFileReader.getTaskInnerMenuIndex(),processingTaskCountText,10);
    }


    public void filterActivityInTaskDetailPage(Page cp, String taskLinkUrl){
        cp.navigate(taskLinkUrl + "/comment", advancedTaskFileReader.getTaskCommentMDBoxInDetailPage());
        assertEquals("点击查看全部评论后，任务详情页没有进入“只看评论”状态", "只看评论",
                cp.getText(advancedTaskFileReader.getTaskDetailActivitySelect()));
        assertTrue("任务详情页点击只看评论，却没有评论", cp.elementIsPresent(".task-activity.comment",2));
        assertFalse("任务详情页点击只看评论，却出现其他动态", cp.elementIsPresent(".task-activity.action", 2));
        cp.clickElement(advancedTaskFileReader.getTaskDetailActivitySelect());
        cp.clickElement(advancedTaskFileReader.getTaskDetailActivitySelect() + " .item");
        assertTrue("任务详情页点击全部动态，却没出现其他动态", cp.elementIsPresent(".task-activity.action", 2));
    }


    private void waitOperator(Page page){
        page.getWaitTool().waitForElement(By.cssSelector(advancedTaskFileReader.getTaskLoadingIcon()), 2);
        page.waitElementInvisible(advancedTaskFileReader.getTaskLoadingIcon());
    }

    @AfterClass
    public static void tearDown() throws Exception {
        zombieProject.deleteProject();
        zombieUser.getPage().getDriver().quit();
        ciwangUser.getPage().getDriver().quit();
    }
}


package org.ChiTest.Reference;

import dataReader.ConfigureInfo;
import org.ChiTest.AdvancedTask.AdvancedTask;
import org.ChiTest.AdvancedTask.AdvancedTaskChecker;
import org.ChiTest.AdvancedTask.AdvancedTaskFileReader;
import org.ChiTest.AdvancedTask.AdvancedTaskTest;
import org.ChiTest.Page.PS;
import org.ChiTest.Page.Page;
import org.ChiTest.Project.Project;
import org.ChiTest.Topic.Topic;
import org.ChiTest.Topic.TopicFileReader;
import org.ChiTest.User.User;
import org.ChiTest.WebComponent.MarkDownInputBox;
import org.ChiTest.WebComponent.ScreenShot;
import org.ChiTest.WebComponent.SearchBox.ReferenceSearchBox;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.Cookie;

import java.text.ParseException;

import static org.junit.Assert.assertTrue;

/**
 * Created by dugancaii on 5/12/2015.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReferenceTest {
    private static Page zp;
    private static User zombieUser;
    private static User ciwangUser;
    private static Cookie ciwangCookie;
    private static Page cp;
    private  static Cookie zombieCookie;
    private  static Logger log = Logger.getLogger("AdvancedTaskLog");
    private static Project zombieProject;
    private static AdvancedTask taskOpreator;
    private static AdvancedTaskFileReader advancedTaskFileReader;
    private static TopicFileReader topicFileReader;
    private static ReferenceFileReader referenceFileReader;
    private static AdvancedTaskTest advancedTaskTest;
    private static AdvancedTaskChecker advancedTaskChecker;

    @BeforeClass
    public static void  setUp() throws Exception {
        ConfigureInfo configureInfo = new ConfigureInfo(true, true);

        zp = configureInfo.getZombieUser().getPage();
        cp =  configureInfo.getCiwangUser().getPage();
        zombieUser = new User("coding48","coding48","123456",zp,"Fruit-3.png");
        ciwangUser = new User("dugancai","dugancai","123456",cp);

        ciwangCookie = cp.getStagingSid("ba1d49e5-1f83-4884-8bdd-187fcb2d899b");
        zombieCookie = zp.getStagingSid("0435d6dc-882a-4abb-89ac-28516e40564e");

        zombieUser.autoLogin(configureInfo.getLoginUrl(),zombieCookie );
        ciwangUser.autoLogin(configureInfo.getLoginUrl(), ciwangCookie);

        advancedTaskFileReader = new AdvancedTaskFileReader();
        topicFileReader = new TopicFileReader();

        //zombieProject = new Project(zombieUser, "TaskPrivate", "private");
        zombieProject = new Project(zombieUser, "Task"+cp.getMark(), "private");
        //zombieProject.deleteAllProject();
        zombieProject.createFixtureProject();

        advancedTaskChecker = new AdvancedTaskChecker(zombieProject);
        taskOpreator = new AdvancedTask();
        zombieProject.addProjectMember( ciwangUser);
        advancedTaskTest = new AdvancedTaskTest();
        referenceFileReader = new ReferenceFileReader();
    }
    @Rule
    public ScreenShot screenShotZombie = new ScreenShot(zombieUser);
    @Rule
    public ScreenShot screenShotCiwang = new ScreenShot(ciwangUser);

    @Test
    public void test01_0_TaskReference() throws ParseException, InterruptedException {
        MarkDownInputBox taskDescriptionMDBox = new MarkDownInputBox(advancedTaskFileReader.getTaskDescriptionMDBox());
        cp.navigate(zombieProject.getProjectTaskLink());
        for (int i = 0; i < 2 ; i++) {
            taskOpreator.sendSimpleTask(cp, zombieUser.getUserName(), "分页填充物" + i, zombieProject.getProjectTaskLink());
        }

        AdvancedTaskReference referenceOne = createAdvancedTaskReference(cp,0);
        AdvancedTaskReference referenceTwo = createAdvancedTaskReference(cp,1);

        assertTrue("任务引用没有自动递增,或列表中任务引用消失",referenceOne.getReferenceId() > referenceTwo.getReferenceId());

        cp.sendKeys(advancedTaskFileReader.getTaskContentInput(), "sdfsdfsd");
        cp.testSelectHintItemWithKeyboard("任务引用", referenceFileReader.getTaskSelectedReferenceHintItem(),
                taskDescriptionMDBox.getBoxInputSelector(), "#分页");

        referenceOne.searchReferenceWithContent(cp, taskDescriptionMDBox.getBoxInputSelector());
    }
    @Test(timeout = 300000)
    public void test01_1_TaskPaging(){
        cp.navigate(zombieProject.getProjectTaskLink());
        sendEnoughTaskForPaging(cp);
        cp.testPageTurning(zombieProject.getProjectTaskLink() + "/processing");
        cp.testPageTurning(zombieProject.getProjectTaskLink() + "/watch");
        cp.testPageTurning(zombieProject.getProjectTaskLink(zombieUser.getUserLoginName()));
        cp.testPageTurning(zombieUser.getMyTaskLink());
    }
    @Test
    public void test01_2_ShowMoreReference() throws ParseException, InterruptedException{
        MarkDownInputBox taskDescriptionMDBox = new MarkDownInputBox(advancedTaskFileReader.getTaskDescriptionMDBox());
        cp.navigate(zombieProject.getProjectTaskLink());
        sendEnoughTaskForPaging(cp);

        AdvancedTaskReference referenceOne = createAdvancedTaskReference(cp,0);
        AdvancedTaskReference referenceTwo = createAdvancedTaskReference(cp,2);
        taskOpreator.removeTask(cp, 0);

        ReferenceSearchBox referenceSearchBox = new ReferenceSearchBox(referenceFileReader.getTaskReferenceSearchBox(),
                referenceFileReader.getTaskReferenceItemInSearchBox(),referenceFileReader.getTaskShowMoreReferenceResult());

        cp.sendKeys(advancedTaskFileReader.getTaskContentInput(),"sdfsdfsd");
        taskDescriptionMDBox.sendKeys(cp, "#");

        referenceSearchBox.openBox(cp);
        referenceSearchBox.assertProjectTitleInSearchBox(cp, zombieProject);
        referenceSearchBox.assertTabChangeAndNoResultInSearchBox(cp);
        referenceSearchBox.assertResultPagingInSearchBox(cp);
        referenceSearchBox.assertSearchContent(cp, referenceTwo);
        referenceSearchBox.assertSearchId(cp, referenceTwo);
        referenceSearchBox.assertSearchDeletedIdInSearchBox(cp, referenceOne);
        referenceSearchBox.assertSearchWrongIdInSearchBox(cp);
        referenceSearchBox.close(cp);
    }
    @Test
    public void test03_1_ReferenceInTaskDescription() throws ParseException {
        cp.navigate(zombieProject.getProjectTaskLink(),advancedTaskFileReader.getTaskContentInput());
        taskOpreator.sendSimpleTask(cp, ciwangUser.getUserName(), "for reference test", zombieProject.getProjectTaskLink() );
        AdvancedTaskReference advancedTaskReference = createAdvancedTaskReference(cp, 0);
        testReferenceInDescription(advancedTaskReference);
        testReferenceInDetailPage(advancedTaskReference);

        cp.navigate(ciwangUser.getMyTaskLink(), advancedTaskFileReader.getTaskProjectIndexItem());
        cp.clickElement(advancedTaskFileReader.getTaskProjectIndexItem(),  0);
        taskOpreator.sendSimpleTask(cp, ciwangUser.getUserName(), "for my task page test", ciwangUser.getMyTaskLink() );
        testReferenceInDescription(createAdvancedTaskReference(cp,0));
    }

    @Test
    public void test05_1_commentBoxReferenceInDetailPage() throws ParseException {
        cp.navigate(zombieProject.getProjectTaskLink(),advancedTaskFileReader.getTaskContentInput());
        taskOpreator.sendSimpleTask(cp, ciwangUser.getUserName(), "reference test", zombieProject.getProjectTaskLink() );
        String taskLinkUrl = advancedTaskTest.getTaskLinkInTaskList(cp, 0);
        advancedTaskTest.forwardTaskDetailPage(cp,0);

        MarkDownInputBox commentMDBox = new MarkDownInputBox(advancedTaskFileReader.getTaskCommentMDBoxInDetailPage());
        AdvancedTaskReference advancedTaskReference =
                new AdvancedTaskReference(Integer.parseInt(cp.getText(referenceFileReader.getTaskReferenceIdInDetailPage()).replace("#","")),
                        cp.getAttribute(advancedTaskFileReader.getTaskContentInTaskList(),"value"),taskLinkUrl);
        advancedTaskReference.verifyErrorReference(cp, commentMDBox.getBoxInputSelector(), "任务描述框" );
        advancedTaskReference.sendReferenceInMarkDown(cp, commentMDBox.getBoxInputSelector());
        advancedTaskReference.verifyReference(cp, "任务详情页的评论");
    }
    @Test(timeout = 300000)
    public void test07_taskReferenceInTopicComment() throws InterruptedException {
        zp.navigate(zombieProject.getProjectTaskLink());
        zp.waitElementInvisible(".loading.icon");
        String topicTask = "taskInTopic";
        if(!zp.elementIsPresent(advancedTaskFileReader.getTaskItem(),16)){
            (new AdvancedTask()).sendSimpleTask(zp, ciwangUser.getUserName(), topicTask, zombieProject.getProjectTaskLink());
        }
        AdvancedTaskReference referenceOne =  createAdvancedTaskReference(zp, 0);
        Topic topic1 = new Topic(zombieProject, "topicForTaskReference","ddd",zombieUser);

        topic1.deleteTopic();
        topic1.createTopic();
        if(!zp.elementIsPresent(topicFileReader.getCommentInput(),2)) {
            zp.navigate(zombieProject.getProjectTopicHomeLink(), topicFileReader.getItemInItemHome());
            zp.clickElement(topicFileReader.getTopicTitleInItemHome(), topic1.findTopicInTopicHomeByTitle(zp), topicFileReader.getCommentInput());
        }
        referenceOne.verifyErrorReference(zp, topicFileReader.getCommentInput(), "讨论的评论");
        referenceOne.sendReferenceInMarkDown(zp, topicFileReader.getCommentInput());
        referenceOne.verifyReference(zp, "讨论的评论");
        zp.checkAlert(topicFileReader.getDeleteButton(), "删除讨论后弹出弹框，点击取消或点击确认跳转出错", "确认删除该讨论？");
    }

    private void testReferenceInDescription(AdvancedTaskReference reference) throws  ParseException {
        taskOpreator.sendSimpleTaskWithDescription(cp,"TaskReference", "#12345678 #"+reference.getReferenceId());
        cp.clickElement(advancedTaskFileReader.getTaskDescriptionButton(), advancedTaskFileReader.getTaskDescriptionInTaskList());
        reference.verifyReference(cp, "任务列表中的描述");
    }
    private void testReferenceInDetailPage(AdvancedTaskReference reference){
        advancedTaskTest.forwardTaskDetailPage(cp,0);
        reference.verifyReference(cp, "任务详情页的描述");
        advancedTaskTest.removeTaskInDetailPage(cp);
    }
    public AdvancedTaskReference createAdvancedTaskReference(Page cp, int taskNum){
        return  new AdvancedTaskReference(Integer.parseInt(cp.getText(referenceFileReader.getTaskReferenceIdInTaskList(),taskNum).replace("#","")),
                cp.getAttribute(advancedTaskFileReader.getTaskContentInTaskList(),"value",taskNum),cp.getLink(advancedTaskFileReader.getTaskTimeInTaskList(),taskNum));
    }
    public void sendEnoughTaskForPaging(Page cp){
        cp.refresh(advancedTaskFileReader.getTaskItem());
        if(!cp.elementIsPresent(cp.getPagingSelector(), PS.midWait)) {
            int restTaskCountForPaging = 12 - cp.getElementCount(advancedTaskFileReader.getTaskItem());
            for (int i = 0; i < restTaskCountForPaging ; i++) {
                taskOpreator.sendSimpleTask(cp, zombieUser.getUserName(), "分页填充物" + i, zombieProject.getProjectTaskLink());
            }
            cp.refresh(advancedTaskFileReader.getTaskItem());
        }
    }

    @AfterClass
    public static void tearDown(){
        cp.getDriver().quit();
        zp.getDriver().quit();
    }

}

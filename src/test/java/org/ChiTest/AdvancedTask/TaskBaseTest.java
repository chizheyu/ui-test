package org.ChiTest.AdvancedTask;

import classForComplicatedData.CountIndexInMyTaskPage;
import classForComplicatedData.CountIndexInProjectPage;
import classForComplicatedData.TaskCountIndex;
import classForComplicatedData.Urgency;
import dataReader.ConfigureInfo;
import org.ChiTest.Activity.AdvancedActivity;
import org.ChiTest.Email.Email;
import org.ChiTest.Email.EmailTest;
import org.ChiTest.Notification.AdvancedNotificationTest;
import org.ChiTest.Notification.NotificationFileReader;
import org.ChiTest.Page.Page;
import org.ChiTest.Project.Project;
import org.ChiTest.User.User;
import org.ChiTest.WebComponent.ScreenShot;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Keys;

import java.text.ParseException;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by dugancaii on 4/9/2015.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TaskBaseTest {
    private static Page zp;
    private static User zombieUser;
    private static User ciwangUser;
    private static Cookie ciwangCookie;
    private static Page cp;
    private  static Cookie zombieCookie;
    private  static Logger log = Logger.getLogger("AdvancedTaskLog");
    private static Project zombieProject;
    private static Project ciwangProject;
    private static  AdvancedTask advancedTask;
    private static  AdvancedTask taskOpreator;
    private static AdvancedTaskFileReader advancedTaskFileReader;
    private static CountIndexInProjectPage indexBeforeChange;
    private static CountIndexInProjectPage indexAfterChange ;
    private static CountIndexInMyTaskPage myTaskIndexAfterChange ;
    private static CountIndexInMyTaskPage myTaskIndexBeforeChange ;
    private static String updatedTaskContent;
    private static String updatedTaskContentInDetailPage;
    private static String watchTaskContent;
    private static NotificationFileReader notificationFileReader;
    private static AdvancedActivity advancedActivity;
    private static AdvancedNotificationTest advancedNotificationTest;
    private static AdvancedTaskChecker advancedTaskChecker;
    private static String classifiedTaskContent;
    private static Set mailCheckList;
    private static EmailTest emailTest;
    public TaskBaseTest(){
        advancedTaskFileReader = new AdvancedTaskFileReader();
        notificationFileReader = new NotificationFileReader();
    }

    @BeforeClass
    public static void  setUp() throws Exception {
        ConfigureInfo ConfigureInfo = new ConfigureInfo(true,true);

        zp = ConfigureInfo.getZombieUser().getPage();
        cp =  ConfigureInfo.getCiwangUser().getPage();
        zombieUser = new User("wangyiA163_-40","coding500","123456",zp,"Fruit-3.png");
        ciwangUser = new User("czy","coding404","123456",cp,"Fruit-2.png");

        zombieCookie = zp.getStagingSid("95d1d6b5-4df2-4974-b2f3-d2f3d4862a61");
        ciwangCookie = cp.getStagingSid("62443776-8180-4dec-b245-93de294d0661");
        emailTest = new EmailTest(new Email("coding_test40", "163.com","chi2014"));
        zombieUser.autoLogin(ConfigureInfo.getLoginUrl(), zombieCookie);
        ciwangUser.autoLogin(ConfigureInfo.getLoginUrl(), ciwangCookie);

        advancedTaskFileReader = new AdvancedTaskFileReader();

        //zombieProject = new Project(zombieUser, "TaskPrivate", "private");
        zombieProject = new Project(zombieUser, "Task"+cp.getMark(), "private");
        zombieProject.deleteAllProject();
        zombieProject.createFixtureProject();
        ciwangProject = new Project(ciwangUser, "Task", "private");
        ciwangProject.deleteAllProject();

        updatedTaskContent = "测试内容已修改" + ((new Date()).getTime()/1000);
        watchTaskContent="watchTaskContent";
        advancedActivity = new AdvancedActivity(ciwangUser,zombieUser,zombieProject);
        advancedNotificationTest = new AdvancedNotificationTest(ciwangUser,zombieUser,zombieProject);
        advancedTaskChecker = new AdvancedTaskChecker(zombieProject);
        updatedTaskContentInDetailPage = "任务详情页面的任务内容已修改." + ((new Date()).getTime()/1000);
        mailCheckList = new HashSet();
        indexAfterChange = new CountIndexInProjectPage(ciwangUser ,zombieUser);
        indexBeforeChange = new CountIndexInProjectPage(ciwangUser, zombieUser);
        taskOpreator = new AdvancedTask();
        classifiedTaskContent = "task classify" +  ((new Date()).getTime()/1000);

        zombieProject.addProjectMember( ciwangUser);
    }

    @Rule
    public ScreenShot screenShotZombie = new ScreenShot(zombieUser);
    @Rule
    public ScreenShot screenShotCiwang = new ScreenShot(ciwangUser);
    @Test(timeout = 1000000)
    public void test01_0_ProcessingTaskInProject() throws ParseException, InterruptedException {
        //  taskOpreator.deleteAllTasks(zombieProject);
        zp.navigate(zp.getNotificationLink(), notificationFileReader.getNotificationContent());
        zp.checkAlert(notificationFileReader.getReadAll(), "标记所有通知为已读的按钮或alert弹框有问题，建议确认和取消都试下",
                "这将会把您所有接收到的通知标记为已读（包括没有在本页显示的），确定继续？");

        cp.navigate(zombieProject.getProjectTaskLink(), advancedTaskFileReader.getDeadlineButtonForCalendarIcon());
        cp.waitLoadingIconInvisible();
        assertEquals("没有处理任务，的提示消失", "没有正在处理的任务，喝杯茶休息下！", cp.getText(advancedTaskFileReader.getTaskEmptyHint()));
        deadlineInputTest(cp);
        priorityInputTest(cp);

        executiveInputTest(cp, zombieUser);
        taskCountIconTest(cp);
        sendStandardTaskTestInProjectHome(cp);
        updateTaskTest(cp, updatedTaskContent);
        finishTaskTest(cp, zombieUser, indexBeforeChange,indexAfterChange);
        changeExecutiveTest(cp, ciwangUser.getUserName());
        taskOpreator.changeExecutive(cp, zombieUser.getUserName());
        checkTaskOrderAndUpdatePriorityWithKeyboard(cp);
        advancedTask.setTaskUrl(cp.getLink(advancedTaskFileReader.getTaskTimeInTaskList()));

        removeTaskInFirstTaskTest(cp,zombieUser,indexBeforeChange,indexAfterChange);
        advancedActivity.constructActivityMap(advancedTaskChecker.getActivityContent(), updatedTaskContent, advancedTask);
        advancedActivity.checkTaskActivity(zp, 2, 3);
        advancedNotificationTest.taskNotificationConstructor(advancedTask, updatedTaskContent, mailCheckList,notificationFileReader.getSystemItemIcon());
        advancedNotificationTest.checkTaskNotification(zp);
        emailTest.checkEmail(zp,mailCheckList);
    }
    private void changeExecutiveTest(Page page, String executiveName) {
        indexBeforeChange.updateTaskIndex(cp);
        taskOpreator.changeExecutive(page, executiveName);
        indexForChangeExecutiveTest(page);

    }
    private void indexForChangeExecutiveTest(Page page)  {
        indexAfterChange.updateTaskIndex(page);
        indexBeforeChange.resignTaskIndexTest(zombieUser.getUserName(), ciwangUser.getUserName(), indexAfterChange);
    }
    public void taskCountIconTest(Page page){
        assertTrue("所有任务的图标消失", page.elementIsPresent(advancedTaskFileReader.getAllTaskItem(), 5));
        assertTrue("正在进行的图标消失", page.elementIsPresent(advancedTaskFileReader.getProcessingTaskItem(),5));
        assertTrue("已完成的图标消失", page.elementIsPresent(advancedTaskFileReader.getFinishTaskItem(), 5));
        assertTrue("‘我关注的’的图标消失", page.elementIsPresent(advancedTaskFileReader.getWatchTaskItem(),5));
    }
    private void executiveInputTest(Page page, User executive){
        if(!page.clickElement(advancedTaskFileReader.getTaskExecutiveButtonForCreate(), advancedTaskFileReader.getTaskExecutiveSelectedItem())){
            page.clickElement(advancedTaskFileReader.getTaskExecutiveButtonForCreate(), advancedTaskFileReader.getTaskExecutiveSelectedItem());
        }
        assertTrue("选择任务执行者时，选择列表中被选中的用户前面的check没了",
                page.elementIsPresent(advancedTaskFileReader.getTaskExecutiveSelectedItem() + ".active .checkmark.icon", 10));
        assertTrue("选择任务执行者时，选择列表中用户的头像没了",
                page.elementIsPresent(advancedTaskFileReader.getTaskExecutiveSelectedItem()+" .avatar.image",5));
        assertTrue("选择任务执行者时，选择列表中用户的用户名没了",
                page.elementIsPresent(advancedTaskFileReader.getTaskExecutiveSelectedItem() + "  span[bo-text='user.name']", 5));
        page.clearAndSendKeys(advancedTaskFileReader.getTaskExecutiveInput(),executive.getUserName());
        page.clickElement(advancedTaskFileReader.getTaskExecutiveSelectedItem());
        assertTrue("选择任务的执行者后，执行者的头像不对",
                page.getAttribute(advancedTaskFileReader.getTaskExecutiveButtonForCreate(), "src").contains(executive.getAvatar()));
    }
    private void checkTaskOrderAndUpdatePriorityWithKeyboard(Page page) throws InterruptedException {
        // 用按键发
        taskOpreator.sendSimpleTask(page,zombieUser.getUserName(),"测试优先级排序用的", zombieProject.getProjectTaskLink());
        taskOpreator.changeDeadline(page,advancedTaskFileReader.getDeadlineButtonForTomorrow());
        page.refresh(advancedTaskFileReader.getTaskItem());
        assertEquals("任务列表中优先级高的任务没有排到前面1", updatedTaskContent,
                page.getAttribute(advancedTaskFileReader.getTaskContentInTaskList(), "value"));

        decreaseTaskPriorityWithKeyboard(page, 0);
        assertEquals("用向下键修改任务列表中的优先级后，灯的数目不对",0,
                advancedTaskChecker.getPriorityLightCount(0, advancedTaskFileReader.getTaskPriorityButtonInTaskList(), advancedTaskFileReader.getTaskPriorityIcon(), page));
        page.refresh(advancedTaskFileReader.getTaskItem());
        assertEquals("任务列表中优先级高的任务没有排到前面2",updatedTaskContent,
                page.getAttribute(advancedTaskFileReader.getTaskContentInTaskList(), "value",1));

        increaseTaskPriorityWithKeyboard(page,1);
        assertEquals("用向上键修改任务列表中的优先级后，灯的数目不对",2,
                advancedTaskChecker.getPriorityLightCount(1, advancedTaskFileReader.getTaskPriorityButtonInTaskList(), advancedTaskFileReader.getTaskPriorityIcon(), page));
        page.refresh(advancedTaskFileReader.getTaskItem());
        assertEquals("任务列表中优先级高的任务没有排到前面3", updatedTaskContent,
                page.getAttribute(advancedTaskFileReader.getTaskContentInTaskList(), "value"));
        taskOpreator.removeTask(page, 1);
    }

    private void sendStandardTaskTestInProjectHome(Page cp){
        indexBeforeChange = new CountIndexInProjectPage(cp, ciwangUser,zombieUser);
        advancedTask = advancedTaskChecker.sendStandardTaskInProjectPage( ciwangUser, zombieUser, zombieProject);
        advancedTaskChecker.verifyTaskContent(cp, advancedTask);
        indexAfterChange = new CountIndexInProjectPage(cp, ciwangUser,zombieUser);
        indexBeforeChange.createTaskIndexTest(zombieUser.getUserName(), indexAfterChange);
    }
    @Test(timeout = 300000)
    public void test01_1_TaskClassify(){
        taskOpreator.deleteAllTasks(zombieProject);
        taskOpreator.deleteMyWatchedTask(ciwangUser);
        cp.navigate(zombieProject.getProjectTaskLink(), advancedTaskFileReader.getTaskContentInput());
        taskOpreator.deleteAllTasksInOneTaskClassify(cp);

        taskOpreator.sendSimpleTask(cp, zombieUser.getUserName(), classifiedTaskContent, zombieProject.getProjectTaskLink());
        increaseTaskPriorityWithKeyboard(cp,0);
        taskOpreator.sendSimpleTask(cp, zombieUser.getUserName(), "forOrder" + classifiedTaskContent, zombieProject.getProjectTaskLink());
        increaseTaskPriorityWithKeyboard(cp,0);
        finishFirstTaskInList(cp);

        cp.refresh(advancedTaskFileReader.getTaskItem());
        verifyTaskClassify(cp, 0, "所有任务");
        assertEquals("已关注的任务排在为关注的任务之前了", classifiedTaskContent,
                cp.getAttribute(advancedTaskFileReader.getTaskContentInTaskList(),"value") );
        taskOpreator.removeTask(cp, 1);
        finishFirstTaskInList(cp);

        verifyTaskClassify(cp, 2, "已完成");
        finishFirstTaskInList(cp);
        verifyTaskClassify(cp,3,"我关注的");

        cp.clickElement(advancedTaskFileReader.getTaskMemberIndexRate(),
                cp.getIndexPosition(zombieUser.getUserName(), advancedTaskFileReader.getTaskMemberIndexItem()));
        waitOperator(cp);
        cp.assertAttributeEquals(zombieUser.getUserName()+"中的任务内容",classifiedTaskContent,advancedTaskFileReader.getTaskContentInTaskList(), "value" );

        zp.refresh(zombieUser.getMyTaskLink(), advancedTaskFileReader.getTaskItem());
        verifyTaskClassify(zp,0,"我的任务页面所有任务");

        zp.clickElement(advancedTaskFileReader.getTaskProjectIndexRate(),
                zp.getIndexPosition(zombieProject.getProjectName(), advancedTaskFileReader.getTaskProjectIndexItem()));
        zp.assertAttributeEquals(zombieProject.getProjectName() + "中的任务内容" , classifiedTaskContent, advancedTaskFileReader.getTaskContentInTaskList(), "value");

        finishFirstTaskInList(zp);
        verifyTaskClassify(zp, 2, "已完成");

        cp.refresh(zombieUser.getMyTaskLink(), advancedTaskFileReader.getTaskCountInHeader());
        finishFirstTaskInList(zp);
        verifyTaskClassify(cp,3,"我关注的");

        taskOpreator.removeTask(cp, 0);
    }
    private void finishFirstTaskInList(Page zp){
        zp.clickElement(advancedTaskFileReader.getTaskFinishButton());
        waitOperator(zp);
    }
    private void verifyTaskClassify(Page cp, int num,String classificationName){
        cp.clickElement(advancedTaskFileReader.getTaskInnerMenuIndex(), num, advancedTaskFileReader.getTaskItem());
        cp.assertAttributeEquals(classificationName + "中的任务内容", classifiedTaskContent, advancedTaskFileReader.getTaskContentInTaskList(), "value");
    }
    private void increaseTaskPriorityWithKeyboard(Page page, int  taskNum){
        page.clickElement(advancedTaskFileReader.getTaskContentInTaskList(), taskNum);
        page.getBuilder().sendKeys(Keys.UP).perform();
        page.getBuilder().sendKeys(Keys.UP).perform();
        waitOperator(page);
    }
    private void decreaseTaskPriorityWithKeyboard(Page page, int taskNum){
        page.clickElement(advancedTaskFileReader.getTaskContentInTaskList(), taskNum);
        page.getBuilder().sendKeys(Keys.DOWN).perform();
        page.getBuilder().sendKeys(Keys.DOWN).perform();
        waitOperator(page);
    }
    private void priorityInputTest(Page page){
        page.clickElement(advancedTaskFileReader.getTaskPriorityButton(), advancedTaskFileReader.getTaskPriorityChoice());
        page.waitForElement(advancedTaskFileReader.getTaskPriorityChoice(), 15);
        assertTrue("被选中的任务优先级前面的check没了",
                page.elementIsPresent(advancedTaskFileReader.getTaskPriorityChoice() + ".active .checkmark.checked", 5));
        page.waitForContentNotNull(advancedTaskFileReader.getTaskPriorityChoice(), 0, 15 );
        String itemName[] = {"有空再看","正常处理","优先处理","十万火急"};
        for(int i = 0; i< itemName.length;i++) {
            assertEquals("优先级列表中，“" + itemName[i] + "”消失了", itemName[i], page.getText(advancedTaskFileReader.getTaskPriorityChoice(),
                    3- i));
            assertEquals("优先级列表中，“" + itemName[i] + "”的灯的数目不对了", i,
                    advancedTaskChecker.getPriorityLightCount(3-i,
                            advancedTaskFileReader.getTaskPriorityChoice(), advancedTaskFileReader.getTaskPriorityIcon(), page));
        }

        //verifyPriorityInputItem("有空再看", 3,page);
        //verifyPriorityInputItem("正常处理", 2,page);
        verifyPriorityInputItem("优先处理", 1,page);

        page.clickElement(advancedTaskFileReader.getTaskPriorityButton());
        page.waitElementInvisible(advancedTaskFileReader.getTaskPriorityChoice());
    }
    private void verifyPriorityInputItem(String itemName, int priorityItemNum, Page page){
        page.clickElement(advancedTaskFileReader.getTaskPriorityChoice(), priorityItemNum);
        assertEquals("选择优先级“" + itemName + "”后灯的数目不对了", 3 - priorityItemNum, advancedTaskChecker.getPriorityLightCount(priorityItemNum,
                advancedTaskFileReader.getTaskPriorityChoice(), advancedTaskFileReader.getTaskPriorityIcon(), page));
        page.clickElement(advancedTaskFileReader.getTaskPriorityButton(), advancedTaskFileReader.getTaskPriorityChoice());
    }

    @Test(timeout = 300000)
    public void test02_0_watchTaskInProcessingPage() throws InterruptedException {
        zp.navigate(zombieProject.getProjectTaskLink(),
                advancedTaskFileReader.getTaskContentInput());
        indexBeforeChange = new CountIndexInProjectPage(zp, ciwangUser,zombieUser);
        indexAfterChange = new CountIndexInProjectPage(zp, ciwangUser,zombieUser);
        taskOpreator.sendSimpleTask(zp, ciwangUser.getUserName(), watchTaskContent, zombieProject.getProjectTaskLink());
        assertWatchCount(zp,"发送一个任务给别人后，我关注的任务计数没有加一",   indexBeforeChange.getWathchedTaskCount() + 1);
        zp.assertIconAndText("已发送的任务上没有显示 \"已关注\"",
                advancedTaskFileReader.getTaskWatchHoverButton() + " .marked ","已关注", "unhide",0);

        String watchCountIndex = zp.getText(advancedTaskFileReader.getTaskInnerMenuIndex(),3);
        taskOpreator.removeTask(zp, 0);
        zp.waitForContentChange(advancedTaskFileReader.getTaskInnerMenuIndex(), 3, watchCountIndex, 5);
        assertWatchCount(zp,"删除关注任务后，我关注的任务计数没有减一",   indexBeforeChange.getWathchedTaskCount() );
    }
    @Test(timeout = 300000)
    public void test02_1_transferWatchingTaskToOneself() throws InterruptedException {
        zp.navigate(zombieProject.getProjectTaskLink(),
                advancedTaskFileReader.getTaskContentInput());
        taskOpreator.sendSimpleTask(zp, ciwangUser.getUserName(), watchTaskContent, zombieProject.getProjectWatchedTaskLink());
        watchTestInTaskList(zp);
        taskOpreator.changeExecutive(zp,zombieUser.getUserName());
        zp.assertElementNotPresent("把任务转给自己后,已关注没有消失", advancedTaskFileReader.getTaskWatchHoverButton()," .marked");
    }

    @Test(timeout = 300000)
    public void test02_2_watchTaskInWatchingPage() throws InterruptedException {
        zp.navigate(zombieProject.getProjectTaskLink(),
                advancedTaskFileReader.getTaskContentInput());
        zp.clickElement(advancedTaskFileReader.getTaskInnerMenuIndex(), 3, advancedTaskFileReader.getTaskContentInput());
        taskOpreator.sendSimpleTask(zp, ciwangUser.getUserName(), watchTaskContent, zombieProject.getProjectWatchedTaskLink());
        assertFalse("在我关注的页面上，有task显示‘已关注’", zp.elementIsPresent(advancedTaskFileReader.getTaskWatched() + " .unhide.icon", 2));
        watchTestInTaskList(zp);
        taskOpreator.removeTask(zp, 0);
    }
    @Test(timeout = 600000)
    public void test03_MyTaskPageAndWatchNotification( ) throws InterruptedException {
        mailCheckList.clear();
        cp.navigate(ciwangUser.getMyTaskLink(), advancedTaskFileReader.getTaskItem());
        myTaskIndexBeforeChange = new CountIndexInMyTaskPage(cp, zombieProject.getProjectName());
        AdvancedTask watchedTask =  advancedTaskChecker.sendStandardTaskInMyTaskPage( ciwangUser, ciwangUser,zombieProject);
        watchedTask.setTaskUrl(getTaskLinkInTaskList(cp));
        zp.refresh(zombieProject.getProjectTaskLink(), advancedTaskFileReader.getTaskItem());
        advancedTaskChecker.verifyTaskContent(zp, watchedTask);

        myTaskIndexAfterChange = new CountIndexInMyTaskPage(cp,zombieProject.getProjectName());
        myTaskIndexBeforeChange.createTaskIndexTest(zombieUser.getUserName(), myTaskIndexAfterChange);

        zp.moveToElement(advancedTaskFileReader.getTaskWatchHoverButton());
        zp.clickElement(zp.getElement(advancedTaskFileReader.getTaskWatchToggle() + " span"));
        zp.waitElementInvisible(advancedTaskFileReader.getTaskLoadingIcon());

        updateTaskTest(cp, updatedTaskContent);

        finishTaskTest(cp, ciwangUser,myTaskIndexBeforeChange,myTaskIndexAfterChange);

        myTaskIndexBeforeChange.updateTaskIndex(cp);
        taskOpreator.changeExecutive(cp, zombieUser.getUserName());
        assertEquals("在我的任务页面转发一个任务给别人后，已关注没有出现", "已关注", cp.getText(advancedTaskFileReader.getTaskWatched()));
        myTaskIndexAfterChange.updateTaskIndex(cp);
        myTaskIndexBeforeChange.resignTaskIndexTest(ciwangUser.getUserName(),zombieUser.getUserName(), myTaskIndexAfterChange);
        taskOpreator.changeExecutive(cp, ciwangUser.getUserName());
        removeTaskInFirstTaskTest(cp,ciwangUser,myTaskIndexBeforeChange,myTaskIndexAfterChange);

        advancedNotificationTest.constructNotificationMapForUpdateTask(watchedTask,updatedTaskContent,mailCheckList, notificationFileReader.getSystemItemIcon());
        advancedNotificationTest.checkTaskNotification(zp);
        emailTest.checkEmail(zp,mailCheckList);
    }

    @Test(timeout = 600000)
    public void test04_watchOthersTaskGetNotification() throws InterruptedException {
        mailCheckList.clear();
        Project ciwangProject =  new Project(ciwangUser, "TaskPrivate", "private");
        ciwangProject.createSampleProject();
        ciwangProject.addProjectMember(zombieUser);
        AdvancedTask watchedTask =  advancedTaskChecker.sendStandardTaskInProjectPage( zombieUser, ciwangUser, ciwangProject);

        watchedTask.setTaskUrl(getTaskLinkInTaskList(zp));
        zp.navigate(ciwangProject.getProjectTaskLink(),advancedTaskFileReader.getTaskItem());

        cp.navigate(ciwangProject.getProjectTaskLink(), advancedTaskFileReader.getTaskItem() );
        updateTaskTest(cp, updatedTaskContent);

        cp.clickElement(advancedTaskFileReader.getTaskFinishButton());
        waitOperator(cp);
        assertTrue("完成任务后任务没有打钩", cp.elementIsPresent(advancedTaskFileReader.getTaskFinishButton() + ".done", 5));

        cp.clickElement(advancedTaskFileReader.getTaskFinishButton());
        waitOperator(cp);
        assertFalse("完成任务后任务没有打钩", cp.elementIsPresent(advancedTaskFileReader.getTaskFinishButton() + ".done", 1));
        taskOpreator.changeExecutive(cp, zombieUser.getUserName());
        taskOpreator.changeExecutive(cp, ciwangUser.getUserName());
        taskOpreator.removeTask(cp, 0);
        AdvancedNotificationTest advancedNotificationTest = new AdvancedNotificationTest(ciwangUser,zombieUser, ciwangProject);
        advancedNotificationTest.constructNotificationMapForUpdateTask(watchedTask,
                updatedTaskContent, mailCheckList, notificationFileReader.getSystemItemIcon());
        advancedNotificationTest.checkTaskNotification(zp);

        taskOpreator.sendSimpleTask(zp, ciwangUser.getUserName(), "取消关注测试任务", ciwangProject.getProjectTaskLink());
        zp.moveToElement(advancedTaskFileReader.getTaskWatchHoverButton());
        zp.clickElement(zp.getElement(advancedTaskFileReader.getTaskWatchToggle() + " span"));
        zp.waitElementInvisible(advancedTaskFileReader.getTaskLoadingIcon());


        cp.refresh(advancedTaskFileReader.getTaskItem());
        cp.clickElement(advancedTaskFileReader.getTaskFinishButton());
        waitOperator(cp);

        zp.navigate(zp.getNotificationLink(), notificationFileReader.getNotificationContent());
        assertFalse("取消关注的任务后，别人完成该任务后，自己依然会收到通知", zp.getText(notificationFileReader.getNotificationContent()).contains("取消关注测试任务"));
        taskOpreator.removeTask(cp, 0);
        ciwangProject.deleteProject();
        emailTest.checkEmail(zp,mailCheckList);
    }
    public String getTaskLinkInTaskList(Page cp){
        return getTaskLinkInTaskList(cp, 0);
    }
    public String getTaskLinkInTaskList(Page cp, int i){
        return cp.getLink(advancedTaskFileReader.getTaskTimeInTaskList(),i);
    }

    private void waitOperator(Page page){
        page.getWaitTool().waitForElement(By.cssSelector(advancedTaskFileReader.getTaskLoadingIcon()), 2);
        page.waitElementInvisible(advancedTaskFileReader.getTaskLoadingIcon());
    }

    private void updateTaskTest(Page zp, String updatedTaskContent) throws InterruptedException {
        zp.clickElement(advancedTaskFileReader.getTaskPriorityButtonInTaskList(), advancedTaskFileReader.getTaskPriorityChoiceInTaskList());
        zp.clickElement(advancedTaskFileReader.getTaskContentInTaskList());
        zp.clearAndSendKeys(advancedTaskFileReader.getTaskContentInTaskList(), updatedTaskContent);
        zp.sendKeys(Keys.ENTER);
        waitOperator(zp);
        taskOpreator.changePriority(zp, Urgency.PRIOR);
        taskOpreator.changeDeadline(zp, advancedTaskFileReader.getDeadlineButtonForTomorrow());
        zp.refresh(advancedTaskFileReader.getTaskContentInTaskList());
        assertEquals("任务列表中任务内容修改失败", updatedTaskContent, zp.getAttribute(advancedTaskFileReader.getTaskContentInTaskList(), "value"));
        assertEquals("任务列表中截至日期无法改成明天","明天", zp.getText(advancedTaskFileReader.getTaskDeadlineInTaskList() + ".tomorrow"));
        assertEquals("任务列表中正常处理的任务的灯的数目不对",Urgency.PRIOR.getValue(),
                advancedTaskChecker.getPriorityLightCount(0, advancedTaskFileReader.getTaskPriorityButtonInTaskList(),advancedTaskFileReader.getTaskPriorityIcon(),zp));
    }

    private void watchTestInTaskList(Page page) throws InterruptedException {
        indexBeforeChange.updateTaskIndex(page);
        page.moveToElement(advancedTaskFileReader.getTaskWatchHoverButton());
        page.assertIconAndText("已关注的任务上没有显示取消关注", advancedTaskFileReader.getWatchButton(), "取消关注", "hide");
        clickWatchButtonTest(page,"关注", "unhide","任务取消关注后，我关注的任务计数没有变化", indexBeforeChange.getWathchedTaskCount() - 1  );

        page.moveToElement(advancedTaskFileReader.getTaskInnerMenuIndex());
        assertFalse("转移焦点后，任务的关注图案没有消失", page.elementIsPresent(advancedTaskFileReader.getTaskWatchToggle() + " span",2));

        page.moveToElement(advancedTaskFileReader.getTaskWatchHoverButton());
        clickWatchButtonTest(page,"取消关注", "hide","任务关注后，我关注的任务计数没有变化", indexBeforeChange.getWathchedTaskCount() );
    }
    private void clickWatchButtonTest(Page cp,String buttonText, String buttonIcon,String messageText, int watchTaskCount){
        String watchCountIndex = cp.getText(advancedTaskFileReader.getTaskInnerMenuIndex(),3);
        cp.clickElement(advancedTaskFileReader.getWatchButton());
        cp.waitElementDisappear(advancedTaskFileReader.getTaskSendWait());
        cp.waitForContentChange(advancedTaskFileReader.getTaskInnerMenuIndex(), 3, watchCountIndex, 5);
        assertWatchButtonAfterClick(cp, buttonText, buttonIcon);
        indexAfterChange.updateTaskIndex(cp);
        assertEquals(messageText, watchTaskCount, indexAfterChange.getWathchedTaskCount());
    }
    private void assertWatchButtonAfterClick(Page cp,String buttonText, String buttonIcon){
        cp.assertIconAndText("任务上没有显示["+buttonText +"]",advancedTaskFileReader.getWatchButton(), buttonText, buttonIcon,0);
    }
    private void assertWatchCount(Page cp,String messageText, int watchTaskCount ){
        indexAfterChange.updateTaskIndex(cp);
        assertEquals(messageText, watchTaskCount, indexAfterChange.getWathchedTaskCount());
    }
    private void deadlineInputTest(Page page){
        page.assertElementPresent("日历按钮消失",advancedTaskFileReader.getDeadlineButtonForCalendarIcon());
        page.clickElement(advancedTaskFileReader.getDeadlineButton(), advancedTaskFileReader.getDeadlineButtonForTodayMark());
        // 临时解决下 由于没解决月份的问题，即每个月都有不同的天数，每个月都会有问题
        page.clickElement(advancedTaskFileReader.getDeadlineButtonForDateItem(),
                Integer.parseInt(page.getText(advancedTaskFileReader.getDeadlineButtonForTodayMark()))  );
        assertTrue("任务好像不能直接在日历上选截止日期了", page.elementIsPresent(advancedTaskFileReader.getDeadlineButton()+".date",7));

        page.clickElement(advancedTaskFileReader.getDeadlineButton());
        page.clickElement(advancedTaskFileReader.getDeadlineButtonForToday());
        assertTrue("任务好像不能通过'今天'按钮选今天为截至日期了", page.elementIsPresent(advancedTaskFileReader.getDeadlineButton()+".today",7));

        page.clickElement(advancedTaskFileReader.getDeadlineButton());
        page.clickElement(advancedTaskFileReader.getDeadlineButtonForTomorrow());
        assertTrue("任务好像不能通过'明天'按钮选明天为截至日期了", page.elementIsPresent(advancedTaskFileReader.getDeadlineButton()+".tomorrow",7));

        page.clickElement(advancedTaskFileReader.getDeadlineButton());
        page.clickElement(advancedTaskFileReader.getDeadlineButtonForClear());
        assertTrue("任务好像不能通过'清除'按钮清除截至日期了", page.elementIsPresent(advancedTaskFileReader.getDeadlineButtonForCalendarIcon(),7));

    }
    public void finishTaskTest(Page page, User executive, TaskCountIndex indexBeforeChange, TaskCountIndex indexAfterChange) throws InterruptedException {
        indexBeforeChange.updateTaskIndex(page);
        page.clickElement(advancedTaskFileReader.getTaskFinishButton());
        String countText = page.getText(advancedTaskFileReader.getTaskInnerMenuIndex(),1);
        page.assertElementPresent("完成任务后任务没有打钩",advancedTaskFileReader.getTaskFinishButton() + ".done", 15);
        page.waitForContentChange(advancedTaskFileReader.getTaskInnerMenuIndex(),1 , countText,5);
        indexAfterChange.updateTaskIndex(page);
        indexBeforeChange.finishTaskIndexTest(executive.getUserName(), indexAfterChange );

        page.clickElement(advancedTaskFileReader.getTaskExecutiveButtonInTaskList());
        assertFalse("对于已完成的任务，点击执行者选择器，可以选择执行者", page.elementIsPresent(advancedTaskFileReader.getTaskExecutiveInputInTaskList(), 1));
        page.clickElement(advancedTaskFileReader.getTaskFinishButton());
        countText = page.getText(advancedTaskFileReader.getTaskInnerMenuIndex(),1);
        page.waitForContentChange(advancedTaskFileReader.getTaskInnerMenuIndex(),1 , countText,5);
        page.assertElementNotPresent("重新开启任务后，打钩的图案没有消失", advancedTaskFileReader.getTaskFinishButton() + ".done", 3);
        indexAfterChange.updateTaskIndex(page);
        indexBeforeChange.reopenTaskIndexTest(executive.getUserName(), indexAfterChange );

    }
    private void removeTaskInFirstTaskTest(Page page, User user, TaskCountIndex indexBeforeChange, TaskCountIndex indexAfterChange) throws InterruptedException {
        taskOpreator.changeExecutive(page,user.getUserName());
        indexBeforeChange.updateTaskIndex(page);
        taskOpreator.removeTask(page, 0);
        indexForRemoveTaskTest(page, indexBeforeChange,indexAfterChange);
    }
    private void indexForRemoveTaskTest(Page page, TaskCountIndex indexBeforeChange, TaskCountIndex indexAfterChange){
        indexAfterChange.updateTaskIndex(page);
        indexBeforeChange.removeProcessingTaskIndexTest(zombieUser.getUserName(), indexAfterChange);
    }
    @Test(timeout = 300000)
    public void test06_0_watchTestInDetailPage(){
        List<User> users = new ArrayList<User>();
        users.add(ciwangUser);
        users.add(zombieUser);

        taskOpreator.sendSimpleTask(cp,zombieUser.getUserName(), "test Watch Task In task Detail Page", zombieProject.getProjectTaskLink() );
        forwardTaskDetailPage(cp, 0);
        indexBeforeChange.updateTaskIndex(cp);
        cp.assertIconAndText("已关注的任务上没有显示取消关注", advancedTaskFileReader.getWatchButton(), "取消关注", "hide");
        checkWatchUsers(cp, users, "给别人发送任务后");

        clickWatchButtonTest(cp,"关注", "unhide","任务取消关注后，我关注的任务计数没有变化", indexBeforeChange.getWathchedTaskCount() - 1 );
        users.remove(0);
        checkWatchUsers(cp, users, "任务详情页取消关注后");

        clickWatchButtonTest(cp, "取消关注", "hide", "任务关注后，我关注的任务计数没有变化", indexBeforeChange.getWathchedTaskCount());
        users.add(ciwangUser);
        checkWatchUsers(cp, users, "任务详情页重新关注后");

        changeExecutiveInDetailPage(cp, ciwangUser.getUserName());
        cp.assertElementNotPresent("把任务给自己后依然会出现“取消关注”", advancedTaskFileReader.getWatchButton());
    }
    private void forwardTaskDetailPage(Page cp, int taskNum){
        cp.navigate(getTaskLinkInTaskList(cp, taskNum), advancedTaskFileReader.getTaskCommentMDBoxInDetailPage());
    }
    private void checkWatchUsers(Page cp, List<User> users, String message){
        assertEquals(message + "关注列表的header上的用户数与实际用户数不一致",
                cp.getPageNum(".task-detail-watchlist header"), cp.getElementCount(".task-detail-watchlist ul li"));
        int i = 0;
        for(User user : users){
            cp.assertLinkAndTextEquals(message +"关注列表上的用户"+user.getUserName(),
                    user.getUserName(),user.getHomePageLink(), ".task-detail-watchlist ul li a",i);
            cp.assertAttributeContainWords(message +"关注列表上的用户头像", user.getAvatar(), ".task-detail-watchlist .ui.image",i,"src");
            i++;
        }
    }

    public void test06_1_taskOperatorInDetailsPage() throws InterruptedException, ParseException {
        mailCheckList.clear();
        cp.navigate(zombieProject.getProjectTaskLink());
        cp.waitElementInvisible(advancedTaskFileReader.getTaskLoadingIcon());
        indexAfterChange.updateTaskIndex(cp);
        indexBeforeChange.updateTaskIndex(cp);
        AdvancedTask advancedTask = advancedTaskChecker.sendStandardTaskInProjectPage(ciwangUser, zombieUser,zombieProject);
        advancedTask.setTaskUrl(getTaskLinkInTaskList(cp));

        cp.navigate(advancedTask.getTaskUrl(), advancedTaskFileReader.getTaskCommentMDBoxInDetailPage());
        cp.assertAttributeEquals("发送任务后，任务列表中任务内容不对", advancedTask.getContent(), advancedTaskFileReader.getTaskContentInTaskList(), "value");
        cp.assertLinkEquals("任务详情页动态发起人的链接有误",
                ciwangUser.getHomePageLink(), advancedTaskFileReader.getTaskActivitySponsorInDetailPage());
        assertActivityInTaskDetailPage(ciwangUser,"创建任务",0,"创建了任务",".add.icon");

        updateTaskTest(cp, updatedTaskContentInDetailPage);
        assertActivityInTaskDetailPage(ciwangUser, "更新任务内容", 1, "更新了任务", ".edit.icon");
        assertActivityInTaskDetailPage(ciwangUser, "修改任务优先级", 2, "更新了任务优先级为 [优先处理]", ".icon.exclamation");
        assertActivityInTaskDetailPage(ciwangUser,"修改截止日期",3,"更新了任务截止日期为明天",".calendar.icon");

        finishTaskTest(cp, zombieUser,indexBeforeChange,indexAfterChange);
        assertActivityInTaskDetailPage(ciwangUser, "完成任务", 4, "完成了任务", ".checkmark.icon");
        assertActivityInTaskDetailPage(ciwangUser, "重新开启任务", 5, "重新开启了任务", ".circle.blank.icon");

        changeExecutiveInDetailPageTest(cp, ciwangUser.getUserName());
        assertActivityInTaskDetailPage(ciwangUser, "重新指派任务", 6, "重新指派了任务给 自己", ".user.icon");
        changeExecutiveInDetailPage(cp, zombieUser.getUserName());
        assertActivityInTaskDetailPage(ciwangUser, "重新指派任务", 7, "重新指派了任务给 " + zombieUser.getUserName(), ".user.icon");

        increaseTaskPriorityWithKeyboard(cp,0);
        removeZombieTaskInFirstTaskInDetailPageTest(cp,indexBeforeChange,indexAfterChange);

        advancedActivity.constructActivityMap(advancedTaskChecker.getActivityContent(), updatedTaskContentInDetailPage, advancedTask);
        advancedActivity.checkTaskActivity(zp,3,1);
        advancedNotificationTest.taskNotificationConstructor(advancedTask,updatedTaskContentInDetailPage,mailCheckList, notificationFileReader.getSystemItemIcon());
        advancedNotificationTest.checkTaskNotification(zp);

        assertFalse("标记所有通知为已读的按钮失效", zp.elementIsPresent(notificationFileReader.getUnreadCount(), 5));
        emailTest.checkEmail(zp,mailCheckList);
    }
    private void changeExecutiveInDetailPageTest(Page page, String executiveName){
        indexBeforeChange.updateTaskIndex(cp);
        changeExecutiveInDetailPage(page, executiveName);
        indexForChangeExecutiveTest(page);
    }
    private void removeZombieTaskInFirstTaskInDetailPageTest(Page page, TaskCountIndex indexBeforeChange, TaskCountIndex indexAfterChange) throws InterruptedException {
        changeExecutiveInDetailPage(page, zombieUser.getUserName());
        indexBeforeChange.updateTaskIndex(page);
        removeTaskInDetailPage(page);
        indexForRemoveTaskTest(page, indexBeforeChange,indexAfterChange);

    }
    private void removeTaskInDetailPage(Page cp)  {
        String processingTaskCountText = cp.getText(advancedTaskFileReader.getTaskInnerMenuIndex());
        cp.assertTextEquals("任务详情页的删除按钮", "删除", advancedTaskFileReader.getTaskRemoveButtonIndetailPage());
        cp.checkAlert(advancedTaskFileReader.getTaskRemoveButtonIndetailPage(),
                "任务详情页的删除有误", "确认删除该任务？");
        cp.waitForElement(advancedTaskFileReader.getTaskContentInput(),6);
        cp.waitForContentChange(advancedTaskFileReader.getTaskInnerMenuIndex(),processingTaskCountText,10);
    }
    private  void  changeExecutiveInDetailPage(Page cp, String executiveName){
        String nowExecutiveName = cp.getAttribute(advancedTaskFileReader.getTaskExecutiveButtonInDetailPage(),"title");
        if(nowExecutiveName.equals(executiveName)){
            return;
        }
        cp.clickElement(advancedTaskFileReader.getTaskExecutiveButtonInDetailPage(), advancedTaskFileReader.getTaskExecutiveInputInDetailPage());
        cp.clearAndSendKeys(advancedTaskFileReader.getTaskExecutiveInputInDetailPage(), executiveName);
        cp.clickElement(advancedTaskFileReader.getTaskExecutiveSelectedItemInDetailPage());
        cp.waitForAttributeChange(advancedTaskFileReader.getTaskExecutiveButtonInDetailPage(),"title",nowExecutiveName,5);
    }
    public void assertActivityInTaskDetailPage(User sponsor, String message, int itemNum,String action, String selectorForIcon){
        Page cp = sponsor.getPage();
        message = "任务详情页" + message;
        cp.waitForElement(advancedTaskFileReader.getTaskDetailActivity()+":nth-child("+ (itemNum +1)+")",10);
        cp.assertElementPresent(message+"动态图标", advancedTaskFileReader.getTaskDetailActivity() ,itemNum, selectorForIcon);
        cp.assertTextEqualsOneFromThreeExpect(message+"动态", sponsor.getUserName() + " " + action +" - 几秒前"
                , sponsor.getUserName() + " " + action +" - 几秒内" , sponsor.getUserName() + " " + action +" - 1分钟前" , advancedTaskFileReader.getTaskDetailActivity(),itemNum);
    }
    @AfterClass
    public static void tearDown(){
        zp.getDriver().quit();
        cp.getDriver().quit();
    }
}

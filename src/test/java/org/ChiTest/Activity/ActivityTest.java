package org.ChiTest.Activity;

import dataReader.ConfigureInfo;
import org.ChiTest.AdvancedTask.AdvancedTask;
import org.ChiTest.Page.Page;
import org.ChiTest.Project.GroupTest;
import org.ChiTest.Project.Project;
import org.ChiTest.Project.ProjectFileReader;
import org.ChiTest.Project.ProjectTest;
import org.ChiTest.Topic.Topic;
import org.ChiTest.User.User;
import org.ChiTest.WebComponent.ScreenShot;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Created by dugancaii on 8/12/2014.
 */
public class ActivityTest {
    protected  static User zombieUser;
    protected  static User ciwangUser;
    private static Cookie zombieCookie;
    private static Cookie ciwangCookie;
    private static ActivityFileReader activityFileReader;
    private static Page projectPage;
    static GroupTest groupTest;
    static ProjectTest projectTest;
    static Project ciwangProject;
    static Project zombieProject;
    private static ProjectFileReader projectFileReader;

    public ActivityTest(){
        activityFileReader = new ActivityFileReader();
    }

    @BeforeClass
    public static void setUp() throws Exception {
        ConfigureInfo ConfigureInfo = new ConfigureInfo(true,true);
        zombieUser = ConfigureInfo.getZombieUser();
        ciwangUser = ConfigureInfo.getCiwangUser();
        projectPage = zombieUser.getPage();
        zombieCookie = ConfigureInfo.getZombieCookie();
        ciwangCookie = ConfigureInfo.getCiwangCookie();
        zombieUser.autoLogin(ConfigureInfo.getLoginUrl() , zombieCookie);
        ciwangUser.autoLogin(ConfigureInfo.getLoginUrl() ,ciwangCookie);
        projectTest = new ProjectTest();
        groupTest = new GroupTest();
        zombieProject = new Project(zombieUser, zombieUser.getUserLoginName()+"Private");
        ciwangProject = new Project(ciwangUser, ciwangUser.getUserLoginName()+"Private");
        projectFileReader = new ProjectFileReader();
    }
    @Rule
    public ScreenShot screenShotZombie = new ScreenShot(zombieUser);
    @Rule
    public ScreenShot screenShotCiwang = new ScreenShot(ciwangUser);
    @Test
    public void test01_checkActivityStyle() throws ParseException, InterruptedException {
        projectPage.setPageUrl(projectPage.getBaseUrl() + "user");
        Project project = new Project(zombieUser, "privateForTopic");
        Topic topic = new Topic(project, "试验"+projectPage.getMark(),"好啦好啦",zombieUser);
        topic.createTopic();
        topic.deleteTopic();
        testStartDate(projectPage, projectPage.getBaseUrl() + "user");
        projectPage.clickElement(activityFileReader.getShowMoreActivity());
        projectPage.waitForElement("#inner-menu", 10);
        assertEquals("个人中心点击显示更多动态后没有跳转到动态墙", projectPage.getDriver().getCurrentUrl(), projectPage.getBaseUrl() + "user/activities");
        testStartDate(projectPage, project.getProjectLink());
        checkShowMoreActivity(projectPage);
        testStartDate(projectPage,
                project.getProjectMemberLink(project.getCreator().getUserLoginName()));
        checkShowMoreActivity(projectPage);
    }
    @Test
    public void test02_unreadActivityCount() throws InterruptedException, ParseException {
        projectPage = zombieProject.getCreator().getPage();
        zombieProject.addProjectMember(ciwangUser);

        projectPage.navigate(zombieUser.getUserCenterLink(),projectFileReader.getProjectItemForBlockList());
        projectTest.clearActivityUnreadNum(projectPage);

        projectPage.navigate(zombieProject.getProjectLink(), activityFileReader.getActivityTime());
        groupTest.removeAllGroup(projectPage);

        projectTest.addPin(projectPage, zombieProject.getProjectFullName());
        groupTest.addNewGroup(projectPage, zombieProject.getProjectFullName());

        AdvancedTask advancedTask = new AdvancedTask();
        advancedTask.sendSimpleTask(ciwangUser.getPage(), zombieUser.getUserName(), "hello ", zombieProject.getProjectTaskLink() );

        projectPage.refresh(zombieUser.getUserCenterLink(), projectFileReader.getProjectItemForBlockList());
        int zombieProjectNum = projectPage.findItemBySign(zombieProject.getProjectFullName(), projectFileReader.getProjectNameInBlockList(), projectPage.getItemEqualsFinder());
        assertEquals("个人空间里项目没有把有动态的项目排到最前" + projectPage.getUrl(), 0,zombieProjectNum );
        projectPage.moveToElement(projectFileReader.getDropIconInTopMenu());
        assertEquals("常用项目里，未显示项目动态数或动态数显示不正确", "1", projectPage.getText(projectFileReader.getUnreadActivityCountInPinList()));
        projectPage.navigate(projectPage.getBaseUrl()+"user/projects", projectFileReader.getProjectGroupItem());
        assertEquals("项目分组里的未读动态数显示不正常或分组功能失败", "1", projectPage.getText(projectFileReader.getUnreadActivityCountOnGroupItem()));
        projectPage.clickElement(projectFileReader.getProjectGroupItem(),projectFileReader.getUnreadActivityCountForItemInGroup());
        assertEquals("项目分组里的项目未读动态数显示不正常或分组功能失败", "1", projectPage.getText(projectFileReader.getUnreadActivityCountForItemInGroup()));

        projectPage.navigate(projectPage.getBaseUrl()+"user", projectFileReader.getProjectItemForBlockList());
        assertEquals("个人中心项目图标上的未读动态数不正确",
                projectPage.getElement(projectFileReader.getProjectItemForBlockList()).findElement(By.cssSelector(projectFileReader.getUnreadActivityCountOnProjectIcon())).getText(), "1");

        projectPage.clickElement(projectFileReader.getProjectItemForBlockList(), projectFileReader.getUnreadActivityCountIndropDownMenu());
        Thread.sleep(2000);
        // todo  不行可能有问题，一闪就没了
        //assertTrue("项目首页的动态更新线或时钟图标消失", projectPage.elementIsPresent(".update-divide-line .time.icon", 5));

        projectPage.navigate(projectPage.getBaseUrl() + projectFileReader.getUserCenterUrl(), projectFileReader.getProjectItemForBlockList());
        assertFalse("点击未读项目后，回到个人中心，项目图标上的未读动态数没消失", projectPage.elementIsPresent(projectFileReader.getUnreadActivityCountOnProjectIcon(),
                projectPage.findItemInOnePageByTextEquals(projectFileReader.getProjectItemForBlockList(), zombieProject.getProjectFullName()), 2));
        projectPage.getBuilder().moveToElement(projectPage.getElement(projectFileReader.getDropIconInTopMenu())).perform();
        assertFalse("点击项目后后，常用项目里，项目未读数不为0",projectPage.elementIsPresent(projectFileReader.getUnreadActivityCountInPinList(),2));

        projectPage.navigate(projectPage.getProjectUrl(), projectFileReader.getProjectGroupItem());
        assertFalse("点击项目后，项目分组里的未读动态数显示不正常或分组功能失败", projectPage.elementIsPresent(projectFileReader.getUnreadActivityCountOnGroupItem(),2));
        projectPage.clickElement(projectFileReader.getProjectGroupItem(),projectFileReader.getProjectGroupItemsWrapper());
        assertFalse("点击项目后，项目分组里的项目未读动态数显示不正常或分组功能失败", projectPage.elementIsPresent(projectFileReader.getUnreadActivityCountForItemInGroup(),2));
        advancedTask.deleteAllTasks(zombieProject);
        groupTest.removeAllGroup(projectPage);
    }

    private void checkShowMoreActivity(Page page){
        int activityCount = page.getElementCount(activityFileReader.getActivityItem());
        page.clickElement(activityFileReader.getShowMoreActivity());
        page.waitElementInvisible(".loading.icon");
        page.assertCountEquals("项目首页查看更多动态", activityCount + 20, activityFileReader.getActivityItem());
        page.assertElementPresent("点击查看更多动态后，查看更多动态按钮", activityFileReader.getShowMoreActivity());
        assertNotEquals("点击查看更多动态后，新刷出的第一条动态与原来的第一条动态",
                page.getText(activityFileReader.getActivityItem()), page.getText(activityFileReader.getActivityItem(), activityCount));
        assertNotEquals("点击查看更多动态后，新刷出的第一条动态与原来的最后条动态",
                page.getText(activityFileReader.getActivityItem(), activityCount - 1),
                page.getText(activityFileReader.getActivityItem(), activityCount));
    }


    private void testStartDate(Page page,   String url) throws InterruptedException, ParseException {
        page.navigate(url, activityFileReader.getActivityTime());
        DateFormat day = new SimpleDateFormat("yyyy-M-d E", Locale.CHINA);
        String ActivityDate = page.getText(".start-date");
        String today = day.format(new Date());
        assertEquals("动态页面显示的日期有误", "今天（" + today.replace("星期", "周") + "）", ActivityDate);
        assertTrue("页面"+ url + "的时间轴消失", projectPage.elementIsPresent(activityFileReader.getActivityTimeLine(),4));
    }

    @AfterClass
    public static void tearDown() {
        ciwangUser.getPage().getDriver().quit();
        zombieUser.getPage().getDriver().quit();
    }
}

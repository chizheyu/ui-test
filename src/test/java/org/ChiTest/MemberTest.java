package org.ChiTest;

import dataReader.ConfigureInfo;
import org.ChiTest.Activity.ActivityFileReader;
import org.ChiTest.Activity.AdvancedActivity;
import org.ChiTest.AdvancedTask.AdvancedTask;
import org.ChiTest.Email.Email;
import org.ChiTest.Email.EmailTest;
import org.ChiTest.Friend.RelationShip;
import org.ChiTest.Message.MessageTest;
import org.ChiTest.Notification.Notification;
import org.ChiTest.Notification.NotificationFileReader;
import org.ChiTest.Notification.NotificationTest;
import org.ChiTest.Page.Page;
import org.ChiTest.Project.GroupTest;
import org.ChiTest.Project.Project;
import org.ChiTest.Project.ProjectFileReader;
import org.ChiTest.Project.ProjectTest;
import org.ChiTest.User.User;
import org.ChiTest.WebComponent.ScreenShot;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.Cookie;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by dugancaii on 11/13/2014.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MemberTest {
    protected  static User zombieUser;
    protected  static User ciwangUser;
    private static Page ciwangPage;
    private static Cookie zombieCookie;
    private static Cookie ciwangCookie;
    private static ProjectFileReader projectFileReader;
    private static NotificationFileReader notificationFileReader;
    private static ActivityFileReader activityFileReader;
    private static NotificationTest notificationTest;
    private static Page zombiePage;
    private static ProjectTest projectTest;
    private static GroupTest groupTest;
    private  static ConfigureInfo configureInfo;
    private static Logger log = Logger.getLogger("memberTest");
    private static AdvancedTask advancedTask;
    private static Project ciwangProject;
    private static Project zombieProject;
    private static Set mailSetList;
    public MemberTest()  {
        advancedTask = new AdvancedTask();
        projectFileReader = new ProjectFileReader();
    }
    @BeforeClass
    public static void setUp() throws Exception {
        configureInfo = new ConfigureInfo(true,true);
        activityFileReader = new ActivityFileReader();
        notificationTest = new NotificationTest();
        notificationFileReader = new NotificationFileReader();
        projectTest = new ProjectTest();
        groupTest = new GroupTest();
        advancedTask = new AdvancedTask();
        projectFileReader = new ProjectFileReader();

        zombiePage = configureInfo.getZombieUser().getPage();
        ciwangPage = configureInfo.getCiwangUser().getPage();

        zombieUser = new User("test163hello","Z_member_test","123456",zombiePage,"Fruit-3.png");
        ciwangUser = new User("wangyiA163_-41","C_member_test","123456",ciwangPage,"Fruit-2.png");

        zombieCookie = zombiePage.getStagingSid("2126fbcd-344a-462f-bb10-087dca12294e");
        ciwangCookie = ciwangPage.getStagingSid("848d6907-dd7d-4f21-bd2c-2c016bb63d23");

        zombieUser.autoLogin(configureInfo.getLoginUrl(),zombieCookie );
        ciwangUser.autoLogin(configureInfo.getLoginUrl(), ciwangCookie);

        ciwangProject = new Project(ciwangUser, ciwangUser.getUserLoginName()+"Private","private");
        zombieProject = new Project(zombieUser, zombieUser.getUserLoginName()+ "Private" ,"this is a test ","private", true, "MIT License", "Java" );
        ciwangProject.createFixtureProject();
        zombieProject.createFixtureProject();
        mailSetList = new HashSet();
        List<PageLink> projectLinks = new ArrayList<PageLink>();
        projectLinks.add(new PageLink(notificationFileReader.getNotificationSecondLink(), ciwangProject.getProjectName(),
                projectFileReader.getPrivateProjectTitle(), ciwangProject.getProjectLink()));
    }
    @Rule
    public ScreenShot screenShotZombie = new ScreenShot(zombieUser);
    @Rule
    public ScreenShot screenShotCiwang = new ScreenShot(ciwangUser);
    @Test
    public void test01_0_addProjectMember() throws InterruptedException, ParseException {
        ciwangProject.removeProjectMember(zombieUser);
        ciwangProject.addProjectMember( zombieUser);

        List<PageLink> projectLinks = new ArrayList<PageLink>();
        projectLinks.add(new PageLink(notificationFileReader.getNotificationSecondLink(), ciwangProject.getProjectName(),
                projectFileReader.getPrivateProjectTitle(), ciwangProject.getProjectLink(), ciwangUser));
        Notification addProjectMember = new Notification(notificationFileReader.getProjectMemberIconClass(),
                ciwangUser,ciwangUser.getUserName()+" 将你添加为项目 "+ciwangProject.getProjectName() + " 的成员", zombieUser, projectLinks );
        notificationTest.verifyForMultiLinkFormat(addProjectMember, "将某人添加为项目成员 ", notificationFileReader.getSystemItemIcon());
        AdvancedActivity addMember = new AdvancedActivity(ciwangUser,zombieUser,ciwangProject);
        addMember.constructAddMemberActivity(ciwangProject.getCreator(), zombieUser, activityFileReader.getUserIconClass());
        addMember.checkActivity(zombiePage, activityFileReader.getOtherActivityTagInProjectHomePage());
        mailSetList.add(addProjectMember.getContent());
    }
    @Test
    public void test01_1_MemberAddBox() throws InterruptedException, ParseException {
        RelationShip relationShip = new RelationShip();
        relationShip.followSomeone(ciwangUser,zombieUser);
        relationShip.followSomeone(zombieUser, ciwangUser);
        zombieProject.removeProjectMember(ciwangUser);
        addProjectMemberTest(zombieProject, ciwangUser);
    }
    @Test
    public void test06_emailTest() throws InterruptedException {
        (new EmailTest()).checkEmail(zombiePage, mailSetList,new Email("coding_test42","163.com","chi2014"));
    }

    public void verfiyMemberIsPresent(Page page,String itemName, String itemSelector, User member ){
        assertTrue("点击添加成员框里" + itemName + "点击失败", page.clickElement(itemSelector));
        page.waitElementInvisible(".loading.icon");
        assertTrue(itemName + "一栏里面没有ciwang", page.elementIsPresent(".select-users li[title='" + member.getUserName() + "']", 2));
    }
    public void addProjectMemberTest(Project project, User adder) throws ParseException, InterruptedException {
        Page page = project.getCreator().getPage();
        page.navigate(project.getProjectLink(), ".big.users.icon");
        page.clickElement(".big.users.icon", projectFileReader.getMemberCountText());
        page.waitForElement(projectFileReader.getMemberCountText(),5);
        int basePageNum = page.getPageNum(projectFileReader.getMemberCountText());
        String projectListImageInAddMemberFrame = projectFileReader.getProjectListImageInAddMemberFrame().replace("{0}", adder.getUserLoginName());
        assertTrue("点击添加项目成员按钮后无弹框或项目列表中"+adder.getUserLoginName()+"Private没刷出",
                page.clickElement(projectFileReader.getAddIconForAddMember(), projectListImageInAddMemberFrame));
        page.clickElement(projectFileReader.getCancelButtonForAddMember());
        assertTrue("点击取消按钮,添加成员框不会消失",  page.waitElementInvisible(projectFileReader.getCancelButtonForAddMember(),7));
        page.clickElement(projectFileReader.getAddIconForAddMember(), projectListImageInAddMemberFrame);
        page.clickElement(projectFileReader.getCloseButtonForAddMemberFrame());
        assertTrue("点击关闭按钮,添加成员框不会消失", page.waitElementInvisible(projectFileReader.getCancelButtonForAddMember(),7));
        page.clickElement(projectFileReader.getAddIconForAddMember(), projectListImageInAddMemberFrame);

        verfiyMemberIsPresent(page,"我关注的人", projectFileReader.getFollowIconInAddMemberFrame(), adder);
        verfiyMemberIsPresent(page,"粉丝", projectFileReader.getFansIconInAddMemberFrame(), adder);
        verfiyMemberIsPresent(page,adder.getUserLoginName()+"Private项目列表", projectListImageInAddMemberFrame, adder);

        assertEquals("添加项目成员框显示的还可以添加的项目成员数有误", 10 - basePageNum,
                page.getPageNum(projectFileReader.getMaxMemberHintForAddMember()));
        assertEquals("未选择成员时，已选成员栏要显示 '已选成员：\n尚未选择任何成员'", "已选成员：\n尚未选择任何成员",
                page.getText(projectFileReader.getSelectedMemberNameInAddMember()));
        page.clickElement(".select-users li[title='" + adder.getUserName() + "']");
        assertEquals("选择成员后，已选成员一栏没有出现成员标签或显示不正确", adder.getUserName(), page.getText(projectFileReader.getSelectedMemberNameInAddMember() + " li span"));
        assertEquals("添加项目成员框显示的还可以添加的项目成员数有误", 9 - basePageNum, page.getPageNum(projectFileReader.getMaxMemberHintForAddMember(), "[^0-9]|/"));
        String maxMemberHintText = page.getText(projectFileReader.getSelectedMemberNameInAddMember());
        page.clickElement(".select-users li[title='" + adder.getUserName() + "']");
        assertTrue("取消选择成员后，成员方块上绿钩没有消失", page.waitElementDisappear(".select-users li[title='" + adder.getUserName() + "'] .corner.green"));
        page.clickElement(".select-users li[title='" + adder.getUserName() + "']", projectFileReader.getSelectedMemberNameInAddMember() + " .remove.icon");
        assertTrue("选择成员后，成员方块上没有出现绿钩", page.elementIsPresent(".select-users li[title='" + adder.getUserName() + "']", 0, " .corner.green"));
        page.clickElement(projectFileReader.getSelectedMemberNameInAddMember() + " .remove.icon");
        page.waitForContentChange(projectFileReader.getSelectedMemberNameInAddMember(), maxMemberHintText, 5);
        assertFalse("取消选择成员后，已选成员一栏有出现成员标签", page.elementIsPresent(projectFileReader.getSelectedMemberNameInAddMember() +  " li span", 2));
        assertEquals("添加项目成员框显示的还可以添加的项目成员数有误", 10 - basePageNum, page.getPageNum(projectFileReader.getMaxMemberHintForAddMember(), "[^0-9]|/"));

        page.clickElement(projectFileReader.getProjectMemberWaitForSelect(adder.getUserName()));
        assertTrue("已有成员会显示灰色钩子",  page.elementIsPresent(projectFileReader.getProjectMemberWaitForSelect(project.getCreator().getUserName()+"已加入该项目")+" .checkmark.icon",5));
        page.clickElement(projectFileReader.getProjectMemberWaitForSelect(zombieUser.getUserName() + "已加入该项目"));
        assertFalse("点击已有成员会显示绿色钩子", page.elementIsPresent(projectFileReader.getProjectMemberWaitForSelect(project.getCreator().getUserName() + "已加入该项目") + " .corner.green", 2));
        String projectMemberCountText = page.getText(projectFileReader.getMemberCountText());
        page.clickElement(projectFileReader.getConfirmButtonForAddMember());

        page.waitForContentChange(projectFileReader.getMemberCountText(), projectMemberCountText, 20);
        assertEquals("添加项目成员后，项目成员计数没有加一", page.getPageNum(projectFileReader.getMemberCountText(), "[^0-9]|/"), basePageNum + 1);

        assertTrue("添加成员后刷新页面无法搜索到该新成员", searchProjectMember(project, adder));
        assertTrue("点击项目成员，没有显示项目成员的动态", page.clickElement("#inner-menu img", "#project-activities"));
        assertTrue("加入时间有误", page.comparePageTime(new Date(), ".member.info .time", new SimpleDateFormat("yyyy-MM-dd HH:mm")));

        assertTrue("添加"+adder.getUserLoginName()+"为项目成员后，能否访问项目,"+adder.getUserLoginName()+"不能访问该项目", adder.getPage().navigate(page.getBaseUrl() + "u/" + project.getCreator().getUserLoginName() + "/p/" + project.getProjectName(), "#project-home"));
        page.getWaitTool().waitForJavaScriptCondition("return $(\"#inner-menu .header\").text().indexOf(0)== -1", 10);
    }


    @Test
    public void test02_1_removeProjectMember() throws InterruptedException, ParseException {
        ciwangProject.addProjectMember( zombieUser);
        groupTest.removeAllGroup(zombiePage);
        projectTest.addPin(zombiePage, ciwangProject.getProjectFullName());
        groupTest.addNewGroup(zombiePage, ciwangProject.getProjectFullName());

        int memberCountAfterRemoving = ciwangProject.removeProjectMember(zombieUser);
        List<PageLink> projectLinks = new ArrayList<PageLink>();
        Notification removeProjectMember = new Notification(notificationFileReader.getProjectMemberIconClass(),
                ciwangProject.getCreator(), ciwangProject.getCreator().getUserName()+" 将你从项目 "+ciwangProject.getProjectName() + " 移除",
                zombieUser,projectLinks);
        notificationTest.verifyForOneLinkFormat(removeProjectMember,"将某人从项目中移除 ",notificationFileReader.getSystemItemIcon());
        mailSetList.add(removeProjectMember.getContent());

        AdvancedActivity removeMember = new AdvancedActivity(ciwangUser,zombieUser,ciwangProject);
        removeMember.constructRemoveMemberActivity(ciwangProject.getCreator(), zombieUser, activityFileReader.getUserIconClass());
        removeMember.checkActivity(ciwangProject.getCreator().getPage(), activityFileReader.getOtherActivityTagInProjectHomePage());
        verifyRemoveProjectMember(ciwangProject, zombieUser, memberCountAfterRemoving);
    }
    @Test
    public void test02_0_removeProjectMemberHasTask() throws InterruptedException, ParseException {
        ciwangProject.addProjectMember(zombieUser);
        Page projectPage = ciwangProject.getCreator().getPage();
        advancedTask.putTestTaskForSomeone(zombieUser.getPage(),
                advancedTask.getAdvancedTestTask(ciwangProject.getCreator(), ciwangProject, zombieUser));
        projectPage.navigate(ciwangProject.getProjectMemberLink(), "#inner-menu img");
        projectPage.clearAndSendKeys(projectFileReader.getSearchMemberInput(), zombieUser.getUserName());
        projectPage.clickElement("#inner-menu img", ".remove.icon");
        projectPage.clickElement(".remove.icon");
        projectPage.getDriver().switchTo().alert().accept();
        assertTrue("项目成员还有未完成的任务，却可以被删除", projectPage.verifyHint("该用户还有未完成任务，请指派给其它人后再进行操作"));

    }



    public void verifyRemoveProjectMember(Project project, User ciwangUser, int memberCountAfterRemoving) throws InterruptedException {
        Page projectPage = project.getCreator().getPage();
        projectPage.refresh(project.getProjectMemberLink(),"#inner-menu img");
        projectPage.getWaitTool().waitForJavaScriptCondition(" return $(\"#inner-menu .header\").text().indexOf(0)== -1", 10);
        assertEquals("删除用户后项目成员的计数没有减一",projectPage.getPageNum("#inner-menu .header","[^0-9]|/"),memberCountAfterRemoving );
        assertFalse("删除用户失败", searchProjectMember(project, ciwangUser));
        ciwangUser.getPage().navigate(project.getProjectMemberLink(),projectFileReader.getProjectActivity());
        ciwangUser.getPage().assertElementPresent("被移除项目的成员还能进入项目中",projectFileReader.getProjectActivity(),5);

        assertEquals("ciwang被移除后，访问项目，没有跳转到个人中心页面", ciwangUser.getPage().getBaseUrl()+projectFileReader.getUserCenterUrl(), ciwangUser.getPage().getDriver().getCurrentUrl());
        assertEquals("用户被移除项目后，依然能在常用项目中发现该项目",-1,projectTest.findPrivateProjectInPinList(ciwangUser.getPage(),project.getProjectName()));
        ciwangUser.getPage().navigate(ciwangUser.getPage().getBaseUrl()+"user/projects",projectFileReader.getProjectItemInAllProjectPage());
        assertFalse("项目成员被移除项目后，分组为消失", ciwangUser.getPage().elementIsPresent(projectFileReader.getProjectGroupItem(), 2));

    }

    @Test
    public void test03_searchProjectMember() throws ParseException, InterruptedException {
        zombieProject.addProjectMember( ciwangUser);
        searchProjectMemberTest(zombieProject, ciwangUser);
        assertTrue("点击项目成员，没有显示项目成员的动态", zombiePage.clickElement("#inner-menu img", "#project-activities"));
        assertEquals("点击搜索出的项目成员，项目成员的名字显示有误", ciwangUser.getUserName(), zombiePage.getText(".member.info .title"));
        zombiePage.clickElement(".black.button", "#send-message-modal");
        assertTrue("项目成员页面，点击某成员，详情页面点击私信吗没有弹框", zombiePage.elementIsPresent("#send-message-modal",5) );
        zombiePage.clickElement("#send-message-modal .close");
        zombiePage.getWaitTool().waitForJavaScriptCondition("return $(\"#send-message-modal\").css(\"display\") == \"none\" ", 5);
    }
    public boolean searchProjectMember(Project project, User user){
        Page projectPage = project.getCreator().getPage();
        projectPage.navigate(project.getProjectMemberLink(), projectFileReader.getSearchMemberInput());
        return  projectPage.findItemInOnePageByTextEquals(projectFileReader.getMemberInMemberList(),user.getUserName()) != -1;
    }
    public boolean searchProjectMemberTest(Project project, User user){
        Page projectPage = project.getCreator().getPage();
        projectPage.navigate(project.getProjectMemberLink(), projectFileReader.getSearchMemberInput());
        projectPage.clearAndSendKeys(projectFileReader.getSearchMemberInput(), user.getUserName());
        if(!projectPage.elementIsPresent(projectFileReader.getSelectedMemberInMemberList(),1)){
            return false;
        }
        String memberName = projectPage.getText(projectFileReader.getSelectedMemberInMemberList());
        if(!memberName.equals(user.getUserName())){
            return false;
        }

        return true;
    }
    @Test
    public void test04_addMemberOverLimitedAndRepeatAdd() throws ParseException, InterruptedException {
        Page projectPage = zombieProject.getCreator().getPage();
        projectPage.navigate(zombieProject.getProjectMemberLink(),projectFileReader.getAddIconForAddMember());
        projectPage.clickElement(projectFileReader.getAddIconForAddMember(), projectFileReader.getSearchInputForAddMember());
        String originalMaxMemberCountText = projectPage.getText(projectFileReader.getMaxMemberHintForAddMember());
        projectPage.clearAndSendKeys(projectFileReader.getSearchInputForAddMember(), "c");
        projectPage.waitElementInvisible(".loading.icon");
        projectPage.clickElements(projectFileReader.getSelectedMemberItemInAddMember());
        projectPage.waitForContentChange(projectFileReader.getMaxMemberHintForAddMember(), originalMaxMemberCountText, 5);
        assertEquals("添加的项目成员超过了最大值(10)", "已到达到成员最大数，不能再继续选择成员！",
                projectPage.getText(projectFileReader.getMaxMemberHintForAddMember()));
        int selectedMemberCount = projectPage.getElements(projectFileReader.getSelectedMemberNameInAddMember() + " li").size();
        projectPage.clearAndSendKeys(projectFileReader.getSearchInputForAddMember(), "z");
        projectPage.waitForElement(".loading.icon",1);
        projectPage.waitElementInvisible(".loading.icon");
        projectPage.clickElements(projectFileReader.getSelectedMemberItemInAddMember());
        assertFalse("添加成员框，超过上限后，依然可以继续选择成员",
                projectPage.waitForItemCountChange(projectFileReader.getSelectedMemberNameInAddMember() + " li", selectedMemberCount, 2));
    }

    @Test
    public void test05_1_testExitProject() throws ParseException, InterruptedException {
        zombieProject.addProjectMember(ciwangUser);
        groupTest.removeAllGroup(ciwangPage);
        projectTest.addPin(ciwangPage, zombieProject.getProjectFullName());
        groupTest.addNewGroup(ciwangPage,zombieProject.getProjectFullName());
        testExitProject(ciwangUser, zombieProject);

        List<PageLink> projectLinks = new ArrayList<PageLink>();
        projectLinks.add(new PageLink(notificationFileReader.getNotificationSecondLink(), zombieProject.getProjectName(),
                projectFileReader.getPrivateProjectTitle(), zombieProject.getProjectLink()));
        Notification exitProject = new Notification(notificationFileReader.getProjectMemberIconClass(),ciwangUser,
                ciwangUser.getUserName()+" 退出了你的项目 "+zombieProject.getProjectName() + "。",zombieUser,projectLinks);
        notificationTest.verifyForMultiLinkFormat(exitProject, "退出项目 ", notificationFileReader.getSystemItemIcon());
        mailSetList.add(exitProject.getContent());

        AdvancedActivity exitProjectActivity = new AdvancedActivity(ciwangUser,zombieUser,zombieProject);
        exitProjectActivity.constructExitProjectActivity(ciwangUser, activityFileReader.getProjectIconClass());
        exitProjectActivity.checkActivity(zombiePage, activityFileReader.getOtherActivityTagInProjectHomePage());
    }
    @Test
    public void test05_0_testExitProject() throws  InterruptedException {
        zombieProject.addProjectMember(ciwangUser);
        advancedTask.putTestTaskForSomeone(zombieProject.getCreator().getPage(),
                advancedTask.getAdvancedTestTask(zombieProject.getCreator(), zombieProject, ciwangUser));
        ciwangPage.navigate(zombieProject.getProjectMemberLink(ciwangUser.getUserLoginName()) , "#inner-menu img");
        ciwangPage.clearAndSendKeys(projectFileReader.getSearchMemberInput(), ciwangUser.getUserName().substring(0, ciwangUser.getUserName().length() - 2));
        ciwangPage.clickElement(projectFileReader.getExitProject());
        String currentUrl = ciwangPage.getCurrentUrl();
        ciwangPage.getDriver().switchTo().alert().accept();
        ciwangPage.verifyHint("你还有未完成任务，请指派给其它人后再退出");
        Thread.sleep(4000);
        assertEquals("项目成员还有未完成的任务，却可以退出项目",currentUrl,  ciwangPage.getCurrentUrl() );
    }

    public void testExitProject( User ciwangUser,Project project) throws InterruptedException {
        Page ciwangPage = ciwangUser.getPage();

        advancedTask.deleteSomeoneAllTasks(zombiePage,project,ciwangUser);
        ciwangPage.navigate(project.getProjectMemberLink(ciwangUser.getUserLoginName()), projectFileReader.getExitProject());
        ciwangPage.checkAlert(projectFileReader.getExitProject(),"退出项目的button按钮失效，建议点确定和取消","确认退出项目？");
        ciwangPage.refresh(project.getProjectMemberLink(ciwangUser.getUserLoginName()),activityFileReader.getActivityItem());
        ciwangPage.assertElementPresent("ciwang被移除后，访问项目，没有跳转到个人中心页面",
               activityFileReader.getActivityItem() );
        assertEquals("用户退出项目后，依然能在常用项目中发现该项目",-1,projectTest.findPrivateProjectInPinList(ciwangUser.getPage(),project.getProjectName()));
        ciwangPage.navigate(ciwangPage.getProjectUrl(),projectFileReader.getProjectItemInAllProjectPage());
        assertFalse("用户退出项目后，分组未消失", ciwangUser.getPage().elementIsPresent(projectFileReader.getProjectGroupItem(), 2));
    }


    @Test
    public void test08_sendHimMessage() throws ParseException, InterruptedException {
        ciwangProject.addProjectMember(zombieUser);
        ciwangPage.navigate(ciwangProject.getProjectMemberLink(zombieUser.getUserLoginName())
               , projectFileReader.getSendHimMessageButton());
        MessageTest messageTest = new MessageTest();
        messageTest.sendHimMessageTest(zombieUser,ciwangUser,"hisMessage :"+ciwangPage.getMark(),projectFileReader.getSendHimMessageButton());
    }

    @AfterClass
    public static void tearDown(){
        zombiePage.getDriver().quit();
        ciwangPage.getDriver().quit();
    }

}

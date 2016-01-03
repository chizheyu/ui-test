package org.ChiTest.Project;

import dataReader.ConfigureInfo;
import org.ChiTest.Activity.ActivityFileReader;
import org.ChiTest.Activity.AdvancedActivity;
import org.ChiTest.Code.CodeFileReader;
import org.ChiTest.Email.Email;
import org.ChiTest.Email.EmailTest;
import org.ChiTest.Notification.AdvancedNotificationTest;
import org.ChiTest.Page.PS;
import org.ChiTest.Page.Page;
import org.ChiTest.Topic.Topic;
import org.ChiTest.Topic.TopicFileReader;
import org.ChiTest.User.User;
import org.ChiTest.WebComponent.Tag.TagFileReader;
import org.ChiTest.WebComponent.Tag.TopicTag;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.Cookie;
import org.ChiTest.WebComponent.ScreenShot;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by dugancaii on 1/12/2015.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProjectOwnerTest{
    static User zombieUser;
    static User ciwangUser;
    static Page ciwangPage;
    static Page zombiePage;
    static Cookie zombieCookie;
    static Cookie ciwangCookie;
    static ProjectOwnerFileReader projectOwnerFileReader;
    static ProjectFileReader projectFileReader;
    static ActivityFileReader activityFileReader;
    static CodeFileReader codeFileReader;
    static TopicFileReader topicFileReader;
    static TagFileReader tagFileReader;
    static Project zombieProject;
    static Project duplicateProject;
    static ConfigureInfo configureInfo;
    static String projectName;
    static Set mailCheckList;
    static TopicTag newOwnerTag;
    static TopicTag oldOwnerTag;
    static TopicTag bugTag;

    static Logger log = Logger.getLogger("projectOwnerTest");
    @BeforeClass
    public static void setUp() throws Exception {
        configureInfo = new ConfigureInfo(true,true);
        projectOwnerFileReader = new ProjectOwnerFileReader();
        activityFileReader = new ActivityFileReader();
        projectFileReader = new ProjectFileReader();
        codeFileReader = new CodeFileReader();
        topicFileReader = new TopicFileReader();
        tagFileReader = new TagFileReader();
        zombiePage = configureInfo.getZombieUser().getPage();
        ciwangPage = configureInfo.getCiwangUser().getPage();

        zombieUser = new User("coding48","coding48","123456",zombiePage,"Fruit-3.png");
        ciwangUser = new User("dugancai","dugancai","123456",ciwangPage);

        zombieCookie = zombiePage.getStagingSid("0435d6dc-882a-4abb-89ac-28516e40564e");
        ciwangCookie = ciwangPage.getStagingSid("ba1d49e5-1f83-4884-8bdd-187fcb2d899b");

        zombieUser.autoLogin(configureInfo.getLoginUrl(),zombieCookie );
        ciwangUser.autoLogin(configureInfo.getLoginUrl(), ciwangCookie);
        projectName = projectFileReader.getPrivateProject() +zombiePage.getMark();
        zombieProject = new Project(zombieUser,projectName ,"this is a test ","private");
        duplicateProject = new Project(ciwangUser,projectName+"dup" ,"this is a test ","private");
        zombieProject.createSampleProject();
        mailCheckList = new HashSet();
        newOwnerTag = new TopicTag("newOwnerTag","","#1b1c1d",ciwangUser,zombieProject);
        oldOwnerTag = new TopicTag("oldOwnerTag","","#1b1c1d",zombieUser,zombieProject);
        bugTag = new TopicTag("Bug","","#1b1c1d",zombieUser,zombieProject);
        //zombieProject = new Project(zombieUser, "testOwner","this is a test ","private");
    }
    @Rule
    public ScreenShot screenShotZombie = new ScreenShot(zombieUser);
    @Rule
    public ScreenShot screenShotCiwang = new ScreenShot(ciwangUser);

    @Test
    public void test01_projectOwnerChange() throws InterruptedException {
        Page projectPage = zombieProject.getCreator().getPage();
        projectPage.navigate(zombieProject.getProjectAdvancedSettingLink(), projectOwnerFileReader.getProjectTransferButton());
        projectPage.assertElementPresent("项目转让的设置一开始不是默认 disabled 的", projectOwnerFileReader.getProjectTransferButton() + ".disabled");
        projectPage.assertTextEquals("项目转让的标题错了", "转让项目",projectOwnerFileReader.getProjectTransferHeader());
        projectPage.assertTextEquals("项目转让的注意事项错了", "注意： 项目转让后，你将不再是“项目所有者”，你将失去某些管理权限（如添加成员、开启演示、删除项目等）。同时，项目转让后，项目的git 仓库访问路径会发生变化，" +
                        "例如：用户 Coding 将项目转给用户 Demo 后， git 仓库访问路径将由 https://git.coding.net/coding/test.git 变成 https://git.coding.net/demo/test.git",
                projectOwnerFileReader.getProjectTransferNotice());

        projectPage.assertElementPresent("转让成员选择框旁的下拉箭头消失", projectOwnerFileReader.getProjectTransferSelectBox() + " .dropdown.icon");
        projectPage.clickElement(projectOwnerFileReader.getProjectTransferSelectBox(), "#transfer-member .no-members span");
        projectPage.assertTextEquals("没有项目成员可以选择转让时，下拉框", "无其他成员", "#transfer-member .no-members span");
    }
    @Test
    public void test02_authorityAfterTransfer() throws InterruptedException {
        Page projectPage = zombieProject.getCreator().getPage();
        zombieProject.addProjectMember(ciwangUser);
        zombieProject.addProjectMember( new User("coding", "coding", "dffff"));

        projectPage.navigate(zombieProject.getProjectAdvancedSettingLink(), projectOwnerFileReader.getProjectTransferButton());
        dropDownSelectTest(projectPage, projectOwnerFileReader.getProjectTransferSelectBox(), projectOwnerFileReader.getProjectTransferSelectItem());

        sendPassWordConfirmTest(projectPage,zombieUser);
        zombieProject.setCreator(ciwangUser);

        //验证 zombie 的权限
        verifyZombieAuthorityAfterChangeOwner(projectPage);
        //验证 ciwang 的权限
        verifyCiwangAuthorityAfterChangeOwner();
        zombieProject.deleteProject();
    }
    @Test
    public void test03_transferSameProject() throws InterruptedException {
        Project zombieProject = new Project(zombieUser,duplicateProject.getProjectName() ,"this is a test ","private");
        Page projectPage = zombieProject.getCreator().getPage();
        duplicateProject.createSampleProject();
        zombieProject.createFixtureProject();
        duplicateProject.addProjectMember(zombieUser);
        ciwangPage.navigate(duplicateProject.getProjectAdvancedSettingLink(), projectOwnerFileReader.getProjectTransferSelectBox());
        ciwangPage.clickElement(projectOwnerFileReader.getProjectTransferSelectBox(), projectOwnerFileReader.getProjectTransferSelectItem());
        ciwangPage.clickElement(projectOwnerFileReader.getProjectTransferSelectItem());
        sendPassWordConfirm(ciwangUser);
        ciwangPage.verifyHint("对方存在同名项目");
        assertEquals("存在相同项目的情况下转让成功了", duplicateProject.getProjectAdvancedSettingLink(),ciwangPage.getCurrentUrl());
        zombieProject.deleteProject();

        sendPassWordConfirm(ciwangUser);
        ciwangPage.verifyHint("转让成功！");
        duplicateProject.setCreator(zombieUser);
        ciwangPage.waitForElement(activityFileReader.getActivityItem(),10);
        assertEquals("转让之后没有调到项目首页", duplicateProject.getProjectLink(),ciwangPage.getCurrentUrl());
        projectPage.navigate(duplicateProject.getProjectAdvancedSettingLink());
        assertTrue("再次转让项目后，新的创建者无法进入项目", projectPage.elementIsPresent(projectOwnerFileReader.getProjectTransferButton(), 10));

        AdvancedActivity advancedActivity = new AdvancedActivity(ciwangUser,zombieUser,duplicateProject);
        advancedActivity.constructOwnerChangeActivity(ciwangUser,zombieUser, activityFileReader.getProjectIconClass());
        advancedActivity.checkActivity(zombiePage, activityFileReader.getOtherActivityTagInProjectHomePage());

        AdvancedNotificationTest advancedNotificationTest = new AdvancedNotificationTest(ciwangUser,zombieUser,duplicateProject);
        advancedNotificationTest.constructprojectOwnerChange(ciwangUser, mailCheckList);
        advancedNotificationTest.checkTaskNotification(zombiePage);
        (new EmailTest()).checkEmail(zombiePage, mailCheckList, new Email("coding_test48","163.com","chi2014"));
        duplicateProject.deleteProject();
    }

    public void verifyCiwangAuthorityAfterChangeOwner() throws  InterruptedException {
        Topic zombieTopic = new Topic(zombieProject, "topic for authority", "hello topic", zombieUser);
        zombieTopic.createTopic();
        bugTag.removeTagInProject(zombieProject);
        ciwangPage.assertCountEquals("新的项目所有者不能删除老的用户原来的tag", 4, tagFileReader.getTagItemInTagList());
        oldOwnerTag.removeTagInProject(zombieProject);
        ciwangPage.assertCountEquals("新的项目所有者不能删除老的用户新创建的tag", 3, tagFileReader.getTagItemInTagList() );
        zombieTopic.editTopic(ciwangPage, "I can edit", "1111");
        zombieProject.removeProjectMember( zombieUser);
        zombieProject.addProjectMember(zombieUser);
        ciwangPage.navigate(zombieProject.getProjectCodeLink(), codeFileReader.getCodeUrlInput() );
        ciwangPage.assertAttributeEquals("转让项目后，代码仓库的 url 没有变化",zombieProject.getProjectCodeCloneUrl(),
                codeFileReader.getCodeUrlInput(), "value");
    }
    public void verifyZombieAuthorityAfterChangeOwner(Page projectPage) throws InterruptedException {
        projectPage.navigate(zombieProject.getSettingLink());
        projectPage.assertElementNotPresent( "转让项目后，依然可以进入项目设置", "item[href*='setting/advanced']");

        Topic ciwangTopic = new Topic(zombieProject, "topic for authority 2", "hello topic", ciwangUser);
        ciwangTopic.createTopic();
        projectPage.navigate(ciwangTopic.getTopicUrl(), topicFileReader.getTopicTitleInDetailPage() );
        assertFalse("转让项目后，进入别人创建的讨论后，竟然可以看到删除和编辑讨论的按钮！！", projectPage.elementIsPresent(topicFileReader.getDeleteButton(), 2));

        newOwnerTag.addTagInProject(ciwangPage);
        oldOwnerTag.addTagInProject(projectPage);

        projectPage.navigate(zombieProject.getProjectTopicHomeLink(),tagFileReader.getTagSettingIcon());
        projectPage.clickElement(tagFileReader.getTagSettingIcon(), tagFileReader.getNewTagTextInput());
        projectPage.assertAttributeEquals("项目成员无法编辑别人的标签","Bug", tagFileReader.getTagTextInput(),"value");
        projectPage.navigate(zombieProject.getProjectMemberLink());
        projectPage.assertElementNotPresent("转让项目后,还能够添加项目成员", projectFileReader.getAddIconForAddMember());
    }
    public void sendPassWordConfirm(User projectOwner){
        Page projectPage = projectOwner.getPage();
        projectPage.clickElement(projectOwnerFileReader.getProjectTransferButton());
        projectPage.clearAndSendKeys(projectOwnerFileReader.getProjectTransferPasswordInput(), projectOwner.getUserPassword());
        projectPage.clickElement(projectOwnerFileReader.getProjectTransferPasswordConfirm());
        projectPage.waitElementInvisible(".loading.icon");
        if(!projectPage.waitElementInvisible(projectOwnerFileReader.getProjectTransferPasswordConfirm())){
            projectPage.clickElement(projectOwnerFileReader.getProjectTransferPasswordConfirm());
        }
    }
    public void sendPassWordConfirmTest(Page projectPage, User transfer){
        projectPage.clickElement(projectOwnerFileReader.getProjectTransferButton());
        projectPage.clearAndSendKeys(projectOwnerFileReader.getProjectTransferPasswordInput(),"ddddddd");
        projectPage.clickElement(projectOwnerFileReader.getProjectTransferPasswordConfirm());
        projectPage.waitElementInvisible(projectOwnerFileReader.getProjectTransferPasswordConfirm());
        projectPage.verifyHint("密码错误");

        projectPage.waitForElement(projectOwnerFileReader.getProjectTransferPasswordInput(), PS.midWait);
        if(!projectPage.elementIsPresent(projectOwnerFileReader.getProjectTransferPasswordInput())){
            projectPage.clickElement(projectOwnerFileReader.getProjectTransferButton());
        }
        projectPage.clearAndSendKeys(projectOwnerFileReader.getProjectTransferPasswordInput(),transfer.getUserPassword());
        projectPage.clickElement(projectOwnerFileReader.getProjectTransferPasswordConfirm());
        projectPage.waitElementInvisible(projectOwnerFileReader.getProjectTransferPasswordConfirm());
        projectPage.verifyHint("转让成功！");
    }
    public void dropDownSelectTest(Page projectPage, String dropDownButton, String selectItem){
        projectPage.clickElement(dropDownButton,selectItem);
        projectPage.assertElementPresent("转让成员选择框旁的头像消失", selectItem + " img");
        projectPage.clickElement(selectItem,1);
        projectPage.assertTextEquals("选中项目成员后，项目选择框上显示有误", ciwangUser.getUserName(),
                dropDownButton);
    }
    @AfterClass
    public static void tearDown() throws Exception {
        zombieProject.deleteProject();
        duplicateProject.deleteProject();
        zombieUser.getPage().getDriver().quit();
        ciwangUser.getPage().getDriver().quit();
    }


}

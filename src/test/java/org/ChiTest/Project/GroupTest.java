package org.ChiTest.Project;

import dataReader.ConfigureInfo;
import org.ChiTest.Page.PS;
import org.ChiTest.Page.Page;
import org.ChiTest.User.User;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Keys;
import org.ChiTest.WebComponent.ScreenShot;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by dugancaii on 11/13/2014.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GroupTest {
    protected  static User zombieUser;
    protected  static User ciwangUser;
    private static Cookie zombieCookie;
    private static ProjectTest projectTest;
    private static ProjectFileReader projectFileReader;
    private static Page projectPage;
    private static Project zombiePrivate;
    private static Project zombiePublic;
    private  static ConfigureInfo configureInfo;
    private static Logger log = Logger.getLogger("GroupTest");
    public GroupTest() throws ParseException {
        projectTest = new ProjectTest();
        projectFileReader = new ProjectFileReader();
    }
    @BeforeClass
    public static void setUp() throws Exception {
        configureInfo = new ConfigureInfo(false,true);
        projectPage = configureInfo.getZombieUser().getPage();
        zombieUser = new User("coding_test","Z_topic_test","123456",projectPage,"Fruit-3.png");
        zombieCookie = projectPage.getStagingSid("c8419ba1-f1de-4315-ad4f-aabd17b08b6d");
        zombieUser.autoLogin(configureInfo.getLoginUrl(),zombieCookie );

        projectTest = new ProjectTest();
        projectFileReader = new ProjectFileReader();

        zombieCookie = configureInfo.getZombieCookie();
        zombiePrivate = new Project(zombieUser,zombieUser.getUserLoginName() + "Private","private");
        zombiePublic = new Project(zombieUser, "public","public");

        removeAllGroup(projectPage);
        projectPage.navigate(projectPage.getProjectUrl(),projectFileReader.getProjectItemForBlockListInAllProjectPage());
        if(projectPage.getElementCount(projectFileReader.getProjectItemForBlockListInAllProjectPage())<10){
            String projectName = "groupProject";
            Project project1 = new Project(zombieUser,projectName,"private");
            for(int i = 0; i < 10 - projectPage.getElementCount(projectFileReader.getProjectImageForBlockList());i++) {
                project1.createFixtureProject();
                project1.setProjectName(projectName + i);
            }
        }

        zombiePrivate.createFixtureProject();
        zombiePublic.createFixtureProject();
    }
    @Rule
    public ScreenShot screenShotZombie = new ScreenShot(zombieUser);

    @Test
    public void test01_addNewGroup()  {
        projectPage.navigate(projectPage.getProjectUrl(), projectFileReader.getProjectNameForBlockList());
        projectTest.changeProjectListLayout(projectPage, ".block", projectFileReader.getProjectItemForBlockListInAllProjectPage());
        checkGroupButton();
        startGroup(projectPage);
        projectPage.assertTextEquals("项目分组时，选择项目时的计数信息显示不正确，页面上方", "(已选 0 个项目)", projectFileReader.getProjectGroupCountText());
        int projectNum = projectPage.findItemInOnePageByTextEquals(projectFileReader.getProjectNameForBlockList(), zombiePrivate.getProjectFullName());
        projectPage.selectItemWithCountTest(projectFileReader.getProjectItemForBlockListInAllProjectPage(),
                projectNum, projectFileReader.getProjectGroupCountText(),"项目分组时",".green"  );
        projectPage.selectItemCancelWithCountTest(projectFileReader.getProjectItemForBlockListInAllProjectPage(),
                projectNum, projectFileReader.getProjectGroupCountText(), "项目分组时", ".green");

        projectPage.clickSelectingItem(projectFileReader.getProjectNameForBlockList(), projectNum, projectFileReader.getProjectGroupCountText());
        projectPage.backToTop();
        projectPage.clickElement(projectFileReader.getProjectGroupConfirmButtonBeforeHaveGroup(), projectFileReader.getProjectGroupItemsWrapper());
        projectPage.clickElement(projectFileReader.getProjectGroupItem(), projectFileReader.getProjectGroupTitle());

        projectPage.refresh(projectFileReader.getProjectGroupTitle());
        projectPage.assertTextEquals("新建分组的名字不正确", "新分组", projectFileReader.getProjectGroupTitle());
        projectPage.clickElement(projectFileReader.getProjectGroupItem());
        projectPage.assertTextEquals("分组内的项目的名字不正确", zombiePrivate.getProjectFullName(), projectFileReader.getProjectNameInFirstGroup());

        startGroup(projectPage);
        projectPage.clickElement(projectFileReader.getProjectGroupItem(), projectFileReader.getProjectNameInFirstGroup());
        projectPage.selectItemWithCountTest(projectFileReader.getProjectInFirstGroup(), projectFileReader.getProjectGroupCountText(), "项目分组内", " .green");
        projectPage.selectItemCancelWithCountTest(projectFileReader.getProjectInFirstGroup(), projectFileReader.getProjectGroupCountText(), "项目分组内", " .green");
        projectPage.clickElement(projectFileReader.getProjectGroupItem());
        projectPage.clickElement(projectFileReader.getProjectGroupCancelButton());

        projectPage.clickElement(projectFileReader.getProjectGroupItem());
        projectPage.clickElement(projectFileReader.getProjectLogoInFirstGroup(),projectFileReader.getPrivateProjectTitle());
        projectPage.getWaitTool().waitForContentNotNull(projectFileReader.getPrivateProjectTitle(),15);
        projectPage.assertTextEquals("分组后点击分组内的项目，不能到达相应项目", zombiePrivate.getProjectName(), projectFileReader.getPrivateProjectTitle());
    }
    @Test
    public void test02_moveToOtherGroup() throws InterruptedException {
        projectPage.refresh(projectPage.getProjectUrl(),projectFileReader.getProjectNameForBlockList());
        if(!projectPage.elementIsPresent(projectFileReader.getProjectGroupItem(),2)) {
            addNewGroup(projectPage, zombiePrivate.getProjectFullName());
        }
        int projectNum = projectPage.findItemInOnePageByTextEquals(projectFileReader.getProjectNameForBlockList(), zombiePublic.getProjectFullName());
        int itemCount = projectPage.getElementCount(projectFileReader.getProjectItemInAllProjectPage());
        startGroup(projectPage);
        projectPage.clickElement(projectFileReader.getProjectNameForBlockList(),projectNum);
        projectPage.backToTop();
        projectPage.clickElement(projectFileReader.getProjectGroupConfirmButton(),projectFileReader.getProjectMoveOutOfGroup());
        projectPage.clickElement(projectFileReader.getProjectMoveOutOfGroup());
        projectPage.verifyHint("请在分组内选择需要移出的项目！");

        projectPage.clickElement(projectFileReader.getProjectNameForBlockList(),projectNum);
        projectPage.backToTop();
        projectPage.clickElement(projectFileReader.getProjectGroupConfirmButton(),projectFileReader.getProjectMoveToOtherGroup());
        projectPage.clickElement(projectFileReader.getProjectMoveToOtherGroup());
        assertTrue("将项目移动到已有分组内，组内项目数量没有变化", projectPage.waitForItemCountChange(projectFileReader.getProjectItemInAllProjectPage(), itemCount, 5));
        projectPage.refresh(projectFileReader.getProjectGroupItem());
        projectPage.backToTop();
        projectPage.clickElement(projectFileReader.getProjectGroupItem(), projectFileReader.getProjectNameInFirstGroup());
        projectPage.assertCountEquals("将项目移动到已有分组内，分组内的项目数没有变化", 2, projectFileReader.getProjectNameInFirstGroup());
    }
    public void addProjectInFirstGroup(Page projectPage, int projectNum){
        projectPage.clickElement(projectFileReader.getProjectNameForBlockList(),projectNum);
        projectPage.clickElement(projectFileReader.getProjectGroupConfirmButton());
        projectPage.clickElement(projectFileReader.getProjectMoveToOtherGroup());
    }

    @Test
    public void test03_moveToNewGroup(){
        startGroup(projectPage);
        if(!projectPage.elementIsPresent(projectFileReader.getProjectGroupItem(),2)) {
            addNewGroup(projectPage, zombiePrivate.getProjectFullName());
            addProjectInFirstGroup(projectPage,0);
        }
        int groupCount = projectPage.getElementCount(projectFileReader.getProjectGroupItem());
        if(!projectPage.elementIsPresent(projectFileReader.getProjectGroupItemsWrapper())){
            projectPage.clickElement(projectFileReader.getProjectGroupItem());
        }
        projectPage.clickElement(projectFileReader.getProjectNameInFirstGroup());
        String moveOutProjectName = projectPage.getText(projectFileReader.getProjectNameInFirstGroup());
        projectPage.clickElement(projectFileReader.getProjectGroupConfirmButton());
        projectPage.clickElement(projectFileReader.getProjectMoveToNewGroup());

        assertTrue("将分组内项目移动到新分组，分组数量没有变化", projectPage.waitForItemCountChange(projectFileReader.getProjectItemInAllProjectPage(), groupCount, 5));
        projectPage.refresh(projectFileReader.getProjectGroupItem());
        projectPage.clickElement(projectFileReader.getProjectGroupItem());
        assertEquals("将项目移动到新分组内，新分组内的项目不正确", moveOutProjectName ,
                projectPage.getText(projectFileReader.getProjectNameInFirstGroup()));
        assertEquals("将项目移动到新分组内，新分组的名字不正确","新分组1",
                projectPage.getAttribute(projectFileReader.getProjectGroupTitleInput(), "value"));
    }
    @Test
    public void test04_renameGroup(){
        startGroup(projectPage);
        if(!projectPage.elementIsPresent(projectFileReader.getProjectGroupItem(),2)) {
            addNewGroup(projectPage, zombiePrivate.getProjectFullName());
        }
        projectPage.clickElement(projectFileReader.getProjectGroupItem());
        projectPage.clearAndSendKeys(projectFileReader.getProjectGroupTitleInput(), "重命名过了");
        projectPage.sendKeys(Keys.ENTER);
        projectPage.verifyHint("分组名更新成功！");
        projectPage.refresh(projectFileReader.getProjectGroupTitle());
        assertEquals("项目分组重命名后，刷新页面，名字有误", "重命名过了" ,projectPage.getText(projectFileReader.getProjectGroupTitle()));
    }
    @Test
    public void test05_groupOverLimited(){
        removeAllGroup(projectPage);
        startGroup(projectPage);
        int groupCount = projectPage.getElementCount(".group");
        for(int i = 0; i < 9; i++) {
            projectPage.clickSelectingItem(projectFileReader.getProjectNameForBlockList(), i, projectFileReader.getProjectGroupCountText());
        }
        if(projectPage.elementIsPresent(projectFileReader.getProjectGroupItem(),2)) {
            projectPage.clickElement(projectFileReader.getProjectGroupConfirmButton(),projectFileReader.getProjectMoveToNewGroup());
            projectPage.clickElement(projectFileReader.getProjectMoveToNewGroup(),projectFileReader.getProjectGroupItemsWrapper());
        }else{
            projectPage.clickElement(projectFileReader.getProjectGroupConfirmButtonBeforeHaveGroup(), projectFileReader.getProjectGroupItemsWrapper());
        }

        if(!projectPage.elementIsPresent(".group.open ")){
            projectPage.clickElement(projectFileReader.getProjectGroupItem());
        }
        projectPage.waitForItemCountChange(".group", groupCount,5);
        projectPage.assertCountEquals("项目分组内的项目没有达到9个", 9, ".group.open .pin-project-cover.open-group");
        projectPage.refresh(".group");
        startGroup(projectPage);
        projectPage.clickSelectingItem(projectFileReader.getProjectNameForBlockList(),0, projectFileReader.getProjectGroupCountText());
        projectPage.clickElement(projectFileReader.getProjectGroupConfirmButton(),projectFileReader.getProjectMoveToOtherGroup());
        projectPage.clickElement(projectFileReader.getProjectMoveToOtherGroup());
        assertTrue("每个分组项目数不能超过9个的提示消失",projectPage.verifyHint("每个分组项目数不能超过 9 个！"));
    }
    public static void startGroup(Page projectPage) {
        projectPage.navigate(projectPage.getProjectUrl(), projectFileReader.getWaitEelementInProjectHome());
        projectPage.backToTop();
        projectTest.changeProjectListLayout(projectPage, ".block", projectFileReader.getProjectItemInAllProjectPage());
        if (projectPage.elementIsPresent(projectFileReader.getProjectGroupButton(),2)) {
            projectPage.clickElement(projectFileReader.getProjectGroupButton(), projectFileReader.getProjectGroupConfirmButton());
        }
    }
    public static void removeAllGroup(Page projectPage)  {
        startGroup(projectPage);
        while(projectPage.elementIsPresent(projectFileReader.getProjectGroupItem(), PS.shortWait)) {
            int itemCount = projectPage.getElements(projectFileReader.getProjectItemInAllProjectPage()).size();
            projectPage.clickElement(projectFileReader.getProjectGroupItem());
            projectPage.clickElements(projectFileReader.getProjectNameInFirstGroup());
            projectPage.clickElement(projectFileReader.getProjectGroupConfirmButton());
            projectPage.clickElement(projectFileReader.getProjectMoveOutOfGroup());
            assertTrue("删除项目分组后，分组数没有改变",projectPage.waitForItemCountChange(projectFileReader.getProjectItemInAllProjectPage(), itemCount,10));
        }
        projectPage.clickElement(projectFileReader.getProjectGroupCancelButton());
    }

    public void checkGroupButton(){
        projectPage.assertTextEquals("项目分组的按钮消失或按钮上没有显示‘分组’", "分组", projectFileReader.getProjectGroupButton());
        projectPage.clickElement(projectFileReader.getProjectGroupButton(), projectFileReader.getProjectGroupConfirmButtonBeforeHaveGroup());
        projectPage.assertTextEquals("项目分组按钮点击无效", "移动至新分组", projectFileReader.getProjectGroupConfirmButtonBeforeHaveGroup());
        projectPage.assertTextEquals("项目分组的按钮消失或按钮上没有显示‘返回’", "返回", projectFileReader.getProjectGroupCancelButton());
        assertTrue("分组取消按钮失效", projectPage.clickElement(projectFileReader.getProjectGroupCancelButton(), projectFileReader.getProjectGroupButton()));
    }
    public void addNewGroup(Page projectPage, String projectFullName) {
        projectPage.navigate(projectPage.getProjectUrl(),projectFileReader.getProjectGroupButton());
        startGroup(projectPage);
        projectPage.clickSelectingItem(projectFileReader.getProjectNameForBlockList(),
                projectPage.findItemInOnePageByTextEquals(projectFileReader.getProjectNameForBlockList(), projectFullName), projectFileReader.getProjectGroupCountText());
        projectPage.backToTop();
        projectPage.clickElement(projectFileReader.getProjectGroupConfirmButtonBeforeHaveGroup(), projectFileReader.getProjectGroupItemsWrapper());
        projectPage.clickElement(projectFileReader.getProjectGroupItem());
        projectPage.refresh(projectFileReader.getProjectNameForBlockList());
    }
    @AfterClass
    public static void tearDown() {
        projectPage.getDriver().quit();
    }

}

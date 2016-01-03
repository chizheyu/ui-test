package org.ChiTest.Project;

import dataReader.ConfigureInfo;
import org.ChiTest.Page.PS;
import org.ChiTest.Page.Page;
import org.ChiTest.User.User;
import org.ChiTest.WebComponent.ScreenShot;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.TimeoutException;

import java.text.ParseException;

import static org.junit.Assert.*;

/**
 * Created by dugancaii on 4/7/2015.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PinTest {
    protected  static User zombieUser;
    protected  static User ciwangUser;
    private static Cookie zombieCookie;
    private static ProjectFileReader projectFileReader;
    private static Page projectPage;
    private static Project zombiePrivate;
    private static Project zombiePublic;
    private  static ConfigureInfo configureInfo;
    private static ProjectTest projectTest;
    private static GroupTest groupTest;
    private static Logger log = Logger.getLogger("PinTest");


    @BeforeClass
    public static void setUp() throws Exception {
        configureInfo = new ConfigureInfo(false,true);
        zombieUser = configureInfo.getZombieUser();
        projectTest = new ProjectTest();
        projectFileReader = new ProjectFileReader();
        projectPage = zombieUser.getPage();

        zombieCookie = configureInfo.getZombieCookie();
        zombieUser.autoLogin(configureInfo.getLoginUrl(), zombieCookie);
        zombiePrivate = new Project(zombieUser,zombieUser.getUserLoginName() + "Private","private");
        zombiePublic = new Project(zombieUser, "public","public");
        groupTest = new GroupTest();
        zombiePrivate.createFixtureProject();
        zombiePublic.createFixtureProject();
        groupTest.removeAllGroup(projectPage);
    }
    public PinTest()  {
        projectFileReader = new ProjectFileReader();
        projectTest = new ProjectTest();
    }

    @Rule
    public ScreenShot screenShotZombie = new ScreenShot(zombieUser);
    @Test
    public void test01_0_projectPin() throws InterruptedException, ParseException {
        deleteAllPin(projectPage);
        addPinTest();
        deletePinTest();
    }
    public void deleteAllPin(Page projectPage){
        projectPage.navigate(projectPage.getProjectUrl(), projectFileReader.getWaitEelementInProjectHome());
        int pinProjectCount;
        projectTest.changeProjectListLayout(projectPage, ".block", projectFileReader.getProjectNameForBlockList());
        while (projectPage.elementIsPresent(projectFileReader.getActivePinIconInProjectPage(), PS.midWait)) {
            pinProjectCount = projectPage.getElementCount(projectFileReader.getActivePinIconInProjectPage());
            projectPage.clickElement(projectFileReader.getActivePinIconInProjectPage());
            projectPage.waitForItemCountChange(projectFileReader.getActivePinIconInProjectPage(), pinProjectCount, 5);
        }
    }

    public void deletePinTest() throws InterruptedException {
        //获取项目图片链接
        projectPage.navigate(projectPage.getProjectUrl(), projectFileReader.getWaitEelementInProjectHome());
        projectTest.changeProjectListLayout(projectPage, ".block", projectFileReader.getProjectNameForBlockList());
        projectPage.moveToElement(projectFileReader.getDropIconInTopMenu());
        int publicNum = findTest5PublicInBlockList();
        String publicImageUrl =  projectPage.getAttribute(projectFileReader.getProjectImageForBlockList(),"src",publicNum);

        //到所有项目页取消项目固定，先取消一个私有项目看有没有锁,去掉私有项目，看图片的链接
        moveToItemClickPin(zombiePrivate.getProjectFullName());
        projectPage.moveToElement(projectFileReader.getDropIconInTopMenu());
        projectPage.waitElementDisappear(projectFileReader.getPrivateProjectNameInPinList(), 5);
        projectPage.assertElementNotPresent("去掉常用项目private后，还能在常用列表中发现项目", projectFileReader.getPrivateProjectNameInPinList(),PS.shortWait);
        projectPage.assertAttributeEquals("去掉private项目后，public项目显示不正常", publicImageUrl, projectFileReader.getProjectImageInPinList(), "src");
        //去掉public，看看设置按钮是否出现
        moveToItemClickPin(zombiePublic.getProjectFullName());
        projectPage.moveToElement(projectFileReader.getDropIconInTopMenu());
        projectPage.waitForElement(projectFileReader.getPinSettingButtonInEmptyList(),7);
        projectPage.assertElementNotPresent("去掉常用项目public后，还能在常用列表中发现项目", projectFileReader.getPublicProjectNameInPinList(), PS.shortWait);
        projectPage.moveToElement(projectFileReader.getDropIconInTopMenu());
        try {
            projectPage.waitForContentNotNull(projectFileReader.getPinSettingButtonInEmptyList(), 5);
        }catch (TimeoutException e){
            projectPage.moveToElement(projectFileReader.getProjectItemInAllProjectPage());
            projectPage.moveToElement(projectFileReader.getDropIconInTopMenu());
            projectPage.waitForContentNotNull(projectFileReader.getPinSettingButtonInEmptyList(), 5);
        }
        projectPage.assertTextEquals("删光常用项目后，没有出现马上设置按钮", "马上去设置", projectFileReader.getPinSettingButtonInEmptyList());
    }
    public void addPinTest() throws InterruptedException {
        projectPage.navigate(projectPage.getUserCenterUrl(), projectFileReader.getDropIconInTopMenu());
        //点击左上角的按钮，点击马上设置去到达，项目首页,点击设置icon到项目首页
        projectPage.moveToElement(projectFileReader.getDropIconInTopMenu());
        projectPage.waitForContentNotNull(projectFileReader.getPinSettingButtonInEmptyList(), 3);

        projectPage.assertTextEquals("删光常用项目后，没有出现马上设置按钮", "马上去设置", projectFileReader.getPinSettingButtonInEmptyList());
        assertTrue("在常用项目里，点击马上去设置，没有跳转到项目主页",
                projectPage.clickElement(projectFileReader.getPinSettingButtonInEmptyList(),projectFileReader.getWaitEelementInProjectHome()));
        projectPage.navigate(projectPage.getUserCenterUrl(), projectFileReader.getDropIconInTopMenu());
        projectPage.moveToElement(projectFileReader.getDropIconInTopMenu());
        assertTrue("在常用项目里，点击设置图标，没有跳转到项目主页",
                projectPage.clickElement(projectFileReader.getPinSettingIcon(),projectFileReader.getWaitEelementInProjectHome()));
        //查找test5Private，public项目点击
        // 抽出来做操作函数
        projectTest.changeProjectListLayout(projectPage, ".block", projectFileReader.getProjectNameForBlockList());
        projectPage.waitForElement(projectFileReader.getProjectImageForBlockList(),7);
        projectPage.clickElement("#top-menu");
        Thread.sleep(2000);

        moveToItemClickPin(zombiePrivate.getProjectFullName());

        assertTrue("点击项目固定按钮(Test5Private)，按钮没有固定", projectPage.elementIsPresent(projectFileReader.getActivePinIconInProjectPage(), 4));

        int activityPinCount = projectPage.getElementCount(projectFileReader.getActivePinIconInProjectPage());
        moveToItemClickPin(zombiePublic.getProjectFullName());
        assertTrue("点击项目固定按钮(public)，按钮没有固定",
                projectPage.waitForItemCountChange(projectFileReader.getActivePinIconInProjectPage(), activityPinCount, 4));

        // 公有和私有,检查图标的锁项目名是否正确
        projectPage.moveToElement(projectFileReader.getDropIconInTopMenu());
        assertTrue("常用项目图标的图似乎没了", projectPage.getAttribute(projectFileReader.getProjectImageInPinList(), "src").contains("/static/project_icon/") ||
                projectPage.getAttribute(projectFileReader.getProjectImageInPinList(), "src").contains(projectPage.getProjectImgStoreUrl()));
        projectPage.assertTextEquals("test5Private似乎不在常用项目里", zombiePrivate.getProjectName(),projectFileReader.getPrivateProjectNameInPinList());
        projectPage.assertTextEquals("test5Public似乎不在常用项目里", zombiePublic.getProjectName(),projectFileReader.getPublicProjectNameInPinList());
        //点击左上角按钮看看是否存在，点击是否能进入相应的项目
        projectPage.assertLinkEquals("常用新项目的图标的链接出错", zombiePrivate.getProjectLink(),
                projectFileReader.getPrivateProjectLinkInPinList());

        projectPage.clickElement(projectFileReader.getTest5PrivateImageInPinList(),projectFileReader.getPrivateProjectTitle());
        projectPage.waitForContentNotNull(projectFileReader.getPrivateProjectTitle(), 7);
        projectPage.assertTextEquals("在常用项目里点击test5Private，没有到相应项目", zombiePrivate.getProjectName(),projectFileReader.getPrivateProjectTitle());
    }
    @Test
    public void test01_1_pinOverLimited() throws InterruptedException {
        projectPage.navigate(projectPage.getProjectUrl(),projectFileReader.getProjectItemForBlockListInAllProjectPage());
        if(projectPage.getElementCount(projectFileReader.getProjectItemForBlockListInAllProjectPage())<10){
            String projectName = "pinProject";
            Project project1 = new Project(zombieUser,projectName,"private");
            for(int i = 0; i < 10 - projectPage.getElementCount(projectFileReader.getProjectItemForBlockListInAllProjectPage());i++) {
                project1.createFixtureProject();
                project1.setProjectName(projectName + i);
            }
        }
        projectPage.refresh(projectFileReader.getProjectImageForBlockList());
        projectPage.moveToElement(projectFileReader.getProjectHeaderForWait());
        for(int i = 0; i < 9; i++) {
            projectPage.moveToElement(projectFileReader.getProjectImageForBlockList(), i);
            if(projectPage.getAttribute(projectFileReader.getPinIconInProjectIcon(),"title", i).equals("设置常用")){
                projectPage.clickElement(projectFileReader.getPinIconInProjectIcon(), i);
                assertNotNull("固定项目失败，项目图标上的针没有固定", projectPage.getWaitTool().waitForElement(
                        projectPage.getElement(projectFileReader.getProjectItemInAllProjectPage(), i, projectFileReader.getActivePinIconInProjectPage()), 10));

            }
        }

        projectPage.moveToElement( projectFileReader.getDropIconInTopMenu());
        assertEquals("常用项目数不能达到九个", 9, projectPage.getElementCount(projectFileReader.getProjectImageInPinList()));
        //不移动鼠标的话， 导航栏 的 drop down 会挡住
        projectPage.moveToElement( projectFileReader.getAddIconForCreateProjectInProjectPage());
        projectPage.moveToElement(projectFileReader.getProjectImageForBlockList(), 10);
        projectPage.clickElement(projectFileReader.getPinIconInProjectIcon(), 10);
        projectPage.verifyHint("常用项目设置不能超过9个");
        projectPage.assertCountEquals("项目列表，常用项目数可以超过九个",9, projectFileReader.getActivePinIconInProjectPage() );
    }


    public void moveToItemClickPin(String projectFullName) throws InterruptedException {
        projectPage.backToTop();
        int projectNum = projectPage.findItemInOnePageByTextEquals(projectFileReader.getProjectNameForBlockList(), projectFullName);
        if(projectNum>35) {
            //直接移到图标上会超出窗口所以要先移到它前两排
            projectPage.moveToElement(projectFileReader.getProjectImageForBlockList(),projectNum -18);
            projectPage.moveToElement(projectFileReader.getProjectImageForBlockList(),projectNum);
        }
        projectPage.moveToElement(projectFileReader.getProjectImageForBlockList(), projectNum);
        projectPage.clickElement(projectFileReader.getPinIconInProjectIcon(), projectNum);
    }
    private int findTest5PublicInBlockList( ) throws InterruptedException {
        return projectPage.findItemBySign(zombiePublic.getProjectFullName(),
                projectFileReader.getProjectNameForBlockList(),  projectPage.getItemEqualsFinder());
    }
    @AfterClass
    public static void tearDown() {
        projectPage.getDriver().quit();
    }

}

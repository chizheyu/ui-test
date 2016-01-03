package org.ChiTest.Project;

import dataReader.ConfigureInfo;
import org.ChiTest.Activity.ActivityFileReader;
import org.ChiTest.Activity.ActivityTest;
import org.ChiTest.Activity.AdvancedActivity;
import org.ChiTest.Email.Email;
import org.ChiTest.Email.EmailTest;
import org.ChiTest.InterFace.ItemFinder;
import org.ChiTest.Notification.AdvancedNotificationTest;
import org.ChiTest.Page.PS;
import org.ChiTest.Page.Page;
import org.ChiTest.User.User;
import org.ChiTest.WebComponent.ScreenShot;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import reference.ConfigFileReader;

import java.text.ParseException;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by dugancaii on 8/11/2014.c
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProjectTest {
    static User zombieUser;
    static User ciwangUser;
    static Page ciwangPage;
    static Page zombiePage;
    static Cookie zombieCookie;
    static Cookie ciwangCookie;
    static ProjectFileReader projectFileReader;
    ActivityTest activityTest;
    ActivityFileReader activityFileReader;
    long projectMark=(new Date()).getTime();
    static Project project;
    static Project zombiePrivate;
    static Project zombiePublic;
    static Project ciwangPrivate;
    static Project ciwangPublic;
    static GroupTest groupTest;
    static Page projectPage;
    static ConfigureInfo configureInfo;
    static Logger log = Logger.getLogger("projectTest");
    static  EmailTest emailTest;
    static Set mailCheckList;
    public ProjectTest()  {
        activityTest = new ActivityTest();
        activityFileReader = new ActivityFileReader();
        projectFileReader = new ProjectFileReader();
    }
    @BeforeClass
    public static void setUp() throws Exception {
        configureInfo = new ConfigureInfo(true,true);
        zombiePage = configureInfo.getZombieUser().getPage();
        ciwangPage = configureInfo.getCiwangUser().getPage();
        projectPage = zombiePage;

        zombieUser = new User("coding48","coding48","123456",zombiePage,"Fruit-3.png");
        ciwangUser = new User("dugancai","dugancai","123456",ciwangPage);

        ciwangCookie = ciwangPage.getStagingSid( "ba1d49e5-1f83-4884-8bdd-187fcb2d899b");
        zombieCookie = zombiePage.getStagingSid("0435d6dc-882a-4abb-89ac-28516e40564e");

        zombieUser.autoLogin(configureInfo.getLoginUrl(),zombieCookie );
        ciwangUser.autoLogin(configureInfo.getLoginUrl(), ciwangCookie);

        groupTest = new GroupTest();
        emailTest = new EmailTest(new Email("coding_test48","163.com","chi2014"));
        projectFileReader = new ProjectFileReader();

        mailCheckList = new HashSet();
        project = new Project(ciwangUser,projectFileReader.getPrivateProject() +(new Date()).getTime() ,"this is a test ","private", true, "MIT License", "Java" );
        zombiePublic = new Project(zombieUser,  "public","public");
        zombiePrivate = new Project(zombieUser, zombieUser.getUserLoginName()+ "Private","private");
        ciwangPrivate = new Project(ciwangUser, ciwangUser.getUserLoginName()+"Private","private");
        ciwangPublic = new Project(ciwangUser, "public","public" );
        projectFixture();
    }
    @Rule
    public ScreenShot screenShotZombie = new ScreenShot(zombieUser);
    @Rule
    public ScreenShot screenShotCiwang = new ScreenShot(ciwangUser);

    @Test
    public void test01_alertInCreateProjectFailure() throws InterruptedException, ParseException {
        createAlertTest(zombiePage);
    }
    @Test
    public void test02_createProject() throws InterruptedException, ParseException {
        createProjectTest(project);
        AdvancedActivity createProject = new AdvancedActivity(ciwangUser,zombieUser,project);
        createProject.constructCreateProjectActivity(project.getCreator(), activityFileReader.getProjectIconClass(),activityFileReader.getUserIconClass());
        createProject.checkActivity(ciwangPage, activityFileReader.getOtherActivityTagInProjectHomePage());
    }
    @Test
    public void test03_renameProjectAndDescription() throws InterruptedException, ParseException {
        String reName = "PrivateTest1234";
        Project project1 = new Project(project.getCreator(), reName);
        Page projectPage =project.getCreator().getPage();
        projectPage.navigate(projectPage.getProjectUrl(),projectFileReader.getProjectNameForBlockList());

        project1.deleteProject();
        renameProjectAndDescription(project, project.getProjectName(), reName, "hello " + projectMark);
        verifyProjectName(project, reName);
        renameProjectAndDescription(project,reName, project.getProjectName(), "original description" );
        project.setProjectName(project.getProjectName());

        AdvancedActivity createProject = new AdvancedActivity(ciwangUser,zombieUser,project);
        createProject.constructRenameProjectActivity(project.getCreator(), activityFileReader.getProjectIconClass());
        createProject.checkActivity(ciwangPage, activityFileReader.getOtherActivityTagInProjectHomePage());
    }
    @Test
    public void test04_deleteProject() throws InterruptedException, ParseException {
        Page projectPage =project.getCreator().getPage();
        groupTest.removeAllGroup(projectPage);
        addPin(projectPage, project.getProjectFullName());
        groupTest.addNewGroup(projectPage, project.getProjectFullName());
        project.addProjectMember(zombieUser);
        deleteProjectTest(project);

        AdvancedNotificationTest advancedNotificationTest = new AdvancedNotificationTest(ciwangUser,zombieUser,project);
        advancedNotificationTest.constructprojectDelete(ciwangUser, mailCheckList);
        advancedNotificationTest.checkTaskNotification(zombiePage);
        emailTest.checkEmail(zombiePage, mailCheckList);
    }
    @Test
    public void test08_changeProjectIcon() throws InterruptedException {
        Project test5Public = new Project(zombieUser,"public","public");
        projectPage.navigate(test5Public.getSettingLink(),
                projectFileReader.getProjectEeditedIconImage());
        String projectUrl = projectPage.getAttribute(projectFileReader.getProjectEeditedIconImage(), "src");
        projectPage.sendImgFilePath(projectFileReader.getProjectIconInput(),
                ConfigFileReader.class.getResource("/DAimG_2014110472968258X9BR.jpg").toString());
        assertTrue("上传项目图标不超过1M的限制消失", projectPage.verifyHint("图像大于 1M"));

        projectPage.sendImgFilePath(projectFileReader.getProjectIconInput(),
                ConfigFileReader.class.getResource("/Venn_0110_1000_1000_0000.png").toString());
        projectPage.waitLoadingIconInvisible();
        projectPage.verifyHint("请上传小于 1M 且大小在 90*90 与 800*800 之间的静态图片");
        projectPage.assertAttributeEquals("上传项目图标大小的小在 90*90 与 800*800 之间限制消失", projectUrl, projectFileReader.getProjectEeditedIconImage(), "src");

        projectPage.sendImgFilePath(projectFileReader.getProjectIconInput(),
                ConfigFileReader.class.getResource("/6B80D68F0817475DB84CF4BF6D2CF154.jpg").toString());
        projectPage.waitElementInvisible(".loading.icon",10);
        projectPage.verifyHint("上传成功!");
        projectPage.refresh(projectFileReader.getProjectEeditedIconImage());
        assertNotEquals("新上传的项目图标与原来的图标的url一样", projectUrl, projectPage.getAttribute(projectFileReader.getProjectEeditedIconImage(), "src"));
        assertTrue("新上传的项目图标有问题，可能未上传到骑牛",
                projectPage.getAttribute(projectFileReader.getProjectEeditedIconImage(), "src").
                        contains(projectPage.getProjectImgStoreUrl()));
    }
   @Test
    public void test05_projectOrderInUserCenter() throws InterruptedException, ParseException {
        GroupTest groupTest =new GroupTest();
        groupTest.removeAllGroup(projectPage);
        projectOrderInUserCenter(getZombiePrivateProject(), getCiwangPrivateProject());
    }
    private Project getZombiePrivateProject(){
        return new Project(zombieUser, zombieUser.getUserLoginName()+"Private");
    }
    private Project getCiwangPrivateProject(){
        return new Project(ciwangUser, ciwangUser.getUserLoginName()+"Private");
    }
    @Test
    public void test06_projectCenterAndListFormat() throws InterruptedException {
        if(!projectPage.getDriver().getCurrentUrl().equals(projectPage.getBaseUrl() + "user/projects/all" )) {
            projectPage.navigate(projectPage.getBaseUrl() + "user/projects/all", ".list.icon");
        }
        else {
            projectPage.refresh( ".list.icon");
        }
        projectPage.setPageUrl(projectPage.getBaseUrl() + "user/projects/all");
        System.out.println("开始测试所有项目 ");
        List<User> userList = new LinkedList<User>();

        userList.add(ciwangUser);
        userList.add(zombieUser);
        changeProjectListLayout(projectPage, ".block", projectFileReader.getProjectNameForBlockList());
        projectPage.getWaitTool().waitForElement(By.cssSelector(projectFileReader.getProjectImageForBlockList()), 5);
        assertTrue("项目图标可能消失了",
                projectPage.checkImage(projectFileReader.getProjectImageForBlockList(), "/static/project_icon/scenery") ||
                        projectPage.checkImage(projectFileReader.getProjectImageForBlockList(), "https://dn-coding-net-production-static.qbox.me") );

        clickProjectAndCheckInAllProjectItem(userList, projectPage, projectFileReader.getProjectLinkForBlockListInAllProjectItem(),
                projectFileReader.getProjectNameForBlockList() );

        assertTrue("我的项目模块，项目无法转换成列表结构", projectPage.clickElement(".list.icon", ".block"));
        assertTrue("我的项目模块，项目视图转化为列表结构后的按钮的样式出错", projectPage.elementIsPresent(".block",5));
        assertTrue("我的项目模块，项目视图转化为列表结构后的",projectPage.elementIsPresent(".description",5));
        clickProjectAndCheckInAllProjectItem(userList, projectPage, "#project-list .list .name a", ".name span");
        assertTrue("我的项目模块，项目视图转化为列表结构后的项目添加的按钮失效", projectPage.clickElement(".right.add ", "#project-create-form"));
    }
    @Test
    public void test07_projectItemInProjectCenter() throws InterruptedException {
        Page projectPage = ciwangPage;
        if(!projectPage.getDriver().getCurrentUrl().equals(projectPage.getBaseUrl() + "user/projects/joined" )) {
            projectPage.navigate(projectPage.getBaseUrl() + "user/projects/joined", ".list.icon");
        }
        else {
            projectPage.refresh( ".list.icon");
        }

        changeProjectCenterToTest(projectPage,projectPage.getBaseUrl() + "user/projects/created", 3, "我创建的", ciwangUser);
        projectPage.clickElement(".list ", "#project-list .list .name span");
        assertEquals("在“我的创建”页面，有不属于该用户的项目", projectPage.findItemBySign(ciwangUser.getUserLoginName(), "#project-list .list .name span", getItemChecker()), -1);

        changeProjectCenterToTest(projectPage,projectPage.getBaseUrl() + "user/projects/joined",4,"我参与的",zombieUser);
        changeProjectCenterToTest(projectPage,projectPage.getBaseUrl() + "user/projects/watched",7,"我关注的",zombieUser);
        changeProjectCenterToTest(projectPage,projectPage.getBaseUrl() + "user/projects/stared",8,"我收藏的",zombieUser);

        projectPage.navigate(projectPage.getProjectUrl(), projectFileReader.getWaitEelementInProjectHome());
        changeProjectListLayout(projectPage, ".block", projectFileReader.getProjectNameForBlockList());
    }


    public void verifyProjectName(Project project, String reName) throws InterruptedException {
        Page projectPage = project.getCreator().getPage();
        projectPage.navigate(projectPage.getBaseUrl() + "user/projects/created", projectFileReader.getProjectHeaderForWait());
        changeProjectListLayout(projectPage, ".list", ".name span");
        projectPage.waitElementInvisible(".loading.icon");
        int projectNum = projectPage.findItemBySign(project.getCreator().getUserLoginName()+"/"+ reName, ".name span", projectPage.getItemEqualsFinder());
        assertEquals("项目描述修改后，项目列表模式下的描述，没有修改", projectPage.getText(".description", projectNum),"hello " +projectMark );
        projectPage.clickElement("#project-list .list .name a", projectNum);
        assertNotNull("项目改名后，无法从个人中心的项目列表，直接进入项目",projectPage.getWaitTool().waitForElement(By.cssSelector("#project-home"),5));
    }


    public void changeProjectListLayout(Page projectPage, String layoutSelector, String waitElement){
        if(projectPage.elementIsPresent(layoutSelector, PS.shortWait)){
            projectPage.clickElement(layoutSelector, waitElement);
        }
    }
    public void renameProjectAndDescription(Project project,String originalName,String newName, String newDescription) throws InterruptedException {
        Page projectPage = project.getCreator().getPage();
        projectPage.navigate(projectPage.getBaseUrl()+"user/projects/created", projectFileReader.getProjectHeaderForWait());
        changeProjectListLayout(projectPage, ".block", projectFileReader.getProjectNameForBlockList());
        projectPage.clickElement(projectFileReader.getProjectNameForBlockList(),
                projectPage.findItemBySign(project.getCreator().getUserLoginName()+"/"+originalName,projectFileReader.getProjectNameForBlockList(), projectPage.getItemEqualsFinder()));
        projectPage.getWaitTool().waitForElement(By.cssSelector(projectFileReader.getPrivateProjectTitle()), 10);
        projectPage.clickElement("a[href=\"/u/"+project.getCreator().getUserLoginName()+"/p/"+ originalName +"/setting\"]",
                "a[href*=\"basic\"]");
        projectPage.clickElement("a[href*=\"basic\"]");
        projectPage.clearAndSendKeys(".field input",newName);
        projectPage.clearAndSendKeys(".field textarea",newDescription);
        projectPage.clickElement(".submit ");
        if(!projectPage.verifyHint("更新成功！")){
            System.out.println("项目名更新后没有提示 ");
        }
    }

    public void createAlertTest(Page projectPage)  {
        projectPage.navigate(projectPage.getBaseUrl()+ projectFileReader.getCreateProjectsUrl(),"#project-create-form input[placeholder=\"项目名称\"]");
        createAlertUnitTest(projectPage, "", "项目名不能为空");
        createAlertUnitTest(projectPage, "a", "请输入2 ~ 31位以内的项目名称");
        createAlertUnitTest(projectPage, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "请输入2 ~ 31位以内的项目名称");
        createAlertUnitTest(projectPage, "dfddf分隔符", "项目名只允许字母、数字或者下划线(_)、中划线(-)，必须以字母或者数字开头,且不能以.git结尾");
        createAlertUnitTest(projectPage, "-fgdfg11", "项目名只允许字母、数字或者下划线(_)、中划线(-)，必须以字母或者数字开头,且不能以.git结尾");
        createAlertUnitTest(projectPage, "_fgdfg11", "项目名只允许字母、数字或者下划线(_)、中划线(-)，必须以字母或者数字开头,且不能以.git结尾");
        createAlertUnitTest(projectPage, "+aaaaa11", "项目名只允许字母、数字或者下划线(_)、中划线(-)，必须以字母或者数字开头,且不能以.git结尾");
        createAlertUnitTest(projectPage, "tt.git", "项目名只允许字母、数字或者下划线(_)、中划线(-)，必须以字母或者数字开头,且不能以.git结尾");
        createAlertUnitTest(projectPage, "public", "项目名已经存在");

    }
    public void createAlertUnitTest(Page projectPage, String projectName, String verifyMessage){
        projectPage.clearInput(projectFileReader.getInputProjectName());
        projectPage.sendKeys(projectFileReader.getInputProjectName(), projectName);
        projectPage.clickElement(projectFileReader.getCreateProjectButton());
        projectPage.waitElementDisappear(".loading");
        assertTrue("输入项目名为" + projectName + "时未出现提示",projectPage.verifyHint(verifyMessage));
    }
    public void projectOrderInUserCenter(Project zombieProject, Project ciwangProject) throws InterruptedException, ParseException {
        int ciwangProjectNum;
        int zombieProjectNum;
        Page projectPage = zombieProject.getCreator().getPage();
        if(!projectPage.getDriver().getCurrentUrl().equals(projectPage.getBaseUrl()+"user" )) {
            projectPage.navigate(projectPage.getBaseUrl()+"user" , projectFileReader.getLookAllUserProjectInUserCenter());
        }
        else {
            projectPage.refresh(projectFileReader.getLookAllUserProjectInUserCenter());
        }

        assertEquals("个人首页的“查看所有”链接有误", projectPage.getBaseUrl() + "user/projects", projectPage.getLink(projectFileReader.getLookAllUserProjectInUserCenter()));
        clearActivityUnreadNum(projectPage);

        clickProjectInProjectHome(projectPage, ciwangProject);
        assertNotNull("私有项目导航栏的私有项目图标（锁）可能消失了", projectPage.getElement("#project_title_dropdown .lock"));
        clickProjectInProjectHome(projectPage, zombieProject);
        projectPage.refresh(projectPage.getUserCenterUrl(),projectFileReader.getLookAllUserProjectInUserCenter());
        zombieProjectNum = projectPage.findItemInOnePageByTextEquals(projectFileReader.getProjectNameInBlockList(),zombieProject.getProjectFullName());
        ciwangProjectNum = projectPage.findItemInOnePageByTextEquals( projectFileReader.getProjectNameInBlockList(),ciwangProject.getProjectFullName());
        assertTrue("点击个人中心的项目后，回到个人中心，该项目没有排到最前" + projectPage.getUrl() + " zombieNum:"+ zombieProjectNum + " ciwangNum: "+ ciwangProjectNum,
                zombieProjectNum < ciwangProjectNum);

        clickProjectInProjectHome(projectPage, ciwangProject);
        projectPage.refresh(projectPage.getUserCenterUrl(),projectFileReader.getLookAllUserProjectInUserCenter());
        zombieProjectNum = projectPage.findItemInOnePageByTextEquals( projectFileReader.getProjectNameInBlockList(),zombieProject.getProjectFullName());
        ciwangProjectNum = projectPage.findItemInOnePageByTextEquals( projectFileReader.getProjectNameInBlockList(),ciwangProject.getProjectFullName());
        assertTrue("点击个人中心的项目后，回到个人中心，该项目没有排到最前" + projectPage.getUrl() + " zombieNum:"+ zombieProjectNum + " ciwangNum: "+ ciwangProjectNum,
                zombieProjectNum > ciwangProjectNum );
    }

    public void clearActivityUnreadNum(Page projectPage) throws InterruptedException {
        projectPage.navigate(projectPage.getBaseUrl()+"user", projectFileReader.getProjectItemForBlockList());
        while (projectPage.elementIsPresent(projectFileReader.getUnreadActivityCountOnProjectIcon(),2)){
            projectPage.clickElement(projectFileReader.getProjectItemForBlockList(), projectFileReader.getPrivateProjectTitle());
            projectPage.refresh(projectPage.getBaseUrl()+projectFileReader.getUserCenterUrl(),projectFileReader.getProjectItemForBlockList());
        }
    }
    public void clickProjectInProjectHome(Page page, Project project) throws InterruptedException {
        int num =  project.findProjectInAllProjectPage(page);
        page.clickElement(projectFileReader.getProjectLinkForBlockListInAllProjectItem(),
                num,projectFileReader.getProjectActivity());

    }
    public void changeProjectCenterToTest(Page projectPage,String pageUrl, int num,String pageTheme,User user) throws InterruptedException {
        projectPage.clickElement("#inner-menu .item:nth-child("+num +")");
        assertTrue("点击项目中心侧栏的“" + pageTheme + "”没有跳转到相应的网页",
                projectPage.getWaitTool().waitForJavaScriptCondition("return $(\".container .header\").text().indexOf(\"" + pageTheme + "\") != -1",10));
        projectPage.setPageUrl(pageUrl);
        projectPage.waitElementInvisible(".loading.icon");
        assertTrue(pageTheme + "的分页消失", projectPage.elementIsPresent(".page.active", 3));
        changeProjectListLayout(projectPage, ".block", projectFileReader.getProjectNameForBlockList());

        if(projectPage.getUrl().contains("created")) {
            assertTrue("项目页面私有项目的“锁图标”消失了 url " + projectPage.getUrl(), projectPage.elementIsPresent(projectFileReader.getProjectItemForBlockListInAllProjectPage(),
                    projectPage.findItemBySign(user.getUserLoginName() + "/" + user.getUserLoginName()+"Private",  projectFileReader.getProjectNameForBlockList(), projectPage.getItemEqualsFinder()), ".icon"));

            assertEquals(user.getUserLoginName() + "Private" + "项目图标上的链接失效",
                    projectPage.getBaseUrl() + "u/" + user.getUserLoginName() + "/p/" + user.getUserLoginName() + "Private", projectPage.getLink(projectFileReader.getProjectLinkForBlockListInAllProjectItem(),
                            findProjectInAllProjectPage(projectPage, user.getUserLoginName() + "/" + user.getUserLoginName() + "Private", projectFileReader.getProjectNameForBlockList())));

        }else {
            assertEquals(user.getUserLoginName() + "/public" + "项目图标上的链接失效",
                    projectPage.getBaseUrl() + "u/" + user.getUserLoginName() + "/p/public", projectPage.getLink(projectFileReader.getProjectLinkForBlockListInAllProjectItem(),
                            findProjectInAllProjectPage(projectPage, user.getUserLoginName() + "/public", projectFileReader.getProjectNameForBlockList())));
        }
    }
    private int findProjectInAllProjectPage(Page projectPage, String projectFullName,String projectNameSelector) throws InterruptedException {
        int projectNum;

        projectNum = projectPage.findItemBySign(projectFullName,  projectNameSelector, projectPage.getItemEqualsFinder());
        if(projectNum==-1) {
            assertEquals("项目"+ projectFullName+" 没找到" ,1,2);
        }
        return projectNum;
    }
    public void clickProjectAndCheckInAllProjectItem(List<User> userList, Page projectPage, String projectClickSelector, String projectNameSelector) throws InterruptedException {
        for(User user : userList) {
            assertEquals( user.getUserLoginName()+"Private" + "项目图标上的链接失效",
                    projectPage.getBaseUrl() + "u/" + user.getUserLoginName() + "/p/" + user.getUserLoginName() + "Private", projectPage.getLink(projectClickSelector,
                            findProjectInAllProjectPage(projectPage, user.getUserLoginName() + "/" + user.getUserLoginName() + "Private", projectNameSelector)) );

            assertEquals( user.getUserLoginName()+"/public" + "项目图标上的链接失效",
                    projectPage.getBaseUrl() + "u/" + user.getUserLoginName() + "/p/public", projectPage.getLink(projectClickSelector,
                            findProjectInAllProjectPage(projectPage, user.getUserLoginName() + "/public", projectNameSelector)) );
        }
    }

    public ItemFinder getItemChecker(){
        return new ItemFinder(){
            @Override
            public int findItem(List<WebElement> follows, int j, String signContent) {
                if (!follows.get(j).getText().contains(signContent)) {
                    return j;
                }
                return -2;
            }
        };
    }



    @Test
    public void test09_changePrivateProjectInTitle(){
        projectPage.navigate(zombiePrivate.getProjectLink(), projectFileReader.getPrivateProjectTitle());
        String titleText = projectPage.getText(projectFileReader.getPrivateProjectTitle());
        projectPage.moveToElement(projectFileReader.getPrivateProjectTitle());
        String newTitleText = projectPage.getText( projectFileReader.getPrivateProjectTitleList());
        projectPage.clickElement(projectFileReader.getPrivateProjectTitleList());
        projectPage.waitForContentChange(projectFileReader.getPrivateProjectTitle(),titleText,10);
        projectPage.waitForContentChange(projectFileReader.getPrivateProjectTitle(),"",10);
        assertEquals("无法再私有项目主页的标题一栏切换项目", projectPage.getText(projectFileReader.getPrivateProjectTitle()),newTitleText);
    }


    public void addPin(Page projectPage, String projectFullName) throws InterruptedException {
        (new PinTest()).deleteAllPin(projectPage);
        projectPage.refresh(projectPage.getProjectUrl(), projectFileReader.getWaitEelementInProjectHome());
        if(pinIsPresent(projectPage,projectFullName)){
            System.out.println("pin is added!!");
            return;
        }
        if(findProjectInBlockList(projectPage,projectFullName)>36){
            projectPage.getBuilder().moveToElement(projectPage.getElement(projectFileReader.getProjectImageForBlockList(),findProjectInBlockList(projectPage, projectFullName))).perform();
            projectPage.getBuilder().moveToElement(projectPage.getElement(projectFileReader.getProjectImageForBlockList(), findProjectInBlockList(projectPage,projectFullName)-9)).perform();
        }else {
            projectPage.backToTop();
        }
        projectPage.waitForElement(projectFileReader.getProjectImageForBlockList(),5);
        projectPage.moveToElement(projectFileReader.getProjectImageForBlockList(), findProjectInBlockList(projectPage, projectFullName));
        projectPage.clickElement(projectFileReader.getPinIconInProjectIcon(),findProjectInBlockList(projectPage,projectFullName));
        assertTrue("固定项目失败，项目图标上的针没有固定",
                projectPage.elementIsPresent(projectFileReader.getActivePinIconInProjectPage(),10));
    }
    private boolean pinIsPresent(Page projectPage, String projectFullName) throws InterruptedException {
        try {
            return projectPage.getElement(projectFileReader.getProjectItemInAllProjectPage(), findProjectInBlockList(projectPage, projectFullName)).
                    findElement(By.cssSelector(projectFileReader.getActivePinIconInProjectPage())) != null;
        }catch (Exception e){
            return false;
        }
    }

    public int findProjectInBlockList(Page projectPage,String projectFullName ) throws InterruptedException {
        projectPage.navigate(projectPage.getProjectUrl());
        return projectPage.findItemBySign(projectFullName,
                projectFileReader.getProjectNameForBlockList(), projectPage.getItemEqualsFinder());
    }
    public int findPrivateProjectInPinList(Page projectPage, String projectName ) throws InterruptedException {
        projectPage.navigate(projectPage.getUserCenterUrl(), projectFileReader.getDropIconInTopMenu());
        projectPage.getBuilder().moveToElement(projectPage.getElement(projectFileReader.getDropIconInTopMenu())).perform();
        return projectPage.findItemBySign(projectName,
                projectFileReader.getPrivateProjectNameInPinList(), projectPage.getItemEqualsFinder());
    }

    private static void projectFixture() throws InterruptedException, ParseException {
        zombiePublic.createFixtureProject();
        zombiePrivate.createFixtureProject();
        ciwangPublic.createFixtureProject();
        ciwangPrivate.createFixtureProject();
        zombiePublic.addProjectMember(ciwangUser);
        zombiePrivate.addProjectMember(ciwangUser);
        ciwangPublic.addProjectMember(zombieUser);
        ciwangPrivate.addProjectMember(zombieUser);

        PublicProjectTest publicProjectTest = new PublicProjectTest();
        publicProjectTest.watchProject(ciwangPage, zombiePublic);
        publicProjectTest.stareProject(ciwangPage, zombiePublic);
    }

    public void createProjectTest(Project project) throws InterruptedException {
        Page projectPage = project.getCreator().getPage();
        if(!projectPage.getDriver().getCurrentUrl().equals(projectPage.getBaseUrl() + projectFileReader.getUserCenterUrl() )) {
            projectPage.navigate(projectPage.getBaseUrl() +projectFileReader.getUserCenterUrl(), projectFileReader.getProjectActivity());
        }
        else {
            projectPage.refresh(projectFileReader.getProjectActivity());
        }
        assertEquals("个人首页的“+创建项目”链接有误", projectPage.getBaseUrl() + projectFileReader.getCreateProjectsUrl(),
                projectPage.getLink(projectFileReader.getCreateProjectsInUserCenter()));
        assertTrue("导航栏的“加号”失效", projectPage.clickElement(projectFileReader.getAddSignInNavigationBar(), projectFileReader.getCreateProjectInNavigationBar()));
        assertEquals("导航栏的“创建项目”链接有误", projectPage.getBaseUrl() + projectFileReader.getCreateProjectsUrl(),
                projectPage.getAttribute(projectFileReader.getCreateProjectInNavigationBar(), "href"));
        projectPage.clickElement(projectFileReader.getProjectButtonInContextMenu(), projectFileReader.getCreateProjectInProjectList());
        projectPage.waitLoadingIconInvisible();
        assertEquals("项目图标内“创建项目”链接有误", projectPage.getBaseUrl() + projectFileReader.getCreateProjectsUrl(),
                projectPage.getAttribute(projectFileReader.getCreateProjectInProjectList(), "href"));
        projectPage.clickElement(projectFileReader.getAddIconForCreateProjectInProjectPage(),projectFileReader.getWaitEelementInCreateProjectPage());

        projectPage.sendKeys(projectFileReader.getInputProjectName(), project.getProjectName());
        projectPage.sendKeys(projectFileReader.getInputProjectDescription(),project.getDescription());
        if(project.getType().equals("public")){
            projectPage.clickElement(projectFileReader.getPublicProjectType());
        }
        if(project.getType().equals("private")) {
            //projectPage.getDriver().executeScript("$(\""+ projectFileReader.getPrivateProjectType()+"\").click()");
            projectPage.clickElement(projectFileReader.getPrivateProjectType());
        }
        if(project.isHaveReadme())   {
            projectPage.getDriver().executeScript("$(\""+projectFileReader.getHasReadMe() +"\").click()");
        }
        if(!project.getLicence().equals("")||project.getLicence()!=null){
            projectPage.getDriver().executeScript("$(\""+projectFileReader.getSelectLicence() +"\").click()");
            projectPage.clickElement(projectFileReader.getOption()+"[value='"+project.getLicence() +"']");
        }
        if(!project.getGitignore().equals("")||project.getGitignore()!=null){
            projectPage.getDriver().executeScript("$(\""+projectFileReader.getSelectGitIgnore() +"\").click()");
            projectPage.clickElement(projectFileReader.getOption()+"[value='" + project.getGitignore()+"']");
        }
        projectPage.clickElement(projectFileReader.getCreateProjectButton());
        if(!projectPage.elementIsPresent(".loading",5)) {
            log.error("创建项目时loading图标消失");
        }
        project.setCreateTime(new Date());

        projectPage.getWaitTool().waitForJavaScriptCondition("return $('"+ projectFileReader.getPublicProjectTitle()+"').length != 0 || $('"+ projectFileReader.getPrivateProjectTitle()+"').length != 0 ",10);
        assertTrue("创建项目失败，创建后没有跳转到新项目首页",projectPage.elementIsPresent(projectFileReader.getPrivateProjectTitle(),5));
    }

    public void deleteProjectTest( Project project) throws InterruptedException {
        Page projectPage = project.getCreator().getPage();
        projectPage.navigate(project.getProjectLink(),
                "a[href='/u/" + project.getCreator().getUserLoginName() + "/p/" + project.getProjectName() + "/setting']");
        projectPage.clickElement("a[href='/u/" + project.getCreator().getUserLoginName() + "/p/" + project.getProjectName() + "/setting']");
        projectPage.clickElement("a[href='/u/" + project.getCreator().getUserLoginName() + "/p/" + project.getProjectName() + "/setting/advance']");
        projectPage.clickElement("#project-setting-content .button.red.default","input[placeholder='请输入密码']");
        projectPage.sendKeys("input[placeholder='请输入密码']", project.getCreator().getUserPassword());
        projectPage.clickElement("#password-validate-modal .red.button");
        projectPage.getWaitTool().waitForElement(By.cssSelector(projectFileReader.getWaitEelementInProjectHome()),180);
        assertTrue("用户还能进入删除的项目",
                projectPage.navigate(projectPage.getBaseUrl()+"u/"+project.getCreator().getUserLoginName()+"/p/"+project.getProjectName(), projectFileReader.getProjectActivity()) );
        assertEquals("用户删除项目后，依然能在常用项目中发现该项目",-1,findPrivateProjectInPinList(projectPage,project.getProjectName()));
        projectPage.navigate(projectPage.getBaseUrl()+"user/projects",projectFileReader.getProjectItemInAllProjectPage());
        assertFalse("用户删除项目后，分组未消失", projectPage.elementIsPresent(projectFileReader.getProjectGroupItem(), 2));

    }
    @AfterClass
    public static void tearDown(){
         zombiePage.getDriver().quit();
         ciwangPage.getDriver().quit();
    }
}

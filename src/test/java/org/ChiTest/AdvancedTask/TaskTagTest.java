package org.ChiTest.AdvancedTask;

import dataReader.ConfigureInfo;
import org.ChiTest.Page.Page;
import org.ChiTest.Project.Project;
import org.ChiTest.User.User;
import org.ChiTest.WebComponent.Tag.Tag;
import org.ChiTest.WebComponent.Tag.TagFileReader;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Cookie;

/**
 * Created by dugancaii on 4/23/2015.
 */
public class TaskTagTest {
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
    private static Tag bug;
    private static Tag function;
    private static Tag investigate;
    private static TagFileReader tagFileReader;


    @BeforeClass
    public static void  setUp() throws Exception {
        ConfigureInfo ConfigureInfo = new ConfigureInfo(true, true);

        zp = ConfigureInfo.getZombieUser().getPage();
        cp =  ConfigureInfo.getCiwangUser().getPage();
        zombieUser = new User("wangyiA163_-31","wangyiTask","123456",zp,"Fruit-3.png");
        ciwangUser = new User("test6","testZp_task","123456",cp);

        ciwangCookie = cp.getStagingSid("83283140-8148-4bae-8d7b-03f03f4e2fe0");
        zombieCookie = zp.getStagingSid("a934e2e7-5fcc-4908-9bf1-85f770375793");
        zombieUser.autoLogin(ConfigureInfo.getLoginUrl(), zombieCookie);
        ciwangUser.autoLogin(ConfigureInfo.getLoginUrl(), ciwangCookie);

        advancedTaskFileReader = new AdvancedTaskFileReader();

        //zombieProject = new Project(zombieUser, "TaskPrivate", "private");
        zombieProject = new Project(zombieUser, "Task"+cp.getMark(), "private");
        zombieProject.deleteAllProject();
        zombieProject.createFixtureProject();
        taskOpreator = new AdvancedTask();

        bug = new Tag("Bug", "(217,92,92)", zombieUser, zombieProject);
        function = new Tag("功能", "(91, 189, 114)", zombieUser, zombieProject);
        investigate = new Tag("调研", "(242, 198, 31)", zombieUser, zombieProject);
        tagFileReader = new TagFileReader();

        zombieProject.addProjectMember( ciwangUser);
    }

    @Test
    public void test13_0_taskDefaultTag(){
        zp.navigate(zombieProject.getProjectTaskLink(), advancedTaskFileReader.getTagClassifyHeader());
        zp.assertIconAndText("任务列表的'所有标签' ", advancedTaskFileReader.getTagClassifyHeader(), "所有标签 (3)", "tags");
        bug.checkTopicTag(zp,tagFileReader.getTagItemInTagList(), 0 );
        function.checkTopicTag(zp,tagFileReader.getTagItemInTagList(),1);
        investigate.checkTopicTag(zp, tagFileReader.getTagItemInTagList(),2);
    }
    @Test
    public void test13_1_addAndDeleteTaskTagAndFilter(){
        //选择框搜不到后，添加新标签
        zp.navigate(zombieProject.getProjectTaskLink());
        zp.waitLoadingIconInvisible();
        taskOpreator.sendSimpleTask(zp, zombieUser.getUserName(), "task tag 1");
        taskOpreator.sendSimpleTask(zp,zombieUser.getUserName(), "task tag 2");

    }
    //添加，编辑，删除线标签
    @Test
    public void test13_2_addNewTag(){
        //添加标签后，看看能不能添加
    }
    @Test
    public void test13_3_editAndDeleteTag(){
        //添加标签后，看看能不能添加
        //选择框搜不到后，添加新标签
    }
    @AfterClass
    public static void tearDown() throws Exception {
        zombieUser.getPage().getDriver().quit();
        ciwangUser.getPage().getDriver().quit();
    }

}

package org.ChiTest.Code;

import org.ChiTest.Activity.ActivityFileReader;
import dataReader.ConfigureInfo;
import org.ChiTest.MemberTest;
import org.ChiTest.Page.Page;
import org.ChiTest.Project.Project;
import org.ChiTest.Project.ProjectFileReader;
import org.ChiTest.Project.ProjectOwnerFileReader;
import org.ChiTest.Project.ProjectTest;
import org.ChiTest.Topic.TopicFileReader;
import org.ChiTest.User.User;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Cookie;

import java.util.Date;

/**
 * Created by dugancaii on 1/13/2015.
 */
public class CodeTest {
    static String loginUrl;
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
    static Project project;
    static MemberTest memberTest;
    static ProjectTest projectTest;
    static ConfigureInfo configureInfo;
    static Project zombiePrivateProject;
    static Logger log = Logger.getLogger("documentTest");
    @BeforeClass
    public static void setUp() throws Exception {
        configureInfo = new ConfigureInfo(true,true);
        zombieUser = configureInfo.getZombieUser();
        memberTest = new MemberTest();
        projectOwnerFileReader = new ProjectOwnerFileReader();
        activityFileReader = new ActivityFileReader();
        codeFileReader = new CodeFileReader();
        topicFileReader = new TopicFileReader();
        projectFileReader = new ProjectFileReader();
        zombieCookie = configureInfo.getZombieCookie();
        zombieUser.autoLogin(configureInfo.getLoginUrl(),zombieCookie );

        ciwangUser = configureInfo.getCiwangUser();
        ciwangPage = ciwangUser.getPage();
        zombiePage = zombieUser.getPage();
        ciwangCookie = configureInfo.getCiwangCookie();
        ciwangUser.autoLogin(configureInfo.getLoginUrl(), ciwangCookie);
        project = new Project(ciwangUser,projectFileReader.getPrivateProject() +(new Date()).getTime() ,"this is a test ","private");
        zombiePrivateProject = new Project(zombieUser,"test5Private");


    }
    @Test
    public void test01_projectWithoutGit(){
        zombiePage.navigate(zombiePrivateProject.getProjectCodeLink(), ".quick-setup" );
        verifyCodePageDefaultInfo();

        verifyEveryEmptyPage(codeFileReader.getInnerMenuBranchButton(), "暂无分支",  ".ui.coding.aligned.center");
        verifyEveryEmptyPage(codeFileReader.getInnerMenuTagButton(), "本项目还没有标签。",  ".create-tag-tips strong");
        verifyEveryEmptyPage(codeFileReader.getInnerMenuMRButton(), "暂无未处理的 Merge Request",  ".ui.coding.aligned.center");

        zombiePage.clickElement(codeFileReader.getInnerMenuNetworkButton());
        zombiePage.verifyHint("git仓库没有提交");
        zombiePage.clickElement(codeFileReader.getInnerMenuStatisticsButton());
        zombiePage.verifyHint("git仓库没有提交");
        zombiePage.clickElement(codeFileReader.getInnerMenuReadButton());
        zombiePage.verifyHint("git仓库没有提交");

    }
    public void verifyCodePageDefaultInfo(){
        zombiePage.assertTextEquals("代码首页的标题", "Git 仓库", codeFileReader.getCodeHeaderInInnerMenu());
        zombiePage.assertIconAndTextAndLink("项目左侧导航栏的代码按钮", "代码", "big.code",
                zombiePrivateProject.getProjectCodeLink(), codeFileReader.getCodeButtonInContextMenu());
        zombiePage.assertIconAndText("代码首页的侧边栏上的代码按钮", codeFileReader.getInnerMenuCodeButton(), "代码", "code");
        zombiePage.assertIconAndText("代码首页的侧边栏上的分支按钮",
                codeFileReader.getInnerMenuBranchButton(), "分支", "fork.code");
        zombiePage.assertIconAndText("代码首页的侧边栏上的标签按钮", codeFileReader.getInnerMenuTagButton(), "标签", "tags");
        zombiePage.assertIconAndText("代码首页的侧边栏上的合并请求按钮", codeFileReader.getInnerMenuMRButton(), "合并请求", "merge-request");
        zombiePage.assertIconAndText("代码首页的侧边栏上的项目网络按钮", codeFileReader.getInnerMenuNetworkButton(), "项目网络", "globe");
        zombiePage.assertIconAndText("代码首页的侧边栏上的项目统计按钮", codeFileReader.getInnerMenuStatisticsButton(), "项目统计", "chart");
        zombiePage.assertIconAndText("代码首页的侧边栏上的代码阅读按钮", codeFileReader.getInnerMenuReadButton(), "代码阅读", "book");
        zombiePage.assertElementPresent("当前项目没有代码的提示", ".quick-setup");
        zombiePage.assertLinkAndTextEquals("项目代码没有的提示", "不懂如何上传代码到 coding？ 请点击我 >",
                zombiePage.getBaseUrl() + "help/about_git", ".quick-setup .coding.center a");
        zombiePage.assertElementPresent("当前项目没有代码时，创建仓库的命令提示", ".git-empty .new-repo-cli-container ");
        zombiePage.assertElementPresent("当前项目没有代码时，创建仓库的 readme 提示", ".empty-repo-recommend ");
    }
    private void verifyEveryEmptyPage(String selectorForInnerButton, String hint, String hitSelector ){
        zombiePage.clickElement(selectorForInnerButton, codeFileReader.getAddNewButton());
        zombiePage.assertTextEquals(hint + "提示消失", hint,hitSelector);
    }
    @Test
    public void test02_gitUrl(){
        //查看https和ssh的方式是否正确
        //copy 链接粘到 搜索框里看看是否正确


    }
    //传建一个文件夹 first


    public static void fixtureProject(){

    }




}

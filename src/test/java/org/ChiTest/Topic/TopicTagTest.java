package org.ChiTest.Topic;

import dataReader.ConfigureInfo;
import org.ChiTest.Activity.ActivityFileReader;
import org.ChiTest.Activity.AdvancedActivity;
import org.ChiTest.Friend.RelationShip;
import org.ChiTest.Page.PS;
import org.ChiTest.Page.Page;
import org.ChiTest.Project.Project;
import org.ChiTest.Project.PublicProjectTest;
import org.ChiTest.User.User;
import org.ChiTest.WebComponent.ScreenShot;
import org.ChiTest.WebComponent.SearchBox.TagSearchBox;
import org.ChiTest.WebComponent.Tag.Tag;
import org.ChiTest.WebComponent.Tag.TagFileReader;
import org.ChiTest.WebComponent.Tag.TopicTag;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Keys;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by dugancaii on 1/22/2015.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TopicTagTest {
    protected  static User zombieUser;
    protected  static User ciwangUser;
    private static Page ciwangPage;
    private static Page zombiePage;
    private static Cookie zombieCookie;
    private static Cookie ciwangCookie;
    private static TopicFileReader topicFileReader;
    private static TagFileReader tagFileReader;
    private static ActivityFileReader activityFileReader;
    private static Project ciwangPublic;
    private  static ConfigureInfo configureInfo;
    public static String topicTitle  = "<script>topic</script> https://www.baidu.com 嘿嘿";
    private static String stringForTagTest;
    private static String errorTagHint;
    private static Logger log = Logger.getLogger("topicTagTest");
    private static Topic topic1;
    private static Topic topic2;
    private static TopicTag newTag;
    private static TopicTag bugTag;
    private static TopicTag featureTag;
    private static TagSearchBox tagSearchBox;
    public TopicTagTest()  {
        topicFileReader = new TopicFileReader();
        tagFileReader = new TagFileReader();
    }
    @BeforeClass
    public static void setUp() throws Exception {
        topicFileReader = new TopicFileReader();
        configureInfo = new ConfigureInfo(true, true);
        zombiePage = configureInfo.getZombieUser().getPage();
        ciwangPage = configureInfo.getCiwangUser().getPage();

        zombieUser = new User("coding48","coding48","123456",zombiePage,"Fruit-3.png");
        ciwangUser = new User("dugancai","dugancai","123456",ciwangPage);

        ciwangCookie = ciwangPage.getStagingSid("ba1d49e5-1f83-4884-8bdd-187fcb2d899b");
        zombieCookie = zombiePage.getStagingSid("0435d6dc-882a-4abb-89ac-28516e40564e");

        zombieUser.autoLogin(configureInfo.getLoginUrl(),zombieCookie );
        ciwangUser.autoLogin(configureInfo.getLoginUrl(), ciwangCookie);

        //ciwangPublic = new Project(ciwangUser,"public","public");
        ciwangPublic = new Project(ciwangUser,"public"+ ciwangPage.getMark(),"public");
        ciwangPublic.createFixtureProject();
        stringForTagTest = "newTag很好123456789012345678901234567";
        activityFileReader = new ActivityFileReader();
        tagFileReader = new TagFileReader();
        topic1 = new Topic(ciwangPublic,"tag test", "the content of tag test",zombieUser);
        topic2 = new Topic(ciwangPublic,"tag test2", "the content of tag test",zombieUser);
        ciwangPublic.addProjectMember( zombieUser);
        zombiePage.navigate(ciwangPublic.getProjectTopicHomeLink(), topicFileReader.getAddTopic());
        ciwangPage.navigate(ciwangPublic.getProjectTopicHomeLink(), tagFileReader.getTagSettingIcon());
        topic1.deleteAllTopic();
        topic1.createTopic();
        topic2.createTopic();
        errorTagHint = "标签不能为空且不能超过36个字符,且只能为字母数字汉字" ;
        newTag = new TopicTag(stringForTagTest,"(178, 233, 230)","#b2e9e6", zombieUser, ciwangPublic );
        bugTag = new TopicTag("Bug","(217, 92, 92)","#d95c5c", zombieUser, ciwangPublic );
        featureTag = new TopicTag("功能","(91, 189, 114)", "#5bbd72",zombieUser, ciwangPublic );
        tagSearchBox = new TagSearchBox();
    }
    @Rule
    public ScreenShot screenShotZombie = new ScreenShot(zombieUser);
    @Rule
    public ScreenShot screenShotCiwang = new ScreenShot(ciwangUser);
    @Test
    public void test01_0_checkDefaultTag(){
        zombiePage.navigate(ciwangPublic.getProjectTopicHomeLink(), tagFileReader.getTagItemInTagList());
        checkDefaultTag(zombiePage);
        zombiePage.clickElement(tagFileReader.getTagSettingIcon(), tagFileReader.getTagTextInput());
        checkTagCanEdit(zombiePage, 0);
    }
    @Test
    public void test01_1_addTagAlert(){
        openTagEditInterFace(zombiePage);
        zombiePage.clearAndSendKeys(tagFileReader.getNewTagTextInput(), newTag.getTagName() + "dddd");
        zombiePage.sendKeys(Keys.ENTER);
        zombiePage.verifyHint(errorTagHint);
        int tagRemoveTagCount = zombiePage.getElementCount(tagFileReader.getTagRemoveIcon());
        zombiePage.clearAndSendKeys(tagFileReader.getNewTagTextInput(), "@ddddd");
        zombiePage.sendKeys(Keys.ENTER);
        zombiePage.waitForItemCountChange(tagFileReader.getTagRemoveIcon(),tagRemoveTagCount,5);
        zombiePage.assertCountEquals("添加新标签失败", tagRemoveTagCount + 1, tagFileReader.getTagRemoveIcon());
    }
    @Test
    public void test01_2_addRepeatTag(){
        openTagEditInterFace(zombiePage);
        zombiePage.clearAndSendKeys(tagFileReader.getNewTagTextInput(), "Bug");
        zombiePage.sendKeys(Keys.ENTER);
        zombiePage.verifyHint("存在同名的标签");
    }
    @Test
    public void test01_3_addTopicTagInProject() throws ParseException, InterruptedException {
        openTagEditInterFace(zombiePage);
        zombiePage.clickElement(tagFileReader.getNewTagColorEditButton(),tagFileReader.getNewTagColorInput());
        String colorText = zombiePage.getAttribute(tagFileReader.getNewTagColorInput(), "value");
        zombiePage.clickElement(topicFileReader.getTopicNewTagPredefinedColor());
        zombiePage.waitForAttributeChange(tagFileReader.getNewTagColorInput(), "value", colorText, 0,5);
        zombiePage.assertAttributeContainWords("选择预先选定的黑色标签色之后，标签颜色输入框", "#1b1c1d", tagFileReader.getNewTagColorInput(), "value");
        zombiePage.assertAttributeContainWords("选择预先选定的黑色标签色之后，标签颜色框", "rgb(27, 28, 29)", tagFileReader.getNewTagColorEditButton(), "style");

        zombiePage.clearAndSendKeys(tagFileReader.getNewTagColorInput(), "#b2e9e6");
        zombiePage.assertAttributeContainWords("选择预先选定的黑色标签色之后，标签颜色框", "rgb(178, 233, 230)", tagFileReader.getNewTagColorEditButton(), "style");

        int tagRemoveTagCount = zombiePage.getElementCount(tagFileReader.getTagRemoveIcon());
        zombiePage.clearAndSendKeys(tagFileReader.getNewTagTextInput(),newTag.getTagName() );
        zombiePage.sendKeys(Keys.ENTER);
        zombiePage.waitForItemCountChange(tagFileReader.getTagRemoveIcon(),tagRemoveTagCount,5);
        zombiePage.assertCountEquals("添加新标签失败", tagRemoveTagCount + 1, tagFileReader.getTagRemoveIcon());

        zombiePage.clickElement(tagFileReader.getTagEditConfirmIcon());
        newTag.checkTopicTag(zombiePage,tagFileReader.getTagItemInTagList(),4);
    }
    @Test
    public void test01_5_editTopicTagInProject(){
        openTagEditInterFace(zombiePage);
        TopicTag editTag = new TopicTag("tagForEdit", "(164, 211, 55)","#A4D337",zombieUser,ciwangPublic);
        editTag.addTagInProject(zombiePage);
        editTag.addTagInTopicDetail(zombiePage, topic1,topicFileReader.getItemHomeHeader());
        editTag.editTag(zombiePage, "tagHasEdit", "(244, 206, 206)", "#f4cece");
        zombiePage.assertTextEquals("编辑标签后，相应的讨论上的标签没有变掉", editTag.getTagName(),
                tagFileReader.getTagInItemDetail(),
                zombiePage.findItemInOnePageByAttributeContains(tagFileReader.getTagInItemDetail(),"style",editTag.getTagColorRBG()));

        zombiePage.navigate(ciwangPublic.getProjectTopicHomeLink(), tagFileReader.getTagInItemTitle());
        zombiePage.assertTextEquals("编辑标签后，相应的讨论上的标签没有变掉", editTag.getTagName(),
                tagFileReader.getTagInItemTitle());
    }
    @Test
    public void test01_4_removeTopicTagInProject(){
        openTagEditInterFace(zombiePage);
        TopicTag removeTag = new TopicTag("tagForRemove", "(164,211,55)","#A4D337" ,zombieUser,ciwangPublic);
        removeTag.addTagInProject(zombiePage);
        removeTag.addTagInTopicDetail(zombiePage, topic1, topicFileReader.getItemHomeHeader());

        openTagEditInterFace(zombiePage);
        int tagRemoveTagCount = zombiePage.getElementCount(tagFileReader.getTagRemoveIcon());
        zombiePage.checkAlert(tagFileReader.getTagRemoveIcon(),tagRemoveTagCount - 1,
                "删除讨论标签","确认删除标签 tagForRemove ?");
        zombiePage.waitForItemCountChange(tagFileReader.getTagRemoveIcon(),tagRemoveTagCount,5);
        zombiePage.assertCountEquals("删除新标签失败",tagRemoveTagCount - 1, tagFileReader.getTagRemoveIcon());
        zombiePage.refresh(topicFileReader.getItemInItemHome());
        zombiePage.assertElementNotPresent("删除标签后，相应的讨论上的标签没有去掉", tagFileReader.getTagInItemTitle());

        zombiePage.navigate(topic1.getTopicUrl(), tagFileReader.getTagAddButtonInItem());
        tagSearchBox.openBox(zombiePage);
        assertEquals("给讨论添加标签时，被删除的标签还可以看到",-1,
                zombiePage.findItemInOnePageByTextEquals(tagFileReader.getTagItemInAddTagList(), removeTag.getTagName()));
    }

    @Test
    public void test02_0_addAndRemoveTagInTopicDetail() throws ParseException, InterruptedException {
        zombiePage.navigate(topic2.getTopicUrl(), tagFileReader.getTagAddButtonInItem() );
        newTag.addTagInProject(zombiePage);

        tagSearchBox.openBox(zombiePage);
        zombiePage.testSelectHintItemWithKeyboard("topic 的标签时", topicFileReader.getTopicTagCurItemInAddTagList(),
                topicFileReader.getTopicTagSelectorInputInAddTagList(), "");
        tagSearchBox.searchAddTag(zombiePage, newTag);

        Tag addTagInSearchBox = new Tag("addNewTagInTagSearchBox","","",zombieUser,ciwangPublic);
        tagSearchBox.searchItem(zombiePage, addTagInSearchBox.getTagName());
        zombiePage.assertTextEquals("给讨论添加新的标签时的搜索框，搜不到标签时提示有误",
                "未找到匹配标签，回车可添加 "+addTagInSearchBox.getTagName(),tagFileReader.getTagItemInAddTagList());
        addTagInSearchBox.setTagColorRBG(zombiePage.getAttribute(tagFileReader.getTagItemInAddTagList()+" span","style"));
        int tagCount = zombiePage.getElementCount(tagFileReader.getTagInItemDetail());
        zombiePage.sendKeys(Keys.ENTER);
        zombiePage.waitForItemCountChange(tagFileReader.getTagInItemDetail(),tagCount,5);
        assertNotEquals("标签列表没找到匹配标签，回车失败" ,-1,zombiePage.findItemInOnePageByTextEquals(tagFileReader.getTagItemInTagList(),addTagInSearchBox.getTagName()));
        assertNotEquals("讨论标题未找到匹配标签，回车失败", -1, zombiePage.findItemInOnePageByTextEquals(tagFileReader.getTagInItemDetail(), addTagInSearchBox.getTagName()));

        newTag.removeTagInItemDetail(zombiePage);

        tagCount = zombiePage.getElementCount(tagFileReader.getTagInItemDetail());
        tagSearchBox.openBox(zombiePage);
        zombiePage.sendKeys(Keys.ENTER);
        zombiePage.waitForItemCountChange(tagFileReader.getTagInItemDetail(), tagCount, 6);
        assertNotEquals("讨论标签列表没找到匹配标签，回车添加讨论标签失败", -1,
                zombiePage.findItemInOnePageByTextEquals(tagFileReader.getTagInItemDetail(), bugTag.getTagName()));
        zombiePage.assertElementPresent("给讨论添加标签时，已添加的标签在标签列表上没有出现 打勾标记",
                tagFileReader.getTagItemInAddTagList() + " .checkmark.icon",
                zombiePage.findItemInOnePageByTextEquals( tagFileReader.getTagItemInAddTagList(),  bugTag.getTagName()));
        tagCount = zombiePage.getElementCount(tagFileReader.getTagInItemDetail());
        zombiePage.sendKeys(Keys.ENTER);
        zombiePage.waitForItemCountChange(tagFileReader.getTagInItemDetail(), tagCount, 6);
        assertEquals("讨论标签列表找到匹配标签，回车删除讨论标签失败", -1,
                zombiePage.findItemInOnePageByTextEquals(tagFileReader.getTagInItemDetail(), bugTag.getTagName()));

        zombiePage.navigate(ciwangPublic.getProjectTopicHomeLink(), topicFileReader.getItemInItemHome());
        zombiePage.assertTextEquals("讨论标签没有在讨论页出现", addTagInSearchBox.getTagName(), tagFileReader.getTagInItemTitle(),
                zombiePage.findItemInOnePageByTextEquals(topicFileReader.getTopicTitleInItemHome(), topic2.getTopicTitle()));
    }
    @Test
    public void test03_0_TagFilter(){
        zombiePage.refresh(ciwangPublic.getProjectTopicHomeLink(), topicFileReader.getItemInItemHome());
        bugTag.addTagInTopicDetail(zombiePage, topic1, topicFileReader.getItemInItemHome());
        bugTag.addTagInTopicDetail(zombiePage,topic2, topicFileReader.getItemInItemHome());
        featureTag.addTagInTopicDetail(zombiePage,topic1, topicFileReader.getItemInItemHome());
        zombiePage.assertTextEquals("标签'Bug'的数目不正确", "Bug (2)", tagFileReader.getTagItemInTagList());

        bugTag.verifyTagFilterInTagList(zombiePage,topic2, 2,topicFileReader.getItemHomeHeader());
        featureTag.verifyTagFilterInTopicTitle(zombiePage,topic1,1,topicFileReader.getItemHomeHeader());
    }


    @Test
    public void test05_addAndDeleteTagWhenCreateAndEditTopic() throws InterruptedException {
        TagSearchBox tagSearchBox = new TagSearchBox();
        newTag.addTagInProject(zombiePage);
        Topic newTopic = new Topic(ciwangPublic, "testName","sdfsdfds", zombieUser );
        zombiePage.navigate(ciwangPublic.getProjectTopicCreateLink(), topicFileReader.getTopicTitleInput());
        zombiePage.clearAndSendKeys(topicFileReader.getTopicTitleInput(),"testName");
        zombiePage.clearAndSendKeys(topicFileReader.getTopicContentInput(), "sdfsdfds");
        tagSearchBox.openBox(zombiePage);
        tagSearchBox.searchAddTag(zombiePage, newTag);
        tagSearchBox.searchAddTag(zombiePage, bugTag);
        bugTag.removeTagInItemDetail(zombiePage);
        zombiePage.clickElement(topicFileReader.getReleaseTopicButton(),topicFileReader.getCommentInput());
        zombiePage.assertTextEquals("编辑讨论时，添加删除标签出错", newTag.getTagName(), tagFileReader.getTagInItemDetail());
        newTopic.setTopicUrl(zombiePage.getCurrentUrl());

        zombiePage.navigate(newTopic.getTopicEditUrl(), topicFileReader.getTopicTitleInput());
        zombiePage.clearAndSendKeys(topicFileReader.getTopicContentInput(), "just edit");
        tagSearchBox.openBox(zombiePage);
        tagSearchBox.searchAddTag(zombiePage, bugTag);
        newTag.removeTagInItemDetail(zombiePage);
        zombiePage.clickElement(topicFileReader.getReleaseTopicButton(),topicFileReader.getCommentInput());
        zombiePage.assertTextEquals("编辑讨论时，添加删除标签出错", bugTag.getTagName(), tagFileReader.getTagInItemDetail());

        zombiePage.clickElement(topicFileReader.getEditButton(), tagFileReader.getTagInItemDetail());
        assertNotEquals("编辑讨论时，原有的标签消失", -1,
                zombiePage.findItemInOnePageByTextEquals(tagFileReader.getTagInItemDetail(), bugTag.getTagName()));

    }
    @Test
    public void test06_0_checkTagEditAuthorityForNoneMember() throws InterruptedException {
        // 删除 zombie 项目成员， 检查能否编辑和添加标签
        ciwangPublic.setType("public");
        ciwangPublic.removeProjectMember(zombieUser);
        zombiePage.navigate(ciwangPublic.getProjectTopicHomeLink(), topicFileReader.getAddTopic());
        zombiePage.assertElementNotPresent("被移除项目后，讨论标签编辑按钮", tagFileReader.getTagSettingIcon());
        zombiePage.clickElement(topicFileReader.getTopicTitleInItemHome(),topicFileReader.getCommentInput());
        zombiePage.assertElementNotPresent("被移除项目后，讨论详情页标签添加按钮", tagFileReader.getTagAddButtonInItem());
        zombiePage.assertElementNotPresent("被移除项目后，讨论详情页标签删除按钮", tagFileReader.getTagRemoveIconWhenEditItem());
    }
    @Test
    public void test06_1_activityInMyWatchProjectAndWatchUser() throws InterruptedException, ParseException {
        (new RelationShip()).followSomeone(zombieUser,ciwangUser);
        Project  ciwangPublic = new Project(ciwangUser, "public");
        (new PublicProjectTest()).watchProject(zombiePage,ciwangPublic);
        Topic topic3 = new Topic(ciwangPublic,"tag test3"+ zombiePage.getMark(), "the content of tag test",ciwangUser);
        topic3.createTopic();
        AdvancedActivity advancedActivity = new AdvancedActivity(ciwangUser,zombieUser,ciwangPublic);
        advancedActivity.constructCreateTopicInPublicProjectActivity(topic3, activityFileReader.getTopicChatIconClass());
        zombiePage.navigate(zombieUser.getUserCenterLink(), activityFileReader.getActivityItem());
        String activeTabText = zombiePage.getText(activityFileReader.getActivityTabInUserCenter() + ".active");
        zombiePage.clickElement(activityFileReader.getActivityTabInUserCenter(),1);
        zombiePage.waitForContentChange(activityFileReader.getActivityTabInUserCenter() + ".active", activeTabText, 5);
        advancedActivity.watchActivityCheck(zombiePage);
        activeTabText = zombiePage.getText(activityFileReader.getActivityTabInUserCenter() + ".active");
        zombiePage.clickElement(activityFileReader.getActivityTabInUserCenter(),2);
        zombiePage.waitForContentChange(activityFileReader.getActivityTabInUserCenter() + ".active", activeTabText, 5);
        advancedActivity.watchActivityCheck(zombiePage);
    }

    public void checkDefaultTag(Page page){
        page.assertIconAndText("默认常用讨论标签'Bug'", tagFileReader.getTagItemInTagList(), "Bug", "tag");
        page.assertIconAndText("默认常用讨论标签'功能'", tagFileReader.getTagItemInTagList(), "功能", "tag", 1);
        page.assertIconAndText("默认常用讨论标签'调研'", tagFileReader.getTagItemInTagList(), "调研", "tag", 2);
    }
    public void openTagEditInterFace(Page page){
        if(page.elementIsPresent(tagFileReader.getTagSettingIcon(), PS.shortWait)){
            page.clickElement(tagFileReader.getTagSettingIcon(),tagFileReader.getTagTextInput());
        }
    }

    public void checkTagCanEdit(Page projectPage, int tagNum){
        if(projectPage.elementIsPresent(tagFileReader.getNewTagTextInput(),PS.shortWait)) {
            projectPage.assertElementPresent("验证标签编辑权限时，便签编辑框未出现", tagFileReader.getNewTagTextInput());
            projectPage.clearAndSendKeys(tagFileReader.getTagTextInput(), tagNum, "BugTT");
            projectPage.assertAttributeEquals("项目成员不可以编辑其他人的标签","BugTT",tagFileReader.getTagTextInput(), tagNum,"value");
        }
    }


    @AfterClass
    public static void tearDown() throws Exception {
       // ciwangPublic.deleteProject();
        zombieUser.getPage().getDriver().quit();
        ciwangUser.getPage().getDriver().quit();
    }

}

package org.ChiTest.Setting;

import dataReader.ConfigureInfo;
import org.ChiTest.Page.Page;
import org.ChiTest.User.User;
import org.ChiTest.WebComponent.ScreenShot;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.Cookie;
import reference.ConfigFileReader;

import java.text.ParseException;

import static org.junit.Assert.*;

/**
 * Created by dugancaii on 10/16/2014.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SettingTest {
    private  static String loginUrl;
    protected  static User zombieUser;
    protected  static User ciwangUser;
    private static Page zombiePage;
    private static Cookie zombieCookie;
    private static Cookie ciwangCookie;
    private  static ConfigureInfo configureInfo;
    private SettingFileReader  settingFileReader;
    private String filePath = ConfigFileReader.class.getResource("/6B80D68F0817475DB84CF4BF6D2CF154.jpg").toString();
    public SettingTest() throws ParseException {
        settingFileReader = new SettingFileReader();
    }
    @BeforeClass
    public static void setUp() throws Exception {
        configureInfo = new ConfigureInfo(true,true);
        zombiePage = configureInfo.getZombieUser().getPage();
        zombieUser = new User("codingTutorial","codingTutorial","123456",zombiePage,"Fruit-3.png");
        ciwangUser = configureInfo.getCiwangUser();
        zombieCookie = zombiePage.getStagingSid("61d87148-75f1-4ae0-bdec-8239a02e7d7c");
        ciwangCookie = configureInfo.getCiwangCookie();
        zombieUser.autoLogin(configureInfo.getLoginUrl(),zombieCookie );
        ciwangUser.autoLogin(configureInfo.getLoginUrl(), ciwangCookie);
    }
    //todo 生日和地区的输入框,工作另外测暂时不测先
    @Rule
    public ScreenShot screenShotZombie = new ScreenShot(zombieUser);
    @Rule
    public ScreenShot screenShotCiwang = new ScreenShot(ciwangUser);
    @Test
    public void test01_SettingPersonalInfo() throws InterruptedException {
        zombiePage.navigate(zombieUser.getUserSettingLink(), settingFileReader.getNameInput());
        /*
        zombiePage.clickElement(".select2-container");
        zombiePage.executeScript("$(\".select2-chosen\").html(\"产品\")");
        clickAndVerifyInfo();
        assertEquals("工作信息更新不成功","产品",zombiePage.getText(".select2-chosen"));
        */
        clearTag();
        zombiePage.clickElement("#tag-select .ng-scope[value=\"6\"]");
        clickAndVerifyInfo();
        assertEquals("更新个性标签失败","Python",zombiePage.getText(".select2-search-choice div"));
        zombiePage.clickElement(".select2-choices .select2-search-choice-close");
        clickAndVerifyInfo();
        assertFalse("删除个性标签失败", zombiePage.elementIsPresent(".select2-search-choice div",2));

        //todo google selenium radio button click
        /*
        zombiePage.clickElement(settingFileReader.getMaleInput());
        clickAndVerifyInfo();
        assertEquals("性别信息更新不成功", "true", zombiePage.getAttribute(settingFileReader.getMaleInput(), "checked"));
        zombiePage.verifyHint("个人信息更新成功！");
        Thread.sleep(2000);
        */
        zombiePage.clearAndSendKeys(settingFileReader.getNameInput(), "zombie_debug111111111111111111");
        zombiePage.clickElement(settingFileReader.getSubmitButton());
        zombiePage.verifyHint("昵称太长");
        zombiePage.clearAndSendKeys(settingFileReader.getNameInput(), "zombie_debug");
        zombiePage.clickElement(settingFileReader.getSubmitButton());
        zombiePage.verifyHint("昵称已经被使用");
        zombiePage.clearAndSendKeys(settingFileReader.getNameInput(), "zombie_debug112");
        zombiePage.clickElement(settingFileReader.getSubmitButton());
        zombiePage.verifyHint("个人信息更新成功！");
        changePersonalInfo(settingFileReader.getNameInput(), "zombie_debug11","昵称信息");
        changePersonalInfo(settingFileReader.getCompanyInput(), "coding","公司信息");
        changePersonalInfo(settingFileReader.getLocationInput(), "浙江 温州","地区信息");
        changePersonalInfo(settingFileReader.getBirthdayInput(), "1991-11-12","生日信息");
        changePersonalInfo(settingFileReader.getSloganInput(), "海纳百川，有容乃大","座右铭信息");
        resetInfo();
        zombiePage.clearAndSendKeys(settingFileReader.getPhoneInput(), "851-65885551");
        zombiePage.assertTextEquals("电话格式不对的提示没出现","格式不对！",".phone-error");
    }
    @Test
    public  void test02_SettingAvatar() throws InterruptedException {
        //上传大图现在无限制，先不做这个测试
        zombiePage.navigate(zombieUser.getUserSettingLink(), settingFileReader.getAvatarEditButton());
        zombiePage.clickElement(settingFileReader.getAvatarEditButton(), settingFileReader.getAvatarCancelEditButton());
        String avatarPreviewImgUrl = zombiePage.getAttribute(settingFileReader.getAvatarPreview(), "src");
        assertTrue("取消个人头像修改的测试失效", zombiePage.clickElement(settingFileReader.getAvatarCancelEditButton(), settingFileReader.getAvatarEditButton()));
        zombiePage.clickElement(settingFileReader.getAvatarEditButton(), settingFileReader.getAvatarInputZone());
        zombiePage.clickElement(settingFileReader.getAvatarInputZone());
        zombiePage.executeScript("$('" + settingFileReader.getAvatarInputZone() + "').css('display', 'block')");
        zombiePage.sendImgFilePath(settingFileReader.getAvatarInputZone(), filePath);
        zombiePage.waitLoadingIconInvisible();
        //zombiePage.elementIsPresent(".image-crop",5);
        //todo phantomjs 有误 截图  看看
        assertTrue("头像设置里面上传头像后，没有裁边的框", zombiePage.elementIsPresent(settingFileReader.getJcropHline(), 4));
        zombiePage.clickElement("#crop-avatar-modal .gray.button");
        Thread.sleep(1000);
        assertNotEquals("个人头像设置时，上传图片后，预览没有变化",
                zombiePage.getAttribute(settingFileReader.getAvatarPreview(), "src"), avatarPreviewImgUrl);
        avatarPreviewImgUrl = zombiePage.getAttribute(settingFileReader.getAvatarPreview(), "src");
        zombiePage.clickElement(settingFileReader.getAvatarSaveConfirmButton(), settingFileReader.getAvatarImg());
        zombiePage.verifyHint("修改成功");
        assertEquals("个人头像修改失败", avatarPreviewImgUrl,
                zombiePage.getAttribute(settingFileReader.getAvatarImg(), "src"));
        zombiePage.clickElement(settingFileReader.getAvatarEditButton(), settingFileReader.getAvatarPreview());
        assertTrue("修改头像时当前头像显示有误", zombiePage.getAttribute(settingFileReader.getAvatarPreview(), "src").contains("https://dn-coding-net-avatar.qbox.me"));
        zombiePage.clickElement(settingFileReader.getFruitAvatar(4));
        assertTrue("设置个人头像时,选择水果头像后，没有显示红色打钩", zombiePage.elementIsPresent(settingFileReader.getFruitAvatar(4) + " .red", 4));
        avatarPreviewImgUrl = zombiePage.getAttribute(settingFileReader.getAvatarPreview(), "src");
        zombiePage.clickElement(settingFileReader.getFruitAvatar(3));
        assertNotEquals("个人头像设置时，选择水果头像后，头像预览没有变化",
                zombiePage.getAttribute(settingFileReader.getAvatarPreview(), "src"), avatarPreviewImgUrl);
        zombiePage.clickElement(settingFileReader.getAvatarSaveConfirmButton(),settingFileReader.getAvatarImg());
        zombiePage.waitElementInvisible(".loading.icon");
        assertTrue("个人头像修改失败",
                zombiePage.getAttribute(settingFileReader.getAvatarImg(),"src").contains("Fruit-3.png"));
    }
    @Test
    public void test03_setGravatar() throws InterruptedException {
        Page page = ciwangUser.getPage();
        page.navigate(ciwangUser.getUserSettingLink(), settingFileReader.getAvatarEditButton());
        page.clickElement(settingFileReader.getAvatarEditButton(), settingFileReader.getAvatarPreview());
        page.assertTextEquals("garvatar 标题出错", "使用 Gravatar 头像", settingFileReader.getGravatarSyncTitle());
        String previewUrl = page.getAttribute(settingFileReader.getGravatarSyncPreview(), "src");
        page.clickElement(settingFileReader.getGravatarSyncButton());
        if(!page.waitForAttributeChange(settingFileReader.getGravatarSyncPreview(), "src",previewUrl,10)){
            page.clickElement(settingFileReader.getGravatarSyncButton());
        }
        assertTrue("同步 garvatar 头像后，garvatar 预览url没有变化",
                page.waitForAttributeChange(settingFileReader.getGravatarSyncPreview(), "src",previewUrl,30));
        previewUrl = page.getAttribute(settingFileReader.getGravatarSyncPreview(), "src");
        page.clickElement(settingFileReader.getGravatarSyncPreview());
        assertTrue("设置个人头像时,选择 garvatar 头像后，没有显示红色打钩", page.elementIsPresent( "#edit-avatar .system-avatar:nth-child(4) .ui .red", 4));

        assertEquals("设置个人头像时,选择 garvatar 头像后，头像的预览没有和 garvatar 头像一致", previewUrl, page.getAttribute(settingFileReader.getGravatarSyncPreview(), "src") );

        page.clickElement(settingFileReader.getAvatarSaveConfirmButton(), settingFileReader.getAvatarImg());
        page.verifyHint("修改成功");

        assertEquals("garvatar 头像修改失败",
                previewUrl, page.getAttribute(settingFileReader.getAvatarImg(), "src"));

        page.clickElement(settingFileReader.getAvatarEditButton(), settingFileReader.getAvatarPreview());
        page.clickElement(settingFileReader.getFruitAvatar(2));
        page.clickElement(settingFileReader.getAvatarSaveConfirmButton(), settingFileReader.getAvatarImg());
    }
    @Test
    public void test04_remindSetting(){
        zombiePage.navigate(zombieUser.getUserNotificationSettingLink(), settingFileReader.getNotificationSettingShowMore());
        zombiePage.assertTextEquals("设置中心的标题有误", "系统提醒", "#notification-setting table tr td:nth-child(2)");
        zombiePage.assertTextEquals("设置中心的标题有误", "邮件提醒", "#notification-setting table tr td:nth-child(3)");
        //验证文案，两个check框，点击后是否会出现修改成功和 ng-pristine 变成  ng-dirty
        /*
        settingCheckBoxTest("#AtSettingNotification","#AtSettingEmail");
        settingCheckBoxTest("#ProjectTopicSettingNotification","#ProjectTopicSettingEmail");
        settingCheckBoxTest("#TaskSettingNotification","#TaskSettingEmail");
        zombiePage.getAttribute("#AtSettingNotification","class");
        */

        int settingItemCount = zombiePage.getElementCount(settingFileReader.getNotificationSettingItem());
        zombiePage.clickElement(settingFileReader.getNotificationSettingShowMore());
        assertTrue("提醒设置的设置更多貌似失效",zombiePage.waitForItemCountChange(settingFileReader.getNotificationSettingItem(), settingItemCount,5));

        //todo 关闭通知后，操作未发通知

        zombiePage.assertTextEquals("提醒设置中\"@我\"消失","@我", settingFileReader.getNotificationSettingItem());
        zombiePage.assertTextEquals("提醒设置中 有新讨论 消失","有新讨论", settingFileReader.getNotificationSettingItem(),1);
        zombiePage.assertTextEquals("提醒设置中 任务提醒 消失","任务提醒", settingFileReader.getNotificationSettingItem(),2);
        zombiePage.assertTextEquals("提醒设置中 Commit 评论 消失","Commit 评论", settingFileReader.getNotificationSettingItem(),3);
        zombiePage.assertTextEquals("提醒设置中 新增粉丝 消失","新增粉丝", settingFileReader.getNotificationSettingItem(),4);
        zombiePage.assertTextEquals("提醒设置中 Merge Request 消失","Merge Request", settingFileReader.getNotificationSettingItem(),5);
        zombiePage.assertTextEquals("提醒设置中 加入/退出项目 消失","加入/退出项目", settingFileReader.getNotificationSettingItem(),6);
        zombiePage.assertTextEquals("提醒设置中 讨论有回复 消失","讨论有回复", settingFileReader.getNotificationSettingItem(),7);
        zombiePage.assertTextEquals("提醒设置中 Pull Request 消失","Pull Request", settingFileReader.getNotificationSettingItem(),8);
        zombiePage.assertTextEquals("提醒设置中 质量管理 消失","质量管理", settingFileReader.getNotificationSettingItem(),9);
        zombiePage.assertTextEquals("提醒设置中 项目转让 消失","项目转让", settingFileReader.getNotificationSettingItem(),10);
        zombiePage.assertTextEquals("提醒设置中 冒泡被点赞 消失","冒泡被点赞", settingFileReader.getNotificationSettingItem(),11);
        zombiePage.assertTextEquals("提醒设置中 冒泡有回复 消失","冒泡有回复", settingFileReader.getNotificationSettingItem(),12);
        /*
        settingCheckBoxTest("#FansSettingNotification","#FansSettingEmail");
        settingCheckBoxTest("#MergeRequestSettingNotification","#MergeRequestSettingEmail");
        settingCheckBoxTest("#ProjectMemberSettingNotification","#ProjectMemberSettingEmail");
        settingCheckBoxTest("#ProjectTopicCommentSettingNotification","#ProjectTopicCommentSettingEmail");
        settingCheckBoxTest("#PullRequestSettingNotification","#PullRequestSettingEmail");
        settingCheckBoxTest("#QCSettingNotification","#QCSettingEmail");
        settingCheckBoxTest("#TransferProjectNotification","#TransferProjectEmail");
        settingCheckBoxTest("#TweetLikeSettingNotification","#TweetLikeSettingEmail");
        settingCheckBoxTest("#TweetSettingNotification","#TweetSettingEmail");
        */



    }
    public void settingCheckBoxTest(String settingNotification, String settingEmail){
        zombiePage.clickElement(settingNotification);
        assertTrue("提醒按键"+settingNotification+"失灵",zombiePage.waitForAttributeChange(settingNotification,"class","ng-pristine ng-valid",5));
        zombiePage.clickElement(settingEmail);
        assertTrue("提醒按键"+settingEmail+"失灵",zombiePage.waitForAttributeChange(settingEmail,"class","ng-pristine ng-valid",5));
        zombiePage.clickElement(settingNotification);
        zombiePage.clickElement(settingEmail);
        zombiePage.verifyHint("修改成功");
    }



    private void clearTag(){
        while (zombiePage.elementIsPresent(".select2-container-multi  .select2-search-choice-close",5)){
            zombiePage.clickElement(".select2-container-multi  .select2-search-choice-close");
        }
    }

    public void changePersonalInfo(String selectorForChangeInfoInput, String changeInfo, String messageAlert){
        zombiePage.clearAndSendKeys(selectorForChangeInfoInput, changeInfo);
        clickAndVerifyInfo();
        assertEquals(messageAlert + "修改失败",changeInfo, zombiePage.getAttribute(selectorForChangeInfoInput,"value") );
    }
    public void clickAndVerifyInfo(){
        zombiePage.clickElement(settingFileReader.getSubmitButton());
        zombiePage.waitElementDisappear(".loading.form");
        zombiePage.clickElement(settingFileReader.getPasswordSetting(), settingFileReader.getCurrentPasswordInput());
        zombiePage.clickElement(settingFileReader.getPersonalSetting(), settingFileReader.getNameInput());
    }
    public void resetInfo(){
        changePersonalInfo(settingFileReader.getNameInput(), "coding5002","昵称信息");
        changePersonalInfo(settingFileReader.getCompanyInput(), "希云","公司信息");
        changePersonalInfo(settingFileReader.getLocationInput(), "广东 深圳","地区信息");
        changePersonalInfo(settingFileReader.getBirthdayInput(), "1991-12-07","生日信息");
        changePersonalInfo(settingFileReader.getSloganInput(), "活在当下，自强不息","座右铭信息");
        clickAndVerifyInfo();
    }
    @AfterClass
    public static void tearDown() throws Exception {
        ciwangUser.getPage().getDriver().quit();
        zombiePage.getDriver().quit();
    }
}

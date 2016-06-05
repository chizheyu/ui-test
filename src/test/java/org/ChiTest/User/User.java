package org.ChiTest.User;

import org.ChiTest.congfig.ConfigureInfo;
import org.ChiTest.Page.PS;
import org.ChiTest.Page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import reference.ConfigFileReader;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;


public class User {
    private Page page;
    private String baseUrl;

    private String UserLoginName;//若是你的昵称和个性后缀不一样，要用个性后缀名登录
    private String UserName;

    ConfigFileReader fileReader = new ConfigFileReader();

    public String getUserPassword() {
        return UserPassword;
    }

    protected String UserPassword;
    private String avatar;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    public List<String> getFollow() {
        return follow;
    }

    public void setFollow(List<String> follow) {
        this.follow = follow;
    }

    public List<String> getFans() {
        return fans;
    }

    public void setFans(List<String> fans) {
        this.fans = fans;
    }

    private List<String> follow = new LinkedList<String>();
    private List<String> fans = new LinkedList<String>();

    private String projectName;



    public String getMessageMark() {
        return messageMark;
    }

    public void setMessageMark(String messageMark) {
        this.messageMark = messageMark;
    }

    private String messageMark;

    public int addMessageReceivedNum() {
        return messageReceivedNum++;
    }

    public void reduceMessageReceivedNum() {
        this.messageReceivedNum--;
    }
    public void resetMessageReceivedNum() {
        this.messageReceivedNum = 0;
    }
    public int getMessageReceivedNum() {
       return this.messageReceivedNum ;
    }

    private int messageReceivedNum = 0;
    private int messageNotReadNum  = 0;
    private int messageNotReadBaseNum = 0;
    private int messageInSessionBaseNum;
    private int messageInSessionNum;
    public int getMessageInSessionBaseNum() {
        return messageInSessionBaseNum;
    }

    public void setMessageInSessionBaseNum(int messageInSessionBaseNum) {
        this.messageInSessionBaseNum = messageInSessionBaseNum;
    }

    public int getMessageInSessionNum() {
        return messageInSessionNum;
    }

    public void setMessageInSessionNum(int messageInSessionNum) {
        this.messageInSessionNum = messageInSessionNum;
    }

    public int getMessageNotReadBaseNum() {
        return messageNotReadBaseNum;
    }

    public void setMessageNotReadBaseNum(int messageNotReadBaseNum) {
        this.messageNotReadBaseNum = messageNotReadBaseNum;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public int getMessageNotReadNum() {
        return messageNotReadNum;
    }

    public void setMessageNotReadNum(int messageNotReadNum) {
        this.messageNotReadNum = messageNotReadNum;
    }
    public String getHomePageLink(){
        return this.getPage().getBaseUrl() + "u/" + this.getUserLoginName();
    }
    public String getMyTaskLink(){
        return this.getPage().getBaseUrl() + "user/tasks";
    }
    public String getMyWatchedTaskLink(){
        return this.getPage().getBaseUrl() + "user/tasks/watch";
    }
    public String getBubblingLink(){
        return this.getPage().getBaseUrl() + "pp";
    }
    public String getUserHomePageBubblingLink(){
        return this.getPage().getBaseUrl() + "u/"+this.getUserLoginName()+"/bubble";
    }


    public String getUserCenterLink(){
        return this.getPage().getBaseUrl()+"user";
    }
    public String getAllUserMessageLink(){
        return this.getPage().getBaseUrl()+"user/messages/basic";
    }
    public String getMessageLinkWithSomeone(User receiver){
        return this.getPage().getBaseUrl()+"user/messages/history/" + receiver.getUserLoginName();
    }
    public String getUserSettingLink(){
        return this.getPage().getBaseUrl()+"user/account/setting";
    }
    public String getUserNotificationSettingLink(){
        return this.getPage().getBaseUrl()+"user/account/setting/notification";
    }
    public String getUserNotificationLink(){
        return this.getPage().getBaseUrl()+"user/notifications/basic";
    }

    public String getAvatar() {
        return avatar;
    }
    public String getFruitAvatar(){
        return  this.page.getBaseUrl()+ "static/fruit_avatar/"+ avatar;
    }
    public String getProject() {
        return projectName;
    }
    public void setProject(String projectName) {
        this.projectName = projectName;
    }


    public User(){
        this.UserLoginName = fileReader.getValue("defaultUserName") ;
        this.UserName = fileReader.getValue("defaultUserName");
        this.UserPassword = fileReader.getValue("defaultUserPassword");
    }
    public User(String UserLoginName, String UserName, String UserPassword){
        this.UserLoginName = UserLoginName;
        this.UserName = UserName;
        this.UserPassword =  UserPassword;
    }
    public User(String UserLoginName, String UserName, String UserPassword, Page page){
        this(UserLoginName,  UserName,  UserPassword);
        this.page = page;
    }
    public User(String UserLoginName, String UserName, String UserPassword, Page page,String avatar){
        this(UserLoginName,  UserName,  UserPassword,page);
        this.avatar = avatar;
    }
    public User(String UserLoginName, String UserName, String UserPassword, String projectName,String avatar){
        this.UserLoginName = UserLoginName;
        this.UserName = UserName;
        this.UserPassword =  UserPassword;
        this.avatar = avatar;
        this.projectName = projectName;
    }



    public String getUserLoginName() {
        return UserLoginName;
    }

    public String getUserName() {
        return UserName;
    }


    public void autoLogin( String LoginUrl,Cookie cookie)  {
            this.page.navigate(LoginUrl,"#email");
            WebDriver driver = page.getDriver() ;
            driver.manage().deleteCookieNamed("sid");
            driver.manage().addCookie(cookie);
            if(null == driver.manage().getCookieNamed("sid")){
                System.out.println("cookie 没有加成功");
                driver.manage().addCookie(cookie);
            }
            for(int i = 0;i<3;i++) {
                this.page.navigate(this.getUserCenterLink());
                if(this.page.getWaitTool().waitForElement(By.cssSelector("#project-activities"), PS.longWait)!=null){
                    return;
                }
                driver.manage().deleteCookieNamed("sid");
                driver.manage().addCookie(cookie);
            }
        page.assertTextEquals("登录失败，可能cookie失效",page.getDriver().getCurrentUrl(),this.baseUrl + "user");
        //assertEquals(this.getUserLoginName() + "无法自动登录,已获取新的sid:"+getSid.getSid(this)+"请修改配置文件",1,2 );
        //如果以后搞持续集成sid可以写到配置文件里，每次去取也没关系
    }
    public void login() throws ParseException {
        ConfigureInfo ConfigureInfo = new ConfigureInfo(false, false);
        this.page.navigate(ConfigureInfo.getLoginUrl(),"#email");
        page.clearAndSendKeys("#email",this.getUserLoginName());
        page.clearAndSendKeys("#password",this.getUserPassword());
        page.clickElement(".large.button  ", "#activity " );

    }
}


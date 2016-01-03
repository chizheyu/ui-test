package org.ChiTest.Login;

import dataReader.ConfigureInfo;
import org.ChiTest.Email.Email;
import org.ChiTest.Email.EmailTest;
import org.ChiTest.Page.Page;
import org.ChiTest.User.User;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import reference.ConfigFileReader;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginTest {
    private ConfigFileReader fileReader;
    private User zombieUser;
    private org.openqa.selenium.support.events.EventFiringWebDriver driver;
    private Page loginPage;
    private static String baseUrl;
    private Email email163;
    private Navigation navigation;
    private static String loginUrl;

    @Before
    public void setUp() throws Exception {
        ConfigureInfo ConfigureInfo = new ConfigureInfo(false, true);
        zombieUser = ConfigureInfo.getZombieUser();
        fileReader = new ConfigFileReader();
        loginUrl = ConfigureInfo.getLoginUrl();
        baseUrl = ConfigureInfo.getBaseUrl();

        loginPage = zombieUser.getPage();

        email163 = new Email(fileReader.getValue("emailName"),fileReader.getValue("postfix"),fileReader.getValue("password"));
        driver = loginPage.getDriver();
        navigation =  loginPage.getDriver().navigate();
        loginPage.navigate(loginUrl);

    }


    @Test
    public void test02_forgetPassword() throws Exception {
        forgetPassword();
    }
    @Test
    public void test04_rememberMe() throws Exception {
        loginPage.navigate(loginUrl + "login","#email" );
        loginPage.clearAndSendKeys("#email", zombieUser.getUserLoginName());
        loginPage.clearAndSendKeys("#password", zombieUser.getUserPassword());
        loginPage.clickElement(".checkbox label");
        loginPage.clickElement(".button.large", "#project-activities");

        assertEquals("点击按钮失败 ",baseUrl + "user", driver.getCurrentUrl());
        assertNotNull("普登录后（记住我）cookie的过期时间为null", loginPage.getDriver().manage().getCookieNamed("sid").getExpiry());
    }

    @Test
    public void test05_badLogin() throws Exception {
        doBadLogin();
    }

    @Test
    public void test01_register() throws Exception  {
        //测试首页的登录
        System.out.println("测试注册模块" + loginUrl);
        loginPage.navigate(loginUrl, ".input-wrapper.gkey input");
        System.out.println("输入正确的后缀和邮箱");
        loginPage.sendKeys(".input-wrapper.gkey input", email163.getPostfix());
        loginPage.sendKeys(".input-wrapper.email input", email163.getEmailName() + "@163.com");
        loginPage.clickElement(".button-wrapper button",".ui.form .coding.large.green.gray.button" );
        Thread.sleep(15000);
        loginPage.clickElement(".ui.form .coding.large.green.gray.button ", "#project-list");
        loginPage.verifyHint("欢迎注册 Coding，请尽快去邮箱查收邮件并激活账号。如若在收件箱中未看到激活邮件，请留意一下垃圾邮件箱(T_T)。");
        loginPage.clickElement("#coding-start-button .button");
        loginPage.getWaitTool().waitForJavaScriptCondition("return $('#coding-start-button .button').length == 0 ",2);
        loginPage.clickElement("#dropdown-icon");
        loginPage.clickElement(".sign.out", ".input-wrapper.gkey input");

        System.out.println("测试已有的后缀");
        loginPage.clearAndSendKeys(".input-wrapper.gkey input","ciwang");
        loginPage.clearAndSendKeys(".input-wrapper.email input",email163.getEmailName() + "@163.com");
        loginPage.clickElement(".button-wrapper button");
        loginPage.getWaitTool().waitForJavaScriptCondition("return $(\".loading.icon\").length == 0",2);
        assertTrue("可以注册已有的后缀", loginPage.elementIsPresent(".ui.form .coding.large.green.gray.button ",5));

        System.out.println("测试已有邮箱");
        loginPage.sendKeys(".input-wrapper.gkey input","ciwang");
        loginPage.sendKeys(".input-wrapper.email input","675991451@qq.com");
        loginPage.clickElement(".button-wrapper button");
        loginPage.getWaitTool().waitForJavaScriptCondition("return $(\".loading.icon\").length == 0",2);

        assertTrue("可以注册已有的邮箱", loginPage.elementIsPresent(".ui.form .coding.large.green.gray.button ",5));
        sendReActivateEmail(email163.getEmailName()  + "@163.com");
        Thread.sleep(10000);
        verifyEmail(email163);

        //到登录的注册页面
        /*
        loginPage.navigate(baseUrl+"register",".large.button");
        email163 = new Email(fileReader.getValue("emailName2"),fileReader.getValue("postfix2"),fileReader.getValue("password2"));
        System.out.println("测试普通注册页面" + baseUrl+"register");
        loginPage.navigate(baseUrl+"register",".form input");
        loginPage.sendKeys(".form .global input",email163.getPostfix() + (new Date()).getTime()/(1000));
        loginPage.sendKeys(".form input",email163.getEmailName()+ (new Date()).getTime()/(1000) + "@163.com");
        loginPage.clickElement(".large.button");
        loginPage.verifyHint("欢迎注册 Coding，请尽快去邮箱查收邮件并激活账号。如若在收件箱中未看到激活邮件，请留意一下垃圾邮件箱(T_T)。");

        loginPage.clickElement("#coding-start-button .button");
        loginPage.getWaitTool().waitForJavaScriptCondition("return $('#coding-start-button .button').length == 0 ",2);
        loginPage.clickElement("#dropdown-icon");
        loginPage.clickElement(".sign.out");
        */
    }
    public void doBadLogin() throws InterruptedException {
        loginPage.navigate(loginUrl + "login", ".form .button");
        loginPage.clearAndSendKeys("#email","coding_test5@163.com");
        loginPage.clearAndSendKeys("#password","123456ddddd");
        for(int i = 0; i<5; i++) {
            loginPage.clickElement( ".form .button");
        }
        assertTrue("验证码没有出现",loginPage.elementIsPresent(".captcha",5));

    }


    public void sendReActivateEmail(String email) throws InterruptedException {
        loginPage.refresh(baseUrl + "login",".activate a");
        loginPage.refresh();
        loginPage.navigate(baseUrl + "login",".activate a");
        loginPage.clickElement(".activate a");
        loginPage.getWaitTool().waitForJavaScriptCondition("return $(\".button span\").html() == \"重发激活邮件\"",2);

        System.out.println("验证邮件格式错误和验证码不为空");
        loginPage.sendKeys(".form input","12");
        loginPage.clickElement("form .button");
        assertEquals("邮件格式错误", loginPage.getText(".arrow_box"));
        loginPage.verifyHint("验证码不能为空");

        System.out.println("验证你输入的验证码有误");
        driver.findElement(By.cssSelector("form .ng-isolate-scope")).sendKeys("@qq.com");
        driver.findElement(By.tagName("form")).findElements(By.tagName("input")).get(1).sendKeys("df");
        driver.findElement(By.cssSelector("form .button")).click();
        Thread.sleep(1000);
        loginPage.verifyHint("你输入的验证码有误");

        System.out.println("验证用户不存在");
        driver.findElement(By.cssSelector("form img")).click();
        driver.findElement(By.cssSelector("form .button")).click();
        driver.findElement(By.cssSelector(".outer span")).click();
        loginPage.verifyHint("用户不存在");

        System.out.println("重发邮件成功");
        driver.findElement(By.cssSelector("form .ng-isolate-scope")).clear();
        driver.findElement(By.cssSelector("form .ng-isolate-scope")).sendKeys(email);
        driver.findElement(By.cssSelector("form img")).click();
        driver.findElement(By.cssSelector("form .button")).click();
        loginPage.verifyHint("已经发送邮件");
        Thread.sleep(5000);
    }


    @Test
    public void test06_doBadRegister() throws Exception{
        int markNumBase = (int)(new Date()).getTime()/(1000);
        int markNum;
        System.out.println("markNumBase " + markNumBase);

        //打开下一个tab，输入用户名和邮箱
        //首页登录更新
        loginPage.getDriver().quit();
        for(int i = 0; i<6; i++) {
            markNum = markNumBase + i;
            loginPage.setDriver(new EventFiringWebDriver(new FirefoxDriver()) );
            loginPage.navigate(loginUrl, "#video-description .email input");

            loginPage.sendKeys("#video-description .gkey input" , "Test" + markNum);
            loginPage.sendKeys("#video-description .email input", "doEmail" + markNum + "@qq.com");
            loginPage.clickElement(".red.register.button");
            Thread.sleep(2000);
            if(null != loginPage.getElement(".captcha")){
                break;
            }
            loginPage.getDriver().quit();

            Thread.sleep(3000);
        }
        assertNotNull(loginPage.getElement(".captcha"));
    }

    public void verifyEmail(Email email) throws InterruptedException {
        //到163网站去验证
        System.out.println("去网易邮箱验证");
        navigation.to(email.getEmailUrl());
        driver.findElement(By.id("idInput")).sendKeys(email.getEmailName());
        driver.findElement(By.id("pwdInput")).sendKeys(email.getPassword());
        driver.findElement(By.id("loginBtn")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("/html/body/div[1]/nav/div[1]/ul/li[1]/span[2]")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div/div/div[4]/div[2]")).click();
        String iframe = driver.findElement(By.className("nu0")).findElement(By.tagName("iframe")).getAttribute("id");
        driver.switchTo().frame(iframe);
        driver.findElement(By.xpath("/html/body/table/tbody/tr[5]/td/div/a")).click();

        //重设密码
        System.out.println("重设密码,输入小于6位的密码");
        String[] handles = new String[driver.getWindowHandles().size()];
        driver.getWindowHandles().toArray(handles);
        WebDriver newWindow = driver.switchTo().window(handles[1]);
        newWindow.findElement(By.xpath("/html/body/div[3]/div/form/div[1]/div[2]/div/input")).sendKeys("12345");

        assertEquals("密码太短", newWindow.findElement(By.cssSelector(".ui.form .field.error .red.horizontal.label.arrow_box")).getText());

        WebElement passInput = newWindow.findElement(By.xpath("/html/body/div[3]/div/form/div[1]/div[2]/div/input"));
        WebElement rePassInput = newWindow.findElement(By.xpath("/html/body/div[3]/div/form/div[1]/div[3]/div/input"));
        passInput.clear();
        passInput.sendKeys("123456");
        rePassInput.sendKeys("12345");
        System.out.println("重设密码,两次输入密码不一致");
        assertEquals("两次输入密码不一致", newWindow.findElement(By.cssSelector(".ui.form .field.error .red.horizontal.label.arrow_box")).getText());



        //输入正确密码
        System.out.println("输入正确密码");
        rePassInput.sendKeys("6");
        newWindow.findElement(By.cssSelector("form .button")).click();
        Thread.sleep(1000);
        assertTrue(newWindow.getCurrentUrl().contains(baseUrl + "user"));

        loginPage.verifyHint("激活成功");
        loginPage.getDriver().findElement(By.id("dropdown-icon")).click();
        loginPage.getDriver().findElement(By.linkText("退出")).click();

    }
    @Test
    public void test03_login() throws Exception {
        loginPage.navigate(loginUrl ,".login");
        System.out.println("测试登录模块，主要是测试登录后的url是否是，https://coding.net/user");
        System.out.println("以及登录是的一些提示信息，登录名是否为空之类的");
        loginPage.clickElement(".login","#email");

        // 登录成功达到“https://coding.net/user”   提示“登录成功，Welcome!*** 用户名”
        System.out.println("输入正确的邮箱和密码，成功登录");
        loginPage.clearAndSendKeys("#email","coding_test5@163.com");
        loginPage.clearAndSendKeys("#password","123456");
        loginPage.clickElement(".large.button  ");
        driver.findElement(By.cssSelector(".outer")).click();
        loginPage.verifyHint("Welcome! zombie_debug");
        assertEquals("登录不成功",baseUrl + "user", loginPage.getDriver().getCurrentUrl());
        loginPage.clickElement("#dropdown-icon");
        loginPage.clickElement(".sign.out",".login");
        loginPage.clickElement(".login");
        loginPage.getWaitTool().waitForElement(By.cssSelector("#email"),3);

        //验证密码输入是否正确，输入错误提示“用户名和密码不匹配”
        System.out.println("输入正确的用户名和错误的密码");
        loginPage.clearAndSendKeys("#email","test5"
        );
        loginPage.clearAndSendKeys("#password", "1212121");
        loginPage.clickElement(".large.button ");
        loginPage.verifyHint("用户名密码不匹配");

        System.out.println("输入正确的个性后缀和密码，成功登录");
        // 登录成功达到“https://coding.net/user”   提示“登录成功，Welcome!*** 用户名”
        loginPage.clearAndSendKeys("#email","test5");
        loginPage.clearAndSendKeys("#password", "123456");
        loginPage.clickElement(".large.button ","#context-menu");

        loginPage.verifyHint("Welcome! zombie_debug");

        Cookie cookie = loginPage.getDriver().manage().getCookieNamed("sid");
        assertNull("普通登录后（没有记住我）cookie的过期时间不为null", cookie.getExpiry());
        assertEquals("登录不成功",baseUrl + "user", driver.getCurrentUrl());


    }
    public void forgetPassword() throws Exception {
        String password = (new Date()).getTime()/1000 + "i";
        navigation.to(baseUrl + "login");

        driver.findElement(By.linkText("忘记密码?")).click();
        driver.findElement(By.linkText("返回")).click();
        driver.findElement(By.linkText("忘记密码?")).click();
        System.out.println("验证找回密码模块");
        //输入错误邮箱和空验证码
        System.out.println("输入错误邮箱和空验证码");
        WebElement input = driver.findElement(By.className("field")).findElement(By.tagName("input"));
        input.sendKeys("codi");
        loginPage.clickElement(".large.button");
        //Thread.sleep(500);
        loginPage.verifyHint("验证码不能为空");
        assertEquals("找回密码页面，邮件格式有误提示消失","邮件格式错误",loginPage.getText(".red.horizontal"));

        //输入错误验证码
        System.out.println("输入错误验证码");
        input.sendKeys(fileReader.getValue("zombieEmail"));
        loginPage.sendKeys("#captcha","1234");
        loginPage.clickElement(".large.button");
        driver.findElement(By.className("outer")).click();
        loginPage.verifyHint("你输入的验证码有误");

        //输入正确验证码错误邮箱
        Thread.sleep(12000);
        System.out.println("输入正确验证码错误邮箱");
        loginPage.clickElement(".large.button");
        driver.findElement(By.className("outer")).click();
        loginPage.verifyHint("用户不存在");

        //输入正确邮箱
        System.out.println("输入正确邮箱");
        input.clear();
        input.sendKeys(fileReader.getValue("zombieEmail"));
        Thread.sleep(12000);
        loginPage.clickElement(".large.button");
        Thread.sleep(2000);
        assertEquals(baseUrl+"login", driver.getCurrentUrl());
        Thread.sleep(20000);

        //去网易邮箱验证
        System.out.println("去网易邮箱验证");
        loginPage.navigate("http://mail.163.com/","#idInput");
        driver.findElement(By.id("idInput")).sendKeys(fileReader.getValue("zombieEmail"));
        driver.findElement(By.id("pwdInput")).sendKeys("chi2014");
        driver.findElement(By.id("loginBtn")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("/html/body/div[1]/nav/div[1]/ul/li[1]/span[2]")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div/div/div[4]/div[2]")).click();
        String iframe = driver.findElement(By.className("nu0")).findElement(By.tagName("iframe")).getAttribute("id");
        driver.switchTo().frame(iframe);
        driver.findElement(By.xpath("/html/body/table/tbody/tr[5]/td/div/a")).click();

        //重设密码
        System.out.println("重设密码,输入小于6位的密码");
        String[] handles = new String[driver.getWindowHandles().size()];
        driver.getWindowHandles().toArray(handles);
        driver.switchTo().window(handles[1]);
        loginPage.clearAndSendKeys("input[type='password']","12345");
        assertEquals("重置密码,密码太短提示有误","密码太短",loginPage.getText(".arrow_box"));
        loginPage.clearAndSendKeys("input[type='password']",password);
        loginPage.clearAndSendKeys("input[placeholder='重复密码']","12345");
        assertEquals("两次输入密码不一致",loginPage.getText(".arrow_box"));

        //输入正确密码
        System.out.println("输入正确密码");
        loginPage.clearAndSendKeys("input[placeholder='重复密码']",password);
        loginPage.clickElement("input[value='确定']");
        loginPage.verifyHint("密码已经重置");
        assertEquals("重置密码后没有跳转到登入页",baseUrl + "login", loginPage.getDriver().getCurrentUrl());

        loginPage.clearAndSendKeys("#email", fileReader.getValue("zombieEmail"));
        loginPage.clearAndSendKeys("#password",password);
        loginPage.clickElement(".large.button ", "#project-list");
        assertEquals("登录不成功",baseUrl + "user", loginPage.getDriver().getCurrentUrl());
        //测试重置密码
        loginPage.clickElement("#dropdown-icon");
        loginPage.clickElement(".settings",".shield.icon");
        loginPage.clickElement(".shield.icon");

        loginPage.clearAndSendKeys("input[name='passwd.current_password']",password+"dfd");
        loginPage.clearAndSendKeys("input[name='passwd.password']","123");
        loginPage.clearAndSendKeys("input[name='passwd.confirm_password']","123");
        loginPage.clickElement(".submit.button");
        loginPage.verifyHint("新密码长度为6~64位");

        loginPage.clearAndSendKeys("input[name='passwd.password']","123456");
        loginPage.clearAndSendKeys("input[name='passwd.confirm_password']","123456");
        loginPage.clickElement(".submit.button");
        loginPage.verifyHint("用户的原始密码有误");

        loginPage.clearAndSendKeys("input[name='passwd.current_password']",password);
        loginPage.clickElement(".submit.button");
        loginPage.verifyHint("修改成功,请使用新密码重新登录！");
        loginPage.getWaitTool().waitForElement(By.cssSelector("#email"), 10);
        assertEquals("修改密码后没有进入登录页", baseUrl + "login", loginPage.getDriver().getCurrentUrl());

        loginPage.clearAndSendKeys("#email",fileReader.getValue("zombieEmail"));
        loginPage.clearAndSendKeys("#password","123456");
        loginPage.clickElement(".large.button ", "#project-list");
        assertEquals("修改密码后重新登录不成功",baseUrl + "user", loginPage.getDriver().getCurrentUrl());

        Set mailCheckList = new HashSet();
        EmailTest emailTest = new EmailTest();
        mailCheckList.add( "\"您Coding.net中的密码被修改了。如果是您操作的，请忽略此邮件，如果不是您操作的，可以点此处重置密码。\"");
        mailCheckList.add( "秒重置了你的账号密码");
        Thread.sleep(20000);
        emailTest.checkEmail(loginPage, mailCheckList);

    }

    @After
    public void tearDown() throws Exception {
      loginPage.getDriver().quit();
    }
   }

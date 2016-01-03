package reference;

import org.ChiTest.MyWebDriverListener;
import org.ChiTest.Page.Page;
import org.ChiTest.User.User;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TaskTest {
    private WebDriver driverBase;
    private WebDriver associateDriverBase;
    private static ConfigFileReader fileReader = new ConfigFileReader();
    protected static User zombieUser;
    protected static User ciwangUser;
    private static String testBaseUrl;
    private static String loginUrl;
    private String projectName[] = new String[2];
    private String projectOwner[] = new String[2];

    private String zombieUserName;
    private String ciwangUserName;

    private org.openqa.selenium.support.events.EventFiringWebDriver driver;
    private org.openqa.selenium.support.events.EventFiringWebDriver associateDriver;

    private Navigation navigation;
    private Navigation associateNavigation;
    private static Cookie ciwangCookie;
    private static Cookie zombieCookie;

    private Page zombiePage;
    private Page ciwangPage;

    @BeforeClass
    public  static void configure() {
        Date date = new Date(115, 7, 10);
        int a =Integer.parseInt(fileReader.getValue("option"));
        System.out.println("a " + a);
        zombieUser = new User(fileReader.getValue("zombieUserPostfix"), fileReader.getValue("zombieUserName"), fileReader.getValue("zombieUserPassword"));
        ciwangUser = new User(fileReader.getValue("ciwangUserPostfix"), fileReader.getValue("ciwangUserName"), fileReader.getValue("ciwangUserPassword"));
        if(a == 2){
            testBaseUrl = fileReader.getValue("baseUrl");
            loginUrl = fileReader.getValue("loginUrl");
            ciwangCookie = new Cookie(fileReader.getValue("cookieNameCiwang"),fileReader.getValue("cookieValueCiwang"), fileReader.getValue("cookieZoneCiwang"), fileReader.getValue("cookiePathCiwang"), date);
            zombieCookie = new Cookie(fileReader.getValue("cookieNameZombie"),fileReader.getValue("cookieValueZombie"), fileReader.getValue("cookieZoneZombie"), fileReader.getValue("cookiePathZombie"), date);

        }
        else {
            testBaseUrl = fileReader.getValue("stagingBaseUrl");
            loginUrl = fileReader.getValue("stagingLoginUrl");
            ciwangCookie = new Cookie(fileReader.getValue("stagingCookieNameCiwang"),fileReader.getValue("stagingCookieValueCiwang"), fileReader.getValue("stagingCookieZoneCiwang"), fileReader.getValue("stagingCookiePathCiwang"), date);
            zombieCookie = new Cookie(fileReader.getValue("stagingCookieNameZombie"),fileReader.getValue("stagingCookieValueZombie"), fileReader.getValue("stagingCookieZoneZombie"), fileReader.getValue("stagingCookiePathZombie"), date);
        }

    }

    @Before
    public void setUp() throws Exception {
        //WebDriver associateDriverBase = new InternetExplorerDriver();
        System.setProperty("webdriver.chrome.driver",fileReader.getValue("driverlocation"));
        driverBase = new FirefoxDriver();
        driverBase.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driverBase.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);

        associateDriverBase = new ChromeDriver();
        associateDriverBase.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        associateDriverBase.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);


        //创建者的名字
        projectOwner[0] = zombieUser.getUserLoginName();
        projectOwner[1] = ciwangUser.getUserLoginName();

        //项目名
        projectName[0] = zombieUser.getProject();
        projectName[1] = ciwangUser.getProject();


        //用户名
        zombieUserName = zombieUser.getUserName();
        ciwangUserName = ciwangUser.getUserName();

        //创建webdriver
        driver = new org.openqa.selenium.support.events.EventFiringWebDriver(driverBase);
        driver.register(new MyWebDriverListener());
        navigation= driver.navigate();

        associateDriver = new org.openqa.selenium.support.events.EventFiringWebDriver(associateDriverBase);
        associateDriver.register(new MyWebDriverListener());
        associateNavigation= associateDriver.navigate();

        zombiePage = new Page(loginUrl,driver);
        ciwangPage = new Page(loginUrl,associateDriver);

        zombieUser.setPage(zombiePage);
        ciwangUser.setPage(ciwangPage);

        //自动登录
        zombieUser.autoLogin(loginUrl + "login",zombieCookie);
        ciwangUser.autoLogin(loginUrl + "login",ciwangCookie);


        Thread.sleep(2000);
        System.out.println("自动登录成功" );
    }

    //测试我的任务中已完成的页面
//@Test
    public void testMyTaskDonePage() throws Exception
    {
        String taskId[] = new String[4];//获取任务的id
        //main的基本参数
        int projectTaskBaseNum[] = {0,0,0};//原始的项目数
        int projectTaskTempNum[] = {0,0,0};//改变后的项目数
        int pageTaskTempNum = 0;//task框上方显示的改变后的任务数
        //项目的任务数
        int mainProjectTask[]= {0,0};
        int associateProjectTask[]= {0,0};
        //改变后的项目的任务数
        int mainProjectTmepTask[]= {0,0};
        int associateProjectTmepberTask[]= {0,0};

        //associate的基本参数
        int as_projectTaskBaseNum[] = {0,0,0};//原始的项目数
        int as_projectTaskTempNum[] = {0,0,0};//改变后的项目数
        int as_pageTaskTempNum = 0;//task框上方显示的改变后的任务数
        //项目的任务数
        int as_mainProjectTask[]= {0,0};

        //改变后的项目的任务数
        int as_mainProjectTmepTask[]= {0,0};

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = df.format(new Date());// new Date()为获取当前系统时间

        String processUrl =testBaseUrl+ "user/tasks/processing";

        //taskUrl
        String mainTaskUrl = testBaseUrl+"u/"+projectOwner[0]+"/p/" + projectName[0]+ "/tasks/processing";
        String associateTaskUrl = testBaseUrl + "u/"+projectOwner[1]+"/p/" + projectName[1]+ "/tasks/processing";
        System.out.println("mainTaskUrl " + mainTaskUrl);
        //String testUrl = "https://coding.net/user/tasks/done";
        String testUrl = testBaseUrl +"user/tasks/done";
        System.out.println("开始测试“我的任务”模块，url：" +  testUrl);
        System.out.println("测试的主用户账户为：" +  zombieUserName);
        System.out.println("测试的辅助账户为：" +  ciwangUserName);
        System.out.println("密码均为：123456" );
        System.out.println("测试的主项目为：" +  projectName[0]);
        System.out.println("测试的辅助项目为：" +   projectName[1]);

        //在两个项目中发送task，并且记录task的id
        //task[0] 和 task[2]是mian的task
        //到ciwangUser的页面上获取基本数据
        associateNavigation.to(testUrl);
        Thread.sleep(6000);
        getTaskNumber(associateDriver, as_projectTaskBaseNum);
        getProjectTaskNum(associateDriver, as_mainProjectTask, projectName[0]);


        //回到zombieUser的页面获取基本的的数据
        navigation.to(testUrl);
        Thread.sleep(5000);
        getTaskNumber(driver, projectTaskBaseNum);
        getProjectTaskNum(driver, mainProjectTask, projectName[0]);
        getProjectTaskNum(driver, associateProjectTask, projectName[1]);
        System.out.println("已获取页面基本数值信息  " );
        System.out.println("所有任务数是  " + projectTaskBaseNum[0]);
        navigation.to(mainTaskUrl);
        Thread.sleep(5000);
        pushTask(driver, "associate user's first task in " + projectName[0] + " at "+ time, ciwangUserName);
        Thread.sleep(2000);
        pushTask(driver, "main user's first task in " + projectName[0] + " at "+ time, zombieUserName);
        Thread.sleep(4000);
        taskId[0] = driver.findElement(By.id("task-list")).findElement(By.className("project")).findElements(By.tagName("textarea")).get(0).getAttribute("id");
        taskId[1] = driver.findElement(By.id("task-list")).findElement(By.className("project")).findElements(By.tagName("textarea")).get(1).getAttribute("id");
        System.out.println("taskId[0] " + taskId[0]);
        System.out.println("taskId[1] " +taskId[1]);
        checkTask(driver, taskId[0]);
        Thread.sleep(2000);
        checkTask(driver, taskId[1]);
        Thread.sleep(2000);
        navigation.to(associateTaskUrl);
        Thread.sleep(2000);
        pushTask(driver, "associate user's first task in " + projectName[1] + " at "+ time, ciwangUserName);
        Thread.sleep(2000);
        pushTask(driver, "main user's first task in " + projectName[1] + " at "+ time, zombieUserName);
        Thread.sleep(2000);

        taskId[2] = driver.findElement(By.id("task-list")).findElement(By.className("project")).findElements(By.tagName("textarea")).get(0).getAttribute("id");
        taskId[3] = driver.findElement(By.id("task-list")).findElement(By.className("project")).findElements(By.tagName("textarea")).get(1).getAttribute("id");
        System.out.println("taskId[2] " + taskId[2]);
        System.out.println("taskId[3] " + taskId[3]);
        checkTask(driver, taskId[2]);
        Thread.sleep(2000);
        checkTask(driver, taskId[3]);
        Thread.sleep(2000);
        System.out.println("已发送两个用户的任务并且已完成，每人每个项目发一个任务 " );

        //将其中mianUser的task变成未完成
        navigation.to(testUrl);
        //测试已完成的任务是否无法编辑
        System.out.println("测试mianUser已完成的任务无法编辑 " );
        Thread.sleep(2000);
        assertFalse(driver.findElement(By.id(taskId[0])).isEnabled());
        checkTask(driver,  taskId[0]);

        Thread.sleep(2000);
        pageTaskTempNum = getTaskNumber(driver, projectTaskTempNum);
        getProjectTaskNum(driver, mainProjectTmepTask,projectName[0]);
        getProjectTaskNum(driver, associateProjectTmepberTask,projectName[1]);

        System.out.println("将其中mianUser的task变成正在完成，task[0] " );
        assertEquals(projectTaskTempNum[2], pageTaskTempNum);
        assertEquals(projectTaskBaseNum[0] + 2, projectTaskTempNum[0]);
        assertEquals(projectTaskBaseNum[1] + 1, projectTaskTempNum[1]);
        assertEquals(projectTaskBaseNum[2] + 1, projectTaskTempNum[2]);
        assertEquals(mainProjectTask[0] + 1, mainProjectTmepTask[0]);
        assertEquals(mainProjectTask[1] + 1, mainProjectTmepTask[1]);
        assertEquals(associateProjectTask[0] , associateProjectTmepberTask[0]);
        assertEquals(associateProjectTask[1] + 1, associateProjectTmepberTask[1]);


        //测试编辑框是否可以编辑
        System.out.println("测试任务编辑框可编辑 " );
        String originalContent = getTaskContent(taskId[0], driver);
        editTask(taskId[0],"task owner has changed, the new owner ", driver);
        Thread.sleep(4000);
        checkTask(driver,  taskId[0]);
        Thread.sleep(4000);
        navigation.refresh();
        Thread.sleep(4000);
        String editContent = getTaskContent(taskId[0], driver);
        System.out.println("originalContent " + originalContent);
        System.out.println("editContent " + editContent);

        assertNotEquals(originalContent, editContent);

        //转移任务给其他人
        checkTask(driver,  taskId[0]);
        Thread.sleep(2000);
        resignTask(taskId[0], ciwangUserName,  driver);
        Thread.sleep(3000);
        pageTaskTempNum = getTaskNumber(driver, projectTaskTempNum);
        getProjectTaskNum(driver, mainProjectTmepTask,projectName[0]);
        getProjectTaskNum(driver, associateProjectTmepberTask,projectName[1]);
        System.out.println("测试转移任务给ciwangUser " );
        assertEquals(projectTaskTempNum[2], pageTaskTempNum);
        assertEquals(projectTaskBaseNum[0] + 1, projectTaskTempNum[0]);
        assertEquals(projectTaskBaseNum[1] , projectTaskTempNum[1]);
        assertEquals(projectTaskBaseNum[2] + 1 , projectTaskTempNum[2]);
        assertEquals(mainProjectTask[0] , mainProjectTmepTask[0]);
        assertEquals(mainProjectTask[1] , mainProjectTmepTask[1]);
        assertEquals(associateProjectTask[0], associateProjectTmepberTask[0]);
        assertEquals(associateProjectTask[1] + 1, associateProjectTmepberTask[1]);

        //到ciwangUser的task页面上看看任务数
        associateNavigation.refresh();
        Thread.sleep(7000);
        as_pageTaskTempNum = getTaskNumber(associateDriver, as_projectTaskTempNum);
        getProjectTaskNum(associateDriver, as_mainProjectTmepTask,projectName[0]);
        System.out.println("测试ciwangUser的任务页面在转移任务后是否发生变化 " );
        assertEquals(as_projectTaskTempNum[2], as_pageTaskTempNum);
        assertEquals(as_projectTaskBaseNum[0] + 3, as_projectTaskTempNum[0]);
        assertEquals(as_projectTaskBaseNum[1] + 1, as_projectTaskTempNum[1]);
        assertEquals(as_projectTaskBaseNum[2] + 2, as_projectTaskTempNum[2]);
        assertEquals(as_mainProjectTask[0] + 1, as_mainProjectTmepTask[0]);
        assertEquals(as_mainProjectTask[1] + 2, as_mainProjectTmepTask[1]);
        //先回到正在进行页面再
        associateNavigation.to(processUrl);
        Thread.sleep(4000);
        resignTask(taskId[0], zombieUserName,  associateDriver);
        Thread.sleep(2000);
        associateNavigation.to(testUrl);
        Thread.sleep(4000);
        as_pageTaskTempNum = getTaskNumber(associateDriver, as_projectTaskTempNum);
        getProjectTaskNum(associateDriver, as_mainProjectTmepTask,projectName[0]);
        System.out.println("测试ciwangUser把刚才的任务转移给zombieUser " );

        assertEquals(as_projectTaskTempNum[2], as_pageTaskTempNum);
        assertEquals(as_projectTaskBaseNum[0] + 2, as_projectTaskTempNum[0]);
        assertEquals(as_projectTaskBaseNum[1], as_projectTaskTempNum[1]);
        assertEquals(as_projectTaskBaseNum[2] + 2, as_projectTaskTempNum[2]);
        assertEquals(as_mainProjectTask[0] , as_mainProjectTmepTask[0]);
        assertEquals(as_mainProjectTask[1] + 1, as_mainProjectTmepTask[1]);

        //zombieUser删掉一个正在完成
        //测试先关闭再激活会不会加1，
        navigation.refresh();
        Thread.sleep(4000);
        checkTask(driver,  taskId[2]);
        Thread.sleep(2000);
        pageTaskTempNum = getTaskNumber(driver, projectTaskTempNum);
        getProjectTaskNum(driver, mainProjectTmepTask,projectName[0]);
        getProjectTaskNum(driver, associateProjectTmepberTask,projectName[1]);
        System.out.println("zombieUser在已完成页面重新激活一个新任务 " );

        assertEquals(projectTaskTempNum[2], pageTaskTempNum);
        assertEquals(projectTaskBaseNum[0] + 2, projectTaskTempNum[0]);
        assertEquals(projectTaskBaseNum[1] + 2, projectTaskTempNum[1]);
        assertEquals(projectTaskBaseNum[2], projectTaskTempNum[2]);
        assertEquals(mainProjectTask[0] + 1, mainProjectTmepTask[0]);
        assertEquals(mainProjectTask[1] + 1, mainProjectTmepTask[1]);
        assertEquals(associateProjectTask[0]+1, associateProjectTmepberTask[0]);
        assertEquals(associateProjectTask[1]+1, associateProjectTmepberTask[1]);



        removeTask(driver,  taskId[2]);
        Thread.sleep(2000);
        pageTaskTempNum = getTaskNumber(driver, projectTaskTempNum);
        getProjectTaskNum(driver, mainProjectTmepTask,projectName[0]);
        getProjectTaskNum(driver, associateProjectTmepberTask,projectName[1]);
        System.out.println("zombieUser删掉一个已完成的任务 " );

        assertEquals(projectTaskTempNum[2], pageTaskTempNum);
        assertEquals(projectTaskBaseNum[0] + 1, projectTaskTempNum[0]);
        assertEquals(projectTaskBaseNum[1] + 1, projectTaskTempNum[1]);
        assertEquals(projectTaskBaseNum[2], projectTaskTempNum[2]);
        assertEquals(mainProjectTask[0] + 1, mainProjectTmepTask[0]);
        assertEquals(mainProjectTask[1] + 1, mainProjectTmepTask[1]);
        assertEquals(associateProjectTask[0], associateProjectTmepberTask[0]);
        assertEquals(associateProjectTask[1], associateProjectTmepberTask[1]);

        //zombieUser删掉一个正在完成的任务
        //先回到正在完成的页面，完成任务，再回到已完成页面，再删除

        navigation.to(processUrl);
        checkTask(driver,  taskId[0]);
        navigation.to(testUrl);
        removeTask(driver,  taskId[0]);
        Thread.sleep(2000);
        pageTaskTempNum = getTaskNumber(driver, projectTaskTempNum);
        getProjectTaskNum(driver, mainProjectTmepTask,projectName[0]);
        getProjectTaskNum(driver, associateProjectTmepberTask,projectName[1]);
        System.out.println("zombieUser删掉一个正在完成的任务 " );
        assertEquals(projectTaskTempNum[2], pageTaskTempNum);
        assertEquals(projectTaskBaseNum[0], projectTaskTempNum[0]);
        assertEquals(projectTaskBaseNum[1], projectTaskTempNum[1]);
        assertEquals(projectTaskBaseNum[2], projectTaskTempNum[2]);
        assertEquals(mainProjectTask[0], mainProjectTmepTask[0]);
        assertEquals(mainProjectTask[1], mainProjectTmepTask[1]);
        assertEquals(associateProjectTask[0], associateProjectTmepberTask[0]);
        assertEquals(associateProjectTask[1], associateProjectTmepberTask[1]);
    }







    public void testMyTaskBasePage(int pageNumber, String testUrl,WebDriver driver, Navigation navigation, WebDriver associateDriver, Navigation associateNavigation) throws Exception
    {


        String taskId[] = new String[4];//获取任务的id

        //main的基本参数
        int projectTaskBaseNum[] = {0,0,0};//原始的项目数
        int projectTaskTempNum[] = {0,0,0,0};//改变后的项目数
        int pageTaskTempNum = 0;//task框上方显示的改变后的任务数
        //项目的任务数
        int mainProjectTask[]= {0,0};
        //改变后的项目的任务数
        int mainProjectTmepTask[]= {0,0};

        //associate的基本参数
        int as_projectTaskBaseNum[] = {0,0,0};//原始的项目数
        int as_projectTaskTempNum[] = {0,0,0,0};//改变后的项目数
        int as_pageTaskTempNum = 0;//task框上方显示的改变后的任务数
        //项目的任务数
        int as_mainProjectTask[]= {0,0};

        //改变后的项目的任务数
        int as_mainProjectTmepTask[]= {0,0};

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = df.format(new Date());// new Date()为获取当前系统时间

        //taskUrl
        String mainTaskUrl = testBaseUrl+"u/"+projectOwner[0]+"/p/" + projectName[0]+ "/tasks/processing";
        System.out.println("开始测试“我的任务”模块，url：" +  testUrl);
        System.out.println("测试的主用户账户为：" +  zombieUserName);
        System.out.println("测试的辅助账户为：" +  ciwangUserName);
        System.out.println("密码均为：123456" );
        System.out.println("测试的主项目为：" +  projectName[0]);


        //在两个项目中发送task，并且记录task的id
        //task[0] 和 task[2]是mian的task
        //到ciwangUser的页面上获取基本数据
        Thread.sleep(2000);
        associateNavigation.to(testUrl);
        Thread.sleep(5000);
        getTaskNumber(associateDriver, as_projectTaskBaseNum);
        getProjectTaskNum(associateDriver, as_mainProjectTask, projectName[0]);


        //回到zombieUser的页面获取基本的的数据
        navigation.to(testUrl);
        Thread.sleep(5000);
        getTaskNumber(driver, projectTaskBaseNum);
        getProjectTaskNum(driver, mainProjectTask, projectName[0]);
        System.out.println("已获取页面基本数值信息  " );

        navigation.to(mainTaskUrl);
        Thread.sleep(5000);
        pushTask(driver, "associate user's first task in " + projectName[0] + " at "+ time, ciwangUserName);
        Thread.sleep(1000);
        pushTask(driver, "main user's first task in " + projectName[0] + " at "+ time, zombieUserName);
        Thread.sleep(1000);
        taskId[0] = driver.findElement(By.id("task-list")).findElement(By.className("project")).findElements(By.tagName("textarea")).get(0).getAttribute("id");
        taskId[1] = driver.findElement(By.id("task-list")).findElement(By.className("project")).findElements(By.tagName("textarea")).get(1).getAttribute("id");
        System.out.println("taskId[0] " + taskId[0]);
        System.out.println("taskId[1] " +taskId[1]);

        pushTask(driver, "associate user's second task in " + projectName[0] + " at "+ time, ciwangUserName);
        Thread.sleep(1000);
        pushTask(driver, "main user's second task in " + projectName[0] + " at "+ time, zombieUserName);
        Thread.sleep(1000);
        taskId[2] = driver.findElement(By.id("task-list")).findElement(By.className("project")).findElements(By.tagName("textarea")).get(0).getAttribute("id");
        taskId[3] = driver.findElement(By.id("task-list")).findElement(By.className("project")).findElements(By.tagName("textarea")).get(1).getAttribute("id");
        System.out.println("taskId[2] " + taskId[2]);
        System.out.println("taskId[3] " + taskId[3]);

        System.out.println("已发送两个用户的任务，每人每个项目发一个任务 " );

        //将其中mianUser的task变成已完成
        navigation.to(testUrl);
        Thread.sleep(5000);
        checkTask(driver,  taskId[0]);
        Thread.sleep(1000);
        pageTaskTempNum = getTaskNumber(driver, projectTaskTempNum);
        getProjectTaskNum(driver, mainProjectTmepTask,projectName[0]);

        System.out.println("将其中mianUser的task变成已完成，task[0] " );
        projectTaskTempNum[3] = mainProjectTmepTask[1];
        assertEquals(projectTaskTempNum[pageNumber], pageTaskTempNum);
        assertEquals(projectTaskBaseNum[0] + 2, projectTaskTempNum[0]);
        assertEquals(projectTaskBaseNum[1] + 1, projectTaskTempNum[1]);
        assertEquals(projectTaskBaseNum[2] + 1, projectTaskTempNum[2]);
        assertEquals(mainProjectTask[0] + 1, mainProjectTmepTask[0]);
        assertEquals(mainProjectTask[1] + 2, mainProjectTmepTask[1]);

        //测试已完成的任务是否无法编辑
        System.out.println("测试mianUser已完成的任务无法编辑 " );
        assertFalse(driver.findElement(By.id(taskId[0])).isEnabled());
        //测试编辑框是否可以编辑
        System.out.println("测试任务编辑框可编辑 " );
        checkTask(driver,  taskId[0]);
        Thread.sleep(1000);
        String originalContent = getTaskContent(taskId[0], driver);
        editTask(taskId[0],"task owner has changed, the new owner ", driver);
        Thread.sleep(1000);
        navigation.to(testUrl);
        Thread.sleep(5000);
        String editContent = getTaskContent(taskId[0], driver);
        System.out.println("originalContent " + originalContent);
        System.out.println("editContent " + editContent);
        assertNotEquals(originalContent, editContent);

        //转移任务给其他人
        resignTask(taskId[0], ciwangUserName,  driver);
        Thread.sleep(1000);
        pageTaskTempNum = getTaskNumber(driver, projectTaskTempNum);
        getProjectTaskNum(driver, mainProjectTmepTask,projectName[0]);
        System.out.println("测试转移任务给ciwangUser " );
        projectTaskTempNum[3] = mainProjectTmepTask[1];
        assertEquals(projectTaskTempNum[pageNumber], pageTaskTempNum);
        assertEquals(projectTaskBaseNum[0] + 1, projectTaskTempNum[0]);
        assertEquals(projectTaskBaseNum[1] + 1, projectTaskTempNum[1]);
        assertEquals(projectTaskBaseNum[2] , projectTaskTempNum[2]);
        assertEquals(mainProjectTask[0] + 1, mainProjectTmepTask[0]);
        assertEquals(mainProjectTask[1] + 1 , mainProjectTmepTask[1]);


        //到ciwangUser的task页面上看看任务数
        associateNavigation.refresh();
        Thread.sleep(6000);
        as_pageTaskTempNum = getTaskNumber(associateDriver, as_projectTaskTempNum);
        getProjectTaskNum(associateDriver, as_mainProjectTmepTask,projectName[0]);
        System.out.println("测试ciwangUser的任务页面在转移任务后是否发生变化 " );
        as_projectTaskTempNum[3] = as_mainProjectTmepTask[1];
        assertEquals(as_projectTaskTempNum[pageNumber], as_pageTaskTempNum);
        assertEquals(as_projectTaskBaseNum[0] + 3, as_projectTaskTempNum[0]);
        assertEquals(as_projectTaskBaseNum[1] + 3, as_projectTaskTempNum[1]);
        assertEquals(as_projectTaskBaseNum[2] , as_projectTaskTempNum[2]);
        assertEquals(as_mainProjectTask[0] + 3, as_mainProjectTmepTask[0]);
        assertEquals(as_mainProjectTask[1] + 3, as_mainProjectTmepTask[1]);


        resignTask(taskId[0], zombieUserName,  associateDriver);
        Thread.sleep(1000);
        as_pageTaskTempNum = getTaskNumber(associateDriver, as_projectTaskTempNum);
        getProjectTaskNum(associateDriver, as_mainProjectTmepTask,projectName[0]);
        System.out.println("测试ciwangUser把刚才的任务转移给zombieUser " );
        as_projectTaskTempNum[3] = as_mainProjectTmepTask[1];
        assertEquals(as_projectTaskTempNum[pageNumber], as_pageTaskTempNum);
        assertEquals(as_projectTaskBaseNum[0] + 2, as_projectTaskTempNum[0]);
        assertEquals(as_projectTaskBaseNum[1] + 2, as_projectTaskTempNum[1]);
        assertEquals(as_projectTaskBaseNum[2] , as_projectTaskTempNum[2]);
        assertEquals(as_mainProjectTask[0] + 2, as_mainProjectTmepTask[0]);
        assertEquals(as_mainProjectTask[1] + 2, as_mainProjectTmepTask[1]);

        //zombieUser删掉一个已完成
        //测试先关闭再激活会不会加1，然后再关闭
        navigation.refresh();
        System.out.println("projectTaskBaseNum[0] " + projectTaskBaseNum[0] );
        Thread.sleep(5000);
        checkTask(driver,  taskId[2]);
        Thread.sleep(1000);
        checkTask(driver,  taskId[2]);
        Thread.sleep(1000);
        checkTask(driver,  taskId[2]);
        System.out.println("projectTaskBaseNum[1] " + projectTaskBaseNum[1]);
        Thread.sleep(4000);
        removeTask(driver,  taskId[2]);
        Thread.sleep(1000);
        pageTaskTempNum = getTaskNumber(driver, projectTaskTempNum);
        getProjectTaskNum(driver, mainProjectTmepTask,projectName[0]);

        System.out.println("zombieUser删掉一个已完成的任务 " );
        projectTaskTempNum[3] = mainProjectTmepTask[1];
        assertEquals(projectTaskTempNum[pageNumber], pageTaskTempNum);
        assertEquals(projectTaskBaseNum[0] + 1, projectTaskTempNum[0]);
        assertEquals(projectTaskBaseNum[1] + 1, projectTaskTempNum[1]);
        assertEquals(projectTaskBaseNum[2], projectTaskTempNum[2]);
        assertEquals(mainProjectTask[0] + 1, mainProjectTmepTask[0]);
        assertEquals(mainProjectTask[1] + 1, mainProjectTmepTask[1]);


        //zombieUser删掉一个正在完成的任务
        //checkTask(driver,  taskId[0]);
        //Thread.sleep(1000);
        removeTask(driver,  taskId[0]);
        Thread.sleep(1000);
        pageTaskTempNum = getTaskNumber(driver, projectTaskTempNum);
        getProjectTaskNum(driver, mainProjectTmepTask,projectName[0]);
        System.out.println("zombieUser删掉一个正在完成的任务 " );
        projectTaskTempNum[3] = mainProjectTmepTask[1];
        assertEquals(projectTaskTempNum[pageNumber], pageTaskTempNum);
        assertEquals(projectTaskBaseNum[0], projectTaskTempNum[0]);
        assertEquals(projectTaskBaseNum[1], projectTaskTempNum[1]);
        assertEquals(projectTaskBaseNum[2], projectTaskTempNum[2]);
        assertEquals(mainProjectTask[0], mainProjectTmepTask[0]);
        assertEquals(mainProjectTask[1], mainProjectTmepTask[1]);

        System.out.println("完成测试“我的任务”模块，url：" +  testUrl);

    }


    public void getProjectTaskNum(WebDriver driver, int [] taskNum, String projectName ) throws Exception
    {
        List<WebElement> taskMemberList = driver.findElement(By.id("task_projects")).findElements(By.tagName("a"));
        WebElement taskMember;
        String tempString = "";
        String tempMemberName = "";
        String num[] = new String[2];
        String regEx2 = "/";
        Pattern pat2 = Pattern.compile(regEx2);
        System.out.println("dsfd " + taskMemberList.size());
        for(int i = 0; i<taskMemberList.size();i++)
        {
            taskMember = taskMemberList.get(i).findElements(By.tagName("div")).get(1);
            tempMemberName = taskMember.getText();
            tempString = taskMember.findElement(By.tagName("span")).getText();

            tempMemberName = tempMemberName.replace(tempString, "").trim();
            System.out.println("name is " + tempMemberName);
            System.out.println("memberName is " + projectName);
            if( tempMemberName.equals(projectName))
            {
                System.out.println("found ");
                num=  pat2.split(tempString);
                taskNum[0] = Integer.parseInt(num[0]);//正在进行任务数
                taskNum[1] = Integer.parseInt(num[1]);//任务总数
                break;
            }

        }
    }



    //@Test
    public void testMemberTaskPage() throws Exception
    {

        String taskId[] = new String[3];//获取任务的id
        int projectTaskBaseNum[] = {0,0,0};//原始的项目数
        int projectTaskTempNum[] = {0,0,0};//改变后的项目数
        int pageTaskTempNum = 0;//task框上方显示的改变后的任务数
        //成员的任务数
        int memberTask[]= {0,0};
        int associateMemberTask[]= {0,0};

        //改变后的成员的任务数
        int memberTmepTask[]= {0,0};
        int associateMemTmepberTask[]= {0,0};

        //项目的任务
        //taskUrl
        String taskUrl = testBaseUrl + "u/"+projectOwner[0]+"/p/"+projectName[0]+"/tasks/user/" + projectOwner[0] +  "/all";
        System.out.println("测试项目成员的任务模块，url：" +  taskUrl);
        System.out.println("测试的主用户账户为：" +  zombieUserName);
        System.out.println("测试的辅助账户为：" +  ciwangUserName);
        System.out.println("密码均为：123456" );
        System.out.println("测试的项目名为tt1111， 创建者为test5" );

        navigation.to(taskUrl);
        Thread.sleep(3000);
        getTaskNumber(driver, projectTaskBaseNum);
        getMemberTaskNumber(driver, memberTask,zombieUserName);
        getMemberTaskNumber(driver, associateMemberTask,ciwangUserName);
        System.out.println("成功获取项目页面基本参数，正在完成数，成员任务数等" );
        System.out.println("为zombieUser和ciwangUsertian个发送一个任务" );
        pushTask(driver, "associate user's first task", ciwangUserName);
        pushTask(driver, "main user's first task", zombieUserName);
        Thread.sleep(2000);
        pageTaskTempNum = getTaskNumber(driver, projectTaskTempNum);
        getMemberTaskNumber(driver, memberTmepTask,zombieUserName);
        getMemberTaskNumber(driver, associateMemTmepberTask,ciwangUserName);

        System.out.println("发送两个任务，ciwangUser和zombieUser各一个" );
        assertEquals(memberTmepTask[1], pageTaskTempNum);
        assertEquals(projectTaskBaseNum[0] + 2, projectTaskTempNum[0]);
        assertEquals(projectTaskBaseNum[1] + 2, projectTaskTempNum[1]);
        assertEquals(projectTaskBaseNum[2] , projectTaskTempNum[2]);
        assertEquals(memberTask[0] + 1, memberTmepTask[0]);
        assertEquals(memberTask[1] + 1, memberTmepTask[1]);
        assertEquals(associateMemberTask[0] + 1, associateMemTmepberTask[0]);
        assertEquals(associateMemberTask[1] + 1, associateMemTmepberTask[1]);

        // taskId[0] is main task and taskId[1] is associate task
        taskId[0] = driver.findElement(By.id("task-list")).findElement(By.className("project")).findElements(By.tagName("textarea")).get(0).getAttribute("id");
        taskId[1] = driver.findElement(By.id("task-list")).findElement(By.className("project")).findElements(By.tagName("textarea")).get(1).getAttribute("id");
        System.out.println(taskId[0]);
        System.out.println(taskId[1]);
        //重新指派任务
        resignTask(taskId[1], zombieUserName, driver);
        Thread.sleep(500);
        pageTaskTempNum = getTaskNumber(driver, projectTaskTempNum);
        getMemberTaskNumber(driver, memberTmepTask,zombieUserName);
        getMemberTaskNumber(driver, associateMemTmepberTask,ciwangUserName);

        System.out.println("将ciwangUser的任务重新指派给zombieUser" );
        assertEquals(memberTmepTask[1], pageTaskTempNum);
        assertEquals(projectTaskBaseNum[0] + 2, projectTaskTempNum[0]);
        assertEquals(projectTaskBaseNum[1] + 2, projectTaskTempNum[1]);
        assertEquals(projectTaskBaseNum[2] , projectTaskTempNum[2]);
        assertEquals(memberTask[0] + 2, memberTmepTask[0]);
        assertEquals(memberTask[1] + 2, memberTmepTask[1]);
        assertEquals(associateMemberTask[0], associateMemTmepberTask[0]);
        assertEquals(associateMemberTask[1], associateMemTmepberTask[1]);
        //编辑任务
        String originalContent = getTaskContent(taskId[0], driver);
        editTask(taskId[0],"task owner has changed, the new owner ", driver);
        Thread.sleep(1000);
        navigation.to(taskUrl);
        Thread.sleep(2000);
        System.out.println("测试任务框是否可以编辑 " );
        String editContent = getTaskContent(taskId[0], driver);
        assertNotEquals(originalContent, editContent);
        //完成任务
        checkTask(driver, taskId[1]);
        Thread.sleep(500);
        //测试已完成的任务是否无法编辑
        System.out.println("测试mianUser已完成的任务无法编辑 " );
        assertFalse(driver.findElement(By.id(taskId[1])).isEnabled());

        pageTaskTempNum = getTaskNumber(driver, projectTaskTempNum);
        getMemberTaskNumber(driver, memberTmepTask,zombieUserName);
        getMemberTaskNumber(driver, associateMemTmepberTask,ciwangUserName);
        System.out.println("测试mianUser完成一个任务 " );
        assertEquals(memberTmepTask[1], pageTaskTempNum);
        assertEquals(projectTaskBaseNum[0] + 2, projectTaskTempNum[0]);
        assertEquals(projectTaskBaseNum[1] + 1, projectTaskTempNum[1]);
        assertEquals(projectTaskBaseNum[2] + 1, projectTaskTempNum[2]);
        assertEquals(memberTask[0] + 1, memberTmepTask[0]);
        assertEquals(memberTask[1] + 2, memberTmepTask[1]);
        assertEquals(associateMemberTask[0], associateMemTmepberTask[0]);
        assertEquals(associateMemberTask[1], associateMemTmepberTask[1]);


        //重新激活任务
        checkTask(driver, taskId[1]);
        Thread.sleep(1000);
        System.out.println("测试mianUser重新开启一个项目 " );
        pageTaskTempNum = getTaskNumber(driver, projectTaskTempNum);
        getMemberTaskNumber(driver, memberTmepTask,zombieUserName);
        getMemberTaskNumber(driver, associateMemTmepberTask,ciwangUserName);
        assertEquals(memberTmepTask[1], pageTaskTempNum);
        assertEquals(projectTaskBaseNum[0] + 2, projectTaskTempNum[0]);
        assertEquals(projectTaskBaseNum[1] + 2, projectTaskTempNum[1]);
        assertEquals(projectTaskBaseNum[2] , projectTaskTempNum[2]);
        assertEquals(memberTask[0] + 2, memberTmepTask[0]);
        assertEquals(memberTask[1] + 2, memberTmepTask[1]);
        assertEquals(associateMemberTask[0], associateMemTmepberTask[0]);
        assertEquals(associateMemberTask[1], associateMemTmepberTask[1]);

        //完成任务并删除掉任务
        checkTask(driver, taskId[1]);
        Thread.sleep(500);
        removeTask(driver, taskId[1]);
        Thread.sleep(500);
        System.out.println("测试删除一个已完成的任务 " );
        pageTaskTempNum = getTaskNumber(driver, projectTaskTempNum);
        getMemberTaskNumber(driver, memberTmepTask,zombieUserName);
        getMemberTaskNumber(driver, associateMemTmepberTask,ciwangUserName);
        assertEquals(memberTmepTask[1], pageTaskTempNum);
        assertEquals(projectTaskBaseNum[0] + 1, projectTaskTempNum[0]);
        assertEquals(projectTaskBaseNum[1] + 1, projectTaskTempNum[1]);
        assertEquals(projectTaskBaseNum[2] , projectTaskTempNum[2]);
        assertEquals(memberTask[0] + 1, memberTmepTask[0]);
        assertEquals(memberTask[1] + 1, memberTmepTask[1]);
        assertEquals(associateMemberTask[0], associateMemTmepberTask[0]);
        assertEquals(associateMemberTask[1], associateMemTmepberTask[1]);

        //删除正在进行的任务
        System.out.println("测试删除一个正在进行的任务 " );
        checkTask(driver, taskId[1]);
        removeTask(driver, taskId[0]);
        Thread.sleep(500);

        pageTaskTempNum = getTaskNumber(driver, projectTaskTempNum);
        getMemberTaskNumber(driver, memberTmepTask,zombieUserName);
        getMemberTaskNumber(driver, associateMemTmepberTask,ciwangUserName);
        assertEquals(memberTmepTask[1], pageTaskTempNum);
        assertEquals(projectTaskBaseNum[0], projectTaskTempNum[0]);
        assertEquals(projectTaskBaseNum[1], projectTaskTempNum[1]);
        assertEquals(projectTaskBaseNum[2] , projectTaskTempNum[2]);
        assertEquals(memberTask[0], memberTmepTask[0]);
        assertEquals(memberTask[1], memberTmepTask[1]);
        assertEquals(associateMemberTask[0], associateMemTmepberTask[0]);
        assertEquals(associateMemberTask[1], associateMemTmepberTask[1]);
    }


    @Test
    public void test1()throws Exception {
        testAllTaskPage();
    }
    @Test
    public void test2()throws Exception {
        testMemberTaskPage();
    }
    @Test
    public void test3()throws Exception {
        testMyTaskDonePage();
    }
    @Test
    public void test4()throws Exception {
        //测试我的任务的“所有任务”页面
        testMyTaskBasePage(0, testBaseUrl+"/user/tasks/all", driver, navigation, associateDriver, associateNavigation);
    }
    @Test
    public void test5()throws Exception {
        //测试我的任务的“正在进行的任务”页面
        testMyTaskBasePage(1, testBaseUrl+"user/tasks/processing", driver, navigation, associateDriver, associateNavigation);
    }
    @Test
    public void test6()throws Exception {
        //测试我的任务的“项目任务”页面
        testMyTaskBasePage(3, testBaseUrl+"user/tasks/project/"+"392"+"/all", driver, navigation, associateDriver, associateNavigation);
    }

    @Test
    public void test7()throws Exception {
        testProcessingAndDonePageTask();
    }

    //@Test
    public void testAllTaskPage() throws Exception
    {

        String taskId[] = new String[3];//获取任务的id
        int projectTaskBaseNum[] = {0,0,0};//原始的项目数
        int projectTaskTempNum[] = {0,0,0};//改变后的项目数
        int pageTaskTempNum = 0;//task框上方显示的改变后的任务数
        //成员的任务数
        int memberTask[]= {0,0};
        int associateMemberTask[]= {0,0};

        //改变后的成员的任务数
        int memberTmepTask[]= {0,0};
        int associateMemTmepberTask[]= {0,0};

        //taskUrl
        String taskUrl =testBaseUrl+ "u/"+projectOwner[0]+"/p/"+ projectName[0]+"/tasks/all";
        System.out.println("测试项目的所有任务模块，url：" + taskUrl);
        System.out.println("测试的主用户账户为：" + zombieUserName);
        System.out.println("测试的辅助账户为：" + ciwangUserName);
        System.out.println("密码均为：123456" );
        System.out.println("测试的项目名为tt1111， 创建者为test5" );
        System.out.println("自动登入" );
        navigation.to(taskUrl);
        Thread.sleep(2000);

        getTaskNumber(driver, projectTaskBaseNum);
        getMemberTaskNumber(driver, memberTask,zombieUserName);
        getMemberTaskNumber(driver, associateMemberTask,ciwangUserName);

        System.out.println("发送两个任务，ciwangUser和zombieUser各一个" );
        pushTask(driver, "taskId[1] associate user's first task", ciwangUserName);
        pushTask(driver, "taskId[0] main user's first task", zombieUserName);
        Thread.sleep(1500);
        pageTaskTempNum = getTaskNumber(driver, projectTaskTempNum);
        getMemberTaskNumber(driver, memberTmepTask,zombieUserName);
        getMemberTaskNumber(driver, associateMemTmepberTask,ciwangUserName);
        System.out.println("测试添加任务后的数值变化" );
        assertEquals(projectTaskTempNum[0], pageTaskTempNum);
        assertEquals(projectTaskBaseNum[0] + 2, projectTaskTempNum[0]);
        assertEquals(projectTaskBaseNum[1] + 2, projectTaskTempNum[1]);
        assertEquals(projectTaskBaseNum[2] , projectTaskTempNum[2]);
        assertEquals(memberTask[0] + 1, memberTmepTask[0]);
        assertEquals(memberTask[1] + 1, memberTmepTask[1]);
        assertEquals(associateMemberTask[0] + 1, associateMemTmepberTask[0]);
        assertEquals(associateMemberTask[1] + 1, associateMemTmepberTask[1]);

        // taskId[0] is main task and taskId[1] is associate task
        taskId[0] = driver.findElement(By.id("task-list")).findElement(By.className("project")).findElements(By.tagName("textarea")).get(0).getAttribute("id");
        taskId[1] = driver.findElement(By.id("task-list")).findElement(By.className("project")).findElements(By.tagName("textarea")).get(1).getAttribute("id");
        System.out.println(taskId[0]);
        System.out.println(taskId[1]);
        //重新指派任务
        System.out.println("将ciwangUser的任务重新指派给zombieUser" );
        resignTask(taskId[1], zombieUserName, driver);
        Thread.sleep(1000);
        pageTaskTempNum = getTaskNumber(driver, projectTaskTempNum);
        getMemberTaskNumber(driver, memberTmepTask,zombieUserName);
        getMemberTaskNumber(driver, associateMemTmepberTask,ciwangUserName);

        assertEquals(projectTaskTempNum[0], pageTaskTempNum);
        assertEquals(projectTaskBaseNum[0] + 2, projectTaskTempNum[0]);
        assertEquals(projectTaskBaseNum[1] + 2, projectTaskTempNum[1]);
        assertEquals(projectTaskBaseNum[2] , projectTaskTempNum[2]);
        assertEquals(memberTask[0] + 2, memberTmepTask[0]);
        assertEquals(memberTask[1] + 2, memberTmepTask[1]);
        assertEquals(associateMemberTask[0], associateMemTmepberTask[0]);
        assertEquals(associateMemberTask[1], associateMemTmepberTask[1]);
        //编辑任务
        System.out.println("测试任务框是否可以编辑 " );
        String originalContent = getTaskContent(taskId[0], driver);
        editTask(taskId[0],"task owner has changed, the new owner ", driver);
        Thread.sleep(1000);
        navigation.to(taskUrl);
        Thread.sleep(1000);
        String editContent = getTaskContent(taskId[0], driver);
        System.out.println("originalContent " + originalContent);
        System.out.println("editContent " + editContent);
        assertNotEquals(originalContent, editContent);
        //完成任务
        checkTask(driver, taskId[1]);
        Thread.sleep(1500);
        //测试已完成的任务是否无法编辑
        System.out.println("测试mianUser已完成的任务无法编辑 " );
        assertFalse(driver.findElement(By.id(taskId[1])).isEnabled());
        pageTaskTempNum = getTaskNumber(driver, projectTaskTempNum);
        getMemberTaskNumber(driver, memberTmepTask,zombieUserName);
        getMemberTaskNumber(driver, associateMemTmepberTask,ciwangUserName);

        assertEquals(projectTaskTempNum[0], pageTaskTempNum);
        assertEquals(projectTaskBaseNum[0] + 2, projectTaskTempNum[0]);
        assertEquals(projectTaskBaseNum[1] + 1, projectTaskTempNum[1]);
        assertEquals(projectTaskBaseNum[2] + 1, projectTaskTempNum[2]);
        assertEquals(memberTask[0] + 1, memberTmepTask[0]);
        assertEquals(memberTask[1] + 2, memberTmepTask[1]);
        assertEquals(associateMemberTask[0], associateMemTmepberTask[0]);
        assertEquals(associateMemberTask[1], associateMemTmepberTask[1]);


        //重新激活任务
        System.out.println("测试mianUser重新开启一个项目 " );
        checkTask(driver, taskId[1]);
        Thread.sleep(500);
        pageTaskTempNum = getTaskNumber(driver, projectTaskTempNum);
        getMemberTaskNumber(driver, memberTmepTask,zombieUserName);
        getMemberTaskNumber(driver, associateMemTmepberTask,ciwangUserName);
        assertEquals(projectTaskTempNum[0], pageTaskTempNum);
        assertEquals(projectTaskBaseNum[0] + 2, projectTaskTempNum[0]);
        assertEquals(projectTaskBaseNum[1] + 2, projectTaskTempNum[1]);
        assertEquals(projectTaskBaseNum[2] , projectTaskTempNum[2]);
        assertEquals(memberTask[0] + 2, memberTmepTask[0]);
        assertEquals(memberTask[1] + 2, memberTmepTask[1]);
        assertEquals(associateMemberTask[0], associateMemTmepberTask[0]);
        assertEquals(associateMemberTask[1], associateMemTmepberTask[1]);

        //完成任务并删除掉任务
        checkTask(driver, taskId[1]);
        Thread.sleep(500);
        removeTask(driver, taskId[1]);
        Thread.sleep(500);
        System.out.println("测试删除一个已完成的任务 " );
        pageTaskTempNum = getTaskNumber(driver, projectTaskTempNum);
        getMemberTaskNumber(driver, memberTmepTask,zombieUserName);
        getMemberTaskNumber(driver, associateMemTmepberTask,ciwangUserName);
        assertEquals(projectTaskTempNum[0], pageTaskTempNum);
        assertEquals(projectTaskBaseNum[0] + 1, projectTaskTempNum[0]);
        assertEquals(projectTaskBaseNum[1] + 1, projectTaskTempNum[1]);
        assertEquals(projectTaskBaseNum[2] , projectTaskTempNum[2]);
        assertEquals(memberTask[0] + 1, memberTmepTask[0]);
        assertEquals(memberTask[1] + 1, memberTmepTask[1]);
        assertEquals(associateMemberTask[0], associateMemTmepberTask[0]);
        assertEquals(associateMemberTask[1], associateMemTmepberTask[1]);


        System.out.println("taskId[0] " + taskId[0]);
        //删除正在进行的任务
        removeTask(driver, taskId[0]);
        Thread.sleep(500);
        System.out.println("测试删除一个正在进行的任务 " );
        pageTaskTempNum = getTaskNumber(driver, projectTaskTempNum);
        getMemberTaskNumber(driver, memberTmepTask,zombieUserName);
        getMemberTaskNumber(driver, associateMemTmepberTask,ciwangUserName);
        assertEquals(projectTaskTempNum[0], pageTaskTempNum);
        assertEquals(projectTaskBaseNum[0], projectTaskTempNum[0]);
        assertEquals(projectTaskBaseNum[1], projectTaskTempNum[1]);
        assertEquals(projectTaskBaseNum[2] , projectTaskTempNum[2]);
        assertEquals(memberTask[0], memberTmepTask[0]);
        assertEquals(memberTask[1], memberTmepTask[1]);
        assertEquals(associateMemberTask[0], associateMemTmepberTask[0]);
        assertEquals(associateMemberTask[1], associateMemTmepberTask[1]);
        System.out.println("已完成测试项目的所有任务模块，url：" + taskUrl);
    }



    //@Test
    public void testProcessingAndDonePageTask() throws Exception {

        String taskId[] = new String[3];//获取任务的id
        int projectTaskBaseNum[] = {0,0,0};//原始的项目数
        int projectTaskTempNum[] = {0,0,0};//改变后的项目数
        int pageTaskBaseNum = 0;//task框上方显示的原来任务数
        int pageTaskTempNum = 0;//task框上方显示的改变后的任务数
        //成员的任务数
        int memberTask[]= {0,0};
        int associateMemberTask[]= {0,0};

        //改变后的成员的任务数
        int memberTmepTask[]= {0,0};
        int associateMemTmepberTask[]= {0,0};

        //任务的改变数
        int numberChange[] = {0, 0,0};

        //taskUrl
        String taskUrl = testBaseUrl +"u/"+projectOwner[0]+"/p/"+projectName[0] +"/tasks/processing";
        System.out.println("测试项目正在进行的任务和已完成任务模块，url：" + taskUrl);
        System.out.println("测试的主用户账户为：" + zombieUserName);
        System.out.println("测试的辅助账户为：" + ciwangUserName);
        System.out.println("密码均为：123456" );
        System.out.println("测试的项目名为tt1111， 创建者为test5" );
        System.out.println("自动登录" );


        System.out.println("自动登入成功， 跳转至项目页面" );
        //test5发送task给zombieUser

        navigation.to(taskUrl);
        Thread.sleep(3000);
        //获得任务数
        pageTaskBaseNum = getTaskNumber(driver, projectTaskBaseNum);
        getMemberTaskNumber(driver, memberTask,zombieUserName);
        getMemberTaskNumber(driver, associateMemberTask,ciwangUserName);
        System.out.println("成功获取项目页面基本参数，正在完成数，成员任务数等" );

        System.out.println("为zombieUser发送两个任务和ciwangUsertian个发送一个任务" );
        pushTask(driver,"task来一个", ciwangUserName);
        Thread.sleep(1000);
        pushTask(driver,"task来一个2", zombieUserName);
        Thread.sleep(1000);
        pushTask(driver,"task来一个3", zombieUserName);
        Thread.sleep(1000);
        //获取task的id
        taskId[2] = driver.findElement(By.id("task-list")).findElement(By.className("project")).findElements(By.tagName("textarea")).get(0).getAttribute("id");
        taskId[1] = driver.findElement(By.id("task-list")).findElement(By.className("project")).findElements(By.tagName("textarea")).get(1).getAttribute("id");
        taskId[0] = driver.findElement(By.id("task-list")).findElement(By.className("project")).findElements(By.tagName("textarea")).get(2).getAttribute("id");
        System.out.println(taskId[2]);
        System.out.println(taskId[1]);
        System.out.println(taskId[0]);


        Thread.sleep(1000);
        pageTaskTempNum = getTaskNumber(driver, projectTaskTempNum);
        getMemberTaskNumber(driver, memberTmepTask,zombieUserName);
        getMemberTaskNumber(driver, associateMemTmepberTask,ciwangUserName);
        System.out.println("测试发送task之后，数值的变化" );

        assertEquals(pageTaskBaseNum + 3, pageTaskTempNum);
        assertEquals(projectTaskBaseNum[0] + 3, projectTaskTempNum[0]);
        assertEquals(projectTaskBaseNum[1] + 3, projectTaskTempNum[1]);

        assertEquals(memberTask[0] + 2, memberTmepTask[0]);
        assertEquals(memberTask[1] + 2, memberTmepTask[1]);
        assertEquals(associateMemberTask[0] + 1, associateMemTmepberTask[0]);
        assertEquals(associateMemberTask[1] + 1, associateMemTmepberTask[1]);

        //ciwang转发task给test5
        associateNavigation.to(taskUrl);
        Thread.sleep(5000);
        resignTask(taskId[0],zombieUserName,associateDriver);
        Thread.sleep(1500);
        System.out.println("将ciwangUser的任务重新指派给zombieUser");
        getMemberTaskNumber(associateDriver, memberTmepTask,zombieUserName);
        getMemberTaskNumber(associateDriver, associateMemTmepberTask,ciwangUserName);

        assertEquals(memberTask[0] + 3, memberTmepTask[0]);
        assertEquals(memberTask[1] + 3, memberTmepTask[1]);
        assertEquals(associateMemberTask[0] , associateMemTmepberTask[0]);
        assertEquals(associateMemberTask[1] , associateMemTmepberTask[1]);

        //test5对任务进行编辑,发送给ciwang，再完成自己的任务，在重新激活，再删掉一个正在进行任务和一个已完成的任务
        navigation.to(testBaseUrl +"u/"+zombieUser.getUserLoginName()+"/p/"+projectName[0]+"/tasks/processing");
        Thread.sleep(5000);
        System.out.println("测试任务框是否可以编辑 " );
        resignTask(taskId[0],ciwangUserName,driver);
        String originalContent  = driver.findElement(By.id(taskId[0])).getAttribute("value");
        String taskContent = ",hell[]{}【】**……%%；‘’“”》《/-》11111111111111111&&&&&&&&……dddddfdjfojpdfpao打飞机啊都浪费精力说的克己复礼空间……………RRRRRRRR";
        editTask(taskId[0],taskContent, driver);
        Thread.sleep(4000);
        navigation.to(taskUrl);
        Thread.sleep(5000);
        String editContent = driver.findElement(By.id(taskId[0])).getAttribute("value");
        assertNotEquals(originalContent, editContent);
        System.out.println("测试完成两个任务 taskId[1] " + taskId[1] +", taskId[2] " + taskId[2]);
        checkTask(driver, taskId[1]);
        Thread.sleep(1000);
        checkTask(driver, taskId[2]);
        Thread.sleep(1000);

        pageTaskTempNum = getTaskNumber(driver, projectTaskTempNum);
        getMemberTaskNumber(driver, memberTmepTask,zombieUserName);
        getMemberTaskNumber(driver, associateMemTmepberTask,ciwangUserName);

        //完成任务后的数字变化
        numberChange[0] = 3 ;
        numberChange[1] = 1 ;
        numberChange[2] = 2 ;
        assertEquals(pageTaskBaseNum + numberChange[1], pageTaskTempNum);
        verifyPageProjectNum(projectTaskBaseNum, projectTaskTempNum, numberChange);


        assertEquals(memberTask[0] , memberTmepTask[0]);
        assertEquals(memberTask[1] + 2, memberTmepTask[1]);
        assertEquals(associateMemberTask[0] + 1 , associateMemTmepberTask[0]);
        assertEquals(associateMemberTask[1] + 1, associateMemTmepberTask[1]);
        //再次激活test5的任务
        checkTask(driver, taskId[1]);
        System.out.println("测试完成一个任务 " );
        Thread.sleep(1000);
        pageTaskTempNum = getTaskNumber(driver, projectTaskTempNum);
        getMemberTaskNumber(driver, memberTmepTask,zombieUserName);
        getMemberTaskNumber(driver, associateMemTmepberTask,ciwangUserName);
        numberChange[0] = 3 ;
        numberChange[1] = 2 ;
        numberChange[2] = 1 ;
        assertEquals(pageTaskBaseNum + numberChange[1], pageTaskTempNum);
        verifyPageProjectNum(projectTaskBaseNum, projectTaskTempNum, numberChange);
        assertEquals(memberTask[0] + 1, memberTmepTask[0]);
        assertEquals(memberTask[1] + 2, memberTmepTask[1]);
        assertEquals(associateMemberTask[0] + 1 , associateMemTmepberTask[0]);
        assertEquals(associateMemberTask[1] + 1, associateMemTmepberTask[1]);

        //让test5的任务完成，再删除掉
        checkTask(driver, taskId[1]);
        Thread.sleep(1000);
        //测试已完成的任务是否无法编辑
        System.out.println("测试mianUser已完成的任务无法编辑 " );
        assertFalse(driver.findElement(By.id(taskId[1])).isEnabled());
        Thread.sleep(1000);
        System.out.println("测试删除一个已完成的任务 " );
        checkTask(driver, taskId[1]);
        Thread.sleep(1000);
        removeTask(driver, taskId[1]);
        Thread.sleep(1000);
        pageTaskTempNum = getTaskNumber(driver, projectTaskTempNum);
        getMemberTaskNumber(driver, memberTmepTask,zombieUserName);
        getMemberTaskNumber(driver, associateMemTmepberTask,ciwangUserName);

        assertEquals(pageTaskBaseNum + 1, pageTaskTempNum);
        assertEquals(projectTaskBaseNum[0] + 2, projectTaskTempNum[0]);
        assertEquals(projectTaskBaseNum[1] + 1, projectTaskTempNum[1]);
        assertEquals(projectTaskBaseNum[2] + 1, projectTaskTempNum[2]);
        assertEquals(memberTask[0] , memberTmepTask[0]);
        assertEquals(memberTask[1] + 1, memberTmepTask[1]);

        //创建一个任务，删除一个正在进行的任务
        pushTask(driver,"task来一个", ciwangUserName);
        Thread.sleep(1000);
        taskId[1] = driver.findElement(By.id("task-list")).findElements(By.className("project")).get(0).findElements(By.tagName("textarea")).get(0).getAttribute("id");
        System.out.println("remove task " +  taskId[1]);
        checkTask(driver, taskId[1]);//删除前是否要触发一下？？？
        Thread.sleep(1000);
        removeTask(driver, taskId[1]);
        Thread.sleep(1000);
        System.out.println("测试删除一个正在进行的任务 " );
        pageTaskTempNum = getTaskNumber(driver, projectTaskTempNum);
        getMemberTaskNumber(driver, memberTmepTask,zombieUserName);
        getMemberTaskNumber(driver, associateMemTmepberTask,ciwangUserName);

        assertEquals(pageTaskBaseNum + 1, pageTaskTempNum);
        assertEquals(projectTaskBaseNum[0] + 2, projectTaskTempNum[0]);
        assertEquals(projectTaskBaseNum[1] + 1, projectTaskTempNum[1]);
        assertEquals(projectTaskBaseNum[2] + 1, projectTaskTempNum[2]);
        assertEquals(memberTask[0] , memberTmepTask[0]);
        assertEquals(memberTask[1] + 1, memberTmepTask[1]);

        //ciwang对任务完成，重新激活，删除，再删掉一个已完成的任务的任务
        System.out.println("测试项目的已完成任务模块，url：" + testBaseUrl + "u/test5/p/tt1111/tasks/done");
        associateNavigation.to(testBaseUrl + "u/"+projectOwner[0]+"/p/"+  projectName[0]+"/tasks/processing");
        Thread.sleep(5000);
        checkTask(associateDriver, taskId[0]);//完成任务
        Thread.sleep(1000);
        associateNavigation.to( testBaseUrl + "u/"+projectOwner[0]+"/p/"+projectName[0]+"/tasks/done");
        Thread.sleep(5000);
        checkTask(associateDriver, taskId[0]);
        Thread.sleep(2000);
        pageTaskTempNum = getTaskNumber(associateDriver, projectTaskTempNum);
        getMemberTaskNumber(driver, memberTmepTask,zombieUserName);
        getMemberTaskNumber(driver, associateMemTmepberTask,ciwangUserName);
        System.out.println("projectTaskTempNum[2] " + projectTaskTempNum[2]);
        System.out.println("projectTaskTempNum[1] " + projectTaskTempNum[1]);
        System.out.println("projectTaskTempNum[0] " + projectTaskTempNum[0]);
        System.out.println("pageTaskTempNum " + pageTaskTempNum);
        assertEquals(projectTaskTempNum[2], pageTaskTempNum);
        assertEquals(projectTaskBaseNum[0] + 2, projectTaskTempNum[0]);
        assertEquals(projectTaskBaseNum[1] + 1, projectTaskTempNum[1]);
        assertEquals(projectTaskBaseNum[2] + 1, projectTaskTempNum[2]);
        assertEquals(associateMemberTask[0] + 1, associateMemTmepberTask[0]);
        assertEquals(associateMemberTask[1] + 1, associateMemTmepberTask[1]);

        //删除一个重新激活的任务
        System.out.println("测试删除一个正在进行的任务 " );
        checkTask(driver, taskId[0]);
        Thread.sleep(1000);
        removeTask(associateDriver, taskId[0]);
        Thread.sleep(1000);
        pageTaskTempNum = getTaskNumber(associateDriver, projectTaskTempNum);
        getMemberTaskNumber(associateDriver, memberTmepTask,zombieUserName);
        getMemberTaskNumber(associateDriver, associateMemTmepberTask,ciwangUserName);
        assertEquals(projectTaskTempNum[2], pageTaskTempNum);
        assertEquals(projectTaskBaseNum[0] + 1, projectTaskTempNum[0]);
        assertEquals(projectTaskBaseNum[1] , projectTaskTempNum[1]);
        assertEquals(projectTaskBaseNum[2] + 1, projectTaskTempNum[2]);
        assertEquals(associateMemberTask[0], associateMemTmepberTask[0]);
        assertEquals(associateMemberTask[1], associateMemTmepberTask[1]);


        //删除一个已完成的test5的任务
        System.out.println("测试删除一个已完成的任务 " );
        checkTask(driver, taskId[2]);
        Thread.sleep(1000);
        checkTask(driver, taskId[2]);
        Thread.sleep(1000);
        removeTask(associateDriver, taskId[2]);
        Thread.sleep(1000);
        pageTaskTempNum = getTaskNumber(associateDriver, projectTaskTempNum);
        getMemberTaskNumber(associateDriver, memberTmepTask,zombieUserName);
        getMemberTaskNumber(associateDriver, associateMemTmepberTask,ciwangUserName);
        assertEquals(projectTaskTempNum[2], pageTaskTempNum);
        assertEquals(projectTaskBaseNum[0] , projectTaskTempNum[0]);
        assertEquals(projectTaskBaseNum[1], projectTaskTempNum[1]);
        assertEquals(projectTaskBaseNum[2], projectTaskTempNum[2]);
        assertEquals(associateMemberTask[0], associateMemTmepberTask[0]);
        assertEquals(associateMemberTask[1], associateMemTmepberTask[1]);
        System.out.println("已完成测试项目正在进行的任务和已完成任务模块，url：" + taskUrl);

    }




    public void verifyPageProjectNum( int projectTaskBaseNum[], int projectTaskTempNum[], int numberChange[]){
        assertEquals(projectTaskBaseNum[0] + numberChange[0], projectTaskTempNum[0]);
        assertEquals(projectTaskBaseNum[1] + numberChange[1], projectTaskTempNum[1]);
        assertEquals(projectTaskBaseNum[2] + numberChange[2], projectTaskTempNum[2]);
    }

    public void removeTask(WebDriver driver, String taskId) throws Exception{
        System.out.println("start remove ");
        List<WebElement> removeTaskList = driver.findElement(By.id("task-list")).findElements(By.className("project")).get(0).findElements(By.className("task"));

        for(int i = 0 ;i < removeTaskList.size(); i++)
        {
            if(removeTaskList.get(i).findElement(By.tagName("textarea")).getAttribute("id").equals(taskId))
            {
                removeTaskList.get(i).click();
                driver.findElement(By.id("task-list")).findElements(By.className("project")).get(0).findElements(By.className("task")).get(i).findElement(By.className("remove-task")).click();
                assertEquals(driver.switchTo().alert().getText(), "确认删除该任务？");
                driver.switchTo().alert().accept();
                break;
            }
        }
        System.out.println("finish remove ");
    }
    //todolist edit 改回来
    public void editTask(String taskId, String taskContent, WebDriver driver) throws InterruptedException
    {
        System.out.println("start edit " + taskId);
        Thread.sleep(500);
        List<WebElement> memberPicker = driver.findElement(By.id("task-list")).findElements(By.className("project")).get(0).findElements(By.className("task"));
        //System.out.println(memberPicker.size());
        //System.out.println("taskId is " + taskId);
        for(int i = 0; i < memberPicker.size(); i++)
        {
            System.out.println("id is " + memberPicker.get(i).findElement(By.tagName("textarea")).getAttribute("id"));
            if(memberPicker.get(i).findElement(By.tagName("textarea")).getAttribute("id").equals(taskId))
            {
                //System.out.println("id is " + memberPicker.get(i).findElement(By.tagName("textarea")).getAttribute("id"));
                memberPicker.get(i).findElement(By.tagName("textarea")).clear();
                memberPicker.get(i).findElement(By.tagName("textarea")).sendKeys(taskContent);
                driver.findElement(By.id("task-list")).findElements(By.className("project")).get(0).findElements(By.className("task")).get(i).click();

                System.out.println("success edit "+taskId);
                break;
            }
        }

    }

    public String getTaskContent(String taskId,  WebDriver driver ){
        System.out.println("start get task Content" + taskId);
        String text = driver.findElement(By.id(taskId)).getAttribute("value");

        System.out.println("success get task Content " + taskId);
        return text;
    }


    public void checkTask(WebDriver driver, String taskId){
        System.out.println("start check" + taskId);
        //List<WebElement> taskCheckBoxList = driver.findElement(By.id("task-list")).findElements(By.className("project")).get(0).findElements(By.className("task"));
        List<WebElement> taskCheckBoxList = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("task")));
        //List<WebElement> taskCheckBoxList = taskList.findElements(By.className("project")).get(0).findElements(By.className("task"));
        WebElement taskCheckBox;
        //System.out.println(taskCheckBoxList.size());
        for(int i = 0 ;i < taskCheckBoxList.size(); i++)
        {
            //System.out.println(taskCheckBoxList.get(i).findElement(By.tagName("textarea")).getAttribute("id"));

            if(taskCheckBoxList.get(i).findElement(By.tagName("textarea")).getAttribute("id").equals(taskId))
            {
                //System.out.println("found");
                //System.out.print(taskCheckBoxList.get(i).findElement(By.className("check-icon")).getAttribute("ng-click"));
                taskCheckBox = taskCheckBoxList.get(i).findElement(By.className("check-icon"));
                taskCheckBox.click();
                System.out.println("finish check "+taskId);
                return;
            }

        }
        System.out.println("fail check "+taskId);
    }


    public  void resignTask(String taskId, String receiver, WebDriver driver) throws Exception{
        System.out.println("star resign Task");
        Thread.sleep(1000);
        List<WebElement> memberPicker = driver.findElement(By.id("task-list")).findElements(By.className("project")).get(0).findElements(By.className("task"));
        //System.out.println(memberPicker.size());
        //System.out.println("taskId is " + taskId);
        for(int i = 0; i < memberPicker.size(); i++)
        {
            //System.out.println("id is " + memberPicker.get(i).findElement(By.tagName("textarea")).getAttribute("id"));

            if(memberPicker.get(i).findElement(By.tagName("textarea")).getAttribute("id").equals(taskId))
            {
                //System.out.println("id is " + memberPicker.get(i).findElement(By.tagName("textarea")).getAttribute("id"));
                //System.out.println("img click " + memberPicker.get(i).findElement(By.tagName("img")).getAttribute("title"));
                memberPicker.get(i).findElement(By.tagName("img")).click();
                WebElement input = memberPicker.get(i).findElements(By.tagName("input")).get(1);
                //System.out.println(input.getTagName());
                input.sendKeys(receiver);
                memberPicker.get(i).findElement(By.className("members-outer")).findElement(By.tagName("img")).click();
                System.out.println("success resign Task");
                break;

            }
        }


    }




    public int getTaskNumber(WebDriver driver, int projectTaskNum[])
    {
        System.out.println("star get task number");
        String tempString = "";
        Matcher mat;
        WebElement taskList = driver.findElement(By.id("inner-menu"));
        String regEx = "[^0-9]|/";
        Pattern pat = Pattern.compile(regEx);
        for(int i = 0; i<3; i++)
        {
            tempString = taskList.findElements(By.tagName("a")).get(i).getText();
            mat = pat.matcher(tempString);
            // System.out.println("我的任务数值" + tempString);
            projectTaskNum[i] = Integer.parseInt(mat.replaceAll("").trim());
        }
        tempString = driver.findElement(By.id("task-list")).findElements(By.tagName("div")).get(0).getText();

        String tel[] = tempString.split("（");
        mat = pat.matcher(tel[1]);
        System.out.println("success get task number");
        return Integer.parseInt(mat.replaceAll("").trim());



    }

    public void getMemberTaskNumber(WebDriver driver, int memberTask[],String memberName) throws Exception{
        System.out.println("Start get Member Task Number");
        List<WebElement> taskMemberList = driver.findElement(By.id("members")).findElements(By.tagName("a"));
        WebElement taskMember;
        String tempString = "";
        String tempMemberName = "";
        String num[] = new String[2];
        String regEx2 = "/";
        Pattern pat2 = Pattern.compile(regEx2);
        // System.out.println("dsfd " + taskMemberList.size());
        for(int i = 0; i<taskMemberList.size();i++)
        {
            taskMember = taskMemberList.get(i).findElements(By.tagName("div")).get(1);
            tempMemberName = taskMember.getText();
            tempString = taskMember.findElement(By.tagName("span")).getText();
            tempMemberName = tempMemberName.replace(tempString, "").trim();
            //System.out.println("name is " + tempMemberName);
            //System.out.println("memberName is " + memberName);
            if( tempMemberName.equals(memberName))
            {
                //System.out.println("found ");
                System.out.println("success get Member Task Number");
                num = pat2.split(tempString);
                memberTask[0] = Integer.parseInt(num[0]);//正在进行任务数
                memberTask[1] = Integer.parseInt(num[1]);//任务总数
                break;
            }

        }

    }

    public  void pushTask(WebDriver driver, String taskText, String taskResponser) throws Exception {
        System.out.println("start push task");
        WebElement taskInput = driver.findElement(By.id("main-task-input"));
        Thread.sleep(1000);
        taskInput.sendKeys(taskText);
        WebElement img = driver.findElement(By.tagName("form")).findElement(By.className("member-picker")).findElement(By.tagName("img"));
        Thread.sleep(1000);
        img.click();
        WebElement input = driver.findElement(By.tagName("form")).findElement(By.className("member-picker")).findElements(By.tagName("input")).get(1);
        //System.out.println(input.getTagName());
        input.sendKeys(taskResponser);
        Thread.sleep(300);
        driver.findElement(By.tagName("form")).findElement(By.className("member-picker")).findElement(By.className("members-outer")).findElement(By.tagName("img")).click();
        input.clear();
        driver.findElement(By.tagName("form")).findElement(By.tagName("div")).findElement(By.tagName("i")).click();
        System.out.println("success push task");

    }

    @After
    public void tearDown() throws Exception {
       driverBase.quit();
       associateDriverBase.quit();
    }

}

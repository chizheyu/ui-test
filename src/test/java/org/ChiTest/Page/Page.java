package org.ChiTest.Page;

import org.ChiTest.InterFace.ItemEntry;
import org.ChiTest.InterFace.ItemFinder;
import org.ChiTest.LinksMap;
import org.ChiTest.PageLink;
import org.ChiTest.User.User;
import org.ChiTest.WaitTool;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import reference.ConfigFileReader;

import javax.naming.directory.NoSuchAttributeException;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class Page {

    private  Actions builder;
    private String pageUrl;

    public String getBaseUrl() {
        return baseUrl;
    }
    public ConfigFileReader configFileReader;

    private String baseUrl;
	private EventFiringWebDriver driver;
    private WaitTool waitTool;
    private Logger log = Logger.getLogger("screen");
    private static LinksMap linksMap;

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public Page(String url, EventFiringWebDriver driver, String baseUrl ){
		this.pageUrl = url;
		this.driver = driver;
        this.waitTool = new WaitTool();
        this.waitTool.setDriver(this.driver);
        this.builder = new Actions(this.driver);
        this.baseUrl = baseUrl;
        this.linksMap = LinksMap.getInstance();
        this.configFileReader = new ConfigFileReader();
	}
    public Page(String url, EventFiringWebDriver driver){
        this.pageUrl = url;
        this.driver = driver;
        this.waitTool = new WaitTool();
        this.waitTool.setDriver(this.driver);
        this.builder = new Actions(this.driver);
        this.configFileReader = new ConfigFileReader();
    }

    public Actions getBuilder() {
        return builder;
    }
    public Actions updateBuilder() {
        builder.release();
        builder = new Actions(this.driver);
        return builder;
    }
    public String getImgStoreUrl() {
        return "https://dn-coding-net-tweet.qbox.me";
    }
    public String getImgPreviewUrl() {
        return this.getBaseUrl() + "api/";
    }
    public String getProjectImgStoreUrl() {
        return "https://dn-coding-net-project-icon.qbox.me";
    }
    public String getDocumentImgStoreUrl() {
        return "https://dn-coding-net-test-self.qbox.me/";
    }
    public WaitTool getWaitTool() {
        return waitTool;
    }
    public String getMark() {
        return (new Date()).getTime() / (100) + "";
    }

    public String getCurrentUrl(){
        return driver.getCurrentUrl();
    }
	public EventFiringWebDriver getDriver() {
		return driver;
	}
    public boolean isChromeDriver(){
        return this.getDriver().getWrappedDriver().getClass().getName().toLowerCase().contains("chrome");
    }
    public boolean isPhantomJs(){
        return this.getDriver().getWrappedDriver().getClass().getName().toLowerCase().contains("phantom");
    }
    public static final int DEFAULT_WAIT_4_PAGE = 5;
	public void setDriver(EventFiringWebDriver driver) {
		this.driver = driver;
	}
	public String getUrl() {
        return pageUrl;
    }
    public String getBubblingUrl() {
        return baseUrl + "pp";
    }
    public String getUserCenterUrl() {
        return baseUrl + "user";
    }
    public String getHotBubblingUrl() {
        return baseUrl + "pp/hot";
    }
    public String getFriendsUrl() {
        return  baseUrl + "user/relationship/friends";
    }
    public String getRelationCenterUrl() {
        return  baseUrl + "user/relationship";
    }
    public String getFansUrl() {
        return  baseUrl + "user/relationship/fans";
    }
    public String getProjectUrl() {
        return  baseUrl + "user/projects/all";
    }
    public String getCreatedProjectUrl() {
        return  baseUrl + "user/projects/created";
    }
    public String getPageHeader(){
        return this.getText(".ui.dividing.header");
    }
    public void backToTop(){
        this.executeScript("window.scrollTo(0,0)");
    }
    public int getScrollY(){
        return ((Long) this.executeScript("return window.scrollY")).intValue();
    }
    public void navigate(String url)   {
        try {
            if (!this.driver.getCurrentUrl().equals(url)) {
                this.driver.navigate().to(url);
            }

        }
        catch (TimeoutException e){
            System.out.println("page " + url + "load timeout");
        }
    }
    public Cookie getStagingSid(String sidValue) throws ParseException {
        return new Cookie("sid", sidValue, "staging.coding.net","/",
                (new SimpleDateFormat("yyyy-MM-dd")).parse("2017-08-09"));
    }
    public boolean navigate(String url, String cssSelectorForWaitingElement)  {
        try {
            if (!this.driver.getCurrentUrl().equals(url)) {
                this.driver.navigate().to(url);
                if(this.getWaitTool().waitForElement(By.cssSelector(cssSelectorForWaitingElement),PS.longWait) == null){
                    System.out.println("页面：" + url + "过了10秒都没刷出来或未到指定页面");
                    return false;
                }
            }
            if(!this.elementIsPresent(cssSelectorForWaitingElement,2)){
                this.refresh(cssSelectorForWaitingElement);
            }
        }
        catch (TimeoutException e){
            System.out.println("page " + url + "load timeout");
            return false;
        }
        catch (ElementNotVisibleException e){
            System.out.println("can not find " + cssSelectorForWaitingElement);
            return false;
        }
        return true;
    }
    public boolean navigateContiansUrl(String url, String cssSelectorForWaitingElement)  {
        try {
            if (!(this.driver.getCurrentUrl().equals(url)||this.driver.getCurrentUrl().contains(url))) {
                this.driver.navigate().to(url);
                if(this.getWaitTool().waitForElement(By.cssSelector(cssSelectorForWaitingElement),PS.longWait) == null){
                    System.out.println("页面：" + url + "过了10秒都没刷出来或未到指定页面");
                    return false;
                }
            }
            if(!this.elementIsPresent(cssSelectorForWaitingElement,2)){
                this.refresh(cssSelectorForWaitingElement);
            }
        }
        catch (TimeoutException e){
            System.out.println("page " + url + "load timeout");
            return false;
        }
        catch (ElementNotVisibleException e){
            System.out.println("can not find " + cssSelectorForWaitingElement);
            return false;
        }
        return true;
    }
    public void sendInfoWithKeyboard(){
        getBuilder().sendKeys(Keys.CONTROL).sendKeys(Keys.ENTER).perform();
        getBuilder().sendKeys(Keys.CONTROL).sendKeys(Keys.ENTER).release();
        //释放鼠标后，第一次进行安检操作时，莫名出现 js 错误
        try {
            sendKeys(Keys.ENTER);
        }catch (WebDriverException e){
            System.out.println("control released");
        }
    }

    public boolean waitForAttributeChange(final String cssSelectorForContent,final String attributeName,
                                          final String verifyInfo, final int itemNum,int timeOutInSeconds){
        return this.getWaitTool().waitForAttributeChange(cssSelectorForContent, attributeName, verifyInfo, itemNum, timeOutInSeconds);
    }
    public void refresh(String url, String cssSelectorForWaitingElement) {
        try {
            if (!this.driver.getCurrentUrl().equals(url)) {
                this.driver.navigate().to(url);

            }else{
                this.driver.navigate().refresh();
            }
            if(null == this.getWaitTool().waitForElement(By.cssSelector(cssSelectorForWaitingElement), 30)){
                assertEquals(" can not wait element "+ cssSelectorForWaitingElement +" page " +"load timeout or did not navigate to target file",1,2);
            }
        }
        catch (TimeoutException e){
            System.out.println("page " + url +"load timeout");
        }
    }
    public void refreshWithoutWait(String url) {
        try {
            if (!this.driver.getCurrentUrl().equals(url)) {
                this.driver.navigate().to(url);

            }else{
                this.driver.navigate().refresh();
            }
        }
        catch (TimeoutException e){
            System.out.println("page " + url +"load timeout");
        }
    }
    public void refresh( String cssSelectorForWaitingElement)  {
        try {
                this.driver.navigate().refresh();
                if(null == this.getWaitTool().waitForElement(By.cssSelector(cssSelectorForWaitingElement), 5)){
                    System.out.println(" can not wait element "+ cssSelectorForWaitingElement +" page " +"load timeout or did not navigate to target file");
                }
        }
        catch (TimeoutException e){
            System.out.println("page " +"load timeout");
        }
    }
    public void refresh( )  {
        try {
            this.driver.navigate().refresh();
        }
        catch (TimeoutException e){
            System.out.println("page " +"load timeout");
        }
    }
    public void back(String cssSelectorForWaitingElement) {
        try {
            this.driver.navigate().back();
            if(null == this.getWaitTool().waitForElement(By.cssSelector(cssSelectorForWaitingElement), 5)){
                log.error(" can not wait element "+ cssSelectorForWaitingElement +" page " +"load timeout or did not navigate to target file");
            }
        }
        catch (TimeoutException e){
            System.out.println("page " +"load timeout");
        }
    }
    public void moveToElement(String cssSelectorForTargetElement){
        this.getBuilder().moveToElement(this.getElement(cssSelectorForTargetElement)).perform();
    }
    public void moveToElement(String cssSelectorForTargetElement,int num){
        this.getBuilder().moveToElement(this.getElement(cssSelectorForTargetElement,num)).perform();
    }
    public Object executeScript(String jsString){
        return this.driver.executeScript(jsString);
    }
    public void clearInput(String cssSector){
        try{
            this.driver.findElement(By.cssSelector(cssSector)).clear();
        }
        catch (ElementNotVisibleException e){
            System.out.println("this element "+ cssSector + " can not visible" );
        }
        catch (NoSuchElementException e){
            System.out.println("can not find the element " + cssSector);
        }
    }
    public void assertIconAndText(String message, String elementSelector, String expectText, String iconSelector  ){
        assertIconAndText(message,elementSelector,  expectText, iconSelector, 0);
    }
    public void assertIconAndText(String message, String elementSelector, String expectText, String iconSelector,int itemNum  ){
        assertTrue(message + "旁边的 icon 没了",  this.elementIsPresent(elementSelector + " .icon." + iconSelector,itemNum,7));
        assertEquals(message + "文本出错", expectText , this.getText(elementSelector,itemNum));
    }
    public void assertIconAndTextAndLink(String message, String expectText, String iconSelector, String expectLink , String elementSelector){
        assertTrue(message + "旁边的 icon 没了",  this.elementIsPresent(elementSelector + " .icon." + iconSelector,5));
        assertEquals(message + "文本出错", expectText , this.getText(elementSelector));
        assertEquals(message + "链接出错", expectLink , this.getLink(elementSelector));
    }
    public void assertTextNotContainWords(String message, String containWords,String elementSelector){
        assertTextNotContainWords( message,  containWords, elementSelector, 0);
    }
    public void assertTextNotContainWords(String message, String containWords,String elementSelector, int elementNum){
        assertFalse(message + "没有包含对应字符串: "+ containWords +" 实际的字符串为: "+ this.getText(elementSelector,elementNum),
                this.getText(elementSelector,elementNum).trim().contains(containWords) );
    }
    public void assertTextContainWords(String message, String containWords,String elementSelector){
        assertTrue(message + "没有包含对应字符串: "+ containWords +" 实际的字符串为: "+ this.getText(elementSelector),
                this.getText(elementSelector).trim().contains(containWords) );
    }
    public void assertTextContainWords(String message, String elementSelector,int elementNum, String containWords ){
        assertTrue(message + "没有包含对应字符串: "+ containWords +" 实际的字符串为: "+ this.getText(elementSelector,elementNum),
                this.getText(elementSelector,elementNum).trim().contains(containWords) );
    }
    public void assertAttributeContainWords(String message, String containWords,String elementSelector, String attribute){
        assertTrue(message + "的属性"+ attribute +"没有包含对应字符串: "+ containWords +" 实际的字符串为: "+ this.getAttribute(elementSelector,attribute),
                this.getAttribute(elementSelector,attribute).contains(containWords) );
    }
    public void assertAttributeEquals(String message, String equalWords,String elementSelector, String attribute){
        this.assertAttributeEquals( message,  equalWords, elementSelector, 0, attribute);
    }
    public void assertAttributeNotEquals(String message, String equalWords,String elementSelector, String attribute){
        assertFalse(message + "的属性"+ attribute +"没有包含对应字符串: "+ equalWords +" 实际的字符串为: "+ this.getAttribute(elementSelector,attribute),
                this.getAttribute(elementSelector,attribute).equals(equalWords) );
    }
    public void assertAttributeEquals(String message, String equalWords,String elementSelector,int elementNum, String attribute){
        assertTrue(message + "的属性" + attribute + "没有包含对应字符串: " + equalWords + " 实际的字符串为: " + this.getAttribute(elementSelector, attribute, elementNum).trim(),
                this.getAttribute(elementSelector, attribute, elementNum).trim().equals(equalWords));
    }
    public void assertAttributeContainWords(String message, String containWords,String elementSelector, int elementNum, String attribute){
        assertTrue(message + "的属性"+ attribute + "没有包含对应字符串: "+ containWords +" 实际的字符串为: "+ this.getAttribute(elementSelector,attribute,elementNum),
                this.getAttribute(elementSelector, attribute, elementNum).contains(containWords) );
    }
    public void assertElementPresent(String message,String elementSelector, int timeOut ){
        assertTrue(message + "没有出现", this.elementIsPresent(elementSelector, timeOut));
    }
    public void assertElementPresent(String message,String elementSelector,String childSelector ){
        assertTrue(message + "没有出现", this.elementIsPresent(elementSelector, childSelector ));
    }
    public void assertElementPresent(String message,String elementSelector,int eleNum, String childElementSelector ,int timeoutsecond){
        assertTrue(message + "没有出现", this.elementIsPresent(elementSelector,eleNum, childElementSelector,timeoutsecond));
    }
    public void assertElementPresent(String message,String elementSelector,int eleNum, String childElementSelector ){
        assertTrue(message + "没有出现", this.elementIsPresent(elementSelector,eleNum, childElementSelector,5
        ));
    }
    public void assertElementPresent(String message,String elementSelector ){
        assertTrue(message + "没有出现", this.elementIsPresent(elementSelector, PS.midWait));
    }
    public void assertElementNotPresent(String message,String elementSelector ){
        assertFalse(message + "却出现了", this.elementIsPresent(elementSelector, PS.shortWait));
    }
    public void assertElementNotPresent(String message,String elementSelector,int eleNum, String childElementSelector ){
        assertFalse(message + "却出现了", this.elementIsPresent(elementSelector, eleNum, childElementSelector,  PS.shortWait));
    }
    public void assertElementNotPresent(String message,String elementSelector,int eleNum){
        assertFalse(message + "却出现了", this.elementIsPresent(elementSelector, eleNum,  PS.shortWait));
    }
    public void assertElementNotPresent(String message,String elementSelector,String childElementSelector){
        assertFalse(message + "却出现了", this.elementIsPresent(elementSelector, 0, childElementSelector,  PS.shortWait));
    }
    public void waitForContentNotNull(String cssSelectorForContent, int num, int timeoutSeconds){
        this.getWaitTool().waitForContentNotNull(cssSelectorForContent, num, timeoutSeconds);
    }
    public boolean waitForContentNotNull(String cssSelectorForContent, int timeoutSeconds){
        return this.getWaitTool().waitForContentNotNull(cssSelectorForContent, timeoutSeconds);
    }
    public boolean waitForContentChange(String cssSelectorForContent, String verifyInfo,int timeoutSeconds){
        return waitForContentChange(cssSelectorForContent,0 ,verifyInfo, timeoutSeconds);
    }
    public boolean waitForContentChange(String cssSelectorForContent,int textNum, String verifyInfo,int timeoutSeconds){
        return this.getWaitTool().waitForContentChange(cssSelectorForContent, textNum,verifyInfo, timeoutSeconds);
    }
    public boolean waitForAttributeChange(String cssSelectorForContent,String attribute, String verifyInof,int timeoutSeconds){
        return this.getWaitTool().waitForAttributeChange(cssSelectorForContent,attribute,verifyInof, timeoutSeconds);
    }
    public void assertLinkEquals(String message, String expectLink, String elementSelector){
        assertEquals(message+"[链接不正确]", expectLink,       this.getLink(elementSelector));
    }
    public void assertLinkAndTextEquals(String message,String expectText, String expectLink, String elementSelector){
        assertLinkAndTextEquals(message, expectText,expectLink, elementSelector,0 );
    }
    public void assertLinkAndTextEquals(String message,String expectText, String expectLink, String elementSelector, int eleNum){
        assertEquals(message+"[链接不正确]", expectLink,       this.getLink(elementSelector, eleNum));
        assertEquals(message+"[链接文本不正确]", expectText,       this.getText(elementSelector,eleNum));
    }
    public String getPagingSelector(){
        return ".cg .page";
    }
    public void assertTextEquals(String message, String expectText, String elementSelector){
        assertEquals(message+"[文本不正确]", expectText,this.getText(elementSelector));
    }
    public void assertTextEquals(String message, String expectText, String elementSelector,int eleNum){
        assertEquals(message+"[文本不正确]", expectText,this.getText(elementSelector, eleNum));
    }
    public void assertTextEqualsOneFromTwoExpect(String message, String expectText,String expectTextTwo, String elementSelector,int eleNum){
        assertTrue(message+"[文本不正确] expect: "+expectText +" actually: "+ this.getText(elementSelector, eleNum), expectText.equals(this.getText(elementSelector, eleNum))
                || expectTextTwo.equals(this.getText(elementSelector, eleNum)));
    }
    public void assertTextEqualsOneFromThreeExpect(String message, String expectText,String expectTextTwo,String expectTextThree, String elementSelector,int eleNum){
        assertTrue(message+"[文本不正确] expect: "+expectText +" actually: "+ this.getText(elementSelector, eleNum),
                expectText.equals(this.getText(elementSelector, eleNum))
                || expectTextTwo.equals(this.getText(elementSelector, eleNum)) || expectTextThree.equals(this.getText(elementSelector, eleNum)));
    }
    public void assertTextContainsOneFromTwoExpect(String message, String expectText,String expectTextTwo, String elementSelector,int eleNum){
        assertTrue(message+"[文本不正确] expect: "+expectText +" actually: "+ this.getText(elementSelector, eleNum), this.getText(elementSelector, eleNum).contains(expectText)
                || this.getText(elementSelector, eleNum).contains(expectTextTwo));
    }
    public void assertTextContainsOneFromThreeExpect(String message, String expectText,String expectTextTwo, String expectTextThree,String elementSelector,int eleNum){
        assertTrue(message+"[文本不正确] expect: "+expectText +" actually: "+ this.getText(elementSelector, eleNum), this.getText(elementSelector, eleNum).contains(expectText)
                || this.getText(elementSelector, eleNum).contains(expectTextTwo)
                || this.getText(elementSelector, eleNum).contains(expectTextThree));
    }
    public void assertTextNotEquals(String message, String expectText, String elementSelector){
        assertNotEquals(message + "[文本不正确]", expectText, this.getText(elementSelector));
    }
    public void assertTextNotEquals(String message, String expectText, String elementSelector, int num){
        assertNotEquals(message + "[文本不正确]", expectText, this.getText(elementSelector,num));
    }

    public void sendKeys(CharSequence key){
        this.getBuilder().sendKeys(key).perform();
    }
    public void assertCountEquals(String message, int expectCount, String elementSelector){
        assertEquals(message, expectCount,
                this.getElementCount(elementSelector) );
    }
    public void assertNumInTextEquals(String message, int expectCount, String elementSelector){
        assertNumInTextEquals(message, expectCount, elementSelector, 0);
    }
    public void assertNumInTextEquals(String message, int expectCount, String elementSelector,int eleNum){
        assertEquals(message, expectCount,
                this.getPageNum(elementSelector,eleNum) );
    }
    public boolean waitForItemCountChange(String cssSelectorForItem, int originalCount, int timeOutSeconds ){
        if(! this.getWaitTool().waitForItemCountChange(cssSelectorForItem,originalCount,timeOutSeconds )){
            return false;
        }
        return true;
    }
	public boolean elementIsPresent(String cssSector, String attribute, String verifyInfo){
		try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait()
            boolean isTrue = this.driver.findElement(By.cssSelector(cssSector)).getAttribute(attribute).equals(verifyInfo);
            driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
            return isTrue;
        } catch (NoSuchElementException e) {
            System.out.println("can not find the element " + cssSector);
            return false; 
        }
        catch (NullPointerException e) {
            System.out.println("can not find the element " + cssSector);
            return false;
        }
	}
    public WebElement waitForElement(String cssSelector,int timeOut){
        return this.getWaitTool().waitForElement(By.cssSelector(cssSelector), timeOut);
    }
    public boolean waitForJavaScriptCondition(String javaScript, int timeOutInSeconds){
        return this.getWaitTool().waitForJavaScriptCondition(javaScript, timeOutInSeconds);
    }
    public WebElement waitForElement(String cssSelector,int num,int timeOut){
       return this.getWaitTool().waitForElement(this.getElement(cssSelector,num), timeOut);
    }
    public int getIndexPosition(String itemName, String indexItemSelector) {
        return this.findItemInOnePageByTextContains( indexItemSelector, itemName);
    }

    public ItemFinder getContainsFinder(){
        return new ItemFinder(){
            @Override
            public int findItem(List<WebElement> follows, int j, String signContent) {
                if (follows.get(j).getText().contains(signContent)) {
                    return j;
                }
                return -2;
            }
        };
    }
    public boolean elementIsPresent(String cssSector, int num, String childCssSector){
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait()
            if(this.driver.findElements(By.cssSelector(cssSector)).get(num).findElement(By.cssSelector(childCssSector))!=null){
                return true;
            }
            driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
            return false;
        } catch (NoSuchElementException e) {
            System.out.println("can not find the element " + childCssSector + " in " + cssSector);
            return false;
        }
        catch (NullPointerException e) {
            System.out.println("can not find the element " + childCssSector + " in " + cssSector);
            return false;
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println("IndexOutOfBoundsException " + cssSector + " in "+num);
            return false;
        }
    }
    public boolean elementIsPresent(String cssSector, String childCssSector){
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait()
            if(this.driver.findElement(By.cssSelector(cssSector)).findElement(By.cssSelector(childCssSector))!=null){
                return true;
            }
            driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
            return false;
        } catch (NoSuchElementException e) {
            System.out.println("can not find the element " + childCssSector + " in " + cssSector );
            return false;
        }
        catch (NullPointerException e) {
            System.out.println("can not find the element " + childCssSector + " in " + cssSector );
            return false;
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println("can not find the element " + childCssSector + " in " + cssSector );
            return false;
        }
    }
    public boolean elementIsPresent(String cssSector, int num, String childCssSector, int timeOutSeconds){
        try {
            return this.getWaitTool().waitForElement(this.driver.findElements(By.cssSelector(cssSector)).get(num).findElement(By.cssSelector(childCssSector)), timeOutSeconds)!=null;
        } catch (NoSuchElementException e) {
            System.out.println("can not find the element " + cssSector);
            return false;
        }
        catch (NullPointerException e) {
            System.out.println("can not find the element " + cssSector);
            return false;
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println("can not find the element " + cssSector + " "+num);
            return false;
        }
    }
    public boolean elementIsPresent(String cssSector, int num, int timeWait){
        try {
            return this.getWaitTool().waitForElement(this.driver.findElements(By.cssSelector(cssSector)).get(num), timeWait)!=null;
        } catch (NoSuchElementException e) {
            System.out.println("can not find the element " + cssSector);
            return false;
        }
        catch (NullPointerException e) {
            System.out.println("can not find the element " + cssSector);
            return false;
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println("can not find the element " + cssSector + " "+num);
            return false;
        }
    }
    public boolean elementIsPresent(WebElement element, String cssSector,int timeOutSeconds){
        try {
            return this.getWaitTool().waitForElement(element.findElement(By.cssSelector(cssSector)), timeOutSeconds)!=null;
        } catch (NoSuchElementException e) {
            System.out.println("can not find the element " + cssSector);
            return false;
        }
        catch (NullPointerException e) {
            System.out.println("can not find the element " + cssSector);
            return false;
        }
    }
    public boolean elementIsPresent(WebElement element, String cssSector,long sleepInMills){
        try {
            return this.getWaitTool().waitForElement(element.findElement(By.cssSelector(cssSector)), sleepInMills)!=null;
        } catch (NoSuchElementException e) {
            System.out.println("can not find the element " + cssSector);
            return false;
        }
        catch (NullPointerException e) {
            System.out.println("can not find the element " + cssSector);
            return false;
        }
    }

    public boolean elementIsPresent(String cssSector, int timeWait){
        try {
                driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait()
                if(this.getWaitTool().waitForElement(By.cssSelector(cssSector), timeWait)==null){
                    return  false;
                }
                driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
                return true;
        } catch (NoSuchElementException e) {
            System.out.println("can not find the element " + cssSector);
            return false;
        }
        catch (NullPointerException e) {
            System.out.println("can not find the element " + cssSector);
            return false;
        }catch (ElementNotVisibleException e) {
            System.out.println("this element: "+cssSector +" is not visible ");
            return false;
        }
    }
    public boolean elementIsPresent(String cssSector){
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait()
            if(this.getWaitTool().waitForElement(By.cssSelector(cssSector), PS.midWait)==null){
                return  false;
            }
            driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
            return true;
        } catch (NoSuchElementException e) {
            System.out.println("can not find the element " + cssSector);
            return false;
        }
        catch (NullPointerException e) {
            System.out.println("can not find the element " + cssSector);
            return false;
        }catch (ElementNotVisibleException e) {
            System.out.println("this element: "+cssSector +" is not visible ");
            return false;
        }
    }
    public boolean elementIsPresent(String cssSector, String attribute, String verifyInfo, WebElement element){
        try {
            if(attribute != null){
                return element.findElement(By.cssSelector(cssSector)).getAttribute(attribute).equals(verifyInfo);
            }
            else
            {
                element.findElement(By.cssSelector(cssSector));
                return true;
            }
        } catch (NoSuchElementException e) {
            System.out.println("can not find the element " + cssSector);
            return false;
        }
        catch (NullPointerException e) {
            System.out.println("can not find the element " + cssSector);
            return false;
        }
    }

    public int getPageNum(String cssSelector, String regEx){
        Matcher mat;
        try{
            this.getWaitTool().waitForJavaScriptCondition("return $(\"" + cssSelector + "\").text().indexOf(\"()\") == -1", 5);
            String tempString = this.driver.findElement(By.cssSelector(cssSelector)).getText();
            Pattern pat = Pattern.compile(regEx);
            mat = pat.matcher(tempString);
            System.out.println("the page num is " +  Integer.parseInt(mat.replaceAll("").trim()));
            return Integer.parseInt(mat.replaceAll("").trim());
        }catch (NoSuchElementException e){
            System.out.println(" the num is zero ");
            return 0;
        }

    }
    public int getPageNum(String cssSelector){
        Matcher mat;
        try{
            this.getWaitTool().waitForJavaScriptCondition("return $(\"" + cssSelector + "\").text().indexOf(\"()\") == -1", 5);
            String tempString = this.driver.findElement(By.cssSelector(cssSelector)).getText();
            Pattern pat = Pattern.compile("[^0-9]|/");
            mat = pat.matcher(tempString);
            System.out.println("the page num is " +  Integer.parseInt(mat.replaceAll("").trim()));
            return Integer.parseInt(mat.replaceAll("").trim());
        }catch (NoSuchElementException e){
            System.out.println(" the num is zero ");
            return 0;
        }
    }
    public int getPageNum(String cssSelector, int num){
        Matcher mat;
        try{
            this.getWaitTool().waitForJavaScriptCondition("return $(\"" + cssSelector + "\").text().indexOf(\"()\") == -1", 5);
            String tempString = this.driver.findElements(By.cssSelector(cssSelector)).get(num).getText();
            Pattern pat = Pattern.compile("[^0-9]|/");
            mat = pat.matcher(tempString);
            System.out.println("the page num is " +  Integer.parseInt(mat.replaceAll("").trim()));
            return Integer.parseInt(mat.replaceAll("").trim());
        }catch (NoSuchElementException e){
            System.out.println(" the num is zero ");
            return 0;
        }
    }
    public int getPageNum(String cssSelector, int num, String childSelector){
        try{
            this.getWaitTool().waitForJavaScriptCondition("return $(\"" + cssSelector + "\").text().indexOf(\"()\") == -1",PS.midWait);
            return Integer.parseInt(Pattern.compile("[^0-9]|/").matcher(getText(cssSelector, num,  childSelector)).replaceAll("").trim());
        }catch (NoSuchElementException e){
            return 0;
        }

    }

    public int getPageNumInParentheses(String cssSelector){
        Matcher mat;
        try{
            if(this.elementIsPresent(cssSelector,1)) {
                this.getWaitTool().waitForJavaScriptCondition("return $(\"" + cssSelector + "\").text().indexOf(\"()\") == -1", 5);
                String tempString = this.getText(cssSelector);
                Pattern pat = Pattern.compile("（([0-9]*?)）");
                mat = pat.matcher(tempString);
                mat.find();
                System.out.println("the page num is " + mat.group(1));
                return Integer.parseInt(mat.group(1));
            }else {
                return 0;
            }
        }catch (Exception e){
            System.out.println(" the num is zero ");
            return 0;
        }

    }


	public WebElement getElement(String cssSector){
		try { 
            return this.driver.findElement(By.cssSelector(cssSector));
        } catch (NoSuchElementException e) {
            System.out.println("can not find the element " + cssSector);
            return null; 
        } 
		
	}
    public WebElement getElement(WebElement element, String cssSector){
        try {
            return element.findElement(By.cssSelector(cssSector));
        } catch (NoSuchElementException e) {
            System.out.println("can not find the element " + cssSector);
            return null;
        }catch (NullPointerException e) {
            System.out.println("null element ");
            return null;
        }

    }
    public WebElement getElement(String cssSector, int num){
        try {
            return this.driver.findElements(By.cssSelector(cssSector)).get(num);
        } catch (NoSuchElementException e) {
            System.out.println("can not find the element " + cssSector);
            return null;
        }catch (IndexOutOfBoundsException e) {
            System.out.println("IndexOutOfBoundsException! null element whose cssSelector is " +  cssSector);
            return null;
        }catch (NullPointerException e){
            System.out.println("NullPointerException! can not find the element " + cssSector);
            return null;
        }
    }
    public WebElement getElement(String cssSector, int num, String childElementSelector){
        try {
            return this.driver.findElements(By.cssSelector(cssSector)).get(num).findElement(By.cssSelector(childElementSelector));
        } catch (NoSuchElementException e) {
            System.out.println("can not find the element " + cssSector);
            return null;
        }catch (IndexOutOfBoundsException e) {
            System.out.println("IndexOutOfBoundsException! null element whose cssSelector is " +  cssSector);
            return null;
        }catch (NullPointerException e){
            System.out.println("NullPointerException! can not find the element " + cssSector);
            return null;
        }
    }
    public void selectItemWithCountTest(  String itemSelector, String itemCountSelector,String message,String selectedMark){
        selectItemWithCountTest( itemSelector,0, itemCountSelector,message,selectedMark);
    }
    public void selectItemWithCountTest(  String itemSelector, int itemNum, String itemCountSelector,String message, String selectedMark){
        int selectedProjectCount = this.getPageNum(itemCountSelector);
        clickSelectingItem(itemSelector, itemNum, itemCountSelector);
        this.assertNumInTextEquals(message + "选择后，页面显示选中的项目计数没有加1", selectedProjectCount + 1, itemCountSelector);
        this.assertElementPresent(message + "选择后,元素上没有显示选中标记", itemSelector, itemNum, selectedMark);
    }
    public void selectItemCancelWithCountTest(  String itemSelector, String itemCountSelector,String message, String selectedMark){
        selectItemCancelWithCountTest(itemSelector,0, itemCountSelector,message,  selectedMark);
    }
    public void selectItemCancelWithCountTest(  String itemSelector, int itemNum, String itemCountSelector,String message, String selectedMark){
        int selectedProjectCount = this.getPageNum(itemCountSelector);
        clickSelectingItem(itemSelector,itemNum, itemCountSelector);
        this.assertNumInTextEquals(message + "取消选择后，页面显示选中的项目计数没有减1", selectedProjectCount - 1,itemCountSelector);
        this.assertElementNotPresent(message + "取消选择后,元素上选中标记没有消失", itemSelector, itemNum, selectedMark);
    }
    public void clickSelectingItem(String itemSelector, int itemNum, String itemCountSelector){
        String originalContent = this.getText(itemCountSelector);
        this.clickElement(itemSelector, itemNum);
        this.waitForContentChange(itemCountSelector, originalContent, 3);
    }

    public WebElement getElement(By by){
        try {
            return this.driver.findElement(by);
        } catch (NoSuchElementException e) {
            System.out.println("can not find the element " + by.toString());
            return null;
        }
    }
    public boolean clickElement(String cssSector){
        try {
            if(this.getWaitTool().waitForElement(By.cssSelector(cssSector),3) == null) {
               return false;
            }
            this.driver.findElement(By.cssSelector(cssSector)).click();
            return true;

        } catch (NoSuchElementException e) {
            System.out.println("this element: "+cssSector +" is not exist ");
            return false;
        }
        catch (ElementNotVisibleException e) {
            System.out.println("this element: "+cssSector +" is not visible ");
            return false;
        }
    }
    public boolean clickElements(String cssSector){
        try {
            this.getWaitTool().waitForElementClickable(By.cssSelector(cssSector),10);
            for(int i = 0; i<this.getElements(cssSector).size(); i++){
                this.getElements(cssSector).get(i).click();
            }
            return true;
        } catch (NoSuchElementException e) {
            System.out.println("this element: "+cssSector +" is not exist ");
            return false;
        }
        catch (ElementNotVisibleException e) {
            System.out.println("this element: "+cssSector +" is not visible ");
            return false;
        }
    }
    public boolean  clickElement(String cssSelector, String cssSelectorWaitingElement){
        try {
            this.waitForElement(cssSelector,3);
            this.getWaitTool().waitForElementClickable(By.cssSelector(cssSelector),5);
            this.driver.findElement(By.cssSelector(cssSelector)).click();
            if(null == this.getWaitTool().waitForElement(By.cssSelector(cssSelectorWaitingElement),5)){
               return false;
            }
            return true;
        } catch (NoSuchElementException e) {
            assertEquals("this element: "+cssSelector +" is not exist ", 2,3);
            return true;
        }
        catch (ElementNotVisibleException e) {
            assertEquals("this element: "+cssSelector +" is not visible ", 2,3);
            return true;
        }
    }
    public boolean  clickElementWithTextChange(String cssSelector, String cssSelectorWaitingElement, String textSelector){
        String watchCountText = getText(textSelector);
        clickElement( cssSelector,  cssSelectorWaitingElement);
        return waitForContentChange(textSelector,watchCountText,5 );
    }
    public void clickElement(WebElement webElement, String cssSelectorWaitingElement){
        try {
            this.getWaitTool().waitForElementClickable(webElement,5);
            webElement.click();
            this.getWaitTool().waitForElement(By.cssSelector(cssSelectorWaitingElement),5);
        } catch (NoSuchElementException e) {
            assertEquals("this element: "+ webElement.toString() +" is not exist ", 2,3);
        }
        catch (ElementNotVisibleException e) {
            assertEquals("this element: "+webElement.toString() +" is not visible ", 2,3);
        }
    }
    public void clickElement(WebElement webElement){
        try {
            this.getWaitTool().waitForElementClickable(webElement,5);
            webElement.click();
        } catch (NoSuchElementException e) {
            System.out.println("this element: "+ webElement.getAttribute("class") +" is not exist ");
        }
        catch (ElementNotVisibleException e) {
            System.out.println("this element: "+ webElement.getAttribute("class") +" is not visible ");
        }

    }
    public boolean clickElement(String cssSector, int i){
        try {
            this.getWaitTool().waitForElementClickable(this.driver.findElements(By.cssSelector(cssSector)).get(i),7);
            this.driver.findElements(By.cssSelector(cssSector)).get(i).click();
            return true;
        } catch (NoSuchElementException e) {
            System.out.println("this element: "+cssSector +" is not exist ");
            return false;
        }
        catch (ElementNotVisibleException e) {
            System.out.println("this element: "+cssSector +" is not visible ");
            return false;
        }

    }
    public boolean clickElement(String cssSector, int i, String cssSelectorWaitingElement){
        try {
            this.waitForElement(cssSector,5);
            this.getWaitTool().waitForElementClickable(this.driver.findElements(By.cssSelector(cssSector)).get(i),5);

            this.driver.findElements(By.cssSelector(cssSector)).get(i).click();
            if(this.getWaitTool().waitForElement(By.cssSelector(cssSelectorWaitingElement),6)==null){
                return false;
            }
            return true;
        } catch (NoSuchElementException e) {
            System.out.println("this element: "+cssSector +" is not exist ");
            return false;
        }
        catch (ElementNotVisibleException e) {
            System.out.println("this element: "+cssSector +" is not visible ");
            return false;
        }
    }
    public boolean clickChildElement(String cssSector, int i, String childElement){
        try {
            this.waitForElement(cssSector,5);
            this.getWaitTool().waitForElementClickable(this.driver.findElements(By.cssSelector(cssSector)).get(i),PS.midWait);
            if(this.getWaitTool().waitForElement(this.driver.findElements(By.cssSelector(cssSector)).get(i),PS.midWait)==null){
                return false;
            }
            this.driver.findElements(By.cssSelector(cssSector)).get(i).findElement(By.cssSelector(childElement)).click();
            return true;
        } catch (NoSuchElementException e) {
            System.out.println("this element: "+cssSector +" is not exist ");
            return false;
        }
        catch (ElementNotVisibleException e) {
            System.out.println("this element: "+cssSector +" is not visible ");
            return false;
        }
    }

    public boolean checkAvatar(String imageSelector,  String avatarLinkSelector,User user, int avatarNum ){
        try{
            this.getWaitTool().waitForElement(By.cssSelector(imageSelector),3);
            if(!checkImage( imageSelector, user.getAvatar(), avatarNum)){
                System.out.println("头像的配图有问题\n");
                return false;
            }
            System.out.println("avatar link is "+ this.getAttribute(avatarLinkSelector ,"href", avatarNum));
            if(!this.getAttribute(avatarLinkSelector ,"href" ,avatarNum).equals(user.getHomePageLink())) {
                System.out.println("头像的链接有问题");
                return false;
            }
        }catch (NoSuchElementException e) {
            System.out.println("this element: "+avatarLinkSelector +" is not exist ");
            return false;
        }
        catch (ElementNotVisibleException e) {
            System.out.println("this element: "+avatarLinkSelector +" is not visible ");
            return false;
        }catch (NullPointerException e) {
            System.out.println("NullPointer!! this element: "+avatarLinkSelector +" is not exist ");
            return false;
        }
        return true;
    }
    public boolean checkImage(String imageSelector, String imageSrc, int imageNum){
        try{
            System.out.println("src " + this.getAttribute(imageSelector ,"src", imageNum));
            System.out.println("imageSrc " + imageSrc);
            System.out.println("imageSelector " + imageSelector);
            return this.getAttribute(imageSelector, "src",imageNum).contains(imageSrc);
        }catch (NoSuchElementException e) {
            System.out.println("this element: "+imageSelector +" is not exist ");
            return false;
        }
        catch (ElementNotVisibleException e) {
            System.out.println("this element: "+imageSelector +" is not visible ");
            return false;
        }catch (NullPointerException e) {
            System.out.println("NullPointer!! this element: "+imageSelector +" is not exist ");
            return false;
        }
    }
    public boolean checkAvatar(String imageSelector,  String avatarLinkSelector,User user){
        try{
            if(!checkImage( imageSelector,  user.getAvatar())){
                System.out.println("头像的配图有问题\n");
                return false;
            }
            System.out.println("avatar link is "+ this.getAttribute(avatarLinkSelector, "href"));
            if(!this.getAttribute(avatarLinkSelector, "href").contains("/u/"+user.getUserLoginName())) {
                System.out.println("头像的链接有问题");
                return false;
            }
        }catch (NoSuchElementException e) {
            System.out.println("this element: "+avatarLinkSelector +" is not exist ");
            return false;
        }
        catch (ElementNotVisibleException e) {
            System.out.println("this element: "+avatarLinkSelector +" is not visible ");
            return false;
        }catch (NullPointerException e) {
            System.out.println("NullPointer!! this element: "+avatarLinkSelector +" is not exist ");
            return false;
        }
        return true;
    }
    public boolean checkImage(String imageSelector, String imageSrc){
        try{
            return this.getAttribute(imageSelector, "src").contains(imageSrc);
        }catch (NoSuchElementException e) {
            System.out.println("this element: "+imageSelector +" is not exist ");
            return false;
        }
        catch (ElementNotVisibleException e) {
            System.out.println("this element: "+imageSelector +" is not visible ");
            return false;
        }catch (NullPointerException e) {
            System.out.println("NullPointer!! this element: "+imageSelector +" is not exist ");
            return false;
        }
    }
    public void checkUserSpaceLink(String userName, String cssSelectorForUserSpaceLink,String linkURL ) throws InterruptedException {
        assertEquals("用户空间url链接有误", linkURL,this.getLink(cssSelectorForUserSpaceLink));
        linksMap.addLink(linkURL,new PageLink(userName,"#user-space .user-name"));
    }


    public boolean checkImage(String imageSelector, int num, String imageSrc){
        try{
            return this.getAttribute(imageSelector, "src").contains(imageSrc);
        }catch (NoSuchElementException e) {
            System.out.println("this element: "+imageSelector +" is not exist ");
            return false;
        }
        catch (ElementNotVisibleException e) {
            System.out.println("this element: "+imageSelector +" is not visible ");
            return false;
        }catch (NullPointerException e) {
            System.out.println("NullPointer!! this element: "+imageSelector +" is not exist ");
            return false;
        }
    }
    public String testLinkAndPresent(String CssSecector, String verifyHrefLink,  String elementName) throws NoSuchAttributeException {
        assertTrue(elementName + "不存在", this.elementIsPresent(CssSecector, null, null));
        String hrefLink = this.getLink(CssSecector);
        assertEquals(elementName +"链接不正确",hrefLink, verifyHrefLink);
        return hrefLink;
    }

    public void clickTabTestPresent(String CssSecector,  String elementName, String clickTab) throws NoSuchAttributeException, InterruptedException {
        this.getElement(clickTab).click();
        Thread.sleep(2000);
        assertTrue(elementName + "不存在", this.elementIsPresent(CssSecector, null, null));


    }

    public String getLink(String cssSector)  {
        return this.getAttribute(cssSector,"href");
    }
    public String getLink(String cssSector,int num)  {

        return this.getAttribute(cssSector,"href",num);
    }
	public List<WebElement> getElements(String cssSector){
		try {
            return this.driver.findElements(By.cssSelector(cssSector));
        } catch (NoSuchElementException e) {
            System.out.println("can not find elements: "+cssSector );
            return null; 
        } 
		
	}
    public List<WebElement> getElements(WebElement element, String cssSector){
        try {
            return element.findElements(By.cssSelector(cssSector));
        } catch (NoSuchElementException e) {
            System.out.println("can not find elements: " + cssSector );
            return null;
        }

    }
    public void highLightElemnt(String cssSector){
        try {
            driver.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", driver.findElement(By.cssSelector(cssSector)), "style", "border: 2px solid yellow; color: yellow; font-weight: bold;");

        } catch (NoSuchElementException e) {
            System.out.println("can not find element " + cssSector);
        }

    }
    public void highLightElemnt(WebElement element){
        try {
            driver.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", "border: 2px solid yellow; color: yellow; font-weight: bold;");

        } catch (NoSuchElementException e) {
            System.out.println("can not find element " + element.toString());
        }

    }
    public String getNotificationLink(){
        return this.getBaseUrl() + "user/notifications/basic";
    }
    public int  getElementCount(String cssSector){
        if(this.getElements(cssSector)!=null) {
            return this.getElements(cssSector).size();
        }else{
            return 0;
        }
    }
    public int  getElementCount(String cssSector, int num,String childCssSector){
        return getElementCount(getElement(cssSector,num), childCssSector);
    }
    public int  getElementCount(WebElement element, String childCssSector){
        if(this.elementIsPresent(element ,childCssSector,2)) {
            return this.getElements(element,childCssSector).size();
        }else{
            return 0;
        }
    }
    public String getText(String cssSector){
        try {
            if( (! this.waitForContentNotNull(cssSector,10)) &&(this.driver.findElement(By.cssSelector(cssSector)) == null)) {
                return null;
            }
             return this.driver.findElement(By.cssSelector(cssSector)).getText();
        } catch (NoSuchElementException e) {
            System.out.println("can not find element " + cssSector);
            return null;
        } catch (TimeoutException e){
            System.out.println("can not find element " + cssSector);
            return null;
        }
    }
    public String getScreenFileDir(){
        return configFileReader.getValue("screenDir");
    }
    public void getScreenshot() {
        getScreenshot("");
    }

    public void getScreenshot(String name){
        try {
            File dir = new File(getScreenFileDir()) ;
            dir.mkdirs();
            FileUtils.copyFile(driver.getScreenshotAs(OutputType.FILE), new File( getScreenFileDir()+"\\" +name+"_"+ (new Date()).getTime() + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getText(String cssSector, int i){
        try {
            if(this.waitForElement(cssSector,i,5) ==null&&
                    (this.driver.findElements(By.cssSelector(cssSector)).get(i) == null)) {
                return null;
            }
            return this.driver.findElements(By.cssSelector(cssSector)).get(i).getText();
        } catch (NoSuchElementException e) {
            log.error("can not find element " + cssSector);
            return null;
        }catch (IndexOutOfBoundsException e) {
            log.error( cssSector + "indexOutOfBoundException");
            return null;
        }
    }
    public String getText(String cssSector, int i, String childSelector){
        return getText(getElement(cssSector,i),childSelector);
    }
    public String getText(WebElement element, String cssSector){
        try {
            if(this.waitForElement(cssSector,3) ==null && this.driver.findElement(By.cssSelector(cssSector)) == null) {
                return null;
            }
            return element.findElement(By.cssSelector(cssSector)).getText();
        } catch (NoSuchElementException e) {
            System.out.println("can not find element " + cssSector);
            return null;
        }

    }
    public String getAttribute(String cssSector, String Attribute){
        try {
            if(this.waitForElement(cssSector,3) ==null &&
                    (this.driver.findElement(By.cssSelector(cssSector)).getAttribute(Attribute) == null)) {
                return null;
            }
            return this.driver.findElement(By.cssSelector(cssSector)).getAttribute(Attribute);
        } catch (NoSuchElementException e) {
            System.out.println("can not find element " + cssSector);
            return null;
        }

    }
    public String getAttribute(String cssSector, String Attribute, int num){
        try {
            return this.driver.findElements(By.cssSelector(cssSector)).get(num).getAttribute(Attribute);
        } catch (NoSuchElementException e) {
            System.out.println("can not find element " + cssSector);
            return null;
        } catch (NullPointerException e) {
            System.out.println("can not find element " + cssSector);
            return null;
        }

    }

    public String getAttribute(String cssSector, String Attribute,WebElement element){
        try {
            return element.findElement(By.cssSelector(cssSector)).getAttribute(Attribute);
        } catch (NoSuchElementException e) {
            System.out.println("can not find element " + cssSector);
        }
        return null;
    }
    public void clearAndSendKeys(String cssSector, String inputContents){
        try {
            this.driver.findElement(By.cssSelector(cssSector)).clear();
            this.driver.findElement(By.cssSelector(cssSector)).sendKeys(inputContents);
        } catch (NoSuchElementException e) {
            System.out.println("can not find element " + cssSector);
        }catch (ElementNotVisibleException e) {
            System.out.println("the element: " + cssSector+" is not visible");
        }
    }
    public void clearAndSendKeys(String cssSector, int num, String inputContents){
        try {
            this.driver.findElements(By.cssSelector(cssSector)).get(num).clear();
            this.driver.findElements(By.cssSelector(cssSector)).get(num).sendKeys(inputContents);
        } catch (NoSuchElementException e) {
            System.out.println("can not find element " + cssSector);
        }catch (ElementNotVisibleException e) {
            System.out.println("the element: " + cssSector+" is not visible");
        }
    }
    public void sendKeys(String cssSector, String inputContents){
        try {
            this.waitTool.waitForElement(By.cssSelector(cssSector),4);
            this.driver.findElement(By.cssSelector(cssSector)).sendKeys(inputContents);
        } catch (NoSuchElementException e) {
            System.out.println("can not find element " + cssSector);
        }catch (ElementNotVisibleException e) {
            System.out.println("the element: " + cssSector+" is not visible");
        }
    }
    public void sendImgFilePath( String cssSelectorForImgInput,String filePath) {
        if( isChromeDriver()) {
            sendKeys(cssSelectorForImgInput, filePath.substring(6, filePath.length()));
        }else{
            sendKeys(cssSelectorForImgInput, filePath);
        }
    }
    public void sendKeys(String cssSector, int i, String inputContents){
        try {
            this.waitTool.waitForElement(By.cssSelector(cssSector),4);
            this.driver.findElements(By.cssSelector(cssSector)).get(i).sendKeys(inputContents);
        } catch (NoSuchElementException e) {
            System.out.println("can not find element " + cssSector);
        }catch (ElementNotVisibleException e) {
            System.out.println("the element: " + cssSector+" is not visible");
        }
    }
    public boolean comparePageTime(Date compareTime, String cssSelector) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        System.out.println("createTime  " +this.driver.findElement(By.cssSelector(cssSelector)).getAttribute("title"));
        String createTime[] = this.driver.findElement(By.cssSelector(cssSelector)).getAttribute("title").split("\\+");
        System.out.println("createTime 0 " + createTime[0]);
        Date createTimeDate = df.parse(createTime[0]);
        System.out.println("compareTime " + compareTime.getTime());
        int minutes =(int) Math.abs(createTimeDate.getTime() - compareTime.getTime())/(1000*60);
        return minutes <= 1;

    }
    public boolean comparePageTime(Date compareTime, String cssSelector, DateFormat df) throws ParseException {

        String createTime = (String)this.driver.executeScript("return $(\""+cssSelector + "\").text().slice(6,22)");
        System.out.println("createTime  " + createTime);
        Date createTimeDate = df.parse(createTime);
        System.out.println("createTimeDate  " + createTimeDate.getTime());
        compareTime = df.parse(df.format(compareTime));
        System.out.println("compareTime " + compareTime.getTime());
        int minutes =(int) Math.abs(createTimeDate.getTime() - compareTime.getTime())/(1000*60);
        return minutes <= 3;

    }

    public  boolean verifyTime(Date expectTime, String targetItemTimeJqueryCode,  String dateFormat  ,int timeDiff) {
        DateFormat df  = new SimpleDateFormat(dateFormat);
        String targetItemTime = (String)this.driver.executeScript(targetItemTimeJqueryCode);
        System.out.println("targetItemTimeText  " + targetItemTime);
        Date targetItemTimeDate = null;
        try {
            targetItemTimeDate = df.parse(targetItemTime);
            expectTime = df.parse(df.format(expectTime));
        } catch (ParseException e) {
            log.error("动态的日期无法获取或转化失败");
        }catch (NullPointerException e){
            log.error("动态的日期无法获取或转化失败");
        }
        System.out.println("expectTimeText  " + (expectTime.getHours() + ":" + expectTime.getMinutes()));
        System.out.println("targetItem  " + (targetItemTimeDate.getHours() * 60 + targetItemTimeDate.getMinutes()));
        System.out.println("expectTime " + (expectTime.getHours() * 60 + expectTime.getMinutes()));

        int targetMinutes = targetItemTimeDate.getHours() * 60 + targetItemTimeDate.getMinutes();
        int compareMinutes = expectTime.getHours() * 60 + expectTime.getMinutes();
        if( Math.abs(compareMinutes - targetMinutes) <= timeDiff ) {
            log.info((new Date()) + " 正常");
            return true;
        }
        log.error((new Date()) + " 测试时间不对");
        return false;
    }
    public  int findItemBySign ( String signContent, String itemCssSelector, ItemFinder a ) throws InterruptedException {
        List<WebElement> follows;
        int pageCount;
        int itemNum;
        if(!this.elementIsPresent(".cg .page.active ",PS.shortWait)){
            pageCount = 0;
        }else {
            if(this.getText(".cg .page.active ").equals("")){
                System.out.println("页面数目还没刷不出");
                Thread.sleep(3000);
            }
            if(this.elementIsPresent(".cg .page ",this.getElements(".cg .page ").size()-1,".icon")){
                itemNum =  this.getElements(".cg .page ").size()-2;

                pageCount = Integer.parseInt(this.getText(".cg .page ",itemNum));
            }else {
                itemNum =  this.getElements(".cg .page ").size()-1;
                pageCount = Integer.parseInt(this.getText(".cg .page ", itemNum));
            }
        }
        System.out.println("page count " + pageCount);
        System.out.println("start find item " + signContent);
        String activePage = "1";
        follows = this.getElements(itemCssSelector);
        if(pageCount != 0) {
            if(!this.getText(".page.active").equals("1")){
                this.clickElement(".cg .page",1);
                this.getWaitTool().waitForJavaScriptCondition("return $(\".page.active\").attr(\"title\")== 1" , 5);
                follows = this.getElements(itemCssSelector);
            }
            for (int i = 0; i < pageCount ; i++) {
                for (int j = 0; j < follows.size(); j++) {
                    if(a.findItem(follows,j,signContent) != -2){
                        return  a.findItem(follows,j,signContent);
                    }
                }
                if(i == pageCount - 1){
                    break;
                }
                this.clickElement(".page.next .arrow");
                this.getWaitTool().waitForJavaScriptCondition("return $(\".page.active\").attr(\"title\")!= " + activePage, 5);
                activePage = this.getAttribute(".page.active", "title");
                follows = this.getElements(itemCssSelector);
            }
        }else{
            for (int j = 0; j < follows.size(); j++) {
                if(a.findItem(follows,j,signContent) != -2){
                    return  a.findItem(follows,j,signContent);
                }
            }
        }
        log.info("没有发现目标"+ signContent);
        return -1;
    }



    public boolean verifyCreateTime(Date compareTime, String cssSelector, String format) throws ParseException {
        Matcher mat;
        String regEx = "[^0-9]|/";
        DateFormat df = new SimpleDateFormat(format);//设置日期格式
        Pattern pat = Pattern.compile(regEx);

        int pageNum = 0;
        System.out.println("createTime  " + this.driver.findElement(By.cssSelector(cssSelector)).getAttribute("title"));
        String createTime[] = this.driver.findElement(By.cssSelector(cssSelector)).getAttribute("title").split("\\+");
        System.out.println("createTime 0 " + createTime[0]);

        System.out.println("compare time " + df.format(compareTime));
        Date createTimeDate = df.parse(createTime[0]);
        compareTime = df.parse(df.format(compareTime));
        System.out.println("compareTime " + createTimeDate.getTime());
        System.out.println("createTimeDate " + compareTime.getTime());
        int minutes =(int) Math.abs(createTimeDate.getTime() - compareTime.getTime())/(1000*60);

        if( this.driver.findElement(By.cssSelector(cssSelector)).getText().equals("几秒前")){
            pageNum = 0 ;
        }
        if( this.driver.findElement(By.cssSelector(cssSelector)).getText().contains("分钟"))
        {
            mat = pat.matcher(this.driver.findElement(By.cssSelector(cssSelector)).getText());
            pageNum = Integer.parseInt(mat.replaceAll("").trim());
        }
        if( this.driver.findElement(By.cssSelector(cssSelector)).getText().contains("小时"))
        {
            minutes = minutes / 60;
            mat = pat.matcher(this.driver.findElement(By.cssSelector(".created-time")).getText());
            pageNum = Integer.parseInt(mat.replaceAll("").trim());
        }
        System.out.println("minutes is " + minutes);
        System.out.println("pageNum is " + pageNum);
        return Math.abs(pageNum - minutes) <= 1;
    }
    public boolean waitElementDisappear(String elementSelector, int elementLengthAfterRemove){
        return this.getWaitTool().waitForJavaScriptCondition("return $(\""+ elementSelector +"\").length == " + elementLengthAfterRemove,5);
    }
    public boolean waitForElementDisappear(final String cssSelectorForContent,final int num,final String childElement,int timeOutInSeconds){
        return this.getWaitTool().waitForElementDisappear(cssSelectorForContent, num, childElement, timeOutInSeconds);
    }
    public boolean waitForElementCountChange ( final String cssSelectorForContent,final int num,final String childElement,final int countSize,int timeOutInSeconds){
        return this.getWaitTool().waitForElementCountChange(cssSelectorForContent, num, childElement, countSize, timeOutInSeconds);
    }

    public void waitLoadingIconInvisible(){
        this.waitForElement(".loading.icon", PS.shortWait);
        this.waitElementInvisible(".loading.icon");
    }
    public boolean waitElementDisappear(String elementSelector){
        return this.getWaitTool().waitForJavaScriptCondition("return $(\""+ elementSelector +"\").length == 0 "  ,10);

    }
    public boolean waitElementInvisible(String elementSelector){
       return waitElementInvisible( elementSelector, 5);
    }
    public boolean waitElementInvisible(String elementSelector, int timeOut){
        return this.getWaitTool().waitForJavaScriptCondition("return $(\""+ elementSelector +"\").is(':visible') == false" ,timeOut);
    }

    public void checkAlert(String alertCssSelector, int num,String errorMessage, String alertMessage)  {
        if(this.getDriver().getWrappedDriver().getClass().getName().toLowerCase().contains("phantom")) {
            ((JavascriptExecutor) this.getDriver()).executeScript("window.confirm = function(msg){  " +
                    " if(msg == \"" + alertMessage + "\" ){return true;}};");
            this.clickElement(alertCssSelector,num);
        }else {
            this.clickElement(alertCssSelector,num);
            assertEquals(errorMessage, this.getDriver().switchTo().alert().getText(), alertMessage);
            this.getDriver().switchTo().alert().dismiss();
            this.clickElement(alertCssSelector,num);
            this.getDriver().switchTo().alert().accept();
        }
    }

    public void sureAlert(String alertCssSelector){
        if(!this.getDriver().getWrappedDriver().getClass().getName().toLowerCase().contains("phantom")) {
            this.clickElement(alertCssSelector);
            this.getDriver().switchTo().alert().accept();
        }else {
            ((JavascriptExecutor) this.getDriver()).executeScript("window.confirm = function(msg){  " +
                    " if(true ){return true;}};");
            this.clickElement(alertCssSelector);
        }
    }



    public void checkAlert(String alertCssSelector, String errorMessage, String alertMessage){
        checkAlert(alertCssSelector, 0, errorMessage, alertMessage);
    }
    public void checkAlert(String selector,int num, String childSelector,String errorMessage, String alertMessage){
        if(!this.getDriver().getWrappedDriver().getClass().getName().toLowerCase().contains("phantom")) {
            this.clickChildElement(selector, num, childSelector);
            assertEquals(errorMessage, this.getDriver().switchTo().alert().getText(), alertMessage);
            this.getDriver().switchTo().alert().dismiss();
            this.clickChildElement(selector, num, childSelector);
            this.getDriver().switchTo().alert().accept();
        }else{
            ((JavascriptExecutor) this.getDriver()).executeScript("window.confirm = function(msg){  " +
                    " if(msg == \"" + alertMessage + "\" ){return true;}};");
            this.clickChildElement(selector, num, childSelector);
        }
    }
    public boolean verifyHint(String hintText) {
        List<WebElement> tempWebElementList;//临时的web元素列表
        try {
            if(!this.elementIsPresent(".outer span ",3)){
                return false;
            }
            this.moveToElement(".outer span ");
            tempWebElementList = this.getElements(".outer span");
            String verifyText = "ii";
            for (WebElement aTempWebElementList : tempWebElementList) {
                verifyText = aTempWebElementList.getText();
                if (verifyText.equals(hintText)) {
                    try {
                        this.moveToElement(".outer span ");
                        if(this.elementIsPresent(".flash .close",PS.shortWait)){
                            try {
                                this.clickElement(".flash .close");
                            }catch (WebDriverException e){
                                log.error("提示关闭按钮无法点击");
                            }
                        }
                        return true;
                    }catch (ElementNotVisibleException e) {
                        return true;
                    }
                }
            }
            if(verifyText.equals("ii")) {
                return false;
            }
            assertEquals("网页提示信息不正确: " + hintText, hintText, verifyText);
            return false;
        } catch (ElementNotVisibleException e) {
            log.error("hint "+hintText+" is not visible");
            return false;
        }
        catch(NoSuchElementException e){
            log.error("no such hint "+ hintText);
            return false;
        }
        catch (org.openqa.selenium.StaleElementReferenceException e){
            log.error("页面提示flash可能已经消失，导致无法捕捉");
            return false;
        }
    }
    public void checkItemInMap( Map<String,ItemEntry> itemEntryMap, String itemContentSelector,String itemIconSelector, String checkMessage ){
        ItemEntry itemEntry;
        List<WebElement> checkElements = this.getElements(itemContentSelector);
        for(int i = 0; i < checkElements.size(); i++){
            if(itemEntryMap.containsKey(this.getText(itemContentSelector, i))){
                itemEntry = itemEntryMap.get(this.getText(itemContentSelector, i));

                int linkSize = this.getElements(itemContentSelector).get(i).findElements(By.cssSelector("a[href]")).size();
                try {
                    for (int j = 0; j < linkSize; j++) {
                        assertEquals(checkMessage + ":" + itemEntry.getContent() + "的第" + (j + 1) + "个链接出错", itemEntry.getLinkUrls().get(j),
                                this.getElements(itemContentSelector).get(i).findElements(By.cssSelector("a")).get(j).getAttribute("href"));
                    }
                }catch (IndexOutOfBoundsException e){
                    System.out.println("itemEntry "+ itemEntry.getContent());
                }
                assertTrue(checkMessage + ":" + itemEntry.getContent() + "的图标出错",
                        this.getAttribute(itemIconSelector, "class", i).contains(itemEntry.getIcon()));
                //在批量处理多条相同动态时
                //if(itemEntry.decreaseCount() == 0) {
                    itemEntryMap.remove(this.getText(itemContentSelector, i));
                //}
                if( itemEntryMap.size()==0 ){
                    break;
                }
            }
        }
    }


    public void checkItemEntryMap(Map<String,ItemEntry> itemEntryMap){
        if(!itemEntryMap.isEmpty()) {
            Iterator iterator = itemEntryMap.entrySet().iterator();
            String errorMessage = "如下entry没找到： ";
            Map.Entry<String, List<String>> entry;
            while (iterator.hasNext()) {
                entry = (Map.Entry)iterator.next();
                errorMessage+=  entry.getKey();
            }
            assertEquals(errorMessage, errorMessage, "如下entry没找到： ");
        }
    }
    public void testPageTurning(String url){
        this.navigate(url);
        if( !this.elementIsPresent(".page",3) || this.getElements(".page").size()<3 ){
            System.out.println("该页面无页可翻 url"+ url);
            return;
        }

        assertEquals("url "+ url + " 进入翻页页面显示的页数不是第一页", this.getText(".page.active span"),"1");
        this.clickElement(".page.next .arrow");
        this.getWaitTool().waitForJavaScriptCondition("return $('.page.active span').text() == 2",3);
        assertEquals("url "+ url + " 无法翻页到下一页", this.getText(".page.active span"),"2" );
        this.clickElement(".page.prev .arrow");
        this.getWaitTool().waitForJavaScriptCondition("return $('.page.active span').text() == 1",3);
        assertEquals("url "+ url + " 无法翻页到前一页", this.getText(".page.active span"),"1" );
        this.clickElement(".page[title='2'] ");
        this.getWaitTool().waitForJavaScriptCondition("return $('.page.active span').text() == 2",3);
        assertEquals("url "+ url + " 无法翻页到指定页数", this.getText(".page.active span"),"2" );
    }
    public void testSelectHintItemWithKeyboard(String itemName,String hintItemSelector, String inputSelector,String hintSign ){
        this.clearAndSendKeys(inputSelector, hintSign);
        this.waitForElement(hintItemSelector, 5);
        String originalSelectedItemText = this.getText(hintItemSelector);
        this.sendKeys(Keys.DOWN);
        assertTrue("选择" + itemName + "时向下的按键失灵 当前选中项为" + this.getText(hintItemSelector), this.waitForContentChange(hintItemSelector, originalSelectedItemText, 4));
        originalSelectedItemText = this.getText(hintItemSelector);
        this.sendKeys(Keys.UP);
        assertTrue("选择"+itemName+"时向上的按键失灵 当前选中项为" + this.getText(hintItemSelector),this.waitForContentChange(hintItemSelector,originalSelectedItemText,4));

    }

    public ItemFinder getItemEqualsFinder(){
        return new ItemFinder(){
            @Override
            public int findItem(List<WebElement> follows, int j, String signContent) {
                if (follows.get(j).getText().equals(signContent)) {
                    return j;
                }
                return -2;
            }
        };
    }
    public ItemFinder getLinkContainFinder(){
        return new ItemFinder(){
            @Override
            public int findItem(List<WebElement> follows, int j, String signContent) {
                if (follows.get(j).getAttribute("href").contains(signContent)) {
                    return j;
                }
                return -2;
            }
        };
    }

    public int findItemInOnePageByTextEquals(String itemsSelector, String itemText){
        int tagCount = this.getElementCount(itemsSelector);
        for(int i = 0; i<tagCount ;i++) {
            if (this.getText(itemsSelector,i).equals(itemText)){
                return i;
            }
        }
        System.out.println("not find " + itemsSelector);
        return  -1;
    }
    public int findItemInOnePageByTextContains(String itemsSelector,String itemText){
        int tagCount = this.getElementCount(itemsSelector);
        for(int i = 0; i<tagCount ;i++) {
            if (this.getText(itemsSelector,i).contains(itemText)){
                return i;
            }
        }
        return  -1;
    }
    public int findItemInOnePageByAttributeContains(String itemsSelector,String attribute,String itemText){
        int tagCount = this.getElementCount(itemsSelector);
        for(int i = 0; i<tagCount ;i++) {
            if (this.getAttribute(itemsSelector,attribute,i).contains(itemText)){
                return i;
            }
        }
        return  -1;
    }
}

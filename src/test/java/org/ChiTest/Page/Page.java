package org.ChiTest.Page;

import org.ChiTest.WebComponent.InterFace.ItemEntry;
import org.ChiTest.WebComponent.InterFace.ItemFinder;
import org.ChiTest.LinksMap;
import org.ChiTest.PageLink;
import org.ChiTest.TestHelper;
import org.ChiTest.User.User;
import org.ChiTest.WaitTool;
import org.ChiTest.rc.constans.RCHttpJsonResultObject;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.ChiTest.congfig.ConfigFileReader;

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
    public void backToTop(){
        this.executeScript("window.scrollTo(0,0)");
    }
    public int getScrollY(){
        return ((Long) this.executeScript("return window.scrollY")).intValue();
    }
    public Cookie getStagingSid(String sidValue) throws ParseException {
        return new Cookie("sid", sidValue, "staging.coding.net","/",
                (new SimpleDateFormat("yyyy-MM-dd")).parse("2017-08-09"));
    }

    /**
     * 跳转到某个 url, 若 url 相同则不跳转
     * @param url
     */
    public void navigate(String url)   {
        try {
            if (this.driver.getCurrentUrl().equals(url)) {
                return;
            }
            this.driver.navigate().to(url);

        }
        catch (TimeoutException e){
            System.out.println("page " + url + "load timeout");
        }
    }

    public boolean navigate(String url, String cssSelectorForWaitingElement)  {
        try {

            if (this.driver.getCurrentUrl().equals(url)) {
                return false;
            }
            this.driver.navigate().to(url);
            if(this.getWaitTool().waitForElement(By.cssSelector(cssSelectorForWaitingElement),PS.longWait) == null){
                System.out.println("页面：" + url + "过了10秒都没刷出来或未到指定页面");
                return false;
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


    public String sendRequest(Map<String ,String>  data ,String methodName,  String url ){
        String dataString = "";
        if(data != null ){
            dataString = "var data = new FormData();";
            for(Map.Entry<String, String> entry:data.entrySet()){
                dataString += "\ndata.append('"+ entry.getKey() + "','" + entry.getValue() + "');\n";
            }
        }
        //发送一个 ajax 请求,并创建一个元素来存返回的 请求值
        String script =
                "var xhr = new XMLHttpRequest();\n" +
                "     xhr.open('" + methodName + "', '" + url + "', false);\n" +
                "     xhr.onreadystatechange = function() {\n" +
                "        if(xhr.readyState == 4 && xhr.status == 200) {\n" +
                "           var para =  document.createElement(\"mySeleniumDiv\");\n" +
                "           document.getElementsByTagName(\"mySeleniumDiv\").innerHTML=xhr.responseText;\n" +
                "        }\n" +
                "     }\n" +
                "     xhr.send(data);";

        driver.executeScript(dataString + script);
        //获取返回值
        return (String) driver.executeScript("return document.getElementsByTagName(\"mySeleniumDiv\").innerHTML");
    }


    public void assertPostTrue(RCHttpJsonResultObject rcHttpJsonResultObject, String url){
        assertEquals("post "+ url +" 执行失败", "true", rcHttpJsonResultObject.getSuccess());
        assertEquals("post "+ url +" 执行失败", "true",  rcHttpJsonResultObject.getData());
        assertEquals("post "+ url +" 执行失败", "null" ,rcHttpJsonResultObject.getErrorCode());
        assertEquals("post "+ url +" 执行失败", "null" ,rcHttpJsonResultObject.getErrorMsg());
    }

    public void assertGetTrue(RCHttpJsonResultObject rcHttpJsonResultObject, String url){
        assertEquals("get "+ url +" 执行失败", "true", rcHttpJsonResultObject.getSuccess());
        assertEquals("get "+ url +" 执行失败", "", rcHttpJsonResultObject.getErrMsg());
    }

    public void assertDataExistInJason(List<JSONObject> jObjects, JSONObject jObject, String keyForFindObj){
        for (JSONObject ojb:jObjects) {
           assertTrue("没有找到要对比的对象", assertDataExistInJason(keyForFindObj, ojb, jObject));
        }


    }

    public boolean assertDataExistInJason(String keyForFindObj,JSONObject expectJObject  , JSONObject actualJObject){
        boolean isFind = false;
        Iterator<?> keys = actualJObject.keys();
        while( keys.hasNext() ) {
            String key = (String)keys.next();
            //若指定字段的值相同直接比较
            if (key.equals( keyForFindObj) &&  expectJObject.get(keyForFindObj).equals(actualJObject.get(keyForFindObj).toString() )){
                actualJObject.equals(expectJObject);
                isFind = true;
                return isFind;
            }

            //若字段是对象,则递归
            if ( actualJObject.get(key) instanceof JSONObject ) {
                if(assertDataExistInJason(keyForFindObj, expectJObject, actualJObject )){
                    return true;
                }
            }

            //若是数组则遍历数组递归
            if (actualJObject.get(key) instanceof JSONArray){
                JSONArray jsonArray =  (JSONArray) actualJObject.get(key);
                for (int i = 0;  i< jsonArray.length() ;i++){
                    if (assertDataExistInJason(keyForFindObj, expectJObject, jsonArray.getJSONObject(i) )){
                        return true ;
                    }
                }
            }
        }
        return  isFind;
    }






    public Object getConstructObjectFromJason(String responseString, Class<?> ObjectClass){
        JSONObject obj = new JSONObject(responseString);
        Object object = null;
        try {
            object = ObjectClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        //根据 json 的 key 字段,  来填充返回的结果
        for(String entry:obj.keySet()){
            try {
                TestHelper.setClassField(object,entry, obj.get(entry).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return object;
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
                if(this.navigate(url, cssSelectorForWaitingElement)){
                    return;
                }
                this.driver.navigate().refresh();
                if(null == this.getWaitTool().waitForElement(By.cssSelector(cssSelectorForWaitingElement), PS.longWait)){
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
        refresh(this.driver.getCurrentUrl(), cssSelectorForWaitingElement);
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
        return this.findItemNumberInOnePageByTextContains( indexItemSelector, itemName);
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
        return elementIsPresent( cssSector,  0,  childCssSector);
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
       return getPageNum( cssSelector, 0 ,regEx);

    }
    public int getPageNum(String cssSelector){
        return getPageNum(cssSelector, 0 ,"[^0-9]|/");
    }
    public int getPageNum(String cssSelector, int num){
        return getPageNum(cssSelector, num ,"[^0-9]|/");
    }


    public int getPageNum(String cssSelector, int num , String regEx){
        Matcher mat;
        try{
            this.getWaitTool().waitForJavaScriptCondition("return $(\"" + cssSelector + "\").text().indexOf(\"()\") == -1", 5);
            String tempString = this.driver.findElements(By.cssSelector(cssSelector)).get(num).getText();
            Pattern pat = Pattern.compile(regEx);
            mat = pat.matcher(tempString);
            System.out.println("the page num is " +  Integer.parseInt(mat.replaceAll("").trim()));
            return Integer.parseInt(mat.replaceAll("").trim());
        }catch (NoSuchElementException e){
            System.out.println(" the num is zero ");
            return 0;
        }
    }
    public int getPageNum(String cssSelector, int num, String childSelector,String regEx){
        try{
            this.getWaitTool().waitForJavaScriptCondition("return $(\"" + cssSelector + "\").text().indexOf(\"()\") == -1",PS.midWait);
            return Integer.parseInt(Pattern.compile(regEx).matcher(getText(cssSelector, num,  childSelector)).replaceAll("").trim());
        }catch (NoSuchElementException e){
            return 0;
        }

    }

    public int getPageNumInParentheses(String cssSelector){
        return  getPageNum(cssSelector, "（([0-9]*?)）");
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
        return clickElement(cssSelector, 0, cssSelectorWaitingElement);
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
            this.waitForElement(cssSector,PS.midWait);
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




    public boolean checkImage(String imageSelector, String imageSrc){
        return checkImage(imageSelector,  0 ,imageSrc);
    }


    public boolean checkImage(String imageSelector, int num, String imageSrc){
        try{
            return this.getAttribute(imageSelector,  "src",num).contains(imageSrc);
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
        return getText(cssSector, 0);
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
            FileUtils.copyFile(driver.getScreenshotAs(OutputType.FILE), new File( getScreenFileDir()+"/" +name+"_"+ (new Date()).getTime() + ".png"));
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
        return getAttribute( cssSector, Attribute, 0);
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
        clearAndSendKeys(cssSector,0,inputContents);
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
        sendKeys(cssSector, 0,  inputContents);
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

    //处理时间的工具
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
        return 0;
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
    public int findItemNumberInOnePageByTextContains(String itemsSelector, String itemText){
        int tagCount = this.getElementCount(itemsSelector);
        for(int i = 0; i<tagCount ;i++) {
            if (this.getText(itemsSelector,i).contains(itemText)){
                return i;
            }
        }
        return  -1;
    }
    public int findItemNumberInOnePageByAttributeContains(String itemsSelector, String attribute, String itemText){
        int tagCount = this.getElementCount(itemsSelector);
        for(int i = 0; i<tagCount ;i++) {
            if (this.getAttribute(itemsSelector,attribute,i).contains(itemText)){
                return i;
            }
        }
        return  -1;
    }
}

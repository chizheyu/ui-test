package org.ChiTest.WebComponent;

import org.ChiTest.WebComponent.InterFace.ImageSender;
import org.ChiTest.Page.Page;
import org.ChiTest.User.User;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriverException;
import org.ChiTest.congfig.ConfigFileReader;

import java.text.DateFormat;
import java.text.ParseException;

import static org.junit.Assert.*;

/**
 * Created by dugancaii on 11/25/2014.
 */
public class MarkDownInputBox extends CommentBox {
    public String getSpecialSign() {
        return specialSign;
    }

    private String filePath;
    private  String specialSign;

    public User getAtSomeOne() {
        return atSomeOne;
    }

    public void setAtSomeOne(User atSomeOne) {
        this.atSomeOne = atSomeOne;
    }


    private String photoInputSelector;

    private User atSomeOne;

    public String getHttpsLink() {
        return httpsLink;
    }

    private String httpsLink;

    public String getHttpLinkText() {
        return httpLinkText;
    }

    private String httpLinkText;

    public String getHttpsLinkText() {
        return httpsLinkText;
    }

    private String httpsLinkText;

    public String getHttpLink() {
        return httpLink;
    }

    private String httpLink;

    public String getBoxInputSelector(){
        return boxInputSelector;
    }
    public String getMarkDownConfirmSelector(){
        return boxSelector + " .clockwise.rotated.level.down.icon";
    }

    private String shortForBaseContent;

    public String getSendWithKeyboardsContent() {
        return sendWithKeyboardsContent;
    }

    private String sendWithKeyboardsContent;
    private DateFormat df;

    public String getBaseTestContent() {
        return baseTestContent;
    }
    public String getShortForBaseContent() {
        return shortForBaseContent;
    }

    private String baseTestContent;

    public String getImageFileName() {
        return imageFileName;
    }

    private String imageFileName;

    public MarkDownInputBox(String boxSelector, String sentItemSelector)  {
        super(boxSelector, sentItemSelector);
        boxSubmitSelector = boxSelector + " .clockwise.rotated";
        boxInputSelector = boxSelector + " textarea";
        this.sentItemSelector = sentItemSelector;

        imageFileName = "6B80D68F0817475DB84CF4BF6D2CF154.jpg";
        filePath = ConfigFileReader.class.getResource("/"+imageFileName).toString();

        specialSign = "<>《》~,.`1!@$%)+|-=\\\"；‘、、/-+-+。，、：“‘”’——……+=-|！@#￥$%……*（）{}【】？\"#";
        //todo  "&(" 这两个符号没出来, 先去掉下划线
        //specialSign = "_&(";

        //todo 无连接中文版的
        //httpsLink = "https://www.baidu.com/s?wd=%E5%8F%B0%E5%8C%97101&rsv_spt=1&issp=1&f=8&rsv_bp=0&ie=utf-8&tn=baiduhome_pg";
        //httpsLinkText = "https://www.baidu.com/s?wd=%E5%8F%B0%E5%8C%97101&rsv_spt=1&issp=1&f=8&rsv_bp=0&ie=utf-8&tn=baiduhome_pg";

        httpsLink = "https://www.baidu.com/";
        httpsLinkText = "https://www.baidu.com";
        //todo 等彭博改完 改成 www.baidu.com
        httpLink = "http://www.baidu.com/";
        httpLinkText = "www.baidu.com";
        //baseTestContent = "\n## h2 \n<em>ok</em> <html> www.baidu.com 你妈" + this.getSpecialSign() + " https://www.baidu.com\n";
        //todo 完整版
        baseTestContent = " \n## h2 \n<em>ok</em> "+ httpLinkText +"。。。" + this.getSpecialSign() +httpsLinkText +"中文";
        shortForBaseContent = "h2 ok "+ httpLinkText +"。。。"+ this.getSpecialSign()+ httpsLinkText + "中文";
        photoInputSelector = " input[name='tweetImg']";
        sendWithKeyboardsContent = "test send with keyboards";
    }
    public MarkDownInputBox(String boxSelector)  {
        this(boxSelector,"");
    }

    public MarkDownInputBox(String boxSelector , User atSomeOne)  {
        this(boxSelector,"");
        this.atSomeOne= atSomeOne;
    }

    public MarkDownInputBox(String boxSelector, String sentItemSelector , User atSomeOne) throws ParseException {
        this(boxSelector,sentItemSelector);
        this.atSomeOne= atSomeOne;
    }

    public void clickMDConfirmButton(Page cp){
        cp.clickElement(boxSelector + " .clockwise.rotated.icon");
        cp.waitLoadingIconInvisible();
    }
    public void clickMDConfirmButton(Page cp, String itemSelector){
        int itemCount = cp.getElementCount(itemSelector);
        cp.clickElement(boxSelector + " .clockwise.rotated.icon");
        cp.waitLoadingIconInvisible();
        cp.waitForItemCountChange(itemSelector,itemCount,10);
    }
    public void sendKeys(Page cp, String content ){
        cp.sendKeys(boxInputSelector, content );
    }
    public void clearAndSendKeys(Page cp, String content ){
        cp.clearAndSendKeys(boxInputSelector, content );
    }
    public void clickCodeIcon(Page cp ){
        cp.clickElement(boxSelector + " .code");
    }
    public void clickPreviewIcon(Page cp ){
        cp.clickElement(boxSelector + " .unhide" );
    }
    public void clickUnPreviewIcon(Page cp ){
        cp.clickElement(boxSelector + " .hide" );
    }


    public void sendInfoWithKeyboard(Page page, String MDItemSelector){
        int commentCount = page.getElementCount(MDItemSelector);
        page.sendKeys(this.getBoxInputSelector(), sendWithKeyboardsContent);
        page.getBuilder().sendKeys(Keys.CONTROL).sendKeys(Keys.ENTER).perform();
        assertTrue("无法用 Crtl + Enter 来发送信息", page.waitForItemCountChange(MDItemSelector, commentCount, 10));
        page.getBuilder().sendKeys(Keys.CONTROL).sendKeys(Keys.ENTER).release();
        try {
            page.moveToElement(MDItemSelector);
        }catch (WebDriverException e){
            System.out.println("control released");
        }
    }

    public String getMDPreviewSelector(){
        return boxSelector + " .preview .markdown.content";
    }

    public void verifyMarkdownContent(Page page, String type, String markdownContentSelector, boolean checkAt){
        page.waitForElement(markdownContentSelector + "  pre code", 6);
        assertTrue("代码消失" + type, page.elementIsPresent(markdownContentSelector + "  pre code", 3));
        assertTrue("图片上传有误"+ type+"the url is " + page.getDriver().getCurrentUrl(), page.elementIsPresent(markdownContentSelector + "  .bubble-markdown-image",10));
        assertTrue( type+"图片上传后的URl有误, 实际为 " +page.getAttribute(markdownContentSelector + "  .bubble-markdown-image", "src") ,
                page.getAttribute(markdownContentSelector + "  .bubble-markdown-image", "src").contains(page.getImgPreviewUrl()) ||
                        page.getAttribute(markdownContentSelector + "  .bubble-markdown-image", "src").contains(page.getImgStoreUrl())  );
        assertTrue("html消失，可能被转义了"+ type,page.elementIsPresent( markdownContentSelector + "  em",3));
        assertTrue("markdown预览解析失败"+ type,page.elementIsPresent( markdownContentSelector + "  h2",3));
        assertEquals("markdown里emoji表情显示不正确"+ type, page.getAttribute(markdownContentSelector +" .emoji","src"),
                page.getBaseUrl() + "static/emojis/+1.png");

        assertTrue("markdown里符号不正确 " + type, page.getText(markdownContentSelector).contains(specialSign));
        assertEquals("markdown里面的链接(http)不正确" + type, this.httpLink, page.getLink(markdownContentSelector + " .auto-link"));
        assertEquals("markdown里面的链接文本（http）不正确" + type, this.httpLinkText , page.getText(markdownContentSelector + " .auto-link"));
        assertEquals("markdown里面的链接(https开头)不正确"+ type,this.httpsLink , page.getLink(markdownContentSelector +" .auto-link",1));
        assertEquals("markdown里面的链接文本(https开头)不正确"+ type,this.httpsLinkText , page.getText(markdownContentSelector + " .auto-link", 1));


        if(checkAt){
            assertEquals("markdown里面的@某人的链接出错" + type,page.getBaseUrl()+"u/"+this.atSomeOne.getUserLoginName() , page.getLink(markdownContentSelector + " .at-someone") );
            assertEquals("markdown里面的@某人的文本出错" + type,"@"+this.atSomeOne.getUserName() , page.getText(markdownContentSelector + " .at-someone") );
        }
        //todo 重新开启
      //  assertEquals("markdown里面的洋葱候的表情显示不正确"+ type, page.getAttribute(markdownContentSelector +" .monkey", "src"),
                //page.getBaseUrl() + "static/coding-emotions/coding-emoji-28.png");

    }
    public void cancelAndCloseBox(Page page,String boxButtonSelector, String boxCancelSelector, String messageText){
        page.clickElement(boxButtonSelector, boxCancelSelector);
        page.clickElement(boxCancelSelector);
        page.waitElementInvisible(boxCancelSelector);
        if (page.elementIsPresent(boxCancelSelector,2) ) {
            page.clickElement(boxCancelSelector);
            page.waitElementInvisible(boxCancelSelector);
            System.out.println("click again");
        }
        assertFalse(messageText, page.elementIsPresent(boxCancelSelector, 2));
    }
    public void verifyEmojiHint(Page page, String emojiString){
        page.sendKeys(boxInputSelector, emojiString);
        page.clearAndSendKeys(boxInputSelector, page.getAttribute(boxInputSelector, "value"));
        assertNotEquals("表情的提示不正确", -1,page.findItemInOnePageByAttributeContains(".atwho-view .cur", "data-value",emojiString+":" ));
        page.sendKeys(Keys.ENTER);
       // page.getDriver().getKeyboard().sendKeys(Keys.ENTER);
    }
    public void verifyCanNotAtSomeoneHint(Page page, String contentInputSelector){
        page.sendKeys(contentInputSelector, "@codi");
        assertFalse("@某人出现提示", page.elementIsPresent(".cur", 2));
        page.sendKeys(contentInputSelector, "ng");
    }
    public void verifyCanAtSomeoneHint(Page page){
        page.sendKeys(boxInputSelector, "@" + atSomeOne.getUserName());
        assertEquals("@提示出错", atSomeOne.getUserName(), page.getText(".atwho-view .atwho-view-ul li[data-value*='@']"));
        page.sendKeys(Keys.ENTER);
        assertTrue("点击@提示失败", page.getAttribute(boxInputSelector, "value").contains("@" + atSomeOne.getUserName()));
    }
    public void verifyCanAtSomeoneHint(Page page, String markDownInputSelector){
        page.sendKeys(markDownInputSelector, "@" + atSomeOne.getUserName());
        assertEquals("@提示出错", atSomeOne.getUserName(), page.getText(".atwho-view .atwho-view-ul li[data-value*='@']"));
        page.sendKeys(Keys.ENTER);
        assertTrue("点击@提示失败", page.getAttribute(markDownInputSelector, "value").contains("@" + atSomeOne.getUserName()));
    }
    public void verifyMarkDownCodeContent(Page page,String codeContent){
        page.clickElement(boxSelector + " .code.icon");
        page.sendKeys(boxSelector + " textarea", codeContent);
    }
    public void sendMarkdownContent(Page page, User sender,User atSomeOne, ImageSender imageSender) {
        page.clearInput(boxSelector + " textarea");
        this.verifyEmojiHint(page, ":+1");
        this.verifyEmojiHint(page, ":听音乐");
        page.sendKeys(boxSelector + " textarea", baseTestContent);
        imageSender.sendImgInInput(page, boxSelector + " " + photoInputSelector);
        page.sendKeys(Keys.ARROW_DOWN);
        sender.setMessageMark(page.getMark());

        page.sendKeys(boxSelector +" textarea", sender.getMessageMark());
        if(atSomeOne!= null){
            this.setAtSomeOne(atSomeOne);
            this.verifyCanAtSomeoneHint(page);
        }else {
            this.verifyCanNotAtSomeoneHint(page, boxSelector +" textarea");
        }
        verifyMarkDownCodeContent(page, "hello test" );
    }

    public void sendMarkDownContentForActivity(Page page, ImageSender imageSender){
        this.verifyEmojiHint(page, ":听音乐");
        System.out.println("发送弹框验证上传图片 ");
        imageSender.sendImgInInput(page, boxSelector + " " + photoInputSelector);
        page.clickElement(boxSelector);
        this.setAtSomeOne(atSomeOne);
        this.verifyCanAtSomeoneHint(page);
        page.sendKeys(boxSelector + " textarea", "https://www.baidu.com\n");
        page.clickElement(boxSelector + " .code.icon");
        page.sendKeys(boxSelector + " textarea", "hello test");
        this.clickMDConfirmButton(page);
    }

    public void testMarkDownPreview(Page page, String previewContentSelector){
        page.clickElement(boxSelector +" .unhide.icon", previewContentSelector);
        page.waitElementInvisible(".loading.icon");
        this.verifyMarkdownContent(page, "[预览]", previewContentSelector,false);
        page.clickElement(boxSelector +" .hide.icon");

        assertFalse("取消预览后，预览按钮没有消失", page.elementIsPresent(boxSelector +" .hide.icon", 1));
        assertFalse("取消预览后，预览内容没有消失",
                page.elementIsPresent(previewContentSelector.replace(".preview",".preview.active"),1));
    }

    public void sendImgFilePath(Page page, String cssSelectorForImgInput) {
        if( !page.getDriver().getClass().getName().toLowerCase().contains("firefox")) {
            page.sendKeys(cssSelectorForImgInput, filePath.substring(6, filePath.length()));
        }else{
            page.sendKeys(cssSelectorForImgInput, filePath);
        }
    }
    public void sendFile(Page page, String cssSelectorForImgInput, String filePath){
        if(page.isChromeDriver()) {
            page.sendKeys(cssSelectorForImgInput, filePath.substring(6, filePath.length()));
        }else {
                page.sendKeys(cssSelectorForImgInput, filePath);
        }
    }


    public ImageSender getComplicateImageSender(){
        return new ImageSender(){
            @Override
            public void sendImgInInput(Page messagePage, String cssSelectorForImgInput )  {
                messagePage.clickElement( boxSelector + " .photo.icon"  );
                messagePage.getDriver().executeScript("$(\"#import-image-input\").attr(\"style\",\"\")");
                messagePage.getWaitTool().waitForElement(By.cssSelector("#import-image-input"), 5);
                sendImgFilePath(messagePage, "#import-image-input");
                messagePage.waitElementInvisible(".loading.icon");
                messagePage.getWaitTool().waitForElement(By.cssSelector("#im12"), 1);
                messagePage.clickElement("#insert_image_lmd_modal .actions .button.gray");
                messagePage.getWaitTool().waitForElement(By.cssSelector("#im12"), 1);
                //messagePage.clickElement("#insert_image_lmd_modal .close.icon");
            }
        };
    }
    public ImageSender getNormalImageSender(){
        return new ImageSender(){
            @Override
            public void sendImgInInput(Page messagePage, String cssSelectorForImgInput ) {
                messagePage.getDriver().executeScript("$(\""+cssSelectorForImgInput+"\").attr(\"style\",\"\")" );
                messagePage.getWaitTool().waitForElement(By.cssSelector(cssSelectorForImgInput), 5);
                sendImgFilePath(messagePage,cssSelectorForImgInput);
                messagePage.waitElementInvisible(".loading.icon");
            }
        };
    }
    public String getPhotoButtonSelector(){
        return boxSelector + " " + ".photo.icon ";
    }


    public void sendEmojiThroughButton(Page page,  String monkeyEmojiSelector){
        page.clearInput(boxInputSelector);
        page.clickElement(" .emotion", ".emotions .emoji");
        page.clickElement(" .emotions .emoji");
        page.assertAttributeEquals("发送经典表情失败", ":smiley:", boxInputSelector, "value" );

        page.clearInput(boxInputSelector);
        if(page.isPhantomJs()){
            if(!page.elementIsPresent(monkeyEmojiSelector)) {
                page.clickElement(".emotion", monkeyEmojiSelector);
            }
        }else {
            if(page.waitElementInvisible(monkeyEmojiSelector)) {
                page.clickElement(".emotion", monkeyEmojiSelector);
            }
        }

        page.clickElement(monkeyEmojiSelector, ".coding-emoji");
        page.clickElement(".coding-emoji");
        page.assertAttributeEquals("发送猴子表情失败", ":哈哈:", boxInputSelector, "value" );
        page.clearInput(boxInputSelector);
    }
}

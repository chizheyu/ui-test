package org.ChiTest.Bubbling;

import org.ChiTest.Page.Page;
import org.ChiTest.User.User;

import java.util.Date;
import java.util.List;

/**
 * Created by dugancaii on 8/18/2014.
 */
public class Bubbling {
    private User sender;
    private Date time;
    private String content;
    private String bubblingDetailUrl;
    private BubblingFileReader bubblingFileReader;


    public List<String> getCommentList() {
        return commentList;
    }

    private List<String> commentList = null;



    public Bubbling( User sender,String content ){
        this.sender = sender;
        this.content = content;
        bubblingFileReader = new BubblingFileReader();
    }
    public Bubbling( User sender,String content, Date time ){
        this.sender = sender;
        this.content = content;
        this.time = time;
    }
    public void sendBubblingWithTimeInterval() throws InterruptedException {
        Thread.sleep(10000);
        sendBubbling();
    }

    public void sendBubbling(){
        Page senderPage = this.sender.getPage();
        senderPage.navigate(this.sender.getBubblingLink(), ".feed-editor textarea");
        String bubblingText = senderPage.getText(bubblingFileReader.getBubblingTextContent());
        senderPage.clearAndSendKeys(".feed-editor textarea", this.content);
        senderPage.clickElement(".feed-editor .btn-submit");
        senderPage.waitForContentChange(bubblingFileReader.getBubblingTextContent(), bubblingText, 5);
        bubblingDetailUrl = getBubblingUrl(senderPage);
    }
    public String getBubblingDetailUrl(){
        return bubblingDetailUrl;
    }
    private String getBubblingUrl(Page page){
        return  page.getLink(".bubble-detail .info span:nth-child(3) a");
    }
    public User getSender() {
        return sender;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}

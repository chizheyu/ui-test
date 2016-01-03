package org.ChiTest.Message;

import org.ChiTest.Page.Page;
import org.ChiTest.User.User;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;

/**
 * Created by dugancaii on 8/7/2014.
 */
public class Message {
    private User sender;
    private User receiver;
    private String content;
    private MessageFileReader messageFileReader;

    Message(User receiver, User sender){
        this.sender = sender;
        this.receiver = receiver;
        this.messageFileReader = new MessageFileReader();
    }
    Message(User receiver, User sender,String content){
        this(receiver,sender);
        this.content = content;
    }

    public User getSender() {
        return sender;
    }
    public User getReceiver() {
        return receiver;
    }
    public String getContent(){
        return  content;
    }

    public void sendSampleMessage()  {
        Page messagePage = sender.getPage();
        messagePage.navigate(sender.getAllUserMessageLink(), messageFileReader.getMessageSendButtonInMessagePage());
        receiver.addMessageReceivedNum();
        messagePage.clickElement(messageFileReader.getMessageSendButtonInMessagePage(),
                messageFileReader.getMessageReceiverNameInput());
        messagePage.clearInput(messageFileReader.getMessageContentInput());
        messagePage.sendKeys(messageFileReader.getMessageReceiverNameInput(), receiver.getUserName());
        messagePage.sendKeys(messageFileReader.getMessageContentInput(), content);
        messagePage.clickElement(messageFileReader.getMessageConfirmButtonInBox());
        messagePage.verifyHint("发送成功！");
        messagePage.waitElementInvisible(messageFileReader.getMessageConfirmButtonInBox());
    }
    public void verifySampleMessage() throws InterruptedException, ParseException {
        Page messagePage = receiver.getPage();
        messagePage.refresh(receiver.getAllUserMessageLink(), "#inner-menu .item:nth-child(3)");
        messagePage.getWaitTool().waitForJavaScriptCondition("return $(\"#inner-menu .item:nth-child(3)\").text().length == 6",5);
        receiver.setMessageNotReadNum(messagePage.getPageNum("#inner-menu .item:nth-child(3)", "[^0-9]|/"));
        assertEquals("私信未读计数有误", receiver.getMessageNotReadNum(),
                receiver.getMessageNotReadBaseNum() + receiver.getMessageReceivedNum());
        assertEquals("会话显示的最后一条信息内容不正确", content, messagePage.getText(".content .summary span").trim());
    }
}

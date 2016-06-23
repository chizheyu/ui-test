package org.ChiTest.rc.Email;

import org.ChiTest.Page.Page;
import org.junit.Ignore;
import org.openqa.selenium.By;

import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by dugancaii on 9/25/2014.
 */

@Ignore()
public class EmailTest {
    private EmailFileReader emailFileReader;
    private Email email;
    public EmailTest(){
        emailFileReader = new EmailFileReader();
        email = new Email(emailFileReader.getUserName(),"163.com",emailFileReader.getPassword());
    }
    public EmailTest(Email email){
        this.email = email;
        emailFileReader = new EmailFileReader();
    }
    public void checkEmail(Page zombiePage, Set mailCheckList) {
        if(!emailFileReader.getCheckEmail().equals("1")){
            return;
        }
        login163Email(zombiePage);
        checkAllEmailItem(zombiePage, mailCheckList);
        if(!mailCheckList.isEmpty()){
            String message = " 以下邮件没有收到：\n ";
            for(Object mailContent : mailCheckList){
                message += mailContent +"  ||  ";
            }
            mailCheckList.clear();
            assertEquals(message,1,2);
        }
    }
    public void checkEmail(Page zombiePage, Set mailCheckList, Email email) throws InterruptedException {
        if(!emailFileReader.getCheckEmail().equals("1")){
            return;
        }
        login163Email(zombiePage, email);
        checkAllEmailItem(zombiePage,mailCheckList);
        //checkUnreadEmailItem(zombiePage, mailCheckList);
        if(!mailCheckList.isEmpty()){
            String message = " 以下邮件没有收到：\n ";
            for(Object mailContent : mailCheckList){
                message += mailContent +"  ||  ";
            }
            mailCheckList.clear();
            assertEquals(message,1,2);
        }
    }
    public void checkAllEmailItem(Page zombiePage, Set mailCheckList)  {
        zombiePage.waitForElement(emailFileReader.getInboxButton(),7);
        zombiePage.clickElement(emailFileReader.getInboxButton(), emailFileReader.getEmailItemInMailList());
        int mailCount = zombiePage.getElementCount(emailFileReader.getEmailItemInMailList());
        int i = 0;
        while (i<20 && !mailCheckList.isEmpty() && i < mailCount) {
            zombiePage.clickElement(emailFileReader.getEmailItemInMailList(), i % 20, emailFileReader.getEmailTitleInDetailPage());
            zombiePage.getDriver().switchTo().frame(zombiePage.getAttribute(emailFileReader.getEmailIFrame(), "id"));
            if(zombiePage.elementIsPresent(emailFileReader.getEmailContentItem())) {
                checkNotificationEmail(zombiePage.getText(emailFileReader.getEmailContentItem(), 1), mailCheckList);
            }
            if(!zombiePage.isChromeDriver()) {
                zombiePage.getDriver().switchTo().parentFrame();
            }
            zombiePage.back(emailFileReader.getEmailItemInMailList());
            zombiePage.refresh(emailFileReader.getEmailItemInMailList());
            //刷新不能去掉,但刷新后会自动到第一页
            i++;
            /*
            if(i % 20 == 0){
                String pageIndex = zombiePage.getText(emailFileReader.getPageIndex());
                zombiePage.clickElement(emailFileReader.getNextPage());
                zombiePage.waitForContentChange(emailFileReader.getPageIndex(),pageIndex,3);
            }
            */
        }
    }
    public void checkUnreadEmailItem(Page zombiePage, Set mailCheckList) throws InterruptedException {
        zombiePage.clickElement(emailFileReader.getUnReadBoxButton(), emailFileReader.getUnreadMark());
        while (zombiePage.elementIsPresent(emailFileReader.getUnreadMark(),2) && !mailCheckList.isEmpty()) {
            zombiePage.clickElement(emailFileReader.getEmailItemInMailList(),emailFileReader.getEmailIFrame());
            zombiePage.getDriver().switchTo().frame(zombiePage.getAttribute(emailFileReader.getEmailIFrame(), "id"));
            checkNotificationEmail(zombiePage.getText(emailFileReader.getEmailContentItem(), 1), mailCheckList);
            zombiePage.getDriver().switchTo().parentFrame();
            zombiePage.back(emailFileReader.getUnreadMark());
            zombiePage.refresh(emailFileReader.getUnreadMark());
        }
    }

    public void checkNotificationEmail(String emailContent, Set mailCheckList){
        if(mailCheckList.contains(emailContent)){
            mailCheckList.remove(emailContent);
            return;
        }
        if(emailContent.contains("秒重置了你的账号密码") ){
            mailCheckList.remove("秒重置了你的账号密码");
        }
    }
    public void clearUnreadEmail(Page zombiePage){
        login163Email( zombiePage);
        zombiePage.clickElement(emailFileReader.getUnReadBoxButton());
        if(zombiePage.elementIsPresent(emailFileReader.getUnreadMark(),3)) {
            zombiePage.clickElement(emailFileReader.getSetAlreadyReadButton());
        }
    }
    public void login163Email(Page zombiePage){
        login163Email( zombiePage, email);
    }

    public void login163Email(Page zombiePage, Email email){
        zombiePage.navigate("http://mail.163.com/");
        if(zombiePage.getWaitTool().waitForElement(By.cssSelector(emailFileReader.getUserNameInput()),7) != null){
            zombiePage.clearAndSendKeys(emailFileReader.getUserNameInput(), email.getEmailName());
            zombiePage.clearAndSendKeys(emailFileReader.getPasswordInput(), email.getPassword());
            zombiePage.clickElement(emailFileReader.getConfirmButton(), emailFileReader.getInboxButton());
            if(!zombiePage.elementIsPresent(emailFileReader.getInboxButton())){
                zombiePage.clickElement(emailFileReader.getConfirmButton(), emailFileReader.getInboxButton());
            }
        }
    }

}

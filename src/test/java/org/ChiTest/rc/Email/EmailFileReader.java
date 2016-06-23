package org.ChiTest.rc.Email;

import org.ChiTest.congfig.ConfigFileReader;

/**
 * Created by dugancaii on 11/19/2014.
 */
public class EmailFileReader {
    private ConfigFileReader emailFileReader;
    private String emailTitleInDetailPage;

    public String getCheckEmail() {
        return checkEmail;
    }

    private String checkEmail;
    public EmailFileReader(){
        emailFileReader = new ConfigFileReader("/rc/EmailConfig.properties");
        userName = emailFileReader.getValue("userName");
        password = emailFileReader.getValue("password");
        emailTitle = emailFileReader.getValue("emailTitle");
        userNameInput = emailFileReader.getValue("userNameInput");
        passwordInput = emailFileReader.getValue("passwordInput");
        confirmButton = emailFileReader.getValue("confirmButton");
        emailItemInMailList = emailFileReader.getValue("EmailItemInMailList");
        unreadMark = emailFileReader.getValue("unreadMark");
        unReadBoxButton = emailFileReader.getValue("UnReadBoxButton");
        emailIFrame = emailFileReader.getValue("EmailIFrame");
        emailContentItem = emailFileReader.getValue("EmailContentItem");
        emailContent = emailFileReader.getValue("emailContent");
        setAlreadyReadButton = emailFileReader.getValue("setAlreadyReadButton");
        inboxButton = emailFileReader.getValue("inboxButton");
        nextPage = emailFileReader.getValue("nextPage");
        pageIndex = emailFileReader.getValue("pageIndex");
        emailTitleInDetailPage = emailFileReader.getValue("emailTitleInDetailPage");
        checkEmail = emailFileReader.getValue("checkEmail");
    }
    public String getEmailTitleInDetailPage() {
        return emailTitleInDetailPage;
    }

    public String getUserName() {
        return userName;
    }

    public String getConfirmButton() {
        return confirmButton;
    }



    public String getEmailItemInMailList() {
        return emailItemInMailList;
    }

    public String getUnreadMark() {
        return unreadMark;
    }

    public String getUnReadBoxButton() {
        return unReadBoxButton;
    }

    public String getEmailIFrame() {
        return emailIFrame;
    }

    private String emailIFrame;
    private String unReadBoxButton;
    private String unreadMark;
    private String emailItemInMailList;
    private String confirmButton;
    private String userName;

    public String getPageIndex() {
        return pageIndex;
    }

    private String pageIndex;

    public String getNextPage() {
        return nextPage;
    }

    private String nextPage;

    public String getInboxButton() {
        return inboxButton;
    }

    private String inboxButton;

    public String getSetAlreadyReadButton() {
        return setAlreadyReadButton;
    }

    private String setAlreadyReadButton;

    public String getEmailContent() {
        return emailContent;
    }

    private String emailContent;

    public String getEmailContentItem() {
        return emailContentItem;
    }

    private String emailContentItem;

    public String getPasswordInput() {
        return passwordInput;
    }

    private String passwordInput;

    public String getUserNameInput() {
        return userNameInput;
    }

    private String userNameInput;

    public String getEmailTitle() {
        return emailTitle;
    }

    private String emailTitle;

    public String getPassword() {
        return password;
    }

    private String password;

}

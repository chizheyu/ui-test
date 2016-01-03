package org.ChiTest.Message;

import reference.ConfigFileReader;

/**
 * Created by dugancaii on 11/25/2014.
 */
public class MessageFileReader {
    public String getMessageImgInput() {
        return messageImgInput;
    }

    private String messageImgInput;

    public String getMessagePreviewButton() {
        return messagePreviewButton;
    }

    private String messagePreviewButton;

    public String getMessageImgRemove() {
        return messageImgRemove;
    }

    private String messageImgRemove;

    public String getMessagePreviewContent() {
        return messagePreviewContent;
    }

    private String messagePreviewContent;

    public String getMessagePreviewCancelButton() {
        return messagePreviewCancelButton;
    }

    public String getMessageBoxCancelButton() {
        return messageBoxCancelButton;
    }

    public String getMessageBoxCloseButton() {
        return messageBoxCloseButton;
    }

    public String getMessageContentInput() {
        return messageContentInput;
    }

    public String getMessageReceiverNameInput() {
        return messageReceiverNameInput;
    }

    public String getMessageCodeButton() {
        return messageCodeButton;
    }

    public String getMessageConfirmButtonInBox() {
        return messageConfirmButtonInBox;
    }

    public String getMessageContentInSessionPage() {
        return messageContent;
    }

    public String getMessageContentInputInSession() {
        return messageContentInputInSession;
    }

    public String getMessageCodeButtonInSession() {
        return messageCodeButtonInSession;
    }

    public String getMessagePreviewButtonInSession() {
        return messagePreviewButtonInSession;
    }

    public String getMessagePreviewCancelButtonInSession() {
        return messagePreviewCancelButtonInSession;
    }

    public String getMessagePreviewContentInSession() {
        return messagePreviewContentInSession;
    }

    private String messagePreviewContentInSession;
    private String messagePreviewCancelButtonInSession;
    private String messagePreviewButtonInSession;
    private String messageCodeButtonInSession;
    private String messageContentInputInSession;
    private String messageContent;
    private String messageConfirmButtonInBox;
    private String messageCodeButton;
    private String messageReceiverNameInput;
    private String messageContentInput;
    private String messageBoxCloseButton;
    private String messageBoxCancelButton;
    private String messagePreviewCancelButton;

    public String getMessageSendButtonInMessagePage() {
        return messageSendButtonInMessagePage;
    }

    private String messageSendButtonInMessagePage;

    public String getMessageMarkDownBoxInSession() {
        return messageMarkDownBoxInSession;
    }

    private String messageMarkDownBoxInSession;

    public String getMessageRefreshIconInSessionPage() {
        return messageRefreshIconInSessionPage;
    }

    private String messageRefreshIconInSessionPage;

    public String getMessageMarkDownBox() {
        return messageMarkDownBox;
    }

    private String messageMarkDownBox;

    public String getMessageSenderNameInSessionList() {
        return messageSenderNameInSessionList;
    }

    private String messageSenderNameInSessionList;
    private ConfigFileReader emailFileReader;
    public MessageFileReader(){
        emailFileReader = new ConfigFileReader("/MessageConfig.properties");
        messageImgInput = emailFileReader.getValue("messageImgInput");
        messagePreviewButton = emailFileReader.getValue("messagePreviewButton");
        messageImgRemove = emailFileReader.getValue("messageImgRemove");
        messagePreviewContent = emailFileReader.getValue("messagePreviewContent");
        messagePreviewCancelButton = emailFileReader.getValue("messagePreviewCancelButton");
        messageBoxCancelButton = emailFileReader.getValue("messageBoxCancelButton");
        messageBoxCloseButton = emailFileReader.getValue("messageBoxCloseButton");
        messageContentInput = emailFileReader.getValue("messageContentInput");
        messageReceiverNameInput = emailFileReader.getValue("messageReceiverNameInput");
        messageCodeButton = emailFileReader.getValue("messageCodeButton");
        messageConfirmButtonInBox = emailFileReader.getValue("messageConfirmButtonInBox");
        messageContent = emailFileReader.getValue("messageContent");
        messageContentInputInSession = emailFileReader.getValue("messageContentInputInSession");
        messageCodeButtonInSession = emailFileReader.getValue("messageCodeButtonInSession");
        messagePreviewButtonInSession = emailFileReader.getValue("messagePreviewButtonInSession");
        messagePreviewCancelButtonInSession = emailFileReader.getValue("messagePreviewCancelButtonInSession");
        messagePreviewContentInSession = emailFileReader.getValue("messagePreviewContentInSession");
        messageMarkDownBoxInSession = emailFileReader.getValue("messageMarkDownBoxInSession");
        messageSendButtonInMessagePage = emailFileReader.getValue("messageSendButtonInMessagePage");
        messageRefreshIconInSessionPage = emailFileReader.getValue("messageRefreshIconInSessionPage");
        messageMarkDownBox = emailFileReader.getValue("messageMarkDownBox");
        messageSenderNameInSessionList = emailFileReader.getValue("messageSenderNameInSessionList");
    }
}

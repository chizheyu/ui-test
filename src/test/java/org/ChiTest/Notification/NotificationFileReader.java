package org.ChiTest.Notification;

import reference.ConfigFileReader;

/**
 * Created by dugancaii on 10/11/2014.
 */
public class NotificationFileReader {
    private  ConfigFileReader fileReader;


    public String getCommentIconClass() {
        return commentIconClass;

    }

    private String commentIconClass;

    public String getProjectMemberIconClass() {
        return projectMemberIconClass;
    }

    private String projectMemberIconClass;

    public String getFriendIconClass() {
        return friendIconClass;
    }

    private String friendIconClass;

    public String getFavorIconClass() {
        return favorIconClass;
    }

    private String favorIconClass;

    public String getNotificationThirdLink() {
        return notificationThirdLink;
    }

    private String notificationThirdLink;

    public String getNotificationSecondLink() {
        return notificationSecondLink;
    }

    private String notificationSecondLink;

    public String getNotificationContent() {
        return notificationContent;
    }

    private String notificationContent;

    public String getNotificationTime() {
        return notificationTime;
    }

    private String notificationTime;

    public String getNotificationIcon() {
        return notificationIcon;
    }

    private String notificationIcon;

    public String getNotificationSponsor() {
        return notificationSponsor;
    }

    private String notificationSponsor;

    public String getAtSomeoneIconClass() {
        return atSomeoneIconClass;
    }

    private String atSomeoneIconClass;

    public String getChatIconClass() {
        return chatIconClass;
    }

    private String chatIconClass;

    public String getReadAll() {
        return readAll;
    }

    private String readAll;

    public String getReadThisPage() {
        return readThisPage;
    }

    private String readThisPage;

    public String getUnreadCount() {
        return unreadCount;
    }

    private String unreadCount;

    public String getUnreadItemIcon() {
        return unreadItemIcon;
    }

    public String getTotalItemIcon() {
        return totalItemIcon;
    }

    private String totalItemIcon;



    private String unreadItemIcon;

    public String getAtSomeoneItemIcon() {
        return atSomeoneItemIcon;
    }

    private String atSomeoneItemIcon;

    public String getCommentItemIcon() {
        return commentItemIcon;
    }

    private String commentItemIcon;

    public String getSystemItemIcon() {
        return systemItemIcon;
    }

    private String systemItemIcon;

    public String getActiveItemText() {
        return activeItemText;
    }

    private String activeItemText;

    public String getNoThing() {
        return noThing;
    }

    private String noThing;

    public String getPageTitle() {
        return pageTitle;
    }

    private String pageTitle;

    public String getTaskIconClass() {
        return taskIconClass;
    }

    private String taskIconClass;

    public String getInboxIconClass() {
        return inboxIconClass;
    }

    private String inboxIconClass;

    public String getUnReadNotificationItem() {
        return unReadNotificationItem;
    }

    private String unReadNotificationItem;

    public String getNotificationItem() {
        return notificationItem;
    }

    private String notificationItem;



    public NotificationFileReader() {
        fileReader = new ConfigFileReader("/NotificationConfig.properties");
        this.chatIconClass = fileReader.getValue("chatIconClass");
        this.inboxIconClass = fileReader.getValue("inboxIconClass");
        this.atSomeoneIconClass = fileReader.getValue("atSomeoneIconClass");
        this.notificationContent = fileReader.getValue("notificationContent");
        this.notificationIcon = fileReader.getValue("notificationIcon");
        this.notificationSponsor = fileReader.getValue("notificationSponsor");
        this.notificationTime = fileReader.getValue("notificationTime");
        this.notificationSecondLink = fileReader.getValue("notificationSecondLink");
        this.notificationThirdLink = fileReader.getValue("notificationThirdLink");
        this.commentIconClass = fileReader.getValue("commentIconClass");
        this.favorIconClass = fileReader.getValue("favorIconClass");
        this.friendIconClass = fileReader.getValue("friendIconClass");
        this.projectMemberIconClass = fileReader.getValue("projectMemberIconClass");
        this.readAll = fileReader.getValue("readAll");
        this.readThisPage = fileReader.getValue("readThisPage");
        this.unreadCount = fileReader.getValue("unreadCount");
        this.unreadItemIcon = fileReader.getValue("unreadItemIcon");
        this.totalItemIcon = fileReader.getValue("totalItemIcon");
        this.atSomeoneItemIcon = fileReader.getValue("atSomeoneItemIcon");
        this.commentItemIcon = fileReader.getValue("commentItemIcon");
        this.systemItemIcon = fileReader.getValue("systemItemIcon");
        this.pageTitle = fileReader.getValue("pageTitle");
        this.activeItemText = fileReader.getValue("activeItemText");
        this.noThing = fileReader.getValue("noThing");
        this.taskIconClass = fileReader.getValue("taskIconClass");
        this.unReadNotificationItem = fileReader.getValue("unReadNotificationItem");
        this.notificationItem = fileReader.getValue("notificationItem");


    }


}

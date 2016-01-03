package org.ChiTest.Activity;

import reference.ConfigFileReader;

/**
 * Created by dugancaii on 10/7/2014.
 */
public class ActivityFileReader {
    private  ConfigFileReader fileReader;
    public ActivityFileReader() {
        fileReader = new ConfigFileReader("/ActivityConfig.properties");
        activityItem = fileReader.getValue("activityItem");
        activityTime = fileReader.getValue("activityTime");
        activityContent = fileReader.getValue("activityContent");
        activitySponsor = fileReader.getValue("activitySponsor");
        activityIcon = fileReader.getValue("activityIcon");
        activitySponsorAvatarLink = fileReader.getValue("activitySponsorAvatarLink");
        activitySponsorAvatarImg = fileReader.getValue("activitySponsorAvatarImg");
        userSpace = fileReader.getValue("userSpace");
        topicChatIconClass = fileReader.getValue("topicChatIconClass");
        commentIconClass = fileReader.getValue("commentIconClass");
        userIconClass = fileReader.getValue("userIconClass");
        projectIconClass = fileReader.getValue("projectIconClass");
        actionLinkInActivity = fileReader.getValue("actionLinkInActivity");
        targetLinkInActivity = fileReader.getValue("targetLinkInActivity");
        otherActivityTagInProjectHomePage = fileReader.getValue("otherActivityTagInProjectHomePage");
        activeTagInProjectHomePage = fileReader.getValue("activeTagInProjectHomePage");
        sponsorLinkInActivity = fileReader.getValue("sponsorLinkInActivity");
        topicActivityTagInProjectHomePage = fileReader.getValue("topicActivityTagInProjectHomePage");
        actionTopicLinkInActivity = fileReader.getValue("actionTopicLinkInActivity");
        activityTimeLine = fileReader.getValue("activityTimeLine");
        taskIconClass = fileReader.getValue("taskIconClass");
        deadlineIconClass = fileReader.getValue("deadlineIconClass");
        priorityIconClass = fileReader.getValue("priorityIconClass");
        taskActivityTagInProjectHomePage = fileReader.getValue("taskActivityTagInProjectHomePage");
        showMoreActivity = fileReader.getValue("showMoreActivity");
        activityTabInUserCenter = fileReader.getValue("activityTabInUserCenter");
        openFolderIconClass = fileReader.getValue("openFolderIconClass");
        documentActivityTagInProjectHomePage = fileReader.getValue("documentActivityTagInProjectHomePage");
        fileIconClass = fileReader.getValue("fileIconClass");

    }

    public String getPriorityIconClass() {
        return priorityIconClass;
    }

    public String getTaskActivityTagInProjectHomePage() {
        return taskActivityTagInProjectHomePage;
    }

    private String taskActivityTagInProjectHomePage;

    public String getDocumentActivityTagInProjectHomePage() {
        return documentActivityTagInProjectHomePage;
    }

    private String documentActivityTagInProjectHomePage;
    private String priorityIconClass;
    private String deadlineIconClass;





    public String getFileIconClass() {
        return fileIconClass;
    }

    private String fileIconClass;

    public String getShowMoreActivity() {
        return showMoreActivity;
    }

    private String showMoreActivity;

    public String getTaskIconClass() {
        return taskIconClass;
    }

    public String getDeadlineIconClass() {
        return deadlineIconClass;
    }
    private String taskIconClass;
    private String activitySponsorAvatarImg;

    public String getActivityItem() {
        return activityItem;
    }

    public String getActivitySponsorAvatarImg() {
        return activitySponsorAvatarImg;
    }

    public String getTargetLinkInActivity() {
        return targetLinkInActivity;
    }

    public String getOtherActivityTagInProjectHomePage() {
        return otherActivityTagInProjectHomePage;
    }

    public String getSponsorLinkInActivity() {
        return sponsorLinkInActivity;
    }

    private String sponsorLinkInActivity;
    private String otherActivityTagInProjectHomePage;
    private String targetLinkInActivity;

    public String getTopicActivityTagInProjectHomePage() {
        return topicActivityTagInProjectHomePage;
    }

    private String topicActivityTagInProjectHomePage;
    private String activityItem;

    public String getActiveTagInProjectHomePage() {
        return activeTagInProjectHomePage;
    }

    private String activeTagInProjectHomePage;

    public String getActionLinkInActivity() {
        return actionLinkInActivity;
    }

    private String actionLinkInActivity;

    public String getActivitySponsorAvatarLink() {
        return activitySponsorAvatarLink;
    }

    private String activitySponsorAvatarLink;

    public String getActivityIcon() {
        return activityIcon;
    }

    private String activityIcon;

    public String getProjectIconClass() {
        return projectIconClass;
    }

    private String projectIconClass;

    public String getUserIconClass() {
        return userIconClass;
    }

    private String userIconClass;

    public String getCommentIconClass() {
        return commentIconClass;
    }

    private String commentIconClass;

    public String getTopicChatIconClass() {
        return topicChatIconClass;
    }

    private String topicChatIconClass;

    public String getOpenFolderIconClass() {
        return openFolderIconClass;
    }

    private String openFolderIconClass;

    public String getUserSpace() {
        return userSpace;
    }

    private String userSpace;

    public String getActivityTime() {
        return activityTime;
    }

    public String getActivityContent() {
        return activityContent;
    }

    public String getActivitySponsor() {
        return activitySponsor;
    }

    private String activitySponsor;
    private String activityContent;
    private String activityTime;

    public String getActivityTimeLine() {
        return activityTimeLine;
    }

    private String activityTimeLine;

    public String getActionTopicLinkInActivity() {
        return actionTopicLinkInActivity;
    }

    private String actionTopicLinkInActivity;

    public String getActivityTabInUserCenter() {
        return activityTabInUserCenter;
    }

    private String activityTabInUserCenter;

}

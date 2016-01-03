package org.ChiTest.Topic;

import reference.ConfigFileReader;

/**
 * Created by dugancaii on 10/7/2014.
 */
public class TopicFileReader {
    private  ConfigFileReader fileReader;
    private String projectName;
    private String addTopic;
    private String topicTitleInput;
    private String topicContentInput;
    private String topicHomeWaitElement;
    private String releaseTopicButton;
    private String cancelReleaseTopic;
    private String newTopicDetailWaitElement;
    private String atSomeone;

    public String getItemHomeHeader() {
        return topicHomeHeader;
    }

    private String topicHomeHeader;

    public String getPublicProjectName() {
        return publicProjectName;
    }

    private String publicProjectName;


    public String getRemoveComment() {
        return removeComment;
    }

    private String removeComment;

    public String getReplyComment() {
        return replyComment;
    }

    private String replyComment;

    public String getCommentContent() {
        return commentContent;
    }

    private String commentContent;

    public String getCommentTime() {
        return commentTime;
    }

    private String commentTime;

    public String getCommentButton() {
        return commentButton;
    }

    private String commentButton;

    public String getCommentInput() {
        return commentInput;
    }

    private String commentInput;

    public String getSortTopicByTime() {
        return sortTopicByTime;
    }

    private String sortTopicByTime;

    public String getSortTopicByComment() {
        return sortTopicByComment;
    }

    private String sortTopicByComment;

    public String getTopicTitleInDetailPage() {
        return topicTitleInDetailPage;
    }

    private String topicTitleInDetailPage;

    public String getMyTopicCount() {
        return myTopicCount;
    }

    private String myTopicCount;

    public String getAllTopicCount() {
        return allTopicCount;
    }

    private String allTopicCount;

    public String getEditButton() {
        return editButton;
    }

    private String editButton;

    public String getTopicTitleInProjectHome() {
        return topicTitleInProjectHome;
    }

    private String topicTitleInProjectHome;

    public String getEmptyTopicComments() {
        return emptyTopicComments;
    }

    private String emptyTopicComments;

    public String getTopicTime() {
        return topicTime;
    }

    private String topicTime;

    public String getAddTopicInProjectHome() {
        return addTopicInProjectHome;
    }

    private String addTopicInProjectHome;

    public String getDeleteButton() {
        return deleteButton;
    }

    private String deleteButton;

    public String getTopicTitleInItemHome() {
        return topicTitleInTopicHome;
    }

    private String topicTitleInTopicHome;

    public String getItemInItemHome() {
        return topicItemInTopicHome;
    }

    private String topicItemInTopicHome;


    public String getRefreshIcon() {
        return refreshIcon;
    }

    private String refreshIcon;

    public String getAutoLink() {
        return autoLink;
    }

    private String autoLink;

    public String getTopicContent() {
        return topicContent;
    }

    private String topicContent;

    public String getMarkDown() {
        return markDown;
    }

    private String markDown;

    public String getEmotionEmoji() {
        return emotionEmoji;
    }

    private String emotionEmoji;

    public String getTopicOwnerInTopicDetailPage() {
        return topicOwnerInTopicDetailPage;
    }

    private String topicOwnerInTopicDetailPage;

    public String getAtSomeone() {
        return atSomeone;
    }

    public String getNewTopicDetailWaitElement() {
        return newTopicDetailWaitElement;
    }

    public String getTopicHomeWaitElement() {
        return topicHomeWaitElement;
    }


    public String getReleaseTopicButton() {
        return releaseTopicButton;
    }

    public String getCancelReleaseTopic() {
        return cancelReleaseTopic;
    }


    public String getAddTopic() {
        return addTopic;
    }

    public String getTopicTitleInput() {
        return topicTitleInput;
    }

    public String getTopicContentInput() {
        return topicContentInput;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getTopicTimeInTopicDetailPage() {
        return topicTimeInTopicDetailPage;
    }

    private String topicTimeInTopicDetailPage;

    public String getCommentCountInTopicInfo() {
        return commentCountInTopicInfo;
    }

    private String commentCountInTopicInfo;

    public String getCommentCountInCommentHeader() {
        return commentCountInCommentHeader;
    }

    private String commentCountInCommentHeader;

    public String getCommentAvatar() {
        return commentAvatar;
    }

    private String commentAvatar;

    public String getCommentCreatorName() {
        return commentCreatorName;
    }

    private String commentCreatorName;

    public String getCommentMDbox() {
        return commentMDbox;
    }

    private String commentMDbox;





    public String getTopicNewTagPredefinedColor() {
        return topicTagPredefinedColor.replace(".tag.ng-scope", ".new-tag");
    }

    private String topicTagPredefinedColor;


    public String getTopicTagColorInAddTagList() {
        return topicTagColorInAddTagList;
    }

    private String topicTagColorInAddTagList;

    public String getTopicTagSelectorInputInAddTagList() {
        return topicTagSelectorInputInAddTagList;
    }

    private String topicTagSelectorInputInAddTagList;

    public String getTopicTagCurItemInAddTagList() {
        return topicTagCurItemInAddTagList;
    }

    private String topicTagCurItemInAddTagList;



    public String getTopicPreviewButton() {
        return topicPreviewButton;
    }

    private String topicPreviewButton;

    public String getTopicComment() {
        return topicComment;
    }

    private String topicComment;

    public String getTopicTagSearchBox() {
        return topicTagSearchBox;
    }

    private String topicTagSearchBox;




    public String getWatchButton() {
        return watchButton;
    }
    public String getWatchCountNextToButton() {
        return watchButton + "[title]";
    }

    private String watchButton;

    public String getWatchUserHeader() {
        return watchUserHeader;
    }

    private String watchUserHeader;

    public String getWatchUser() {
        return watchUser;
    }

    private String watchUser;

    public String getMyWatchTopicCount() {
        return myWatchTopicCount;
    }

    private String myWatchTopicCount;

    public String getTopicTagHeader() {
        return topicTagHeader;
    }

    private String topicTagHeader;

    public String getEmptyTopicListInfo() {
        return emptyTopicListInfo;
    }

    private String emptyTopicListInfo;





    public TopicFileReader()  {
        fileReader = new ConfigFileReader("/TopicConfig.properties");
        projectName= fileReader.getValue("projectName");
        topicTagSelectorInputInAddTagList = fileReader.getValue("topicTagSelectorInputInAddTagList");
        topicTagCurItemInAddTagList = fileReader.getValue("topicTagCurItemInAddTagList");
        topicTagColorInAddTagList = fileReader.getValue("topicTagColorInAddTagList");
        topicTagPredefinedColor = fileReader.getValue("topicTagPredefinedColor");
        topicTagHeader= fileReader.getValue("topicTagHeader");
        addTopic = fileReader.getValue("addTopic");
        topicTimeInTopicDetailPage = fileReader.getValue("topicTimeInTopicDetailPage");
        topicTitleInput = fileReader.getValue("topicTitleInput");
        releaseTopicButton = fileReader.getValue("releaseTopicButton");
        cancelReleaseTopic = fileReader.getValue("cancelReleaseTopic");
        topicContentInput = fileReader.getValue("topicContentInput");
        topicHomeWaitElement= fileReader.getValue("topicHomeWaitElement");
        newTopicDetailWaitElement = fileReader.getValue("newTopicDetailWaitElement");
        atSomeone = fileReader.getValue("atSomeone");
        emotionEmoji =fileReader.getValue("emotionEmoji");
        markDown =fileReader.getValue("markDown");
        topicContent =fileReader.getValue("topicContent");
        autoLink = fileReader.getValue("autoLink");
        refreshIcon = fileReader.getValue("refreshIcon");
        topicItemInTopicHome = fileReader.getValue("topicItemInTopicHome");
        topicTitleInTopicHome = fileReader.getValue("topicTitleInTopicHome");
        deleteButton = fileReader.getValue("deleteButton");
        addTopicInProjectHome = fileReader.getValue("addTopicInProjectHome");
        topicTime = fileReader.getValue("topicTime");
        emptyTopicComments = fileReader.getValue("emptyTopicComments");
        topicTitleInProjectHome = fileReader.getValue("topicTitleInProjectHome");
        editButton = fileReader.getValue("editButton");
        allTopicCount = fileReader.getValue("allTopicCount");
        myTopicCount = fileReader.getValue("myTopicCount");
        topicTitleInDetailPage = fileReader.getValue("topicTitleInDetailPage");
        sortTopicByComment = fileReader.getValue("sortTopicByComment");
        sortTopicByTime = fileReader.getValue("sortTopicByTime");
        commentInput = fileReader.getValue("commentInput");
        commentButton = fileReader.getValue("commentButton");
        commentTime = fileReader.getValue("commentTime");
        commentContent = fileReader.getValue("commentContent");
        replyComment = fileReader.getValue("replyComment");
        removeComment = fileReader.getValue("removeComment");
        publicProjectName = fileReader.getValue("publicProjectName");
        topicHomeHeader = fileReader.getValue("topicHomeHeader");
        topicOwnerInTopicDetailPage = fileReader.getValue("topicOwnerInTopicDetailPage");
        commentCountInTopicInfo = fileReader.getValue("commentCountInTopicInfo");
        commentCountInCommentHeader = fileReader.getValue("commentCountInCommentHeader");
        commentAvatar = fileReader.getValue("commentAvatar");
        commentCreatorName = fileReader.getValue("commentCreatorName");
        commentMDbox = fileReader.getValue("commentMDbox");
        emptyTopicListInfo = fileReader.getValue("emptyTopicListInfo");
        topicPreviewButton = fileReader.getValue("topicPreviewButton");
        topicComment = fileReader.getValue("topicComment");
        topicTagSearchBox = fileReader.getValue("topicTagSearchBox");
        watchButton = fileReader.getValue("watchButton");
        watchUserHeader = fileReader.getValue("watchUserHeader");
        watchUser = fileReader.getValue("watchUser");
        myWatchTopicCount = fileReader.getValue("myWatchTopicCount");
    }

}

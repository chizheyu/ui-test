package org.ChiTest.Friend;

import reference.ConfigFileReader;

/**
 * Created by dugancaii on 3/11/2015.
 */
public class FriendFileReader {
    private ConfigFileReader friendFileReader;

    public String getUserNameInNameCard() {
        return userNameInNameCard;
    }

    private String userNameInNameCard;

    public String getCancelFollowButton() {
        return cancelFollowButton;
    }

    private String cancelFollowButton;

    public String getFansInnerItem() {
        return fansInnerItem;
    }

    private String fansInnerItem;

    public String getFriendsInnerItem() {
        return friendsInnerItem;
    }

    private String friendsInnerItem;

    public String getResultItemInFriendSearchBox() {
        return resultItemInFriendSearchBox;
    }

    private String resultItemInFriendSearchBox;

    public String getChooseStatusInFriendSearchBox() {
        return chooseStatusInFriendSearchBox;
    }

    private String chooseStatusInFriendSearchBox;

    public String getUserNameCard() {
        return userNameCard;
    }

    private String userNameCard;

    public String getOpenSearchBoxButtonSelector() {
        return openSearchBoxButtonSelector;
    }

    private String openSearchBoxButtonSelector;

    public String getUserRelationshipInNameCard() {
        return userRelationshipInNameCard;
    }

    private String userRelationshipInNameCard;

    public String getFollowButton() {
        return followButton;
    }

    private String followButton;
    public FriendFileReader() {
        friendFileReader =new ConfigFileReader("/friendConfig.properties");
        userNameInNameCard = friendFileReader.getValue("userNameInNameCard");
        cancelFollowButton = friendFileReader.getValue("cancelFollowButton");
        fansInnerItem = friendFileReader.getValue("fansInnerItem");
        friendsInnerItem = friendFileReader.getValue("friendsInnerItem");
        resultItemInFriendSearchBox = friendFileReader.getValue("resultItemInFriendSearchBox");
        chooseStatusInFriendSearchBox = friendFileReader.getValue("chooseStatusInFriendSearchBox");
        openSearchBoxButtonSelector = friendFileReader.getValue("openSearchBoxButtonSelector");
        userNameCard = friendFileReader.getValue("userNameCard");
        userRelationshipInNameCard = friendFileReader.getValue("userRelationshipInNameCard");
        followButton = friendFileReader.getValue("followButton");
    }

 }
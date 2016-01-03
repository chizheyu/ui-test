package org.ChiTest.Friend;

import org.ChiTest.Page.Page;
import org.ChiTest.User.User;
import org.ChiTest.WebComponent.SearchBox.SearchBox;

import static org.junit.Assert.assertEquals;

/**
 * Created by dugancaii on 3/11/2015.
 */
public class SearchFriendBox extends SearchBox {
    private FriendFileReader friendFileReader;
    public SearchFriendBox(String boxSelector, String searchResultSelector, String openBoxButtonSelector){
        super(boxSelector,searchResultSelector, openBoxButtonSelector);
        this.friendFileReader = new FriendFileReader();
    }

    public void searchAndChooseFriends(Page operatorPage, User friend)  {
        searchItem(operatorPage, friend.getUserLoginName());
        assertEquals("关系中心搜查好友时，输入好友昵称后，搜出来的第一个不是该好友",
                friend.getUserName() + " - " +friend.getUserLoginName(),  operatorPage.getText(searchResultSelector) );

        String chooseStatus = getChooseStatusText( operatorPage);
        chooseResult(operatorPage);
        operatorPage.waitForContentChange( friendFileReader.getChooseStatusInFriendSearchBox(),chooseStatus,5);
        assertEquals("关系中心搜查好友时，输入好友昵称后，选中收出来的用户，未显示成功",  getChooseStatusText(operatorPage), "成功" );

    }
    public String getChooseStatusText(Page operatorPage){
        return operatorPage.getText(friendFileReader.getChooseStatusInFriendSearchBox());
    }




}

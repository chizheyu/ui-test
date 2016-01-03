package org.ChiTest.WebComponent;

import org.ChiTest.Friend.FriendFileReader;
import org.ChiTest.Page.Page;
import org.openqa.selenium.WebElement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by dugancaii on 3/12/2015.
 */
public class UserNameCard {
    private FriendFileReader friendFileReader;

    public UserNameCard(){

        this.friendFileReader = new FriendFileReader();
    }
    public void unFollow(Page fansPage, int cardNum){
        int followsCount = fansPage.getElementCount(friendFileReader.getCancelFollowButton());
        assertEquals("关注页面的‘取消关注’按钮消失或文案不对","取消关注",fansPage.getText(fansPage.getElement(friendFileReader.getUserNameCard(), cardNum), friendFileReader.getCancelFollowButton()));
        fansPage.getElement(friendFileReader.getUserNameCard(), cardNum, friendFileReader.getCancelFollowButton()).click();
        fansPage.waitForItemCountChange(friendFileReader.getCancelFollowButton(), followsCount, 5);
        assertEquals("取消关注失败", followsCount - 1,
                fansPage.getElementCount(friendFileReader.getCancelFollowButton()));
    }
    public void follow(Page fansPage, int cardNum){
        int followButtonCount = fansPage.getElementCount(friendFileReader.getFollowButton());
        assertEquals("关注页面的‘马上关注’按钮消失或文案不对","马上关注",fansPage.getText(fansPage.getElement(friendFileReader.getUserNameCard(), cardNum), friendFileReader.getFollowButton()));
        fansPage.getElement(friendFileReader.getUserNameCard(), cardNum, friendFileReader.getFollowButton()).click();
        fansPage.waitForItemCountChange(friendFileReader.getFollowButton(), followButtonCount, 5);
        assertEquals("关注失败", followButtonCount - 1,
                fansPage.getElementCount(friendFileReader.getFollowButton()));
    }




    public void checkFollowEachIcon(Page starPage, WebElement nameCard){
        assertEquals("关系中心的粉丝页面相互关注的图标显示不正常", "互相关注",
                starPage.getText(nameCard,friendFileReader.getUserRelationshipInNameCard()));
        assertNotNull("关系中心的粉丝页面的相互关注的箭头消失", starPage.getElement(nameCard, friendFileReader.getUserRelationshipInNameCard() + " .exchange"));
    }
    public void checkFollowedIcon(Page starPage, WebElement nameCard){
        assertEquals("关系中心的粉丝页面相互关注的图标显示不正常", "已关注",
                starPage.getText(nameCard,friendFileReader.getUserRelationshipInNameCard()));
        assertNotNull("关系中心的粉丝页面的相互关注的箭头消失", starPage.getElement(nameCard, friendFileReader.getUserRelationshipInNameCard() + " .checkmark"));
    }
    public void verifyJoinTime(Page fansPage, WebElement nameCard){
        String joinTime = fansPage.getAttribute(".created-time","title",nameCard);
        fansPage.navigate(fansPage.getAttribute(".friend .info a", "href"), ".user-created-at .created-time");
        assertEquals("个人首页的加入时间与关注页面显示的时间不同", joinTime, fansPage.getAttribute(".user-created-at .created-time","title"));
    }

}

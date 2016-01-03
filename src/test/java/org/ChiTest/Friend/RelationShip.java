package org.ChiTest.Friend;

import org.ChiTest.Page.Page;
import org.ChiTest.User.User;
import org.ChiTest.WebComponent.UserNameCard;

import static org.junit.Assert.assertEquals;

/**
 * Created by dugancaii on 3/11/2015.
 */
public class RelationShip {
    private FriendFileReader friendFileReader;
    private SearchFriendBox searchFriendBox;
    private UserNameCard userNameCard;
    private String friendsInnerItemInitString;

    public  User getFans() {
        return fans;
    }

    private  User fans;

    public  User getStar() {
        return star;
    }

    private  User star;
    public  RelationShip(User fans, User star){
        this();
        this.fans = fans;
        this.star = star;
    }
    public  RelationShip(){
        this.friendFileReader = new FriendFileReader();
        searchFriendBox = new SearchFriendBox("#add-friend-modal",
                friendFileReader.getResultItemInFriendSearchBox(),friendFileReader.getOpenSearchBoxButtonSelector());
        userNameCard = new UserNameCard();
        friendsInnerItemInitString = "关注 ()";

    }
    public int followSomeone() throws InterruptedException {
        return followSomeone(fans,star);
    }
    public int followSomeone(User fans, User star) throws InterruptedException {
        Page fansPage = fans.getPage();
        fansPage.navigate(fansPage.getFriendsUrl(),friendFileReader.getUserNameInNameCard());

        if(getUserCardNum(fansPage, star.getUserLoginName())  != -1) {
            return  -1;
        }

        int baseFollowsNumInPage = getFollowerCountFromInnerItem(fansPage);
        String userNameInNameCard = fansPage.getText(friendFileReader.getUserNameInNameCard());
        String baseFollowsNumTextInPage = fansPage.getText(friendFileReader.getFriendsInnerItem());

        searchFriendBox.openBox(fansPage);
        searchFriendBox.searchAndChooseFriends(fansPage, star);
        searchFriendBox.close(fansPage);

        fansPage.waitForContentChange(friendFileReader.getUserNameInNameCard(), userNameInNameCard, 5);
        assertEquals("关系中心的关注页面，添加关注后,新用户的姓名显示不正确或根本没出现",fansPage.getAttribute(friendFileReader.getUserNameInNameCard(), "title"),star.getUserName());

        fansPage.waitForContentChange(friendFileReader.getFriendsInnerItem(), baseFollowsNumTextInPage, 5);
        fansPage.assertNumInTextEquals("关系中心的关注页面，关注某用户后，页面左侧的关注数不正确，可能是关注的用户，没有在关注页面上出现",
                baseFollowsNumInPage + 1, friendFileReader.getFriendsInnerItem());
        return  0;
    }

    public int cancelFollow() throws InterruptedException {
        Page fansPage = fans.getPage();
        fansPage.navigate(fansPage.getFriendsUrl(), friendFileReader.getUserNameInNameCard());
        if(getUserCardNum(fansPage, star.getUserLoginName()) == -1) {
            return -1;
        }
        int followerCount = getFollowerCountFromInnerItem(fansPage);
        userNameCard.unFollow(fansPage, getUserCardNum(fansPage, star.getUserLoginName()));
        fansPage.assertNumInTextEquals("取消关注后，页面上显示的关注人数没有减一", followerCount -1, friendFileReader.getFriendsInnerItem());
        return 0;

    }
    public int getFollowerCountFromInnerItem(Page page){
        page.waitForContentChange(friendFileReader.getFriendsInnerItem(), friendsInnerItemInitString, 5);
        return page.getPageNum(friendFileReader.getFriendsInnerItem());
    }
    private int getUserCardNum(Page page, String uerLoginName) throws InterruptedException {
        return page.findItemBySign(uerLoginName,friendFileReader.getUserNameInNameCard(),page.getLinkContainFinder() );
    }


}

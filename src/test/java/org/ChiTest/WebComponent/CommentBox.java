package org.ChiTest.WebComponent;

import org.ChiTest.Page.PS;
import org.ChiTest.Page.Page;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by dugancaii on 3/18/2015.
 */
public class CommentBox extends Inputbox {
    public String replySelector;
    public String removeSelector;
    public String reviewerName;
    public CommentBox(String boxSelector, String sentItemSelector){
        super(boxSelector,sentItemSelector);
        this.replySelector = sentItemSelector + "  a[ng-click*='omment']";
        this.removeSelector =  "  a[ng-click*='emove(']";
        this.reviewerName = sentItemSelector + " .mr10";
    }

    public void replyComment(Page page,int num){
        int commentCount = page.getElementCount(sentItemSelector);
        page.moveToElement(sentItemSelector,num);
        assertReplyButtonText(page);
        page.clickElement(replySelector + " .edit.icon");
        page.waitForAttributeChange(boxInputSelector, "value",null,PS.midWait);
        page.assertAttributeEquals("任务评论时点击评论框，在评论框中没有显示出要回复的人",
                "@" + page.getText(reviewerName,num),boxInputSelector, "value" );
        sendReply(page);
        assertTrue("回复评论时，评论数没有增长("+ page.getCurrentUrl()+")", page.waitForItemCountChange(sentItemSelector,commentCount,15));
    }
    public void replyCommentForTopic(Page page,int num, String replySelector){
        int commentCount = page.getElementCount(sentItemSelector);
        page.moveToElement(sentItemSelector,num);
        String commentContent = page.getAttribute(boxInputSelector, "value");
        assertEquals("任务评论按钮显示文字不对", "回复", page.getText(replySelector));
        page.clickElement(replySelector + " .icon" );
        page.waitForAttributeChange(boxInputSelector, "value", "", 3);
        page.waitForAttributeChange(boxInputSelector, "value", commentContent,5);
        page.assertAttributeEquals("评论时点击评论框，在评论框中没有显示出要回复的人",
                "@" + page.getText(reviewerName,num),boxInputSelector, "value" );
        sendReply(page);
        assertTrue("回复评论时，评论数没有增长("+ page.getCurrentUrl()+")", page.waitForItemCountChange(sentItemSelector,commentCount,15));
    }

    public void removeCommentWithBackTest(Page cp, int commentNum){
        removeCommentWithBackTest(cp, commentNum, "评论");
    }
    public void removeComment(Page cp, int commentNum){
        removeComment(cp, commentNum, "评论");
    }
    public void removeComment(Page cp, int commentNum, String message){
        int commentCount = cp.getElementCount(sentItemSelector);
        cp.moveToElement(sentItemSelector, commentNum);
        assertEquals("任务评论删除按钮显示文字不对", "删除", cp.getText(sentItemSelector, commentNum,removeSelector));
        cp.checkAlert(sentItemSelector, commentNum,removeSelector+ " .remove.icon",
                "删除任务的弹框有误", "确认删除该"+message +"？");
        cp.waitForItemCountChange(sentItemSelector, commentCount, 5);
        cp.assertCountEquals("删除操作后,"+ message +"数量没有减一或删除操作失败", commentCount - 1, sentItemSelector);
    }
    public void removeCommentForTopic(Page cp, int commentNum, String message,String removeSelector){
        int commentCount = cp.getElementCount(sentItemSelector);
        cp.moveToElement(sentItemSelector, commentNum);
        assertEquals("任务评论删除按钮显示文字不对", "删除", cp.getText(sentItemSelector, commentNum,removeSelector));
        cp.checkAlert(sentItemSelector, commentNum,removeSelector+ " .remove.icon",
                "删除任务的弹框有误", "确认删除该"+message +"？");
        cp.waitForItemCountChange(sentItemSelector, commentCount, 5);
        cp.assertCountEquals("删除操作后,"+ message +"数量没有减一或删除操作失败", commentCount - 1, sentItemSelector);
    }

    public void removeCommentWithBackTest(Page cp, int commentNum, String message){
        int commentCount = cp.getElementCount(sentItemSelector);
        removeComment(cp, commentNum, message);
        cp.refresh(sentItemSelector);
        cp.assertCountEquals(message + "数量没有减一，后端没更新", commentCount - 1, sentItemSelector);
    }
    public void assertReplyButtonText(Page page){
        assertEquals("任务评论按钮显示文字不对", "评论", page.getText(replySelector));
    }
    public void sendReply(Page page){
        page.clickElement(boxSubmitSelector);
    }
}

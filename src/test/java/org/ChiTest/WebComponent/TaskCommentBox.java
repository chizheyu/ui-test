package org.ChiTest.WebComponent;

import org.ChiTest.Page.Page;

import static org.junit.Assert.assertEquals;

/**
 * Created by dugancaii on 3/24/2015.
 */
public class TaskCommentBox  extends CommentBox {
    public TaskCommentBox(String boxSelector,String sentItemSelector){
        super(boxSelector, sentItemSelector);
        this.boxInputSelector = boxSelector + " input";
        this.replySelector = sentItemSelector + " .comment-button.gray";
    }
    @Override
    public void assertReplyButtonText(Page page){
        assertEquals("任务评论按钮显示文字不对", "评论", page.getText(replySelector));
    }
    @Override
    public void sendReply(Page page){
        page.sendInfoWithKeyboard();
    }


}

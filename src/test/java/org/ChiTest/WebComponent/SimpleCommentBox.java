package org.ChiTest.WebComponent;

import org.ChiTest.Page.Page;

import static org.junit.Assert.assertEquals;

/**
 * Created by dugancaii on 3/5/2015.
 */
public class SimpleCommentBox extends CommentBox {

    public SimpleCommentBox(String boxSelector,String sentItemSelector){
        super(boxSelector, sentItemSelector);
        this.boxInputSelector = boxSelector + " input";
        this.boxSubmitSelector = boxSelector + " .right.icon";
    }
    @Override
    public void assertReplyButtonText(Page page){
        assertEquals("任务评论按钮显示文字不对", "回复", page.getText(replySelector));
    }
    @Override
    public void sendReply(Page page){
        page.sendInfoWithKeyboard();
    }

}

package org.ChiTest.Bubbling;

import org.ChiTest.Page.Page;

/**
 * Created by dugancaii on 3/3/2015.
 */
public class BubblingOperator {
    private Page operatorPage;
    public BubblingOperator(Page operatorPage){
        this.operatorPage = operatorPage;
    }

    public void sendBubblingContentToInputBox(String bubblingContent) throws InterruptedException {
        operatorPage.clearAndSendKeys(".feed-editor textarea", bubblingContent);
    }
    public void clickBubblingSendButton(){
        operatorPage.clickElement(".feed-editor .rotated");
    }
}

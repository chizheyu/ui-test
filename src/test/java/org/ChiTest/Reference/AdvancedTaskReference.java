package org.ChiTest.Reference;

import org.ChiTest.AdvancedTask.AdvancedTaskFileReader;
import org.ChiTest.Page.Page;

import java.util.Date;

/**
 * Created by dugancaii on 12/23/2014.
 */
public class AdvancedTaskReference {
    public AdvancedTaskReference(int referenceId, String referenceContent, String referenceLink) {
        this.referenceId = referenceId;
        this.referenceContent = referenceContent;
        this.referenceLink = referenceLink;
        this.advancedTaskFileReader = new AdvancedTaskFileReader();
        this.referenceFileReader = new ReferenceFileReader();
    }
    private AdvancedTaskFileReader advancedTaskFileReader;
    private ReferenceFileReader referenceFileReader;
    public int getReferenceId() {
        return referenceId;
    }

    public String getReferenceContent() {
        return referenceContent;
    }

    public String getReferenceLink() {
        return referenceLink;
    }
    public String getReferenceItemText(){
        return "#"+referenceId + " " + referenceContent;
    }

    public void sendReferenceInMarkDown(Page cp, String MDInputSelector){
        searchReferenceWithId(cp, MDInputSelector);
        cp.clickElement(referenceFileReader.getTaskReferenceHintItem());
        cp.clickElement(".clockwise.rotated.level.down.icon");
        cp.waitElementInvisible(advancedTaskFileReader.getTaskSendWait());
    }
    public void searchReferenceWithId(Page cp, String MDInputSelector){
        cp.clearAndSendKeys(MDInputSelector, "#"+this.referenceId);
        cp.assertElementPresent("任务详情页评论框的引用提示有误", referenceFileReader.getTaskReferenceHintItem());
        cp.assertElementPresent("引用提示中资源图标消失",
                referenceFileReader.getTaskReferenceHintItem() +" "+ referenceFileReader.getTaskReferenceIcon(),2);
        cp.assertTextEquals("用引用 id 搜索到的任务的内容（id 号加内容）出错", getReferenceItemText(),
                referenceFileReader.getTaskSelectedReferenceHintItem());
    }
    public void searchReferenceWithContent(Page cp, String MDInputSelector ){
        cp.clearAndSendKeys( MDInputSelector,
                "#"+getReferenceContent().substring(0,getReferenceContent().length()-1));
        cp.waitForElement(referenceFileReader.getTaskReferenceHintItem(),5);
        cp.assertTextEquals("用引用任务内容搜索到的任务的内容（id 号加内容）出错", getReferenceItemText(),
                referenceFileReader.getTaskSelectedReferenceHintItem());
    }
    public void verifyReference(Page cp, String message){
        cp.assertLinkAndTextEquals("["+message+"]" + "引用的链接和文本不对",     "#"+referenceId , referenceLink,
                 ".refer-resource-link.task");
        cp.moveToElement(".refer-resource-link.task");
        cp.assertElementPresent("["+message+"]" +"鼠标移动到资源引用上时，不会出现任务内容提示", ".refer-resource-link.task" + ".visible" ,2 );
        cp.assertTextEquals("["+message+"]" +"鼠标移动到资源引用上时，不会出现任务内容提示有误", referenceContent ,".ui.popup.small.top.center");
    }
    public void verifyErrorReference(Page page, String MDInputSelector, String message){
        page.clearAndSendKeys(MDInputSelector, "#" + ((new Date().getTime()) / 1000));
        page.clickElement(".clockwise.rotated.level.down.icon");
        page.waitElementInvisible(".loading.icon");
        page.assertElementNotPresent("[" + message + "]" + "错误的引用资源上出现链接",
                ".refer-resource-link.task");
    }


    private int referenceId;
    private String referenceContent;
    private String referenceLink;

}

package org.ChiTest.Topic;

import org.ChiTest.Page.Page;
import org.ChiTest.Project.Project;
import org.ChiTest.User.User;
import org.ChiTest.WebComponent.Tag.TagFileReader;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by dugancaii on 8/11/2014.
 */
public class Topic {
    public Topic(Project project, String topicTitle, String topicContent, User topicOwner) {
        this.project = project;
        this.topicTitle = topicTitle;
        this.topicContent = topicContent;
        this.topicOwner = topicOwner;
        this.topicFileReader = new TopicFileReader();
        this.tagFileReader = new TagFileReader();

    }

    private Project project;
    private TopicFileReader topicFileReader;
    private TagFileReader tagFileReader;

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public void setTopicContent(String topicContent) {
        this.topicContent = topicContent;
    }

    private String topicTitle;

    public String getTopicUrl() {
        return topicUrl;
    }
    public String getTopicEditUrl() {
        return topicUrl + "/edit";
    }

    public void setTopicUrl(String topicUrl) {
        this.topicUrl = topicUrl;
    }

    private String topicUrl;

    public Project getProject() {
        return project;
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public String getTopicContent() {
        return topicContent;
    }

    public User getTopicOwner() {
        return topicOwner;
    }

    private String topicContent;

    private User topicOwner;
    private static Logger log = Logger.getLogger("topic");
    public void createTopic() throws InterruptedException {
        Page page = topicOwner.getPage();
        //int topicNum = findTopicInTopicHomeByTitle(page);
        //if(topicNum != -1){
           // return;
       // }
        page.navigate(project.getProjectTopicCreateLink(), topicFileReader.getTopicTitleInput());
        page.clearAndSendKeys(topicFileReader.getTopicTitleInput(), topicTitle);
        page.clearAndSendKeys(topicFileReader.getTopicContentInput(), topicContent);
        page.clickElement(topicFileReader.getReleaseTopicButton());
        page.waitElementDisappear(".loading.ui.form");
        assertTrue("点击发布后，讨论发布不成功，或新讨论的无评论时咖啡图案消失", page.elementIsPresent(topicFileReader.getNewTopicDetailWaitElement(), 30));
        setTopicUrl(page.getDriver().getCurrentUrl());
    }

    public void deleteTopic() throws InterruptedException {
        Page page = topicOwner.getPage();
        int topicNum = findTopicInTopicHomeByTitle(page);
        if(topicNum == -1){
            return;
        }

        page.navigate(page.getLink(topicFileReader.getTopicTitleInItemHome(), topicNum), topicFileReader.getDeleteButton());

        page.checkAlert(topicFileReader.getDeleteButton(), "删除讨论后弹出弹框，点击取消或点击确认跳转出错", "确认删除该讨论？");
        if(! page.verifyHint("讨论删除成功！")){
            log.error("删除讨论的提示消失");
        }
        page.getWaitTool().waitForElement(By.cssSelector(topicFileReader.getTopicHomeWaitElement()), 5);
        assertEquals("讨论删除后还能在讨论首页出现(topic/all)", -1, page.findItemBySign( topicTitle,
                topicFileReader.getTopicTitleInItemHome(),page.getItemEqualsFinder() ));
    }
    public void deleteAllTopic() throws InterruptedException {
        Page page = project.getCreator().getPage();
        page.navigate(project.getProjectTopicHomeLink());
        page.waitElementInvisible(".loading.icon");
        while(page.elementIsPresent(topicFileReader.getTopicTitleInItemHome(),4)) {
            assertTrue("讨论首页点击讨论帖子，不能跳转到项目讨论详情页", page.clickElement(topicFileReader.getTopicTitleInItemHome(), topicFileReader.getDeleteButton()));
            page.checkAlert(topicFileReader.getDeleteButton(), "删除讨论后弹出弹框，点击取消或点击确认跳转出错", "确认删除该讨论？");
            if (!page.verifyHint("讨论删除成功！")) {
                log.error("删除讨论的提示消失");
            }
        }
        Thread.sleep(1000);
    }
    public void removeAllTagInTopicDetail(){
        Page page = topicOwner.getPage();
        int tagCount ;
        page.navigate(topicUrl, tagFileReader.getTagAddButtonInItem());
        while(page.elementIsPresent(tagFileReader.getTagInItemDetail(),2)) {
            tagCount = page.getElementCount(tagFileReader.getTagRemoveIconWhenEditItem());
            page.moveToElement(tagFileReader.getTagInItemDetail());
            page.clickElement(tagFileReader.getTagRemoveIconWhenEditItem());
            page.waitForItemCountChange( tagFileReader.getTagInItemDetail(), tagCount,5 );
        }

    }
    public int findTopicInTopicHomeByTitle(Page zombiePage) throws InterruptedException {
        zombiePage.refresh( this.project.getProjectTopicHomeLink(), topicFileReader.getTopicHomeWaitElement());
        int topicNum = zombiePage.findItemBySign(topicTitle, topicFileReader.getTopicTitleInItemHome(), zombiePage.getItemEqualsFinder() );
        if(topicNum == -1) {
            System.out.println("can not find the topic " + topicTitle);
            return -1;
        }
        return topicNum;

    }
    public void editTopic(Page zombiePage,String newTitle, String  newContent) throws InterruptedException {
        int topicNum =  findTopicInTopicHomeByTitle(zombiePage);
        if(topicNum == -1){
            return;
        }
        assertTrue("讨论首页点击讨论帖子，不能跳转到项目讨论详情页",zombiePage.clickElement(topicFileReader.getTopicTitleInItemHome(),topicNum, topicFileReader.getDeleteButton()));
        System.out.println("bianji " + zombiePage.getText(topicFileReader.getEditButton()));
        assertTrue("点击讨论编辑按钮，未能跳转到编辑页面", zombiePage.clickElement(topicFileReader.getEditButton(), topicFileReader.getTopicTitleInput()));
        zombiePage.refresh(topicFileReader.getTopicContentInput());
        assertEquals("点击讨论的编辑按钮后，发现还未编辑时，编辑的内容就与原来的不一致", topicContent, zombiePage.getAttribute(topicFileReader.getTopicContentInput(),"value"));

        zombiePage.sendKeys(topicFileReader.getTopicTitleInput(), newTitle);
        zombiePage.clearAndSendKeys(topicFileReader.getTopicContentInput(), newContent);
        zombiePage.clickElement(topicFileReader.getReleaseTopicButton());
        zombiePage.waitElementDisappear(".loading.form");
    }


}

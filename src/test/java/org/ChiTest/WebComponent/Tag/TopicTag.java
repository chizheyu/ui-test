package org.ChiTest.WebComponent.Tag;

import org.ChiTest.Page.Page;
import org.ChiTest.Project.Project;
import org.ChiTest.Topic.Topic;
import org.ChiTest.Topic.TopicFileReader;
import org.ChiTest.User.User;

import static org.junit.Assert.assertTrue;

/**
 * Created by dugancaii on 4/17/2015.
 */
public class TopicTag extends Tag {
    private TopicFileReader topicFileReader;

    public TopicTag(String tagName, String tagColorRBG,String hexadecimalValue, User owner, Project project){
        super( tagName, tagColorRBG,hexadecimalValue, owner, project);
        topicFileReader = new TopicFileReader();
    }
    public TopicTag(String tagName, String tagColorRBG, User owner, Project project){
        super( tagName, tagColorRBG, owner, project);
        topicFileReader = new TopicFileReader();
    }
    public void addTagInTopicDetail(Page page, Topic topic,String itemHomeHeaderSelector){
        page.navigate(topic.getProject().getProjectTopicHomeLink(), itemHomeHeaderSelector);
        int topicNum = page.findItemInOnePageByTextEquals(topicFileReader.getTopicTitleInItemHome(), topic.getTopicTitle());
        if(topicNum == -1){
            assertTrue("没有找到可以添加标签的讨论",false);
        }
        page.clickElement(topicFileReader.getTopicTitleInItemHome(), topicNum, topicFileReader.getTopicContent());
        addTagInItem(page);
    }

    protected void assertVerifiedItem(Page page,int topicCount, String itemTitle){
        page.assertTextEquals("根据新标签筛选的讨论标题", itemTitle, topicFileReader.getTopicTitleInItemHome());
        page.assertCountEquals("根据新标签筛选的讨论标题", topicCount, topicFileReader.getTopicTitleInItemHome());
    }

}

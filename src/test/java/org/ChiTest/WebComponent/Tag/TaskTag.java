package org.ChiTest.WebComponent.Tag;

import org.ChiTest.AdvancedTask.AdvancedTaskFileReader;
import org.ChiTest.Page.Page;
import org.ChiTest.Project.Project;
import org.ChiTest.Topic.Topic;
import org.ChiTest.User.User;

/**
 * Created by dugancaii on 4/24/2015.
 */
public class TaskTag extends Tag {
    private AdvancedTaskFileReader advancedTaskFileReader;

    public TaskTag(String tagName, String tagColorRBG,String hexadecimalValue, User owner, Project project){
        super( tagName, tagColorRBG,hexadecimalValue, owner, project);
        advancedTaskFileReader = new AdvancedTaskFileReader();
    }
    public TaskTag(String tagName, String tagColorRBG, User owner, Project project){
        super( tagName, tagColorRBG, owner, project);
        advancedTaskFileReader = new AdvancedTaskFileReader();
    }
    public void addTagInTaskDetail(Page page, Topic topic,String itemHomeHeaderSelector){

    }

    protected void assertVerifiedItem(Page page,int topicCount, String itemTitle){
        //page.assertTextEquals("根据新标签筛选的讨论标题", itemTitle, advancedTaskFileReader.getTopicTitleInItemHome());
        //page.assertCountEquals("根据新标签筛选的讨论标题", topicCount, advancedTaskFileReader.getTopicTitleInItemHome());
    }



}

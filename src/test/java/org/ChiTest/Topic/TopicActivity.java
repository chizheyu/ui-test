package org.ChiTest.Topic;

import org.ChiTest.Project.ProjectFileReader;
import org.ChiTest.Activity.Activity;
import org.ChiTest.Page.Page;
import org.ChiTest.PageLink;
import org.ChiTest.Project.Project;
import org.ChiTest.User.User;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dugancaii on 11/5/2014.
 */
public class TopicActivity extends Activity {
    private Project project;
    private Topic topic;

    public PageLink getProjectLink() {
        return projectLink;
    }

    protected PageLink projectLink;


    public TopicActivity(Project project, Topic topic) {
        super();
        this.project = project;
        this.topic = topic;
    }
    public void createBaseTopicActivity(User activitySponsor, String activityContent,String topicIcon ){
        ProjectFileReader projectFileReader = new ProjectFileReader();
        User projectOwner = project.getCreator();
        Page projectPage = projectOwner.getPage();
        time = new Date();
        projectUrl = projectPage.getBaseUrl() +"u/" + projectOwner.getUserLoginName()+"/p/"+ project.getProjectName();
        userCenterUrl = projectPage.getBaseUrl() + "user";
        projectMemberUrl = projectUrl + "/members/" + activitySponsor.getUserLoginName();
        pageLinks = new ArrayList<PageLink>();
        sponsorLink = new PageLink(activityFileReader.getSponsorLinkInActivity(),activitySponsor.getUserName(),activityFileReader.getUserSpace(),
                projectPage.getBaseUrl() +"u/"+activitySponsor.getUserLoginName());
        pageLinks.add(sponsorLink);

        projectLink = new PageLink(activityFileReader.getActionLinkInActivity(),project.getProjectName(),projectFileReader.getPrivateProjectTitle(),
                projectUrl );

        sponsor  = activitySponsor;
        content = activityContent;
        iconStyle = topicIcon;

    }

}

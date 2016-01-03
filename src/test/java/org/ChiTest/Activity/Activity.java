package org.ChiTest.Activity;

import org.ChiTest.Activity.UserCenterFormateChanger.UserCenterActivityDefaultChanger;
import org.ChiTest.InterFace.ItemEntry;
import org.ChiTest.Activity.UserCenterFormateChanger.UserCenterActivityChanger;
import org.ChiTest.PageLink;
import org.ChiTest.Project.Project;
import org.ChiTest.User.User;

import java.util.Date;
import java.util.List;

/**
 * Created by dugancaii on 8/12/2014.
 */
public class Activity implements ItemEntry {
   // private final static Log log = LogFactory.getLog(Activity.class);
    protected Date time;
    protected User sponsor ;

    public String getIcon() {
        return iconStyle;
    }
    public String getClassifySelector(){
        return "";
    }

    protected String iconStyle;
    protected String content;

    public int decreaseCount() {
        return --activityCount;
    }

    protected int activityCount;

    public Project getProject() {
        return project;
    }

    protected Project project;
    protected ActivityFileReader activityFileReader;

    public PageLink getSponsorLink() {
        return sponsorLink;
    }

    protected PageLink sponsorLink;

    public List<PageLink> getPageLinks() {
        return pageLinks;
    }

    protected List<PageLink> pageLinks;

    public List<String> getLinkUrls() {
        return linkUrls;
    }

    public  List<String> linkUrls;


    public UserCenterActivityChanger activityChanger;
    public Activity(  User sponsor,  String content,String iconClass, List<String> linkUrls, UserCenterActivityChanger activityChanger,Project project){
        this(sponsor, content, iconClass, linkUrls, project);
        this.activityChanger = activityChanger;
    }
    public Activity(   User sponsor,  String content,String iconClass, List<String> linkUrls, int activityCount, UserCenterActivityChanger activityChanger,Project project){
        this(sponsor,   content, iconClass,  linkUrls, activityCount,project);
        this.activityChanger = activityChanger;
    }

    public Activity(  User sponsor,  String content,String iconClass, List<String> linkUrls,Project project){
        this(project);
        this.sponsor  = sponsor;
        this.content = content;
        this.iconStyle = iconClass;
        this.linkUrls = linkUrls;
        //有些动态会批量出现多次
        this.activityCount = 1;
    }
    public Activity(  User sponsor,  String content,String iconClass, List<String> linkUrls, int activityCount,Project project){
        this(project);
        this.sponsor  = sponsor;
        this.content = content;
        this.iconStyle = iconClass;
        this.linkUrls = linkUrls;
        //有些动态会批量出现多次
        this.activityCount = activityCount;
    }
    public Activity(Project project){
        this();
        this.project = project;
    }

    public Activity(){
        this.activityFileReader = new ActivityFileReader();
        activityChanger = new UserCenterActivityDefaultChanger();
    }

    public String getProjectUrl() {
        return projectUrl;
    }

    public String getUserCenterUrl() {
        return userCenterUrl;
    }

    public String getProjectMemberUrl() {
        return projectMemberUrl;
    }

    protected String projectUrl ;
    protected String userCenterUrl ;
    protected String projectMemberUrl;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public User getSponsor() {
        return sponsor ;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void changeToUserCenterFormat(){
        activityChanger.changeToUserCenterFormat(this);
    }



}

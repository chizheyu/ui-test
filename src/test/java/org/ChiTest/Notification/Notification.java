package org.ChiTest.Notification;

import org.ChiTest.Bubbling.Bubbling;
import org.ChiTest.InterFace.ItemEntry;
import org.ChiTest.PageLink;
import org.ChiTest.Project.Project;
import org.ChiTest.Topic.Topic;
import org.ChiTest.User.User;

import java.util.Date;
import java.util.List;

/**
 * Created by dugancaii on 8/12/2014.
 */
public class Notification implements ItemEntry {
   // private final static Log log = LogFactory.getLog(Activity.class);
    private Date time;
    private User sponsor;
    private User receiver;
    private String content;

    public int decreaseCount() {
        return notificationCount--;
    }

    private int notificationCount;

    public List<PageLink> getPageLinks() {
        return pageLinks;
    }

    private List<PageLink> pageLinks ;

    public List<String> getLinkUrls() {
        return linkUrls;
    }

    private List<String> linkUrls;
    private String icon;
    private Project project;
    private Bubbling bubbling;

    public String getClassifySelector() {
        return classifySelector;
    }

    private String classifySelector;
    public Topic getTopic() {
        return topic;
    }

    private Topic topic;
    public Notification(){

    }

    public Notification(Date time, User sponsor,  String content, Project project ){
        this.time = time;
        this.sponsor  = sponsor;
        this.content = content;
        this.project = project;
    }
    public Notification(String icon, User sponsor,  String content, User receiver){
        this.icon = icon;
        this.sponsor = sponsor;
        this.content = content;
        this.receiver = receiver;

    }
    public Notification(String icon, User sponsor,  String content, User receiver,  List<PageLink> pageLinks){
        this.icon = icon;
        this.sponsor = sponsor;
        this.content = content;
        this.receiver = receiver;
        this.pageLinks = pageLinks;
    }

    public Notification(String icon, User sponsor,  String content,  List<String> linkUrls){
        this.icon = icon;
        this.sponsor = sponsor;
        this.content = content;
        this.linkUrls = linkUrls;

    }
    public Notification(String icon, User sponsor,  String content,  List<String> linkUrls,String classifySelector){
        this(icon, sponsor, content,  linkUrls);
        this.classifySelector = classifySelector;
        this.notificationCount = 1;
    }
    public Notification( Bubbling bubbling,String icon ){
        this.time = bubbling.getTime();
        this.sponsor = bubbling.getSender();
        this.content = bubbling.getContent();
        this.icon = icon;
        this.bubbling = bubbling;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    public User getReceiver() {
        return receiver;
    }

    public void setReceive(User receiver) {
        this.receiver = receiver;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Bubbling getBubbling() {
        return bubbling;
    }

    public void setBubbling(Bubbling bubbling) {
        this.bubbling = bubbling;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getSponsor() {
        return sponsor;
    }

    public void setSponsor(User sponsor) {
        this.sponsor = sponsor;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }


}

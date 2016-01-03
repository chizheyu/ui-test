package org.ChiTest.Notification;

import org.ChiTest.AdvancedTask.AdvancedTask;
import org.ChiTest.AdvancedTask.AdvancedTaskFileReader;
import org.ChiTest.InterFace.ItemEntry;
import org.ChiTest.Page.Page;
import org.ChiTest.Project.Project;
import org.ChiTest.Topic.Topic;
import org.ChiTest.Topic.TopicFileReader;
import org.ChiTest.User.User;
import org.ChiTest.WebComponent.MarkDownInputBox;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by dugancaii on 12/17/2014.
 */
public class AdvancedNotificationTest extends Notification {
    private NotificationFileReader notificationFileReader;
    private AdvancedTaskFileReader advancedTaskFileReader;
    private TopicFileReader topicFileReader;
    private  Map<String, ItemEntry> notificationMap;
    private User ciwangUser;
    private  User zombieUser;
    private Project project;
    private  int itemHasVerified;
    private List<String> twoLinkForDeleteTask;
    private List<String> twoLinkForCommentTask;
    private List<String> twoLinkForTopicComment;
    private  String replyCommentContent;
    private int notificationSize;

    public AdvancedNotificationTest(User ciwangUser, User zombieUser, Project project){
        this.ciwangUser = ciwangUser;
        this.zombieUser = zombieUser;
        this.notificationFileReader = new NotificationFileReader();
        this.advancedTaskFileReader = new AdvancedTaskFileReader();
        this.topicFileReader = new TopicFileReader();
        this.project = project;
        twoLinkForDeleteTask = new ArrayList<String>();
        twoLinkForDeleteTask.add(ciwangUser.getHomePageLink());
        twoLinkForDeleteTask.add(project.getProjectLink());

        twoLinkForCommentTask = new ArrayList<String>();
        twoLinkForTopicComment  = new ArrayList<String>();

        notificationMap = new HashMap<String, ItemEntry>();
        replyCommentContent = "@"+zombieUser.getUserName();


    }
    public void constructCiwangNotificationForTopicComment(MarkDownInputBox topicCommentBox, Topic topic, Set mailCheckList){
        mailCheckList.clear();
        notificationMap.clear();
        twoLinkForTopicComment.clear();
        twoLinkForTopicComment.add(ciwangUser.getHomePageLink());
        twoLinkForTopicComment.add(topic.getTopicUrl()+"?page=1");

        addItemIntoMapAndList(createCiwangNotification(notificationFileReader.getAtSomeoneIconClass(), ciwangUser.getUserName()+ " 在讨论 "+ topic.getTopicTitle() + " 的评论中提到了你。",
                notificationFileReader.getAtSomeoneItemIcon(), twoLinkForTopicComment), mailCheckList);
        addItemIntoMapAndList(createCiwangNotification(notificationFileReader.getChatIconClass(), ciwangUser.getUserName()+ " 评论了讨论 "+ topic.getTopicTitle() + " ： "+ topicCommentBox.getSendWithKeyboardsContent(),
                notificationFileReader.getCommentItemIcon(), twoLinkForTopicComment), mailCheckList);
    }
    public void constructNotificationForTopicWatch(MarkDownInputBox topicCommentBox, Topic topic, Set mailCheckList,User sponsor){
        mailCheckList.clear();
        notificationMap.clear();
        twoLinkForTopicComment.clear();
        twoLinkForTopicComment.add(sponsor.getHomePageLink());
        twoLinkForTopicComment.add(topic.getTopicUrl()+"?page=1");

        addItemIntoMapAndList(createCiwangNotification(notificationFileReader.getChatIconClass(), sponsor.getUserName()+ " 评论了讨论 "+ topic.getTopicTitle() + " ： "+ topicCommentBox.getSendWithKeyboardsContent(),
                notificationFileReader.getCommentItemIcon(), twoLinkForTopicComment), mailCheckList);
    }
    public void constructAtSomeoneInTopicContent( Topic topic, User topicOwner,Set mailCheckList){
        mailCheckList.clear();
        notificationMap.clear();
        twoLinkForTopicComment.clear();
        twoLinkForTopicComment.add(topicOwner.getHomePageLink());
        twoLinkForTopicComment.add(topic.getTopicUrl()+"?page=1");

        addItemIntoMapAndList(createCiwangNotification(notificationFileReader.getAtSomeoneIconClass(), topicOwner.getUserName()+ " 在讨论 "+ topic.getTopicTitle() + " 中提到了你。",
                notificationFileReader.getAtSomeoneItemIcon(), twoLinkForTopicComment), mailCheckList);

    }

    public Notification createCiwangNotification(String taskIcon, String content, String classifySelector, List<String> twoLinkForTopicComment ){
        return new Notification(taskIcon, ciwangUser,content , twoLinkForTopicComment, classifySelector);
    }


    public void constructNotificationMapForUpdateTask(AdvancedTask advancedTask,
                                                         String updatedTaskContent,   Set mailCheckList,String classifySelector){
        notificationMap.clear();
        updateTaskNotification(advancedTask,updatedTaskContent,mailCheckList, classifySelector);
        addItemIntoMapAndList(getRemoveTaskNotification(updatedTaskContent, classifySelector), mailCheckList);

    }
    public void constructNotificationMapAfterComment(
            String commentContent, String atContent, AdvancedTask advancedTask, Set mailCheckList){

        notificationMap.clear();
        twoLinkForCommentTask.clear();
        twoLinkForCommentTask.add(ciwangUser.getHomePageLink());
        twoLinkForCommentTask.add(advancedTask.getTaskUrl());
        addItemIntoMapAndList( getCiwangNotificationForComment(notificationFileReader.getTaskIconClass(),
                ciwangUser.getUserName()+" 评论了任务 "+advancedTask.getContent()+"："+ commentContent, notificationFileReader.getSystemItemIcon()),mailCheckList);
        addItemIntoMapAndList( getCiwangNotificationForComment(notificationFileReader.getAtSomeoneIconClass(),
                ciwangUser.getUserName()+" 在任务 "+advancedTask.getContent()+" 中提到了你 :"+ atContent, notificationFileReader.getAtSomeoneItemIcon()),mailCheckList);
        addItemIntoMapAndList( getCiwangNotificationForComment(notificationFileReader.getAtSomeoneIconClass(),
                ciwangUser.getUserName()+" 在任务 "+advancedTask.getContent()+" 中提到了你 :"+ this.replyCommentContent, notificationFileReader.getAtSomeoneItemIcon()),mailCheckList);
    }
    public Notification getCiwangNotificationForComment(String taskIcon, String content, String classifySelector ){
        return new Notification(taskIcon, ciwangUser,content , twoLinkForCommentTask, classifySelector);
    }

    public void taskNotificationConstructor(AdvancedTask advancedTask,
                                            String updatedTaskContent,   Set mailCheckList,String classifyIcon){

        notificationMap.clear();
        List<String> threeLink = new ArrayList<String>();
        threeLink.add(ciwangUser.getHomePageLink());
        threeLink.add(project.getProjectLink());

        threeLink.add(advancedTask.getTaskUrl());
        Notification createTaskNotification =
                new Notification(notificationFileReader.getTaskIconClass(), ciwangUser,
                        ciwangUser.getUserName()+" 在项目 "+ project.getProjectName()+" 中给你创建了一个新任务："+advancedTask.getContent().substring(0,51) + "...", threeLink,classifyIcon);
        addItemIntoMapAndList(createTaskNotification, mailCheckList);

        updateTaskNotification(advancedTask,updatedTaskContent,mailCheckList, classifyIcon);

        addItemIntoMapAndList(getRemoveTaskNotification(updatedTaskContent, classifyIcon), mailCheckList);
    }
    public void constructprojectOwnerChange(User olderOwner, Set mailCheckList){
        notificationMap.clear();
        List<String> towLinks = new ArrayList<String>();
        towLinks.add(olderOwner.getHomePageLink());
        towLinks.add(project.getProjectLink());

        Notification createTaskNotification =
                new Notification(notificationFileReader.getProjectMemberIconClass(), olderOwner,
                        olderOwner.getUserName()+" 将项目 "+project.getProjectName()+" 转让给了你", towLinks,notificationFileReader.getSystemItemIcon());
        addItemIntoMapAndList(createTaskNotification, mailCheckList);

    }
    public void constructprojectDelete(User projectOwner, Set mailCheckList){
        notificationMap.clear();
        List<String> towLinks = new ArrayList<String>();
        towLinks.add(projectOwner.getHomePageLink());
        towLinks.add(project.getProjectLink());

        Notification createTaskNotification =
                new Notification(notificationFileReader.getInboxIconClass(), projectOwner,
                        projectOwner.getUserName()+" 删除了项目 "+project.getProjectName()+"。", towLinks,notificationFileReader.getSystemItemIcon());
        addItemIntoMapAndList(createTaskNotification, mailCheckList);

    }
    public void updateTaskNotification(AdvancedTask advancedTask,
                                       String updatedTaskContent,   Set mailCheckList,String classifyIcon){
        Project project = advancedTask.getProject();
        List<String> threeLink = new ArrayList<String>();
        threeLink.add(ciwangUser.getHomePageLink());
        threeLink.add(project.getProjectLink());
        threeLink.add(advancedTask.getTaskUrl());

        Notification updateTaskNotification =
                new Notification(notificationFileReader.getTaskIconClass(), ciwangUser,
                        ciwangUser.getUserName()+" 在项目 "+ project.getProjectName()+" 更新了任务内容："+ updatedTaskContent, threeLink,classifyIcon);
        addItemIntoMapAndList(updateTaskNotification, mailCheckList);

        Notification priorityTaskNotification =
                new Notification(notificationFileReader.getTaskIconClass(), ciwangUser,
                        ciwangUser.getUserName()+" 在项目 "+ project.getProjectName()+" 更新了任务的紧急程度："+ updatedTaskContent, threeLink,classifyIcon);
        addItemIntoMapAndList(priorityTaskNotification, mailCheckList);

        Notification deadlineTaskNotification =
                new Notification(notificationFileReader.getTaskIconClass(), ciwangUser,
                        ciwangUser.getUserName()+" 在项目 "+ project.getProjectName()+" 更新了任务截止日期："+ updatedTaskContent, threeLink,classifyIcon);
        addItemIntoMapAndList(deadlineTaskNotification, mailCheckList);

        Notification finishTaskNotification =
                new Notification(notificationFileReader.getTaskIconClass(), ciwangUser,
                        ciwangUser.getUserName()+" 在项目 "+ project.getProjectName()+" 完成了任务："+ updatedTaskContent, threeLink,classifyIcon);
        addItemIntoMapAndList(finishTaskNotification, mailCheckList);

        Notification reopenTaskNotification =
                new Notification(notificationFileReader.getTaskIconClass(), ciwangUser,
                        ciwangUser.getUserName()+" 在项目 "+ project.getProjectName()+" 重新开启了任务："+ updatedTaskContent, threeLink,classifyIcon);
        addItemIntoMapAndList(reopenTaskNotification, mailCheckList);

        List<String> fourLink = new ArrayList<String>();
        fourLink.add(ciwangUser.getHomePageLink());
        fourLink.add(project.getProjectLink());
        fourLink.add(zombieUser.getHomePageLink());
        fourLink.add(advancedTask.getTaskUrl());

        Notification resignTaskNotification =
                new Notification(notificationFileReader.getTaskIconClass(), ciwangUser,
                        ciwangUser.getUserName()+" 在项目 "+ project.getProjectName()+" 中给 "+ zombieUser.getUserName()+" 指派了一个任务："+ updatedTaskContent, fourLink,classifyIcon);
        addItemIntoMapAndList(resignTaskNotification, mailCheckList);
    }




    public void addItemIntoMapAndList(Notification notification, Set mailCheckList){
        notificationMap.put(notification.getContent(), notification);
        mailCheckList.add(notification.getContent());
    }
    public Notification getRemoveTaskNotification(String taskContent, String classifySelector){
        return  new Notification(notificationFileReader.getTaskIconClass(), ciwangUser,
                ciwangUser.getUserName()+" 删除了项目 "+ project.getProjectName()+" 中的任务 "+ taskContent, twoLinkForDeleteTask,classifySelector);
    }
    public void checkTaskNotification( Page page)  {
        page.refresh(page.getNotificationLink(), notificationFileReader.getNotificationContent());
        int notificationPageCount;
        String activeItemText;
        Set classifySet = new HashSet();
        Iterator it = notificationMap.entrySet().iterator();
        notificationSize = notificationMap.size();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            classifySet.add(notificationMap.get(entry.getKey()).getClassifySelector());
        }
        itemHasVerified = 0;
        for (int i = 0; i < classifySet.size() + 1; i++) {
            activeItemText = page.getText(notificationFileReader.getActiveItemText());
            notificationPageCount = page.getPageNum(notificationFileReader.getUnreadCount())/10;
            for (int j = 0; j < notificationPageCount + 1  ; j++) {
                if(!page.elementIsPresent(notificationFileReader.getUnReadNotificationItem(),2)){
                    break;
                }
                checkNotificationItemInMap(page, notificationMap, notificationFileReader.getNotificationContent(), notificationFileReader.getNotificationIcon(), "任务通知(系统通知)");
                if(itemHasVerified >= notificationSize){
                    itemHasVerified = 0;
                    break;
                }
                page.clickElement(".cg .page.next");
                page.waitElementInvisible(advancedTaskFileReader.getTaskLoadingIcon());
            }
            if(i == classifySet.size() ){
                break;
            }
            page.clickElement(classifySet.toArray()[i].toString());
            page.waitForContentChange(notificationFileReader.getActiveItemText(), activeItemText, 10);
        }
        page.checkItemEntryMap(notificationMap);

        String  notificationTitle = page.getText(notificationFileReader.getPageTitle());
        page.checkAlert(notificationFileReader.getReadAll(), "标记所有通知为已读的按钮或alert弹框有问题，建议确认和取消都试下",
                "这将会把您所有接收到的通知标记为已读（包括没有在本页显示的），确定继续？");
        page.waitForContentChange(notificationFileReader.getPageTitle(),notificationTitle,5);

        page.navigate(page.getNotificationLink(), notificationFileReader.getNotificationContent());

        assertTrue(
                page.getText(notificationFileReader.getNotificationTime()).endsWith("分钟前") ||
                        page.getText(notificationFileReader.getNotificationTime()).endsWith("几秒前")||
                        page.getText(notificationFileReader.getNotificationTime()).endsWith("几秒内"));
        if(page.getText(notificationFileReader.getNotificationTime()).endsWith("分钟前")) {
            assertTrue("收到的最近一条任务通知竟然不在1到15分钟内 ", 0 < page.getPageNum(notificationFileReader.getNotificationTime())
                    && page.getPageNum(notificationFileReader.getNotificationTime()) < 15);
        }



    }

    private void checkNotificationItemInMap(Page page, Map<String,ItemEntry> itemEntryMap, String itemContentSelector,String itemIconSelector, String checkMessage ){
        ItemEntry itemEntry;
        List<WebElement> checkElements = page.getElements(itemContentSelector);

        for(int i = 0; i < checkElements.size(); i++){
            if(itemEntryMap.containsKey(page.getText(itemContentSelector, i))){
                itemEntry = itemEntryMap.get(page.getText(itemContentSelector, i));
                for(int j = 0; j < itemEntry.getLinkUrls().size(); j++  ) {
                    assertEquals(checkMessage +":"+ itemEntry.getContent() + "的第"+ (j + 1) + "个链接出错" , itemEntry.getLinkUrls().get(j),
                            page.getElements(itemContentSelector).get(i).findElements(By.cssSelector("a")).get(j).getAttribute("href")  );
                }
                assertTrue(checkMessage + ":" + itemEntry.getContent() + "的图标出错",
                        page.getAttribute(itemIconSelector, "class", i).contains(itemEntry.getIcon()));
                if(page.elementIsPresent(notificationFileReader.getActiveItemText(),0,itemEntry.getClassifySelector())){
                    itemEntryMap.remove(page.getText(itemContentSelector, i));
                }
                itemHasVerified++;
                if(itemHasVerified >= notificationSize){
                    break;
                }

            }
        }
    }

}

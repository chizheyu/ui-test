package org.ChiTest.Activity;

import org.ChiTest.Activity.UserCenterFormateChanger.UserCenterActivityChangerForExitProject;
import org.ChiTest.Activity.UserCenterFormateChanger.UserCenterActivityNoChanger;
import org.ChiTest.AdvancedTask.AdvancedTask;
import org.ChiTest.AdvancedTask.AdvancedTaskFileReader;
import org.ChiTest.Document.File;
import org.ChiTest.Document.Folder;
import org.ChiTest.InterFace.ItemEntry;
import org.ChiTest.Page.Page;
import org.ChiTest.Project.Project;
import org.ChiTest.Topic.Topic;
import org.ChiTest.User.User;
import org.openqa.selenium.By;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by dugancaii on 12/17/2014.
 */
public class AdvancedActivity {
    private  ActivityFileReader activityFileReader;
    private AdvancedTaskFileReader advancedTaskFileReader;
    private User ciwangUser;
    private  User zombieUser;
    private Project project;
    private  Map<String, ItemEntry> activityMap;
    private List<String> commentActivityLink;
    private String replyCommentContent;

    public AdvancedActivity(User ciwangUser, User zombieUser, Project project){
        this.ciwangUser = ciwangUser;
        this.zombieUser = zombieUser;
        this.activityFileReader = new ActivityFileReader();
        this.advancedTaskFileReader = new AdvancedTaskFileReader();
        this.project = project;
        this.activityMap = new HashMap<String, ItemEntry>();
        this.commentActivityLink = new ArrayList<String>();
        replyCommentContent = "@"+zombieUser.getUserName();
    }
    public void constructEditDescriptionActivity(String taskUrl, String taskContent,String descriptionContent){
        activityMap.clear();
        List<String> editTaskDescriptionLinks = new ArrayList<String>();
        editTaskDescriptionLinks.add(ciwangUser.getHomePageLink());
        editTaskDescriptionLinks.add(taskUrl);
        editTaskDescriptionLinks.add(taskUrl);
        Activity editTaskDescriptionActivity = new Activity(ciwangUser,
        ciwangUser.getUserName()+" 更新了任务 "+ taskContent +" 的描述\n" + descriptionContent, advancedTaskFileReader.getTaskDescriptionIconClass(), editTaskDescriptionLinks,project);
        activityMap.put(editTaskDescriptionActivity.getContent(),editTaskDescriptionActivity);
    }

    public void constructDocumentDirectoryActivity(User sponsor,Folder folder, Folder subFolder){
        activityMap.clear();
        List<String> folderFolderActivityUrl = new ArrayList<String>();
        folderFolderActivityUrl.add(sponsor.getHomePageLink());
        folderFolderActivityUrl.add(folder.getFolderUrl());
        List<String> folderFolderActivityUrl_1 = new ArrayList<String>();
        folderFolderActivityUrl_1.addAll(folderFolderActivityUrl);

        List<String> subFolderActivityUrl = new ArrayList<String>();
        subFolderActivityUrl.add(sponsor.getHomePageLink());
        subFolderActivityUrl.add(subFolder.getFolderUrl());
        List<String> subFolderActivityUrl_1 = new ArrayList<String>();
        subFolderActivityUrl_1.addAll(subFolderActivityUrl);

        List<String> folderDocumentActivityUrl = new ArrayList<String>();
        folderDocumentActivityUrl.add(sponsor.getHomePageLink());
        folderDocumentActivityUrl.add(project.getProjectDocumentLink());
        List<String> subFolderDocumentActivityUrl = new ArrayList<String>();
        subFolderDocumentActivityUrl.addAll(folderDocumentActivityUrl);

        putItemIntoMap(createFolderActivity(sponsor, sponsor.getUserName() + " 删除了 文件夹\n" + folder.getFolderName(), folderDocumentActivityUrl));
        putItemIntoMap(createFolderActivity(sponsor, sponsor.getUserName() + " 删除了 文件夹\n" + subFolder.getFolderName(), subFolderDocumentActivityUrl));

        //putItemIntoMap(createFolderActivity(sponsor, sponsor.getUserName() + " 新建了 文件夹\n" + folder.getFolderName(), folderFolderActivityUrl));
        //putItemIntoMap(createFolderActivity(sponsor, sponsor.getUserName() + " 新建了 文件夹\n" + subFolder.getFolderName(), subFolderActivityUrl));

        putItemIntoMap(createFolderActivity(sponsor, sponsor.getUserName() + " 重命名了 文件夹\n" + folder.getFolderName(), folderFolderActivityUrl_1));
        putItemIntoMap(createFolderActivity(sponsor, sponsor.getUserName() + " 重命名了 文件夹\n" + subFolder.getFolderName(), subFolderActivityUrl_1));
    }
    public void constructRemoveFileInFolderActivity(File folderFile, File subFolderFile){
        activityMap.clear();
        List<String> removeFolderFileActivity = new ArrayList<String>();
        removeFolderFileActivity.add(folderFile.getFileOwner().getHomePageLink());

        List<String> removeSubFolderFileActivity = new ArrayList<String>();
        removeSubFolderFileActivity.add(subFolderFile.getFileOwner().getHomePageLink());

        putItemIntoMap(createFileActivity(folderFile.getFileOwner(), folderFile.getFileOwner().getUserName() + " 删除了 文件\n" + folderFile.getFileName(), removeFolderFileActivity));
        putItemIntoMap(createFileActivity(subFolderFile.getFileOwner(), subFolderFile.getFileOwner().getUserName() + " 删除了 文件\n" + subFolderFile.getFileName(), removeSubFolderFileActivity));
    }
    public void constructUploadFileInFolderActivity(File folderFile, File subFolderFile){
        activityMap.clear();
        List<String> uploadFolderFileActivityUrl = new ArrayList<String>();
        uploadFolderFileActivityUrl.add(folderFile.getFileOwner().getHomePageLink());
        uploadFolderFileActivityUrl.add(folderFile.getFileUrl());

        List<String> uploadSubFolderFileActivityUrl = new ArrayList<String>();
        uploadSubFolderFileActivityUrl.add(subFolderFile.getFileOwner().getHomePageLink());
        uploadSubFolderFileActivityUrl.add(subFolderFile.getFileUrl());


        putItemIntoMap(createFileActivity(folderFile.getFileOwner(), folderFile.getFileOwner().getUserName() + " 上传了 文件\n" + folderFile.getFileName(), uploadFolderFileActivityUrl));
        putItemIntoMap(createFileActivity(subFolderFile.getFileOwner(), subFolderFile.getFileOwner().getUserName() + " 上传了 文件\n" + subFolderFile.getFileName(), uploadSubFolderFileActivityUrl));
    }




    public void constructFileOperationActivity(File operateFile){
        activityMap.clear();
        List<String> createFileActivityUrl = new ArrayList<String>();
        createFileActivityUrl.add(operateFile.getFileOwner().getHomePageLink());
        createFileActivityUrl.add(operateFile.getFileUrl());

        List<String> moveFileActivity = new ArrayList<String>();
        moveFileActivity.addAll(createFileActivityUrl);

        List<String> updateFileActivity = new ArrayList<String>();
        updateFileActivity.addAll(createFileActivityUrl);

        List<String> removeFileActivity = new ArrayList<String>();
        removeFileActivity.add(operateFile.getFileOwner().getHomePageLink());

        putItemIntoMap(createFileActivity(operateFile.getFileOwner(), operateFile.getFileOwner().getUserName() + " 新建了 文件\n" + operateFile.getOldFileFullName(), createFileActivityUrl));
        putItemIntoMap(createFileActivity(operateFile.getFileOwner(), operateFile.getFileOwner().getUserName() + " 删除了 文件\n" + operateFile.getFileFullName(), removeFileActivity));
        putItemIntoMap(createFileActivity(operateFile.getFileOwner(), operateFile.getFileOwner().getUserName() + " 移动了 文件\n" + operateFile.getFileFullName(), moveFileActivity));
        putItemIntoMap(createFileActivity(operateFile.getFileOwner(), operateFile.getFileOwner().getUserName() + " 更新了 文件\n" + operateFile.getFileFullName(), updateFileActivity));


    }
    private Activity createFolderActivity(User sponsor, String activityContent, List<String> folderActivityUrl){
        return  new Activity(sponsor,
                activityContent, activityFileReader.getOpenFolderIconClass(), folderActivityUrl,project);
    }
    private Activity createFileActivity(User sponsor, String activityContent, List<String> fileActivityUrl){
        return  new Activity(sponsor,
                activityContent, activityFileReader.getFileIconClass(), fileActivityUrl,project);
    }

    public void constructMapAfterCommentTest(String commentContent, String atSomeoneContent, String deleteCommentContent,AdvancedTask advancedTask){
        activityMap.clear();
        commentActivityLink.clear();
        commentActivityLink.add(ciwangUser.getHomePageLink());
        commentActivityLink.add(advancedTask.getTaskUrl());
        commentActivityLink.add(advancedTask.getTaskUrl());
        List<String> commentActivityLink1 = new ArrayList<String>();
        commentActivityLink1.addAll(commentActivityLink);
       // putItemIntoMap(getRemoveTaskActivity(ciwangUser,zombieUser,advancedTask.getContent()));
        putItemIntoMap(ciwangTaskCommentActivity(ciwangUser.getUserName() + " 创建了 任务 " + advancedTask.getContent() + " 的评论\n" + commentContent, commentActivityLink1));
        List<String> commentActivityLink2 = new ArrayList<String>();
        commentActivityLink2.addAll(commentActivityLink);
        putItemIntoMap(ciwangTaskCommentActivity(ciwangUser.getUserName() + " 创建了 任务 " + advancedTask.getContent() + " 的评论\n" + atSomeoneContent, commentActivityLink2));
        List<String> commentActivityLink3 = new ArrayList<String>();
        commentActivityLink3.addAll(commentActivityLink);
        putItemIntoMap(ciwangTaskCommentActivity(ciwangUser.getUserName() + " 创建了 任务 " + advancedTask.getContent() + " 的评论\n" + this.replyCommentContent, commentActivityLink3));

        List<String> removeCommentActivityLink = new ArrayList<String>();
        removeCommentActivityLink.addAll(commentActivityLink);
        removeCommentActivityLink.remove(2);
        putItemIntoMap(ciwangTaskCommentActivity(ciwangUser.getUserName() + " 删除了 任务 " + advancedTask.getContent() + " 的评论\n" + deleteCommentContent, removeCommentActivityLink));



    }
    private Activity ciwangTaskCommentActivity(String content,List<String> link){
        return new Activity(ciwangUser,  content, activityFileReader.getCommentIconClass(), link,project);
    }

    private void putItemIntoMap(Activity activity){
        activityMap.put(activity.getContent(), activity);
    }


    public void constructActivityMap(String activityContent,String updatedTaskContent,AdvancedTask advancedTask ){
        activityMap.clear();
        List<String> createTaskLinks = new ArrayList<String>();
        createTaskLinks.add(ciwangUser.getHomePageLink());
        createTaskLinks.add(zombieUser.getHomePageLink());
        createTaskLinks.add(advancedTask.getTaskUrl());
        Activity createTaskActivity = new Activity(ciwangUser,
                ciwangUser.getUserName()+" 创建了 "+zombieUser.getUserName()+" 的 任务\n" + activityContent,activityFileReader.getTaskIconClass(), createTaskLinks,project);

        List<String> updateTaskLinks = new ArrayList<String>();
        updateTaskLinks.addAll(createTaskLinks);
        activityMap.put(createTaskActivity.getContent(),createTaskActivity);
        Activity updateTaskActivity = new Activity(ciwangUser,
                ciwangUser.getUserName() +" 更新了 "+zombieUser.getUserName()+" 的 任务\n" + updatedTaskContent,activityFileReader.getTaskIconClass(), updateTaskLinks,project);
        activityMap.put(updateTaskActivity.getContent(),updateTaskActivity);

        List<String> finishTaskLinks = new ArrayList<String>();
        finishTaskLinks.addAll(createTaskLinks);
        Activity finishTaskActivity = new Activity(ciwangUser,
                ciwangUser.getUserName() +" 完成了 "+zombieUser.getUserName()+" 的 任务\n" + updatedTaskContent,activityFileReader.getTaskIconClass(), finishTaskLinks,project);
        activityMap.put(finishTaskActivity.getContent(),finishTaskActivity);

        List<String> reopenTaskLinks = new ArrayList<String>();
        reopenTaskLinks.addAll(createTaskLinks);
        Activity reopenTaskActivity = new Activity(ciwangUser,
                ciwangUser.getUserName() +" 重新开启了 "+zombieUser.getUserName()+" 的 任务\n" + updatedTaskContent,activityFileReader.getTaskIconClass(), reopenTaskLinks,project);
        activityMap.put(reopenTaskActivity.getContent(),reopenTaskActivity);

        Activity removeTaskActivity = getRemoveTaskActivity(ciwangUser, zombieUser, updatedTaskContent);
        activityMap.put(removeTaskActivity.getContent(),removeTaskActivity);

        List<String> resignTaskLinks = new ArrayList<String>();
        resignTaskLinks.add(ciwangUser.getHomePageLink());
        resignTaskLinks.add(zombieUser.getHomePageLink());
        resignTaskLinks.add(ciwangUser.getHomePageLink());
        resignTaskLinks.add(advancedTask.getTaskUrl());

        Activity resignTaskActivity = new Activity(ciwangUser,
                ciwangUser.getUserName() +" 重新指派了 "+zombieUser.getUserName()+" 的 任务 给 "+ciwangUser.getUserName()+"\n" + updatedTaskContent,
                activityFileReader.getTaskIconClass(), resignTaskLinks,project);
        activityMap.put(resignTaskActivity.getContent(),resignTaskActivity);


        List<String> priorityTaskLinks = new ArrayList<String>();
        priorityTaskLinks.add(ciwangUser.getHomePageLink());
        priorityTaskLinks.add(advancedTask.getTaskUrl());
        Activity updateTaskPriorityActivity = new Activity(ciwangUser,
                ciwangUser.getUserName() +" 更新了任务的优先级\n[优先处理] "+updatedTaskContent ,
                activityFileReader.getPriorityIconClass(), priorityTaskLinks,project);
        activityMap.put(updateTaskPriorityActivity.getContent(),updateTaskPriorityActivity);

        List<String> deadlineTaskLinks = new ArrayList<String>();
        deadlineTaskLinks.addAll(priorityTaskLinks);
        Activity updateTaskDeadlineActivity = new Activity(ciwangUser,
                ciwangUser.getUserName() +" 更新了任务的截止日期\n[明天] "+updatedTaskContent,
                activityFileReader.getDeadlineIconClass(), deadlineTaskLinks,project);
        activityMap.put(updateTaskDeadlineActivity.getContent(),updateTaskDeadlineActivity);

    }
    public void constructCreateProjectActivity(User creator,String projectActivityIconClass, String userIconClass){
        activityMap.clear();
        List<String> projectCreateLinks= new ArrayList<String>();
        projectCreateLinks.add(creator.getHomePageLink());
        projectCreateLinks.add(project.getProjectLink());

        Activity createTaskActivity = new Activity(creator,
                creator.getUserName()+" 创建了 项目\n" + project.getProjectFullName(),
                projectActivityIconClass, projectCreateLinks, new UserCenterActivityNoChanger(),project);
        activityMap.put(createTaskActivity.getContent(),createTaskActivity);

        List<String> addMemberLinks= new ArrayList<String>();
        addMemberLinks.add(creator.getHomePageLink());
        addMemberLinks.add(creator.getHomePageLink());

        Activity addMemberActivity = new Activity(creator,
                creator.getUserName()+" 添加了 项目成员\n" + creator.getUserName(),
                userIconClass, addMemberLinks,project);
        activityMap.put(addMemberActivity.getContent(),addMemberActivity);
    }
    public void constructRenameProjectActivity(User creator,String projectActivityIconClass){
        activityMap.clear();
        List<String> projectRenameLinks= new ArrayList<String>();
        projectRenameLinks.add(creator.getHomePageLink());
        projectRenameLinks.add(project.getProjectLink());

        Activity renameProjectActivity = new Activity(creator,
                creator.getUserName()+" 更新了 项目\n" + project.getProjectFullName(),
                projectActivityIconClass, projectRenameLinks,new UserCenterActivityNoChanger(),project);
        activityMap.put(renameProjectActivity.getContent(), renameProjectActivity);
    }
    public void constructExitProjectActivity(User exitor,String projectActivityIconClass){
        activityMap.clear();
        List<String> exitProjectLinks= new ArrayList<String>();
        exitProjectLinks.add(exitor.getHomePageLink());
        exitProjectLinks.add(project.getProjectLink());

        Activity exitProjectActivity = new Activity(exitor,
                exitor.getUserName()+" 退出了 项目\n" + project.getProjectFullName(),
                projectActivityIconClass, exitProjectLinks, new UserCenterActivityChangerForExitProject(),project);
        activityMap.put(exitProjectActivity.getContent(), exitProjectActivity);
    }
    public void constructAddMemberActivity(User creator,User member,String userIconClass){
        activityMap.clear();
        List<String> addMember= new ArrayList<String>();
        addMember.add(creator.getHomePageLink());
        addMember.add(member.getHomePageLink());

        Activity renameProjectActivity = new Activity(creator,
                creator.getUserName()+" 添加了 项目成员\n" + member.getUserName(),
                userIconClass, addMember,project);
        activityMap.put(renameProjectActivity.getContent(), renameProjectActivity);
    }
    public void constructRemoveMemberActivity(User creator,User member,String userIconClass){
        activityMap.clear();
        List<String> removeMember= new ArrayList<String>();
        removeMember.add(creator.getHomePageLink());
        removeMember.add(member.getHomePageLink());

        Activity renameProjectActivity = new Activity(creator,
                creator.getUserName()+" 移除了 项目成员\n" + member.getUserName(),
                userIconClass, removeMember,project);
        activityMap.put(renameProjectActivity.getContent(), renameProjectActivity);
    }
    public void constructOwnerChangeActivity(User oldOwner, User newOwner, String activityIconClass){
        activityMap.clear();
        List<String> ownerChangeLinks= new ArrayList<String>();
        ownerChangeLinks.add(oldOwner.getHomePageLink());
        ownerChangeLinks.add(project.getProjectLink());
        ownerChangeLinks.add(newOwner.getHomePageLink());
        ownerChangeLinks.add(project.getProjectLink());
        Activity changeOwner = new Activity(oldOwner,
                oldOwner.getUserName()+" 将项目 "+project.getProjectFullName()+" 转让给了 " + newOwner.getUserName() +"\n" + project.getProjectFullName(),
                activityIconClass, ownerChangeLinks, new UserCenterActivityNoChanger(),project);
        activityMap.put(changeOwner.getContent(), changeOwner);
    }
    public void constructCreateTopicInPublicProjectActivity(Topic topic, String activityIconClass){
        activityMap.clear();
        List<String> createTopicLinks= new ArrayList<String>();
        createTopicLinks.add(topic.getTopicOwner().getHomePageLink());
        createTopicLinks.add(project.getProjectLink());
        createTopicLinks.add(topic.getTopicUrl());
        Activity createTaskActivity = new Activity(topic.getTopicOwner(),
                topic.getTopicOwner().getUserName()+" 在项目 "+project.getProjectFullName()+" 中 创建了 讨论"+"\n" + topic.getTopicTitle(),
                activityIconClass, createTopicLinks,project);
        activityMap.put(createTaskActivity.getContent(),createTaskActivity);
    }
    public void constructCreateTopicInPrivateProjectActivity(Topic topic, String activityIconClass){
        activityMap.clear();
        List<String> createTopicLinks= new ArrayList<String>();
        createTopicLinks.add(topic.getTopicOwner().getHomePageLink());
        createTopicLinks.add(topic.getTopicUrl());
        Activity createTaskActivity = new Activity(topic.getTopicOwner(),
                topic.getTopicOwner().getUserName()+" 创建了 讨论"+"\n" + topic.getTopicTitle(),
                activityIconClass, createTopicLinks,project);
        activityMap.put(createTaskActivity.getContent(),createTaskActivity);
    }
    public void constructEditAndDeleteTopicInPrivateProjectActivity(Topic topic, String activityIconClass){
        activityMap.clear();
        List<String> createTopicLinks= new ArrayList<String>();
        createTopicLinks.add(topic.getTopicOwner().getHomePageLink());
        createTopicLinks.add(topic.getTopicUrl());
        Activity createTaskActivity = new Activity(topic.getTopicOwner(),
                topic.getTopicOwner().getUserName()+" 更新了 讨论\n" +topic.getTopicTitle(),
                activityIconClass, createTopicLinks,project);
        activityMap.put(createTaskActivity.getContent(),createTaskActivity);
        Activity deleteTaskActivity = new Activity(topic.getTopicOwner(),
                topic.getTopicOwner().getUserName()+" 删除了 讨论\n"+ topic.getTopicTitle(),
                activityIconClass, createTopicLinks,project);
        activityMap.put(createTaskActivity.getContent(),deleteTaskActivity);
    }
    public void constructTopicCommentInPrivateProjectActivity(Topic topic, String commentContent, String activityIconClass){
        activityMap.clear();
        List<String> createTopicLinks= new ArrayList<String>();
        createTopicLinks.add(topic.getTopicOwner().getHomePageLink());
        createTopicLinks.add(topic.getTopicUrl());
        Activity createTaskActivity = new Activity(topic.getTopicOwner(),
                topic.getTopicOwner().getUserName()+" 评论了 讨论 "+ topic.getTopicTitle()+ "\n"+commentContent,
                activityIconClass, createTopicLinks,project);
        activityMap.put(createTaskActivity.getContent(),createTaskActivity);
    }

    private Activity getRemoveTaskActivity(User remover, User taskExecutive, String taskContent){
        List<String> removeTaskLinks = new ArrayList<String>();
        removeTaskLinks.add(remover.getHomePageLink());
        removeTaskLinks.add(taskExecutive.getHomePageLink());
        return   new Activity(remover,
                remover.getUserName() +" 删除了 "+taskExecutive.getUserName()+" 的 任务\n" + taskContent,activityFileReader.getTaskIconClass(), removeTaskLinks,project);

    }
    public void checkTaskActivity(Page page,int redLightCount, int whiteLightCount) throws ParseException {

        page.navigate(project.getProjectLink(), activityFileReader.getActivityTime());
        Map<String, ItemEntry> activityMapInProjectHome = new HashMap<String, ItemEntry>();
        activityMapInProjectHome.putAll(activityMap);
        checkTaskUrgency(page, redLightCount, whiteLightCount);
        activityCheck(page, activityMapInProjectHome);


        page.clickElement(activityFileReader.getTaskActivityTagInProjectHomePage(), activityFileReader.getActivityItem());
        page.waitElementInvisible(".loading.icon");
        Map<String, ItemEntry> activityMapForTag = new HashMap<String, ItemEntry>();
        activityMapForTag.putAll(activityMap);
        checkTaskUrgency(page, redLightCount, whiteLightCount);
        activityCheck(page, activityMapForTag);
        /*
        page.navigate(project.getProjectMemberLink(((ItemEntry) activityMap.values().toArray()[0]).getSponsor().getUserLoginName()),
                activityFileReader.getActivityItem());
        page.waitElementInvisible(".loading.icon");
        Map<String, ItemEntry> activityInProjectMember = new HashMap<String, ItemEntry>();
        activityInProjectMember.putAll(activityMap);
        checkTaskUrgency(page,redLightCount,whiteLightCount);
        activityCheck(page, activityInProjectMember);
        */

        page.navigate(page.getBaseUrl()+"user", activityFileReader.getActivityItem());
        page.waitElementInvisible(".loading.icon");
        checkTaskUrgency(page,redLightCount,whiteLightCount);
        activityCheck(page, changeActivityFormatToUserCenter(activityMap));
    }
    public void checkActivity(Page page, String activityTagInProjectHomePage)  {
        page.navigate(project.getProjectLink(), activityFileReader.getActivityTime());
        Map<String, ItemEntry> activityMapInProjectHome = new HashMap<String, ItemEntry>();
        activityMapInProjectHome.putAll(activityMap);
        activityCheck(page, activityMapInProjectHome);


        page.clickElement(activityTagInProjectHomePage, activityFileReader.getActivityItem());
        page.waitElementInvisible(".loading.icon");
        Map<String, ItemEntry> activityMapForTag = new HashMap<String, ItemEntry>();
        activityMapForTag.putAll(activityMap);
        activityCheck(page, activityMapForTag);
        //批量处理的动态中有多个项目成员的动态时，要改变下测试方式
        /*
        page.navigate(project.getProjectMemberLink(((ItemEntry) activityMap.values().toArray()[0]).getSponsor().getUserLoginName()),
                activityFileReader.getActivityItem());
        page.waitElementInvisible(".loading.icon");
        Map<String, ItemEntry> activityInProjectMember = new HashMap<String, ItemEntry>();
        activityInProjectMember.putAll(activityMap);
        activityCheck(page, activityInProjectMember);
        */

        page.navigate(page.getUserCenterUrl(), activityFileReader.getActivityItem());
        page.waitElementInvisible(".loading.icon");
        activityCheck(page, changeActivityFormatToUserCenter(activityMap));
    }


    private Map<String, ItemEntry> changeActivityFormatToUserCenter( Map<String, ItemEntry> activityMap){
        Map<String, ItemEntry> activityInUserCenter = new HashMap<String, ItemEntry>();
        Iterator iterator = activityMap.entrySet().iterator();
        Map.Entry<String, ItemEntry> entry;
        Activity tempActivity ;
        for (int i = 0; i < activityMap.size(); i++) {
            entry = (Map.Entry)iterator.next();
            tempActivity = (Activity) entry.getValue();
            tempActivity.changeToUserCenterFormat();
            activityInUserCenter.put(tempActivity.getContent(), tempActivity);
        }
        return activityInUserCenter;
    }



    private void checkTaskUrgency(Page page,int redLightCount, int whiteLightCount){
        if(redLightCount == 0 && whiteLightCount == 0){
            return;
        }
        assertEquals("动态中任务紧急度中亮着的灯数目不对", redLightCount, page.getElement(advancedTaskFileReader.getTaskPriorityActivityTarget()).
                findElements(By.cssSelector(advancedTaskFileReader.getTaskPriorityIcon() + ".active")).size());
        assertEquals("动态中任务紧急度中不亮的灯数目不对",whiteLightCount,page.getElement(advancedTaskFileReader.getTaskPriorityActivityTarget(),1).
                findElements( By.cssSelector(advancedTaskFileReader.getTaskPriorityLightIcon()) ).size());
    }

    private void activityCheck(Page page, Map<String, ItemEntry> activityMap) {
        checkActivityAvatar(page, activityMap, activityFileReader.getActivityItem());
        assertActivityTime(page);

        page.checkItemInMap(activityMap, activityFileReader.getActivityContent(), activityFileReader.getActivityIcon(),"动态");
        page.checkItemEntryMap(activityMap);
    }

    public void watchActivityCheck(Page page) throws ParseException {
        Map<String, ItemEntry> tempActivityMap = new HashMap<String, ItemEntry>();
        tempActivityMap.putAll(activityMap);
        checkActivityAvatar(page, tempActivityMap, activityFileReader.getActivityItem());
        assertActivityTime(page);

        page.checkItemInMap(tempActivityMap, activityFileReader.getActivityContent(), activityFileReader.getActivityIcon(),"任务动态");
        page.checkItemEntryMap(tempActivityMap);
    }
    public void assertActivityTime(Page page)  {
        DateFormat df = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        try {
            assertTrue("动态的时间有误,或未出现动态",
                    (df.parse(df.format(date)).getTime() - df.parse(page.getText(activityFileReader.getActivityTime())).getTime()) / 60000 < 15);
        }catch (ParseException e){

        }
    }



    private void checkActivityAvatar(Page page,  Map<String,ItemEntry> itemEntryMap, String itemContentSelector ){
        for(int i = 0; i <  itemEntryMap.size(); i++){
            if(itemEntryMap.containsKey(page.getText(itemContentSelector, i))){
                page.checkAvatar(activityFileReader.getActivitySponsorAvatarImg(),
                        activityFileReader.getActivitySponsorAvatarLink(),activityMap.get(page.getText(activityFileReader.getActivityContent())).getSponsor(),i);
            }
        }
    }
}

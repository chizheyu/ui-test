package org.ChiTest.AdvancedTask;

import classForComplicatedData.Urgency;
import org.ChiTest.Page.Page;
import org.ChiTest.Project.Project;
import org.ChiTest.User.User;
import org.ChiTest.WebComponent.MarkDownInputBox;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by dugancaii on 12/3/2014.
 */
public class AdvancedTask {
    private int status;
    private String deadline;
    private String time;
    private User creator;
    private Urgency priority;
    private  AdvancedTaskFileReader advancedTaskFileReader;
    public String getTaskUrl() {
        return taskUrl;
    }

    public void setTaskUrl(String taskUrl) {
        this.taskUrl = taskUrl;
    }

    private String taskUrl;

    public String getMemberTaskLink( User member){
        return this.getProject().getProjectTaskLink()+"/user/"+ member.getUserLoginName()+"/all";
    }
    public Project getProject() {
        return project;
    }

    private Project project;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public Urgency getPriority() {
        return priority;
    }

    public void setPriority(Urgency priority) {
        this.priority = priority;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public User getExecutive() {
        return executive;
    }

    public void setExecutive(User executive) {
        this.executive = executive;
    }

    public String getContent() {
        return content;
    }

    public void setContent(Page cp,String content) {
        cp.clickElement(advancedTaskFileReader.getTaskContentInTaskList());
        cp.clearAndSendKeys(advancedTaskFileReader.getTaskContentInTaskList(), content);
        cp.getBuilder().sendKeys(Keys.ENTER).perform();
        waitOperator(cp);
        this.content = content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    private String content;
    private User executive;
    public AdvancedTask(){
        this.status = 1;
        this.deadline = "今天";
        this.time = "几秒前";
        this.priority = Urgency.EMERGENCY;
        this.description = "";
        this.advancedTaskFileReader = new AdvancedTaskFileReader();
    }

    public AdvancedTask(int status, String deadline, User creator, String time, User executive, Urgency priority, String description,String content,Project project) {
        this.status = status;
        this.deadline = deadline;
        this.creator = creator;
        this.time = time;
        this.executive = executive;
        this.priority = priority;
        this.description = description;
        this.content = content;
        this.project = project;
        this.advancedTaskFileReader = new AdvancedTaskFileReader();
    }
    public AdvancedTask( User creator, Project project, User executive,String content) {
        this();
        this.creator = creator;
        this.executive = executive;
        this.content = content;
        this.project = project;
    }
    public boolean checkSomeoneTaskEmpty(Page page,Project project, User user){
        page.navigate(project.getProjectTaskLink() + "/user/" + user.getUserLoginName() + "/all", advancedTaskFileReader.getTaskCountInHeader());
        page.getWaitTool().waitForContentNotNull(advancedTaskFileReader.getTaskCountInHeader(),15);
        page.waitElementInvisible(advancedTaskFileReader.getTaskLoadingIcon());
        return page.getPageNumInParentheses(advancedTaskFileReader.getTaskCountInHeader()) == 0;
    }

    public void deleteSomeoneAllTasks(Page page, Project project, User user){
        if(!checkSomeoneTaskEmpty(page, project, user)){
            deleteAllTasksInOneTaskClassify(page);
        }
    }
    public void deleteMyWatchedTask(User user){
        Page page = user.getPage();
        page.navigate(user.getMyTaskLink());
        page.clickElement(advancedTaskFileReader.getTaskInnerMenuIndex(),3);
        page.waitElementInvisible(advancedTaskFileReader.getTaskLoadingIcon());
        deleteAllTasksInOneTaskClassify(page);
    }


    public void deleteAllTasksInOneTaskClassify(Page page){
        String countText;
        int i = 0;
        while(page.getPageNum(advancedTaskFileReader.getTaskCountInHeader()) != 0 &&
                i != page.getPageNum(advancedTaskFileReader.getTaskCountInHeader())){
            if(page.elementIsPresent(advancedTaskFileReader.getTaskItem(),2)) {
                countText = page.getText(advancedTaskFileReader.getTaskCountInHeader());
                page.moveToElement(advancedTaskFileReader.getTaskItem(),i);
                if(page.elementIsPresent(advancedTaskFileReader.getTaskRemoveButton(),1)) {
                    page.sureAlert(advancedTaskFileReader.getTaskRemoveButton());
                    page.waitForContentChange(advancedTaskFileReader.getTaskCountInHeader(), countText, 6);
                }else {

                    i++;
                }
            }
            else{
                if(page.elementIsPresent(".cg .page.next",2)) {
                    page.clickElement(".cg .page.next",advancedTaskFileReader.getTaskItem());
                }
                if(page.elementIsPresent(".cg .page.prev",2)){
                    page.clickElement(".cg .page.prev",advancedTaskFileReader.getTaskItem());
                }
            }
        }
    }

    public void sendSimpleTask(Page page,String executiveName,String taskContent){
        sendSimpleTask(page,executiveName,taskContent,page.getProjectUrl());
    }

    public void sendSimpleTask(Page page,String executiveName,String taskContent, String sendUrl){
        page.navigateContiansUrl(sendUrl,advancedTaskFileReader.getTaskCountInHeader());
        page.waitElementInvisible(advancedTaskFileReader.getTaskLoadingIcon());
        String countIndexText = page.getText(advancedTaskFileReader.getTaskCountInHeader());
        page.clearAndSendKeys(advancedTaskFileReader.getTaskContentInput(), taskContent);
        if(!executiveName.equals(page.getAttribute(advancedTaskFileReader.getTaskExecutiveButtonForCreate(),"title")) ) {
            page.clickElement(advancedTaskFileReader.getTaskExecutiveButtonForCreate());
            page.sendKeys(advancedTaskFileReader.getTaskExecutiveInput(), executiveName);
            page.clickElement(advancedTaskFileReader.getTaskExecutiveSelectedItem());
        }
        page.clickElement(advancedTaskFileReader.getTaskSendConfirmButton());
        page.waitElementDisappear(advancedTaskFileReader.getTaskSendWait());
        assertTrue("发送任务后，页面上的header计数没有变化，或发送失败 " + countIndexText + "   "+  taskContent,
                page.waitForContentChange(advancedTaskFileReader.getTaskCountInHeader(), countIndexText, 30));
    }
    public void sendSimpleTaskWithDescription(Page cp, String taskContent, String taskDescriptionContent) throws ParseException {
        MarkDownInputBox taskDescriptionMDBox = new MarkDownInputBox(advancedTaskFileReader.getTaskDescriptionMDBox());
        cp.clearAndSendKeys(advancedTaskFileReader.getTaskContentInput(), taskContent);
        Actions builder = new Actions(cp.getDriver());
        builder.moveToElement(cp.getElement(advancedTaskFileReader.getTaskContentInput())).click().build().perform();
        cp.clearAndSendKeys(taskDescriptionMDBox.getBoxInputSelector(), taskDescriptionContent);
        int taskCount = cp.getElementCount(advancedTaskFileReader.getTaskTimeInTaskList());
        cp.clickElement(advancedTaskFileReader.getTaskSendConfirmButton());
        cp.waitForItemCountChange(advancedTaskFileReader.getTaskTimeInTaskList(), taskCount, 10);
    }
    public void deleteAllTasks(Project project){
        Page page = project.getCreator().getPage();
        page.navigate(project.getProjectAllTaskLink(),advancedTaskFileReader.getTaskContentInput());
        page.waitElementInvisible(advancedTaskFileReader.getTaskLoadingIcon());
        deleteAllTasksInOneTaskClassify(page);
        page.refresh(advancedTaskFileReader.getTaskContentInput());
    }

    public void changeExecutiveInCreateTask(Page page, String executiveName){
        if(page.getAttribute(advancedTaskFileReader.getTaskExecutiveButtonForCreate(),"title").equals(executiveName)){
            return;
        }
        page.clickElement(advancedTaskFileReader.getTaskExecutiveButtonForCreate(),advancedTaskFileReader.getTaskExecutiveInput());
        page.clearAndSendKeys(advancedTaskFileReader.getTaskExecutiveInput(), executiveName);
        page.clickElement(advancedTaskFileReader.getTaskExecutiveSelectedItem());
        waitOperator(page);
    }

    public  AdvancedTask getAdvancedTestTask(User creator, Project project,User executive){
        return new AdvancedTask(creator,project,executive, "this is a test task");
    }

    public void putTestTaskForSomeone(Page page, AdvancedTask advancedTask) throws InterruptedException {
        page.navigate(advancedTask.getMemberTaskLink(advancedTask.getExecutive()), advancedTaskFileReader.getTaskContentInput());
        page.waitElementInvisible(advancedTaskFileReader.getTaskLoadingIcon());
        int taskCount = page.getElementCount(advancedTaskFileReader.getTaskItem());
        System.out.println("task count before sending" + taskCount);
        changeExecutiveInCreateTask(page, advancedTask.getExecutive().getUserName());
        page.clearAndSendKeys(advancedTaskFileReader.getTaskContentInput(),advancedTask.getContent());
        page.clickElement(advancedTaskFileReader.getTaskSendConfirmButton());
        page.waitElementDisappear(advancedTaskFileReader.getTaskSendWait());
        page.waitForItemCountChange(advancedTaskFileReader.getTaskItem(), taskCount, 10);
        System.out.println("task count after sending" + taskCount);
        int i = 0;
        while(taskCount ==  page.getElementCount(advancedTaskFileReader.getTaskItem())){
            if(i > 3){
                assertEquals("测试退出项目或踢出项目成员时，发送任务失败", 1,2);
            }
            System.out.println("第"+i+"次发任务不成功");
            page.refresh(advancedTaskFileReader.getTaskContentInput());
            page.clearAndSendKeys(advancedTaskFileReader.getTaskContentInput(),advancedTask.getContent());
            page.clickElement(advancedTaskFileReader.getTaskSendConfirmButton());
            page.waitElementDisappear(advancedTaskFileReader.getTaskSendWait());
            i++;
        }
    }
    public void removeTask(Page page, int taskNum){
        String countText = page.getText(advancedTaskFileReader.getTaskCountInHeader());
        page.moveToElement(advancedTaskFileReader.getTaskItem(), taskNum);
        page.clickElement(advancedTaskFileReader.getTaskRemoveButton(), taskNum);
        page.getDriver().switchTo().alert().accept();
        page.waitForContentChange(advancedTaskFileReader.getTaskCountInHeader(), countText, 6);
    }

    private void waitOperator(Page page){
        page.getWaitTool().waitForElement(By.cssSelector(advancedTaskFileReader.getTaskLoadingIcon()), 2);
        page.waitElementInvisible(advancedTaskFileReader.getTaskLoadingIcon());
    }
    public void changeDeadline(Page page, String deadlineButtonSelector){
        page.clickElement(advancedTaskFileReader.getTaskDeadlineInTaskList(), deadlineButtonSelector);
        page.clickElement(deadlineButtonSelector);
        waitOperator(page);
    }


    public void changePriority(Page page,Urgency urgency){
        //有时候修改内容后，优先级选择列表自动出现了
        if(!page.elementIsPresent(advancedTaskFileReader.getTaskPriorityChoiceInTaskList())) {
            page.clickElement(advancedTaskFileReader.getTaskPriorityButtonInTaskList(), advancedTaskFileReader.getTaskPriorityChoiceInTaskList());
        }
        page.clickElement(advancedTaskFileReader.getTaskPriorityChoiceInTaskList(), 3 - urgency.getValue());
        waitOperator(page);
    }

    public void changeExecutive(Page page, String executiveName){
        if(page.getAttribute(advancedTaskFileReader.getTaskExecutiveButtonInTaskList(),"title").equals(executiveName)){
            return;
        }
        page.clickElement(advancedTaskFileReader.getTaskExecutiveButtonInTaskList(),advancedTaskFileReader.getTaskExecutiveInputInTaskList());
        page.clearAndSendKeys(advancedTaskFileReader.getTaskExecutiveInputInTaskList(), executiveName);
        page.clickElement(advancedTaskFileReader.getTaskExecutiveSelectedItemInTaskList());
        waitOperator(page);
    }
}

package org.ChiTest.AdvancedTask;

import classForComplicatedData.Urgency;
import org.ChiTest.Page.Page;
import org.ChiTest.Project.Project;
import org.ChiTest.User.User;
import org.ChiTest.WebComponent.MarkDownInputBox;
import org.openqa.selenium.By;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by dugancaii on 12/18/2014.
 */
public class AdvancedTaskChecker {
    private Project zombieProject;
    private AdvancedTaskFileReader advancedTaskFileReader;
    private MarkDownInputBox markDownInputBox;
    private AdvancedTask advancedTask;

    public String getActivityContent() {
        return advancedTask.getContent().replaceAll("<img.*?>","");
    }

    private  String activityContent;
    public AdvancedTaskChecker (Project zombieProject) throws ParseException {
        this.zombieProject = zombieProject;
        this.advancedTaskFileReader = new AdvancedTaskFileReader();
        this.markDownInputBox = new MarkDownInputBox("");
    }

    public AdvancedTask sendStandardTaskInProjectPage( User taskCreator, User executive, Project project){
        Page cp = taskCreator.getPage();
        cp.navigate(project.getProjectTaskLink());
        cp.waitElementInvisible(advancedTaskFileReader.getTaskLoadingIcon());
        return sendStandardTask( cp,taskCreator, executive, project);
    }
    public AdvancedTask sendStandardTaskInMyTaskPage( User taskCreator, User executive, Project project){
        Page cp = taskCreator.getPage();
        navigateToMyTaskToSendTask(cp, taskCreator);
        return sendStandardTask( cp,taskCreator, executive,project);
    }
    public void navigateToMyTaskToSendTask(Page cp, User taskCreator){
        cp.navigate(taskCreator.getMyTaskLink(),  advancedTaskFileReader.getTaskProjectIndexItem());
        cp.clickElement(advancedTaskFileReader.getTaskProjectIndexItem(),
                cp.getIndexPosition(zombieProject.getProjectName(), advancedTaskFileReader.getTaskProjectIndexItem())
                ,advancedTaskFileReader.getTaskContentInput());
    }
    private AdvancedTask sendStandardTask(Page cp, User taskCreator, User executive,  Project project){
        advancedTask = new AdvancedTask(1,"今天",taskCreator,"几秒前",executive, Urgency.EMERGENCY,
                "法轮大法我是一个测试任务",cp.getMark() +"任务测试<img a = 'href' > **sdfd** "+ markDownInputBox.getSpecialSign(),project);

        cp.clickElement(advancedTaskFileReader.getDeadlineButton());
        cp.clickElement(advancedTaskFileReader.getDeadlineButtonForToday());

        cp.clickElement(advancedTaskFileReader.getTaskPriorityButton(), advancedTaskFileReader.getTaskPriorityChoice());
        cp.clickElement(advancedTaskFileReader.getTaskPriorityChoice(), 0);

        cp.clearAndSendKeys(advancedTaskFileReader.getTaskContentInput(), advancedTask.getContent());
        if(!cp.getAttribute(advancedTaskFileReader.getTaskExecutiveButtonForCreate(),"title").equals(executive.getUserName())){
            cp.clickElement(advancedTaskFileReader.getTaskExecutiveButtonForCreate(), advancedTaskFileReader.getTaskExecutiveSelectedItem());
            cp.clearAndSendKeys(advancedTaskFileReader.getTaskExecutiveInput(),executive.getUserName());
            cp.clickElement(advancedTaskFileReader.getTaskExecutiveSelectedItem());
        }
        cp.clickElement(advancedTaskFileReader.getTaskSendConfirmButton());
        cp.waitElementDisappear(advancedTaskFileReader.getTaskSendWait());
        return  advancedTask;
    }


    public void verifyTaskContent(Page page, AdvancedTask advancedTask){
        assertEquals("发送任务后，任务列表中十万火急的任务的灯的数目不对",advancedTask.getPriority().getValue(),
                getPriorityLightCount(0, advancedTaskFileReader.getTaskPriorityButtonInTaskList(), advancedTaskFileReader.getTaskPriorityIcon(), page));
        assertEquals("发送任务后，任务列表中任务的截止日期好像不是今天", advancedTask.getDeadline(), page.getText(advancedTaskFileReader.getTaskDeadlineInTaskList() + ".today"));
        assertTrue("发送任务后，任务图标中时间图案消失", page.elementIsPresent(advancedTaskFileReader.getTaskTimeInTaskList() + " .time.icon", 2));
        assertTrue("发送任务后，任务列表中显示发送时间有误", advancedTask.getTime().equals(page.getText(advancedTaskFileReader.getTaskTimeInTaskList()))
                || "几秒内".equals(page.getText(advancedTaskFileReader.getTaskTimeInTaskList()))
                || "几秒前".equals(page.getText(advancedTaskFileReader.getTaskTimeInTaskList()))
                || "1分钟前".equals(page.getText(advancedTaskFileReader.getTaskTimeInTaskList())));
        assertTrue("发送任务后，任务图标中评论图案消失", page.elementIsPresent(advancedTaskFileReader.getTaskCommentButtonInTaskList() + " .chat.outline.icon", 2));
        assertEquals("发送任务后，任务列表中评论数有误", "0 条评论", page.getText(advancedTaskFileReader.getTaskCommentButtonInTaskList()));
        assertEquals("发送任务后，任务列表中，执行者有误", advancedTask.getExecutive().getUserName(), page.getAttribute(advancedTaskFileReader.getTaskExecutiveButtonInTaskList(), "title"));
        assertEquals("发送任务后，任务列表中任务内容不对", advancedTask.getContent(), page.getAttribute(advancedTaskFileReader.getTaskContentInTaskList(), "value"));
        assertEquals("发送任务后，任务的创建者有误", advancedTask.getCreator().getUserName(), page.getText(advancedTaskFileReader.getTaskCreatorNameInTaskList()));
    }
    public void verifyTaskContentInDetailPage(Page page, AdvancedTask advancedTask){
        assertEquals("发送任务后，任务列表中十万火急的任务的灯的数目不对",advancedTask.getPriority().getValue(),
                getPriorityLightCount(0, advancedTaskFileReader.getTaskPriorityButtonInTaskList(), advancedTaskFileReader.getTaskPriorityIcon(), page));
        assertEquals("发送任务后，任务列表中任务的截止日期好像不是今天", advancedTask.getDeadline(), page.getText(advancedTaskFileReader.getTaskDeadlineInTaskList() + ".today"));
        assertTrue("发送任务后，任务图标中时间图案消失", page.elementIsPresent(advancedTaskFileReader.getTaskTimeInDetailPage() + " .time.icon", 2));
        assertTrue("发送任务后，任务列表中显示发送时间有误"+ page.getText(advancedTaskFileReader.getTaskTimeInDetailPage()) , advancedTask.getTime().equals(page.getText(advancedTaskFileReader.getTaskTimeInDetailPage()))
                || "几秒内".equals(page.getText(advancedTaskFileReader.getTaskTimeInDetailPage()))
                || "1分钟前".equals(page.getText(advancedTaskFileReader.getTaskTimeInDetailPage())));
        assertTrue("发送任务后，任务图标中评论图案消失", page.elementIsPresent(advancedTaskFileReader.getTaskCommentInDetailPage() + " .chat.outline.icon", 2));
        assertEquals("发送任务后，任务列表中评论数有误", "0 条评论", page.getText(advancedTaskFileReader.getTaskCommentInDetailPage()));
        assertEquals("发送任务后，任务列表中，执行者有误", advancedTask.getExecutive().getUserName(), page.getAttribute(advancedTaskFileReader.getTaskExecutiveButtonInDetailPage(), "title"));
        assertEquals("发送任务后，任务列表中任务内容不对", advancedTask.getContent(), page.getAttribute(advancedTaskFileReader.getTaskContentInTaskList(), "value"));
        assertEquals("发送任务后，任务的创建者有误", advancedTask.getCreator().getUserName(),
                page.getText(advancedTaskFileReader.getTaskCreatorNameInTaskList()));
    }
    public int getPriorityLightCount(int priorityItemNum, String priorityChoiceSelector, String taskPriorityIcon, Page page){
        if(page.elementIsPresent(priorityChoiceSelector, priorityItemNum,
                taskPriorityIcon)){
            return page.getElement(priorityChoiceSelector, priorityItemNum).
                    findElements(By.cssSelector(taskPriorityIcon)).size();
        }
        return 0;
    }
}

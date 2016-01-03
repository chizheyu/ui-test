package classForComplicatedData;

import org.ChiTest.AdvancedTask.AdvancedTaskFileReader;
import org.ChiTest.Page.Page;
import org.ChiTest.User.User;

import static org.junit.Assert.assertEquals;

/**
 * Created by dugancaii on 12/5/2014.
 */
public class CountIndexInProjectPage implements TaskCountIndex {
    private AdvancedTaskFileReader advancedTaskFileReader;
    private User ciwangUser;
    private User zombieUser;
    public CountIndexInProjectPage(Page page, User ciwangUser, User zombieUser)  {
        this(ciwangUser, zombieUser);
        updateTaskIndex(page);
    }
    public CountIndexInProjectPage(User ciwangUser, User zombieUser){
        advancedTaskFileReader = new AdvancedTaskFileReader();
        this.ciwangUser = ciwangUser;
        this.zombieUser = zombieUser;
    }
    private int allTaskCount;
    private int processingTaskCount;
    private int finishedTaskCount;
    private int wathchedTaskCount;
    private int headerTaskCount;
    private int zombieProcessingTaskCount;
    private int zombieAllTaskCount;
    private int ciwangProcessingTaskCount;
    private int ciwangAllTaskCount;
    private String processingCountText = " ";

    public void updateTaskIndex(Page page)  {
      //  page.waitForContentChange(advancedTaskFileReader.getTaskInnerMenuIndex(),1,processingCountText,10);
        processingTaskCount =  Integer.parseInt(page.getText(advancedTaskFileReader.getTaskInnerMenuIndex(),1));
        allTaskCount =  Integer.parseInt(page.getText(advancedTaskFileReader.getTaskInnerMenuIndex()));
        finishedTaskCount =  Integer.parseInt(page.getText(advancedTaskFileReader.getTaskInnerMenuIndex(),2));
        wathchedTaskCount =  Integer.parseInt(page.getText(advancedTaskFileReader.getTaskInnerMenuIndex(),3));
        if(page.elementIsPresent(advancedTaskFileReader.getTaskCountInHeader(),1)) {
            headerTaskCount = page.getPageNumInParentheses(advancedTaskFileReader.getTaskCountInHeader());
        }else {
            headerTaskCount = -1;
        }
        ciwangAllTaskCount = Integer.parseInt(page.getText(advancedTaskFileReader.getTaskMemberIndexRate(),
                page.getIndexPosition(ciwangUser.getUserName(),advancedTaskFileReader.getTaskMemberIndexItem())).split("/")[1]);
        ciwangProcessingTaskCount = Integer.parseInt(page.getText(advancedTaskFileReader.getTaskMemberIndexRate(),
                page.getIndexPosition( ciwangUser.getUserName(),advancedTaskFileReader.getTaskMemberIndexItem())).split("/")[0]);
        zombieAllTaskCount = Integer.parseInt(page.getText(advancedTaskFileReader.getTaskMemberIndexRate(),
                page.getIndexPosition( zombieUser.getUserName(),advancedTaskFileReader.getTaskMemberIndexItem())).split("/")[1]);
        zombieProcessingTaskCount = Integer.parseInt(page.getText(advancedTaskFileReader.getTaskMemberIndexRate(),
                page.getIndexPosition( zombieUser.getUserName(),advancedTaskFileReader.getTaskMemberIndexItem())).split("/")[0]);
     //   processingCountText =  page.getText(advancedTaskFileReader.getTaskInnerMenuIndex(),1);
    }

    public int getUserProcessingTaskCount(String userName){
        if(userName == ciwangUser.getUserName()){
            return ciwangProcessingTaskCount;
        }
        if(userName == zombieUser.getUserName()){
            return zombieProcessingTaskCount;
        }
        return -1;
    }
    public int getUserAllTaskCount(String userName){
        if(userName == ciwangUser.getUserName()){
            return ciwangAllTaskCount;
        }
        if(userName == zombieUser.getUserName()){
            return zombieAllTaskCount;
        }
        return -1;
    }


    public int getHeaderTaskCount() {
        return headerTaskCount;
    }

    public int getWathchedTaskCount() {
        return wathchedTaskCount;
    }

    public int getFinishedTaskCount() {
        return finishedTaskCount;
    }

    public int getProcessingTaskCount() {
        return processingTaskCount;
    }

    public int getAllTaskCount() {
        return allTaskCount;
    }



    public void finishTaskIndexTest(String itemName, TaskCountIndex indexAfterChange) {
        assertEquals("在项目任务页面完成"+itemName+"的任务后，正在进行任务计数没有减1",
                this.getProcessingTaskCount() - 1, indexAfterChange.getProcessingTaskCount());
        if(this.getHeaderTaskCount() != -1) {
            assertEquals("在项目任务页面完成" + itemName + "的任务后，header任务计数没有减1",
                    this.getHeaderTaskCount() - 1, indexAfterChange.getHeaderTaskCount());
        }
        assertEquals("在项目任务页面完成"+itemName+"的任务后，zombie正在进行任务计数没有减1",
                this.getUserProcessingTaskCount(itemName) - 1,
                ( (CountIndexInProjectPage) indexAfterChange).getUserProcessingTaskCount(itemName));
        assertEquals("在项目任务页面完成" + itemName + "的任务后，已完成的任务数没有加1",
                this.getFinishedTaskCount() + 1, indexAfterChange.getFinishedTaskCount());
    }


    public void reopenTaskIndexTest(String itemName, TaskCountIndex indexAfterChange) {
        assertEquals("在项目任务页面重新开启"+itemName+"的任务后，正在进行任务计数没有加1",
                this.getProcessingTaskCount() , indexAfterChange.getProcessingTaskCount());
        if(this.getHeaderTaskCount() != -1) {
            assertEquals("在项目任务页面重新开启" + itemName + "的任务后，header任务计数没有加1",
                    this.getHeaderTaskCount(), indexAfterChange.getHeaderTaskCount());
        }
        assertEquals("在项目任务页面重新开启"+itemName+"的任务后，zombie正在进行任务计数没有加1",
                this.getUserProcessingTaskCount(itemName) ,
                ( (CountIndexInProjectPage) indexAfterChange).getUserProcessingTaskCount(itemName));
        assertEquals("在项目任务页面重新开启"+itemName+"的任务后，已完成的任务数没有减1",
                this.getFinishedTaskCount()  , indexAfterChange.getFinishedTaskCount());
    }


    public void createTaskIndexTest(String itemName, TaskCountIndex indexAfterChange) {
        assertEquals("在项目任务页面发送任务给" + itemName + "后所有任务计数没有+1",
                this.getAllTaskCount() + 1, indexAfterChange.getAllTaskCount());
        assertEquals("在项目任务页面发送任务给" + itemName + "后正在进行任务计数没有+1",
                this.getProcessingTaskCount() + 1, indexAfterChange.getProcessingTaskCount());
        assertEquals("在项目任务页面发送任务给" + itemName + "后header计数没有+1",
                this.getHeaderTaskCount() + 1, indexAfterChange.getHeaderTaskCount());
        assertEquals("在项目任务页面发送任务给" + itemName + "后成员的正在进行任务数没有+1",
                this.getUserProcessingTaskCount(itemName) + 1,
                ((CountIndexInProjectPage) indexAfterChange).getUserProcessingTaskCount(itemName));
        assertEquals("在项目任务页面发送任务给" + itemName + "后成员的全部任务数没有+1",
                this.getUserAllTaskCount(itemName) + 1, ((CountIndexInProjectPage) indexAfterChange).getUserAllTaskCount(itemName));
    }


    public void removeProcessingTaskIndexTest(String itemName, TaskCountIndex indexAfterChange) {
        assertEquals("在项目任务页面将" + itemName + "的任务删除后" + itemName + "的正在进行的任务计数没有减1",
                this.getUserProcessingTaskCount(itemName) - 1, ((CountIndexInProjectPage) indexAfterChange).getUserProcessingTaskCount(itemName));
        assertEquals("在项目任务页面将" + itemName + "的任务删除后" + itemName + "的所有任务计数没有减1",
                this.getUserAllTaskCount(itemName) - 1, ((CountIndexInProjectPage) indexAfterChange).getUserAllTaskCount(itemName));

        assertEquals("在项目任务页面将" + itemName + "的任务删除后页面上的所有任务计数没有减1",
                this.getAllTaskCount() - 1, indexAfterChange.getAllTaskCount());
        assertEquals("在项目任务页面将" + itemName + "的任务删除后页面上的正在进行任务计数没有减1",
                this.getProcessingTaskCount() - 1,  indexAfterChange.getProcessingTaskCount());
    }


    public void resignTaskIndexTest(String oldExecutiveName, String newExecutiveName, TaskCountIndex indexAfterChange) {
        assertEquals("在项目任务页面将" + oldExecutiveName + "指派给" + newExecutiveName + "，" + newExecutiveName + "的正在进行的任务计数没有加1",
                this.getUserProcessingTaskCount(newExecutiveName) + 1, ((CountIndexInProjectPage) indexAfterChange).getUserProcessingTaskCount(newExecutiveName));
        assertEquals("在项目任务页面将"+oldExecutiveName+"指派给"+newExecutiveName+"，"+newExecutiveName+"的所有任务计数没有加1",
                this.getUserAllTaskCount(newExecutiveName) + 1, ((CountIndexInProjectPage) indexAfterChange).getUserAllTaskCount(newExecutiveName));
        assertEquals("在项目任务页面将"+oldExecutiveName+"指派给"+newExecutiveName+"，"+oldExecutiveName+"的所有任务计数没有减1",
                this.getUserAllTaskCount(oldExecutiveName) - 1, ((CountIndexInProjectPage) indexAfterChange).getUserAllTaskCount(oldExecutiveName));
        assertEquals("在项目任务页面将"+oldExecutiveName+"指派给"+newExecutiveName+"，"+oldExecutiveName+"的正在进行的任务计数没有减1",
                this.getUserProcessingTaskCount(oldExecutiveName) -1, ((CountIndexInProjectPage) indexAfterChange).getUserProcessingTaskCount(oldExecutiveName));
    }
}

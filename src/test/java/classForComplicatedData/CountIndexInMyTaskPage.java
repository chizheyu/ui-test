package classForComplicatedData;

import org.ChiTest.AdvancedTask.AdvancedTaskFileReader;
import org.ChiTest.Page.Page;

import static org.junit.Assert.assertEquals;

/**
 * Created by dugancaii on 1/3/2015.
 */
public class CountIndexInMyTaskPage implements TaskCountIndex {
    private AdvancedTaskFileReader advancedTaskFileReader;
    private int allTaskCount;
    private int processingTaskCount;
    private int finishedTaskCount;
    private int wathchedTaskCount;
    private int projectProcessingTaskCount;
    private Page page;
    private int projectAllTaskCount;
    private String projectName;

    public CountIndexInMyTaskPage(Page page, String projectName)  {
        this.projectName  = projectName;
        this.page = page;
        advancedTaskFileReader = new AdvancedTaskFileReader();
        updateTaskIndex(page);
    }


    @Override
    public void finishTaskIndexTest(String itemName, TaskCountIndex indexAfterChange) {
        assertEquals("在我的任务页面完成"+itemName+"的任务后，正在进行任务计数没有减1",
                this.getProcessingTaskCount() - 1, indexAfterChange.getProcessingTaskCount());

        assertEquals("在我的任务页面完成"+itemName+"的任务后，"+ itemName+"正在进行任务计数没有减1",
                this.projectProcessingTaskCount - 1,
                ( (CountIndexInMyTaskPage) indexAfterChange).projectProcessingTaskCount);
        assertEquals("在我的任务页面完成" + itemName + "的任务后，已完成的任务数没有加1",
                this.getFinishedTaskCount() + 1, indexAfterChange.getFinishedTaskCount());
    }

    @Override
    public void reopenTaskIndexTest(String itemName, TaskCountIndex indexAfterChange) {
        assertEquals("在我的任务页面重新开启"+itemName+"的任务后，正在进行任务计数没有加1",
                this.getProcessingTaskCount() , indexAfterChange.getProcessingTaskCount());

        assertEquals("在我的任务页面重新开启"+itemName+"的任务后，"+ itemName+"正在进行任务计数没有加1",
                this.projectProcessingTaskCount,
                ( (CountIndexInMyTaskPage) indexAfterChange).projectProcessingTaskCount);
        assertEquals("在我的任务页面重新开启"+itemName+"的任务后，已完成的任务数没有减1",
                this.getFinishedTaskCount()  , indexAfterChange.getFinishedTaskCount());
    }

    @Override
    public void createTaskIndexTest(String itemName, TaskCountIndex indexAfterChange) {
        assertEquals("在我的任务页面发送任务给" + itemName + "后所有任务计数没有+1",
                this.getAllTaskCount() + 1, indexAfterChange.getAllTaskCount());
        assertEquals("在我的任务页面发送任务给" + itemName + "后正在进行任务计数没有+1",
                this.getProcessingTaskCount() + 1, indexAfterChange.getProcessingTaskCount());

        assertEquals("在我的任务页面发送任务给" + itemName + "后项目的正在进行任务数没有+1",
                this.projectProcessingTaskCount + 1,
                ((CountIndexInMyTaskPage) indexAfterChange).projectProcessingTaskCount);
        assertEquals("在我的任务页面发送任务给" + itemName + "后成员的全部任务数没有+1",
                this.projectAllTaskCount + 1, ((CountIndexInMyTaskPage) indexAfterChange).projectAllTaskCount);
    }

    @Override
    public void removeProcessingTaskIndexTest(String itemName, TaskCountIndex indexAfterChange) {
        assertEquals("在我的任务页面将" + itemName + "的任务删除后" + itemName + "的正在进行的任务计数没有减1",
                this.projectProcessingTaskCount - 1, ((CountIndexInMyTaskPage) indexAfterChange).projectProcessingTaskCount);
        assertEquals("在我的任务页面将" + itemName + "的任务删除后" + itemName + "的所有任务计数没有减1",
                this.projectAllTaskCount - 1, ((CountIndexInMyTaskPage) indexAfterChange).projectAllTaskCount);

        assertEquals("在我的任务页面将" + itemName + "的任务删除后页面上的所有任务计数没有减1",
                this.getAllTaskCount() - 1, indexAfterChange.getAllTaskCount());
        assertEquals("在我的任务页面将" + itemName + "的任务删除后页面上的正在进行任务计数没有减1",
                this.getProcessingTaskCount() - 1,  indexAfterChange.getProcessingTaskCount());
    }
    public void resignTaskIndexTest(String oldExecutiveName, String newExecutiveName, CountIndexInMyTaskPage indexAfterChange) {
        assertEquals("在我的任务页面将" + oldExecutiveName + "指派给" + newExecutiveName + ", 项目的正在进行的任务计数没有减1",
                this.projectProcessingTaskCount - 1, indexAfterChange.projectProcessingTaskCount );
        assertEquals("在我的任务页面将"+oldExecutiveName+"指派给"+newExecutiveName+", 项目的正在进行的任务计数没有减1",
                this.projectAllTaskCount - 1, indexAfterChange.projectAllTaskCount);
       
    }

    @Override
    public void updateTaskIndex(Page page) {

        allTaskCount =  Integer.parseInt(page.getText(advancedTaskFileReader.getTaskInnerMenuIndex()));
        processingTaskCount =  Integer.parseInt(page.getText(advancedTaskFileReader.getTaskInnerMenuIndex(),1));
        finishedTaskCount =  Integer.parseInt(page.getText(advancedTaskFileReader.getTaskInnerMenuIndex(),2));
        wathchedTaskCount =  Integer.parseInt(page.getText(advancedTaskFileReader.getTaskInnerMenuIndex(),3));

        projectAllTaskCount = Integer.parseInt(page.getText(advancedTaskFileReader.getTaskProjectIndexRate(),
                page.getIndexPosition( projectName,advancedTaskFileReader.getTaskProjectIndexItem())).split("/")[1]);
        projectProcessingTaskCount = Integer.parseInt(page.getText(advancedTaskFileReader.getTaskProjectIndexRate(),
                page.getIndexPosition( projectName,advancedTaskFileReader.getTaskProjectIndexItem())).split("/")[0]);
    }



    @Override
    public int getAllTaskCount() {
        return allTaskCount;
    }

    @Override
    public int getProcessingTaskCount() {
        return processingTaskCount;
    }

    @Override
    public int getHeaderTaskCount() {
        return 0;
    }


    @Override
    public int getFinishedTaskCount() {
        return finishedTaskCount;
    }

    @Override
    public int getWathchedTaskCount() {
        return wathchedTaskCount;
    }
}

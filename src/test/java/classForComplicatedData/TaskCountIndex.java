package classForComplicatedData;

import org.ChiTest.Page.Page;

/**
 * Created by dugancaii on 12/28/2014.
 */
public interface TaskCountIndex {
    public abstract void  finishTaskIndexTest(String itemName, TaskCountIndex indexAfterChange);
    public abstract void  reopenTaskIndexTest(String itemName, TaskCountIndex indexAfterChange);
    public abstract void  createTaskIndexTest(String itemName, TaskCountIndex indexAfterChange);
    public abstract void  removeProcessingTaskIndexTest(String itemName, TaskCountIndex indexAfterChange);
    public abstract void  updateTaskIndex(Page page);
    public abstract int  getAllTaskCount();
    public abstract int  getProcessingTaskCount();
    public abstract int  getHeaderTaskCount();
    public abstract int  getFinishedTaskCount();
    public abstract int  getWathchedTaskCount();


}

package org.ChiTest.WebComponent.SearchBox;

import org.ChiTest.Page.Page;
import org.ChiTest.Project.Project;
import org.ChiTest.Reference.AdvancedTaskReference;
import org.ChiTest.Reference.ReferenceFileReader;

import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * Created by dugancaii on 3/14/2015.
 */
public class ReferenceSearchBox extends SearchBox {
    private ReferenceFileReader referenceFileReader;
    public ReferenceSearchBox(String boxSelector, String searchResultSelector, String openBoxButtonSelector) {
        super(boxSelector,searchResultSelector, openBoxButtonSelector);
        referenceFileReader = new ReferenceFileReader();
    }

    public void assertSearchContent(Page cp, AdvancedTaskReference reference){
        searchItem(cp, reference.getReferenceContent() );
        cp.assertTextEquals("输入引用内容，搜索框的搜索的内容不对", "#" + reference.getReferenceId() + " " + reference.getReferenceContent(),
                referenceFileReader.getTaskReferenceItemInSearchBox());
    }
    public void assertSearchId(Page cp, AdvancedTaskReference reference){
        searchItem(cp, reference.getReferenceId() + "");
        cp.assertTextEquals("输入引用 id，点击搜索框后，搜索框的搜索的内容不对", "#" + reference.getReferenceId() + " " + reference.getReferenceContent(),
                referenceFileReader.getTaskReferenceItemInSearchBox());
    }
    public void assertProjectTitleInSearchBox(Page cp, Project zombieProject){
        cp.assertTextEquals("查看更多引用结果的项目标题出错", zombieProject.getProjectFullName(),
                referenceFileReader.getTaskReferenceProjectTitle());
    }
    public void assertTabChangeAndNoResultInSearchBox(Page cp){
        cp.assertTextEquals("查看更多引用结果的 task tab 上的文字出错", "任务", referenceFileReader.getTaskReferenceTabInSearchBox()+".active");
        cp.assertElementPresent("查看更多引用结果的 task tab 上的图标出错", referenceFileReader.getTaskReferenceTabInSearchBox() + ".active" + " .tasks.icon");
        cp.clickElement(referenceFileReader.getTaskReferenceTabInSearchBox(), 3);
        cp.waitLoadingIconInvisible();

        cp.assertTextEquals("查看更多引用结果的 merge request tab 上的文字出错", "Merge Request", referenceFileReader.getTaskReferenceTabInSearchBox()+".active");
        cp.assertElementPresent("查看更多引用结果的 merge request tab 上的图标出错",
                referenceFileReader.getTaskReferenceTabInSearchBox()+".active " + referenceFileReader.getMergeRequestIcon());
        cp.assertElementPresent("在没有搜索结果的时候，引用搜索框内没有出现咖啡标志", referenceFileReader.getTaskEmptyListInSearchBox() + " .coffee.icon");
        cp.assertTextEquals("在没有搜索结果的时候，引用搜索框内没有出现提示","没有找到匹配数据...", referenceFileReader.getTaskEmptyListInSearchBox());
    }
    public void assertResultPagingInSearchBox(Page cp){
        cp.clickElement(referenceFileReader.getTaskReferenceTabInSearchBox(), referenceFileReader.getTaskReferencePagingInSearchBox("1"));
        cp.clickElement(referenceFileReader.getTaskReferencePagingInSearchBox(">"));
        cp.waitLoadingIconInvisible();
        assertTrue("引用搜索框的的分页点击下一页失效", 8 > cp.getElementCount(referenceFileReader.getTaskReferenceItemInSearchBox()));
    }
    public void assertSearchDeletedIdInSearchBox(Page cp, AdvancedTaskReference reference){
        cp.clearAndSendKeys(boxInputSelector, reference.getReferenceId()+"");
        cp.waitLoadingIconInvisible();
        cp.assertElementPresent("输入已删除的引用id，可以找到原来的引用", referenceFileReader.getTaskEmptyListInSearchBox());
    }
    public void assertSearchWrongIdInSearchBox(Page cp){
        cp.clearAndSendKeys(boxInputSelector,"#"+ ((new Date()).getTime()/10000));
        cp.waitLoadingIconInvisible();
        cp.assertElementPresent("输入无效的引用id，却有结果", referenceFileReader.getTaskEmptyListInSearchBox());
    }





}

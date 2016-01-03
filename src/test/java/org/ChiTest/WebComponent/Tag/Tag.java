package org.ChiTest.WebComponent.Tag;

import org.ChiTest.Page.Page;
import org.ChiTest.Project.Project;
import org.ChiTest.Topic.Topic;
import org.ChiTest.User.User;
import org.openqa.selenium.Keys;

import java.util.Set;

/**
 * Created by dugancaii on 3/27/2015.
 */
public class Tag {
    public String getTagName() {
        return tagName;
    }

    public String getTagColorRBG() {
        return tagColorRBG;
    }

    protected String tagName;

    public void setTagColorRBG(String tagColorRBG) {
        this.tagColorRBG = tagColorRBG;
    }

    protected String tagColorRBG;

    public String getHexadecimalValue() {
        return hexadecimalValue;
    }

    protected String hexadecimalValue;
    protected Set<Topic> TopicList;
    protected User owner;
    protected Project project;
    protected TagFileReader tagFileReader;
    public Tag(String tagName, String tagColorRBG,String hexadecimalValue, User owner, Project project){
        this.tagName = tagName;
        this.tagColorRBG = tagColorRBG;
        this.owner = owner;
        this.project = project;
        this.hexadecimalValue = hexadecimalValue;
        this.tagFileReader = new TagFileReader();
    }
    public Tag(String tagName, String tagColorRBG, User owner, Project project){
        this(tagName, tagColorRBG, "", owner, project);
    }

    public void addTagInProject(Page page){
        if(page.findItemInOnePageByTextContains(tagFileReader.getTagItemInTagList(), tagName) == -1){
            openTagEditInterFace(page);
            page.clickElement(tagFileReader.getNewTagColorEditButton(), tagFileReader.getNewTagTextInput());
            page.clearAndSendKeys(tagFileReader.getNewTagColorInput(), hexadecimalValue);
            page.clearAndSendKeys(tagFileReader.getNewTagTextInput(), tagName);
            page.sendKeys(Keys.ENTER);
            page.clickElement(tagFileReader.getTagEditConfirmIcon());
        }
    }
    public void removeTagInProject(Project project ) throws InterruptedException {
        Page page = project.getCreator().getPage();
        page.navigate(project.getProjectTopicHomeLink(), tagFileReader.getTagSettingIcon());
        int tagNum  = page.findItemInOnePageByTextEquals(tagFileReader.getTagItemInTagList(), tagName);
        openTagEditInterFace(page);
        page.checkAlert(tagFileReader.getTagRemoveIcon(),tagNum,
                "在标签编辑页删除某个标签，提示有误", "确认删除标签 " + tagName + " ?");
        page.clickElement(tagFileReader.getTagEditConfirmIcon());
    }

    public void removeTagInItemDetail(Page page){
        removeTagInItemDetail(page, page.findItemInOnePageByTextEquals(tagFileReader.getTagInItemDetail(), tagName));
    }
    public void removeTagInItemDetail(Page page, int tagNumInDetail){
        int tagCount = page.getElementCount(tagFileReader.getTagInItemDetail());
        page.moveToElement(tagFileReader.getTagInItemDetail(), tagNumInDetail);
        page.clickElement(tagFileReader.getTagRemoveIconWhenEditItem(), tagNumInDetail);
        page.waitForItemCountChange(tagFileReader.getTagInItemDetail(), tagCount,10);
        page.assertCountEquals("编辑讨论时删除项目标签", tagCount - 1, tagFileReader.getTagInItemDetail());
    }
    public void checkTopicTag(Page page, String tagItemSelector, int tagNum){
        page.assertTextEquals(tagName + "的名字有误", tagName, tagItemSelector,tagNum);
        page.assertAttributeContainWords(tagName + "的颜色有误", tagColorRBG, tagItemSelector + " .tag.icon", tagNum, "style");
    }

    public void addTagInItem(Page page){
        int tagCount = page.getElementCount(tagFileReader.getTagInItemDetail());
        if(page.findItemInOnePageByTextContains(tagFileReader.getTagInItemDetail(), tagName) == -1) {
            page.clickElement(tagFileReader.getTagAddButtonInItem(), tagFileReader.getTagNameInAddTagList());
            page.clickElement(tagFileReader.getTagNameInAddTagList(),
                    page.findItemInOnePageByTextEquals(tagFileReader.getTagNameInAddTagList(), tagName));
            page.waitForItemCountChange(tagFileReader.getTagInItemDetail(), tagCount, 5);
            page.assertCountEquals("编辑讨论时添加项目标签", tagCount + 1, tagFileReader.getTagInItemDetail());
            page.clickElement(tagFileReader.getTagAddButtonInItem());
        }
    }
    public void editTag(Page page, String newTagText,String newRgbValue, String newRgbValueInBracket){
        int editTagNum = page.findItemInOnePageByTextContains(tagFileReader.getTagItemInTagList(),tagName);
        openTagEditInterFace(page);
        page.clickElement(tagFileReader.getTagColorEditButton(editTagNum), tagFileReader.getTagColorInput(editTagNum));
        page.clearAndSendKeys(tagFileReader.getTagColorInput(editTagNum), newRgbValueInBracket);
        page.clickElement(tagFileReader.getTagColorEditButton(editTagNum));
        page.clearAndSendKeys(tagFileReader.getTagTextInput(), editTagNum, newTagText);
        page.sendKeys(Keys.ENTER);
        page.waitLoadingIconInvisible();
        page.clickElement(tagFileReader.getTagEditConfirmIcon());
        page.assertTextEquals("修改自己的标签后，标签列表中，标签", newTagText, tagFileReader.getTagItemInTagList(), editTagNum);
        page.assertAttributeContainWords("修改自己的讨论标签后",newRgbValue, tagFileReader.getTagItemInTagList() + " .tag.icon", editTagNum, "style");
        tagName = newTagText;
        tagColorRBG = newRgbValue;
    }
    public void verifyTagFilterInTopicTitle(Page page, Topic topic, int topicCount,String itemHomeHeaderSelector){
        verifyTagFilter( page, topic.getTopicTitle(), tagFileReader.getTagInItemTitle(),  topicCount,itemHomeHeaderSelector);
    }
    public void verifyTagFilterInTagList(Page page, Topic topic, int topicCount,String itemHomeHeaderSelector){
        verifyTagFilter( page,  topic.getTopicTitle(), tagFileReader.getTagItemInTagList(), topicCount, itemHomeHeaderSelector);
    }
    protected void verifyTagFilter(Page page, String topicTitle, String tagSelector, int topicCount, String itemHomeHeaderSelector){
        String topicHeader = page.getText(itemHomeHeaderSelector );
        page.clickElement(tagSelector, page.findItemInOnePageByTextContains(tagSelector, tagName));
        page.waitForContentChange(itemHomeHeaderSelector, topicHeader, 10);
        page.assertTextEquals(tagName + " 标签的 header ", "标签：" + tagName, itemHomeHeaderSelector);
        page.assertTextEquals("标题上的标签", tagName, tagFileReader.getTagInItemTitle(),page.findItemInOnePageByTextEquals(tagFileReader.getTagInItemTitle(), tagName));
        assertVerifiedItem( page,topicCount , topicTitle);
    }
    protected void assertVerifiedItem(Page page,int topicCount ,String itemTitle){
    }


    public void openTagEditInterFace(Page page){
        if(page.elementIsPresent(tagFileReader.getTagSettingIcon(), 3)){
            page.clickElement(tagFileReader.getTagSettingIcon(), tagFileReader.getTagTextInput());
        }
    }
}

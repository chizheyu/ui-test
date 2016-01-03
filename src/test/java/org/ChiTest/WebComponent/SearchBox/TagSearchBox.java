package org.ChiTest.WebComponent.SearchBox;

import org.ChiTest.Page.Page;
import org.ChiTest.WebComponent.Tag.Tag;
import org.ChiTest.WebComponent.Tag.TagFileReader;

import static org.junit.Assert.assertNotEquals;

/**
 * Created by dugancaii on 4/14/2015.
 */
public class TagSearchBox extends SearchBox {
    private TagFileReader tagFileReader;
    public TagSearchBox() {
        super();
        tagFileReader = new TagFileReader();
        boxSelector =tagFileReader.getTagSearchBox();
        boxInputSelector = boxSelector + " input";
        searchResultSelector = tagFileReader.getTagItemInAddTagList();
        openBoxButtonSelector = tagFileReader.getTagAddButtonInItem();
    }
    public void searchAddTag(Page page, Tag newTag){
        searchItem(page, newTag.getTagName());
        int tagCount = page.getElementCount(tagFileReader.getTagInItemDetail());
        page.assertTextEquals("新添加的标签(或查询功能失败)", newTag.getTagName(), tagFileReader.getTagNameInAddTagList());
        page.assertAttributeContainWords("新添加的标签(或查询功能失败)", newTag.getTagColorRBG(), tagFileReader.getTagColorInAddTagList(), "style");

        page.clickElement(tagFileReader.getTagNameInAddTagList());
        page.waitForItemCountChange(tagFileReader.getTagInItemDetail(), tagCount, 5);
        assertNotEquals("新添加的标签(讨论信息那里)", -1, page.findItemInOnePageByTextEquals(tagFileReader.getTagInItemDetail(), newTag.getTagName()));

        page.clearInput(boxInputSelector);
    }

}

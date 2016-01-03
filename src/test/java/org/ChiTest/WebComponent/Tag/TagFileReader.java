package org.ChiTest.WebComponent.Tag;

import reference.ConfigFileReader;

/**
 * Created by dugancaii on 4/17/2015.
 */
public class TagFileReader  {
    private ConfigFileReader fileReader;
    public String getTagItemInTagList() {
        return tagItemInTagList;
    }

    public String getTagRemoveIcon() {
        return tagRemoveIcon;
    }

    private String tagRemoveIcon;

    private String tagItemInTagList;
    public String getNewTagColorEditButton() {
        return tagColorEditButton.replace(".tag.ng-scope", ".new-tag");
    }
    public String getTagColorEditButton(int tagNum) {
        return tagColorEditButton.replace(".tag.ng-scope", ".tag.ng-scope:nth-child("+ (tagNum + 1)+")");
    }
    public String getTagColorEditButton() {
        return tagColorEditButton;
    }
    public String getNewTagColorInput() {
        return tagColorInput.replace(".tag.ng-scope", ".new-tag");
    }
    public String getTagSettingIcon() {
        return tagSettingIcon;
    }

    private String tagSettingIcon;

    public String getTagEditConfirmIcon() {
        return tagEditConfirmIcon;
    }

    private String tagEditConfirmIcon;

    public String getTagTextInput() {
        return tagTextInput;
    }
    public String getNewTagTextInput() {
        return tagTextInput.replace(".tag.ng-scope", ".new-tag");
    }

    private String tagTextInput;

    public String getTagColorInput(int tagNum) {
        return tagColorInput.replace(".tag.ng-scope", ".tag.ng-scope:nth-child("+ (tagNum + 1)+")");
    }



    public String getTagInItemDetail() {
        return tagInItemDetail;
    }

    private String tagInItemDetail;

    public String getTagRemoveIconWhenEditItem() {
        return tagRemoveIconWhenEditItem;
    }

    private String tagRemoveIconWhenEditItem;

    public String getTagInItemTitle() {
        return tagInItemTitle;
    }

    private String tagInItemTitle;

    public String getTagAddButtonInItem() {
        return tagAddButtonInItemDetail;
    }

    private String tagAddButtonInItemDetail;

    public String getTagNameInAddTagList() {
        return tagNameInAddTagList;
    }

    private String tagNameInAddTagList;

    private String tagColorInput;

    private String tagColorEditButton;

    public String getTagClassifyHeader() {
        return tagClassifyHeader;
    }

    private String tagClassifyHeader;

    public String getTagColorInAddTagList() {
        return tagColorInAddTagList;
    }

    private String tagColorInAddTagList;

    public String getTagSearchBox() {
        return tagSearchBox;
    }
    public String getTagItemInAddTagList() {
        return tagItemInAddTagList;
    }
    private String tagItemInAddTagList;

    public String getTagIconInTagList() {
        return tagIconInTagList;
    }

    private String tagIconInTagList;

    private String tagSearchBox;
    public TagFileReader() {
        fileReader = new ConfigFileReader("/TagConfig.properties");
        tagInItemTitle = fileReader.getValue("tagInItemTitle");
        tagRemoveIconWhenEditItem = fileReader.getValue("tagRemoveIconWhenEditItem");
        tagInItemDetail = fileReader.getValue("tagInItemDetail");
        tagNameInAddTagList = fileReader.getValue("tagNameInAddTagList");
        tagAddButtonInItemDetail = fileReader.getValue("tagAddButtonInItemDetail");
        tagColorInput = fileReader.getValue("tagColorInput");
        tagColorEditButton = fileReader.getValue("tagColorEditButton");
        tagTextInput = fileReader.getValue("tagTextInput");
        tagSettingIcon = fileReader.getValue("tagSettingIcon");
        tagEditConfirmIcon = fileReader.getValue("tagEditConfirmIcon");
        tagItemInTagList = fileReader.getValue("tagItemInTagList");
        tagRemoveIcon = fileReader.getValue("tagRemoveIcon");
        tagClassifyHeader = fileReader.getValue("tagClassifyHeader");
        tagColorInAddTagList = fileReader.getValue("tagColorInAddTagList");
        tagSearchBox = fileReader.getValue("tagSearchBox");
        tagItemInAddTagList = fileReader.getValue("tagItemInAddTagList");
        tagIconInTagList = fileReader.getValue("tagIconInTagList");
    }
}

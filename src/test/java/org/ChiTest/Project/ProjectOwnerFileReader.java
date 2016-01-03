package org.ChiTest.Project;

import reference.ConfigFileReader;

/**
 * Created by dugancaii on 1/12/2015.
 */
public class ProjectOwnerFileReader {
    private ConfigFileReader projectOwnerFileReader ;

    public String getProjectTransferButton() {
        return projectTransferButton;
    }

    private String projectTransferButton ;

    public String getProjectTransferHeader() {
        return projectTransferHeader;
    }

    private String projectTransferHeader ;

    public String getProjectTransferNotice() {
        return projectTransferNotice;
    }

    private String projectTransferNotice ;

    public String getProjectTransferHint() {
        return projectTransferHint;
    }

    private String projectTransferHint ;

    public String getProjectTransferSelectBox() {
        return projectTransferSelectBox;
    }

    private String projectTransferSelectBox ;

    public String getProjectTransferSelectItem() {
        return projectTransferSelectItem;
    }

    private String projectTransferSelectItem ;

    public String getProjectTransferCurSelectItem() {
        return projectTransferCurSelectItem;
    }

    private String projectTransferCurSelectItem ;

    public String getProjectTransferPasswordInput() {
        return projectTransferPasswordInput;
    }

    private String projectTransferPasswordInput ;

    public String getProjectTransferPasswordConfirm() {
        return projectTransferPasswordConfirm;
    }

    private String projectTransferPasswordConfirm ;

    public ProjectOwnerFileReader() {
        projectOwnerFileReader = new ConfigFileReader("/ProjectOwnerChangeConfig.properties");
        projectTransferHeader = projectOwnerFileReader.getValue("projectTransferHeader");
        projectTransferNotice = projectOwnerFileReader.getValue("projectTransferNotice");
        projectTransferHint = projectOwnerFileReader.getValue("projectTransferHint");
        projectTransferSelectBox = projectOwnerFileReader.getValue("projectTransferSelectBox");
        projectTransferSelectItem = projectOwnerFileReader.getValue("projectTransferSelectItem");
        projectTransferCurSelectItem = projectOwnerFileReader.getValue("projectTransferCurSelectItem");
        projectTransferButton = projectOwnerFileReader.getValue("projectTransferButton");
        projectTransferPasswordInput = projectOwnerFileReader.getValue("projectTransferPasswordInput");
        projectTransferPasswordConfirm = projectOwnerFileReader.getValue("projectTransferPasswordConfirm");
    }
}

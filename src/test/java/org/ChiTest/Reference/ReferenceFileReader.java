package org.ChiTest.Reference;

import reference.ConfigFileReader;

/**
 * Created by dugancaii on 5/11/2015.
 */
public class ReferenceFileReader {
    private ConfigFileReader fileReader;
    public String getTaskReferenceIdInTaskList() {
        return taskReferenceIdInTaskList;
    }

    private String taskReferenceIdInTaskList;

    public String getTaskReferenceIcon() {
        return taskReferenceIcon;
    }

    private String taskReferenceIcon;

    public String getTaskReferenceHintItem() {
        return taskReferenceHintItem;
    }

    private String taskReferenceHintItem;

    public String getTaskReferenceIdInDetailPage() {
        return taskReferenceIdInDetailPage;
    }

    private String taskReferenceIdInDetailPage;

    public String getTaskEmptyListInSearchBox() {
        return taskEmptyListInSearchBox;
    }

    public String getTaskReferenceItemInDetailPageComment() {
        return taskReferenceItemInDetailPageComment;
    }

    public String getTaskReferencePagingInSearchBox() {
        return taskReferencePagingInSearchBox;
    }

    private String taskEmptyListInSearchBox;
    private String taskSelectedReferenceHintItem;
    private String taskReferenceItemInDetailPageComment;
    private String taskReferencePagingInSearchBox;
    public String getTaskReferencePagingInSearchBox(String pageMark) {
        return taskReferencePagingInSearchBox.replace("{0}",pageMark);
    }
    public String getTaskReferenceItemInSearchBox() {
        return taskReferenceItemInSearchBox;
    }

    private String taskReferenceItemInSearchBox;

    public String getTaskReferenceTabInSearchBox() {
        return taskReferenceTabInSearchBox;
    }

    private String taskReferenceTabInSearchBox;

    public String getTaskShowMoreReferenceResult() {
        return taskShowMoreReferenceResult;
    }

    private String taskShowMoreReferenceResult;

    public String getTaskReferenceProjectTitle() {
        return taskReferenceProjectTitle;
    }

    private String taskReferenceProjectTitle;

    public String getTaskSelectedReferenceHintItem() {
        return taskSelectedReferenceHintItem;
    }


    public String getTaskReferenceSearchButton() {
        return taskReferenceSearchButton;
    }

    private String taskReferenceSearchButton;

    public String getTaskReferenceSearchBox() {
        return taskReferenceSearchBox;
    }

    private String taskReferenceSearchBox;

    public String getMergeRequestIcon() {
        return mergeRequestIcon;
    }

    private String mergeRequestIcon;


    public  ReferenceFileReader() {
        fileReader = new ConfigFileReader("/referenceConfig.properties");
        taskReferenceIcon = fileReader.getValue("taskReferenceIcon");
        taskReferenceHintItem = fileReader.getValue("taskReferenceHintItem");
        taskSelectedReferenceHintItem = fileReader.getValue("taskSelectedReferenceHintItem");
        taskShowMoreReferenceResult = fileReader.getValue("taskShowMoreReferenceResult");
        taskReferenceProjectTitle = fileReader.getValue("taskReferenceProjectTitle");
        taskReferenceSearchButton = fileReader.getValue("taskReferenceSearchButton");
        taskReferenceSearchBox = fileReader.getValue("taskReferenceSearchBox");
        taskReferenceTabInSearchBox = fileReader.getValue("taskReferenceTabInSearchBox");
        mergeRequestIcon = fileReader.getValue("mergeRequestIcon");
        taskReferenceItemInSearchBox = fileReader.getValue("taskReferenceItemInSearchBox");
        taskReferencePagingInSearchBox = fileReader.getValue("taskReferencePagingInSearchBox");
        taskEmptyListInSearchBox = fileReader.getValue("taskEmptyListInSearchBox");
        taskReferenceIdInDetailPage = fileReader.getValue("taskReferenceIdInDetailPage");
        taskReferenceItemInDetailPageComment = fileReader.getValue("taskReferenceItemInDetailPageComment");
        taskReferenceIdInTaskList = fileReader.getValue("taskReferenceIdInTaskList");

    }
}

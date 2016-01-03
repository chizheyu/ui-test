package org.ChiTest.Project;

import reference.ConfigFileReader;

/**
 * Created by dugancaii on 10/6/2014.
 */
public class ProjectFileReader {
    private ConfigFileReader projectFileReader ;
    private String privateProject;
    private String waitEelementInCreateProjectPage;
    private String createProjectsUrl;
    private String inputProjectName;
    private String createProjectButton;
    private String projectActivity;
    private String userCenterUrl;
    private String addSignInNavigationBar;
    private String createProjectInNavigationBar;
    private String createProjectsInUserCenter;
    private String addIconForCreateProjectInProjectPage;
    private String projectButtonInContextMenu;
    private String inputProjectDescription;
    private String publicProjectType;
    private String privateProjectType;
    private String hasReadMe;
    private String selectLicence;
    private String option;
    private String selectGitIgnore;

    public String getProjectInFirstGroup() {
        return projectInFirstGroup;
    }

    private String projectInFirstGroup;

    public String getExitProject() {
        return exitProject;
    }

    private String exitProject;


    private String ProjectItemForBlockList;

    private String lookAllUserProjectInUserCenter;
    private String publicProjectTitle;
    private String privateProjectTitle;
    private String createProjectInProjectList;
    private String waitEelementInProjectHome;

    public String getProjectHeaderForWait() {
        return projectHeaderForWait;
    }

    private String projectHeaderForWait;

    public String getUnreadActivityCountOnProjectIcon() {
        return unreadActivityCountOnProjectIcon;
    }

    private String unreadActivityCountOnProjectIcon;

    public String getUnreadActivityCountIndropDownMenu() {
        return unreadActivityCountIndropDownMenu;
    }

    private String unreadActivityCountIndropDownMenu;

    public String getSearchInputForAddMember() {
        return searchInputForAddMember;
    }

    private String searchInputForAddMember;

    public String getConfirmButtonForAddMember() {
        return confirmButtonForAddMember;
    }

    private String confirmButtonForAddMember;

    public String getSelectedMemberItemInAddMember() {
        return selectedMemberItemInAddMember;
    }

    public String getMemberCountText() {
        return memberCountText;
    }

    public String getProjectGroupCheckMarkIcon() {
        return projectGroupCheckMarkIcon;
    }

    private String projectGroupCheckMarkIcon;

    public String getProjectGroupItemsWrapper() {
        return projectGroupItemsWrapper;
    }

    private String projectGroupItemsWrapper;
    private String memberCountText;

    public String getAddIconForAddMember() {
        return addIconForAddMember;
    }

    private String addIconForAddMember;
    private String selectedMemberItemInAddMember;

    public String getSelectedMemberInMemberList() {
        return selectedMemberInMemberList;
    }

    private String selectedMemberInMemberList;

    public String getSearchMemberInput() {
        return searchMemberInput;
    }

    public String getMaxMemberHintForAddMember() {
        return maxMemberHintForAddMember;
    }

    private String maxMemberHintForAddMember;

    public String getSelectedMemberNameInAddMember() {
        return selectedMemberNameInAddMember;
    }

    private String selectedMemberNameInAddMember;

    public String getProjectNameForBlockList() {
        return projectNameForBlockList;
    }

    private String projectNameForBlockList;
    private String searchMemberInput;

    public String getProjectLinkForBlockListInAllProjectItem() {
        return ProjectLinkForBlockListInAllProjectItem;
    }

    public String getProjectGroupButton() {
        return projectGroupButton;
    }

    private String projectGroupButton;

    public String getProjectGroupConfirmButtonBeforeHaveGroup() {
        return projectGroupConfirmButtonBeforeHaveGroup;
    }

    private String projectGroupConfirmButtonBeforeHaveGroup;

    public String getProjectGroupCancelButton() {
        return projectGroupCancelButton;
    }

    public String getProjectGroupCountText() {
        return projectGroupCountText;
    }

    private String projectGroupCountText;
    private String projectGroupCancelButton;

    public String getProjectGroupTitleInput() {
        return projectGroupTitleInput;
    }

    private String projectGroupTitleInput;

    public String getProjectGroupTitle() {
        return projectGroupTitle;
    }

    private String projectNameInBlockList;
    private String projectGroupTitle;
    private String ProjectLinkForBlockListInAllProjectItem;

    public String getProjectNameInFirstGroup() {
        return projectNameInFirstGroup;
    }

    public String getProjectItemForBlockListInAllProjectPage() {
        return projectItemForBlockListInAllProjectPage;
    }

    private String projectItemForBlockListInAllProjectPage;
    private String projectNameInFirstGroup;

    public String getProjectGroupItem() {
        return projectGroupItem;
    }

    private String projectGroupItem;

    public String getMemberInMemberList() {
        return memberInMemberList;
    }

    private String memberInMemberList;


    public String getWaitEelementInProjectHome() {
        return waitEelementInProjectHome;
    }

    public ProjectFileReader(){
        projectFileReader = new ConfigFileReader("/projectConfig.properties");
        privateProject = projectFileReader.getValue("privateProject");
        waitEelementInCreateProjectPage =  projectFileReader.getValue("waitEelementInCreateProjectPage");
        createProjectsUrl =  projectFileReader.getValue("createProjectsUrl");
        inputProjectName =  projectFileReader.getValue("inputProjectName");
        createProjectButton =  projectFileReader.getValue("createProjectButton");
        projectActivity = projectFileReader.getValue("projectActivity");
        userCenterUrl= projectFileReader.getValue("userCenterUrl");
        addSignInNavigationBar= projectFileReader.getValue("addSignInNavigationBar");
        createProjectInNavigationBar= projectFileReader.getValue("createProjectInNavigationBar");
        createProjectsInUserCenter= projectFileReader.getValue("createProjectsInUserCenter");
        addIconForCreateProjectInProjectPage = projectFileReader.getValue("addIconForCreateProjectInProjectPage");
        projectButtonInContextMenu= projectFileReader.getValue("projectButtonInContextMenu");
        inputProjectDescription= projectFileReader.getValue("inputProjectDescription");
        publicProjectType= projectFileReader.getValue("publicProjectType");
        privateProjectType= projectFileReader.getValue("privateProjectType");
        hasReadMe= projectFileReader.getValue("hasReadMe");
        selectLicence= projectFileReader.getValue("selectLicence");
        option = projectFileReader.getValue("option");
        selectGitIgnore= projectFileReader.getValue("selectGitIgnore");
        publicProjectTitle= projectFileReader.getValue("publicProjectTitle");
        privateProjectTitle= projectFileReader.getValue("privateProjectTitle");
        createProjectInProjectList=projectFileReader.getValue("createProjectInProjectList");
        lookAllUserProjectInUserCenter=projectFileReader.getValue("lookAllUserProjectInUserCenter");
        projectNameInBlockList=projectFileReader.getValue("projectNameInBlockList");
        ProjectItemForBlockList=projectFileReader.getValue("ProjectItemForBlockList");
        waitEelementInProjectHome= projectFileReader.getValue("waitElementInProjectHome");
        exitProject= projectFileReader.getValue("exitProject");
        unreadActivityCountOnProjectIcon= projectFileReader.getValue("unreadActivityCountOnProjectIcon");
        unreadActivityCountIndropDownMenu= projectFileReader.getValue("unreadActivityCountInDropDownMenu");
        searchInputForAddMember = projectFileReader.getValue("searchInputForAddMember");
        selectedMemberItemInAddMember = projectFileReader.getValue("selectedMemberItemInAddMember");
        confirmButtonForAddMember= projectFileReader.getValue("confirmButtonForAddMember");
        memberCountText= projectFileReader.getValue("memberCountText");
        addIconForAddMember= projectFileReader.getValue("addIconForAddMember");
        selectedMemberInMemberList= projectFileReader.getValue("selectedMemberInMemberList");
        memberInMemberList= projectFileReader.getValue("memberInMemberList");
        searchMemberInput= projectFileReader.getValue("searchMemberInput");
        maxMemberHintForAddMember = projectFileReader.getValue("maxMemberHintForAddMember");
        selectedMemberNameInAddMember = projectFileReader.getValue("selectedMemberNameInAddMember");
        projectNameForBlockList = projectFileReader.getValue("projectNameForBlockList");
        ProjectLinkForBlockListInAllProjectItem = projectFileReader.getValue("ProjectLinkForBlockListInAllProjectItem");
        projectGroupButton = projectFileReader.getValue("projectGroupButton");
        projectGroupConfirmButtonBeforeHaveGroup = projectFileReader.getValue("projectGroupConfirmButtonBeforeHaveGroup");
        projectGroupCancelButton = projectFileReader.getValue("projectGroupCancelButton");
        projectGroupCountText = projectFileReader.getValue("projectGroupCountText");
        projectGroupCheckMarkIcon = projectFileReader.getValue("projectGroupCheckMarkIcon");
        projectGroupItemsWrapper = projectFileReader.getValue("projectGroupItemsWrapper");
        projectGroupTitle = projectFileReader.getValue("projectGroupTitle");
        projectGroupTitleInput = projectFileReader.getValue("projectGroupTitleInput");
        projectNameInFirstGroup = projectFileReader.getValue("projectNameInFirstGroup");
        projectItemForBlockListInAllProjectPage = projectFileReader.getValue("projectItemForBlockListInAllProjectPage");
        projectGroupItem = projectFileReader.getValue("projectGroupItem");
        projectHeaderForWait = projectFileReader.getValue("projectHeaderForWait");
        projectMoveOutOfGroup = projectFileReader.getValue("projectMoveOutOfGroup");
        projectItemInAllProjectPage = projectFileReader.getValue("projectItemInAllProjectPage");
        projectMoveToNewGroup = projectFileReader.getValue("projectMoveToNewGroup");
        projectMoveToOtherGroup = projectFileReader.getValue("projectMoveToOtherGroup");
        projectGroupConfirmButton = projectFileReader.getValue("projectGroupConfirmButton");
        projectLogoInFirstGroup = projectFileReader.getValue("projectLogoInFirstGroup");
        projectImageForBlockList = projectFileReader.getValue("projectImageForBlockList");
        projectIconInput = projectFileReader.getValue("projectIconInput");
        projectEeditedIconImage = projectFileReader.getValue("projectEeditedIconImage");
        dropIconInTopMenu = projectFileReader.getValue("dropIconInTopMenu");
        pinSettingButtonInEmptyList = projectFileReader.getValue("pinSettingButtonInEmptyList");
        pinSettingIcon = projectFileReader.getValue("pinSettingIcon");
        pinIconInProjectIcon = projectFileReader.getValue("pinIconInProjectIcon");
        activePinIconInProjectPage = projectFileReader.getValue("activePinIconInProjectPage");
        projectImageInPinList = projectFileReader.getValue("projectImageInPinList");
        privateProjectNameInPinList = projectFileReader.getValue("privateProjectNameInPinList");
        publicProjectNameInPinList = projectFileReader.getValue("publicProjectNameInPinList");
        unreadActivityCountInPinList = projectFileReader.getValue("unreadActivityCountInPinList");
        unreadActivityCountOnGroupItem = projectFileReader.getValue("unreadActivityCountOnGroupItem");
        unreadActivityCountForItemInGroup = projectFileReader.getValue("unreadActivityCountForItemInGroup");
        projectListImageInAddMemberFrame = projectFileReader.getValue("projectListImageInAddMemberFrame");
        followIconInAddMemberFrame = projectFileReader.getValue("followIconInAddMemberFrame");
        fansIconInAddMemberFrame = projectFileReader.getValue("fansIconInAddMemberFrame");
        cancelButtonForAddMember = projectFileReader.getValue("cancelButtonForAddMember");
        closeButtonForAddMemberFrame = projectFileReader.getValue("closeButtonForAddMemberFrame");
        sendHimMessageButton = projectFileReader.getValue("sendHimMessageButton");
        privateProjectTitleList = projectFileReader.getValue("privateProjectTitleList");
        signInCreateProjectPage = projectFileReader.getValue("signInCreateProjectPage");
        signInAllProjectsPage = projectFileReader.getValue("signInAllProjectsPage");
        privateProjectLinkInPinList = projectFileReader.getValue("privateProjectLinkInPinList");
        test5PrivateImageInPinList = projectFileReader.getValue("test5PrivateImageInPinList");
        projectInFirstGroup = projectFileReader.getValue("projectInFirstGroup");

    }

    public String getProjectMoveOutOfGroup() {
        return projectMoveOutOfGroup;
    }

    public String getProjectMoveToNewGroup() {
        return projectMoveToNewGroup;
    }

    public String getProjectMoveToOtherGroup() {
        return projectMoveToOtherGroup;
    }

    public String getProjectImageForBlockList() {
        return projectImageForBlockList;
    }

    public String getProjectIconInput() {
        return projectIconInput;
    }

    public String getPrivateProjectNameInPinList() {
        return privateProjectNameInPinList;
    }

    public String getPublicProjectNameInPinList() {
        return publicProjectNameInPinList;
    }


    public String getSendHimMessageButton() {
        return sendHimMessageButton;
    }

    public String getPrivateProjectTitleList() {
        return privateProjectTitleList;
    }

    public String getSignInCreateProjectPage() {
        return signInCreateProjectPage;
    }

    public String getSignInAllProjectsPage() {
        return signInAllProjectsPage;
    }


    private String signInAllProjectsPage;
    private String signInCreateProjectPage;
    private String privateProjectTitleList;
    private String sendHimMessageButton;
    private String publicProjectNameInPinList;
    private String privateProjectNameInPinList;
    private String projectIconInput;
    private String projectImageForBlockList;
    private String projectMoveToOtherGroup;
    private String projectMoveToNewGroup;
    private String projectMoveOutOfGroup;

    public String getTest5PrivateImageInPinList() {
        return test5PrivateImageInPinList;
    }

    private String test5PrivateImageInPinList;

    public String getPrivateProjectLinkInPinList() {
        return privateProjectLinkInPinList;
    }

    private String privateProjectLinkInPinList;



    public String getCloseButtonForAddMemberFrame() {
        return closeButtonForAddMemberFrame;
    }

    private String closeButtonForAddMemberFrame;

    public String getCancelButtonForAddMember() {
        return cancelButtonForAddMember;
    }

    private String cancelButtonForAddMember;

    public String getProjectMemberWaitForSelect(String memberName) {
        return ".select-users li[title='" + memberName + "']";
    }


    public String getFansIconInAddMemberFrame() {
        return fansIconInAddMemberFrame;
    }

    private String fansIconInAddMemberFrame;

    public String getFollowIconInAddMemberFrame() {
        return followIconInAddMemberFrame;
    }

    private String followIconInAddMemberFrame;

    public String getProjectListImageInAddMemberFrame() {
        return projectListImageInAddMemberFrame;
    }

    private String projectListImageInAddMemberFrame;

    public String getUnreadActivityCountForItemInGroup() {
        return unreadActivityCountForItemInGroup;
    }

    private String unreadActivityCountForItemInGroup;

    public String getUnreadActivityCountOnGroupItem() {
        return unreadActivityCountOnGroupItem;
    }

    private String unreadActivityCountOnGroupItem;

    public String getProjectImageInPinList() {
        return projectImageInPinList;
    }

    private String projectImageInPinList;

    public String getActivePinIconInProjectPage() {
        return activePinIconInProjectPage;
    }

    private String activePinIconInProjectPage;

    public String getUnreadActivityCountInPinList() {
        return unreadActivityCountInPinList;
    }

    private String unreadActivityCountInPinList;

    public String getPinIconInProjectIcon() {
        return pinIconInProjectIcon;
    }

    private String pinIconInProjectIcon;

    public String getPinSettingIcon() {
        return pinSettingIcon;
    }

    private String pinSettingIcon;

    public String getPinSettingButtonInEmptyList() {
        return pinSettingButtonInEmptyList;
    }

    private String pinSettingButtonInEmptyList;

    public String getDropIconInTopMenu() {
        return dropIconInTopMenu;
    }

    private String dropIconInTopMenu;

    public String getProjectEeditedIconImage() {
        return projectEeditedIconImage;
    }

    private String projectEeditedIconImage;

    public String getProjectLogoInFirstGroup() {
        return projectLogoInFirstGroup;
    }

    private String projectLogoInFirstGroup;

    public ConfigFileReader getProjectFileReader() {
        return projectFileReader;
    }

    public String getProjectGroupConfirmButton() {
        return projectGroupConfirmButton;
    }

    private String projectGroupConfirmButton;

    public String getProjectItemInAllProjectPage() {
        return projectItemInAllProjectPage;
    }

    private String projectItemInAllProjectPage;

    public String getLookAllUserProjectInUserCenter() {
        return lookAllUserProjectInUserCenter;
    }

    public String getProjectNameInBlockList() {
        return projectNameInBlockList;
    }

    public String getProjectItemForBlockList() {
        return ProjectItemForBlockList;
    }
    public String getPrivateProject() {
        return privateProject;
    }

    public String getWaitEelementInCreateProjectPage() {
        return waitEelementInCreateProjectPage;
    }
    public String getOption() {
        return option;
    }

    public String getSelectGitIgnore() {
        return selectGitIgnore;
    }


    public String getPublicProjectTitle() {
        return publicProjectTitle;
    }

    public String getPrivateProjectTitle() {
        return privateProjectTitle;
    }

    public String getCreateProjectInProjectList() {
        return createProjectInProjectList;
    }



    public String getSelectLicence() {
        return selectLicence;
    }

    public String getHasReadMe() {
        return hasReadMe;
    }

    public String getPrivateProjectType() {
        return privateProjectType;
    }

    public String getPublicProjectType() {
        return publicProjectType;
    }

    public String getInputProjectDescription() {
        return inputProjectDescription;
    }

    public String getProjectButtonInContextMenu() {
        return projectButtonInContextMenu;
    }

    public String getAddIconForCreateProjectInProjectPage() {
        return addIconForCreateProjectInProjectPage;
    }

    public String getCreateProjectsInUserCenter() {
        return createProjectsInUserCenter;
    }

    public String getCreateProjectInNavigationBar() {
        return createProjectInNavigationBar;
    }

    public String getAddSignInNavigationBar() {
        return addSignInNavigationBar;
    }

    public String getUserCenterUrl() {
        return userCenterUrl;
    }

    public String getProjectActivity() {
        return projectActivity;
    }

    public String getCreateProjectsUrl() {
        return createProjectsUrl;
    }

    public String getInputProjectName() {
        return inputProjectName;
    }

    public String getCreateProjectButton() {
        return createProjectButton;
    }


}

package org.ChiTest.Project;

import org.ChiTest.AdvancedTask.AdvancedTask;
import org.ChiTest.Document.DocumentTest;
import org.ChiTest.Page.PS;
import org.ChiTest.Page.Page;
import org.ChiTest.Topic.Topic;
import org.ChiTest.User.User;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by dugancaii on 8/11/2014.  cc
 */
public class Project {
    private User creator;
    private String description;
    private String projectName;
    private List<User> member;
    private String type;
    private boolean haveReadme;
    private String licence;
    private String gitignore;
    private List<Topic> topics;
    private DocumentTest documents;
    private Date createTime;
    private ProjectFileReader projectFileReader;
    private AdvancedTask advancedTask;

    public Project(User creator, String projectName, String description, String type) {
        this.creator = creator;
        this.projectName = projectName;
        this.description = description;
        this.type = type;
        projectFileReader = new ProjectFileReader();
        advancedTask = new AdvancedTask();
    }
    public Project(User creator, String projectName) {
        this.creator = creator;
        this.projectName = projectName;
        this.type = "private";
        projectFileReader = new ProjectFileReader();
        advancedTask = new AdvancedTask();
    }
    public Project(User creator, String projectName,String type) {
        this.creator = creator;
        this.projectName = projectName;
        this.description = "sampleProject";
        this.type = type;
        projectFileReader = new ProjectFileReader();
        advancedTask = new AdvancedTask();
    }

    public String getProjectLink() {
        return this.getCreator().getPage().getBaseUrl() + "u/"+ this.getCreator().getUserLoginName()+"/p/"+this.getProjectName();
    }
    public String getProjectTopicHomeLink() {
        return this.getCreator().getPage().getBaseUrl() + "u/"+ this.getCreator().getUserLoginName()+"/p/"+this.getProjectName()+"/topic/all";
    }
    public String getProjectTopicCreateLink() {
        return this.getCreator().getPage().getBaseUrl() + "u/"+ this.getCreator().getUserLoginName()+"/p/"+this.getProjectName()+"/topic/create";
    }
    public String getProjectMemberLink() {
        return this.getCreator().getPage().getBaseUrl() + "u/"+ this.getCreator().getUserLoginName()+"/p/"+this.getProjectName()+ "/members";
    }
    public String getProjectMemberLink(String memberName) {
        return this.getCreator().getPage().getBaseUrl() + "u/"+ this.getCreator().getUserLoginName()+"/p/"+this.getProjectName()+ "/members/" + memberName ;
    }
    public String getProjectTaskLink() {
        return this.getCreator().getPage().getBaseUrl() + "u/"+ this.getCreator().getUserLoginName()+"/p/"+this.getProjectName() + "/tasks";
    }
    public String getProjectWatchedTaskLink() {
        return this.getCreator().getPage().getBaseUrl() + "u/"+ this.getCreator().getUserLoginName()+"/p/"+this.getProjectName() + "/tasks/watch";
    }
    public String getProjectTaskLink(String memberLoginName) {
        return this.getCreator().getPage().getBaseUrl() + "u/"+ this.getCreator().getUserLoginName()+"/p/"+this.getProjectName() + "/tasks/user/" + memberLoginName;
    }
    public String getProjectAllTaskLink() {
        return this.getCreator().getPage().getBaseUrl() + "u/"+ this.getCreator().getUserLoginName()+"/p/"+this.getProjectName() + "/tasks/all";
    }
    public String getProjectAdvancedSettingLink() {
        return this.getCreator().getPage().getBaseUrl() + "u/"+ this.getCreator().getUserLoginName()+"/p/"+this.getProjectName() + "/setting/advance";
    }
    public String getSettingLink() {
        return this.getCreator().getPage().getBaseUrl() + "u/"+ this.getCreator().getUserLoginName()+"/p/"+this.getProjectName() + "/setting";
    }
    public String getProjectCodeLink() {
        return this.getCreator().getPage().getBaseUrl() + "u/"+ this.getCreator().getUserLoginName()+"/p/"+this.getProjectName() + "/git";
    }
    public String getProjectCodeCloneUrl() {
        return "https://git.coding.com/" + this.getCreator().getUserLoginName()+"/"+this.getProjectName() + ".git";
    }
    public String getProjectCodeBranchLink() {
        return this.getCreator().getPage().getBaseUrl() + "u/"+ this.getCreator().getUserLoginName()+"/p/"+this.getProjectName() + "/git/branches";
    }
    public String getProjectDocumentLink() {
        return this.getCreator().getPage().getBaseUrl() + "u/"+ this.getCreator().getUserLoginName()+"/p/"+this.getProjectName() + "/attachment";
    }

    public void setProjectLink(String projectLink) {
        this.projectLink = projectLink;
    }

    private String projectLink;


    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getDescription() {
        return description;
    }



    public String getProjectName() {
        return projectName;
    }
    public String getProjectFullName() {
        return this.getCreator().getUserLoginName() + "/"+projectName;
    }

    public List<User> getMember() {
        return member;
    }

    public void setMember(List<User> member) {
        this.member = member;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isHaveReadme() {
        return haveReadme;
    }



    public String getLicence() {
        return licence;
    }

    public void setProjectName(String projectName){
        this.projectName = projectName;

    }

    public String getGitignore() {
        return gitignore;
    }


    public DocumentTest getDocuments() {
        return documents;
    }

    public void setDocuments(DocumentTest documents) {
        this.documents = documents;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Project(User creator, String projectName, String description, String type, boolean haveReadme, String licence, String gitignore ){
        this.creator = creator;
        this.description = description;
        this.projectName = projectName;
        this.type = type;
        this.haveReadme = haveReadme;
        this.licence = licence;
        this.gitignore = gitignore;
        this.member = new LinkedList<User>();
        this.projectFileReader = new ProjectFileReader();
        advancedTask = new AdvancedTask();
    }
    public void createSampleProject()  {
        Page projectPage = creator.getPage();
        projectPage.navigate(projectPage.getBaseUrl() +projectFileReader.getCreateProjectsUrl(), projectFileReader.getInputProjectName());
        projectPage.sendKeys(projectFileReader.getInputProjectName(), projectName);
        projectPage.sendKeys(projectFileReader.getInputProjectDescription(), description );
        if(type.equals("private")) {
            projectPage.clickElement(projectFileReader.getPrivateProjectType());
        }else{
            projectPage.clickElement(projectFileReader.getPublicProjectType());
        }
        projectPage.getDriver().executeScript("$(\""+projectFileReader.getHasReadMe() +"\").click()");
        projectPage.clickElement(projectFileReader.getCreateProjectButton());
        projectPage.waitElementDisappear(".form.loading");
    }

    public void deleteProject() throws InterruptedException {
        Page projectPage = creator.getPage();
        if(findProjectInAllProjectPage(projectPage)!= -1) {
            deleteProject(creator, this.getProjectAdvancedSettingLink());
        }
    }
    public void addProjectMember( User addMember)  {
        Page page = creator.getPage();
        if(!searchProjectMember(addMember)) {
            String projectMemberCountText = page.getText(projectFileReader.getMemberCountText());
            page.clickElement(projectFileReader.getAddIconForAddMember(), projectFileReader.getSearchInputForAddMember());
            page.clearAndSendKeys(projectFileReader.getSearchInputForAddMember(), addMember.getUserName());
            page.waitLoadingIconInvisible();
            page.clickElement(projectFileReader.getSelectedMemberItemInAddMember(),
                    page.findItemInOnePageByTextEquals(projectFileReader.getSelectedMemberItemInAddMember(), addMember.getUserName()));
            page.clickElement(projectFileReader.getConfirmButtonForAddMember());
            assertTrue("添加的成员后在前端没更新,原来 " + projectMemberCountText + " " + "现在 " + page.getText(projectFileReader.getMemberCountText()),
                    page.waitForContentChange(projectFileReader.getMemberCountText(), projectMemberCountText, 10));
            assertTrue("添加成员后刷新页面无法搜索到该新成员",searchProjectMember( addMember));
        }
    }
    public int removeProjectMember( User removedMember){
        Page projectPage = getCreator().getPage();
        if(searchProjectMember( removedMember) ) {
            if( !getType().equals("public") ) {
                advancedTask.deleteSomeoneAllTasks(projectPage, this, removedMember);
            }
            projectPage.navigate(getProjectMemberLink(), "#inner-menu img");
            String projectMemberCountText = projectPage.getText("#inner-menu .header.item");
            int basePageNum = projectPage.getPageNum("#inner-menu .header");
            projectPage.clearAndSendKeys(projectFileReader.getSearchMemberInput(), removedMember.getUserName());
            projectPage.clickElement("#inner-menu img", ".remove.icon");
            //用chrome 调试下
            projectPage.checkAlert(".remove.icon", "删除成员的提示有误","确认移除该成员？");
            projectPage.verifyHint("成员移除成功！");
            assertTrue("删除项目成员后，页面上的计数没有改变",projectPage.waitForContentChange("#inner-menu .header.item", projectMemberCountText,20));
            return basePageNum -1 ;
        }
        return -1;
    }

    public boolean searchProjectMember( User user){
        Page projectPage = this.creator.getPage();
        projectPage.navigateContiansUrl(getProjectMemberLink(), projectFileReader.getSearchMemberInput());
        return  projectPage.findItemInOnePageByTextEquals(projectFileReader.getMemberInMemberList(),user.getUserName()) != -1;
    }
    private void deleteProject(User creator, String settingLink){
        Page projectPage = creator.getPage();
        if(!settingLink.contains(creator.getUserLoginName())){
            return;
        }
        projectPage.navigate(settingLink);
        if(projectPage.elementIsPresent("#project-setting-content .button",4)) {
            projectPage.clickElement("#project-setting-content .button.red.default", "input[placeholder='请输入密码']");
            projectPage.sendKeys("input[placeholder='请输入密码']", creator.getUserPassword());
            projectPage.clickElement("#password-validate-modal .red.button");
            projectPage.getWaitTool().waitForElement(By.cssSelector(projectFileReader.getWaitEelementInProjectHome()), 20);
        }
    }
    public void deleteAllProject(){
        deleteAllProject(creator);
    }
    public void deleteAllProject(User creator)  {
        Page projectPage = creator.getPage();
        projectPage.navigate(projectPage.getProjectUrl());
        projectPage.waitLoadingIconInvisible();
        List<String> links = new ArrayList<>();
        changeProjectListLayout(projectPage, ".block", ".list.layout.icon");
        if(projectPage.elementIsPresent(projectFileReader.getProjectLinkForBlockListInAllProjectItem())) {
            int projectCount = projectPage.getElementCount(projectFileReader.getProjectLinkForBlockListInAllProjectItem());
            for (int i = 0 ; i< projectCount;i++) {
                links.add(projectPage.getLink(projectFileReader.getProjectLinkForBlockListInAllProjectItem(),i)+"/setting/advance");
            }
        }
        for(String link : links){
            deleteProject(creator, link);
        }


    }
    public int findProjectInAllProjectPage(Page page) throws InterruptedException {
        page.navigate(page.getProjectUrl(),projectFileReader.getWaitEelementInProjectHome());
        if(page.elementIsPresent(".block", PS.shortWait)){
            page.clickElement(".block",  projectFileReader.getProjectNameForBlockList());
        }
        return page.findItemInOnePageByTextEquals(projectFileReader.getProjectNameForBlockList(),getProjectFullName());

    }

    public void changeProjectListLayout(Page projectPage, String layoutSelector, String waitElement){
        if(projectPage.elementIsPresent(layoutSelector,2)){
            projectPage.clickElement(layoutSelector, waitElement);
        }
    }
    public void createFixtureProject() throws InterruptedException {
        if( findProjectInAllProjectPage(this.getCreator().getPage()) == -1){
            createSampleProject();
        }
    }

}

package org.ChiTest.Document;

import dataReader.ConfigureInfo;
import org.ChiTest.Activity.ActivityFileReader;
import org.ChiTest.Activity.AdvancedActivity;
import org.ChiTest.MemberTest;
import org.ChiTest.Page.PS;
import org.ChiTest.Page.Page;
import org.ChiTest.Project.Project;
import org.ChiTest.Project.ProjectFileReader;
import org.ChiTest.Topic.Topic;
import org.ChiTest.Topic.TopicFileReader;
import org.ChiTest.User.User;
import org.ChiTest.WebComponent.MarkDownInputBox;
import org.ChiTest.WebComponent.ScreenShot;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Keys;
import reference.ConfigFileReader;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by dugancaii on 8/12/2014.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DocumentTest {
    static User zombieUser;
    static User ciwangUser;
    static Page ciwangPage;
    static Page zombiePage;
    static Cookie zombieCookie;
    static Cookie ciwangCookie;
    static ProjectFileReader projectFileReader;
    static DocumentFileReader documentFileReader;
    static TopicFileReader topicFileReader;
    static ActivityFileReader activityFileReader;
    static Project project;
    static MemberTest memberTest;
    static MarkDownInputBox markDownInputBox;
    static ConfigureInfo configureInfo;
    static Logger log = Logger.getLogger("documentTest");
    static String fileRemoveExit;
   // static Project testProject;
    static String fileCountText;
    static AdvancedActivity advancedActivity;
    static Folder folderOperator;

    @BeforeClass
    public static void setUp() throws Exception {
        configureInfo = new ConfigureInfo(true,true);
        memberTest = new MemberTest();
        documentFileReader = new DocumentFileReader();
        projectFileReader = new ProjectFileReader();

        zombiePage = configureInfo.getZombieUser().getPage();
        ciwangPage = configureInfo.getCiwangUser().getPage();

        ciwangUser  = new User("coding_test60","coding_test60","123456",ciwangPage,"Fruit-2.png");
        zombieUser = new User("czy123","池哲宇","123456",zombiePage,"Fruit-3.png");

        ciwangCookie = ciwangPage.getStagingSid("82dafd5a-9f16-45cd-8a6d-5824bf6d6f6f");
        zombieCookie = zombiePage.getStagingSid("5b2c800e-8a7b-467f-a58b-d7067da892e9");

        zombieUser.autoLogin(configureInfo.getLoginUrl(),zombieCookie );
        ciwangUser.autoLogin(configureInfo.getLoginUrl(), ciwangCookie);

        project = new Project(zombieUser,projectFileReader.getPrivateProject() +zombiePage.getMark(),"this is a test ","private");
        fileRemoveExit = "请先清空所有子文件夹及文件";
        markDownInputBox = new MarkDownInputBox("");
        project.createFixtureProject();
        //project = new Project(zombieUser, "documentTest1");
        topicFileReader  = new TopicFileReader();
        advancedActivity = new AdvancedActivity(ciwangUser,zombieUser, project);
        activityFileReader = new ActivityFileReader();
        folderOperator = new Folder("",zombieUser);
        project.addProjectMember(ciwangUser);
    }
    @Rule
    public ScreenShot screenShotZombie = new ScreenShot(zombieUser);
    @Rule
    public ScreenShot screenShotCiwang = new ScreenShot(ciwangUser);
    @Test
    public void test01_0_defaultDirectory(){
        zombiePage.navigate(project.getProjectDocumentLink(), documentFileReader.getDefaultFolder());
        zombiePage.assertIconAndTextAndLink("项目首页又侧栏的文档图标", "文档", "big.archive",
                project.getProjectDocumentLink(), "#context-menu .item:nth-child(4)");
        zombiePage.assertTextEquals("默认文件夹不存在或文件数目不为0", "默认文件夹\n(0)", documentFileReader.getDefaultFolder());
        zombiePage.assertElementPresent("默认文件夹选中时，文件夹图标没有呈打开状态", documentFileReader.getDefaultFolder(),
                documentFileReader.getFolderOpenIcon());
    }
    @Test
    public void test01_1_Folder(){
        zombiePage.navigate(project.getProjectDocumentLink(), documentFileReader.getDefaultFolder());
        zombiePage.clickElement(documentFileReader.getAddFolder(), documentFileReader.getFolderNameInput());
        zombiePage.assertElementPresent("默认文件夹没有选中时，文件夹图标没有呈关闭状态", documentFileReader.getDefaultFolder(),
                documentFileReader.getFolderCloseIcon());

        zombiePage.assertAttributeEquals("新建文件夹的默认名字有误", "新建文件夹", documentFileReader.getFolderNameInput(), "value");
        zombiePage.clearAndSendKeys(documentFileReader.getFolderNameInput(), "A");
        zombiePage.clickElement(documentFileReader.getFolderHeader());
        assertFolderHeaderNameEquals(zombiePage, "A");
        zombiePage.assertTextEquals("添加子文件夹的控件上的提示文字", "添加子文件夹", documentFileReader.getAddSubFolder());
        zombiePage.assertTextEquals("添加文件夹的控件上的提示文字", "添加文件夹", documentFileReader.getAddFolder());

        Folder folder = new Folder("A",zombieUser);
        folder.createFolderWithoutCheck(zombiePage, "A");
        zombiePage.verifyHint("存在同名的文件夹");

        folder.renameDocumentFolder("A4");
        folder.renameDocumentFolderWithoutCheck("A4",0);
        zombiePage.verifyHint("存在同名的文件夹");
        assertFolderHeaderNameEquals(zombiePage,"新建文件夹");
    }

    @Test
    public void test01_2_subFolder(){
        zombiePage.navigate(project.getProjectDocumentLink(), documentFileReader.getDefaultFolder());
        Folder folder = new Folder("subTestFolder",zombieUser) ;
        folder.createFolder();
        Folder subFolder = folder.createSubFolder("subTestSubFolder");
        assertFolderHeaderNameEquals(zombiePage,subFolder.getFolderName());
        zombiePage.assertElementPresent("选中子文件夹时，文件夹图标没有呈打开状态", documentFileReader.getNoDefaultFolder(),
                documentFileReader.getFolderOpenIcon());
        zombiePage.assertElementPresent("选中子文件夹时，子文件夹图标没有呈打开状态", documentFileReader.getSubFolder(),
                documentFileReader.getFolderOpenIcon());

        folderOperator.createSubFolderWithoutCheck(zombiePage, subFolder.getFolderName());
        zombiePage.verifyHint("存在同名的文件夹");
        assertFolderHeaderNameEquals(zombiePage,"新建文件夹");

        folder.renameDocumentSubFolder(subFolder,"B4");
        folder.renameDocumentSubFolderWithoutCheck(subFolder,"B4",0);
        zombiePage.verifyHint("存在同名的文件夹");
        assertFolderHeaderNameEquals(zombiePage,"新建文件夹");

        zombiePage.clickElement(documentFileReader.getDefaultFolder());
        zombiePage.assertElementNotPresent("选择其他文件夹子文件夹没有收起", documentFileReader.getSubFolder());
    }
    @Test
     public void test01_3_removeFolder(){
        zombiePage.navigate(project.getProjectDocumentLink(), documentFileReader.getDefaultFolder());
        Folder folder = new Folder("removeFolder",zombieUser) ;
        folder.createFolder();
        Folder subFolder = folder.createSubFolder("removeSubFolder");

        folderOperator.refuseRemoveFolderTest(zombiePage,folder.getFolderName());
        zombiePage.assertTextEquals("有文件的子文件夹被删除 ", folder.getFolderName(), documentFileReader.getFolderHeader() );

        folder.removeSubFolder(subFolder);
        folder.removeFolder();
    }
    @Test
    public void test01_4_FolderActivity() throws ParseException {
        zombiePage.navigate(project.getProjectDocumentLink(), documentFileReader.getDefaultFolder());
        Folder folder = new Folder("folder",zombieUser) ;
        folder.createFolder();
        folder.renameDocumentFolder("newFolder");
        Folder subFolder = folder.createSubFolder("subFolder");
        folder.renameDocumentSubFolder(subFolder,"newSubFolder");

        folder.removeSubFolder(subFolder);
        folder.removeFolder();

        advancedActivity.constructDocumentDirectoryActivity(zombieUser, folder, subFolder);
        advancedActivity.checkActivity(zombiePage, activityFileReader.getDocumentActivityTagInProjectHomePage() );
    }

   // @Test
    public void test02_0_uploadFileInFolderAndSubFolder(){
        zombiePage.navigate(project.getProjectDocumentLink(), documentFileReader.getDefaultFolder());
        Folder folder,subFolder;
        if(!zombiePage.elementIsPresent(documentFileReader.getNoDefaultFolder())){
            folder = new Folder("upLoadFile", zombieUser);
            folder.createFolder();
        }else{
            folder = new Folder(zombiePage.getText(documentFileReader.getNoDefaultFolder()),zombieUser,zombiePage.getCurrentUrl());
        }
        zombiePage.clickElement(documentFileReader.getNoDefaultFolder());
        if(!zombiePage.elementIsPresent(documentFileReader.getSubFolder())){
            subFolder = folder.createSubFolder("upLoadFileSub");
        }else{
            subFolder = new Folder(zombiePage.getText(documentFileReader.getSubFolder()),zombieUser,zombiePage.getCurrentUrl());
        }

        zombiePage.clickElement(documentFileReader.getNoDefaultFolder(),documentFileReader.getFileItemInFileList());
        zombiePage.assertTextEquals("文档的上传zone消失了", "还未上传任何文件，点击或将文件拖拽至此上传！\n" +
                "支持所有文件格式，单个文件不超过50M。", documentFileReader.getFileUploadZoneInFileList());
        File folderFile = fileUploadTest( folder, zombieUser, documentFileReader.getFolderCountIndex(), documentFileReader.getNoDefaultFolder());

        zombiePage.clickElement(documentFileReader.getSubFolder(),documentFileReader.getFileItemInFileList());
        ciwangPage.navigate(zombiePage.getCurrentUrl(),  documentFileReader.getFileUploadZoneInFileList());
        File subFolderFile = fileUploadTest(subFolder,ciwangUser,documentFileReader.getSubFolderCountIndex(), documentFileReader.getSubFolder() );
        ciwangPage.assertTextEquals("子文档有文件上传后，父文档的计数没有加一", "(2)", documentFileReader.getFolderCountIndex());

        zombiePage.clickElement(documentFileReader.getSubFolder());
        verifyFileInFileList(zombiePage,
                markDownInputBox.getImageFileName(), ciwangUser, "109 KB", 0);
        zombiePage.assertAttributeContainWords("文件上传后，列表中，文档的小图片预览消失了或url不对", zombiePage.getDocumentImgStoreUrl(),
                documentFileReader.getFilePreviewForFileItemInFileList(), "src");

        advancedActivity.constructUploadFileInFolderActivity(folderFile, subFolderFile);
        advancedActivity.checkActivity(zombiePage, activityFileReader.getDocumentActivityTagInProjectHomePage() );
    }

    public File fileUploadTest(Folder folder, User fileOperator, String countSelector, String folderSelector){
        Page page = fileOperator.getPage();
        upLoadImageInDocument(page);
        page.waitForContentChange(countSelector, "(0)", 10);
        page.assertTextEquals("文件上传后，文档的计数没有加一，文件 ["+folder.getFolderName()+"]", "(1)", countSelector);
        refuseRemoveFolderTest(folderSelector, 0, 1, folder.getFolderName(), "有文件的子文件夹被删除");
        page.clickElement(documentFileReader.getFileNameForFileItemInFileList(), documentFileReader.getFileDownloadButtonInPreview());
        return new File(fileOperator,markDownInputBox.getImageFileName(),page.getCurrentUrl());
    }



    //@Test
    public void test02_uploadFile() throws ParseException, InterruptedException {
        zombiePage.navigate(project.getProjectDocumentLink(), documentFileReader.getDefaultFolder());
        // 建一个文件夹，上传文件，看计数加一，文件夹有文件则无法删除
        removeAllFileAndNoDefaultFolderInProject(project);
        Folder parentFolder = new Folder("upLoadFileA",zombieUser);
        String folderName = "upLoadFileA";
        String subFolderName = "upLoadFileB";
        parentFolder.createFolder();
        zombiePage.assertTextEquals("文档的上传zone消失了", "还未上传任何文件，点击或将文件拖拽至此上传！\n" +
                "支持所有文件格式，单个文件不超过50M。", "#attachment .file-upload-zone span");
        upLoadImageInDocument(zombiePage);
        zombiePage.waitForContentChange(documentFileReader.getFolderCountIndex(), "(0)", 10);
        zombiePage.assertTextEquals("文件上传后，文档的计数没有加一", "(1)", documentFileReader.getFolderCountIndex());
        refuseRemoveFolderTest(documentFileReader.getNoDefaultFolder(),0, 0,folderName, "有文件的文件夹被删除");
        zombiePage.assertTextEquals("有文件的子文件夹被删除 upLoadFileA", folderName, documentFileReader.getFolderHeader() );
        //验证项目成员的权
        ciwangPage.navigate(zombiePage.getCurrentUrl(),documentFileReader.getFileItemInFileList());
        ciwangPage.moveToElement(documentFileReader.getFileItemInFileList());
        ciwangPage.assertElementNotPresent("项目成员有权力删除别人的文件",documentFileReader.getFileRemoveIcon());

        zombiePage.refresh(documentFileReader.getFileNameForFileItemInFileList());
        zombiePage.clickElement(documentFileReader.getFileNameForFileItemInFileList(), documentFileReader.getFileRemoveButtonInPreview());
        File folderFile = new File(zombieUser,markDownInputBox.getImageFileName(),zombiePage.getCurrentUrl());

        createDocumentSubFolder(zombiePage, subFolderName);
        ciwangPage.navigate(zombiePage.getCurrentUrl(), "#attachment .file-upload-zone span");
        upLoadImageInDocument(ciwangPage);
        ciwangPage.waitForContentChange(documentFileReader.getSubFolderCountIndex(),"(0)",10);
        ciwangPage.assertTextEquals("文件上传后，子文档的计数没有加一", "(1)", documentFileReader.getSubFolderCountIndex());
        ciwangPage.assertTextEquals("子文档有文件上传后，父文档的计数没有加一", "(2)", documentFileReader.getFolderCountIndex());
        refuseRemoveFolderTest(documentFileReader.getSubFolder(), 0, 1, subFolderName, "有文件的子文件夹被删除");
        ciwangPage.assertTextEquals("有文件的子文件夹被删除 upLoadFileB", subFolderName, documentFileReader.getFolderHeader() );
        ciwangPage.refresh(documentFileReader.getFileNameForFileItemInFileList());
        ciwangPage.clickElement(documentFileReader.getFileNameForFileItemInFileList(), documentFileReader.getFileDownloadButtonInPreview());
        File subFolderFile = new File(ciwangUser,markDownInputBox.getImageFileName(),ciwangPage.getCurrentUrl());

        zombiePage.clickElement(documentFileReader.getSubFolder());
        //上传或新建的文件要在文件夹列表中，检查文件预览，文件名，大小，作者，时间
        verifyFileInFileList(zombiePage,
                markDownInputBox.getImageFileName(), ciwangUser, "109 KB", 0);
        zombiePage.assertAttributeContainWords("文件上传后，列表中，文档的小图片预览消失了或url不对", zombiePage.getDocumentImgStoreUrl(),
                documentFileReader.getFilePreviewForFileItemInFileList(), "src");

        zombiePage.moveToElement(documentFileReader.getFileItemInFileList());
        zombiePage.assertElementPresent("项目所有者没有权力删除别人的文件",documentFileReader.getFileRemoveIcon());


        //父子文件夹中的文件删除
        ciwangPage.clickElement(documentFileReader.getSubFolder(),documentFileReader.getFileItemInFileList());
        ciwangPage.moveToElement(documentFileReader.getFileItemInFileList());
        ciwangPage.checkAlert(documentFileReader.getFileRemoveIcon(),
                "删除文件的提示", "确认删除文件 \"" + markDownInputBox.getImageFileName() + "\" ?");
        ciwangPage.verifyHint("文档删除成功！");
        ciwangPage.assertElementNotPresent("删除文件后文件依然存在", documentFileReader.getFileItemInFileList());
        ciwangPage.waitForContentChange(documentFileReader.getSubFolderCountIndex(),"(1)",10);
        ciwangPage.waitForContentChange(documentFileReader.getFolderCountIndex(),"(2)",10);
        ciwangPage.assertTextEquals("删除子文件夹的文件后，文档的计数没有减一", "(0)", documentFileReader.getSubFolderCountIndex());
        ciwangPage.assertTextEquals("删除父文件夹的文件后，文档的计数没有减一", "(1)", documentFileReader.getFolderCountIndex());

        zombiePage.clickElement(documentFileReader.getNoDefaultFolder(),documentFileReader.getFileItemInFileList());
        zombiePage.moveToElement(documentFileReader.getFileItemInFileList());
        zombiePage.checkAlert(documentFileReader.getFileRemoveIcon(),
                "删除文件的提示", "确认删除文件 \"" + markDownInputBox.getImageFileName() + "\" ?");
        zombiePage.verifyHint("文档删除成功！");
        zombiePage.waitForContentChange(documentFileReader.getFolderCountIndex(),"(1)",10);
        zombiePage.assertTextEquals("删除父文件夹的文件后，文档的计数没有减一", "(0)", documentFileReader.getFolderCountIndex());

        advancedActivity.constructUploadFileInFolderActivity(folderFile, subFolderFile);
        advancedActivity.checkActivity(zombiePage, activityFileReader.getDocumentActivityTagInProjectHomePage() );
    }

    @Test
    public void test03_fileOperation() throws InterruptedException, ParseException {
        String folderName = "fileOperationFolderName";
        String subFolderName = "fileOperationSubFolderName";
        //ciwangPage.navigate("https://staging.coding.net/u/test5/p/documentTest1/attachment/12652",documentFileReader.getAddFolder());
        File oldTxtFile = new File("txtFile", "txtFileContent中文", ciwangUser,"20.00 bytes","txt",folderName, subFolderName,project);
        File newTxtFile = new File("TxtFile", "TxtFileContent中文",oldTxtFile);
        //todo 加了中文
        File oldMDFile =  new File("markDownFile", "## h2中文", ciwangUser,"11.00 bytes","md",folderName, subFolderName,project);
        File newMDFile =  new File("MarkDownFile", "## h3中文", oldMDFile);

        File oldHtmlFile =  new File("htmlFile", "## h2中文", ciwangUser,"18.00 bytes","html",folderName, subFolderName,project);
        File newHtmlFile =  new File("HtmlFile", "## h3中文", oldHtmlFile);
        int fileCount;

        removeAllFileAndNoDefaultFolderInProject(project);
        ciwangPage.navigate(project.getProjectDocumentLink(),documentFileReader.getAddFolder());
        createDocumentFolder(ciwangPage, folderName);
        createDocumentSubFolder(ciwangPage, subFolderName);
        // txt文档
        createFileInDocumentTest(ciwangPage, oldTxtFile,"TXT",".file-icon.txt.ellipsis",documentFileReader.getFileAddText(),documentFileReader.getFileContentInput());
        editFileInDocumentTest(ciwangPage, oldTxtFile, newTxtFile,documentFileReader.getFileContentInput());
        filePreviewTest(ciwangPage, newTxtFile, documentFileReader.getFileTextContentInPreview(), newTxtFile.getFileContent());

        // Markdown文档
        createFileInDocumentTest(ciwangPage, oldMDFile,"Markdown",".file-icon.md.ellipsis",documentFileReader.getFileAddMarkDown(),documentFileReader.getFileContentInput());
        editFileInDocumentTest(ciwangPage, oldMDFile, newMDFile,documentFileReader.getFileContentInput());
        filePreviewTest(ciwangPage,newMDFile, documentFileReader.getFileMDContentInPreview(),"h3中文" );

        // HTML文档
        createFileInDocumentTest(ciwangPage, oldHtmlFile,"HTML",".file-icon.html.ellipsis",documentFileReader.getFileAddHtml(),".cke_reset.cke_editable ");
        editFileInDocumentTest(ciwangPage, oldHtmlFile, newHtmlFile, ".cke_reset.cke_editable ");
        filePreviewTest(ciwangPage, newHtmlFile, documentFileReader.getFileHTMLContentInPreview(),newHtmlFile.getFileContent());

        //预览
        ciwangPage.clickElement(documentFileReader.getFileAddCancelButton(),documentFileReader.getNextFileInPreview());
        //获取url
        newHtmlFile.setFileUrl(ciwangPage.getCurrentUrl());
        ciwangPage.assertTextEquals("文档预览向后翻按钮显示的文件名有误", newMDFile.getFileFullName(), documentFileReader.getNextFileInPreview());
        ciwangPage.clickElement(documentFileReader.getNextFileInPreview());
        ciwangPage.waitForElement(documentFileReader.getFileMDContentInPreview(), 7);
        Thread.sleep(1000);
        ciwangPage.assertTextEquals("文档预览向后翻,显示的文件夹名有误", newMDFile.getFileContent().replace("## ", ""), documentFileReader.getFileMDContentInPreview());
        ciwangPage.assertTextEquals("文档预览向后翻,显示的文件内容有误", newMDFile.getFileFullName(),documentFileReader.getCurrentFileNameInPreview());
        ciwangPage.assertTextEquals("文档预览向后翻按钮显示的文件名有误", newTxtFile.getFileFullName(),documentFileReader.getNextFileInPreview());
        ciwangPage.assertTextEquals("文档预览向前翻按钮显示的文件名有误",  newHtmlFile.getFileFullName(),documentFileReader.getPrevFileInPreview());

        ciwangPage.clickElement(documentFileReader.getPrevFileInPreview());
        ciwangPage.waitForElement(documentFileReader.getFileMDContentInPreview(), 7);
        Thread.sleep(1000);
        ciwangPage.assertTextEquals("文档预览向后翻按钮显示的文件名有误", newMDFile.getFileFullName(), documentFileReader.getNextFileInPreview());
        ciwangPage.assertTextEquals("文档预览向后翻,显示的文件夹名有误", newHtmlFile.getFileContent(),documentFileReader.getFileHTMLContentInPreview());
        ciwangPage.assertTextEquals("文档预览向后翻,显示的文件内容有误", newHtmlFile.getFileFullName(),documentFileReader.getCurrentFileNameInPreview());

        //全选
        //编辑, 测试全选和取消全选，取消编辑，点击选择框取消，点击选择框勾选，勾选的计数
        ciwangPage.clickElement(documentFileReader.getSubFolder(),  documentFileReader.getFileListEditButton());
        ciwangPage.assertIconAndText("编辑列表按钮", documentFileReader.getFileListEditButton(), "编辑列表", "edit");
        ciwangPage.clickElement(documentFileReader.getFileListEditButton(), documentFileReader.getFileListEditCancelButton());
        ciwangPage.assertTextEquals("编辑列表，编辑返回按钮", "返回", documentFileReader.getFileListEditCancelButton());
        ciwangPage.assertTextEquals("编辑列表，编辑全选按钮", "全选" , documentFileReader.getFileListAllSelectedButton());
        ciwangPage.clickElement(documentFileReader.getFileListEditCancelButton(), documentFileReader.getFileListEditButton());
        ciwangPage.clickElement(documentFileReader.getFileListEditButton(), documentFileReader.getFileListEditCancelButton());
        verifyFileSelectedCount(ciwangPage, 0, "没有选文档时，显示选择的文档数不为0");

        ciwangPage.clickElement(documentFileReader.getFileListAllSelectedButton());
        ciwangPage.waitForContentChange(documentFileReader.getFileSelectedCountText(), fileCountText, 10);
        ciwangPage.assertCountEquals("全选时，被打勾的文档数目不对",3, documentFileReader.getFileSelectedSignIcon());
        verifyFileSelectedCount(ciwangPage, 3, "全选文档时，显示选择的文档数不为3");

        ciwangPage.clickElement( documentFileReader.getFileListAllSelectedButton());
        ciwangPage.assertCountEquals("取消全选时，被打勾的文档数目不对", 3, documentFileReader.getFileNotSelectedSignIcon());
        ciwangPage.waitForContentChange(documentFileReader.getFileSelectedCountText(), fileCountText, 10);
        verifyFileSelectedCount(ciwangPage, 0, "没有选文档时，显示选择的文档数不为0");
        //单个勾选

        ciwangPage.clickElement(documentFileReader.getFileNotSelectedSignIcon(), documentFileReader.getFileSelectedSignIcon());
        ciwangPage.assertElementPresent("编辑文档列表时，文档无法单个勾选", documentFileReader.getFileSelectedSignIcon());

        ciwangPage.clickElement(documentFileReader.getFileSelectedSignIcon(), documentFileReader.getFileNotSelectedSignIcon());
        ciwangPage.assertCountEquals("编辑文档列表时，文档无法单个取消勾选",3, documentFileReader.getFileNotSelectedSignIcon());

        //移动 把html移动到folder, 再移动到subfolder
        ciwangPage.clickElement(documentFileReader.getFileNotSelectedSignIcon(), documentFileReader.getFileSelectedSignIcon());
        ciwangPage.assertIconAndText("编辑文档列表时，文档移动按钮", documentFileReader.getFileMoveFolderButton(), "移动到...", "folder");
        ciwangPage.clickElement(documentFileReader.getFileMoveFolderButton(), documentFileReader.getFileFolderInMoveFolderList());
        ciwangPage.assertIconAndText("文档移动的文件列表，默认文件夹",
                documentFileReader.getFileFolderInMoveFolderList(), "默认文件夹", "folder");
        ciwangPage.assertIconAndText("文档移动的文件列表，父文件夹",
                documentFileReader.getFileFolderInMoveFolderList(), folderName, "folder", 1);
        assertTrue("文档移动的文件列表，子文件夹(或当前文件夹不对)",
                (Boolean) ciwangPage.executeScript("return $(\""+  documentFileReader.getFileSubFolderInMoveFolderList() + " .current"  +" \").is(':visible') == false"));

        ciwangPage.clickElement(documentFileReader.getFileFolderInMoveFolderList(),1);
        ciwangPage.verifyHint("1个文档移动成功!");

        changeDocument(ciwangPage, folderName, documentFileReader.getNoDefaultFolder());
        ciwangPage.assertTextEquals("子文件夹中的文件不能移动到文件夹中", newHtmlFile.getFileFullName(),documentFileReader.getFileNameForFileItemInFileList());


        ciwangPage.clickElement(documentFileReader.getFileListEditButton(), documentFileReader.getFileListEditCancelButton());
        ciwangPage.clickElement(documentFileReader.getFileNotSelectedSignIcon(), documentFileReader.getFileSelectedSignIcon());
        ciwangPage.clickElement(documentFileReader.getFileMoveFolderButton(), documentFileReader.getFileFolderInMoveFolderList());

        assertTrue("文档移动的文件列表，子文件夹(或当前文件夹不对)",
                (Boolean) ciwangPage.executeScript("return $(\""+  documentFileReader.getFileFolderInMoveFolderList() + " .current"  +" \").is(':visible') == false"));

        ciwangPage.clickElement(documentFileReader.getFileSubFolderInMoveFolderList());
        ciwangPage.verifyHint("1个文档移动成功!");
        changeDocument(ciwangPage, subFolderName, documentFileReader.getSubFolder());
        ciwangPage.assertTextEquals("父文件夹中的文件不能移动到子文件夹中", newHtmlFile.getFileFullName(),documentFileReader.getFileNameForFileItemInFileList());
        fileCount = ciwangPage.getElementCount(documentFileReader.getFileItemInFileList());
        ciwangPage.clickElement(documentFileReader.getFileNameForFileItemInFileList(), documentFileReader.getFileRemoveButtonInPreview());
        ciwangPage.assertIconAndText("文档预览删除",documentFileReader.getFileRemoveButtonInPreview(),"删除", "cloud.trash" );
        ciwangPage.checkAlert(documentFileReader.getFileRemoveButtonInPreview(), "在文件预览页，删除文件", "确认删除文件 \""+ newHtmlFile.getFileFullName()+"\" ?");
        ciwangPage.waitForItemCountChange(documentFileReader.getFileItemInFileList(), fileCount, 5);
        ciwangPage.assertCountEquals( "文档预览中删除文档失败" , fileCount - 1,documentFileReader.getFileItemInFileList() );

        zombiePage.navigate(ciwangPage.getCurrentUrl(), documentFileReader.getFileItemInFileList() );
        fileCount = zombiePage.getElementCount(documentFileReader.getFileItemInFileList());
        zombiePage.clickElement(documentFileReader.getFileListEditButton(), documentFileReader.getFileListAllSelectedButton());

        //让项目所有者在编辑列表删除
        zombiePage.clickElement(documentFileReader.getFileListAllSelectedButton());
        zombiePage.checkAlert(documentFileReader.getFileRemoveButtonInFileList(),
                "项目所有者，在编辑列表删除文档失败", "确认删除选定的 "+fileCount+" 个文档？");
        zombiePage.verifyHint("选定的 2 个文档删除成功！");
        zombiePage.waitForItemCountChange(documentFileReader.getFileItemInFileList(), fileCount, 5);
        zombiePage.assertCountEquals( "项目所有者，在编辑列表删除文档失败" , 0,documentFileReader.getFileItemInFileList() );

        advancedActivity.constructFileOperationActivity(newHtmlFile);
        advancedActivity.checkActivity(zombiePage, activityFileReader.getDocumentActivityTagInProjectHomePage());
    }

    @Test
    public void test04_filesPreview(){
        zombiePage.refresh(project.getProjectDocumentLink(),documentFileReader.getDefaultFolder());
        zombiePage.clickElement(documentFileReader.getDefaultFolder(), documentFileReader.getFileItemInFileList() );

        upLoadFileInDocument(zombiePage, "川端康成文集之《名人 舞姬》.pdf");
        zombiePage.assertElementPresent("上传 pdf 文档后，文档图标有误", ".file-icon.pdf.ellipsis");
        zombiePage.refresh(documentFileReader.getFileNameForFileItemInFileList());
        zombiePage.clickElement(documentFileReader.getFileNameForFileItemInFileList(), "#pdf-preview .next.button");
        zombiePage.assertElementPresent("pdf 预览", "#pdf-preview .next.button", 15);
        zombiePage.clickElement(documentFileReader.getDefaultFolder(), documentFileReader.getFileItemInFileList());

        upLoadFileAndPreview( "文档excel.xlsx","excel");
        upLoadFileAndPreview( "文档ppt.pptx","pptx");
        upLoadFileAndPreview("文档word.docx", "docx");
    }
    public void upLoadFileAndPreview(String fileFullName, String type){
        upLoadFileInDocument(zombiePage, fileFullName);
        zombiePage.refresh(documentFileReader.getFileNameForFileItemInFileList());
        zombiePage.clickElement(documentFileReader.getFileNameForFileItemInFileList(),"#preview-icon .cloud.download.icon");
        zombiePage.assertElementPresent(type + " 预览", "iframe[id='office_preview']",15);
        zombiePage.clickElement(documentFileReader.getDefaultFolder(), documentFileReader.getFileItemInFileList() );
    }
    @Test
    public void test05_upLoadImageFromDocumentTest() throws InterruptedException, ParseException {
        zombiePage.navigate(project.getProjectDocumentLink(),documentFileReader.getDefaultFolder());
        removeAllFileAndNoDefaultFolderInProject(project);
        createDocumentFolder(zombiePage, "A父文件夹");
        upLoadFileInDocument(zombiePage, "川端康成文集之《名人 舞姬》.pdf");
        upLoadImageInDocument(zombiePage);
        createDocumentSubFolder(zombiePage,"B子文件夹");
        upLoadFileInDocument(zombiePage, "文档word.docx");
        upLoadFileInDocument(zombiePage, "Venn_0110_1000_1000_0000.png");

        //打开上传框，检查文件夹，文件数，选择子文件夹内的文件，
        Topic topic = new Topic(project, "uploadImageInDocument","uploadImage",zombieUser);
        topic.createTopic();
        MarkDownInputBox markDownInputBox1 = new MarkDownInputBox(topicFileReader.getCommentMDbox());
        zombiePage.clickElement(markDownInputBox1.getPhotoButtonSelector(), documentFileReader.getFolderItemInProjectFileUploadWrapper());
        zombiePage.assertElementPresent("文档图片上传框选中文件夹，文件夹没出现打开 icon ", documentFileReader.getFolderItemInProjectFileUploadWrapper(),
                documentFileReader.getFolderOpenIcon());
        zombiePage.assertElementPresent("文档图片上传框选中未文件夹，文件夹没出现闭合 icon ", documentFileReader.getFolderItemInProjectFileUploadWrapper(),1,
                documentFileReader.getFolderCloseIcon());
        zombiePage.assertTextEquals("文档图片上传框里，父文件夹图片数显示不对", "A父文件夹\n(1)",
                documentFileReader.getFolderItemInProjectFileUploadWrapper(),1);
        zombiePage.assertTextEquals("文档图片上传框里，子文件夹图片数显示不对", "B子文件夹\n" +
                "(1)", documentFileReader.getFolderItemInProjectFileUploadWrapper(),2);
        zombiePage.clickElement(documentFileReader.getFolderItemInProjectFileUploadWrapper(), 1);
        zombiePage.clickElement(documentFileReader.getImageItemInProjectFileUploadWrapper());
        zombiePage.assertElementPresent("文档图片上传框里，选择图片后，没有出现红色勾选图标",  documentFileReader.getImageItemInProjectFileUploadWrapper()
                + ".selected  .red.corner.label .checkmark.icon ");

        zombiePage.clickElement(documentFileReader.getImageItemInProjectFileUploadWrapper());
        zombiePage.assertElementNotPresent("文档图片上传框里，取消选择图片后，红色勾选图标没有消失", documentFileReader.getImageItemInProjectFileUploadWrapper()
                + ".selected  .red.corner.label .checkmark.icon ");
        zombiePage.clickElement(documentFileReader.getImageItemInProjectFileUploadWrapper());
        zombiePage.clickElement(documentFileReader.getFileConfirmInProjectFileUploadWrapper());
        zombiePage.waitElementInvisible(documentFileReader.getFileConfirmInProjectFileUploadWrapper());
        zombiePage.assertAttributeContainWords("选择父文件夹中，上传的图片有误", "imagePreview", topicFileReader.getCommentInput(),"value");

        zombiePage.clickElement(markDownInputBox1.getPhotoButtonSelector(), documentFileReader.getFolderItemInProjectFileUploadWrapper());
        zombiePage.clickElement(documentFileReader.getFolderItemInProjectFileUploadWrapper(),2);
        zombiePage.clickElement(documentFileReader.getImageItemInProjectFileUploadWrapper());
        zombiePage.clickElement(documentFileReader.getFileConfirmInProjectFileUploadWrapper());
        zombiePage.waitElementInvisible(documentFileReader.getImageItemInProjectFileUploadWrapper());
        markDownInputBox1.clickMDConfirmButton(zombiePage);
        zombiePage.waitForElement("#topic-comments .comment   .bubble-markdown-image",5 );
        zombiePage.assertCountEquals("选择父文件夹和子文件夹的图片发送后，图片数目不对", 2, "#topic-comments .comment   .bubble-markdown-image" );


    }
    private void changeDocument(Page ciwangPage, String folderName, String folderSelector){
        String folderTitle = ciwangPage.getText(documentFileReader.getFolderHeader());
        ciwangPage.clickElement(folderSelector,
                ciwangPage.findItemInOnePageByTextContains(folderSelector, folderName));
        ciwangPage.waitForContentChange(folderSelector, folderTitle, 10);
    }
    private void verifyFileSelectedCount(Page ciwangPage, int num, String message){
        ciwangPage.assertTextEquals(message,"（已选 ? 个文档）".replace("?",num+""),documentFileReader.getFileSelectedCountText());
        fileCountText = ciwangPage.getText(documentFileReader.getFileSelectedCountText());
    }
    public void createFileInDocumentTest( Page page,File file,String fileMenu,String fileIcon, String addFileSelector,String contentInputSelector ) throws InterruptedException {
        String type = "["+file.getFilePostfix()+"]";
        page.clickElement(documentFileReader.getNoDefaultFolder(),
                page.findItemInOnePageByTextContains(documentFileReader.getNoDefaultFolder(), file.getFolderName()));
        page.clickElement(documentFileReader.getSubFolder(),
                page.findItemInOnePageByTextContains(documentFileReader.getSubFolder(), file.getSubFolderName()),documentFileReader.getFileItemInFileList());
        int fileCount = page.getElementCount(documentFileReader.getFileItemInFileList());
        page.assertIconAndText("从文档中创建文件的按钮有误", documentFileReader.getFileAddButton(), "创建...", "add");
        page.moveToElement(documentFileReader.getFileAddButton());
        page.assertTextEquals("从文档中创建文件的菜单上文字有误" + type, fileMenu, addFileSelector);
        page.clickElement(addFileSelector, documentFileReader.getFilePostfix());
        page.assertTextEquals("后缀有误" + type, "." + file.getFilePostfix(), documentFileReader.getFilePostfix());
        page.clickElement(documentFileReader.getFileAddCancelButton(), documentFileReader.getFileAddButton());
        page.assertElementPresent("文档取消创建失灵" + type, documentFileReader.getFileAddButton());
        page.moveToElement(documentFileReader.getFileAddButton());
        page.waitForElement(addFileSelector,5);
        page.clickElement(addFileSelector, documentFileReader.getFilePostfix());
        if(file.getFilePostfix().equals("html")) {
            page.clickElement("#cke_18");
            page.executeScript("$(\"" + contentInputSelector + "\").text(\"" + file.getFileContent() + "  \")");
        }else {
            page.clearAndSendKeys(documentFileReader.getFileContentInput(), file.getFileContent());
           // page.clearAndSendKeys("#cke_36_contents .cke_editable", file.getFileContent());
        }
        page.clearAndSendKeys(documentFileReader.getFileTitleInput(), file.getFileName());
        page.clickElement(documentFileReader.getFileAddSaveButton());
        page.waitForElement(documentFileReader.getFileItemInFileList(),10);
        page.verifyHint("创建成功！");
        page.assertCountEquals("文档添加新文件失败"+type ,fileCount + 1 , documentFileReader.getFileItemInFileList());
        verifyFileInFileList(page, file.getFileFullName(), ciwangUser, file.getFileSize(), 0);
        page.assertElementPresent("文档的缩略图消失"+type,fileIcon);
    }
    public void editFileInDocumentTest( Page page, File oldFile, File newFile,String fileContentSelector){
        String type = "["+oldFile.getFilePostfix()+"]";
        page.moveToElement(documentFileReader.getFileItemInFileList(), 0);
        page.clickElement(documentFileReader.getFileEditButtonInFileList(), 0, documentFileReader.getFileTitleInput());
        page.assertAttributeEquals("文档编辑时，标题输入框和原来的标题不一致" + type, oldFile.getFileName(), documentFileReader.getFileTitleInput(), "value");
        page.assertAttributeContainWords("文档编辑时，文档内容输入框和原来的内容不一致" + type, oldFile.getFileContent(),
                documentFileReader.getFileContentInput(), "value");
        page.assertTextEquals("文档编辑室，更新按钮的文本有误" + type, "更新", documentFileReader.getFileAddSaveButton());
        page.clickElement(documentFileReader.getFileAddCancelButton(), documentFileReader.getFileItemInFileList());
        page.moveToElement(documentFileReader.getFileItemInFileList(), 0);
        page.clickElement(documentFileReader.getFileEditButtonInFileList(), 0, documentFileReader.getFileTitleInput());
        if(oldFile.getFilePostfix().equals("html")) {
            page.clickElement("#cke_18");
            page.executeScript("$(\"" + fileContentSelector + "\").text(\"" + newFile.getFileContent() + "  \")");
        }else {
            page.clearAndSendKeys(documentFileReader.getFileContentInput(), newFile.getFileContent());
            // page.clearAndSendKeys("#cke_36_contents .cke_editable", file.getFileContent());
        }
        page.clearAndSendKeys(documentFileReader.getFileTitleInput(), newFile.getFileName());
        page.clickElement(documentFileReader.getFileAddSaveButton());
        page.verifyHint("更新成功！");
        page.assertElementPresent("文件更新失败" + type, documentFileReader.getCurrentFileNameInPreview(), 10);
    }
    public void filePreviewTest(Page page, File  newFile, String fileContentInPreview,String preViewContent){
        String type = "["+newFile.getFilePostfix()+"]";
        page.waitForElement(fileContentInPreview, 10);
        page.assertTextEquals("文件编辑后，预览页文件名不正确" + type, newFile.getFileFullName(), documentFileReader.getFileTitleInPreview());
        page.assertTextEquals("文件编辑后，预览页文件名不正确" + type, preViewContent, fileContentInPreview);
        page.clickElement(documentFileReader.getFileInfoInPreview(), documentFileReader.getFileInfoPopupInPreview());
        page.assertTextContainsOneFromThreeExpect("文件更新后,预览的文件信息中[最近更新]不对" + type, "最近更新: 几秒前", "最近更新: 几秒内", "创建时间: 1分钟前",documentFileReader.getFileInfoPopupInPreview(),0);
        page.assertTextContainsOneFromThreeExpect("文件更新后,预览的文件信息中[创建时间]不对" + type,"创建时间: 几秒内", "创建时间: 几秒前", "创建时间: 1分钟前",documentFileReader.getFileInfoPopupInPreview(),0);
        verifyFileInfoInPreview(page, "文件类型", newFile.getFilePostfix(),type);
        verifyFileInfoInPreview(page, "文件大小", newFile.getFileSize().replace("00","0"),type);
        verifyFileInfoInPreview(page, "创建人", newFile.getFileOwner().getUserName(),type);
        page.assertIconAndText("文档预览页面的编辑按钮", documentFileReader.getFileEditButtonInPreview(), "编辑", "edit.cloud");
        page.assertIconAndText("文档预览页面的下载按钮", documentFileReader.getFileDownloadButtonInPreview(), "下载", "cloud.download");
        page.assertIconAndText("文档预览页面的删除按钮",documentFileReader.getFileRemoveButtonInPreview(),"删除", "cloud.trash" );

        page.clickElement(documentFileReader.getFileEditButtonInPreview(),documentFileReader.getFilePostfix());
        page.assertTextEquals("文档预览页的编辑按钮没有跳到", "." + newFile.getFilePostfix(),  documentFileReader.getFilePostfix());

    }
    private void verifyFileInfoInPreview(Page page, String infoType, String info, String type ){
        page.assertTextContainWords("文件更新后,预览的文件信息中["+infoType+"]不对" + type, infoType + ": "+ info, documentFileReader.getFileInfoPopupInPreview() );
    }
    public void verifyDownloadIcon(Page page,int itemNum){
        page.assertIconAndText("列表中，第"+ (itemNum + 1) +"个文档，下载按钮有误", documentFileReader.getFileDownloadButtonInFileList(), "", "download", itemNum);
        page.assertAttributeContainWords("列表中，第"+ (itemNum + 1) +"个文档下载连接有误", "download", documentFileReader.getFileDownloadButtonInFileList(), itemNum, "href");
    }
    public void verifyFileInFileList(Page page, String fileName, User uploader, String fileSize, int fileItemNum )  {
        page.assertTextEqualsOneFromThreeExpect("文件上传后，列表中，文档的上传时间有误", "几秒内",
                "几秒前", "1分钟前",documentFileReader.getFileUploaderTimeForFileItemInFileList(), fileItemNum);

        page.assertTextEquals("文件上传后，列表中，文档的文件名不对", fileName,
                documentFileReader.getFileNameForFileItemInFileList(), fileItemNum);
        assertEquals("文件上传后，列表中，文档的大小不对", fileSize.replaceAll("\\.[0-9]*",""),
                page.getText(documentFileReader.getFileSizeForFileItemInFileList()).replaceAll("\\.[0-9]*", ""));

        page.assertAttributeEquals("文件上传后，列表中，文档的上传者头像有误", uploader.getFruitAvatar(),
                documentFileReader.getFileUploaderAvatarForFileItemInFileList(), fileItemNum, "src");
        page.assertTextEquals("文件上传后，列表中，文档的上传者名字有误", uploader.getUserName(),
                documentFileReader.getFileUploaderNameForFileItemInFileList(), fileItemNum);
        page.moveToElement(documentFileReader.getFileItemInFileList());
        verifyDownloadIcon(page, fileItemNum);
    }

    private void upLoadImageInDocument(Page page){
        page.getDriver().executeScript("$('" + documentFileReader.getFileUploadZone() + "').css('display', 'block')");
        markDownInputBox.sendImgFilePath(page, documentFileReader.getFileUploadZone());
    }
    private void upLoadFileInDocument(Page page, String fileName){
        page.getDriver().executeScript("$('" + documentFileReader.getFileUploadZone() + "').css('display', 'block')");
        try {
            markDownInputBox.sendFile(page, documentFileReader.getFileUploadZone(),
                    URLDecoder.decode(ConfigFileReader.class.getResource("/"+fileName).toString(),"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        page.assertTextNotEquals("文件" + fileName + "上传失败", "上传失败",".outer span");
    }
    public static void removeAllFileInFileList(Page page){
        int fileCount;
        while (page.elementIsPresent(documentFileReader.getFileItemInFileList())) {
            fileCount = page.getElementCount(documentFileReader.getFileItemInFileList());
            page.moveToElement(documentFileReader.getFileItemInFileList());
            page.sureAlert(documentFileReader.getFileRemoveIcon());
            page.verifyHint("文档删除成功！");
            page.waitForItemCountChange(documentFileReader.getFileItemInFileList(), fileCount, 10);
            page.assertCountEquals("删除文件后，文档的计数没有减一", fileCount - 1, documentFileReader.getFileItemInFileList());
        }
    }

    public static void removeAllFileAndNoDefaultFolderInProject(Project project){
        Page page = project.getCreator().getPage();
        page.navigate(project.getProjectDocumentLink(), documentFileReader.getDefaultFolder());
        while(page.elementIsPresent(documentFileReader.getNoDefaultFolder(), PS.shortWait)){
            page.clickElement(documentFileReader.getNoDefaultFolder());
            while (page.elementIsPresent(documentFileReader.getSubFolder(),PS.shortWait)){
                page.clickElement(documentFileReader.getSubFolder());
                while(page.elementIsPresent(documentFileReader.getFileItemInFileList(),PS.shortWait)){
                    removeAllFileInFileList(page);
                }
                page.moveToElement(documentFileReader.getSubFolder());
                page.clickElement(documentFileReader.getSubFolder() + " " + documentFileReader.getFolderEditIcon());
                page.waitForElement(documentFileReader.getSubFolder() + " "+documentFileReader.getRemoveIcon(),5);
                int subFolderCount = page.getElementCount(documentFileReader.getSubFolder());
                page.sureAlert(documentFileReader.getSubFolder() + " " + documentFileReader.getRemoveIcon());
                page.waitForItemCountChange(documentFileReader.getSubFolder(), subFolderCount, 5);
            }
            while(page.elementIsPresent(documentFileReader.getFileItemInFileList(),2)){
                removeAllFileInFileList(page);
            }
            page.moveToElement(documentFileReader.getNoDefaultFolder());
            page.clickElement(documentFileReader.getNoDefaultFolder() + " " +documentFileReader.getFolderEditIcon());
            page.waitForElement(documentFileReader.getRemoveIcon(),5);
            int folderCount = page.getElementCount(documentFileReader.getNoDefaultFolder());
            page.sureAlert(documentFileReader.getRemoveIcon());
            page.waitForItemCountChange(documentFileReader.getNoDefaultFolder(), folderCount, 5);
        }
    }

    public void refuseRemoveFolderTest(String folderSelector,int noDefaultFolderNum,int removeIconNum, String folderName, String testMessage){
        zombiePage.clickElement(folderSelector,noDefaultFolderNum);
        zombiePage.moveToElement(folderSelector,noDefaultFolderNum);
        zombiePage.clickElement(documentFileReader.getFolderEditIcon(),removeIconNum);
        zombiePage.waitForElement(documentFileReader.getRemoveIcon(),removeIconNum,5);
        int folderCount = zombiePage.getElementCount(folderSelector);

        zombiePage.checkAlert(documentFileReader.getRemoveIcon(),
                removeIconNum,"删除文件夹","确认删除文件夹 \""+folderName+"\" ?");
        zombiePage.verifyHint(fileRemoveExit);
        zombiePage.assertCountEquals(testMessage+"["+ folderName +"]", folderCount, folderSelector);

    }

    public void assertFolderHeaderNameEquals(Page page,String folderHeaderName){
        page.assertTextEquals("文件夹的header不正确", folderHeaderName, documentFileReader.getFolderHeader());
    }

    public void createDocumentFolder(Page zombiePage, String folderName ){
        zombiePage.clickElement(documentFileReader.getAddFolder(), documentFileReader.getFolderNameInput());
        zombiePage.clearAndSendKeys(documentFileReader.getFolderNameInput(), folderName);
        zombiePage.clickElement(documentFileReader.getFolderHeader());
    }
    public void createDocumentSubFolder(Page zombiePage, String subFolderName ){
        zombiePage.clickElement(documentFileReader.getAddSubFolder(),documentFileReader.getSubFolderNameInput());
        zombiePage.clearAndSendKeys(documentFileReader.getSubFolderNameInput(), subFolderName);
        zombiePage.sendKeys(Keys.ENTER);
    }
    @AfterClass
    public static void tearDown() throws InterruptedException {
        project.deleteProject();
        zombiePage.getDriver().quit();
        ciwangPage.getDriver().quit();
    }

}

package org.ChiTest.Document;

import org.ChiTest.Page.Page;
import org.ChiTest.User.User;
import org.openqa.selenium.Keys;

import static org.junit.Assert.assertNotEquals;

/**
 * Created by dugancaii on 2/3/2015.
 */
public class Folder {
    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    private String folderName;
    private User owner;

    public String getRenamedName() {
        return renamedName;
    }

    public void setRenamedName(String renamedName) {
        this.renamedName = renamedName;
    }

    private String renamedName;
    private DocumentFileReader documentFileReader;

    public String getFolderUrl() {
        return folderUrl;
    }

    private String folderUrl;


    public Folder(String folderName, User owner){
        this.folderName = folderName;
        this.owner = owner;
        documentFileReader = new DocumentFileReader();
    }
    public Folder(String folderName, User owner, String folderUrl){
        this(folderName,  owner);
        this.folderUrl = folderUrl;
    }

    public Folder createSubFolder(String subFolderName){
        Page page = owner.getPage();
        int folderNum = page.findItemInOnePageByTextContains(documentFileReader.getNoDefaultFolder(),folderName);
        if(folderNum == -1 ){
            createFolder();
        }
        int subFolderCount = createSubFolderWithoutCheck(page, folderNum, subFolderName);
        page.assertTextContainWords("创建子文件夹后，文件夹名不对",subFolderName, documentFileReader.getSubFolder());
        page.assertCountEquals("创建子文件夹后，子文件夹数没有加一", subFolderCount + 1, documentFileReader.getSubFolder());
        return new Folder(subFolderName,owner, owner.getPage().getCurrentUrl());
    }

    public void createFolder( ){
        int folderCount = owner.getPage().getElementCount(documentFileReader.getNoDefaultFolder());
        createFolderWithoutCheck(owner.getPage(),folderName);
        owner.getPage().assertTextContainWords("创建文件夹后，文件夹名不对", folderName, documentFileReader.getNoDefaultFolder());
        owner.getPage().assertCountEquals("创建文件夹后，文件夹数没有加一,可能创建失败", folderCount + 1, documentFileReader.getNoDefaultFolder());
        folderUrl = owner.getPage().getCurrentUrl();
    }

    public void createFolderWithoutCheck(Page page, String folderName ){
        page.clickElement(documentFileReader.getAddFolder(), documentFileReader.getFolderNameInput());
        page.clearAndSendKeys(documentFileReader.getFolderNameInput(), folderName);
        page.clickElement(documentFileReader.getFolderHeader(), documentFileReader.getAddFolder());
    }
    public void renameDocumentFolder( String newFolderName){
        Page page = owner.getPage();
        int folderNum =renameDocumentFolderWithoutCheck(newFolderName,page.findItemInOnePageByTextContains(documentFileReader.getNoDefaultFolder(), folderName));
        page.assertTextContainWords("重命名文件夹后，文件夹名不对",documentFileReader.getNoDefaultFolder(), folderNum, newFolderName);
    }
    public int  renameDocumentFolderWithoutCheck( String newFolderName, int folderNum){
        Page page = owner.getPage();
        page.clickElement(documentFileReader.getNoDefaultFolder(), folderNum);
        page.moveToElement(documentFileReader.getNoDefaultFolder(), folderNum);
        page.clickElement(documentFileReader.getFolderEditIcon(), folderNum);
        page.clearAndSendKeys(documentFileReader.getFolderNameInput(),folderNum, newFolderName);
        page.clickElement(documentFileReader.getFolderHeader());
        page.waitForContentChange(documentFileReader.getNoDefaultFolder(), folderNum, "",5);
        page.waitForContentChange(documentFileReader.getNoDefaultFolder(), folderNum, folderName,5);
        renamedName = folderName;
        folderName = newFolderName;
        return folderNum;
    }


    public int createSubFolderWithoutCheck(Page page,int folderNum, String subFolderName){
        int subFolderCount = page.getElementCount(documentFileReader.getSubFolder());
        page.clickElement(documentFileReader.getNoDefaultFolder(), folderNum);
        page.clickElement(documentFileReader.getAddSubFolder(), documentFileReader.getSubFolderNameInput());
        page.clearAndSendKeys(documentFileReader.getSubFolderNameInput(), subFolderName);
        page.sendKeys(Keys.ENTER);
        return subFolderCount;
    }

    public void createSubFolderWithoutCheck(Page page, String subFolderName){
        createSubFolderWithoutCheck(page,0,subFolderName);
    }
    public void renameDocumentSubFolderWithoutCheck(Folder subFolder, String newFolderName,int subFolderNum){
        Page page = owner.getPage();
        page.clickElement(documentFileReader.getNoDefaultFolder(),
                page.findItemInOnePageByTextContains(documentFileReader.getNoDefaultFolder(), folderName));
        String oldSubFolderName = page.getText(documentFileReader.getSubFolder(),subFolderNum);
        page.clickElement(documentFileReader.getSubFolder(),subFolderNum);
        page.moveToElement(documentFileReader.getSubFolder(), subFolderNum);
        page.clickElement(documentFileReader.getFolderEditIcon(), subFolderNum + 1);
        page.clearAndSendKeys(documentFileReader.getSubFolderNameInput(),subFolderNum, newFolderName);
        page.clickElement(documentFileReader.getFolderHeader());
        page.waitForContentChange(documentFileReader.getSubFolder(), subFolderNum, "", 5);
        page.waitForContentChange(documentFileReader.getSubFolder(),subFolderNum,oldSubFolderName,5);
        subFolder.setRenamedName(subFolder.getFolderName());
        subFolder.setFolderName(newFolderName);

    }
    public void renameDocumentSubFolder(Folder subFolder, String newFolderName){
        renameDocumentSubFolderWithoutCheck(subFolder,newFolderName, owner.getPage().findItemInOnePageByTextContains(documentFileReader.getSubFolder(), subFolder.getFolderName()));
        assertNotEquals("重命名子文件夹后，文件夹名不对", -1, owner.getPage().findItemInOnePageByTextContains(documentFileReader.getSubFolder(), newFolderName));
    }
    public void refuseRemoveFolderTest(Page page, String folderName){
        int folderCount = page.getElementCount(documentFileReader.getNoDefaultFolder());
        removeFolderWithoutCheck(page,folderName);
        page.verifyHint("请先清空所有子文件夹及文件");
        page.assertCountEquals("有子文件夹的文件夹被删除" + "[" + folderName + "]", folderCount, documentFileReader.getNoDefaultFolder());

    }
    public void removeFolderWithoutCheck(Page page, String folderName){
        int folderNum = page.findItemInOnePageByTextContains(documentFileReader.getNoDefaultFolder(), folderName);
        page.clickElement(documentFileReader.getNoDefaultFolder(), folderNum);
        page.moveToElement(documentFileReader.getNoDefaultFolder(), folderNum);
        page.clickElement(documentFileReader.getFolderEditIcon(), folderNum);
        page.waitForElement(documentFileReader.getRemoveIcon(), folderNum, 5);
        page.checkAlert(documentFileReader.getRemoveIcon(),
                folderNum, "删除文件夹", "确认删除文件夹 \"" + folderName + "\" ?");
    }
    public void removeSubFolder(Folder subFolder){
        Page page = owner.getPage();
        int folderNum = page.findItemInOnePageByTextContains(documentFileReader.getNoDefaultFolder(), folderName);
        page.clickElement(documentFileReader.getNoDefaultFolder(), folderNum);
        page.clickElement(documentFileReader.getSubFolder());
        page.moveToElement(documentFileReader.getSubFolder());
        page.clickElement(documentFileReader.getSubFolder() + " " + documentFileReader.getFolderEditIcon());
        page.waitForElement(documentFileReader.getSubFolder() + " " + documentFileReader.getRemoveIcon(), 5);
        int subFolderCount = page.getElementCount(documentFileReader.getSubFolder());
        page.checkAlert(documentFileReader.getSubFolder() + " " + documentFileReader.getRemoveIcon(), 0
                , "删除子文件夹", "确认删除文件夹 \"" + subFolder.getFolderName() + "\" ?");
        page.waitForItemCountChange(documentFileReader.getSubFolder(), subFolderCount, 5);
        page.assertCountEquals("子文件夹删除不成功" + "[" + subFolder.getFolderName() + "]", subFolderCount - 1, documentFileReader.getSubFolder());
    }
    public void removeFolder(){
        Page page = owner.getPage();
        int folderCount = page.getElementCount(documentFileReader.getNoDefaultFolder());
        removeFolderWithoutCheck(page,folderName);
        page.waitForItemCountChange(documentFileReader.getNoDefaultFolder(), folderCount, 5);
        page.assertCountEquals("文件夹删除不成功" + "[" + folderName + "]", folderCount - 1, documentFileReader.getNoDefaultFolder());
    }


}

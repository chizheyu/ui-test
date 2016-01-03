package org.ChiTest.Document;

import org.ChiTest.Project.Project;
import org.ChiTest.User.User;

/**
 * Created by dugancaii on 1/28/2015.
 */
public class File {
    public String getFileContent() {
        return fileContent;
    }

    String fileContent;

    public String getFileName() {
        return fileName;
    }

    public User getFileOwner() {
        return fileOwner;
    }

    public String getFileSize() {
        return fileSize;
    }

    public String getFilePostfix() {
        return filePostfix;
    }
    public String getFileFullName(){
        return fileName +"." + filePostfix;
    }
    public String getOldFileFullName(){
        return oldFileName +"." + filePostfix;
    }

    private String fileName;
    private String oldFileName;
    private User fileOwner;
    private String fileSize;
    private String filePostfix;

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    private String fileUrl;

    public Project getProject() {
        return project;
    }

    private Project project;

    public String getFolderName() {
        return folderName;
    }

    public String getSubFolderName() {
        return subFolderName;
    }

    private String folderName;
    private String subFolderName;
    public File(String fileName,String fileContent,User fileOwner, String fileSize, String filePostfix, Project project){
        this.fileName = fileName;
        this.fileContent = fileContent;
        this.fileOwner = fileOwner;
        this.fileSize = fileSize ;
        this.filePostfix = filePostfix;
        this.project = project;
    }
    public File(String fileName,String fileContent,User fileOwner, String fileSize, String filePostfix,String folderName,String subFolderName, Project project){
        this.fileName = fileName;
        this.fileContent = fileContent;
        this.fileOwner = fileOwner;
        this.fileSize = fileSize ;
        this.filePostfix = filePostfix;
        this.folderName = folderName;
        this.subFolderName = subFolderName;
        this.project = project;
    }
    public File(String fileName,String fileContent,File file){
        this.fileName = fileName;
        this.fileContent = fileContent;
        this.filePostfix = file.getFilePostfix();
        this.fileOwner = file.getFileOwner();
        this.fileSize = file.getFileSize() ;
        this.folderName = file.getFolderName();
        this.subFolderName = file.getSubFolderName();
        this.project = file.getProject();
        this.oldFileName = file.getFileName();

    }
    public File(User fileOwner,String fileName, String fileUrl){
        this.fileName = fileName;
        this.fileOwner = fileOwner;
        this.fileUrl = fileUrl;
    }

}

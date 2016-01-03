package org.ChiTest.Document;

import reference.ConfigFileReader;

/**
 * Created by dugancaii on 1/16/2015.
 */
public class DocumentFileReader {
    private ConfigFileReader documentFileReader;

    public String getDefaultFolder() {
        return defaultFolder;
    }

    private String defaultFolder;

    public String getNoDefaultFolder() {
        return noDefaultFolder;
    }

    private String noDefaultFolder;

    public String getSubFolder() {
        return subFolder;
    }

    private String subFolder;

    public String getFolderHeader() {
        return folderHeader;
    }

    private String folderHeader;

    public String getAddSubFolder() {
        return addSubFolder;
    }

    private String addSubFolder;

    public String getAddFolder() {
        return addFolder;
    }

    private String addFolder;

    public String getFolderCountIndex( ) {
        return  noDefaultFolder + " .count";
    }
    public String getSubFolderCountIndex() {
        return  subFolder + " .count";
    }



    public String getRemoveIcon() {
        return removeIcon;
    }

    private String removeIcon;

    public String getFolderCloseIcon() {
        return folderCloseIcon;
    }

    private String folderCloseIcon;

    public String getFolderOpenIcon() {
        return folderOpenIcon;
    }

    private String folderOpenIcon;

    public String getFolderNameInput() {
        return folderNameInput;
    }

    private String folderNameInput;

    public String getFileUploadZoneInFileList() {
        return fileUploadZoneInFileList;
    }

    private String fileUploadZoneInFileList;

    public String getSubFolderNameInput() {
        return subFolderNameInput;
    }

    private String subFolderNameInput;

    public String getFileItemInFileList() {
        return fileItemInFileList;
    }

    private String fileItemInFileList;

    public String getFileNameForFileItemInFileList() {
        return fileNameForFileItemInFileList + " span";
    }

    public String getFilePreviewForFileItemInFileList() {
        return fileNameForFileItemInFileList + " a img";
    }
    public String getFileSizeForFileItemInFileList() {
        return fileItemInFileList + " .size";
    }
    public String getFileUploaderAvatarForFileItemInFileList() {
        return fileItemInFileList + " .uploader img";
    }
    public String getFileUploaderNameForFileItemInFileList() {
        return fileItemInFileList + " .uploader";
    }
    public String getFileUploaderTimeForFileItemInFileList() {
        return fileItemInFileList + " .time";
    }

    private String fileNameForFileItemInFileList;

    public String getFileRemoveIcon() {
        return fileRemoveIcon;
    }

    private String fileRemoveIcon;

    public String getFileAddButton() {
        return fileAddButton;
    }

    private String fileAddButton;

    public String getFileAddText() {
        return fileAddMenu+":nth-child(1)";
    }
    public String getFileAddMarkDown() {
        return fileAddMenu+":nth-child(2)";
    }
    public String getFileAddHtml() {
        return fileAddMenu+":nth-child(3)";
    }

    private String fileAddMenu;

    public String getFileTitleInput() {
        return fileTitleInput;
    }

    private String fileTitleInput;

    public String getFilePostfix() {
        return filePostfix;
    }

    private String filePostfix;

    public ConfigFileReader getDocumentFileReader() {
        return documentFileReader;
    }

    public String getFileContentInput() {
        return fileContentInput;
    }

    private String fileContentInput;

    public String getFileAddCancelButton() {
        return fileAddCancelButton;
    }

    private String fileAddCancelButton;

    public String getFileAddSaveButton() {
        return fileAddSaveButton;
    }

    private String fileAddSaveButton;

    public String getFileDownloadButtonInFileList() {
        return fileDownloadButtonInFileList;
    }

    private String fileDownloadButtonInFileList;

    public String getFolderEditIcon() {
        return folderEditIcon;
    }

    private String folderEditIcon;

    public String getFileEditButtonInFileList() {
        return fileEditButtonInFileList;
    }

    private String fileEditButtonInFileList;

    public String getFileDownloadButtonInPreview() {
        return fileDownloadButtonInPreview;
    }

    private String fileDownloadButtonInPreview;

    public String getCurrentFileNameInPreview() {
        return currentFileNameInPreview;
    }

    private String currentFileNameInPreview;

    public String getFileInfoInPreview() {
        return fileInfoInPreview;
    }

    private String fileInfoInPreview;

    public String getFileInfoPopupInPreview() {
        return fileInfoPopupInPreview;
    }

    private String fileInfoPopupInPreview;

    public String getFileTitleInPreview() {
        return fileTitleInPreview;
    }

    private String fileTitleInPreview;

    public String getFileTextContentInPreview() {
        return fileTextContentInPreview;
    }

    private String fileTextContentInPreview;

    public String getFileMDContentInPreview() {
        return fileMDContentInPreview;
    }

    private String fileMDContentInPreview;

    public String getFileHTMLContentInPreview() {
        return fileHTMLContentInPreview;
    }

    private String fileHTMLContentInPreview;

    public String getFileEditButtonInPreview() {
        return fileEditButtonInPreview;
    }

    private String fileEditButtonInPreview;

    public String getFileRemoveButtonInPreview() {
        return fileRemoveButtonInPreview;
    }

    private String fileRemoveButtonInPreview;

    public String getPrevFileInPreview() {
        return prevFileInPreview;
    }

    private String prevFileInPreview;

    public String getNextFileInPreview() {
        return nextFileInPreview;
    }

    private String nextFileInPreview;

    public String getFileListEditButton() {
        return fileListEditButton;
    }

    private String fileListEditButton;

    public String getFileListEditCancelButton() {
        return fileListEditCancelButton;
    }

    private String fileListEditCancelButton;

    public String getFileListAllSelectedButton() {
        return fileListAllSelectedButton;
    }

    private String fileListAllSelectedButton;

    public String getFileSelectedCountText() {
        return fileSelectedCountText;
    }

    private String fileSelectedCountText;

    public String getFileSelectedSignIcon() {
        return fileSelectedSignIcon;
    }

    private String fileSelectedSignIcon;

    public String getFileNotSelectedSignIcon() {
        return fileNotSelectedSignIcon;
    }

    private String fileNotSelectedSignIcon;

    public String getFileMoveFolderButton() {
        return fileMoveFolderButton;
    }

    private String fileMoveFolderButton;

    public String getFileFolderInMoveFolderList() {
        return fileFolderInMoveFolderList;
    }

    private String fileFolderInMoveFolderList;

    public String getFileSubFolderInMoveFolderList() {
        return fileSubFolderInMoveFolderList;
    }

    private String fileSubFolderInMoveFolderList;

    public String getFileRemoveButtonInFileList() {
        return fileRemoveButtonInFileList;
    }

    private String fileRemoveButtonInFileList;

    public String getFilePreviewIconInPreviewPage() {
        return filePreviewIconInPreviewPage;
    }

    private String filePreviewIconInPreviewPage;

    public String getFolderItemInProjectFileUploadWrapper() {
        return folderItemInProjectFileUploadWrapper;
    }

    private String folderItemInProjectFileUploadWrapper;

    public String getImageItemInProjectFileUploadWrapper() {
        return imageItemInProjectFileUploadWrapper;
    }

    private String imageItemInProjectFileUploadWrapper;

    public String getFileConfirmInProjectFileUploadWrapper() {
        return fileConfirmInProjectFileUploadWrapper;
    }

    private String fileConfirmInProjectFileUploadWrapper;

    public String getFileAddMenu() {
        return fileAddMenu;
    }

    public String getFileUploadZone() {
        return fileUploadZone;
    }

    private String fileUploadZone;
    public DocumentFileReader() {
        documentFileReader =new ConfigFileReader("/DocumentConfig.properties");
        noDefaultFolder = documentFileReader.getValue("noDefaultFolder");
        defaultFolder = documentFileReader.getValue("defaultFolder");
        subFolder = documentFileReader.getValue("subFolder");
        folderHeader = documentFileReader.getValue("folderHeader");
        addSubFolder = documentFileReader.getValue("addSubFolder");
        addFolder = documentFileReader.getValue("addFolder");
        removeIcon = documentFileReader.getValue("removeIcon");
        folderCloseIcon = documentFileReader.getValue("folderCloseIcon");
        folderOpenIcon = documentFileReader.getValue("folderOpenIcon");
        folderNameInput = documentFileReader.getValue("folderNameInput");
        subFolderNameInput = documentFileReader.getValue("subFolderNameInput");
        fileUploadZone = documentFileReader.getValue("fileUpdateZone");
        fileItemInFileList = documentFileReader.getValue("fileItemInFileList");
        fileNameForFileItemInFileList = documentFileReader.getValue("fileNameForFileItemInFileList");
        fileRemoveIcon = documentFileReader.getValue("fileRemoveIcon");
        fileAddButton = documentFileReader.getValue("fileAddButton");
        fileAddMenu = documentFileReader.getValue("fileAddMenu");
        fileTitleInput = documentFileReader.getValue("fileTitleInput");
        filePostfix = documentFileReader.getValue("filePostfix");
        fileContentInput = documentFileReader.getValue("fileContentInput");
        fileAddCancelButton = documentFileReader.getValue("fileAddCancelButton");
        fileAddSaveButton = documentFileReader.getValue("fileAddSaveButton");
        fileDownloadButtonInFileList = documentFileReader.getValue("fileDownloadButtonInFileList");
        folderEditIcon = documentFileReader.getValue("folderEditIcon");
        fileEditButtonInFileList = documentFileReader.getValue("fileEditButtonInFileList");
        fileDownloadButtonInPreview = documentFileReader.getValue("fileDownloadButtonInPreview");
        currentFileNameInPreview = documentFileReader.getValue("currentFileNameInPreview");
        fileInfoInPreview = documentFileReader.getValue("fileInfoInPreview");
        fileInfoPopupInPreview = documentFileReader.getValue("fileInfoPopupInPreview");
        fileTitleInPreview = documentFileReader.getValue("fileTitleInPreview");
        fileTextContentInPreview = documentFileReader.getValue("fileTextContentInPreview");
        fileMDContentInPreview = documentFileReader.getValue("fileMDContentInPreview");
        fileHTMLContentInPreview = documentFileReader.getValue("fileHTMLContentInPreview");
        fileEditButtonInPreview = documentFileReader.getValue("fileEditButtonInPreview");
        fileRemoveButtonInPreview = documentFileReader.getValue("fileRemoveButtonInPreview");
        prevFileInPreview = documentFileReader.getValue("prevFileInPreview");
        nextFileInPreview = documentFileReader.getValue("nextFileInPreview");
        fileListEditButton = documentFileReader.getValue("fileListEditButton");
        fileListEditCancelButton = documentFileReader.getValue("fileListEditCancelButton");
        fileListAllSelectedButton = documentFileReader.getValue("fileListAllSelectedButton");
        fileSelectedCountText = documentFileReader.getValue("fileSelectedCountText");
        fileSelectedSignIcon = documentFileReader.getValue("fileSelectedSignIcon");
        fileNotSelectedSignIcon = documentFileReader.getValue("fileNotSelectedSignIcon");
        fileMoveFolderButton = documentFileReader.getValue("fileMoveFolderButton");
        fileFolderInMoveFolderList = documentFileReader.getValue("fileFolderInMoveFolderList");
        fileSubFolderInMoveFolderList = documentFileReader.getValue("fileSubFolderInMoveFolderList");
        fileRemoveButtonInFileList = documentFileReader.getValue("fileRemoveButtonInFileList");
        filePreviewIconInPreviewPage = documentFileReader.getValue("filePreviewIconInPreviewPage");
        folderItemInProjectFileUploadWrapper = documentFileReader.getValue("folderItemInProjectFileUploadWrapper");
        imageItemInProjectFileUploadWrapper = documentFileReader.getValue("imageItemInProjectFileUploadWrapper");
        fileConfirmInProjectFileUploadWrapper = documentFileReader.getValue("fileConfirmInProjectFileUploadWrapper");
        fileUploadZoneInFileList = documentFileReader.getValue("fileUploadZoneInFileList");
    }



}

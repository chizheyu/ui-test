package org.ChiTest.Code;

import reference.ConfigFileReader;

/**
 * Created by dugancaii on 1/13/2015.
 */
public class CodeFileReader {
    private ConfigFileReader codeFileReader;

    public String getCodeUrlInput() {
        return codeUrlInput;
    }

    private String codeUrlInput;

    public String getCodeButtonInContextMenu() {
        return codeButtonInContextMenu;
    }

    private String codeButtonInContextMenu;

    public String getInnerMenuCodeButton() {
        return innerMenuButton.replace('?','3');
    }
    public String getInnerMenuBranchButton() {
        return innerMenuButton.replace('?','4');
    }
    public String getInnerMenuTagButton() {
        return innerMenuButton.replace('?','5');
    }
    public String getInnerMenuMRButton() {
        return innerMenuButton.replace('?','6');
    }
    public String getInnerMenuNetworkButton() {
        return innerMenuButton.replace('?','7');
    }
    public String getInnerMenuStatisticsButton() {
        return innerMenuButton.replace('?','8');
    }
    public String getInnerMenuReadButton() {
        return innerMenuButton.replace('?','9');
    }

    private String innerMenuButton;

    public String getCodeHeaderInInnerMenu() {
        return codeHeaderInInnerMenu;
    }

    private String codeHeaderInInnerMenu;

    public String getAddNewTagButton() {
        return newAddButton;
    }
    public String getAddNewButton() {
        return newAddButton;
    }

    private String newAddButton;
    public CodeFileReader() {
        codeFileReader = new ConfigFileReader("/codeConfig.properties");
        codeUrlInput = codeFileReader.getValue("codeUrlInput");
        codeButtonInContextMenu = codeFileReader.getValue("codeButtonInContextMenu");
        innerMenuButton = codeFileReader.getValue("innerMenuButton");
        codeHeaderInInnerMenu = codeFileReader.getValue("codeHeaderInInnerMenu");
        newAddButton = codeFileReader.getValue("newAddButton");


    }
}

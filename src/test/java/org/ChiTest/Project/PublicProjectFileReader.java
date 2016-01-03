package org.ChiTest.Project;

import reference.ConfigFileReader;

/**
 * Created by dugancaii on 12/2/2014.
 */
public class PublicProjectFileReader {
    public String getProjectStareIcon() {
        return projectStareIcon;
    }
    public String getProjectStareButton() {
        return projectStareButton;
    }

    public String getProjectWatchButton() {
        return projectWatchButton;
    }

    public String getProjectWatchIcon() {
        return projectWatchIcon;
    }

    private String projectWatchIcon;
    private String projectWatchButton;
    private String projectStareButton;
    private String projectStareIcon;

    public String getProjectDescription() {
        return projectDescription;
    }

    private String projectDescription;
    private ConfigFileReader publicProjectFileReader;

    public PublicProjectFileReader() {
        publicProjectFileReader = new ConfigFileReader("/PublicProjectConfig.properties");
        projectStareButton = publicProjectFileReader.getValue("projectStareButton");
        projectStareIcon = publicProjectFileReader.getValue("projectStareIcon");
        projectWatchButton = publicProjectFileReader.getValue("projectWatchButton");
        projectWatchIcon = publicProjectFileReader.getValue("projectWatchIcon");
        projectDescription = publicProjectFileReader.getValue("projectDescription");
    }
}

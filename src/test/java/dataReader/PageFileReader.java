package dataReader;

import reference.ConfigFileReader;

/**
 * Created by dugancaii on 12/26/2014.
 */
public class PageFileReader {
    private ConfigFileReader fileReader;

    public String getDropDownIconInNavigationBar() {
        return dropDownIconInNavigationBar;
    }

    private String dropDownIconInNavigationBar;
    public PageFileReader(){
        fileReader = new ConfigFileReader("/PageConfig.properties");
        dropDownIconInNavigationBar = fileReader.getValue("dropDownIconInNavigationBar");
    }

}

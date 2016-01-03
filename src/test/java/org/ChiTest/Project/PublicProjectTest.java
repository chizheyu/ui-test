package org.ChiTest.Project;

import org.ChiTest.Page.PS;
import org.ChiTest.Page.Page;

/**
 * Created by dugancaii on 12/2/2014.
 */
public class PublicProjectTest {
    private PublicProjectFileReader publicProjectFileReader;
    public PublicProjectTest(){
        publicProjectFileReader = new PublicProjectFileReader();
    }


    public void watchProject(Page page, Project project){
        page.navigate(project.getProjectLink(), publicProjectFileReader.getProjectDescription());
        if(page.elementIsPresent(publicProjectFileReader.getProjectWatchIcon(), PS.shortWait)) {
            page.clickElement(publicProjectFileReader.getProjectWatchIcon());
            page.waitElementDisappear(publicProjectFileReader.getProjectWatchIcon());
        }
    }
    public void stareProject(Page page, Project project){
        page.navigate(project.getProjectLink(), publicProjectFileReader.getProjectDescription()   );
        if(page.elementIsPresent(publicProjectFileReader.getProjectStareIcon(), PS.shortWait)) {
            page.clickElement(publicProjectFileReader.getProjectStareIcon());
            page.waitElementDisappear(publicProjectFileReader.getProjectStareIcon());
        }
    }
}

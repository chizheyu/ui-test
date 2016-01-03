package org.ChiTest.WebComponent.SearchBox;

import org.ChiTest.Page.Page;

/**
 * Created by dugancaii on 3/14/2015.
 */
public class SearchBox {
    protected String boxSelector;

    public String getBoxInputSelector() {
        return boxInputSelector;
    }

    protected String boxInputSelector;
    protected  String searchResultSelector;
    protected String openBoxButtonSelector;
    public SearchBox(String boxSelector,  String searchResultSelector, String openBoxButtonSelector){
        this.boxSelector = boxSelector;
        this.boxInputSelector = boxSelector + " input";
        this.searchResultSelector = searchResultSelector;
        this.openBoxButtonSelector = openBoxButtonSelector;
    }
    public SearchBox(){

    }
    public void openBox(Page operatorPage){
        operatorPage.clickElement(openBoxButtonSelector, boxSelector);
        operatorPage.assertElementPresent("搜索框无法打开",boxSelector,5);
    }

    public void searchItem(Page operatorPage, String searchString)  {
        String searchText = operatorPage.getText(searchResultSelector);
        operatorPage.clearAndSendKeys(boxInputSelector, searchString);
        operatorPage.waitForElement(searchResultSelector, 5);
        //可能会出现相同，但也没关系，最多等 5 秒
        operatorPage.waitForContentChange(searchResultSelector, searchText, 5);
    }
    public void chooseResult(Page operatorPage){
        operatorPage.clickElement(searchResultSelector);
    }
    public void close(Page operatorPage){
        operatorPage.clickElement(boxSelector +  " .close");
        operatorPage.waitElementInvisible( boxSelector);
        operatorPage.assertElementNotPresent("搜索框无法关闭", boxSelector );
    }
}

package org.ChiTest.WebComponent;

import org.ChiTest.Page.Page;
import org.openqa.selenium.Keys;

/**
 * Created by dugancaii on 3/4/2015.
 */
  public class Inputbox  {
    protected String boxSelector;

    public String getBoxInputSelector() {
        return boxInputSelector;
    }

    protected String boxInputSelector;
    protected String boxSubmitSelector;
    protected String sentItemSelector;
    public Inputbox(String boxSelector){
        this.boxSelector = boxSelector;
    }
    public Inputbox(String boxSelector, String sentItemSelector){
        this.sentItemSelector = sentItemSelector;
        this.boxSelector = boxSelector;
    }
    public void sendContentToInputBox(Page operatorPage,String itemContent)  {
        operatorPage.clearAndSendKeys(boxInputSelector, itemContent);
    }
    public void clickSendButton(Page operatorPage){
        operatorPage.clickElement(boxSubmitSelector);
    }
    public void sendItem(Page operatorPage, String itemContent){
        int itemCount = operatorPage.getElementCount(sentItemSelector);
        operatorPage.clearAndSendKeys(boxInputSelector, itemContent);
        operatorPage.clickElement(boxSubmitSelector);
        operatorPage.waitForItemCountChange(sentItemSelector, itemCount ,15);
        operatorPage.assertCountEquals(boxInputSelector + " 无法发送东西",itemCount + 1, sentItemSelector);
    }
    public void sendItemWithKeyboard(Page page, String itemContent){
        int itemCount = page.getElementCount(sentItemSelector);
        page.sendKeys(boxInputSelector, itemContent);
        page.getBuilder().sendKeys(Keys.CONTROL).sendKeys(Keys.ENTER).perform();
        page.getBuilder().sendKeys(Keys.CONTROL).sendKeys(Keys.ENTER).perform();
        page.waitLoadingIconInvisible();
        page.waitForItemCountChange(sentItemSelector, itemCount, 10);
        page.assertCountEquals(boxInputSelector + " 无法发送东西", itemCount + 1, sentItemSelector);
    }
    public void sendItemWithoutCheck(Page operatorPage, String itemContent){
        operatorPage.clearAndSendKeys(boxInputSelector, itemContent);
        operatorPage.clickElement(boxSubmitSelector);
    }

}

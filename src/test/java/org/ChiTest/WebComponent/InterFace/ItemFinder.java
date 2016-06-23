package org.ChiTest.WebComponent.InterFace;

import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by dugancaii on 10/20/2014.
 */
public interface ItemFinder {
    public abstract int  findItem(List<WebElement> follows, int j, String signContent);
}

package org.ChiTest.InterFace;

import org.ChiTest.User.User;

import java.util.List;

/**
 * Created by dugancaii on 12/11/2014.
 */
public interface ItemEntry {
    public abstract String  getContent();
    public abstract String  getIcon();
    public abstract List<String> getLinkUrls();
    public abstract User getSponsor();
    public abstract String getClassifySelector();
    public abstract int decreaseCount();

}

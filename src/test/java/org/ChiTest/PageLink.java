package org.ChiTest;

import org.ChiTest.User.User;

/**
 * Created by dugancaii on 10/24/2014.
 */
public class PageLink {
    public String getLinkSelector() {
        return linkSelector;
    }

    public String getVerifyInfoInLinkedPage() {
        return verifyInfoInLinkedPage;
    }

    public String getSelectorForVerifyInfoInLinkedPage() {
        return selectorForVerifyInfoInLinkedPage;
    }

    private String linkSelector;
    private String verifyInfoInLinkedPage;
    private String selectorForVerifyInfoInLinkedPage;

    public User getSpecialOwner() {
        return specialOwner;
    }

    private User specialOwner;

    public String getLinkContent() {
        return linkContent;
    }

    private String linkContent;
    public PageLink(String linkSelector, String verifyInfoInLinkedPage, String selectorForVerifyInfoInLinkedPage, String linkContent) {
        this.linkSelector = linkSelector;
        this.verifyInfoInLinkedPage = verifyInfoInLinkedPage;
        this.selectorForVerifyInfoInLinkedPage = selectorForVerifyInfoInLinkedPage;
        this.linkContent = linkContent;
    }
    public PageLink(String linkSelector, String verifyInfoInLinkedPage, String selectorForVerifyInfoInLinkedPage, String linkContent, User specialOwner) {
        this.linkSelector = linkSelector;
        this.verifyInfoInLinkedPage = verifyInfoInLinkedPage;
        this.selectorForVerifyInfoInLinkedPage = selectorForVerifyInfoInLinkedPage;
        this.linkContent = linkContent;
        this.specialOwner = specialOwner;
    }
    public PageLink(String verifyInfoInLinkedPage, String selectorForVerifyInfoInLinkedPage) {
        this.verifyInfoInLinkedPage = verifyInfoInLinkedPage;
        this.selectorForVerifyInfoInLinkedPage = selectorForVerifyInfoInLinkedPage;
    }
}

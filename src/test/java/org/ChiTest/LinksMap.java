package org.ChiTest;

import org.ChiTest.congfig.ConfigureInfo;
import org.ChiTest.Page.Page;
import org.ChiTest.User.User;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.TimeoutException;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by dugancaii on 11/27/2014.
 */
public class LinksMap {
    private static volatile LinksMap linksMap = null;
    public Map<String, PageLink> getLinkMap() {
        return linkMap;
    }

    private  Map<String, PageLink> linkMap;
    public LinksMap(){
        linkMap = new HashMap<String, PageLink>();
    }
    private static Page zombiePage;
    private static User zombieUser;
    private static User ciwangUser;
    private static Cookie ciwangCookie;
    private static Page ciwangPage;
    private String loginUrl;
    private  static Cookie zombieCookie;
    private  static Logger log = Logger.getLogger("linkLog");
    @Before
    public void setUp() throws Exception {
        ConfigureInfo ConfigureInfo = new ConfigureInfo(true, true);
        zombiePage = ConfigureInfo.getZombieUser().getPage();
        zombieUser = ConfigureInfo.getZombieUser();
        ciwangUser = ConfigureInfo.getCiwangUser();
        ciwangPage =  ConfigureInfo.getCiwangUser().getPage();

        zombieUser.autoLogin(ConfigureInfo.getLoginUrl(), zombieCookie);
        ciwangUser.autoLogin(ConfigureInfo.getLoginUrl(), ciwangCookie);
        linksMap = LinksMap.getInstance();
    }
    @Test
    public void linkTest(){
        log.error("link count is " + linksMap.getLinkMap().size());
        linksMap.verifyAllLinkInMaps(zombiePage);
    }

    public static synchronized LinksMap getInstance(){
        if( linksMap == null) {
            linksMap = new LinksMap();
        }
        return linksMap;
    }

    public  void addLink(String linkKey, PageLink pageLink){
        linksMap.getLinkMap().put(linkKey, pageLink);
    }
    public void verifySpecificLink(Page defaultPage, String linkContent){
        Map<String, PageLink> linkMap = linksMap.getLinkMap();
        Page page = defaultPage;
        String errorMessage = "";
        PageLink pageLink = linkMap.get(linkContent);

        if (pageLink.getSpecialOwner() != null) {
            page = pageLink.getSpecialOwner().getPage();
        }
        try {
            page.navigate(linkContent, pageLink.getSelectorForVerifyInfoInLinkedPage());
            page.getWaitTool().waitForContentNotNull(pageLink.getSelectorForVerifyInfoInLinkedPage(), 15);
            if (!page.getText(pageLink.getSelectorForVerifyInfoInLinkedPage()).contains(pageLink.getVerifyInfoInLinkedPage())) {
                errorMessage = "url " + linkContent + " 未能指向指定的页面\n";
            }
        }
        catch (TimeoutException e){
            log.error((new Date())+ " 神马，15秒都刷不出？？！。。。" + linkContent + " title is " + pageLink.getVerifyInfoInLinkedPage());
        }
        linkMap.remove(linkContent);
        assertEquals(errorMessage, errorMessage, "");
    }
    public  void verifyAllLinkInMaps(Page defaultPage) {
        Map<String, PageLink> linkMap = linksMap.getLinkMap();
        Page page = defaultPage;
        Iterator iterator = linkMap.entrySet().iterator();
        String errorMessage = "";
        while (iterator.hasNext()) {
            Map.Entry<String, PageLink> entry = (Map.Entry) iterator.next();
            if (entry.getValue().getSpecialOwner() != null) {
                page = entry.getValue().getSpecialOwner().getPage();
            }

            try {
                page.navigate(entry.getKey(), entry.getValue().getSelectorForVerifyInfoInLinkedPage());
                page.getWaitTool().waitForContentNotNull(entry.getValue().getSelectorForVerifyInfoInLinkedPage(), 15);
                if (!page.getText(entry.getValue().getSelectorForVerifyInfoInLinkedPage()).contains(entry.getValue().getVerifyInfoInLinkedPage())) {
                    errorMessage = "url " + entry.getKey() + " 未能指向指定的页面\n";
                }
            }
            catch (TimeoutException e){
                log.error((new Date())+ " 神马，15秒都刷不出？？！。。。" + entry.getKey() + " title is " + entry.getValue().getVerifyInfoInLinkedPage());
            }
            log.error((new Date())+ "开始检查连接 " + entry.getKey());
            iterator.remove();
            page = defaultPage;
            assertEquals(errorMessage, errorMessage, "");

        }
    }
}

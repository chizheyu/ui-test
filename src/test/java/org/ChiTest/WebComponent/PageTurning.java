package org.ChiTest.WebComponent;

import org.ChiTest.Page.PS;
import org.ChiTest.Page.Page;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: sangzhu.czy
 * Date: 6/23/16
 * Time: 6:08 PM
 */
public class PageTurning {
    public Page page;
    public String activePageSelector;//选中页的选择器
    public String nextPageSelector;//点下一页的选择器
    public String prevPageSelector;//点上一页的选择器
    public String specifiedPageSelector;//点上一页的选择器

    public String getSpecifiedPageSelector(int i){
        return specifiedPageSelector.replace("?",Integer.toString(i));
    }








    public void testPageTurning(String url){
        page.navigate(url);
        if( !page.elementIsPresent(activePageSelector, PS.midWait)
                || page.getElements(activePageSelector).size()<3 ){ //只有当前页,上翻和下翻按钮,所以无页可翻
            System.out.println("该页面无页可翻 url"+ url);
            return;
        }

        assertEquals("url "+ url + " 进入翻页页面显示的页数不是第一页", page.getText(activePageSelector),"1");
        page.clickElement(nextPageSelector);
        page.getWaitTool().waitForJavaScriptCondition("return $('"+activePageSelector+"').text() == 2",3);
        assertEquals("url "+ url + " 无法翻页到下一页", page.getText(activePageSelector),"2" );
        page.clickElement(prevPageSelector);
        page.getWaitTool().waitForJavaScriptCondition("return $('"+activePageSelector+"').text() == 1",3);
        assertEquals("url "+ url + " 无法翻页到前一页", page.getText(activePageSelector),"1" );
        page.clickElement(getSpecifiedPageSelector(2));
        page.getWaitTool().waitForJavaScriptCondition("return $('"+activePageSelector+"').text() == 2",3);
        assertEquals("url "+ url + " 无法翻页到指定页数", page.getText(activePageSelector),"2" );
    }












}

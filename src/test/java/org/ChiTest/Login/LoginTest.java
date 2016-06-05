package org.ChiTest.Login;


import org.ChiTest.congfig.ConfigureInfo;
import org.ChiTest.Page.Page;
import org.ChiTest.User.User;
import org.ChiTest.constans.RCHttpJsonResultObject;
import org.ChiTest.constans.RequestMethod;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.Map;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginTest {
    private LoginFileReader loginFileReader;
    private User zombieUser;
    private Page loginPage;
    private ConfigureInfo configureInfo;


    @Before
    public void setUp() throws Exception {
        configureInfo = new ConfigureInfo(false, true);
        loginFileReader = new LoginFileReader();
        zombieUser = configureInfo.getZombieUser();
        loginPage = zombieUser.getPage();

    }

    @Test
    public void test01_do_post_and_get() throws Exception  {
        //登录小二工作台
        loginPage.navigate( configureInfo.getLoginUrl(), loginFileReader.getLoginNameInput()  );
        loginPage.sendKeys( loginFileReader.getLoginNameInput() ,zombieUser.getUserLoginName());
        loginPage.sendKeys( loginFileReader.getLoginPasswordInput(), zombieUser.getUserPassword());
        loginPage.clickElement( loginFileReader.getLoginButton(), loginFileReader.getWorkEnum()) ;
        loginPage.assertElementPresent("登入成功",loginFileReader.getWorkEnum() );


        // post
        Map<String ,String> dataMap = new HashMap<>() ;
        dataMap.put("newAlias", "我是一个POST");
        dataMap.put("resId", "1");

        String responseString = loginPage.sendRequest( dataMap ,RequestMethod.POST.getName(),loginFileReader.getPostUrl());
        RCHttpJsonResultObject rcHttpJsonResultObject = (RCHttpJsonResultObject) loginPage.getConstructObjectFromJason(responseString, RCHttpJsonResultObject.class);

        loginPage.assertPostTrue(
                rcHttpJsonResultObject,
                loginFileReader.getPostUrl()
        );

        // get
        String getString = loginPage.sendRequest( null ,RequestMethod.GET.getName(),loginFileReader.getGetUrl());
        rcHttpJsonResultObject = (RCHttpJsonResultObject) loginPage.getConstructObjectFromJason(getString, RCHttpJsonResultObject.class);
        loginPage.assertGetTrue(rcHttpJsonResultObject, loginFileReader.getPostUrl());

        JSONObject dataJasonObjForGet = new JSONObject("{\"code\":\"WLB_LADYGO\",\"text\":\"俪人购物流服务\",\"value\":5000000000011}");


        loginPage.assertDataExistInJason("text",  dataJasonObjForGet, new JSONObject( getString));

        System.out.println();
    }


    @After
    public void tearDown() throws Exception {
      loginPage.getDriver().quit();
    }
   }

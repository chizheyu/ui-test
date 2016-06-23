package org.ChiTest.rc.Login;

import org.ChiTest.congfig.ConfigFileReader;

/**
 * Created with IntelliJ IDEA.
 * User: sangzhu.czy
 * Date: 6/4/16
 * Time: 6:30 PM
 */
public class LoginFileReader {


    public ConfigFileReader configFileReader;

    public String loginNameInput;

    public String getLoginPasswordInput() {
        return loginPasswordInput;
    }

    public void setLoginPasswordInput(String loginPasswordInput) {
        this.loginPasswordInput = loginPasswordInput;
    }

    public String getLoginButton() {
        return loginButton;
    }

    public void setLoginButton(String loginButton) {
        this.loginButton = loginButton;
    }

    public String getLoginNameInput() {
        return loginNameInput;
    }

    public void setLoginNameInput(String loginNameInput) {
        this.loginNameInput = loginNameInput;
    }

    public String loginPasswordInput;
    public String loginButton;

    public String getWorkEnum() {
        return workEnum;
    }

    public void setWorkEnum(String workEnum) {
        this.workEnum = workEnum;
    }

    public String workEnum;


    public LoginFileReader(){
        configFileReader = new ConfigFileReader("/rc/login.properties");
        loginNameInput = configFileReader.getValue("loginNameInput");
        loginPasswordInput= configFileReader.getValue("loginPasswordInput");
        loginButton = configFileReader.getValue("loginButton");
        workEnum = configFileReader.getValue("workEnum");
    }


    public String getPostUrl(){
        return "http://rc.daily.cainiao-inc.net/console/servicemanage/modifyResourceAlias.do?_input_charset=utf-8";
    }
    public String getGetUrl(){
        return "http://rc.daily.cainiao-inc.net/console/verify/sub_divided_two_parts.do?type=view&_input_charset=utf-8";
    }





}

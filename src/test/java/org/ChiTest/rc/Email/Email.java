package org.ChiTest.rc.Email;

/**
 * Created by dugancaii on 7/29/2014.
 */
public class Email {
    private String emailUrl;
    private String emailInput;
    private String passwordInput;
    private String loginButton;
    private String emailName;
    private String postfix;//和用户名绑定的后缀
    private String password;
    //private List<String>
    public Email(){
        this.emailUrl = "http://email.163.com/";
        this.emailInput = ".user input";
        this.passwordInput = ".user input";
        this.loginButton  = ".pass input";
        this.emailName = "coding_test19";
        this. postfix = "wangyi163_-19";
        this.password = "chi2014";
    }
    public Email(String emailName, String postfix, String password) {
        this.emailUrl = "http://mail.163.com/";
        this.emailInput = "#idInputLine input";
        this.passwordInput = "#pwdInputLine input";
        this.loginButton  = "#loginBtn ";
        this.emailName = emailName;
        this.postfix = postfix;
        this.password = password;
    }
    public Email(String emailUrl,  String emailInput, String passwordInput, String loginButton, String emailName, String postfix,String password){
        this.emailUrl = emailUrl;
        this.emailInput = emailInput;
        this.passwordInput = passwordInput;
        this.loginButton  = loginButton;
        this.emailName = emailName;
        this.postfix = postfix;
        this.password = password;
    }

    public String getEmailUrl() {
        return emailUrl;
    }


    public String getEmailName() {
        return emailName;
    }



    public String getPostfix() {
        return postfix;
    }



    public String getPassword() {
        return password;
    }


}

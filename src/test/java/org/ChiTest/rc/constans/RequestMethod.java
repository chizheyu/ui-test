package org.ChiTest.rc.constans;

/**
 * Created with IntelliJ IDEA.
 * User: sangzhu.czy
 * Date: 6/5/16
 * Time: 10:25 AM
 */
public enum RequestMethod {
    POST( "POST"),
    GET( "GET");

    RequestMethod(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
}

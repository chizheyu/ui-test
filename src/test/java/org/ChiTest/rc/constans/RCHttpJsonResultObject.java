package org.ChiTest.rc.constans;

/**
 * Created with IntelliJ IDEA.
 * User: sangzhu.czy
 * Date: 6/5/16
 * Time: 11:23 AM
 */
public class RCHttpJsonResultObject {

    public RCHttpJsonResultObject() {

    }

    public RCHttpJsonResultObject(String success, String errorCode, String data, String errorMsg) {
        this.success = success;
        this.errorCode = errorCode;
        this.data = data;
        this.errorMsg = errorMsg;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String success;
    public String errorCode;
    public String data;
    public String errorMsg;

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String errMsg;



}

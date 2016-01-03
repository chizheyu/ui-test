package org.ChiTest.Setting;

import reference.ConfigFileReader;

/**
 * Created by dugancaii on 10/16/2014.
 */
public class SettingFileReader {
    private ConfigFileReader settingFileReader;

    public String getNameInput() {
        return nameInput;
    }

    public String getAvatarEditButton() {
        return avatarEditButton;
    }

    private String avatarEditButton;
    private String nameInput;

    public String getFemaleInput() {
        return femaleInput;
    }

    private String femaleInput;

    public String getMaleInput() {
        return maleInput;
    }

    private String maleInput;

    public String getSloganInput() {
        return sloganInput;
    }

    private String sloganInput;

    public String getLocationInput() {
        return locationInput;
    }

    private String locationInput;

    public String getCompanyInput() {
        return companyInput;
    }

    private String companyInput;

    public String getBirthdayInput() {
        return birthdayInput;
    }

    private String birthdayInput;

    public String getPhoneInput() {
        return phoneInput;
    }

    private String phoneInput;

    public String getNameInputForJquery() {
        return nameInputForJquery;
    }

    private String nameInputForJquery;

    public String getCurrentPasswordInput() {
        return currentPasswordInput;
    }

    private String currentPasswordInput;

    public String getPasswordSetting() {
        return passwordSetting;
    }

    private String passwordSetting;

    public String getPersonalSetting() {
        return personalSetting;
    }

    private String personalSetting;

    public String getSubmitButton() {
        return submitButton;
    }

    private String submitButton;

    public String getAvatarInputZone() {
        return avatarInputZone;
    }

    private String avatarInputZone;

    public String getJcropHline() {
        return jcropHline;
    }

    private String jcropHline;

    public String getAvatarCancelEditButton() {
        return avatarCancelEditButton;
    }

    private String avatarCancelEditButton;

    public String getAvatarSaveConfirmButton() {
        return avatarSaveConfirmButton;
    }

    private String avatarSaveConfirmButton;

    public String getAvatarImg() {
        return avatarImg;
    }

    private String avatarImg;

    public String getFruitAvatar(int num) {
        return fruitAvatar +":nth-child("+ num +")";
    }

    private String fruitAvatar;

    public String getAvatarPreview() {
        return avatarPreview;
    }

    private String avatarPreview;

    public String getGravatarSyncButton() {
        return gravatarSyncButton;
    }

    private String gravatarSyncButton;

    public String getGravatarSyncTitle() {
        return gravatarSyncTitle;
    }

    private String gravatarSyncTitle;

    public String getGravatarSyncPreview() {
        return gravatarSyncPreview;
    }

    private String gravatarSyncPreview;

    public String getNotificationSettingShowMore() {
        return notificationSettingShowMore;
    }

    private String notificationSettingShowMore;

    public String getNotificationSettingItem() {
        return notificationSettingItem;
    }

    private String notificationSettingItem;
    public SettingFileReader(){
        settingFileReader = new ConfigFileReader("/SettingConfig.properties");
        gravatarSyncTitle = settingFileReader.getValue("gravatarSyncTitle");
        gravatarSyncButton = settingFileReader.getValue("gravatarSyncButton");
        gravatarSyncPreview = settingFileReader.getValue("gravatarSyncPreview");
        nameInput= settingFileReader.getValue("nameInput");
        submitButton= settingFileReader.getValue("submitButton");
        passwordSetting= settingFileReader.getValue("passwordSetting");
        currentPasswordInput = settingFileReader.getValue("currentPasswordInput");
        personalSetting = settingFileReader.getValue("personalSetting");
        nameInputForJquery = settingFileReader.getValue("nameInputForJquery");
        phoneInput = settingFileReader.getValue("phoneInput");
        companyInput = settingFileReader.getValue("companyInput");
        birthdayInput = settingFileReader.getValue("birthdayInput");
        locationInput = settingFileReader.getValue("locationInput");
        sloganInput = settingFileReader.getValue("sloganInput");
        maleInput = settingFileReader.getValue("maleInput");
        femaleInput = settingFileReader.getValue("femaleInput");
        avatarEditButton = settingFileReader.getValue("avatarEditButton");
        avatarInputZone = settingFileReader.getValue("avatarInputZone");
        jcropHline = settingFileReader.getValue("jcropHline");
        avatarCancelEditButton = settingFileReader.getValue("avatarCancelEditButton");
        avatarPreview = settingFileReader.getValue("avatarPreview");
        avatarSaveConfirmButton = settingFileReader.getValue("avatarSaveConfirmButton");
        avatarImg = settingFileReader.getValue("avatarImg");
        fruitAvatar = settingFileReader.getValue("theFruitAvatar");
        notificationSettingShowMore = settingFileReader.getValue("notificationSettingShowMore");
        notificationSettingItem = settingFileReader.getValue("notificationSettingItem");
    }
}

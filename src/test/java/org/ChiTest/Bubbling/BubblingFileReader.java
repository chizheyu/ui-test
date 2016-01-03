package org.ChiTest.Bubbling;

import reference.ConfigFileReader;

/**
 * Created by dugancaii on 10/20/2014.
 */
public class BubblingFileReader {
    public ConfigFileReader configFileReader;
    public String getBubblingTextContent() {
        return bubblingTextContent;
    }
    public String getBubblingItem() {
        return bubblingItem;
    }

    private String bubblingTextContent;
    private String bubblingItem;

    public String getUserHoverCard() {
        return userHoverCard;
    }

    private String userHoverCard;

    public String getFirstUserHoverCardInfo() {
        return firstUserHoverCardInfo;
    }

    private String firstUserHoverCardInfo;

    public String getUserNameInHoverCard() {
        return userNameInHoverCard;
    }

    private String userNameInHoverCard;

    public String getUserAvatarImgInHoverCard() {
        return userAvatarImgInHoverCard;
    }

    private String userAvatarImgInHoverCard;

    public String getSmallButtonInHoverCard() {
        return smallButtonInHoverCard;
    }

    private String smallButtonInHoverCard;

    public String getImageInBubbling() {
        return imageInBubbling;
    }

    private String imageInBubbling;

    public String getGalleryImage() {
        return galleryImage;
    }

    private String galleryImage;

    public String getNextImageIconInGallery() {
        return nextImageIconInGallery;
    }

    private String nextImageIconInGallery;

    public String getBubblingCommentCount() {
        return bubblingCommentCount;
    }

    private String bubblingCommentCount;

    public String getBubblingFavorCount() {
        return bubblingFavorCount;
    }

    private String bubblingFavorCount;
    public BubblingFileReader(){
        configFileReader = new ConfigFileReader("/BubblingConfig.properties");
        this.bubblingTextContent = configFileReader.getValue("BubblingTextContent");
        this.bubblingItem = configFileReader.getValue("bubblingItem");
        this.userHoverCard = configFileReader.getValue("userHoverCard");
        this.firstUserHoverCardInfo = configFileReader.getValue("firstUserHoverCardInfo");
        this.userNameInHoverCard = configFileReader.getValue("userNameInHoverCard");
        this.userAvatarImgInHoverCard = configFileReader.getValue("userAvatarImgInHoverCard");
        this.smallButtonInHoverCard = configFileReader.getValue("smallButtonInHoverCard");
        this.imageInBubbling = configFileReader.getValue("imageInBubbling");
        this.galleryImage = configFileReader.getValue("galleryImage");
        this.nextImageIconInGallery = configFileReader.getValue("nextImageIconInGallery");
        this.bubblingCommentCount = configFileReader.getValue("bubblingCommentCount");
        this.bubblingFavorCount = configFileReader.getValue("bubblingFavorCount");
    }
}

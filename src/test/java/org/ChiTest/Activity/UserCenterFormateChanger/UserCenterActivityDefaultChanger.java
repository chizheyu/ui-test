package org.ChiTest.Activity.UserCenterFormateChanger;

import org.ChiTest.Activity.Activity;
import org.ChiTest.User.User;

/**
 * Created by dugancaii on 4/22/2015.
 */
public class UserCenterActivityDefaultChanger implements UserCenterActivityChanger {


    @Override
    public void changeToUserCenterFormat(Activity activity) {
        User sponsor = activity.getSponsor();
        activity.setContent(activity.getContent().replace(sponsor.getUserName()+" " ,
                sponsor.getUserName() + " 在项目 " + activity.getProject().getProjectFullName() + " 中 "));
        activity.getLinkUrls().add(1, activity.getProject().getProjectLink());
    }

}

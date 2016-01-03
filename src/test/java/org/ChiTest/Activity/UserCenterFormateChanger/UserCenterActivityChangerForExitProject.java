package org.ChiTest.Activity.UserCenterFormateChanger;

import org.ChiTest.Activity.Activity;

/**
 * Created by dugancaii on 4/22/2015.
 */
public class UserCenterActivityChangerForExitProject implements UserCenterActivityChanger {
    public UserCenterActivityChangerForExitProject(){
    }
    @Override
    public void changeToUserCenterFormat(Activity activity) {
        activity.setContent(activity.getContent().replace( "退出了 ","退出了 " +activity.getProject().getProjectFullName()+" "));
        activity.getLinkUrls().add(1, activity.getProject().getProjectLink());
    }
}

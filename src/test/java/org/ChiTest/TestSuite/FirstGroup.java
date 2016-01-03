package org.ChiTest.TestSuite;

/**
 * Created by dugancaii on 3/31/2015.
 */

import org.ChiTest.Activity.ActivityTest;
import org.ChiTest.Friend.FriendTest;
import org.ChiTest.Login.UnregisterTest;
import org.ChiTest.Message.MessageTest;
import org.ChiTest.Notification.NotificationTest;
import org.ChiTest.Project.GroupTest;
import org.ChiTest.Project.ProjectOwnerTest;
import org.ChiTest.Setting.SettingTest;
import org.ChiTest.User.PersonalHomePageTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by dugancaii on 9/11/2014.
 */
@RunWith(org.junit.runners.Suite.class )
//@Suite.SuiteClasses(value = {ProjectTest.class
//,MemberTest.class,EmailTest.class})
@Suite.SuiteClasses(value = {UnregisterTest.class,MessageTest.class, PersonalHomePageTest.class, SettingTest.class,
        NotificationTest.class, ActivityTest.class,GroupTest.class,FriendTest.class,ProjectOwnerTest.class })
public class FirstGroup {
}

package org.ChiTest.TestSuite;

import org.ChiTest.Activity.ActivityTest;
import org.ChiTest.AdvancedTask.AdvancedTaskTest;
import org.ChiTest.AdvancedTask.TaskBaseTest;
import org.ChiTest.Bubbling.BubblingTest;
import org.ChiTest.Document.DocumentTest;
import org.ChiTest.Friend.FriendTest;
import org.ChiTest.Login.UnregisterTest;
import org.ChiTest.MemberTest;
import org.ChiTest.Message.MessageTest;
import org.ChiTest.Notification.NotificationTest;
import org.ChiTest.Project.GroupTest;
import org.ChiTest.Project.PinTest;
import org.ChiTest.Project.ProjectOwnerTest;
import org.ChiTest.Project.ProjectTest;
import org.ChiTest.Reference.ReferenceTest;
import org.ChiTest.Setting.SettingTest;
import org.ChiTest.Topic.TopicTagTest;
import org.ChiTest.Topic.TopicTest;
import org.ChiTest.User.PersonalHomePageTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by dugancaii on 9/11/2014.
 */
@RunWith(org.junit.runners.Suite.class )
//@Suite.SuiteClasses(value = {ProjectTest.class
        //,MemberTest.class,EmailTest.class})
@Suite.SuiteClasses(value = {UnregisterTest.class,MessageTest.class, PersonalHomePageTest.class, SettingTest.class, BubblingTest.class,FriendTest.class,  ProjectTest.class,
       TopicTest.class, TopicTagTest.class, ProjectOwnerTest.class,
       NotificationTest.class,GroupTest.class,MemberTest.class, DocumentTest.class, ActivityTest.class,AdvancedTaskTest.class, TaskBaseTest.class, ReferenceTest.class, PinTest.class})
public class TestAllSuite {
}

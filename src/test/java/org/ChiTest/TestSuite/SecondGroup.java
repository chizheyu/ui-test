package org.ChiTest.TestSuite;

/**
 * Created by dugancaii on 3/31/2015.
 */
import org.ChiTest.AdvancedTask.AdvancedTaskTest;
import org.ChiTest.AdvancedTask.TaskBaseTest;
import org.ChiTest.Bubbling.BubblingTest;
import org.ChiTest.Document.DocumentTest;
import org.ChiTest.MemberTest;
import org.ChiTest.Project.PinTest;
import org.ChiTest.Project.ProjectTest;
import org.ChiTest.Reference.ReferenceTest;
import org.ChiTest.Topic.TopicTagTest;
import org.ChiTest.Topic.TopicTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by dugancaii on 9/11/2014.
 */
@RunWith(org.junit.runners.Suite.class )
//@Suite.SuiteClasses(value = {ProjectTest.class
//,MemberTest.class,EmailTest.class})
@Suite.SuiteClasses(value = { TopicTest.class,TaskBaseTest.class,AdvancedTaskTest.class ,
        MemberTest.class, DocumentTest.class,BubblingTest.class,ProjectTest.class,TopicTagTest.class, ReferenceTest.class, PinTest.class})
public class SecondGroup {
}

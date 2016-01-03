package org.ChiTest.TestSuite;

import org.ChiTest.Bubbling.BaseBubbling;
import org.ChiTest.Bubbling.BubblingTest;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by dugancaii on 3/9/2015.
 */


@RunWith(Categories.class )
//@Suite.SuiteClasses(value = {ProjectTest.class
//,MemberTest.class,EmailTest.class})
@Suite.SuiteClasses(value = { BubblingTest.class })
@Categories.IncludeCategory(BaseBubbling.class)
public class TestCategory {
}

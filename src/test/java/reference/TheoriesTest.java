package reference;

import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assume.assumeThat;

/**
 * Created by dugancaii on 3/9/2015.
 */
@RunWith(Theories.class)
public class TheoriesTest {
    /*
    @DataPoint
    public static String nameValue1 = "Tony";

    @DataPoint
    public static String nameValue2 = "Jim";
    @DataPoint
    public static int ageValue2 = 20;

    @DataPoint
    public static int ageValue1 = 10;
    */
    @DataPoints
    public static String[] names = {"Tony","Jim"};
    @DataPoints
    public static String[] gender = {"Male","Female"};

    @DataPoints
    public static int[] ageValue = {10, 20};

    @Theory
    public void testMethod(String name, int age)
    {
        System.out.println(String.format("%s's age is %s are %s", name, age,name));
    }

    @DataPoint
    public static String GOOD_USERNAME = "optimus";
    @DataPoint
    public static String USERNAME_WITH_SLASH = "optimus/prime";

    @Theory
    public void filenameIncludesUsername(String username) {
        assumeThat(username, not(containsString("/")));

    }
}

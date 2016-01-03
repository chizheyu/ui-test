package reference;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

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
    public void testMethod(String name, int age, String gender)
    {
        System.out.println(String.format("%s's age is %s are %s", name, age,gender));
    }
}

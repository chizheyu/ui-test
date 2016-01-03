package reference;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;


@RunWith(Parameterized.class)
public class TestSuite {
    @Parameterized.Parameters(name = "{index}:{0} = {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { 0, 0 }, { 1, 1 }, { 2, 3}, { 3, 3 }, { 4, 4 }, { 5, 5 },{ 6, 6 }
        });
    }
    private int fInput;
    private int fExpected;

    public TestSuite(int input, int expected) {
        fInput= input;
        fExpected= expected;
    }


    @Test
    public void test() {
        System.out.println(fExpected);
        System.out.println(fInput);
        assertEquals(fExpected, fInput);
    }
}
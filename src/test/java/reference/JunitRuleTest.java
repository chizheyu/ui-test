package reference;

import org.hamcrest.BaseMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Created by dugancaii on 3/9/2015.
 */
public class JunitRuleTest {
    @Rule
    public ExpectedException exp = ExpectedException.none();



    @Test
    public void expectException(){
        exp.expect(IndexOutOfBoundsException.class);
        throw new IndexOutOfBoundsException("Exception method.");
    }
    @Test
    public void expectMessage(){
        exp.expectMessage("Hello World");
        throw new RuntimeException("Hello World will throw exception");
    }
    @Test
    public void expectCourse(){
        exp.expectCause(new BaseMatcher<IllegalArgumentException>()
        {

            public boolean matches(Object item)
            {
                return item instanceof IllegalArgumentException;
            }


            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.appendText("Expected Cause Error.");
            }
        });

        Throwable cause = new IllegalArgumentException("Cause Test.");
        throw new RuntimeException(cause);

    }






}

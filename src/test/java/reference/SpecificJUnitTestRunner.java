package reference;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;

/**
 * Created by dugancaii on 9/17/2014.c
 */
public class SpecificJUnitTestRunner {
    public static void main(String[] args) throws ClassNotFoundException {
        String[] classAndMethod = args[0].split("#");
        Request request;
        Result result;
        for(int i = 1; i < args.length; i++) {
            request = Request.method(Class.forName(classAndMethod[0]), classAndMethod[i]);
            result = new JUnitCore().run(request);
            System.out.println("the result is successful? " + result.wasSuccessful());
        }
       // System.exit(result.wasSuccessful() ? 0 : 1);
    }
}

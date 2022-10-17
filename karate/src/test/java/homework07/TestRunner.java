package homework07;

import com.intuit.karate.junit5.Karate;

public class TestRunner {
    @Karate.Test
    public Karate runTest(){
        return Karate.run("bookingParameters.feature").relativeTo(getClass());
    }
}

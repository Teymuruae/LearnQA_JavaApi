package lib;

import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;

public class AssertionsOwn {

    public static void checkIntValue(Response response, String jsonKey, int expectedValue){
        response.then().assertThat().body("$", Matchers.hasKey(jsonKey));
        int actualValue = response.path(jsonKey);

        Assertions.assertEquals(expectedValue, actualValue);
    }
}

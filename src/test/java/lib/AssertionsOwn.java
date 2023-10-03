package lib;

import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class AssertionsOwn {

    public static void checkIntValue(Response response, String jsonKey, int expectedValue) {
        response.then().assertThat().body("$", Matchers.hasKey(jsonKey));
        int actualValue = response.path(jsonKey);

        Assertions.assertEquals(expectedValue, actualValue);
    }

    public static void checkStringValue(Response response, String jsonKey, String expectedValue) {
        response.then().assertThat().body("$", Matchers.hasKey(jsonKey));
        String actualValue = response.path(jsonKey);

        Assertions.assertEquals(expectedValue, actualValue);
    }

    public static void assertAnswer(Response response, String expectedResult) {
        String actualResult = response.asString();
        Assertions.assertEquals(expectedResult, actualResult);
    }

    public static void assertStatusCode(Response response, int extectedCode) {
        int actualStatusCode = response.statusCode();
        Assertions.assertEquals(extectedCode, actualStatusCode);
    }

    public static void assertJsonHasKey(Response response, String key) {
        response.then().assertThat().body("$", Matchers.hasKey(key));
    }

    public static void assertJsonHasNotKeys(Response response, List<String> keys) {
        for (String key : keys) {
            response.then().assertThat().body("$", Matchers.not(Matchers.hasKey(key)));
        }
    }

    public static void assertJsonHasKeys(Response response, List<String> keys) {
        for (String key : keys) {
            response.then().assertThat().body("$", Matchers.hasKey(key));
        }
    }

    public static void assertHtmlBody(Response response, String expectedBody){
        Assertions.assertEquals(response.htmlPath().getString("body"), expectedBody);
    }

    public static void assertJsonValueNotNull(Response response, String key){
        response.then().assertThat().body(key, Matchers.notNullValue());
    }
}
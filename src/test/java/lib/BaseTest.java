package lib;

import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;

import java.util.Map;

public class BaseTest {

    public String getCookie(Response response, String name){
        Map<String, String> cookies = response.getCookies();
        Assertions.assertTrue(cookies.containsKey(name));
        return cookies.get(name);
    }

    public String getHeader(Response response, String name){
        Headers headers = response.getHeaders();
        Assertions.assertTrue(headers.hasHeaderWithName(name));
        return headers.getValue(name);
    }

    public int getIntValue(Response response, String name){
        response.then().body("$", Matchers.hasKey(name));
        return response.jsonPath().getInt(name);
    }
}

import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.AssertionsOwn;
import lib.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserGetTest extends BaseTest {


    private String
            url = "https://playground.learnqa.ru/api/user/1",
            username = "username",
            firstname = "firstName",
            lastname = "lastName",
            email = "email";

    @Owner("Teymur")
    @Test
    void getUserNotAuthTest() {
        Response response = RestAssured
                .get(url);
        response.print();
        List<String> keys = Arrays.asList(firstname, lastname, email);
        AssertionsOwn.assertJsonHasKey(response, username);
        AssertionsOwn.assertJsonHasNotKeys(response, keys);
    }

    @Owner("Teymur")
    @Test
    void getUserAuthTest() {
        String
                cookieKey = "auth_sid",
                headerKey = "x-csrf-token";
        Map<String, String> body = new HashMap<>() {{
            put("email", "vinkotov@example.com");
            put("password", "1234");
        }};

        List<String> responseKeys = List.of(username, firstname, lastname, email);
        Response authResponse = RestAssured
                .given()
                .body(body)
                .when()
                .post("https://playground.learnqa.ru/api/user/login");

        String cookieValue = getCookie(authResponse, cookieKey);
        String headerValue = getHeader(authResponse, headerKey);

        Response response = RestAssured
                .given()
                .cookie(cookieKey, cookieValue)
                .header(headerKey, headerValue)
                .get(url);
        response.then().log().all();
        AssertionsOwn.assertStatusCode(response, 200);
        AssertionsOwn.assertJsonHasKeys(response, responseKeys);
    }
}

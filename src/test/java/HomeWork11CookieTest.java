import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class HomeWork11CookieTest {

    private String
            url = "https://playground.learnqa.ru/api/homework_cookie",
            cookieKey = "HomeWork",
            cookieValue = "hw_value";

    @Test
    void cookieTest() {
        Response response = RestAssured
                .get(url);

        Map<String, String> cookies = response.getCookies();
        Assertions.assertTrue(cookies.containsKey(cookieKey), "Response hasn't cookie 'HomeWork'");

        String actualCookie = response.getCookie(cookieKey);
        String expectedCookie = cookieValue;
        Assertions.assertEquals(expectedCookie, actualCookie, "response cookie is not equals 'hw_value'");
    }
}
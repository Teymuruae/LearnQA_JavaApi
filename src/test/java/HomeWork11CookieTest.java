import io.qameta.allure.Epic;
import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

@Epic("Cookie")
public class HomeWork11CookieTest {

    private String
            url = "https://playground.learnqa.ru/api/homework_cookie",
            cookieKey = "HomeWork",
            cookieValue = "hw_value";

    @Owner("Teymur")
    @DisplayName("Cookie test")
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
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Map;

public class HomeWork11CookieTest {

    @Test
    void cookieTest() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .then().log().all().extract().response();
        Map<String, String> cookies = response.getCookies();
        Assertions.assertTrue(cookies.containsKey("HomeWork"), "Response hasn't cookie 'HomeWork'");

        String actualCookie = response.getCookie("HomeWork");
        String expectedCookie = "hw_value";
        System.out.println(actualCookie);
        Assertions.assertEquals(expectedCookie, actualCookie, "response cookie is not equals 'hw_value'");
    }
}
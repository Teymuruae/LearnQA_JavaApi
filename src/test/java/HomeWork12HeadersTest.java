import io.restassured.RestAssured;
import io.restassured.http.Headers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HomeWork12HeadersTest {

    private String
            headerKey = "x-secret-homework-header",
            headerValue = "Some secret value",
            url = "https://playground.learnqa.ru/api/homework_header";

    @Test
    void headersTest() {
        Headers headers = RestAssured
                .get(url).getHeaders();

        Assertions.assertTrue(headers.hasHeaderWithName(headerKey));
        Assertions.assertEquals(headerValue, headers.getValue(headerKey));
    }
}
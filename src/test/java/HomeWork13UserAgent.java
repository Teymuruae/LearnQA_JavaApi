import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class HomeWork13UserAgent {

    private String
            url = "https://playground.learnqa.ru/ajax/api/user_agent_check",
            userAgent = "user_agent",
            platform = "platform",
            browser = "browser",
            device = "device";

    private void checkBody(Response response, String expectedUserAgent, String expectedPlatform, String expectedBrowser,
                           String expectedDevice) {
        String actualUserAgent = response.path(userAgent);
        String actualPlatform = response.path(platform);
        String actualBrowser = response.path(browser);
        String actualDevice = response.path(device);

        Assertions.assertEquals(expectedUserAgent, actualUserAgent);
        Assertions.assertEquals(expectedPlatform, actualPlatform);
        Assertions.assertEquals(expectedBrowser, actualBrowser);
        Assertions.assertEquals(expectedDevice, actualDevice);
    }

    static Stream<Arguments> userAgentTest() {
        return Stream.of(
                Arguments.of("Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) " +
                        "AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30", "Mobile", "No", "Android"),
                Arguments.of("Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)" +
                        " CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1", "Mobile", "Chrome", "iOS"),
                Arguments.of("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)", "Googlebot", "Unknown", "Unknown"),
                Arguments.of("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0", "Web", "Chrome", "No"),
                Arguments.of("Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 " +
                        "(KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1", "Mobile", "No", "iPhone")
        );
    }

    @ParameterizedTest
    @MethodSource
    void userAgentTest(String userAgent, String platform, String browser, String device) {
        Response response = RestAssured
                .given()
                .header("User-Agent", userAgent)
                .get(url)
                .then().log().all()
                .extract().response();

        checkBody(response, userAgent, platform, browser, device);
    }
}
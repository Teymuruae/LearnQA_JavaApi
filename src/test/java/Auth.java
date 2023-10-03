import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import jdk.jfr.Description;
import lib.ApiCoreRequests;
import lib.AssertionsOwn;
import lib.BaseTest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

@Issue("AUTH-567")
@Epic("Auth cases")
@Feature("Auth")
public class Auth extends BaseTest {

    private int userId;
    private String
            cookie,
            header,
            authUrl = "https://playground.learnqa.ru/api/user/auth";

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @BeforeEach
    void setUp() {
        Map<String, String> body = new HashMap<>() {{
            put("email", "vinkotov@example.com");
            put("password", "1234");
        }};

        Response response = RestAssured
                .given()
                .body(body)
                .when()
                .post("https://playground.learnqa.ru/api/user/login")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().response();

        this.userId = this.getIntValue(response, "user_id");
        this.cookie = this.getCookie(response, "auth_sid");
        this.header = this.getHeader(response, "x-csrf-token");
    }

    @Owner("Teymur")
    @Description("Auth test with cookie and token")
    @DisplayName("Auth positive test")
    @Test
    void positiveAuth() {

        Map<String, String> authCookie = new HashMap<>() {{
            put("auth_sid", cookie);
        }};
        Response authResponse = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/auth",
                this.cookie, this.header);

        AssertionsOwn.checkIntValue(authResponse, "user_id", userId);
    }

    @Owner("Teymur")
    @Test
    void negativeAuth() {
        Response authResponse = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/auth",
                cookie, this.header);

        AssertionsOwn.checkIntValue(authResponse, "user_id", 0);
    }

    @Owner("Teymur")
    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})
    void paramNegative(String condition) {
        Response response;
        if (condition.equals("cookie")) {
            response = apiCoreRequests.makeGetRequestWithCookie(authUrl, cookie);
            AssertionsOwn.checkIntValue(response, "user_id", 0);
        } else if (condition.equals("headers")) {
            response = apiCoreRequests.makeGetRequestWithToken(authUrl, header);
            AssertionsOwn.checkIntValue(response, "user_id", 0);
        } else {
            throw new IllegalArgumentException("Condition is unknown " + condition);
        }

    }
}
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.AssertionsOwn;
import lib.BaseTest;
import lib.DataGenerator;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Auth cases")
@Feature("Auth")
public class HomeWork16 extends BaseTest {

    private String
            cookie,
            header,
            logiUrl = "https://playground.learnqa.ru/api/user/login",
            getUserUrl = "https://playground.learnqa.ru/api/user/%s",
            url = "https://playground.learnqa.ru/api/user",
            cookieKey = "auth_sid",
            headerKey = "x-csrf-token";

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
                .post(logiUrl)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().response();

        this.cookie = this.getCookie(response, cookieKey);
        this.header = this.getHeader(response, headerKey);
    }

    @Owner("Teymur")
    @DisplayName("Get user by another user")
    @Test
    void getUserByAnotherUserTest() {
        Map<String, String> body = DataGenerator.getRegistrationData();
        //create user
        Response response = apiCoreRequests.makePostRequest(url, body);

        String expectedUserName = body.get("username");
        String userId = response.path("id");

        AssertionsOwn.assertStatusCode(response, 200);
        AssertionsOwn.assertJsonHasKey(response, "id");

        //get user
        Response getUserResponse = apiCoreRequests
                .makeGetRequest(String.format(getUserUrl, userId), cookie, header);
        String actualUserName = getUserResponse.path("username");

        Assertions.assertEquals(expectedUserName, actualUserName);
    }
}
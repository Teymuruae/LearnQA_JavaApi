import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lib.AssertionsOwn;
import lib.BaseTest;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserEditTest extends BaseTest {

    @Owner("Teymur")
    @Test
    void editUserTest() {

        //Create user
        Map<String, String> createBody = DataGenerator.getRegistrationData();
        Response response = RestAssured
                .given()
                .body(createBody)
                .contentType(ContentType.JSON)
                .post("https://playground.learnqa.ru/api/user");
        AssertionsOwn.assertJsonHasKey(response, "id");
        System.out.println(response.headers());
        String userId = response.path("id");
        //Login user
        Map<String, String> loginBody = new HashMap<>() {{
            put("email", createBody.get("email"));
            put("password", createBody.get("password"));
        }};
        String cookieKey = "auth_sid";
        String headerKey = "x-csrf-token";

        Response responseLogin = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(loginBody)
                .when()
                .post("https://playground.learnqa.ru/api/user/login");

        String
                cookie = getCookie(responseLogin, cookieKey),
                header = getHeader(responseLogin, headerKey);

        //Edit user
        String name = "EditedName";
        Map<String, String> editBody = new HashMap<>() {{
            put("firstName", name);
        }};
        Response editResponse = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .cookie(cookieKey, cookie)
                .header(headerKey, header)
                .body(editBody)
                .when()
                .put("https://playground.learnqa.ru/api/user/" + userId);
        AssertionsOwn.assertStatusCode(editResponse, 200);

        //get user
        Response getResponse = RestAssured
                .given()
                .cookie(cookieKey, cookie)
                .header(headerKey, header)
                .get("https://playground.learnqa.ru/api/user/" + userId);

        AssertionsOwn.checkStringValue(getResponse, "firstName", name);
    }
}
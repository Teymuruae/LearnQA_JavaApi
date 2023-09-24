import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.AssertionsOwn;
import lib.BaseTest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

public class Auth extends BaseTest {

    private int userId;
    private String
            cookie,
            header,
            authUrl = "https://playground.learnqa.ru/api/user/auth";


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

    @Test
    void authTest() {

        Response response2 = RestAssured
                .given().log().all()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .when()
                .get("https://playground.learnqa.ru/api/user/auth")
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().response();
    }

    @Test
    void positiveAuth() {

        Map<String, String> authCookie = new HashMap<>() {{
            put("auth_sid", cookie);
        }};

        Response authResponse = RestAssured
                .given()
                .cookies(authCookie)
                .header("x-csrf-token", header)
                .get(authUrl)
                .then().log().all()
                .extract().response();

        AssertionsOwn.checkIntValue(authResponse,"user_id", userId );
    }

    @Test
    void negativeAuth() {

        Map<String, String> authCookie = new HashMap<>() {{
            put("auth_sid", "cookie");
        }};

        Response authResponse = RestAssured
                .given()
                .cookies(authCookie)
                .header("x-csrf-token", header)
                .get(authUrl)
                .then().log().all()
                .extract().response();

        AssertionsOwn.checkIntValue(authResponse,"user_id", 0 );
    }


    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})
    void paramNegative(String condition) {


        RequestSpecification spec = RestAssured.given().log().all();
        spec.baseUri(authUrl);

        Map<String, String> cookies = new HashMap<>();
        cookies.put("auth_sid", cookie);


        if (condition.equals("cookie")) {
            spec.cookies(cookies);
        } else if (condition.equals("headers")) {
            spec.header("x-csrf-token", header);
        } else {
            throw new IllegalArgumentException("Condition is unknown " + condition);
        }

        Response response1 = spec
                .get()
                .then()
                .log()
                .all()
                .extract()
                .response();
    }
}
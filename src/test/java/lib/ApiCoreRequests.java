package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;

import java.util.Map;

public class ApiCoreRequests {

    @Step("full get request")
    public Response makeGetRequest(String url, String cookie, String token){
        return RestAssured
                .given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url);

    }

    @Step("full put request")
    public Response makePutRequest(String url, String cookie, String token, Map<String, String> body){
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(body)
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .put(url);
    }

    @Step("put request without cookie and token")
    public Response makePutRequestNotAuthorized(String url, Map<String, String> body){
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(body)
                .filter(new AllureRestAssured())
                .put(url);
    }

    @Step("full get request with only cookie")
    public Response makeGetRequestWithCookie(String url, String cookie){
        return RestAssured
                .given()
                .filter(new AllureRestAssured())
                .cookie("auth_sid", cookie)
                .get(url);
    }

    @Step("full get request with token only")
    public Response makeGetRequestWithToken(String url, String token){
        return RestAssured
                .given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .get(url);
    }

    @Step("full post request")
    public Response makePostRequest(String url, Map<String, String> body){
        return RestAssured
                .given()
                .filter(new AllureRestAssured())
                .body(body)
                .post(url);
    }

    @Step("delete request")
    public Response makeDeleteRequest(String url, String userId, String cookie, String token){
        return RestAssured
                .given()
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .filter(new AllureRestAssured())
                .delete(url+userId);
    }
}

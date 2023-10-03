import io.qameta.allure.Epic;
import io.qameta.allure.Owner;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.AssertionsOwn;
import lib.BaseTest;
import lib.DataGenerator;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Delete")
public class UserDeleteTest extends BaseTest {

    private ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    private String
            deleteUrl = "https://playground.learnqa.ru/api/user/",
            secondUserLogin = "vinkotov@example.com",
            loginUrl = "https://playground.learnqa.ru/api/user/login",
            cookieKey = "auth_sid",
            headerKey = "x-csrf-token",
            secondUserPassword = "1234";

    @Owner("Teymur")
    @DisplayName("Delete user by id 2 test")
    @Test
    void deleteUser2Test() {
        //Login user
        Map<String, String> loginBody = new HashMap<>() {{
            put("email", secondUserLogin);
            put("password", secondUserPassword);
        }};

        Response responseLogin = apiCoreRequests.makePostRequest(loginUrl, loginBody);

        String
                cookie = getCookie(responseLogin, cookieKey),
                header = getHeader(responseLogin, headerKey);

        //Delete user
        Response deleteResponse = apiCoreRequests.makeDeleteRequest(deleteUrl, "2", cookie, header);
        AssertionsOwn.assertHtmlBody(deleteResponse, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");

        //get user
        Response getResponse = apiCoreRequests.makeGetRequest(deleteUrl + "2", cookie, header);
        AssertionsOwn.checkStringValue(getResponse, "id", "2");
    }

    @Owner("Teymur")
    @DisplayName("Delete user positive test")
    @Test
    void deleteUserPositiveTest() {
        //Create user
        Map<String, String> createBody = createBody = DataGenerator.getRegistrationData();
        Response response = apiCoreRequests.makePostRequest(deleteUrl, createBody);

        AssertionsOwn.assertJsonHasKey(response, "id");
        String
                userId = response.path("id"),
                userEmail = createBody.get("email"),
                userPassword = createBody.get("password");

        //Login user
        Map<String, String> loginBody = new HashMap<>() {{
            put("email", userEmail);
            put("password", userPassword);
        }};

        Response responseLogin = apiCoreRequests.makePostRequest(loginUrl, loginBody);

        String
                cookie = getCookie(responseLogin, cookieKey),
                header = getHeader(responseLogin, headerKey);

        //Delete user
        Response deleteResponse = apiCoreRequests.makeDeleteRequest(deleteUrl, userId, cookie, header);
        AssertionsOwn.assertStatusCode(deleteResponse, HttpStatus.SC_OK);

        //get user
        Response getResponse = apiCoreRequests.makeGetRequest(deleteUrl + userId, cookie, header);
        AssertionsOwn.assertHtmlBody(getResponse, "User not found");
    }

    @Owner("Teymur")
    @DisplayName("Delete user by another user test")
    @Test
    void deleteUserByAnotherUserTest() {
        //Create user
        Map<String, String> createBody = createBody = DataGenerator.getRegistrationData();
        Response response = apiCoreRequests.makePostRequest(deleteUrl, createBody);

        AssertionsOwn.assertJsonHasKey(response, "id");
        String
                userId = response.path("id"),
                userEmail = createBody.get("email"),
                userPassword = createBody.get("password");

        //Login another user
        Map<String, String> loginAnotherUserBody = new HashMap<>() {{
            put("email", secondUserLogin);
            put("password", secondUserPassword);
        }};

        Response responseLoginAnotherUser = apiCoreRequests.makePostRequest(loginUrl, loginAnotherUserBody);

        String
                anotherUserCookie = getCookie(responseLoginAnotherUser, cookieKey),
                anotherUserHeader = getHeader(responseLoginAnotherUser, headerKey);

        //Delete user
        Response deleteResponse = apiCoreRequests.makeDeleteRequest(deleteUrl, userId, anotherUserCookie, anotherUserHeader);
        AssertionsOwn.assertHtmlBody(deleteResponse, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");

        //Login user
        Map<String, String> loginBody = new HashMap<>() {{
            put("email", userEmail);
            put("password", userPassword);
        }};

        Response responseLogin = apiCoreRequests.makePostRequest(loginUrl, loginBody);

        String
                cookie = getCookie(responseLogin, cookieKey),
                header = getHeader(responseLogin, headerKey);
    //get user
        Response getResponse = apiCoreRequests.makeGetRequest(deleteUrl + userId, cookie, header);
        AssertionsOwn.checkStringValue(getResponse, "id", userId);
    }
}
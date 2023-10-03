import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.AssertionsOwn;
import lib.BaseTest;
import lib.DataGenerator;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class HomeWork17EditUser extends BaseTest {

    private ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    private String
            createUserUrl = "https://playground.learnqa.ru/api/user/",
            loginUrl = "https://playground.learnqa.ru/api/user/login",
            cookieKey = "auth_sid",
            headerKey = "x-csrf-token",
            userId;
    private Map<String, String> createBody = new HashMap<>();

    @BeforeEach
    void setUp() {
        //Create user
        createBody = DataGenerator.getRegistrationData();
        Response response = apiCoreRequests.makePostRequest(createUserUrl, createBody);

        AssertionsOwn.assertJsonHasKey(response, "id");
        userId = response.path("id");
    }

    @DisplayName("Trying to edit user by not authorized same user")
    @Test
    void editUserNotAuthorizedSameUserTest() {

        //Edit user
        String editedName = "EditedName";
        Map<String, String> editBody = new HashMap<>() {{
            put("firstName", editedName);
        }};
        Response editResponse = apiCoreRequests.makePutRequestNotAuthorized(createUserUrl + userId, editBody);
        AssertionsOwn.assertHtmlBody(editResponse, "Auth token not supplied");
        //Login user
        Map<String, String> loginBody = new HashMap<>() {{
            put("email", createBody.get("email"));
            put("password", createBody.get("password"));
        }};

        Response responseLogin = apiCoreRequests.makePostRequest(loginUrl, loginBody);

        String
                cookie = getCookie(responseLogin, cookieKey),
                header = getHeader(responseLogin, headerKey);
        //get user
        String expectedName = createBody.get("firstName");
        Response getResponse = apiCoreRequests.makeGetRequest(createUserUrl + userId, cookie, header);

        AssertionsOwn.checkStringValue(getResponse, "firstName", expectedName);
    }

    @DisplayName("Trying to edit user by another user")
    @Test
    void editUserByAnotherUserTest() {
        //Create another user
        Map<String, String> pas = new HashMap<>() {{
            put("password", "9876");
        }};
        Map<String, String> createAnotherUserBody = DataGenerator.getRegistrationData(pas);
        String
                anotherUserEmail = createAnotherUserBody.get("email"),
                anotherUserPassword = createAnotherUserBody.get("password");

        Response response = apiCoreRequests.makePostRequest(createUserUrl, createAnotherUserBody);
        AssertionsOwn.assertJsonHasKey(response, "id");

        //login another user
        Map<String, String> anotherUserLoginBody = new HashMap<>() {{
            put("email", anotherUserEmail);
            put("password", anotherUserPassword);
        }};

        Response authResponse = apiCoreRequests.makePostRequest(loginUrl, anotherUserLoginBody);
        String
                anotherUserCookie = getCookie(authResponse, cookieKey),
                anotherUserToken = getHeader(authResponse, headerKey);

        //Edit user
        String editedName = "EditedName";
        Map<String, String> editBody = new HashMap<>() {{
            put("firstName", editedName);
        }};
        Response editResponse = apiCoreRequests.makePutRequest(createUserUrl + userId,
                anotherUserCookie, anotherUserToken, editBody);
        AssertionsOwn.assertStatusCode(editResponse, HttpStatus.SC_OK);
        //Login user
        Map<String, String> loginBody = new HashMap<>() {{
            put("email", createBody.get("email"));
            put("password", createBody.get("password"));
        }};

        Response responseLogin = apiCoreRequests.makePostRequest(loginUrl, loginBody);

        String
                cookie = getCookie(responseLogin, cookieKey),
                header = getHeader(responseLogin, headerKey);
        //get user
        String expectedName = createBody.get("firstName");
        Response getResponse = apiCoreRequests.makeGetRequest(createUserUrl + userId, cookie, header);

        AssertionsOwn.checkStringValue(getResponse, "firstName", expectedName);
    }

    @DisplayName("Trying to edit user email on wrong email by authorized same user")
    @Test
    void editUserEmailOnWrongEmailAuthorizedSameUserTest() {
        //Login user
        Map<String, String> loginBody = new HashMap<>() {{
            put("email", createBody.get("email"));
            put("password", createBody.get("password"));
        }};

        Response responseLogin = apiCoreRequests.makePostRequest(loginUrl, loginBody);

        String
                cookie = getCookie(responseLogin, cookieKey),
                header = getHeader(responseLogin, headerKey);

        //Edit user
        String editedEmail = "emailtest.ru";
        Map<String, String> editBody = new HashMap<>() {{
            put("email", editedEmail);
        }};

        Response editResponse = apiCoreRequests.makePutRequest(createUserUrl + userId, cookie, header, editBody);
        AssertionsOwn.assertHtmlBody(editResponse, "Invalid email format");

        //get user
        String email = createBody.get("email");
        Response getResponse = apiCoreRequests.makeGetRequest(createUserUrl + userId, cookie, header);

        AssertionsOwn.checkStringValue(getResponse, "email", email);
    }

    @DisplayName("Trying to edit user name on short name by authorized same user")
    @Test
    void editUserNameOnShortNameAuthorizedSameUserTest() {
        //Login user
        Map<String, String> loginBody = new HashMap<>() {{
            put("email", createBody.get("email"));
            put("password", createBody.get("password"));
        }};

        Response responseLogin = apiCoreRequests.makePostRequest(loginUrl, loginBody);

        String
                cookie = getCookie(responseLogin, cookieKey),
                header = getHeader(responseLogin, headerKey);

        //Edit user
        String editedName = "N";
        Map<String, String> editBody = new HashMap<>() {{
            put("firstName", editedName);
        }};

        Response editResponse = apiCoreRequests.makePutRequest(createUserUrl + userId, cookie, header, editBody);
        AssertionsOwn.checkStringValue(editResponse, "error", "Too short value for field firstName");

        //get user
        String expectedName = createBody.get("firstName");
        Response getResponse = apiCoreRequests.makeGetRequest(createUserUrl + userId, cookie, header);

        AssertionsOwn.checkStringValue(getResponse, "firstName", expectedName);
    }
}
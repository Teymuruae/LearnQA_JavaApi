import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.AssertionsOwn;
import lib.BaseTest;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class HomeWork17EditUser extends BaseTest {

    private ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    private String
            createUserUrl = "https://playground.learnqa.ru/api/user/",
            loginUrl = "https://playground.learnqa.ru/api/user/login";

    @Test
    void editUserNegativeTest() {

        //Create user
        Map<String, String> createBody = DataGenerator.getRegistrationData();
        Response response = apiCoreRequests.makePostRequest(createUserUrl, createBody);

        AssertionsOwn.assertJsonHasKey(response, "id");
        String userId = response.path("id");

        //Edit user
        String editedName = "EditedName";
        Map<String, String> editBody = new HashMap<>() {{
            put("firstName", editedName);
        }};
        Response editResponse = apiCoreRequests.makePutRequestNotAuthorized(createUserUrl + userId, editBody);

        //Login user
        Map<String, String> loginBody = new HashMap<>() {{
            put("email", createBody.get("email"));
            put("password", createBody.get("password"));
        }};
        String cookieKey = "auth_sid";
        String headerKey = "x-csrf-token";

        Response responseLogin = apiCoreRequests.makePostRequest(loginUrl, loginBody);

        String
                cookie = getCookie(responseLogin, cookieKey),
                header = getHeader(responseLogin, headerKey);
        //get user
        String expectedName = createBody.get("firstName");
        Response getResponse = apiCoreRequests.makeGetRequest(createUserUrl + userId, cookie, header);

        AssertionsOwn.checkStringValue(getResponse, "firstName", expectedName);
    }
}
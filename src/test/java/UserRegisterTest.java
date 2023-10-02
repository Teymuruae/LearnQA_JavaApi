import io.qameta.allure.Epic;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.AssertionsOwn;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

@Epic("Registration")
public class UserRegisterTest {
    private String
            email = "vinkotov@example.com",
            wrongEmail = "testexapmple.ru",
            url = "https://playground.learnqa.ru/api/user";
    private ApiCoreRequests apiCoreRequests = new ApiCoreRequests();


    @DisplayName("Register user with exist email")
    @Test
    void authNegativeTest() {
        Map<String, String> body = new HashMap<>() {
            {
                put("email", email);
            }
        };
        body = DataGenerator.getRegistrationData(body);

        Response response = RestAssured
                .given()
                .body(body)
                .post(url)
                .andReturn();
        String expectedAnswer = String.format("Users with email '%s' already exists", email);
        AssertionsOwn.assertAnswer(response, expectedAnswer);
        AssertionsOwn.assertStatusCode(response, 400);
    }

    @DisplayName("Auth positive test")
    @Test
    void authPositiveTest() {
        Map<String, String> body = DataGenerator.getRegistrationData();

        Response response = RestAssured
                .given()
                .body(body)
                .post(url)
                .andReturn();

        AssertionsOwn.assertStatusCode(response, 200);
        AssertionsOwn.assertJsonHasKey(response, "id");
    }

    @DisplayName("Auth with wrong email negative test")
    @Test
    void authWithWrongEmailTest() {
        Map<String, String> emailBody = new HashMap<>() {{
            put("email", wrongEmail);
        }};
        Map<String, String> body = DataGenerator.getRegistrationData(emailBody);

        Response response = apiCoreRequests.makePostRequest(url, body);

        AssertionsOwn.assertStatusCode(response, 400);
        AssertionsOwn.assertHtmlBody(response, "Invalid email format");
    }

    @DisplayName("Auth without one field")
    @ValueSource(strings = {
            "email",
            "password",
            "username",
            "firstName",
            "lastName"
    })
    @ParameterizedTest
    void authWithoutOneFieldTest(String field) {
        Map<String, String> body = DataGenerator.getRegistrationDataWithoutOneField(field);

        Response response = apiCoreRequests.makePostRequest(url, body);
        String responseBody = String.format("The following required params are missed: %s", field);

        AssertionsOwn.assertStatusCode(response, 400);
        AssertionsOwn.assertHtmlBody(response, responseBody);
    }

    @DisplayName("Auth with short name test")
    @Test
    void authWithShortNameTest() {
        Map<String, String> nameBody = new HashMap<>() {{
            put("firstName", "N");
        }};
        Map<String, String> body = DataGenerator.getRegistrationData(nameBody);

        Response response = apiCoreRequests.makePostRequest(url, body);

        AssertionsOwn.assertStatusCode(response, 400);
        AssertionsOwn.assertHtmlBody(response, "The value of 'firstName' field is too short");
    }

    @DisplayName("Auth with long name test")
    @Test
    void authWithLongNameTest() {
        String firstName = DataGenerator.getRandomStringByLength(250);
        Map<String, String> nameBody = new HashMap<>() {{
            put("firstName", firstName);
        }};
        Map<String, String> body = DataGenerator.getRegistrationData(nameBody);

        Response response = apiCoreRequests.makePostRequest(url, body);

        AssertionsOwn.assertStatusCode(response, 200);
        AssertionsOwn.assertJsonHasKey(response, "id");
        AssertionsOwn.assertJsonValueNotNull(response, "id");
    }
}
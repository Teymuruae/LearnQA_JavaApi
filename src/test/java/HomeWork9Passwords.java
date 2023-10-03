import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeWork9Passwords {

    private String
            passwordText = "password",
            loginText = "login",
            postLoginPasswordUrl = "https://playground.learnqa.ru/ajax/api/get_secret_password_homework",
            checkAuthCookieUrl = "https://playground.learnqa.ru/ajax/api/check_auth_cookie",
            cookieName = "auth_cookie",
            adminLogin = "super_admin",
            validAuthText = "You are authorized";
    List<String> passwordsList = List.of("123456", "123456789", "qwerty", "password", "1234567", "12345678",
            "12345", "iloveyou", "111111", "123123", "abc123", "qwerty123", "1q2w3e4r", "admin", "qwertyuiop", "654321", "555555",
            "lovely", "7777777", "welcome", "888888", "princess", "dragon", "password1", "123qwe");

    private Map<String, String> map = new HashMap<>() {{
        put(loginText, adminLogin);
    }};

    @Description("Подбираем правильный пароль из списка")
    @Owner("Teymur")
    @Test
    void getValidPasswordTest() {

        String authCookieValue = "";
        String checkAuthMessage = "";

            for (String password : passwordsList) {
                map.put(passwordText, password);
                int loginStatusCode = 0;

                while (loginStatusCode != 200) {
                    Response response = RestAssured
                            .given()
                            .formParams(map)
                            .when()
                            .post(postLoginPasswordUrl)
                            .then()
                            .extract().response();
                    loginStatusCode = response.statusCode();
                    if (loginStatusCode == 200) {
                        authCookieValue = response.getCookie(cookieName);
                    }
                }
                if (!authCookieValue.isEmpty()) {
                    Map<String, String> cookie = new HashMap<>();
                    cookie.put(cookieName, authCookieValue);

                    checkAuthMessage = RestAssured
                            .given()
                            .cookies(cookie)
                            .when()
                            .post(checkAuthCookieUrl)
                            .then()
                            .extract().htmlPath().getString("body");
                } else {
                    Assertions.assertTrue(false, "authCookieValue is empty");
                }
                if(checkAuthMessage.equals(validAuthText)){
                    System.out.println("the password is : " + password);
                    break;
                }
            }
            Assertions.assertEquals(validAuthText, checkAuthMessage);
    }
}
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class GetPlayground {
    Map<String, String> params = new HashMap<>() {{
        put("name", "Jojo");
    }};

    Map<String, String> body = new HashMap<>() {{
        put("login", "secret_login");
        put("password", "secret_pass");
    }};

    @Test
    void rest() {
        Response response = RestAssured
                .given()
                .body(body)
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        //получаем ключ и значение cookie
        Map<String, String> allCookies = response.getCookies();


        // получаем значение cookie
        String authCookie = response.getCookie("auth_cookie");

        //передаём cookie
        Map<String, String> cookie = new HashMap<>();
        cookie.put("auth_cookie", authCookie);

        Response checkResponse = RestAssured
                .given()
                .body(body)
                //первый вариант - напрямую указать ключ и значение cookie
//                .cookie("auth_cookie", authCookie)
                //второй вариант - через hashMap
                .cookies(cookie)
                .when()
                .post("https://playground.learnqa.ru/api/check_auth_cookie")
                .thenReturn();
        checkResponse.print();
    }

    @Test
    void test2() {
        Map<String, String> body2 = new HashMap<>() {{
            put("login", "secret_login");
            put("password", "secret_pass");
        }};

        Response response = RestAssured
                .given()
                .body(body2)
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .thenReturn();

        String authCookie = response.getCookie("auth_cookie");
        Map<String, String> cookie = new HashMap<>();
        cookie.put("auth_cookie", authCookie);

        Response response1 = RestAssured
                .given()
                .body(body2)
                .cookie("auth_cookie", authCookie)
                .when()
                .post("https://playground.learnqa.ru/api/check_auth_cookie")
                .thenReturn();

        response1.print();
    }
}
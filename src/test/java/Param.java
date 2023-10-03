import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

public class Param {

    public static void main(String[] args) {
        String s = "";
        System.out.println(s.length());
    }

    @Owner("Teymur")
    @ValueSource(strings = {"", "Petr", "Ivan"})
    @ParameterizedTest
    void test(String name){

                Map<String, String> map = new HashMap<>(){{
                }};

        if(name.length() >0){
            map.put("name",name);
        }
        Response response = RestAssured
                .given()
                .queryParams(map)
                .get("https://playground.learnqa.ru/api/hello")
                .then().log().all()
                .extract().response();
        String actualMessage = response.path("answer");
        String expectedMessage = (name.length() >0 )? name : "someone";

        Assertions.assertEquals("Hello, " + expectedMessage,actualMessage );
    }
}

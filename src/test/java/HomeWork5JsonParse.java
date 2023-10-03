import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class HomeWork5JsonParse {
    @Owner("Teymur")
    @Test
    void jsonParseTest() {
        Map<String, ?> map  = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath().get("messages[1]");

        System.out.println(map);
    }
}
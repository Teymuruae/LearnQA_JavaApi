import io.qameta.allure.Issue;
import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Issue("REDIR-586")
public class HomeWork6Redirect {

    @Owner("Teymur")
    @DisplayName("check link redirection")
    @Test
    void redirectTest(){
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .thenReturn();

        System.out.println(response.header("Location"));
    }
}

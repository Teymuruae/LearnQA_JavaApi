import io.qameta.allure.Issue;
import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Issue("REDIR-586")
public class HomeWork7LongRedirect {

    @Owner("Teymur")
    @DisplayName("Long redirection test")
    @Test
    void longRedirectTest(){
        String url = "https://playground.learnqa.ru/api/long_redirect";
        int statusCode = 0;

        while(statusCode != 200) {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(url)
                    .thenReturn();
            statusCode = response.statusCode();
            if(statusCode == 301) {
                url = response.header("Location");
            }
        }
        System.out.println(url);
    }
}
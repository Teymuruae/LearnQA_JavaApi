import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class HomeWork7LongRedirect {

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
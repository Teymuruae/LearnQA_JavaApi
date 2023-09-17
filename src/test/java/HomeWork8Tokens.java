import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class HomeWork8Tokens {

    private final String URL = "https://playground.learnqa.ru/ajax/api/longtime_job";
    private String
            tokenName = "token",
            secondsName = "seconds",
            statusName = "status",
            statusJobNotReady = "Job is NOT ready",
            statusJobReady = "Job is ready";

    private long getMilliseconds(int seconds) {
        return (long) seconds * 1000;
    }

    @Test
    void tokenTest() throws InterruptedException {
        Response response = RestAssured
                .get(URL)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(tokenName, Matchers.notNullValue())
                .body(secondsName, Matchers.notNullValue())
                .extract().response();

        RestAssured
                .given()
                .queryParam(tokenName, response.jsonPath().get(tokenName).toString())
                .get(URL)
                .then()
                .body(statusName, Matchers.equalTo(statusJobNotReady));


        long duration = getMilliseconds(response.jsonPath().get(secondsName));
        Thread.sleep(duration);

        RestAssured
                .given()
                .queryParam(tokenName, response.jsonPath().get(tokenName).toString())
                .get(URL)
                .then()
                .body(statusName, Matchers.equalTo(statusJobReady));
    }
}
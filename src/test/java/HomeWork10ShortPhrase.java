import io.qameta.allure.Owner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class HomeWork10ShortPhrase {

    private static final String
            less15 = "one two",
            exact15 = "one two three f",
            greaterThan15 = "one two three four five",
            empty = "";

    public void checkStringLength(String valueToCheck) {
        Assertions.assertTrue(valueToCheck.length() >= 15, "Length is less, than 15");
    }

    @Owner("Teymur")
    @ValueSource(strings = {less15, exact15, greaterThan15, empty})
    @ParameterizedTest
    void stringLengthTest(String value) {
        checkStringLength(value);
    }
}
package lib;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DataGenerator {

    public static String generateRandomEmail() {
        String time = new SimpleDateFormat("ddMMyyyyhhmmss").format(new java.util.Date());
        return "learn" + time + "@example.com";
    }

    public static Map<String, String> getRegistrationData() {
        return new HashMap<>() {
            {
                put("email", DataGenerator.generateRandomEmail());
                put("password", "123");
                put("username", "learnqa");
                put("firstName", "learnqa");
                put("lastName", "learnqa");
            }
        };
    }

    public static Map<String, String> getRegistrationData(Map<String, String> nonDefaultValues) {
        Map<String, String> defaultValues = DataGenerator.getRegistrationData();
        Map<String, String> userData = new HashMap<>();

        List<String> keys = List.of("email", "password", "username", "firstName", "lastName");
        for (String key : keys) {
            if (nonDefaultValues.containsKey(key)) {
                userData.put(key, nonDefaultValues.get(key));
            } else
                userData.put(key, defaultValues.get(key));
        }
        return userData;
    }

    public static Map<String, String> getRegistrationDataWithoutOneField(String field) {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        userData.remove(field);
        return userData;
    }

    public static String getRandomStringByLength(int length){
        return  RandomStringUtils.random(250, true, false);
    }
}
package edu.studio.weather;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class OpenMeteoRestClientTest {

    @Test
    public void testFetchWeatherData() {
        OpenMeteoRestClient openMeteoRestClient = new OpenMeteoRestClient();
        String expectedJson = readJsonFromFile();
        String actualJson = openMeteoRestClient.fetchWeatherData();
        Gson gson = new Gson();
        JsonObject expectedJsonObject = gson.fromJson(expectedJson, JsonObject.class);
        JsonObject actualJsonObject = gson.fromJson(actualJson, JsonObject.class);
        JsonObject expectedHourlyUnits = expectedJsonObject.getAsJsonObject("hourly_units");
        JsonObject actualHourlyUnits = actualJsonObject.getAsJsonObject("hourly_units");
        assertEquals(expectedHourlyUnits, actualHourlyUnits);
    }

    private String readJsonFromFile() {
        StringBuilder content = new StringBuilder();
        String filePath = "src/test/resources/open-meteo-pressure-trend-response.json"; 

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }
}

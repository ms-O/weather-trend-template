package edu.studio.weather;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

public class WeatherTrendDriver {

    protected static final String API_URL = "https://api.open-meteo.com/v1/gfs";

    public static HttpResponse<JsonNode> getWeatherData(double latitude, double longitude) {
        HttpResponse<JsonNode> response = Unirest.get(API_URL).queryString("latitude", latitude)
                .queryString("longitude", longitude).queryString("hourly", "pressure_msl")
                .queryString("timezone", "America/New_York").asJson();

        if (response.getStatus() == 200) {
            System.out.println("GET request succeeded!");
            return response;

        }
        else {
            System.out.println("HTTP Status " + response.getStatus());
            System.out.print("GET request failed!");

        }
        return response;
    }

    public static OpenMeteoParser parseJsonResponseToObject(JsonNode jsonNode) {

        Gson gson = new Gson();
        String jsonString = jsonNode.toString();
        OpenMeteoParser weatherData = gson.fromJson(jsonString, OpenMeteoParser.class);
        // System.out.println("This is the data after parsing => Latitude: " + weatherData.getLatitude());
        return weatherData;
    }

    public static List<Double> promptUserInput() {
        Scanner scanner = new Scanner(System.in);
        List<Double> validCoordinates = new ArrayList();
        boolean isValidLatitude = false;
        boolean isValidLongitude = true;

        while (!isValidLatitude || !isValidLongitude) {
            System.out.print("Enter latitude (between -90 and 90): ");
            String latitudeInput = scanner.nextLine().trim();
            System.out.print("Enter longitude (between -180 and 180): ");
            String longitudeInput = scanner.nextLine().trim();

            try {
                double latitude = Double.parseDouble(latitudeInput);
                double longitude = Double.parseDouble(longitudeInput);

                // Validate latitude range (-90 to 90) and longitude range (-180 to 180)
                if (latitude >= -90 && latitude <= 90) {
                    isValidLatitude = true;
                    validCoordinates.add(latitude);
                }
                else {
                    System.out.println("Invalid latitude! Latitude must be between -90 and 90 degrees.");
                }

                if (longitude >= -180 && longitude <= 180) {
                    isValidLongitude = true;
                    validCoordinates.add(longitude);
                }
                else {
                    System.out.println("Invalid longitude! Longitude must be between -180 and 180 degrees.");
                }

            }
            catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter valid numerical values.");
            }
        }

        // System.out.println("Correct values given by the user!");
        scanner.close();
        return validCoordinates;
    }

    public static void main(String[] args) {
        ForecastAnalyzer forecastAnalyzer = new ForecastAnalyzer();
        Notifier notifier = new Notifier();
        List<Double> latAndLong = promptUserInput();
        Double latitude = latAndLong.get(0);
        Double longitude = latAndLong.get(1);
        HttpResponse<JsonNode> response = getWeatherData(latitude, longitude);
        JsonNode jsonNode = response.getBody();
//        System.out.print(jsonNode);
        OpenMeteoParser openMeteoParser = parseJsonResponseToObject(jsonNode);
        HourlyData hourlyData = openMeteoParser.getHourly();
        List<List<String>> migraneTriggerTimes = forecastAnalyzer.calculateMigranePeriodsPrecise(hourlyData);
        // List<List<String>> migraneTriggerTimes = forecastAnalyzer.calculateMigranePeriods(hourlyData);
        notifier.notifyUsers(migraneTriggerTimes);
    }

}

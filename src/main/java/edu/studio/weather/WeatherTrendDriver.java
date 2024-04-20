package edu.studio.weather;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import java.util.List;

import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;


public class WeatherTrendDriver {
    
    protected static final String API_URL = "https://api.open-meteo.com/v1/gfs";
    
    public static HttpResponse<JsonNode> getWeatherData(double latitude, double longitude) {
        HttpResponse<JsonNode> response = Unirest.get(API_URL)
                .queryString("latitude", latitude)
                .queryString("longitude", longitude)
                .queryString("hourly", "pressure_msl")
                .queryString("timezone","America/New_York")
                .asJson();
        
        if (response.getStatus() == 200) {
            System.out.print("GET request succeeded!!");
            return response;

        }
        else {
            System.out.println("HTTP Status " + response.getStatus());
            System.out.print("GET request failed!!");
            
        }
        System.out.println("HTTP Status " + response.getStatus());
        System.out.println(response.getBody().toPrettyString());
        return response;
    }
    
    
    public static OpenMeteoParser parseJsonResponseToObject(JsonNode jsonNode) {
        
        Gson gson = new Gson();
        String jsonString = jsonNode.toString();
        OpenMeteoParser weatherData = gson.fromJson(jsonString, OpenMeteoParser.class);
        System.out.println("This is the data after parsing => Latitude: " + weatherData.getLatitude());
        return weatherData;
    }
    

    public static void main(String[] args) {
        ForecastAnalyzer forecastAnalyzer = new ForecastAnalyzer();
        Notifier notifier = new Notifier();
        
        System.out.println("Hello Weather Trend");
        HttpResponse<JsonNode> response = getWeatherData(40.03705685765183,  -75.34258923444702);
        JsonNode jsonNode = response.getBody();
        OpenMeteoParser openMeteoParser = parseJsonResponseToObject(jsonNode);
        HourlyData hourlyData = openMeteoParser.getHourly();
        System.out.println("This is a test");
        List<List<String>> migraneTriggerTimes = forecastAnalyzer.calculateMigranePeriodsPrecise(hourlyData);
//        List<List<String>> migraneTriggerTimes = forecastAnalyzer.calculateMigranePeriods(hourlyData);
        
        notifier.notifyUsers(migraneTriggerTimes);
        
      
    }

}

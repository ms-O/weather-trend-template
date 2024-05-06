package edu.studio.weather;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

public class OpenMeteoRestClient {
    
    public static final String API_URL = "https://api.open-meteo.com/v1/gfs";

    public String fetchWeatherData() {
        HttpResponse<JsonNode> response = Unirest.get(API_URL)
                .queryString("latitude", 40.03705685765183)
                .queryString("longitude", -75.34258923444702)
                .queryString("hourly", "pressure_msl")
                .queryString("timezone", "America/New_York")
                .queryString("start_date","2024-04-01")
                .queryString("end_date","2024-04-07")
                .asJson();

        return response.getBody().toString();
    }
    
   

}

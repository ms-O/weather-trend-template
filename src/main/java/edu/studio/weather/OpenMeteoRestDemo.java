package edu.studio.weather;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

/*
 * Open-Meteo API interactive reference page: https://open-meteo.com/en/docs/gfs-api
 * You may freely use this sample code in your project, 
 * but remember to format and refactor it to meet our coding standards.
 */
public class OpenMeteoRestDemo {
    
    protected static final String API_URL = "https://api.open-meteo.com/v1/gfs";

    public static void main(String[] args) {
        
        //Open-Meteo GET request with Unirest
        HttpResponse<JsonNode> response = Unirest.get(API_URL)
                .queryString("latitude", 40.03705685765183)
                .queryString("longitude", -75.34258923444702)
                .queryString("hourly", "pressure_msl")
                .queryString("timezone","America/New_York")
                .asJson();

        System.out.println("HTTP Status " + response.getStatus());
        System.out.println(response.getBody().toPrettyString());
        
        //Gson Parser Example
        //get to the part of the JSON response that we care about: "hourly"
        JsonNode topLevelNode = response.getBody();
        JSONObject outerJsonObject = topLevelNode.getObject();
        JSONObject hourlyValues = outerJsonObject.getJSONObject("hourly");
        
        //Use Gson parser to convert hourly values to a custom data structure
        Gson gson = new Gson();
        ForecastData forecastData = gson.fromJson(hourlyValues.toString(), ForecastData.class);

        //Create a list of forecasts from the custom data structure
        //An alternative would be to make ForecastData iterable so it can simulate this,
        //but that would be custom to Open-Meteo, which makes it less portable to other APIs
        List<Forecast> forecasts = new ArrayList<>();
        for (int i = 0; i < forecastData.getTime().size(); i++) {
            forecasts.add(new Forecast(forecastData.getTime().get(i), forecastData.getPressureMsl().get(i)));
        }

        //print forecasts out to visually confirm it worked
        for (Forecast forecast : forecasts) {
            System.out.println(forecast);
        }        
    }

}

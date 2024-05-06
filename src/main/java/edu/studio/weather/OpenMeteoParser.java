package edu.studio.weather;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import kong.unirest.JsonNode;
import kong.unirest.json.JSONObject;

public class OpenMeteoParser {

    public  List<Forecast> parseData(String weatherData) {
        
        JsonNode topLevelNode = new JsonNode(weatherData);
        JSONObject outerJsonObject = topLevelNode.getObject();
        JSONObject hourlyValues = outerJsonObject.getJSONObject("hourly");
       
        Gson gson = new Gson();
        ForecastData forecastData = gson.fromJson(hourlyValues.toString(), ForecastData.class);

        List<Forecast> forecasts = new ArrayList<>();
        for (int i = 0; i < forecastData.getTime().size(); i++) {
            forecasts.add(new Forecast(forecastData.getTime().get(i), forecastData.getPressureMsl().get(i)));
        }

        return forecasts;
    }
    
}
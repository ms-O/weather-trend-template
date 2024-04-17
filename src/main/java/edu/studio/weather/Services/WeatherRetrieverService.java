package edu.studio.weather.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.gson.Gson;

import edu.studio.weather.Models.Forecast;
import edu.studio.weather.Models.ForecastData;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

public class WeatherRetrieverService {

    protected static final String API_URL = "https://api.open-meteo.com/v1/gfs";

    public CompletableFuture<List<Forecast>> retrieveWeatherAsync() {
        CompletableFuture<JsonNode> jsonFuture = retrieveWeatherJsonAsync();

        return jsonFuture.thenApply(topLevelNode -> parseWeatherJson(topLevelNode));
    }

    // Helper methods
    private CompletableFuture<JsonNode> retrieveWeatherJsonAsync() {
        CompletableFuture<HttpResponse<JsonNode>> responseFuture = Unirest.get(API_URL)
                .queryString("latitude", 40.03705685765183)
                .queryString("longitude", -75.34258923444702)
                .queryString("hourly", "pressure_msl")
                .queryString("timezone", "America/New_York")
                .asJsonAsync();

        // Return the JSON data
        return responseFuture.thenApply(response -> response.getBody());
    }

    private List<Forecast> parseWeatherJson(JsonNode topLevelNode) {
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

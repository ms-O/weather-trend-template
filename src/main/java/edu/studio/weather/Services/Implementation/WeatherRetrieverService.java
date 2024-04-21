package edu.studio.weather.Services.Implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.gson.Gson;

import edu.studio.weather.External.Abstraction.IOpenMateoApiClient;
import edu.studio.weather.Models.Forecast;
import edu.studio.weather.Models.ForecastData;
import edu.studio.weather.Services.Abstraction.IWeatherRetrieverService;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;

public class WeatherRetrieverService implements IWeatherRetrieverService {

    public final IOpenMateoApiClient openMateoApiClient;

    public WeatherRetrieverService(IOpenMateoApiClient openMateoApiClient) {
        this.openMateoApiClient = openMateoApiClient;
    }

    public CompletableFuture<List<Forecast>> retrieveWeatherAsync() {
        CompletableFuture<JsonNode> jsonFuture = openMateoApiClient.GetWeatherJsonAsync();
        return jsonFuture.thenApply(topLevelNode -> parseWeatherJson(topLevelNode)).exceptionally(ex -> {
            System.err.println("Error retrieving weather data: " + ex.getMessage());
            return new ArrayList<>();
        });
    }

    // Helper methods
    private List<Forecast> parseWeatherJson(JsonNode topLevelNode) {
        try {
            JSONObject outerJsonObject = topLevelNode.getObject();
            JSONObject hourlyValues = outerJsonObject.getJSONObject("hourly");

            Gson gson = new Gson();
            ForecastData forecastData = gson.fromJson(hourlyValues.toString(), ForecastData.class);

            List<Forecast> forecasts = new ArrayList<>();
            for (int i = 0; i < forecastData.getTime().size(); i++) {
                forecasts.add(new Forecast(forecastData.getTime().get(i), forecastData.getPressureMsl().get(i)));
            }

            return forecasts;
        } catch (JSONException | NullPointerException e) {
            System.err.println("Error parsing weather JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}

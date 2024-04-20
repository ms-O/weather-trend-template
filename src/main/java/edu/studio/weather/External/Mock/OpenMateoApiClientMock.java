package edu.studio.weather.External.Mock;

import java.util.concurrent.CompletableFuture;
import edu.studio.weather.Config.AppConfig;
import edu.studio.weather.External.Abstraction.IOpenMateoApiClient;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.HttpResponse;

public class OpenMateoApiClientMock implements IOpenMateoApiClient {
    private static final String API_URL = AppConfig.WEATHER_HISTORY_API_URL;

    public CompletableFuture<JsonNode> GetWeatherJsonAsync() {
        CompletableFuture<HttpResponse<JsonNode>> responseFuture = Unirest.get(API_URL)
                .queryString("latitude", 39.9639)
                .queryString("longitude", -75.2484)
                .queryString("start_date", "2024-04-04")
                .queryString("end_date", "2024-04-11")
                .queryString("hourly", "pressure_msl")
                .queryString("timezone", "America/New_York")
                .asJsonAsync();

        // Return the JSON data
        return responseFuture.thenApply(response -> response.getBody());
    }
}

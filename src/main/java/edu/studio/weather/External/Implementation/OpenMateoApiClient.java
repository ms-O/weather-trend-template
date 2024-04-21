package edu.studio.weather.External.Implementation;

import java.util.concurrent.CompletableFuture;
import edu.studio.weather.Config.AppConfig;
import edu.studio.weather.External.Abstraction.IOpenMateoApiClient;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.HttpResponse;

public class OpenMateoApiClient implements IOpenMateoApiClient {

    private static final String API_URL = AppConfig.WEATHER_API_URL;

    public CompletableFuture<JsonNode> GetWeatherJsonAsync() {
        try {
            CompletableFuture<HttpResponse<JsonNode>> responseFuture = Unirest.get(API_URL)
                    .queryString("latitude", 39.9639)
                    .queryString("longitude", -75.2484)
                    .queryString("hourly", "pressure_msl")
                    .queryString("timezone", "America/New_York")
                    .asJsonAsync();

            return responseFuture.thenApply(response -> response.getBody());
        } catch (Exception e) {
            CompletableFuture<JsonNode> exceptionallyCompletedFuture = new CompletableFuture<>();
            exceptionallyCompletedFuture.completeExceptionally(e);
            return exceptionallyCompletedFuture;
        }
    }
}

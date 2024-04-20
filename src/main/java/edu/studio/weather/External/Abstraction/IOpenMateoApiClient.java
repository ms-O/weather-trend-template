package edu.studio.weather.External.Abstraction;

import java.util.concurrent.CompletableFuture;

import kong.unirest.JsonNode;

public interface IOpenMateoApiClient {
    public CompletableFuture<JsonNode> GetWeatherJsonAsync();
}

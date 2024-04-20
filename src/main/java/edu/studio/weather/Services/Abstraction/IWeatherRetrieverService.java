package edu.studio.weather.Services.Abstraction;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import edu.studio.weather.Models.Forecast;

public interface IWeatherRetrieverService {
    CompletableFuture<List<Forecast>> retrieveWeatherAsync();
}

package edu.studio.weather.Mocks;

import java.util.concurrent.CompletableFuture;

import edu.studio.weather.External.Abstraction.IOpenMateoApiClient;
import edu.studio.weather.Services.Abstraction.IForecastAnalyzerService;
import edu.studio.weather.Services.Abstraction.IWeatherRetrieverService;
import edu.studio.weather.Services.Implementation.ForecastAnalyzerService;
import edu.studio.weather.Services.Implementation.WeatherRetrieverService;

public class WeatherTrendDriverMock {
    private final IOpenMateoApiClient openMateoApiClient;
    private final IForecastAnalyzerService forecastAnalyzerService;
    private final IWeatherRetrieverService weatherRetrieverService;

    public WeatherTrendDriverMock() {
        this.openMateoApiClient = new OpenMateoApiClientMock();
        this.weatherRetrieverService = new WeatherRetrieverService(openMateoApiClient);
        this.forecastAnalyzerService = new ForecastAnalyzerService(weatherRetrieverService);
    }

    public CompletableFuture<Void> run() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        forecastAnalyzerService.analyzeForecasts()
                .thenAccept(migraineEpisodes -> {
                    if (migraineEpisodes.isEmpty()) {
                        System.out.println("---No Migrane Alerts---");
                    } else {
                        System.out.println("---Migrane Alerts---");
                        migraineEpisodes.forEach((result) -> {
                            System.out.println(result.toString());
                        });
                    }
                    future.complete(null);
                })
                .exceptionally(ex -> {
                    System.err.println("Error retrieving migraine episodes: " + ex.getMessage());
                    future.completeExceptionally(ex);
                    return null;
                });
        return future;
    }

    public static void main(String[] args) {
        WeatherTrendDriverMock driver = new WeatherTrendDriverMock();
        driver.run().join();
    }
}

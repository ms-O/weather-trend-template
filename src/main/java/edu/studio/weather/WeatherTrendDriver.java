package edu.studio.weather;

import edu.studio.weather.Services.Abstraction.IForecastAnalyzerService;
import edu.studio.weather.Services.Abstraction.IWeatherRetrieverService;
import edu.studio.weather.Services.Implementation.ForecastAnalyzerService;
import edu.studio.weather.Services.Implementation.WeatherRetrieverService;

public class WeatherTrendDriver {
    private final IForecastAnalyzerService forecastAnalyzerService;
    private final IWeatherRetrieverService weatherRetrieverService;

    public WeatherTrendDriver() {
        this.weatherRetrieverService = new WeatherRetrieverService();
        this.forecastAnalyzerService = new ForecastAnalyzerService(weatherRetrieverService);
    }

    public static void main(String[] args) {
        WeatherTrendDriver driver = new WeatherTrendDriver();

        // Retrieve weather data
        driver.forecastAnalyzerService.analyzeForecasts()
                .thenAccept(migraineEpisodes -> {
                    if (migraineEpisodes.isEmpty()) {
                        System.out.println("---No Migrane Alerts---");
                    } else {
                        System.out.println("---Migrane Alerts---");
                        migraineEpisodes.forEach((startTime, endTime) -> {
                            System.out.println("Potential migraine episode from " + startTime + " to " + endTime);
                        });
                    }

                })
                .exceptionally(ex -> {
                    System.err.println("Error retrieving migraine episodes: " + ex.getMessage());
                    return null;
                });
    }

}

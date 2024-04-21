package edu.studio.weather.Services.Implementation;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import edu.studio.weather.Models.Forecast;
import edu.studio.weather.Services.Abstraction.IForecastAnalyzerService;
import edu.studio.weather.Services.Abstraction.IWeatherRetrieverService;

public class ForecastAnalyzerService implements IForecastAnalyzerService {
    final double PRESSURE_DROP_THRESHOLD = 5.0;
    final int TIME_INTERVAL = 6;

    private final IWeatherRetrieverService weatherRetrieverService;

    public ForecastAnalyzerService(IWeatherRetrieverService weatherRetrieverService) {
        this.weatherRetrieverService = weatherRetrieverService;
    }

    public CompletableFuture<Map<LocalDateTime, LocalDateTime>> analyzeForecasts() {
        return weatherRetrieverService.retrieveWeatherAsync().thenApply((List<Forecast> forecasts) -> {
            Map<LocalDateTime, LocalDateTime> migraineEpisodes = new HashMap<>();
            LocalDateTime migraineStartTime = null;

            if (forecasts == null || forecasts.isEmpty())
                return migraineEpisodes;

            for (int i = 0; i < forecasts.size(); i++) {
                Forecast currentForecast = forecasts.get(i);
                double currentPressure = currentForecast.getPressure();
                int timeInterval = (i + TIME_INTERVAL < forecasts.size()) ? TIME_INTERVAL : forecasts.size() - i - 1;

                boolean pressureDropDetected = false;
                for (int j = i + 1; j <= i + timeInterval; j++) {
                    Forecast nextForecast = forecasts.get(j);
                    double nextPressure = nextForecast.getPressure();

                    double pressureDrop = currentPressure - nextPressure;
                    if (pressureDrop >= PRESSURE_DROP_THRESHOLD) {
                        pressureDropDetected = true;
                        break;
                    }
                }

                if (pressureDropDetected) {
                    if (migraineStartTime == null) {
                        migraineStartTime = LocalDateTime.parse(currentForecast.getTime());
                    }
                } else {
                    if (migraineStartTime != null) {
                        LocalDateTime migraineEndTime = LocalDateTime.parse(currentForecast.getTime());
                        migraineEpisodes.put(migraineStartTime, migraineEndTime);
                        migraineStartTime = null;
                    }
                }
            }

            return migraineEpisodes;
        });
    }
}

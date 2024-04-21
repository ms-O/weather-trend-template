package edu.studio.weather.Services.Implementation;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import edu.studio.weather.Models.Forecast;
import edu.studio.weather.Models.Result;
import edu.studio.weather.Services.Abstraction.IForecastAnalyzerService;
import edu.studio.weather.Services.Abstraction.IWeatherRetrieverService;

public class ForecastAnalyzerService implements IForecastAnalyzerService {
    final double PRESSURE_DROP_THRESHOLD = 5.0;
    final int TIME_INTERVAL = 6;

    private final IWeatherRetrieverService weatherRetrieverService;

    public ForecastAnalyzerService(IWeatherRetrieverService weatherRetrieverService) {
        this.weatherRetrieverService = weatherRetrieverService;
    }

    public CompletableFuture<List<Result>> analyzeForecasts() {
        return weatherRetrieverService.retrieveWeatherAsync().thenApply((List<Forecast> forecasts) -> {
            List<Result> migraineResults = new ArrayList<Result>();
            LocalDateTime migraineStartTime = null;
            double maxPressureDrop = 0;

            if (forecasts == null || forecasts.isEmpty())
                return migraineResults;

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
                        maxPressureDrop = Math.max(maxPressureDrop, pressureDrop);
                        pressureDropDetected = true;
                    }
                }

                if (pressureDropDetected && migraineStartTime == null) {
                    migraineStartTime = LocalDateTime.parse(currentForecast.getTime());
                } else if (!pressureDropDetected && migraineStartTime != null) {
                    LocalDateTime migraineEndTime = LocalDateTime.parse(currentForecast.getTime());

                    DecimalFormat df = new DecimalFormat("#.##"); // round to two decimal points
                    migraineResults.add(new Result(migraineStartTime, migraineEndTime,
                            Double.parseDouble(df.format(maxPressureDrop))));
                    migraineStartTime = null;
                    maxPressureDrop = 0;
                }
            }

            return migraineResults;
        });
    }
}

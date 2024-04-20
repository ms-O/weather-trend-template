package edu.studio.weather.Services.Abstraction;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface IForecastAnalyzerService {
    CompletableFuture<Map<LocalDateTime, LocalDateTime>> analyzeForecasts();
}

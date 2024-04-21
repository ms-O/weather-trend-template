package edu.studio.weather.Services.Abstraction;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import edu.studio.weather.Models.Result;

public interface IForecastAnalyzerService {
    CompletableFuture<List<Result>> analyzeForecasts();
}

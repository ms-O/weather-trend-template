package edu.studio.weather.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.studio.weather.Models.Forecast;
import edu.studio.weather.Models.Result;
import edu.studio.weather.Services.Abstraction.IWeatherRetrieverService;
import edu.studio.weather.Services.Implementation.ForecastAnalyzerService;

public class ForecastAnalyzerServiceTest {
    @Mock
    private IWeatherRetrieverService weatherRetrieverServiceMock;
    private ForecastAnalyzerService forecastAnalyzerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        forecastAnalyzerService = new ForecastAnalyzerService(weatherRetrieverServiceMock);
    }

    @Test
    public void testAnalyzeForecastsWithNullForecasts() throws InterruptedException, ExecutionException {
        Mockito.when(weatherRetrieverServiceMock.retrieveWeatherAsync())
                .thenReturn(CompletableFuture.completedFuture(null));

        // Test
        CompletableFuture<List<Result>> result = forecastAnalyzerService.analyzeForecasts();

        // Verify
        assertTrue(result.get().isEmpty());
    }

    @Test
    public void testAnalyzeForecastsWithEmptyForecasts() throws InterruptedException, ExecutionException {
        Mockito.when(weatherRetrieverServiceMock.retrieveWeatherAsync())
                .thenReturn(CompletableFuture.completedFuture(List.of()));

        // Test
        CompletableFuture<List<Result>> result = forecastAnalyzerService.analyzeForecasts();

        // Verify
        assertTrue(result.get().isEmpty());
    }

    @Test
    public void testAnalyzeForecastsWithNoMigraineEpisodes() {
        List<Forecast> forecasts = List.of(
                new Forecast("2024-04-01T12:00:00", 1013.25),
                new Forecast("2024-04-01T18:00:00", 1013.00),
                new Forecast("2024-04-02T00:00:00", 1012.75),
                new Forecast("2024-04-02T06:00:00", 1012.50));
        CompletableFuture<List<Forecast>> future = CompletableFuture.completedFuture(forecasts);
        Mockito.when(weatherRetrieverServiceMock.retrieveWeatherAsync()).thenReturn(future);

        // Test
        CompletableFuture<List<Result>> resultFuture = forecastAnalyzerService.analyzeForecasts();
        List<Result> result = resultFuture.join();

        // Verify
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnalyzeForecastsWithOneMigraneEpisode() throws InterruptedException, ExecutionException {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(1);

        Forecast forecast1 = new Forecast(startTime.toString(), 10.0);
        Forecast forecast2 = new Forecast(endTime.toString(), 5.0);

        Mockito.when(weatherRetrieverServiceMock.retrieveWeatherAsync())
                .thenReturn(CompletableFuture.completedFuture(List.of(forecast1, forecast2)));

        // Test
        CompletableFuture<List<Result>> result = forecastAnalyzerService.analyzeForecasts();

        // Verify
        List<Result> migraineEpisodes = result.join();
        assertEquals(1, migraineEpisodes.size());
        Result expectedResult = new Result(startTime, endTime, 5.0);
        assertTrue(expectedResult.equals(migraineEpisodes.get(0)));
    }

    @Test
    public void testAnalyzeForecastsWithOneForecast() throws InterruptedException, ExecutionException {
        LocalDateTime startTime = LocalDateTime.now();

        Forecast forecast1 = new Forecast(startTime.toString(), 10.0);

        Mockito.when(weatherRetrieverServiceMock.retrieveWeatherAsync())
                .thenReturn(CompletableFuture.completedFuture(List.of(forecast1)));

        // Test
        CompletableFuture<List<Result>> result = forecastAnalyzerService.analyzeForecasts();

        // Verify
        List<Result> migraineEpisodes = result.get();
        assertEquals(0, migraineEpisodes.size());
    }

    @Test
    public void testAnalyzeForecastsWithOneMigraineEpisode() {
        List<Forecast> forecasts = List.of(
                new Forecast("2024-04-01T12:00:00", 1015.00), // potential migraine episode start as pressures falling
                new Forecast("2024-04-01T13:00:00", 1014.75),
                new Forecast("2024-04-01T14:00:00", 1014.50),
                new Forecast("2024-04-01T15:00:00", 1009.00), // potential migraine episode ends as pressure increases
                new Forecast("2024-04-01T16:00:00", 1010.00),
                new Forecast("2024-04-01T17:00:00", 1010.25),
                new Forecast("2024-04-01T18:00:00", 1010.50),
                new Forecast("2024-04-01T19:00:00", 1010.75),
                new Forecast("2024-04-01T20:00:00", 1011.00),
                new Forecast("2024-04-01T21:00:00", 1011.25),
                new Forecast("2024-04-01T22:00:00", 1012.25),
                new Forecast("2024-04-01T23:00:00", 1012.35));
        CompletableFuture<List<Forecast>> future = CompletableFuture.completedFuture(forecasts);
        Mockito.when(weatherRetrieverServiceMock.retrieveWeatherAsync()).thenReturn(future);

        // Test
        CompletableFuture<List<Result>> resultFuture = forecastAnalyzerService.analyzeForecasts();
        List<Result> migraineEpisodes = resultFuture.join();

        // Verify
        assertEquals(1, migraineEpisodes.size());
        LocalDateTime expectedStart = LocalDateTime.parse("2024-04-01T12:00:00");
        LocalDateTime expectedEnd = LocalDateTime.parse("2024-04-01T15:00:00");
        Result expectedResult = new Result(expectedStart, expectedEnd, 6.0);
        assertTrue(expectedResult.equals(migraineEpisodes.get(0)));
    }

    @Test
    public void testAnalyzeForecastsWithTwoMigraineEpisodes() {
        List<Forecast> forecasts = List.of(
                new Forecast("2024-04-01T12:00:00", 1015.00), // potential migraine episode start as pressures falling
                new Forecast("2024-04-01T13:00:00", 1014.75),
                new Forecast("2024-04-01T14:00:00", 1014.50),
                new Forecast("2024-04-01T15:00:00", 1009.00), // potential migraine episode ends as pressure increases
                new Forecast("2024-04-01T16:00:00", 1010.00),
                new Forecast("2024-04-01T17:00:00", 1010.25),
                new Forecast("2024-04-01T18:00:00", 1010.50),
                new Forecast("2024-04-01T19:00:00", 1010.75),
                new Forecast("2024-04-01T20:00:00", 1011.00),
                new Forecast("2024-04-01T21:00:00", 1018.75), // potential migraine episode start as pressures falling
                new Forecast("2024-04-01T22:00:00", 1013.45),
                new Forecast("2024-04-01T23:00:00", 1008.25));// potential migraine episode ends as List ends
        CompletableFuture<List<Forecast>> future = CompletableFuture.completedFuture(forecasts);
        Mockito.when(weatherRetrieverServiceMock.retrieveWeatherAsync()).thenReturn(future);

        // Test
        CompletableFuture<List<Result>> resultFuture = forecastAnalyzerService.analyzeForecasts();
        List<Result> migraineEpisodes = resultFuture.join();

        // Verify
        assertEquals(2, migraineEpisodes.size());

        LocalDateTime expectedStart1 = LocalDateTime.parse("2024-04-01T12:00:00");
        LocalDateTime expectedEnd1 = LocalDateTime.parse("2024-04-01T15:00:00");
        Result expectedResult1 = new Result(expectedStart1, expectedEnd1, 6.0);
        assertTrue(expectedResult1.equals(migraineEpisodes.get(0)));

        LocalDateTime expectedStart2 = LocalDateTime.parse("2024-04-01T21:00:00");
        LocalDateTime expectedEnd2 = LocalDateTime.parse("2024-04-01T23:00:00");
        Result expectedResult2 = new Result(expectedStart2, expectedEnd2, 10.5);
        assertTrue(expectedResult2.equals(migraineEpisodes.get(1)));
    }
}

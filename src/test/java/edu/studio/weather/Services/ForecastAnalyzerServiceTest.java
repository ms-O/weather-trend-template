package edu.studio.weather.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.studio.weather.Models.Forecast;
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
        CompletableFuture<Map<LocalDateTime, LocalDateTime>> result = forecastAnalyzerService.analyzeForecasts();

        // Verify
        assertTrue(result.get().isEmpty());
    }

    @Test
    public void testAnalyzeForecastsWithEmptyForecasts() throws InterruptedException, ExecutionException {
        Mockito.when(weatherRetrieverServiceMock.retrieveWeatherAsync())
                .thenReturn(CompletableFuture.completedFuture(List.of()));

        // Test
        CompletableFuture<Map<LocalDateTime, LocalDateTime>> result = forecastAnalyzerService.analyzeForecasts();

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
        CompletableFuture<Map<LocalDateTime, LocalDateTime>> resultFuture = forecastAnalyzerService.analyzeForecasts();
        Map<LocalDateTime, LocalDateTime> result = resultFuture.join();

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
        CompletableFuture<Map<LocalDateTime, LocalDateTime>> result = forecastAnalyzerService.analyzeForecasts();

        // Verify
        Map<LocalDateTime, LocalDateTime> migraineEpisodes = result.get();
        assertEquals(1, migraineEpisodes.size());
        assertTrue(migraineEpisodes.containsKey(startTime));
        assertEquals(endTime, migraineEpisodes.get(startTime));
    }

    @Test
    public void testAnalyzeForecastsWithOneForecast() throws InterruptedException, ExecutionException {
        LocalDateTime startTime = LocalDateTime.now();

        Forecast forecast1 = new Forecast(startTime.toString(), 10.0);

        Mockito.when(weatherRetrieverServiceMock.retrieveWeatherAsync())
                .thenReturn(CompletableFuture.completedFuture(List.of(forecast1)));

        // Test
        CompletableFuture<Map<LocalDateTime, LocalDateTime>> result = forecastAnalyzerService.analyzeForecasts();

        // Verify
        Map<LocalDateTime, LocalDateTime> migraineEpisodes = result.get();
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

        // Test analyzeForecasts method
        CompletableFuture<Map<LocalDateTime, LocalDateTime>> resultFuture = forecastAnalyzerService.analyzeForecasts();
        Map<LocalDateTime, LocalDateTime> migraineEpisodes = resultFuture.join();

        // Verify migraine episodes detected
        assertEquals(1, migraineEpisodes.size());
        LocalDateTime expectedStart = LocalDateTime.parse("2024-04-01T12:00:00");
        LocalDateTime expectedEnd = LocalDateTime.parse("2024-04-01T15:00:00");
        assertTrue(migraineEpisodes.containsKey(expectedStart));
        assertEquals(expectedEnd, migraineEpisodes.get(expectedStart));
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
                new Forecast("2024-04-01T21:00:00", 1018.75),
                new Forecast("2024-04-01T22:00:00", 1013.45),
                new Forecast("2024-04-01T23:00:00", 1008.35));
        CompletableFuture<List<Forecast>> future = CompletableFuture.completedFuture(forecasts);
        Mockito.when(weatherRetrieverServiceMock.retrieveWeatherAsync()).thenReturn(future);

        // Test analyzeForecasts method
        CompletableFuture<Map<LocalDateTime, LocalDateTime>> resultFuture = forecastAnalyzerService.analyzeForecasts();
        Map<LocalDateTime, LocalDateTime> migraineEpisodes = resultFuture.join();

        // Verify migraine episodes detected
        assertEquals(2, migraineEpisodes.size());

        LocalDateTime expectedStart1 = LocalDateTime.parse("2024-04-01T12:00:00");
        LocalDateTime expectedEnd1 = LocalDateTime.parse("2024-04-01T15:00:00");
        assertTrue(migraineEpisodes.containsKey(expectedStart1));
        assertEquals(expectedEnd1, migraineEpisodes.get(expectedStart1));

        LocalDateTime expectedStart2 = LocalDateTime.parse("2024-04-01T21:00:00");
        LocalDateTime expectedEnd2 = LocalDateTime.parse("2024-04-01T23:00:00");
        assertTrue(migraineEpisodes.containsKey(expectedStart2));
        assertEquals(expectedEnd2, migraineEpisodes.get(expectedStart2));
    }
}

package edu.studio.weather.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.studio.weather.External.Abstraction.IOpenMateoApiClient;
import edu.studio.weather.Models.Forecast;
import edu.studio.weather.Services.Implementation.WeatherRetrieverService;
import kong.unirest.JsonNode;

public class WeatherRetrieverServiceTest {

    @Test
    public void testRetrieveWeatherAsync() {
        IOpenMateoApiClient mockApiClient = Mockito.mock(IOpenMateoApiClient.class);

        // Stub the mock to return the sample JSON response
        String jsonResponse = "{\"hourly\": {\"time\": [\"2024-04-01T00:00:00\", \"2024-04-01T01:00:00\"], \"pressure_msl\": [1013.25, 1013.50]}}";
        CompletableFuture<JsonNode> responseFuture = CompletableFuture.completedFuture(new JsonNode(jsonResponse));

        when(mockApiClient.GetWeatherJsonAsync()).thenReturn(responseFuture);

        WeatherRetrieverService service = new WeatherRetrieverService(mockApiClient);

        CompletableFuture<List<Forecast>> weatherFuture = service.retrieveWeatherAsync();
        List<Forecast> forecasts = weatherFuture.join();

        assertEquals(2, forecasts.size()); // Assuming 2 forecasts in the sample response
        assertEquals("2024-04-01T00:00:00", forecasts.get(0).getTime());
        assertEquals(1013.25, forecasts.get(0).getPressure());
        assertEquals("2024-04-01T01:00:00", forecasts.get(1).getTime());
        assertEquals(1013.50, forecasts.get(1).getPressure());
    }

    @Test
    public void testRetrieveWeatherAsyncWithEmptyResponse() {
        IOpenMateoApiClient mockApiClient = Mockito.mock(IOpenMateoApiClient.class);

        CompletableFuture<JsonNode> responseFuture = CompletableFuture.completedFuture(new JsonNode("{}"));
        when(mockApiClient.GetWeatherJsonAsync()).thenReturn(responseFuture);

        WeatherRetrieverService service = new WeatherRetrieverService(mockApiClient);

        CompletableFuture<List<Forecast>> weatherFuture = service.retrieveWeatherAsync();
        List<Forecast> forecasts = weatherFuture.join();

        assertTrue(forecasts.isEmpty());
    }

    @Test
    public void testRetrieveWeatherAsyncWithNullResponse() {
        IOpenMateoApiClient mockApiClient = Mockito.mock(IOpenMateoApiClient.class);

        CompletableFuture<JsonNode> responseFuture = CompletableFuture.completedFuture(null);
        when(mockApiClient.GetWeatherJsonAsync()).thenReturn(responseFuture);

        WeatherRetrieverService service = new WeatherRetrieverService(mockApiClient);

        CompletableFuture<List<Forecast>> weatherFuture = service.retrieveWeatherAsync();
        List<Forecast> forecasts = weatherFuture.join();

        assertTrue(forecasts.isEmpty());
    }

    @Test
    public void testRetrieveWeatherAsyncWithNullTime() {
        IOpenMateoApiClient mockApiClient = Mockito.mock(IOpenMateoApiClient.class);

        String jsonResponse = "{\"hourly\": {\"time\": null, \"pressure_msl\": [1013.25]}}";

        CompletableFuture<JsonNode> responseFuture = CompletableFuture.completedFuture(new JsonNode(jsonResponse));
        when(mockApiClient.GetWeatherJsonAsync()).thenReturn(responseFuture);

        WeatherRetrieverService service = new WeatherRetrieverService(mockApiClient);

        CompletableFuture<List<Forecast>> weatherFuture = service.retrieveWeatherAsync();
        List<Forecast> forecasts = weatherFuture.join();

        assertTrue(forecasts.isEmpty());
    }

    @Test
    public void testRetrieveWeatherAsyncWithNullPressure() {
        IOpenMateoApiClient mockApiClient = Mockito.mock(IOpenMateoApiClient.class);

        String jsonResponse = "{\"hourly\": {\"time\": [\"2024-04-01T00:00:00\"], \"pressure_msl\": null}}";

        CompletableFuture<JsonNode> responseFuture = CompletableFuture.completedFuture(new JsonNode(jsonResponse));
        when(mockApiClient.GetWeatherJsonAsync()).thenReturn(responseFuture);

        WeatherRetrieverService service = new WeatherRetrieverService(mockApiClient);

        CompletableFuture<List<Forecast>> weatherFuture = service.retrieveWeatherAsync();
        List<Forecast> forecasts = weatherFuture.join();

        assertTrue(forecasts.isEmpty());
    }

    @Test
    public void testRetrieveWeatherAsyncWithErrorResponse() {
        IOpenMateoApiClient mockApiClient = Mockito.mock(IOpenMateoApiClient.class);

        CompletableFuture<JsonNode> responseFuture = CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("Error fetching weather data");
        });
        when(mockApiClient.GetWeatherJsonAsync()).thenReturn(responseFuture);

        WeatherRetrieverService service = new WeatherRetrieverService(mockApiClient);

        CompletableFuture<List<Forecast>> weatherFuture = service.retrieveWeatherAsync();
        List<Forecast> forecasts = weatherFuture.join();

        assertTrue(forecasts.isEmpty());
    }
}

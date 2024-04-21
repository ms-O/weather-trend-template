package edu.studio.weather.External;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.studio.weather.Config.AppConfig;
import edu.studio.weather.External.Implementation.OpenMateoApiClient;
import edu.studio.weather.Mocks.OpenMateoApiClientMock;
import kong.unirest.JsonNode;
import kong.unirest.UnirestException;
import kong.unirest.json.JSONObject;

public class OpenMateoApiClientTest {
    private OpenMateoApiClientMock openMateoApiClientMock;

    @BeforeEach
    public void setUp() {
        openMateoApiClientMock = new OpenMateoApiClientMock();
    }

    @Test
    public void testGetWeatherJsonAsync() {
        String expectedHourlyJson = AppConfig.WEATHER_MOCK_EXPECTED_RESULT;
        JsonNode jsonNodeStub = Mockito.mock(JsonNode.class);
        Mockito.when(jsonNodeStub.toString()).thenReturn(expectedHourlyJson);

        CompletableFuture<JsonNode> stubFuture = CompletableFuture.completedFuture(jsonNodeStub);
        OpenMateoApiClient openMateoApiClientStub = Mockito.mock(OpenMateoApiClient.class);
        Mockito.when(openMateoApiClientStub.GetWeatherJsonAsync()).thenReturn(stubFuture);

        // Test
        CompletableFuture<JsonNode> mockResultFuture = openMateoApiClientMock.GetWeatherJsonAsync();
        CompletableFuture<JsonNode> stubResultFuture = openMateoApiClientStub.GetWeatherJsonAsync();

        // MockResultFuture
        JsonNode mockResultJson = mockResultFuture.join();
        JSONObject mockOuterJsonObject = mockResultJson.getObject();
        JSONObject mockHourlyValues = mockOuterJsonObject.getJSONObject("hourly");

        // stubResultFuture
        JsonNode stubHourlyValues = stubResultFuture.join();

        // Assert
        assertEquals(mockHourlyValues.toString(), stubHourlyValues.toString());
    }

    @Test
    public void testGetWeatherJsonAsyncWithException() {
        OpenMateoApiClientMock openMateoApiClientMock = new OpenMateoApiClientMock();
        UnirestException exception = Mockito.mock(UnirestException.class);
        // Test
        CompletableFuture<JsonNode> future = openMateoApiClientMock.GetWeatherJsonAsync();

        // Assert
        future.exceptionally(ex -> {
            assertEquals(exception, ex.getCause());
            return null;
        });
    }
}

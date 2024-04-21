package edu.studio.weather.Models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ForecastDataTest {

    @Test
    public void testForecastDataGettersAndSetters() {
        List<String> time = Arrays.asList("2024-04-19T12:00:00", "2024-04-19T13:00:00");
        List<Double> pressureMsl = Arrays.asList(1013.25, 1013.50);

        // Test
        ForecastData forecastData = new ForecastData();
        forecastData.setTime(time);
        forecastData.setPressureMsl(pressureMsl);

        // Assert
        assertEquals(time, forecastData.getTime());
        assertEquals(pressureMsl, forecastData.getPressureMsl());
    }

    @Test
    void testNullability() {
        ForecastData forecastData = new ForecastData();

        // Test
        forecastData.setTime(null);
        forecastData.setPressureMsl(null);

        // Assert
        assertNull(forecastData.getTime());
        assertNull(forecastData.getPressureMsl());
    }

    @Test
    void testEmptyList() {
        ForecastData forecastData = new ForecastData();

        // Test
        forecastData.setTime(Collections.emptyList());
        forecastData.setPressureMsl(Collections.emptyList());

        // Assert
        assertTrue(forecastData.getTime().isEmpty());
        assertTrue(forecastData.getPressureMsl().isEmpty());
    }
}

package edu.studio.weather.Models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class ForecastTest {

    @Test
    public void testForecastConstructorAndGetters() {
        String time = "2024-04-19T12:00:00";
        double pressure = 1013.25;

        Forecast forecast = new Forecast(time, pressure);

        assertEquals(time, forecast.getTime());
        assertEquals(pressure, forecast.getPressure());
    }

    @Test
    public void testForecastSetters() {
        Forecast forecast = new Forecast("2024-04-19T12:00:00", 1013.25);
        String newTime = "2024-04-19T13:00:00";
        double newPressure = 1013.50;

        forecast.setTime(newTime);
        forecast.setPressure(newPressure);

        assertEquals(newTime, forecast.getTime());
        assertEquals(newPressure, forecast.getPressure());
    }

    @Test
    public void testForecastToString() {
        Forecast forecast = new Forecast("2024-04-19T12:00:00", 1013.25);
        String expectedString = "Forecast [time=2024-04-19T12:00:00, pressure=1013.25]";

        String resultString = forecast.toString();

        assertEquals(expectedString, resultString);
    }

    @Test
    void testNullability() {
        Forecast forecast = new Forecast("2024-04-19T12:00", 1000.5);
        forecast.setTime(null);
        assertNull(forecast.getTime());
    }

    @Test
    void testNegativePressure() {
        Forecast forecast = new Forecast("2024-04-19T12:00", -100.0);
        assertEquals(-100.0, forecast.getPressure());
    }

    @Test
    void testEdgeCase() {
        Forecast forecast = new Forecast("2024-04-19T12:00", Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, forecast.getPressure());
    }
}

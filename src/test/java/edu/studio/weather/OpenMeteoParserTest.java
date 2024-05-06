package edu.studio.weather;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class OpenMeteoParserTest {
        @Test
        public void testParseData() throws IOException {
            String filePath = "src/test/resources/open-meteo-pressure-trend-response.json";
            String weatherData = new String(Files.readAllBytes(Paths.get(filePath)));

            List<Forecast> expectedForecasts = List.of(
                new Forecast("2024-04-01T00:00", 1015.2),
                new Forecast("2024-04-01T01:00", 1015.3)
            );
            
            OpenMeteoParser parser = new OpenMeteoParser();
            List<Forecast> actualForecasts = parser.parseData(weatherData);

            assertEquals(expectedForecasts.get(0).getTime(), actualForecasts.get(0).getTime());
            assertEquals(expectedForecasts.get(0).getPressure(), actualForecasts.get(0).getPressure(), 0.001);
            assertEquals(expectedForecasts.get(1).getTime(), actualForecasts.get(1).getTime());
            assertEquals(expectedForecasts.get(1).getPressure(), actualForecasts.get(1).getPressure(), 0.001);
        }
        
        @Test
        public void testParseData2() {
            String weatherData = "{\"hourly\":{\"time\":[\"2024-04-16T00:00\",\"2024-04-16T01:00\"],\"pressure_msl\":[1014.6,1016.3]}}";
            List<Forecast> expectedForecasts = new ArrayList<>();
            expectedForecasts.add(new Forecast("2024-04-16T00:00", 1014.6));
            expectedForecasts.add(new Forecast("2024-04-16T01:00", 1016.3));
            OpenMeteoParser parser = new OpenMeteoParser();
            List<Forecast> actualForecasts = parser.parseData(weatherData);
            assertEquals(expectedForecasts.size(), actualForecasts.size());
            for (int i = 0; i < expectedForecasts.size(); i++) {
                Forecast expected = expectedForecasts.get(i);
                Forecast actual = actualForecasts.get(i);
                assertEquals(expected.getTime(), actual.getTime());
                assertEquals(expected.getPressure(), actual.getPressure(), 0.001);
            }
        } 
    
}



